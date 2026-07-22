# cloud-itonami-iso3166-mdv

**`:implemented`** for **MDV** (Maldives). Flagship check
`mdv-registration-missing`, tax check `mira-registration-unverified`.

Independent Public-Sector Market-Entry & Procurement Compliance Service:
a MarketEntry-LLM advisor sealed behind a langgraph-clj `StateGraph`,
censored by a 7-check Market-Entry Compliance Governor, with an
append-only audit ledger and a 0→3 phase rollout gate. See
`docs/business-model.md` for the Trust Controls this actor enforces and
`docs/operator-guide.md` for the human-operator workflow.

```
clojure -M:dev:test   # run the full test suite
clojure -M:lint       # clj-kondo, errors fail
clojure -M:dev:run    # demo driver (marketentry.sim)
```

Regulatory grounding (verified against `.gov.mv`/`.egov.mv` government
domains -- see `src/marketentry/facts.cljc` for full citations):

- **Ministry of Finance (and Public Enterprises)** -- formulates and
  administers public finance/procurement policy.
  `https://www.finance.gov.mv/public-finance/legislation`
- **Public Finance Regulation** -- issued by the Ministry of Finance;
  sets the processes, procedures and requirements state institutions
  must adhere to regarding public finances, including procurement.
- **Business Registration Act** -- Law No. 18/2014, governing
  business/commercial registration in the Maldives.
  `https://business.egov.mv/Downloads/LawsAndRegulation/english-law-no-18-2014-business-registration-act.pdf`
- **MIRA** -- Maldives Inland Revenue Authority, responsible for tax
  administration and implementation of taxation policy (the Ministry
  of Finance and Treasury formulates tax policy; MIRA administers it).

There is no verified evidence of a national transactional
e-procurement portal, and no citable, settled SME-threshold monetary
value -- this actor does not claim either.

AGPL-3.0-or-later.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for the Maldives:

- `src/culture/facts.cljc` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
