[![Discord](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.moddev.zone)
[![CurseForge downloads](https://cf.way2muchnoise.eu/full_base-metals_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/base-metals)
[![Supported Minecraft versions](https://cf.way2muchnoise.eu/versions/Minecraft_base-metals_all.svg)](https://www.curseforge.com/minecraft/mc-mods/base-metals)
[![Build, test, and audit](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/ci.yml/badge.svg?branch=master-1.16.5)](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/ci.yml?query=branch%3Amaster-1.16.5)

# Base Metals

Base Metals adds real and fantasy metals, along with useful alloys, to
Minecraft. Depending on the material, these are available as ores, storage
and decorative blocks, molten fluids, crafting components, tools, armour,
shields, bows, crossbows, arrows, and bolts. The mod also includes Crack
Hammers, scythes, metal anvils, the human detector, villager trades,
advancements, and extra loot to find in chests.

This is Base Metals `3.1.0.116051` for Minecraft 1.16.5. It brings the 1.15.2
release forward while keeping the same blocks, items and recipes. MMDLib and
Additional Loot Tables are no longer needed. OreSpawn places the ores, and
Base Metals supplies the metals and their recipes.

Ores use vanilla stone, netherrack and end-stone textures beneath Kiri's ore
overlays, so resource packs can change their surroundings without making the
ores look out of place. Smith trades and village chest loot use the
Armorer, Weaponsmith and Toolsmith professions introduced in Minecraft 1.14.

The 3.1 release reduces ore-overlay flickering and uses Forge's fluid-bucket
icons, with names that clearly describe their contents. These improvements
are not yet included in the 3.0.1 builds for Minecraft 1.13–1.15.

## Requirements

- Minecraft `1.16.5`
- Forge `36.2.34`
- Java 8
- OreSpawn `[4.1.0.116051,5.0.0)` on both client and server

Put `BaseMetals-3.1.0.116051.jar` and OreSpawn `4.1.0.116051` (or a later
compatible OreSpawn 4 build for 1.16.5) in the `mods` directory. MMDLib and
Additional Loot Tables are not dependencies. Mineralogy is optional.

## Materials and gameplay

Base Metals provides 22 materials:

- Natural and industrial metals: Antimony, Bismuth, Copper, Lead, Mercury,
  Nickel, Platinum, Silver, Tin, and Zinc.
- Alloys: Brass, Bronze, Cupronickel, Electrum, Invar, Pewter, and Steel.
- Fantasy materials: Adamantine, Aquarium, Cold Iron, Mithril, and Starsteel.

Not every material has every form. Recipes use Forge tags, so compatible ores,
ingots, powders, plates, rods, gears and other components from different mods
can be used together.

Notable mechanics include:

- Crack Hammers crush ores and other supported blocks as you mine them. They
  can also crush dropped items by using the hammer on the block beneath them;
  sneaking processes as much of a stack as the hammer's remaining durability
  allows.
- Supported ore powders can be smelted into ingots, and alloy blends provide
  the historical furnace-based alloying route.
- Scythes harvest plants in a horizontal 3x3 area. They work with enchantments
  and land-protection mods, and wear out as you use them.
- Upgrade a shield at an anvil with one plate of a harder metal. The shield
  keeps its enchantments.
- One damaged armour piece or shield and a matching plate fully repair the
  item without losing its name, enchantments or other saved data. These repair
  recipes stay out of the recipe book.
- Base Metals bows fire material arrows. Its legacy-style crossbows are
  draw-and-release weapons which fire material bolts. Both the launcher and
  ammunition materials contribute to damage; Power is added afterward.
  Crossbows do not use arrows. In Creative mode, firing without bolts uses a
  free iron bolt. Arrows and bolts share the vanilla arrow projectile texture.
- Configurable armour and melee effects give Adamantine, Aquarium, Cold Iron,
  Lead, Mithril, and Starsteel their distinctive behaviour. Held Starsteel
  equipment repairs by one durability every 10 seconds; its armour does
  not regenerate.
- Molten-metal buckets let you place and collect fluids. Base Metals does not
  include a smeltery; other mods may provide ways to produce these fluids.
- Crack Hammers turn Nether gold ore into two gold powders, and Ancient Debris
  into two Netherite scraps. Gilded Blackstone is not a crushing ingredient.

Open the configuration from the main menu's **Mods → Base Metals → Config**
button, or from the Base Metals cog in OreSpawn's world-creation settings.
Both open the same screen, with Cancel, Undo and Defaults controls.

Collecting a metal ingot reveals that material's crafting recipes in the recipe
book, as in 1.12. Vanilla-material recipes are discovered through their usual
base materials, such as diamonds, stone or logs. Low Fantasy shows only the
recipes allowed by that mode.

The four switches, enabled by default, control special equipment effects,
Starsteel self-repair, mercury poisoning and villager trades. The content-mode
selector offers **High Fantasy** (craft freely with every metal) and
**Low Fantasy** (keep fantasy metals, but limit what each material can make).
Mode changes require a restart, and clients and servers must use the same mode.
Old configs without this setting remain in High Fantasy.

Everything you already own stays usable in either mode, and ore generation
stays the same. Your choice applies to all your worlds; servers choose their
own mode. See [Content modes and configuration](docs/CONTENT_MODES.md) for the
full list of restrictions. Use OreSpawn to change where ores generate.

## Ore generation

[OreSpawn](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn) is the
only ore-placement engine used by this port. Base Metals contains no native or
fallback world generator.

Fresh installations enable all eleven historical ore rules. Cold Iron and
Adamantine generate in the Nether, Starsteel generates in the End, and Copper,
Silver, Tin, Lead, Zinc, Mercury, Nickel, and Platinum generate in ordinary
dimensions. Antimony and Bismuth ore blocks remain registered but do not
generate by default.

Base Metals does not add rock strata. If Mineralogy is installed, OreSpawn uses
the same rules inside its rock families without generating a second copy of
each ore. See [World generation](docs/WORLDGEN.md) for the complete provider
settings.

## Updating an old world

Minecraft 1.13 changed how worlds store blocks and items. This port supports
direct upgrades from Base Metals 1.10 and 1.12, including the original Cyano
2.4 line and later MMD releases. It restores old Base Metals and MMDLib block
IDs before Minecraft converts the chunks, and also converts old item names,
durability and universal fluid buckets. Chunks containing only Base Metals
items in containers are protected too.

Always perform the first upgrade on a copy of the world and keep the untouched
original. Read [Migration](docs/MIGRATION.md) before opening an old save. A
world already upgraded to Minecraft 1.17 or later must not be opened in this
older version. Worlds from the Base Metals 1.13.2, 1.14.4 and 1.15.2 ports can be
upgraded normally.

## Compatibility

Forge item, block, and fluid tags are Base Metals' public compatibility API.
The 1.16.5 line deliberately contains no version-specific Mekanism, Thermal,
Tinkers' Construct, Ender IO, IC2, or Thaumcraft plugin code. Compatible mods
can consume the common tags without linking to Base Metals internals. See
[Compatibility](docs/COMPATIBILITY.md) and [Supported versions](docs/VERSIONS.md).

## Building and contributing

Base Metals uses ForgeGradle `7.0.34` and Gradle `9.6.1`. Gradle runs on Java
17, ForgeGradle's legacy Minecraft transformation utility runs on Java 25, and
the mod compiles and runs on Java 8. Development builds accept newer Java 8
updates; release auditing requires Temurin `8.0.502+7` for reproducibility.
Node.js is also needed to generate and check the resource files.

The build scripts are divided by purpose; [Build scripts](gradle/README.md)
explains where to find the IDE, resource-generation and release checks.

For a normal development build and client launch, use:

```text
./gradlew build
./gradlew runClient
```

Run these tasks from IntelliJ's Gradle window or a terminal. ForgeGradle's
launcher prepares the assets and native libraries; no manual asset paths are
needed. The Gradle JVM should be Java 17, not the Java 8 game runtime.

Ore model JSONs update automatically when you build, launch with `runClient`,
or refresh the Gradle project in Eclipse. Put transparent ore textures under
`src/main/resources/assets/basemetals/textures/block/ore_overlays`; no separate
generation command is needed. Review and commit the updated models with the
textures. CI and release auditing still reject stale models.

With the pinned release toolchain, run the full release checks:

```text
./gradlew clean check build javadoc verifyReleaseArtifacts writeReleaseChecksums
```

Generate and verify Eclipse launches with:

```text
./gradlew genEclipseRuns eclipse isolateEclipseProductionRuns verifyEclipseProductionClasspath
```

Release candidates can also be exercised as packaged mods in prepared official
Forge 36 client and server runtimes with `packagedRuntimeIntegrationTest`; pass
their directories through the `packagedForgeClientRuntime` and
`packagedForgeServerRuntime` Gradle properties. These probes load the exact
release JAR and OreSpawn dependency, create real worlds, validate client models
and colours, and remain outside the published artifact.

`contentModeIntegrationTest` uses the same runtime properties to check both
content modes, old configuration files, client/server matching and mismatch
messages in disposable multiplayer profiles.

`packagedMineralogyIntegrationTest` checks ore placement alongside Mineralogy.
It uses the server runtime above and a `mineralogyTestJar` property pointing to
the qualified Mineralogy 1.16.5 JAR. Mineralogy remains optional for players.

CI checks a fresh Forge setup, unit tests, generated files, a dedicated server
with the pinned OreSpawn release, release-JAR contents, checksums, reproducible
builds, CodeQL, wrapper validation, and Eclipse classpath isolation. Test probes
and historical fixtures are excluded from the published JAR.

Development and packaged tests use the published
[OreSpawn `4.1.0.116051` release](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn/files/9080646)
through Curse Maven: `curse.maven:mmd-orespawn-245586:9080646`. The build checks
its checksum, Minecraft version and configuration-screen API.

CI stages the same verified download with `gradle/stage-orespawn-release.sh`
and passes its temporary repository through `orespawnVerificationRepository`.
Normal development does not need a local OreSpawn checkout or repository.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.1.0.116051`.

Report defects through the
[Base Metals issue tracker](https://github.com/MinecraftModDevelopmentMods/BaseMetals/issues).

Base Metals is licensed under [LGPL-2.1](LICENSE). Bundled or derived third-party
art is covered by the accompanying attribution and licence files.
