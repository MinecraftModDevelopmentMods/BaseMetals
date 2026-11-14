# Reviewed mining levels for Minecraft 1.13.2

This audit covers all 21 Modern Metals and 22 Base Metals materials, plus the
vanilla iron/gold and non-metal tool references. It changes mining capability
and harvest requirements; the other legacy statistics remain separate.

The numbers are an engineering-informed game policy, not measured physical
units or a unique scientific ranking. Tool geometry, purity, heat treatment,
fracture toughness and wear matter. There is no physical conversion from a
Mohs-style scratch-hardness number to a Minecraft harvest level.

## Policy and assumptions

- Tier 0 is the vanilla wood/gold class, tier 1 stone, tier 2 iron and tier 3
  diamond. Tier 4 remains a fictional-material capability. Bare hands do not
  qualify as a pickaxe, even on a tier-0 solid metal block.
- Galvanized steel uses a steel core: galvanizing does not significantly change
  steel's mechanical properties. Give it the same harvest capability as steel
  and vanilla iron, tier 2. This does not claim zinc coating makes tool steel
  harder. [Galvanizing evidence](https://galvanizeit.org/knowledgebase/article/mechanical-properties-of-galvanized-steel)
- Treat named steel, stainless steel, bronze, Invar, Nichrome and titanium as
  useful engineering materials at tier 2. Exact grades are not specified by the
  mod; this is a coarse capability class, not a hardness comparison between them.
- Soft metals stay in low-duty tiers. Hard but brittle standalone materials do
  not inherit the properties of their carbides, binders, alloys or coatings.
  Tungsten is not tungsten carbide, boron is not boron carbide/CBN, and pure
  chromium is not chromium-alloyed tool steel. [Tool-material context](https://www.sandvik.coromant.com/en-gb/materials),
  [tungsten and carbide distinction](https://periodic-table.rsc.org/element/74/tungsten)
- Solid metal and ore have separate requirements. These providers' ordinary
  metal ores represent minerals dispersed in rock, so their baseline is a
  stone-level pickaxe, tier 1. This is a host-rock gameplay rule, not a claim
  that every ore mineral has the same hardness. It is independent of refined
  metal properties. [USGS mineral/host distinctions](https://pubs.usgs.gov/bul/b1693/html/bull5n38.htm)
- Keep the existing fictional tool/block/active-ore tiers deliberately. There
  is no physical evidence for Adamantine, Aquarium, Cold Iron, Mithril or
  Starsteel as these mods define them.
- Existing alloy recipes are gameplay abstractions and retain their ingredients
  and yields. In particular, the historical Al-Cu `aluminumbrass` recipe is not
  the composition of commercial aluminum brass, and the galvanized-steel
  recipe does not describe a literal 50-percent-zinc tool. The named product,
  rather than fictional recipe mass fractions, determines the interpretation.
- Keep Fantasy Level availability and the Base Metals inheritance unchanged.
  Elemental tools retained for High Fantasy are not recommendations for real
  toolmaking. Base Gems' mineral progression is a separate policy.

## Complete audit

Columns show old -> reviewed tiers. Solid requirements apply to the storage
block and its registered solid forms. `—` means that form is not registered.
A dagger marks a retained historical alloy ore; it still does not generate.
Titanium ore stays disabled by default, with rutile processing available.

### Modern Metals

| Material | Tool | Solid | Ore | Scientific context and disposition |
| --- | ---: | ---: | ---: | --- |
| `aluminum` | 0 -> 0 | 0 -> 0 | 0 -> 1 | Soft elemental aluminium, not a strengthened aircraft alloy. [Source](https://periodic-table.rsc.org/element/13/aluminium) |
| `aluminumbrass` | 0 -> 1 | 0 -> 1 | — | Legacy Al-Cu blend, not commercial Cu-Zn-Al brass. Limited alloy tier; recipe retained. [Source](https://alloys.copper.org/alloy/C68700) |
| `beryllium` | 1 -> 1 | 1 -> 1 | 1 -> 1 | Elemental Be, not copper-beryllium tool alloy. No engineered alloy properties assumed. [Source](https://periodic-table.rsc.org/element/4/beryllium) |
| `boron` | 3 -> 1 | 2 -> 2 | 2 -> 1 | Elemental/metalloid form; not boron carbide or cubic boron nitride tooling. [Source](https://periodic-table.rsc.org/element/5/boron) |
| `cadmium` | 0 -> 0 | 0 -> 0 | 0 -> 1 | Low-duty elemental metal; no hardened alloy assumed. [Source](https://periodic-table.rsc.org/element/48/cadmium) |
| `chromium` | 3 -> 1 | 2 -> 2 | 2 -> 1 | Hard constituent for alloys and coatings; pure-metal hardness is not whole-tool toughness. [Source](https://periodic-table.rsc.org/element/24/chromium) |
| `galvanizedsteel` | 1 -> 2 | 1 -> 2 | 1 -> 1 † | Steel core with a protective zinc coating; same mining capability as steel. [Source](https://galvanizeit.org/knowledgebase/article/mechanical-properties-of-galvanized-steel) |
| `iridium` | 2 -> 1 | 1 -> 2 | 1 -> 1 | Hard, brittle element; no toughened alloy or composite grade assumed. [Source](https://pubchem.ncbi.nlm.nih.gov/element/77) |
| `magnesium` | 0 -> 0 | 0 -> 0 | 0 -> 1 | Light elemental metal, not a structural magnesium alloy. [Source](https://periodic-table.rsc.org/element/12/magnesium) |
| `manganese` | 1 -> 1 | 1 -> 1 | 1 -> 1 | Brittle elemental Mn, distinct from impact-resistant manganese steel. [Source](https://periodic-table.rsc.org/element/25/manganese) |
| `nichrome` | 2 -> 2 | 1 -> 2 | 1 -> 1 † | Ductile Ni-Cr engineering alloy; not a superhard carbide grade. [Source](https://www.kanthal.com/produkte-und-dienstleistungen/datasheets/materialdatenbl%C3%A4tter/band-flachdraht/nikrothal-80/) |
| `osmium` | 2 -> 1 | 2 -> 2 | 2 -> 1 | Hard, brittle element; density does not imply impact-tool performance. [Source](https://periodic-table.rsc.org/element/76/osmium) |
| `plutonium` | 1 -> 1 | 1 -> 1 | 1 -> 1 | Unspecified elemental/phase grade; conservative compatibility tier, no nuclear-property mining bonus. [Source](https://periodic-table.rsc.org/element/94/plutonium) |
| `rutile` | 2 -> 1 | 1 -> 2 | 1 -> 1 | Titanium dioxide feed mineral, not metallic titanium; retained High Fantasy tool is a compatibility abstraction. [Source](https://periodic-table.rsc.org/element/22/titanium) |
| `stainlesssteel` | 2 -> 2 | 1 -> 2 | 1 -> 1 † | Steel family; iron-tier capability without assuming a particular hardened stainless grade. [Source](https://periodic-table.rsc.org/element/24/chromium) |
| `tantalum` | 2 -> 1 | 1 -> 1 | 1 -> 1 | Corrosion-resistant elemental metal, not an engineered high-strength tantalum alloy. [Source](https://periodic-table.rsc.org/element/73/tantalum) |
| `thorium` | 1 -> 1 | 0 -> 1 | 0 -> 1 | Unspecified elemental grade; retained limited tool tier, no nuclear-property mining bonus. [Source](https://periodic-table.rsc.org/element/90/thorium) |
| `titanium` | 2 -> 2 | 1 -> 2 | 1 -> 1 | Strong engineering metal; iron-tier capability does not mean titanium-carbide tooling. [Source](https://periodic-table.rsc.org/element/22/titanium) |
| `tungsten` | 2 -> 1 | 2 -> 2 | 2 -> 1 | Unalloyed tungsten is not cemented WC-Co; room-temperature brittleness limits impact-tool interpretation. [Source](https://www.sciencedirect.com/science/article/pii/S0921509318306440) |
| `uranium` | 2 -> 1 | 1 -> 1 | 1 -> 1 | Unspecified elemental grade; limited tool tier, not a cutting-tool alloy. [Source](https://periodic-table.rsc.org/element/92/uranium) |
| `zirconium` | 1 -> 1 | 1 -> 1 | 1 -> 1 | Corrosion-resistant metal, not zirconia ceramic or a tool alloy. [Source](https://periodic-table.rsc.org/element/40/zirconium) |

### Base Metals

| Material | Tool | Solid | Ore | Scientific context and disposition |
| --- | ---: | ---: | ---: | --- |
| `adamantine` | 4 -> 4 | 3 -> 3 | 3 -> 3 | Fictional: preserve the established active-material rules; no scientific rating. |
| `antimony` | 0 -> 0 | 0 -> 0 | 0 -> 1 | Brittle elemental material, not an antimony-strengthened alloy. [Source](https://periodic-table.rsc.org/element/51/antimony) |
| `aquarium` | 1 -> 1 | 1 -> 1 | — | Fictional: preserve the established active-material rules; no scientific rating. |
| `bismuth` | 0 -> 0 | 0 -> 0 | 0 -> 1 | Brittle elemental material; no useful impact-tool alloy assumed. [Source](https://periodic-table.rsc.org/element/83/bismuth) |
| `brass` | 1 -> 1 | 1 -> 1 | — | Copper-zinc engineering alloy; distinguish it from pure copper. [Source](https://www.copper.org/applications/marine/cuni/alloys/pub-206-copper-alloys-for-marine-environments.pdf) |
| `bronze` | 2 -> 2 | 2 -> 2 | — | Copper-tin tool alloy; useful work-hardened tooling rather than a soft-metal average. [Source](https://periodic-table.rsc.org/element/29/copper) |
| `coldiron` | 2 -> 2 | 2 -> 2 | 2 -> 2 | Fictional: preserve the established active-material rules; no scientific rating. |
| `copper` | 1 -> 1 | 1 -> 1 | 1 -> 1 | Workable elemental copper; bronze remains the stronger tool progression. [Source](https://periodic-table.rsc.org/element/29/copper) |
| `cupronickel` | 2 -> 1 | 1 -> 1 | — | Copper-nickel corrosion-resistant alloy; no hardened steel or carbide grade assumed. [Source](https://www.copper.org/applications/marine/cuni/alloys/pub-206-copper-alloys-for-marine-environments.pdf) |
| `electrum` | 1 -> 0 | 1 -> 0 | — | Gold-silver alloy; precious-metal content does not imply a stronger mining tool. [Source](https://periodic-table.rsc.org/element/79/gold) |
| `invar` | 3 -> 2 | 2 -> 2 | — | Fe-Ni engineering alloy valued for low expansion; no diamond-level cutting grade. [Source](https://www.carpentertechnology.com/hubfs/7407324/Material%20Saftey%20Data%20Sheets/Invar%2036.pdf) |
| `lead` | 0 -> 0 | 0 -> 0 | 0 -> 1 | Soft elemental metal. [Source](https://periodic-table.rsc.org/element/82/lead) |
| `mercury` | — | — | 0 -> 1 | Liquid at room temperature; no equipment is registered. Ore is separately rock-hosted. [Source](https://periodic-table.rsc.org/element/80/mercury) |
| `mithril` | 3 -> 3 | 2 -> 2 | — | Fictional: preserve the established active-material rules; no scientific rating. |
| `nickel` | 1 -> 1 | 1 -> 1 | 1 -> 1 | Elemental nickel, distinct from nickel-based engineered alloys. [Source](https://periodic-table.rsc.org/element/28/nickel) |
| `pewter` | 0 -> 0 | 0 -> 0 | — | Soft tin-based alloy; no tool-steel properties assumed. [Source](https://periodic-table.rsc.org/element/50/tin) |
| `platinum` | 1 -> 0 | 0 -> 0 | 0 -> 1 | Precious elemental metal; corrosion resistance is not a hardened tool grade. [Source](https://periodic-table.rsc.org/element/78/platinum) |
| `silver` | 1 -> 0 | 1 -> 0 | 1 -> 1 | Soft precious elemental metal. [Source](https://periodic-table.rsc.org/element/47/silver) |
| `starsteel` | 3 -> 3 | 3 -> 3 | 3 -> 3 | Fictional: preserve the established active-material rules; no scientific rating. |
| `steel` | 2 -> 2 | 2 -> 2 | — | Generic steel tool baseline; no high-speed, carbide or specially heat-treated grade assumed. [Source](https://periodic-table.rsc.org/element/26/iron) |
| `tin` | 1 -> 0 | 0 -> 0 | 0 -> 1 | Soft, pliable elemental tin, distinct from bronze. [Source](https://periodic-table.rsc.org/element/50/tin) |
| `zinc` | 0 -> 0 | 0 -> 0 | 0 -> 1 | Elemental zinc, distinct from zinc-coated steel. [Source](https://periodic-table.rsc.org/element/30/zinc) |

## Vanilla references and compatibility

Vanilla-reference tool tiers remain wood/gold 0, stone 1, iron 2 and diamond 3.
Base Metals' emerald/obsidian/quartz and other legacy non-metal equipment keep
their existing rules. No vanilla block requirement is overridden.

Mining tiers are explicit constructor data. `toolLevel()` supplies all material
tools; `requiredHarvestLevel()` supplies solid forms and
`requiredOreHarvestLevel()` supplies ores. The original constructor remains for
binary/source compatibility and legacy custom-material defaults. Registered
metal catalogues use the explicit constructor, avoiding accidental changes
when an unrelated hardness statistic is edited.

Existing block/item names, stack NBT, damage, recipe quantities, ore placements
and generation switches retain their identities. A saved tool receives its
reviewed capability from its registered item; its NBT does not need conversion.

Run each project's `check build` and Eclipse tasks. Modern Metals' lightweight
`verifyMiningLevels` checks all 21 profiles without adding test libraries to
production launches; Base Metals' tests cover all 22 profiles, constructor
validation and legacy compatibility. The isolated packaged mining probe checks
registered tools, all solid block states, ore requirements, saved stacks and
real survival harvesting, including galvanized-steel Jasper crushing.
