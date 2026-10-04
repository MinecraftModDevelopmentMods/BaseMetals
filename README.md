[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge downloads](https://cf.way2muchnoise.eu/full_base-metals_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/base-metals)
[![Minecraft versions](https://cf.way2muchnoise.eu/versions/Minecraft_base-metals_all.svg)](https://www.curseforge.com/minecraft/mc-mods/base-metals)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/ci.yml/badge.svg?branch=master-1.12)](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/ci.yml?query=branch%3Amaster-1.12)
[![CodeQL](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/codeql-analysis.yml/badge.svg?branch=master-1.12)](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/codeql-analysis.yml?query=branch%3Amaster-1.12)

# Base Metals for Minecraft 1.12.2

Base Metals adds metals, alloys, equipment, building blocks, and the crackhammer.
Crush ores into powder, smelt the powder into ingots, or combine it into alloy
blends. The metals also work with supported processing and tool-building mods.

This branch builds **2.6.0.112021** for Minecraft 1.12.2 and Forge. It keeps the
existing block and item names for saved worlds, fixes long-standing gameplay
issues, and adds an optional Low Fantasy content mode. See the
[changelog](CHANGELOG.txt) for the release changes.

## Installation

Install these mods on both the client and server:

- [Minecraft Forge](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.12.2.html)
  14.23.5.2859 or newer for Minecraft 1.12.2.
- [MMDLib](https://www.curseforge.com/minecraft/mc-mods/mmdlib), tested with
  `1.0.0-rc2.36`.
- [MMD OreSpawn](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn)
  `4.1.0.112021` or newer in the 4.x line.
- [Base Metals](https://www.curseforge.com/minecraft/mc-mods/base-metals).

Put the jars in the instance's `mods` folder. Minecraft 1.12.2 runs on Java 8.
Back up your worlds and configuration before upgrading. MMD OreSpawn is the
world-generation library, not the unrelated creature mod with a similar name.

## Metals and alloys

The natural materials are Antimony, Bismuth, Copper, Lead, Mercury, Nickel,
Platinum, Silver, Tin, and Zinc. The alloys are Brass, Bronze, Cupronickel,
Electrum, Invar, Pewter, and Steel. Adamantine, Aquarium, Cold Iron, Mithril,
and Starsteel provide the fantasy materials.

OreSpawn places Cold Iron and Adamantine in the Nether and Starsteel in the
End. Copper, Silver, Tin, Lead, Zinc, Mercury, Nickel, and Platinum generate
in ordinary dimensions. Antimony and Bismuth ore blocks are available, but
they do not generate by default. Aquarium and Mithril are alloys, not ores.

Make alloys by combining powders in a crafting grid and smelting the resulting
blend. For example, Bronze uses Copper and Tin; Steel uses Iron and carbon.
Vanilla Bits adds useful forms for vanilla materials, such as Iron powder,
Stone crackhammers, and Obsidian tools.

## Crackhammers and equipment

Mine a crushable block with a crackhammer to get its powdered ingredients.
Many metal ores yield two powders, which can be smelted into ingots. You can
also drop items on the ground and use the hammer on the block beneath them.
The exact output depends on the crusher recipe.

Material choice affects the damage of Base Metals bows, arrows, crossbows,
and bolts. Crossbows use the mod's original draw-and-release behaviour; they
do not store a charged shot. Ordinary vanilla bow-and-arrow damage is unchanged.

Some materials have additional effects:

- Adamantine armor grants Resistance, and its melee tools are stronger against
  creatures with more than 20 maximum health.
- A complete Aquarium suit grants Water Breathing and Resistance while in
  water and removes Mining Fatigue.
- A complete Cold Iron suit grants Fire Resistance.
- A complete Mithril suit removes harmful effects.
- Lead armor slows its wearer.
- Starsteel armor grants Jump Boost and, from two pieces onward, Speed.
  Held Starsteel equipment slowly repairs itself.

One matching plate fully repairs a damaged armor piece or shield in a crafting
grid, preserving its enchantments and name. This uses MMDLib's
`repair_using_plates` setting.

## Configuration

Open **Mods → Base Metals → Config** from the main menu, or use the Base Metals
cog under **Create World → OreSpawn → Mods**. Both open the same configuration
screen. Changes require a Minecraft restart.

- **High Fantasy** is the default and keeps the historical content available.
- **Low Fantasy** limits equipment and building recipes to suitable materials.
  It does not remove existing items or change ore generation.

An old configuration without `contentMode` still uses High Fantasy. The mode
applies to the whole Minecraft instance or server, not to one world. Clients
and servers must use the same mode.

OreSpawn is the only Base Metals ore generator. Edit ore rules in OreSpawn,
not through MMDLib's old `using_orespawn` or `fallback_orespawn` switches.
Existing OreSpawn world settings and explicit overrides are preserved.

See the [configuration guide](docs/configuration.md) and
[world-generation guide](docs/world-generation.md), including the example
for letting CoFH World place the ores instead.

## Mod compatibility

Base Metals includes optional integrations for Tinkers' Construct, Construct's
Armory, Thermal Expansion, Mekanism, Ender IO, IC2, Thaumcraft, Dense Ores,
VeinMiner, and Additional Loot Tables. Ore Dictionary entries let other mods
use the materials without a dedicated integration. These mods are not required
for an ordinary Base Metals installation.

The Tinkers integration keeps Base Metals Adamantine separate from TAIGA's
Adamant. Low Fantasy also restricts the equipment routes exposed by supported
integrations.

The [Brittle modifier crash at one durability](https://github.com/MinecraftModDevelopmentMods/BaseMetals/issues/485)
belongs to MMDLib and is not fixed by this Base Metals release.

## Building and contributing

Use the Gradle wrapper. Gradle runs on Java 17; the build uses Java 25 for
ForgeGradle's Mavenizer and Java 8 to compile and run Minecraft. The exact
toolchain and dependency versions are recorded in `gradle.properties`.

```text
./gradlew check build javadoc verifyReleaseArtifacts
./gradlew prepareEclipse
```

`prepareEclipse` creates client and server launches with processed resources
and keeps optional compile-only mods and test code out of normal launches.
The [build-script guide](gradle/README.md) explains the supporting scripts.
The [release guide](docs/releasing.md) covers publication from the MMD Actions tab.

Release artifacts use the Maven coordinates
`zone.moddev.mc.basemetals:BaseMetals:2.6.0.112021`. Java packages remain under
`com.mcmoddev.basemetals`; this release does not move saved registry names.

Report bugs through the [issue tracker](https://github.com/MinecraftModDevelopmentMods/BaseMetals/issues).
Include your mod versions, relevant configuration, and a log or crash report.
