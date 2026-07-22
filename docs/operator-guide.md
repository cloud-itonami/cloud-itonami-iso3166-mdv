# Operator guide — MDV

Human-gated filing only. Every `:filing/draft`/`:filing/submit`
proposal always pauses for a human market-entry operator's approval —
there is no rollout phase in which either auto-commits (see
`src/marketentry/phase.cljc`).

## Portal / channel

**Ministry of Finance (and Public Enterprises)** —
`https://www.finance.gov.mv/public-finance/legislation` — formulates
and administers public finance/procurement policy via the **Public
Finance Regulation**. There is no verified evidence of a transactional
national e-procurement portal — this actor tracks registration
evidence and prepares filing records, it does not submit through a
portal API that does not exist.

## Workflow

1. `:engagement/intake` — record the operator, portal, and fee terms
   for a new engagement. May auto-commit at phase 3 once the governor
   is clean (low real-world risk — no filing has happened yet).
2. `:jurisdiction/assess` — request the MDV evidence checklist. ALWAYS
   requires human approval, even when clean (governor `escalate?` is
   forced true for this operation regardless of phase).
3. `:filing/draft` — prepare the internal filing-draft record.
   Requires a completed assessment on file (`evidence-incomplete`
   otherwise). ALWAYS requires human approval.
4. `:filing/submit` — prepare the filing-submit record, the step that
   corresponds to an actual real-world filing action. ALWAYS requires
   human approval, and is independently checked against:
   - `mdv-registration-missing` — is a Business Registration Act (Law
     No. 18/2014) registration actually on file when the engagement
     declares one is required?
   - `engagement-fee-mismatch` — does the claimed fee actually equal
     `base-fee + monthly-rate × monitoring-months`?
   - `mira-registration-unverified` — has the MIRA tax registration
     itself actually been independently verified, when the engagement
     declares verification is required?
   - `already-drafted` / `already-submitted` — refuses to double-file
     the same engagement.

Any HARD violation above is a hold NO approver can override — the
operator must fix the underlying engagement record (verify the
Business Registration Act registration, correct the fee, verify the
MIRA registration) before resubmitting, not approve past the governor.

## Required evidence checklist (per `src/marketentry/facts.cljc`)

- Business Registration Act (Law No. 18/2014) registration record
- MIRA tax registration record

## What this actor does NOT claim

There is no verified national transactional e-procurement portal on
file for the Maldives, and no citable, settled SME-threshold monetary
value — secondary sources discuss such a policy proposal, but it is
not encoded here as a fact or a governor check. If you find a
verifiable primary-source citation for either, extend
`src/marketentry/facts.cljc` — do not hand-edit a claim into this
guide or any other doc without an official source.
