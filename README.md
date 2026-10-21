# Base Metals 1.10.2

[![Build](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/ci.yml/badge.svg?branch=master-1.10.2)](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/ci.yml?query=branch%3Amaster-1.10.2)
[![CodeQL](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/codeql-analysis.yml/badge.svg?branch=master-1.10.2)](https://github.com/MinecraftModDevelopmentMods/BaseMetals/actions/workflows/codeql-analysis.yml?query=branch%3Amaster-1.10.2)

## Base Metals Mod

This mod adds historically commonly used metals to Minecraft, specifically Silver, Copper, Tin, Lead, Zinc, Mercury, and Nickel. You will also find a number of metal alloys in this mod and a new tool: the Crack Hammer.

Fantasy metals have been added to the Nether and the End: Cold Iron, Mithril, Adamantine, and Star Steel



## Metals and Alloys

### Natural Metals

**Antimony**: 

**Bismuth**: 

**Copper**: Copper is soft, easy to work, and fairly abundant. It is needed to make bronze and brass metal alloys.

**Lead**: Lead is a very soft metal that is best known for being very heavy and somewhat toxic. Lead tools are fragile, but can dish out a lot of damage when used as weapons.

**Mercury**: This toxic liquid metal has many alchemical uses. The Mithril metal alloys requires mercury.

**Nickel**: This metal is as soft as copper, but it has alchemical properties and forms the invar metal alloy when mixed with iron.

**Platinum**: 

**Silver**: Silver is a soft, shiny metal that is valued for both is beauty and its alchemical uses.

**Tin**: Tin is an extremely soft metal that is not useful by itself, but can be combined with copper to make bronze, a metal alloy nearly as strong as iron.

**Zinc**: Like tin, zinc is worthless on its own, but can be used to make the metal ally brass.



### Metal Alloys
*Metal alloys are made by crafting together the powdered forms of the required metals, and then smelting the resulting alloy blend in a furnace.*

**Brass**: This alloy of copper and zinc (3:1 ratio) is soft, but has a beautiful golden color.

**Bronze**: This alloy of copper and tin (3:1 ratio) is as hard as iron, but not quite as durable.

**Electrum**: This alloy of silver and gold (1:1 ratio) can be enchanted like gold, but is slightly more durable.

**Invar**: This alloy of iron and nickel (2:1 ratio) is harder than steel, but less durable.

**Pewter**: 

**Steel**: This alloy of iron and carbon (8:1 ratio) is as hard as iron and much more durable.


### Fantasy Metals

**Adamantine**: (found in the Nether): Adamantine is a rare magical metal that is as strong as diamond, maybe even stronger. Armor made from Adamantine grants resistance to damage and tools made from adamantine are extra effective against monsters that have more than 10 hearts of health.

**Aquarium**: 

**Cold-Iron**: (found in the Nether): Cold-Iron is a magical metal that is as strong as iron. Tools made from Cold Iron are extra effective against denizens of the Nether and any creature that is immune to fire.

**Mithril**: Mithril is an alloy of alloy of silver, mercury, and cold-iron (2:1:1 ratio). It is as strong as steel and Mithril weapons are extra effective against undead.

**Star-Steel**: (found in the End): Armor made from Star Steel reduces the weight of the wearer, allowing the wearer to jump higher and fall slower. Star-Steel tools slowly repair themselves while held.



## Crack Hammer

Also known as a sledgehammer, this tool is designed for pulverizing rocks. Using this tool on ores will cause them to drop powdered metal instead of the standard ore block. Use can then use the powdered metals to make metal alloy mixes or just smelt the powder into ingots. You can crush items by dropping them on the ground and then right-clicking on the ground beneath the items.



## Requirements

Install Forge `12.18.3.2511` or later and
[MMD OreSpawn `4.1.2.110021`](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn/files/9276430)
or a newer compatible 4.x release for Minecraft 1.10.2. Base Metals no longer
includes its own fallback ore generator.

OreSpawn handles ore placement and imports old OreSpawn configuration files.
The eleven ore defaults are unchanged. See [ore generation and upgrades](docs/ORE_GENERATION.md)
for configuration locations and advice for existing worlds.

## Building and running from source

This branch uses ForgeGradle 7 and the Gradle wrapper, like the newer branches.
Minecraft and the mod still run on Java 8. Gradle uses Java 17, while the initial
Minecraft setup also needs Java 25 for Mavenizer.

Import the inner `BaseMetals` directory as an existing Gradle project. Use the
wrapper, then run `prepareEclipse` and choose `runClient` or `runServer`.
Optional integration mods are compile-time APIs, not ordinary launch dependencies.
See [the build guide](docs/BUILDING.md) for toolchains, checks and legacy-world tests.

Source for this version lives on `master-1.10.2`. Releases use the shared
**Release Base Metals** action after the branch's build and compatibility checks
pass. Normal builds and tags do not publish anything.



## Installing

Install Forge and OreSpawn, then put `BaseMetals-2.5.1.110021.jar` in your
`mods` folder. Downloads are available on CurseForge and the repository's
Releases page once the version is published.

## NEI

The Crack Hammer recipes from this mod will appear in NEI if you have NEI installed. Hooray!

## Upgrading an existing 1.10 world

Back up your world before updating any mods. This build recognizes the original
Cyano names for door items, coal powder and liquid Mercury, including items in
chunks you visit later. Base Metals iron nuggets keep their existing identity.

Mods that use Cyano's old Java API may need their own updates. Restoring saved
items cannot, by itself, repair those Java references. Base Metal Golems
`6.02.1`, with Extra Golems `6.02`, is supported by a small compatibility layer:
its eighteen golems use the existing Base Metals blocks and ingots. Neither
add-on needs changing, and neither is required when playing without golems.

This release uses version `2.5.1.110021`. The final six digits identify Minecraft
1.10.2 on Forge, following the same version format as the newer branches.
The mod ID, saved block and item names, resources and configuration names remain
`basemetals`; moving the Java packages does not rename anything in your world.


## Mod API

New integrations should use `zone.moddev.mc.basemetals.init` for materials,
blocks and items. Maven builds use
`zone.moddev.mc.basemetals:BaseMetals:2.5.1.110021`. Crack Hammer recipes still
use `com.mcmoddev.lib.registry.CrusherRecipeRegistry`; the bundled library API
has not moved.

An early-loading adapter redirects compiled references to the former
`com.mcmoddev.basemetals` classes to their new names. They share the same mod
instance, material objects and registries, rather than registering a second
copy. This covers class references and literal reflection names, not arbitrary
class names assembled by an add-on at runtime. The Cyano compatibility layer
only supplies the block and ingot fields used by Base Metal Golems; it is not a
replacement for the complete Cyano API.


## Q&A

Q: Why make this mod?

A: Minecraft appears to have a pre-industrial fantasy setting, but the fantasy literature (and pre-industrial history) is full of references to silver and copper as well as gold.



Q: What does the Crack Hammer do?

A: When you break ores with the Crack Hammer, you get two piles of metal powders instead of a single block of ore. This is necessary to make metal alloys, which is done by combining the powders of the various components and then smelting the alloy mix.


