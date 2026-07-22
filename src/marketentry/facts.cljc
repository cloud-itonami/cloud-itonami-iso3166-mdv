(ns marketentry.facts
  "Maldives (MDV) market-entry catalog.

  Every fact under \"MDV\" below is grounded ONLY in a verified research
  dossier gathered by web search against `.gov.mv`/`.egov.mv` government
  domains. No regulatory claim, requirement, or institutional detail
  beyond that dossier is stated here -- the same discipline
  `marketentry.governor`'s `spec-basis-violations` enforces
  downstream.

    - Procurement regulator: the Ministry of Finance (and Public
      Enterprises) -- formulates and administers public finance /
      procurement policy. Official legislation page:
      https://www.finance.gov.mv/public-finance/legislation
    - Procurement legal basis: the Public Finance Regulation, issued
      by the Ministry of Finance -- sets the processes, procedures and
      requirements state institutions must adhere to regarding public
      finances, including procurement.
      https://www.finance.gov.mv/media/public-finance-regulations
    - Business/commercial registration: governed by the Business
      Registration Act -- Law No. 18/2014 (18/2014 ވިޔަފާރި
      ރަޖިސްޓަރީކުރުމުގެ ޤާނޫނު). Official English-language text:
      https://business.egov.mv/Downloads/LawsAndRegulation/english-law-no-18-2014-business-registration-act.pdf
    - Tax administration: the Maldives Inland Revenue Authority (MIRA)
      is responsible for tax administration and implementation of
      taxation policy (the Ministry of Finance and Treasury formulates
      tax policy; MIRA administers it).

  What this catalog deliberately does NOT claim: no national
  e-procurement PORTAL was independently confirmed for the Maldives --
  `:national-spec` says so honestly rather than inventing one (contrast
  Angola's SNCP e-procurement system or Mauritius's PPO e-Procurement
  System). Secondary sources discuss a policy proposal to reserve
  government tenders below a specified value threshold exclusively for
  SMEs, and provisions permitting single-source (non-competitive)
  procurement under certain welfare/essential-service conditions --
  neither is confirmed as settled, cited legal text with a specific
  threshold number, so NEITHER is encoded here as a fact, a
  required-evidence item, or a governor check; do not add an
  SME-threshold number without a citable primary source. No dedicated
  MIRA URL was captured in this dossier (only its role, from the
  Ministry of Finance's own description of the tax-policy/
  administration split) -- `:corporate-number-provenance` below states
  that honestly rather than inventing a mira.gov.mv page."
  )

(def catalog
  {"MDV" {:name "Maldives"
          :owner-authority "Ministry of Finance (and Public Enterprises)"
          :legal-basis "Public Finance Regulation (issued by the Ministry of Finance) -- sets the processes, procedures and requirements state institutions must adhere to regarding public finances, including procurement"
          :national-spec "Public Finance Regulation procurement filing (no verified national e-procurement portal)"
          :provenance "https://www.finance.gov.mv/public-finance/legislation"
          :required-evidence ["Business Registration Act (Law No. 18/2014) registration record"
                              "MIRA tax registration record"]
          :rep-owner-authority "Business Registration Act administration (Law No. 18/2014)"
          :rep-legal-basis "Business Registration Act -- Law No. 18/2014 (18/2014 ވިޔަފާރި ރަޖިސްޓަރީކުރުމުގެ ޤާނޫނު) governs business/commercial registration in the Maldives"
          :rep-provenance "https://business.egov.mv/Downloads/LawsAndRegulation/english-law-no-18-2014-business-registration-act.pdf"
          :corporate-number-owner-authority "Maldives Inland Revenue Authority (MIRA)"
          :corporate-number-legal-basis "MIRA is responsible for tax administration and implementation of taxation policy; the Ministry of Finance and Treasury formulates tax policy, MIRA administers it"
          :corporate-number-provenance "Ministry of Finance (and Public Enterprises) — no dedicated MIRA URL captured in this actor's verified research dossier"}
   ;; -- reference jurisdiction, reused verbatim from already-merged
   ;; sibling repos (cloud-itonami-iso3166-ago et al.), not a new claim --
   "USA" {:name "United States" :owner-authority "GSA/SAM.gov" :legal-basis "FAR" :national-spec "SAM.gov" :provenance "https://sam.gov/"
          :required-evidence ["EIN record" "SAM.gov registration record" "State business registration record" "SAM UEI verification record"]}})

(defn spec-basis [iso3] (get catalog iso3))
(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s) missing (remove catalog iso3s)]
     {:requested (count iso3s) :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note "R0 catalog seed"})))
(defn required-evidence-satisfied? [iso3 submitted]
  (when-let [{:keys [required-evidence]} (spec-basis iso3)]
    (= (count required-evidence) (count (filter (set submitted) required-evidence)))))
(defn evidence-checklist [iso3] (:required-evidence (spec-basis iso3) []))
(defn rep-spec-basis [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:rep-owner-authority sb)
      (select-keys sb [:rep-owner-authority :rep-legal-basis :rep-provenance]))))
(defn corporate-number-spec-basis [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:corporate-number-owner-authority sb)
      (select-keys sb [:corporate-number-owner-authority :corporate-number-legal-basis :corporate-number-provenance]))))
