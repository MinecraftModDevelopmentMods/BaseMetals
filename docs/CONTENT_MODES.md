# Content modes and configuration

Open **Mods → Base Metals → Config** from the main menu, or click the Base
Metals cog in OreSpawn's world-creation settings. These open the same screen.
Hover over an option for help. Done saves, Cancel or Escape discards edits,
Undo restores the values shown when the screen opened, and Defaults restores
High Fantasy and the four enabled gameplay switches.

The file is `config/basemetals-common.toml`. Its existing switches keep their
names and defaults: `specialEffects`, `starsteelRegeneration`,
`mercuryImmersionEffects` and `villagerTrades`. Those switches can change live.
`contentMode` is different: changing it requires restarting Minecraft.

## High Fantasy

`contentMode = "high_fantasy"` is the default. All normal Base Metals recipes,
villager trades and chest loot remain available. An older 1.13 config without
this setting, or no config file at all, uses High Fantasy automatically.
Unknown mode names fall back to High Fantasy and are corrected in the config.

Picking up a metal ingot reveals that material's crafting recipes, including
bows and crossbows. You do not need to make rods or plates first. Vanilla-material
recipes use their usual base materials: gems, coal, charcoal, redstone, stone
or logs. Existing recipe-book progress is kept.

## Low Fantasy

`contentMode = "low_fantasy"` keeps fantasy metals, but not every material can
be made into every item. For example, you cannot make an Adamantine bow or
Pewter armour. Base Metals bows, crossbows and fishing rods cannot be crafted
or found in its normal villager trades or chest loot. Vanilla Minecraft
equipment and recipes are unchanged.

The equipment rules are:

The full tool family includes axes, Crack Hammers, hoes, pickaxes, scythes,
shears, shovels and swords.

| Material | Tools and melee weapons |
| --- | --- |
| Adamantine, Aquarium, Bronze, Cold Iron, Copper, Cupronickel, Invar, Mithril, Nickel, Starsteel, Steel, Iron | Full tool family |
| Silver | Sword |
| Obsidian | Axe, sword, scythe |
| Diamond | Crack Hammer, scythe, shears |
| Stone | Crack Hammer, scythe |
| Wood | Crack Hammer |
| Other materials | None |

Armour, shields and horse armour remain available for Adamantine, Aquarium,
Brass, Bronze, Cold Iron, Copper, Cupronickel, Electrum, Invar, Mithril, Nickel,
Platinum, Silver, Starsteel and Steel, plus the Iron, Gold and Diamond additions.
Arrows and bolts remain available for the full-tool materials and Silver,
Obsidian and Diamond.

Gears remain available for the full-tool materials and Brass, Gold and Wood.
The available anvils are Stone, Steel and Adamantine. Solid materials can still
be made into storage and building blocks. Mercury keeps its ore, powders, liquid,
bucket and the ingot used to make Mithril, but not nuggets, solid construction
forms or equipment.

These restrictions also apply to plate repairs, shield upgrades, new villager
trades and chest loot. Smelting small Mercury powders will not give Mercury
nuggets. Unavailable recipes stay out of the recipe book.

If a material earns a recipe advancement in Low Fantasy, returning to High
Fantasy restores that recipe-book unlock when you next join. There is no need
to reset advancements or collect the ingredient again.

## Servers, modpacks and old saves

Your choice applies to all worlds you play on this installation. To join a
server, choose the same mode as the server and restart Minecraft. If the modes
differ, the connection screen tells you how to change yours. An old config
without a mode setting counts as High Fantasy.

Nothing you already own is removed or stops working. That includes bows and
fishing rods you can no longer make in Low Fantasy. All items remain available
in creative mode. Existing villager offers, commands and recipes supplied by
other mods or data packs are left alone.

Neither mode changes the eleven OreSpawn rules or overwrites a world's ore
profile. Use OreSpawn's editor to change ore placement. This port does not
include the old MMDLib integration plugins or their configuration categories.
