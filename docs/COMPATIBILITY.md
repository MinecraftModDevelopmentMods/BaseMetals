# Compatibility

Base Metals uses Forge item, block and fluid tags for compatibility. Recipes
accept tagged ingots, nuggets, ores, storage blocks, dusts, small
dusts, blends, plates, rods, gears, casings, dense plates, crushed and purified
ores, crystals, shards, clumps, dirty dusts, and molten fluids.

Adamantite, Adamantium, Adamant, Quicksilver, and steel-sprocket names are
forwarding tags. Copper-consuming recipes accept `forge:ingots/copper`, except
Base Metals' own compacting and decompacting recipes, whose output must remain
the Base Metals item. On Minecraft 1.13.2 the copper ore, ingot, and storage
tags contain Base Metals entries; other mods can add their equivalents through
normal tag merging.

This version has no mod-specific plugins for Mekanism, Thermal Expansion, Tinkers'
Construct, Ender IO, IC2, Thaumcraft, Dense Ores, VeinMiner, or Constructs
Armory. Their version-specific 1.18 data and APIs are not valid on Forge
25. Generic processors and tools can interoperate through Forge tags.

Mineralogy is entirely optional. Base Metals does not link to Mineralogy Java
classes; OreSpawn's rock-family host matching is the only integration point.
