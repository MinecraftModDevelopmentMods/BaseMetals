[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge downloads](https://cf.way2muchnoise.eu/full_base-metals_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/base-metals)
[![Supported Minecraft versions](https://cf.way2muchnoise.eu/versions/Minecraft_base-metals_all.svg)](https://www.curseforge.com/minecraft/mc-mods/base-metals)

# Base Metals

Base Metals adds historically common metals, useful alloys, and fantasy metals
to Minecraft. Depending on the material, these are available as ores, storage
and decorative blocks, molten fluids, crafting components, tools, armour,
shields, bows, crossbows, arrows, and bolts. The mod also includes Crack
Hammers, scythes, metal anvils, the human detector, villager trades,
advancements, and structure loot.

This is the Minecraft 1.21.1 NeoForge port, `3.1.0.121012`. It includes raw
metals, ore texture overlays and High/Low Fantasy modes. Existing Base Metals items and blocks keep
their registry IDs. OreSpawn handles ore generation; MMDLib and Additional Loot
Tables are no longer needed.

## Requirements

- Minecraft `1.21.1`
- NeoForge `21.1.247` or newer in the Minecraft 1.21.1 line
- Java 21
- OreSpawn `[4.0.16.121012,5.0.0)` for Minecraft 1.21.1 NeoForge on both client and server

This version builds against OreSpawn `4.0.16.121012`. Put
`BaseMetals-3.1.0.121012.jar` and a compatible OreSpawn JAR in the `mods`
directory. MMDLib and Additional Loot Tables must not be carried forward from
an older installation. Mineralogy is optional.

## Materials and gameplay

Base Metals provides 22 materials:

- Natural and industrial metals: Antimony, Bismuth, Copper, Lead, Mercury,
  Nickel, Platinum, Silver, Tin, and Zinc.
- Alloys: Brass, Bronze, Cupronickel, Electrum, Invar, Pewter, and Steel.
- Fantasy materials: Adamantine, Aquarium, Cold Iron, Mithril, and Starsteel.

All existing items and blocks stay registered, including when Low Fantasy
limits which items you can make. Recipes use the shared `c:` tags, so compatible ores,
ingots, powders, plates, rods, gears and other components from different mods
can be used together.

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
  plate while retaining their enchantments.
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

Open **Mods → Base Metals → Config** from the main menu. You can also edit
`config/basemetals-common.toml`.

The OreSpawn 4.0 dependency used by this port does not have the configuration
cog API from OreSpawn 4.1. Use the Mods-list button for Base Metals settings.

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

## Mod and data-pack compatibility

Shared `c:` item, block and fluid tags are Base Metals' public compatibility API.

Mekanism's optional API integration targets its Minecraft 1.21.1 release.
The retained Thermal, Tinkers' Construct and Ender IO data is not a promise of
compatibility with this port. Those integrations still need testing with
matching NeoForge releases. Do not substitute jars built for Forge or another
Minecraft version.

See [Compatibility](docs/COMPATIBILITY.md) for the supported processing chains,
tag aliases, copper rules, and intentionally retired legacy integrations. See
[Supported versions](docs/VERSIONS.md) for the exact dependency and Maven
contracts.

## Building and contributing

This branch uses NeoGradle `7.1.38`, Gradle `9.6.1` and Java 21. OreSpawn
is resolved from MMD's Maven repository. Build with:

```text
./gradlew build
```

Generate Eclipse project files with:

```text
./gradlew eclipse
```

Make sure Eclipse and the command line use the same Gradle user home and
Java 21. If your workspace uses a different cache, override it in this project's
Gradle settings and run configuration. Mixing caches can make the launcher
reject duplicate modules even when the jars are identical.

Use the Gradle **runClient** task for normal testing with Base Metals and
OreSpawn. Mekanism is a compile-only dependency, not an extra mod in this launch.

Use **runData** when changing recipes, models or translations, then include
the generated resources with the source changes. Ore models use KiriCattus'
transparent overlays over vanilla textures,
so resource packs can replace the surrounding stone, netherrack or end stone.

The port regression checks are separate from the normal build. Run them with:

```text
./gradlew -I tools/verification/port-checks.init.gradle portTest runPortGameTestServer
```

These checks cover generated recipe unlocks, translations, chest additions,
shield upgrades, old projectile data, smith trades and recipe network encoding.
They do not change the build or deployment workflow. See
[Port regression checks](tools/verification/README.md) for details.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.1.0.121012`.

Report defects and compatibility problems through the
[Base Metals issue tracker](https://github.com/MinecraftModDevelopmentMods/BaseMetals/issues).

Base Metals is licensed under [LGPL-2.1](LICENSE). Bundled or derived third-party
art is covered by the accompanying attribution and licence files.
