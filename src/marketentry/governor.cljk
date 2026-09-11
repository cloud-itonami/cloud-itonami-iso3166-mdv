(ns marketentry.governor
  "Market-Entry Compliance Governor -- the independent compliance layer
  that earns the MarketEntry-LLM the right to commit. The LLM has no
  notion of Maldivian procurement law, whether a claimed Business
  Registration Act registration is actually on file, whether a claimed
  MIRA tax registration has actually been verified, whether a claimed
  engagement fee actually equals base + months x rate, or when a draft
  stops being a draft and becomes a real-world filing submission, so
  this MUST be a separate system able to *reject* a proposal and fall
  back to HOLD.

  `:itonami.blueprint/governor` is `:market-entry-compliance-governor`
  (shared family keyword on blueprints; this is the MDV-family
  implementation of that governor, adapted from the AGO reference
  implementation in `cloud-itonami-iso3166-ago`).

  This blueprint's own text (docs/business-model.md Trust Controls:
  'any actual filing draft or filing submission requires Market-Entry
  Compliance Governor clearance and always escalates to human sign-off';
  'a false or fabricated regulatory-requirement claim is a HARD hold')
  names exactly the checks below.

  SEVEN checks, in priority order, ALL HARD violations: a human
  approver CANNOT override them. The confidence/actuation gate is
  SOFT: it asks a human to look (low confidence / actuation), and the
  human may approve -- but see `marketentry.phase`: for `:stake
  :actuation/draft-filing`/`:actuation/submit-filing` NO phase ever
  allows auto-commit either. Two independent layers agree that
  actuation is always a human call.

  This is the AGO reference's SEVEN-check shape, not the thinner
  SIX-check shape sibling actors with a single registration fact use
  (`cloud-itonami-iso3166-stp`/`-sdn`): the Maldives dossier
  (`marketentry.facts`) grounds TWO independently-checkable
  registration facts -- Business Registration Act (Law No. 18/2014)
  registration, and MIRA tax registration -- so BOTH get their own
  governor check, exactly as AGO's dossier grounds both an entity
  registration fact (NIF/Registo Comercial-adjacent resident-entity
  requirement) and a distinct NIF tax check.

    1. Spec-basis                       -- did the jurisdiction proposal
                                            cite an OFFICIAL source
                                            (`marketentry.facts`), or
                                            invent one?
    2. Evidence incomplete              -- for `:filing/draft`/
                                            `:filing/submit`, has the
                                            jurisdiction actually been
                                            assessed with a full evidence
                                            checklist on file?
    3. Business-registration missing    -- for `:filing/submit`, when
                                            the engagement declares
                                            `:requires-mdv-registration?
                                            true`, INDEPENDENTLY verify
                                            `:has-mdv-registration?` is
                                            true. FLAGSHIP check for this
                                            vertical, grounded in the
                                            Business Registration Act --
                                            Law No. 18/2014.
    4. Engagement fee mismatch          -- for `:filing/submit`,
                                            INDEPENDENTLY recompute
                                            whether the engagement's own
                                            `:claimed-fee` equals
                                            `base-fee + monthly-rate x
                                            monitoring-months` -- honest
                                            reapplication of the
                                            ground-truth-recompute
                                            discipline sibling actors
                                            use.
    5. MIRA registration unverified     -- for `:filing/submit`, when
                                            the engagement declares
                                            `:requires-mira-registration?
                                            true`, INDEPENDENTLY check
                                            `:mira-registration-verified?`.
                                            CONDITIONAL on the
                                            engagement's own ground
                                            truth. Grounded in MIRA
                                            (Maldives Inland Revenue
                                            Authority) being responsible
                                            for tax administration.
    6. Confidence floor / actuation
       gate                             -- LLM confidence below
                                            threshold, OR the op is
                                            `:filing/draft`/
                                            `:filing/submit` (REAL acts)
                                            -> escalate. (SOFT -- see
                                            above.)

  Two more guards, double-draft/double-submit prevention, are enforced
  off dedicated `:drafted?`/`:submitted?` facts (never a `:status`
  value) -- these plus items 1-5 above are the SEVEN HARD violation
  functions `check` actually concatenates."
  (:require [marketentry.facts :as facts]
            [marketentry.registry :as registry]
            [marketentry.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Stakes grave enough to always require a human, even when clean.
  Drafting a real filing package and submitting a real portal/filing
  registration are the two real-world actuation events this actor
  performs."
  #{:actuation/draft-filing :actuation/submit-filing})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A `:jurisdiction/assess` (or `:filing/draft`/`:filing/submit`)
  proposal with no spec-basis citation is a HARD violation -- never
  invent a jurisdiction's market-entry requirements."
  [{:keys [op]} proposal]
  (when (contains? #{:jurisdiction/assess :filing/draft :filing/submit} op)
    (let [value (:value proposal)]
      (when (or (empty? (:cites proposal))
                (and (contains? value :spec-basis) (nil? (:spec-basis value))))
        [{:rule :no-spec-basis
          :detail "公式spec-basisの引用が無い提案は法域要件として扱えない"}]))))

(defn- evidence-incomplete-violations
  "For `:filing/draft`/`:filing/submit`, the jurisdiction's required
  registration evidence must actually be satisfied."
  [{:keys [op subject]} st]
  (when (contains? #{:filing/draft :filing/submit} op)
    (let [e (store/engagement st subject)
          assessment (store/assessment-of st subject)]
      (when-not (and assessment
                     (facts/required-evidence-satisfied?
                      (:jurisdiction e) (:checklist assessment)))
        [{:rule :evidence-incomplete
          :detail "法域の必要書類(Business Registration Act登記/MIRA納税登録等)が充足していない状態での提案"}]))))

(defn- mdv-registration-missing-violations
  "For `:filing/submit`, when the engagement declares
  `:requires-mdv-registration? true`, INDEPENDENTLY verify
  `:has-mdv-registration?` is true -- the flagship genuinely new
  check this vertical adds, grounded in the Business Registration Act
  (Law No. 18/2014). CONDITIONAL on the engagement's own
  `:requires-mdv-registration?` ground truth."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-mdv-registration? e))
                 (not (true? (:has-mdv-registration? e))))
        [{:rule :mdv-registration-missing
          :detail (str subject " はBusiness Registration Act(Law No. 18/2014)に基づく登記を要するが未確認 -- 提出提案は進められない")}]))))

