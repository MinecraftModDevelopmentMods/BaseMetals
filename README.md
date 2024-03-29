[![](https://img.shields.io/badge/Discord-MMD-green.svg?style=flat&logo=Discord)](https://discord.mcmoddev.com)
[![CurseForge](http://cf.way2muchnoise.eu/base-metals.svg)](https://www.curseforge.com/minecraft/mc-mods/base-metals)
[![Versions](http://cf.way2muchnoise.eu/versions/base-metals.svg)](https://www.curseforge.com/minecraft/mc-mods/base-metals)
[![Build Status](https://ci.mcmoddev.com/job/Base%20Metals/job/Base%20Metals%201.12/badge/icon)](https://ci.mcmoddev.com/job/Base%20Metals/job/Base%20Metals%201.12/)

## Base Metals Mod

This mod adds historically commonly used metals to Minecraft, specifically Silver, Copper, Tin, Lead, Zinc, Mercury, and Nickel. You will also find a number of metal alloys in this mod and a new tool: the Crack Hammer.

Fantasy metals have been added to the Nether and the End: Cold Iron, Mithril, Adamantine, and Star Steel



## Metals and Alloys

###Natural Metals

**Antimony**: 

**Bismuth**: 

**Copper**: Copper is soft, easy to work, and fairly abundant. It is needed to make bronze and brass metal alloys.

**Lead**: Lead is a very soft metal that is best known for being very heavy and somewhat toxic. Lead tools are fragile, but can dish out a lot of damage when used as weapons.

**Mercury**: This toxic liquid metal has many alchemical uses. The Mithril metal alloys requires mercury.

**Nickel**: This metal is as soft as copper, but it has alchemical properties and forms the invar metal alloy when mixed with iron.

**Platinum**: 

**Silver**: Silver is a soft, shiny metal that is valued for both is beauty and its alchemical uses.

**Tin**: Tin is an extremely soft metal that is not useful by itself, but can be combined with copper to make bronze, a metal alloy nearly as strong as iron.

**Zinc**: Like tin, zinc is worthless on its own, but can be used to make the metal alloy brass.



###Metal Alloys
*Metal alloys are made by crafting together the powdered forms of the required metals, and then smelting the resulting alloy blend in a furnace.*

**Brass**: This alloy of copper and zinc (2:1 ratio) is soft, but has a beautiful golden color.

**Bronze**: This alloy of copper and tin (3:1 ratio) is as hard as iron, but not quite as durable.

**Electrum**: This alloy of silver and gold (1:1 ratio) can be enchanted like gold, but is slightly more durable.

**Invar**: This alloy of iron and nickel (2:1 ratio) is harder than steel, but less durable.

**Pewter**: 

**Steel**: This alloy of iron and carbon (8:1 ratio) is as hard as iron and much more durable.


###Fantasy Metals

**Adamantine**: (found in the Nether): Adamantine is a rare magical metal that is as strong as diamond, maybe even stronger. Armor made from Adamantine grants resistance to damage and tools made from adamantine are extra effective against monsters that have more than 10 hearts of health.

**Aquarium**: 

**Cold-Iron**: (found in the Nether): Cold-Iron is a magical metal that is as strong as iron. Tools made from Cold Iron are extra effective against denizens of the Nether and any creature that is immune to fire.

**Mithril**: Mithril is an alloy of alloy of silver, mercury, and cold-iron (2:1:1 ratio). It is as strong as steel and Mithril weapons are extra effective against undead.

**Star-Steel**: (found in the End): Armor made from Star Steel reduces the weight of the wearer, allowing the wearer to jump higher and fall slower. Star-Steel tools slowly repair themselves while held.



## Crack Hammer

Also known as a sledgehammer, this tool is designed for pulverizing rocks. Using this tool on ores will cause them to drop powdered metal instead of the standard ore block. Use can then use the powdered metals to make metal alloy mixes or just smelt the powder into ingots. You can crush items by dropping them on the ground and then right-clicking on the ground beneath the items.



## Requirements

This mod requires that you install Minecraft Forge version 1.12-14.21.1.2387 or later (earlier versions of Forge for Minecraft 1.12 may work, but no guarantees).



## Installing

After you have successfully installed Forge, simply place the file *basemetals-#.#.#.jar* in your *mods* folder. You can get the basemetals-#.#.jar file from the Releases tab of this repository page.

##NEI

The Crack Hammer recipes from this mod will appear in NEI if you have NEI installed. Hooray!


## Mod API

Check the developer releases to download the files *basemetals-#.#.#-deobf.jar*, *basemetals-#.#.#-sources.jar*, *basemetals-#.#.#-javadoc.jar*. In your Eclipse project (and *build.gradle* file), add *basemetals-#.#.#-deobf.jar* as a library dependency. All items and blocks can be conveniently accessed via classes in the *com.mcmoddev.basemetals.init* package. New recipes for the Crack Hammer can be added via the *com.mcmoddev.lib.registry.CrusherRecipeRegistry* class.


## Q&A

Q: Why make this mod?

A: Minecraft appears to have a pre-industrial fantasy setting, but the fantasy literature (and pre-industrial history) is full of references to silver and copper as well as gold.



Q: What does the Crack Hammer do?

A: When you break ores with the Crack Hammer, you get two piles of metal powders instead of a single block of ore. This is necessary to make metal alloys, which is done by combining the powders of the various components and then smelting the alloy mix.


