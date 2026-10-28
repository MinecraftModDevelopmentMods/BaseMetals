# Optional compatibility

Base Metals' permanent public contract is data-driven. The current `c:` common
tags and the retained `forge:` aliases cover ingots, nuggets, ores, storage blocks, dusts, small dusts, blends,
plates, rods, gears, casings, dense plates, crushed and purified ores, crystals,
shards, clumps, dirty dusts, and molten fluids.

Both names lead to the same material entries. Existing recipes using the old
Forge names still work, while other mods can use the current common tags.

Adamantite, Adamantium, Adamant, Quicksilver, and steel-sprocket names are
forwarding tags. Copper-consuming recipes accept `forge:ingots/copper`.
Copper ingots, nuggets, tools, armour, horse armour, bars, storage blocks, doors,
trapdoors, slabs and stairs use their
vanilla equivalents as new recipe outputs. The old Base Metals IDs remain
registered; their ingredient tags include both versions. Copper ore and powders
smelt to vanilla copper ingots. Existing copper equipment remains usable.

Vanilla copper trapdoors cost three ingots each, while the old Base Metals
trapdoors cost four. Their separate recycling recipes return those respective
amounts, so the new recipes cannot turn three ingots into four. Other retained
recycling recipes also accept the appropriate vanilla equivalent.

The tags and existing integration data for Mekanism, Thermal, Tinkers' Construct
and Ender IO remain available for future compatibility work. Their Java API hooks
and runtime jars are not included in this Forge 26.1.2 port. Do not substitute
jars made for older Minecraft versions or NeoForge.

Other processors can use the shared Forge tags without a direct Java integration.

Mineralogy is entirely optional. Base Metals never links to its Java classes;
OreSpawn's rock-family host matching is the only integration point.

## Multiplayer login

Base Metals checks the client's content mode during Forge's configuration phase.
Matching modes can join; different modes receive a message explaining which setting
needs to change. The check completes before the player enters the world.

The Forge 47 connection patch used by the 1.20.1 release is not needed by this
target and is not included.

## Low Fantasy

Base Metals filters its own crafting, processing, trades and chest additions.
Its conditional integration recipes follow the same material restrictions.
The retained Tinkers data does not provide bows or parts that would enable
a restricted tool family. Explicit third-party recipes and data-pack overrides
remain under the pack author's control.

Raw metals have their own `forge:raw_materials/<metal>` tags. They are not dusts
and cannot replace powders in alloy blends. Vanilla raw iron, gold and copper
are supported by the crushing recipes.
