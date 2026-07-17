(ns culture.facts
  "Country-level regional-culture catalog for Maldives (MDV) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.

  Maldives is a thinly-documented micro-state; verification during
  Wave 1 research dropped two plausible candidates that could not be
  confirmed on Wikipedia: the Old Friday Mosque under that exact title
  (404 -- worked around by using its canonical title, Malé Friday
  Mosque) and Thundu Kunaa mat weaving, a real and well-attested
  Maldivian craft for which no dedicated Wikipedia article could be
  found (only tourism/culture-blog sources), so it was dropped rather
  than cited off-Wikipedia without a clear provenance match to the
  reference implementation's sourcing discipline.")

(def catalog
  "iso3 -> vector of culture entries."
  {"MDV"
   [{:culture/id "mdv.dish.mas-huni"
     :culture/name "Mas huni"
     :culture/country "MDV"
     :culture/kind :dish
     :culture/summary "Typical Maldivian breakfast dish consisting of tuna, onion, coconut, lime juice, salt and chili, typically eaten with flatbread and tea."
     :culture/url "https://en.wikipedia.org/wiki/Mas_huni"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mdv.dish.garudhiya"
     :culture/name "Garudhiya"
     :culture/country "MDV"
     :culture/kind :dish
     :culture/summary "Clear fish broth, one of the basic and traditional food items of Maldivian cuisine, made primarily with tuna species and typically served with steamed rice or roshi."
     :culture/url "https://en.wikipedia.org/wiki/Garudhiya"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mdv.dish.rihaakuru"
     :culture/name "Rihaakuru"
     :culture/country "MDV"
     :culture/kind :dish
     :culture/summary "Tuna-based thick sauce serving as a traditional condiment in Maldivian cuisine, produced as a byproduct when processing tuna."
     :culture/url "https://en.wikipedia.org/wiki/Rihaakuru"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mdv.product.maldive-fish"
     :culture/name "Maldive fish"
     :culture/country "MDV"
     :culture/kind :product
     :culture/summary "Cured tuna traditionally produced in the Maldives, serving as a staple export across multiple regional cuisines."
     :culture/url "https://en.wikipedia.org/wiki/Maldive_fish"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mdv.craft.boduberu"
     :culture/name "Boduberu"
     :culture/country "MDV"
     :culture/kind :craft
     :culture/summary "Traditional Maldivian performance art of about 20 people, including three drummers and a lead singer, who create music with drums and other instruments while dancing, likely originating from Indian Ocean sailors centuries ago."
     :culture/url "https://en.wikipedia.org/wiki/Boduberu"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "mdv.heritage.male-friday-mosque"
     :culture/name "Malé Friday Mosque"
     :culture/name-local "Hukuru Miskiy"
     :culture/country "MDV"
     :culture/kind :heritage
     :culture/summary "Constructed between 1656-1658, the oldest and most ornate mosque in the Maldives, added to UNESCO's tentative World Heritage cultural list in 2008 for its coral stone architecture and traditional Maldivian craftsmanship."
     :culture/url "https://en.wikipedia.org/wiki/Mal%C3%A9_Friday_Mosque"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-mdv culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "MDV"))
                 " MDV entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
