# Base Metals 1.11.2

[![Build](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/ci.yml/badge.svg?branch=master-1.11.2)](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/ci.yml?query=branch%3Amaster-1.11.2)
[![CodeQL](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/codeql-analysis.yml/badge.svg?branch=master-1.11.2)](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/codeql-analysis.yml?query=branch%3Amaster-1.11.2)

Base Metals adds metals like copper, tin, lead and silver, plus alloys such as
bronze and steel. There are a few fantasy metals too: look for Cold Iron and
Adamantine in the Nether, and Star Steel in the End.

## Installing and updating

Install Forge `13.20.1.2588` or later and
[MMD OreSpawn `4.1.6.111021`](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn/files/9524249)
or a newer compatible 4.x release for Minecraft 1.11.2. Put the OreSpawn and
`BaseMetals-2.5.1.111021.jar` files in your `mods` folder.

If you're updating an existing world, back up the world and the `config` folder
first. Replace Base Metals and OreSpawn together, but keep your old OreSpawn
config files so their settings can be imported. OreSpawn now handles all Base
Metals ore placement, and the eleven default rules haven't changed. Check
[the ore generation guide](docs/ORE_GENERATION.md) before exploring new chunks,
especially if your pack changes where vanilla ores spawn.

## Using the Crack Hammer

Break an ore block with a Crack Hammer to get two piles of powder. Smelt the
powder into ingots, or mix different powders and smelt the blend to make an
alloy. You can also drop a crushable item and right-click the ground beneath
it with the hammer. If you use JEI, its Crack Hammer category shows what you
can crush.

## Making alloys

Mix three copper powders with one tin powder for bronze, or swap the tin for
zinc to make brass. Equal parts silver and gold make electrum. Two parts iron
to one part nickel make invar; eight parts iron to one part carbon make steel.
Mithril uses two parts silver to one part each of mercury and Cold Iron.

## Choosing a Fantasy Level

Open **Mods → Base Metals → Config** from the main menu, or use the Base Metals
cog under **Create World → OreSpawn → Mods**. Both edit `config/BaseMetals.cfg`.
Restart Minecraft or the server to apply changes.

**High Fantasy** keeps the usual recipes and is the default for older configs.
**Low Fantasy** limits what you can make from each material. For example, you
can still make an Adamantine pickaxe, but not an Adamantine bow. Items you
already have still work, and ore placement doesn't change. In multiplayer,
use the same Fantasy Level on the client and server.

## Building and running from source

Minecraft and the mod still run on Java 8. The Gradle wrapper uses Java 17,
and ForgeGradle's Minecraft setup needs Java 25 for Mavenizer. Import
`BaseMetals` as an existing Gradle project in Eclipse, run `prepareEclipse`,
then choose `runClient` or `runServer`. The normal launches don't load optional
integration mods or test mods.

See [the build guide](docs/BUILDING.md) for toolchains, checks and legacy-world
tests. The source for this version is on `master-1.11.2`.

## Mod API

New integrations should use `zone.moddev.mc.basemetals.init` for materials,
blocks and items. The Maven coordinate is
`zone.moddev.mc.basemetals:BaseMetals:2.5.1.111021`. Crack Hammer recipes still
use `com.mcmoddev.lib.registry.CrusherRecipeRegistry`.

Existing add-ons can still use the old `com.mcmoddev.basemetals` class names.
The adapter handles compiled references and literal class names used in
reflection, but not names an add-on assembles at runtime. Saved block and item
IDs still use `basemetals`.


