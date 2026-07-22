# Business model — MDV

Independent Public-Sector Market-Entry & Procurement Compliance Service
for the Republic of Maldives: an assistive actor that helps a market-
entry operator assemble, track, and (with a human's sign-off) file the
registration evidence a foreign operator needs to bid on Maldivian
public tenders.

## Who this serves

Foreign companies (and their local counsel/agents) preparing to bid on
Maldivian public-sector contracts, who need to track:

- **Business Registration Act (Law No. 18/2014) registration** — the
  statute governing business/commercial registration in the Maldives.
- **MIRA tax registration** — the Maldives Inland Revenue Authority is
  responsible for tax administration and implementation of taxation
  policy (the Ministry of Finance and Treasury formulates tax policy;
  MIRA administers it).
- **Public Finance Regulation compliance** — issued by the Ministry of
  Finance (and Public Enterprises), sets the processes, procedures and
  requirements state institutions must adhere to regarding public
  finances, including procurement.

## What this actor does

1. **Engagement intake** — normalize the operator's own case data
   (operator name, engagement fee terms). No new facts invented.
2. **Jurisdiction assessment** — hand back the MDV evidence checklist
   from `src/marketentry/facts.cljc`, always citing an official source
   (`finance.gov.mv`, `business.egov.mv`). A jurisdiction not in the
   catalog gets NO checklist — the actor states plainly that it has no
   official spec-basis rather than guessing.
3. **Filing draft** — prepare the FILING-DRAFT record (an unsigned,
   internal book-of-record entry — not a real government-portal
   submission).
4. **Filing submit** — prepare the FILING-SUBMIT record. This is the
   step that corresponds to an actual real-world filing action, so it
   is the most tightly gated.

## Grounding: what is and is not verified for the Maldives

What is verified (checked against `.gov.mv`/`.egov.mv` government
domains):

- The **Ministry of Finance (and Public Enterprises)** formulates and
  administers public finance/procurement policy.
  `https://www.finance.gov.mv/public-finance/legislation`
- The **Public Finance Regulation**, issued by the Ministry of Finance,
  sets the processes, procedures and requirements state institutions
  must adhere to regarding public finances, including procurement.
  `https://www.finance.gov.mv/media/public-finance-regulations`
- The **Business Registration Act** — Law No. 18/2014 — governs
  business/commercial registration.
  `https://business.egov.mv/Downloads/LawsAndRegulation/english-law-no-18-2014-business-registration-act.pdf`
- **MIRA** (Maldives Inland Revenue Authority) is responsible for tax
  administration and implementation of taxation policy.

What is NOT verified, and is therefore NOT claimed:

- A national transactional e-procurement portal. Unlike, say, Angola's
  SNCP e-procurement system or Mauritius's PPO e-Procurement System, no
  such portal was independently confirmed for the Maldives —
  `:national-spec` in `src/marketentry/facts.cljc` says so honestly.
- A specific SME-threshold monetary value. Secondary sources discuss a
  policy proposal to reserve government tenders below a specified
  value threshold exclusively for SMEs, and provisions permitting
  single-source (non-competitive) procurement under certain
  welfare/essential-service conditions — neither is confirmed as
  settled, cited legal text with a specific number, so **neither is
  encoded here as a fact, a required-evidence item, or a governor
  check.** If you find a verifiable primary-source citation for a
  specific threshold, extend `src/marketentry/facts.cljc` — do not
  hand-edit a number into this document or any other doc without one.
- A dedicated MIRA web page. This dossier established MIRA's role from
  the Ministry of Finance's own description of the tax-policy/
  administration split, not from a directly-fetched MIRA URL —
  `:corporate-number-provenance` states that honestly.

## Trust Controls

- **A false or fabricated regulatory-requirement claim is a HARD
  hold.** Every jurisdiction assessment must cite an official source
  from `marketentry.facts`; an assessment with no citation, or one that
  claims a `:spec-basis` this actor never verified, is rejected outright
  and no human can override it.
- **Any actual filing draft or filing submission requires Market-Entry
  Compliance Governor clearance and always escalates to human
  sign-off.** `:filing/draft` and `:filing/submit` proposals are NEVER
  auto-committed, at any rollout phase — a human market-entry operator
  makes the actual filing decision every time, even when the governor
  finds nothing wrong.
- **Independent re-verification, not trust-on-claim.** The governor
  independently recomputes the claimed engagement fee (`base-fee +
  monthly-rate × monitoring-months`) rather than trusting the claimed
  total, and independently checks the engagement's own
  `:has-mdv-registration?` / `:mira-registration-verified?` facts
  rather than assuming a filing-draft or filing-submit proposal is
  accurate.
- **Missing Business Registration Act registration is an
  unoverridable HARD hold** when the engagement declares it is
  required (`mdv-registration-missing`, this vertical's flagship
  check, grounded in Law No. 18/2014).
- **Unverified MIRA tax registration is an unoverridable HARD hold**
  when the engagement declares it is required
  (`mira-registration-unverified`), grounded in MIRA being responsible
  for tax administration and implementation of taxation policy.
- **No invented national e-procurement portal, and no invented
  SME-threshold number.** See "Grounding" above — this actor states
  plainly what is not confirmed rather than filling the gap with a
  plausible-sounding invention.
- **Double-actuation is structurally prevented**: `:drafted?`/
  `:submitted?` dedicated facts (never a `:status` value) make
  drafting or submitting the same engagement twice an unoverridable
  HARD hold.
- **Append-only audit ledger.** Every commit or hold decision writes
  exactly one immutable ledger fact — there is a complete,
  tamper-evident record of what was proposed, what the governor found,
  and what a human approved or rejected.

## Regulatory sources (all independently verified)

- Ministry of Finance (and Public Enterprises) — legislation page:
  `https://www.finance.gov.mv/public-finance/legislation`
- Public Finance Regulation:
  `https://www.finance.gov.mv/media/public-finance-regulations`
- Business Registration Act, Law No. 18/2014:
  `https://business.egov.mv/Downloads/LawsAndRegulation/english-law-no-18-2014-business-registration-act.pdf`