(defn- engagement-fee-mismatch-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own claimed fee equals base + months x rate."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when-not (registry/engagement-fee-matches-claim? e)
        [{:rule :engagement-fee-mismatch
          :detail (str subject " の申告手数料(" (:claimed-fee e)
                      ")が独立再計算値(" (registry/compute-engagement-fee e) ")と一致しない")}]))))

(defn- mira-registration-unverified-violations
  "For `:filing/submit`, when the engagement declares
  `:requires-mira-registration? true`, INDEPENDENTLY check
  `:mira-registration-verified?` -- CONDITIONAL on the engagement's
  own ground truth. Grounded in the Maldives Inland Revenue Authority
  (MIRA) being responsible for tax administration and implementation
  of taxation policy."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-mira-registration? e))
                 (not (true? (:mira-registration-verified? e))))
        [{:rule :mira-registration-unverified
          :detail (str subject " はMIRA(Maldives Inland Revenue Authority)納税登録の確認を要するが未確認 -- 提出提案は進められない")}]))))

(defn- already-drafted-violations
  "For `:filing/draft`, refuses to draft the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/draft)
    (when (store/engagement-already-drafted? st subject)
      [{:rule :already-drafted
        :detail (str subject " は既にドラフト済み")}])))

(defn- already-submitted-violations
  "For `:filing/submit`, refuses to submit the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (when (store/engagement-already-submitted? st subject)
      [{:rule :already-submitted
        :detail (str subject " は既に提出済み")}])))

(defn check
  "Censors a MarketEntry-LLM proposal against the governor rules.
  Returns {:ok? bool :violations [..] :confidence c :escalate? bool
  :high-stakes? bool :hard? bool}."
  [request _context proposal st]
  (let [hard (into []
                   (concat (spec-basis-violations request proposal)
                           (evidence-incomplete-violations request st)
                           (mdv-registration-missing-violations request st)
                           (engagement-fee-mismatch-violations request st)
                           (mira-registration-unverified-violations request st)
                           (already-drafted-violations request st)
                           (already-submitted-violations request st)))
        conf (:confidence proposal 0.0)
        low? (< conf confidence-floor)
        stakes? (boolean (high-stakes (:stake proposal)))
        hard? (boolean (seq hard))]
    {:ok?          (and (not hard?) (not low?) (not stakes?))
     :violations   hard
     :confidence   conf
     :hard?        hard?
     :escalate?    (and (not hard?) (or low? stakes?))
     :high-stakes? stakes?}))

(defn hold-fact
  "The audit fact written when a proposal is rejected (HOLD)."
  [request context verdict]
  {:t          :governor-hold
   :op         (:op request)
   :actor      (:actor-id context)
   :subject    (:subject request)
   :disposition :hold
   :basis      (mapv :rule (:violations verdict))
   :violations (:violations verdict)
   :confidence (:confidence verdict)})
