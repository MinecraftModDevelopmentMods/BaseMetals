[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge downloads](https://cf.way2muchnoise.eu/full_base-metals_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/base-metals)
[![Supported Minecraft versions](https://cf.way2muchnoise.eu/versions/Minecraft_base-metals_all.svg)](https://www.curseforge.com/minecraft/mc-mods/base-metals)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/ci.yml/badge.svg?branch=master-26.3)](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/ci.yml?query=branch%3Amaster-26.3)

# Base Metals

Base Metals adds historically common metals, useful alloys, and fantasy metals
to Minecraft. Depending on the material, these are available as ores, storage
and decorative blocks, molten fluids, crafting components, tools, armour,
shields, bows, crossbows, arrows, and bolts. The mod also includes Crack
Hammers, scythes, metal anvils, the human detector, villager trades,
advancements, and structure loot.

This is the Minecraft 26.3 Forge version, `3.1.1.2603001`. It keeps the
fixes and features from the released 26.2 version, including raw metals, ore
texture overlays and High/Low Fantasy modes. Existing Base Metals items and
blocks keep their registry IDs. OreSpawn handles ore generation; MMDLib and
Additional Loot Tables are no longer needed.

## Requirements

- Minecraft `26.3`
- Forge `66.0.0` or newer in the Minecraft 26.3 Forge 66 line
- A current Java 25 runtime
- OreSpawn `[4.1.0.2603001,5.0.0)` on both client and server

The build uses Forge `66.0.0` and is also tested on `66.1.0`.

This version uses OreSpawn `4.1.0.2603001`. Put
`BaseMetals-3.1.1.2603001.jar` and a compatible OreSpawn JAR in the `mods`
directory. MMDLib and Additional Loot Tables must not be carried forward from
an older installation. Mineralogy is optional.

## Materials and gameplay

Base Metals provides 22 materials:

- Natural and industrial metals: Antimony, Bismuth, Copper, Lead, Mercury,
  Nickel, Platinum, Silver, Tin, and Zinc.
- Alloys: Brass, Bronze, Cupronickel, Electrum, Invar, Pewter, and Steel.
- Fantasy materials: Adamantine, Aquarium, Cold Iron, Mithril, and Starsteel.

All existing items and blocks stay registered, including when Low Fantasy
limits which items you can make. Recipes use shared common tags, with legacy
Forge tag names kept as aliases. Compatible ores, ingots, powders, plates,
rods, gears and other components from different mods can be used together.

Where vanilla has the same copper forms, recipes now produce vanilla ingots,
blocks, nuggets, tools, armour, horse armour, bars, doors, trapdoors, cut-copper
slabs and stairs. The old Base Metals items and blocks stay registered and
continue working in existing saves. Shared ingredient tags accept both versions,
so older items and the rest of the copper recipe chains remain usable.
Existing Base Metals copper equipment keeps its
original behaviour.

Notable mechanics include:

- Crack Hammers crush supported mined blocks through the data-driven
  `basemetals:crushing` recipe type. They can also crush dropped items by using
  the hammer on the block beneath them; sneaking processes as much of a stack
  as the hammer's remaining durability allows.
- Mining ores normally gives raw metal. Fortune increases the drops, and Silk
  Touch keeps the ore block. Copper uses vanilla raw copper. A dropped raw piece
  makes one powder when crushed; hammering an ore block directly gives two.
- Raw metals can be smelted or blasted into ingots. Powders, not raw metals, are
  used to make alloy blends.
- Scythes harvest a horizontal 3x3 area while respecting normal Forge block
  break and protection events, drops, enchantments, and durability.
- Material shields can be upgraded at an anvil with one strictly harder tagged
  plate while retaining their enchantments, name and other item data.
- Bows fire arrows, while Base Metals' draw-and-release crossbows fire bolts.
  Both the weapon and ammunition material contribute to the damage, before
  enchantment bonuses. Picked-up projectiles keep their material and item data.
- One matching plate fully repairs damaged armour or a shield in a crafting
  grid, keeping its enchantments, name and other item data.
- Configurable armour and melee effects give Adamantine, Aquarium, Cold Iron,
  Lead, Mithril, and Starsteel their distinctive behaviour. Held Starsteel
  equipment repairs by one durability every 200 server ticks; Starsteel armour
  does not regenerate.
- Molten-metal buckets place and collect the corresponding fluid blocks. These
  fluids are world blocks and compatibility resources rather than a second
  Base Metals processing system.

Ore blocks use KiriCattus' overlays on vanilla stone, netherrack and end stone,
so they follow your resource pack's surrounding textures. Bars, doors and
trapdoors keep their transparent gaps without hiding nearby blocks. Bar models
also avoid overlapping faces that caused their vertical edges to flicker.

## Configuration

Open **Mods → Base Metals → Config** from the main menu, or use the Base Metals
cog in OreSpawn's world-creation screen. You can also edit
`config/basemetals-common.toml`.

High Fantasy is the default and keeps all of the usual crafting options.
Low Fantasy limits equipment to suitable materials and removes Base Metals
bow, crossbow and fishing-rod recipes. Existing equipment still works, and
everything remains available in Creative mode. Neither mode changes vanilla
Minecraft recipes or ore placement.

Restart Minecraft after changing the mode. A client must use the same mode
as its server; older configuration files without this setting default to
High Fantasy. This is a server or modpack setting, not a per-world setting.
Ore generation is still configured in OreSpawn.

The other four switches control equipment effects, held Starsteel regeneration,
mercury immersion effects and villager trades. They are enabled by default.
Done saves your changes; Cancel or Escape leaves the settings unchanged.

## Ore generation

[OreSpawn](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn) is the
only ore-placement engine used by this port. Base Metals contains no native or
fallback world generator.

Fresh installations enable ten ore rules. Cold Iron and Adamantine generate in
the Nether, Starsteel generates in the End, and the remaining enabled ores use
explicit 1.18 height ranges in ordinary dimensions other than the Nether and
End. Vanilla copper generation remains enabled. The Base Metals Copper,
Antimony, and Bismuth ore blocks remain registered for compatibility and data
packs, but do not generate by default.

Base Metals does not add rock strata. If Mineralogy is installed, OreSpawn uses
the same Base Metals rules and its declared rock-family host tags, avoiding a
second copy of each ore.

See [World generation](docs/WORLDGEN.md) for the complete provider contract,
default distributions, and data-pack override guidance.

## Updating an old world

The port supports direct upgrades from Base Metals 1.10 and 1.12, including the
original Cyano 2.4 line and the later MMD 2.5 line. The first upgrade makes
world-local backups before converting legacy numeric chunks, registry aliases,
inventories, equipment, block entities, fluids, and OreSpawn 3 configuration.
Migration stops instead of guessing when required legacy registry information
is unavailable.

Always perform the first upgrade on a copy of the world and keep the untouched
original. Read [Migration](docs/MIGRATION.md) before starting an upgraded world.

Adding Base Metals to a modern world does not trigger the old numeric-ID
conversion just because an older backup is present. Existing player data stays
in place; genuine legacy upgrades still make backups before changing saved data.

The 26.3 update also protects imported terrain from dungeon placement during
conversion. New chunks and the new underground areas can still generate normally.

## Mod and data-pack compatibility

Forge item, block, and fluid tags are Base Metals' public compatibility API.

Compatible tags and the existing recipe data for Mekanism, Thermal, Tinkers'
Construct and Ender IO are retained, but their Java integrations are not enabled
in this Forge 26.3 port. Do not add jars made for older Minecraft versions or
NeoForge to this instance. No optional integration API is required to compile
or launch Base Metals.

The normal development client loads only Base Metals and OreSpawn.

See [Compatibility](docs/COMPATIBILITY.md) for the supported processing chains,
tag aliases, copper rules, and intentionally retired legacy integrations. See
[Supported versions](docs/VERSIONS.md) for the exact dependency and Maven
contracts.

## Building and contributing

Base Metals uses ForgeGradle `7.0.34`, Gradle `9.6.1`, official Minecraft
mappings, and Java 25 for the build tools and Minecraft. A normal command-line
build resolves the published OreSpawn release from CurseMaven:

```text
./gradlew clean check build javadoc
```

Generate and verify Eclipse launches with:

```text
./gradlew prepareEclipse
```

IntelliJ users can import the Gradle project with Java 25 and run
`./gradlew prepareIntelliJ` to add client and server configurations. Both IDEs
use the same Gradle-managed Forge bootstrap and assets.

Use **runClient** for normal testing with Base Metals and OreSpawn only.
The optional-mod profile uses a separate folder, but currently has no additional
mods on this target. Both profiles are available in Eclipse and through Gradle tasks.

Use the Gradle **generateResources** task when changing recipes, models or
translations. It updates the checked-in data; ordinary builds only check for
drift. Ore models use KiriCattus' transparent overlays over vanilla textures,
so resource packs can replace the surrounding stone, netherrack or end stone.

The CI workflow also runs all required GameTests, generated-data drift checks,
packaged client and dedicated-server smokes, release-JAR audits, dependency
identity checks, and a second clean build to prove byte-identical release
artifacts. GameTests, smoke probes, migration fixtures, and reference sources
are isolated from the published JAR.

Virtual-display client checks use `-PvirtualDisplaySmoke=true` to skip Forge's
early splash window. Minecraft still opens its game window and runs the full
world check. Linux CI uses Ubuntu 26.04 with Mesa software rendering and checks
the SDL/OpenGL context before starting Minecraft. Ordinary client launches keep
the normal splash screen and graphics settings.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.1.1.2603001`.

Report defects and compatibility problems through the
[Base Metals issue tracker](https://github.com/MinecraftModDevelopmentMods/BaseMetals/issues).

Base Metals is licensed under [LGPL-2.1](LICENSE). Bundled or derived third-party
art is covered by the accompanying attribution and licence files.
