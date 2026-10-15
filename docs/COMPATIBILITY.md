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

The integration data below is carried over from 1.18. These mods do not publish Forge
1.19.4 builds, so their integrations are not qualified on this branch. Do not
load a jar for another Minecraft version to test them. Their APIs are
compile-only dependencies and never enter the normal development runtime.

- Mekanism 10.2: dirty and clean slurries plus the complete ore-processing
  sequence for Adamantine, Antimony, Bismuth, Cold Iron, Nickel, Platinum,
  Starsteel, and Zinc. The slurry objects are registered only when Mekanism is
  loaded and use its public API.
- Thermal Expansion 9: pulverizer, furnace, crucible, press, and induction
  smelter recipes.
- Tinkers' Construct 3.7: common tags, melting, casting, alloying, and
  data-defined material stats. No removed foreign traits are reimplemented.
- Ender IO 6 alpha: SAG Mill and Alloy Smelter recipes.

IC2, Thaumcraft, Dense Ores, VeinMiner, and Constructs Armory do not have direct
integrations in this version. Their generic tools and ore processors can still interoperate
through Forge tags.

Mineralogy is entirely optional. Base Metals never links to its Java classes;
OreSpawn's rock-family host matching is the only integration point.

## Low Fantasy

Base Metals filters its own crafting, processing, trades and chest additions.
Its conditional integration recipes follow the same material restrictions.
Tinkers' bundled material stats do not provide bows or parts that would enable
a restricted tool family. Explicit third-party recipes and data-pack overrides
remain under the pack author's control.

Raw metals have their own `forge:raw_materials/<metal>` tags. They are not dusts
and cannot replace powders in alloy blends. Vanilla raw iron, gold and copper
are supported by the crushing recipes.
