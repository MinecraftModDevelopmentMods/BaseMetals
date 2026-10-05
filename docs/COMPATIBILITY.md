# Compatibility

Base Metals uses Forge item, block and fluid tags for compatibility. Recipes
accept tagged ingots, nuggets, ores, storage blocks, dusts, small
dusts, blends, plates, rods, gears, casings, dense plates, crushed and purified
ores, crystals, shards, clumps, dirty dusts, and molten fluids.

Adamantite, Adamantium, Adamant, Quicksilver, and steel-sprocket names are
forwarding tags. Copper-consuming recipes accept `forge:ingots/copper`, except
Base Metals' own compacting and decompacting recipes, whose output must remain
the Base Metals item. On Minecraft 1.14.4 the copper ore, ingot, and storage
tags contain Base Metals entries; other mods can add their equivalents through
normal tag merging.

This version has no mod-specific plugins for Mekanism, Thermal Expansion, Tinkers'
Construct, Ender IO, IC2, Thaumcraft, Dense Ores, VeinMiner, or Constructs
Armory. Their version-specific 1.18 data and APIs are not valid on Forge
28. Generic processors and tools can interoperate through Forge tags.

Mineralogy is optional. When installed, OreSpawn can place the same ores in
Mineralogy's rocks. Base Metals does not need Mineralogy to run.

## Changes from the 1.12 release

The native port retains the corrected fuel values, door and trapdoor
recipes, material fishing-rod durability, rail yields, ranged damage and
projectile pickup, armour effects, recipe-book unlocks, plate repairs and the
18 gameplay achievements. High/Low Fantasy and both configuration entry points
are available here too.

MMDLib's individual material, form, molten-fluid and forced-trait switches do
not have equivalents in this port. Every block and item stays available so a
setting change cannot remove anything from an old save. Use Low Fantasy to
limit what players can make, Forge data packs for custom recipes,
and OreSpawn for ore-generation settings.

The old Tinkers/TAIGA and other mod-specific fixes are not copied as plugins:
this branch has no corresponding integrations. Historical material aliases
are forwarding Forge tags instead of shared MMDLib material-registry entries.
