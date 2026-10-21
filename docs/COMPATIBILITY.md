# Optional compatibility

Base Metals' permanent public contract is data-driven. Recipes consume Forge
tags for ingots, nuggets, ores, storage blocks, dusts, small dusts, blends,
plates, rods, gears, casings, dense plates, crushed and purified ores, crystals,
shards, clumps, dirty dusts, and molten fluids.

Adamantite, Adamantium, Adamant, Quicksilver, and steel-sprocket names are
forwarding tags. Copper-consuming recipes accept `forge:ingots/copper`, except
Base Metals' own compacting and decompacting recipes, whose unambiguous output
must remain the Base Metals item. Copper ore and storage tags contain both
vanilla and Base Metals entries; Base Metals copper ore smelts to the Base
Metals ingot.

These integrations use Minecraft 1.20.1 builds. Their APIs are compile-only
dependencies and never enter the normal development runtime. The separate
optional-mod profile uses the exact releases and checksums in
`gradle/optional-mods.json`; don't substitute jars for another Minecraft version.
Thermal and Ender IO use packaged-runtime tests rather than the IDE profile,
because their embedded dependencies retain production names after remapping.

- Mekanism 10.4: dirty and clean slurries plus the complete ore-processing
  sequence for Adamantine, Antimony, Bismuth, Cold Iron, Nickel, Platinum,
  Starsteel, and Zinc. The slurry objects are registered only when Mekanism is
  loaded and use its public API.
- Thermal Expansion 11: pulverizer, furnace, crucible, press, and induction
  smelter recipes.
- Tinkers' Construct 3.11: common tags, melting, casting, alloying, and
  data-defined material stats. No removed foreign traits are reimplemented.
- Ender IO 6.2 beta: SAG Mill and Alloy Smelter recipes.

CoFH Core 11.0.2.56 logs a `MultiPlayerGameModeMixin` warning on dedicated
servers. This also occurs with CoFH Core alone and does not stop the server
loading.

IC2, Thaumcraft, Dense Ores, VeinMiner, and Constructs Armory do not have direct
integrations in this version. Their generic tools and ore processors can still interoperate
through Forge tags.

Mineralogy is entirely optional. Base Metals never links to its Java classes;
OreSpawn's rock-family host matching is the only integration point.

## Multiplayer login

Forge 47 can occasionally stop reading from a connection during login. Base
Metals keeps the protocol's read-pause and read-resume changes on Netty's event
loop to prevent that race. This does not skip any login checks or change the
connection timeout.

## Low Fantasy

Base Metals filters its own crafting, processing, trades and chest additions.
Its conditional integration recipes follow the same material restrictions.
Tinkers' bundled material stats do not provide bows or parts that would enable
a restricted tool family. Explicit third-party recipes and data-pack overrides
remain under the pack author's control.

Raw metals have their own `forge:raw_materials/<metal>` tags. They are not dusts
and cannot replace powders in alloy blends. Vanilla raw iron, gold and copper
are supported by the crushing recipes.
