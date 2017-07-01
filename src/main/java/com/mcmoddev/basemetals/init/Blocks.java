package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.block.BlockHumanDetector;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.TabContainer;

import net.minecraft.block.Block;

/**
 * This class initializes all blocks in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Blocks extends com.mcmoddev.lib.init.Blocks {

	public static Block humanDetector;

	private static boolean initDone = false;
	private static TabContainer myTabs = new TabContainer(ItemGroups.blocksTab, ItemGroups.itemsTab, ItemGroups.toolsTab);

	protected Blocks() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}
		
		Materials.init();
		ItemGroups.init();

		// Vanilla Materials get their Ore and Block always
		final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);
		final MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);
		final MMDMaterial diamond = Materials.getMaterialByName(MaterialNames.DIAMOND);
		final MMDMaterial emerald = Materials.getMaterialByName(MaterialNames.EMERALD);
		final MMDMaterial gold = Materials.getMaterialByName(MaterialNames.GOLD);
		final MMDMaterial iron = Materials.getMaterialByName(MaterialNames.IRON);
		final MMDMaterial lapis = Materials.getMaterialByName(MaterialNames.LAPIS);
		final MMDMaterial obsidian = Materials.getMaterialByName(MaterialNames.OBSIDIAN);
		final MMDMaterial quartz = Materials.getMaterialByName(MaterialNames.QUARTZ);
		final MMDMaterial redstone = Materials.getMaterialByName(MaterialNames.REDSTONE);
		// final MMDMaterial stone = Materials.getMaterialByName(MaterialNames.STONE);
		// final MMDMaterial wood = Materials.getMaterialByName(MaterialNames.WOOD);

		coal.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.COAL_BLOCK);
		coal.addNewBlock(Names.ORE, net.minecraft.init.Blocks.COAL_ORE);
		
		diamond.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.DIAMOND_BLOCK);
		diamond.addNewBlock(Names.ORE, net.minecraft.init.Blocks.DIAMOND_ORE);

		emerald.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.EMERALD_BLOCK);
		emerald.addNewBlock(Names.ORE, net.minecraft.init.Blocks.EMERALD_ORE);

		gold.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.GOLD_BLOCK);
		gold.addNewBlock(Names.ORE, net.minecraft.init.Blocks.GOLD_ORE);
		gold.addNewBlock(Names.PRESSURE_PLATE, net.minecraft.init.Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);

		iron.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.IRON_BLOCK);
		iron.addNewBlock(Names.ORE, net.minecraft.init.Blocks.IRON_ORE);
		iron.addNewBlock(Names.BARS, net.minecraft.init.Blocks.IRON_BARS);
		iron.addNewBlock(Names.DOOR, net.minecraft.init.Blocks.IRON_DOOR);
		iron.addNewBlock(Names.TRAPDOOR, net.minecraft.init.Blocks.IRON_TRAPDOOR);
		iron.addNewBlock(Names.PRESSURE_PLATE, net.minecraft.init.Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);

		lapis.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.LAPIS_BLOCK);
		lapis.addNewBlock(Names.ORE, net.minecraft.init.Blocks.LAPIS_ORE);

		obsidian.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.OBSIDIAN);

		quartz.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.QUARTZ_BLOCK);
		quartz.addNewBlock(Names.ORE, net.minecraft.init.Blocks.QUARTZ_ORE);
		// quartz.addNewBlock(Names.SLAB, net.minecraft.init.Blocks.);
		// quartz.addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.);
		// quartz.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.);

		redstone.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.REDSTONE_BLOCK);
		redstone.addNewBlock(Names.ORE, net.minecraft.init.Blocks.REDSTONE_ORE);

		if (Options.materialEnabled(MaterialNames.ADAMANTINE)) {
			createBlocksFull(MaterialNames.ADAMANTINE, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.ANTIMONY)) {
			createBlocksFull(MaterialNames.ANTIMONY, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.AQUARIUM)) {
			createBlocksFull(MaterialNames.AQUARIUM, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.BISMUTH)) {
			createBlocksFull(MaterialNames.BISMUTH, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.BRASS)) {
			createBlocksFull(MaterialNames.BRASS, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.BRONZE)) {
			createBlocksFull(MaterialNames.BRONZE, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.CHARCOAL)) {
			createBlock(charcoal, myTabs.blocksTab);
		}

		if (Options.materialEnabled(MaterialNames.COLDIRON)) {
			createBlocksFull(MaterialNames.COLDIRON, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.COPPER)) {
			createBlocksFull(MaterialNames.COPPER, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.CUPRONICKEL)) {
			createBlocksFull(MaterialNames.CUPRONICKEL, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.DIAMOND)) {
			createBars(diamond, myTabs.blocksTab);
			createDoor(diamond, myTabs.blocksTab);
			createTrapDoor(diamond, myTabs.blocksTab);

			createBlocksAdditional(diamond, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.ELECTRUM)) {
			createBlocksFull(MaterialNames.ELECTRUM, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.EMERALD)) {
			createBars(emerald, myTabs.blocksTab);
			createDoor(emerald, myTabs.blocksTab);
			createTrapDoor(emerald, myTabs.blocksTab);

			createBlocksAdditional(emerald, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.GOLD)) {
			createPlate(gold, myTabs.blocksTab);
			createBars(gold, myTabs.blocksTab);
			createDoor(gold, myTabs.blocksTab);
			createTrapDoor(gold, myTabs.blocksTab);

			createBlocksAdditional(gold, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.INVAR)) {
			createBlocksFull(MaterialNames.INVAR, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.IRON)) {
			createPlate(iron, myTabs.blocksTab);

			createBlocksAdditional(iron, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.LEAD)) {
			createBlocksFull(MaterialNames.LEAD, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.MERCURY)) {
			MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);
			createOre(mercury, myTabs.blocksTab);
			mercury.getBlock(Names.ORE).setHardness(3.0f).setResistance(5.0f);
		}

		if (Options.materialEnabled(MaterialNames.MITHRIL)) {
			createBlocksFull(MaterialNames.MITHRIL, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.NICKEL)) {
			createBlocksFull(MaterialNames.NICKEL, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.OBSIDIAN)) {
			createBars(obsidian, myTabs.blocksTab);
			createDoor(obsidian, myTabs.blocksTab);
			createTrapDoor(obsidian, myTabs.blocksTab);

			createBlocksAdditional(obsidian, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.PEWTER)) {
			createBlocksFull(MaterialNames.PEWTER, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.PLATINUM)) {
			createBlocksFull(MaterialNames.PLATINUM, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.QUARTZ)) {

			createBars(quartz, myTabs.blocksTab);
			createDoor(quartz, myTabs.blocksTab);
			createTrapDoor(quartz, myTabs.blocksTab);

			createButton(quartz, myTabs.blocksTab);
			createLever(quartz, myTabs.blocksTab);
			createPressurePlate(quartz, myTabs.blocksTab);
			createWall(quartz, myTabs.blocksTab);
		}

		if (Options.materialEnabled(MaterialNames.SILVER)) {
			createBlocksFull(MaterialNames.SILVER, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.STARSTEEL)) {
			final MMDMaterial starsteel = Materials.getMaterialByName(MaterialNames.STARSTEEL);

			createBlocksFull(starsteel, myTabs);
			starsteel.getBlock(Names.BLOCK).setLightLevel(0.5f);
			starsteel.getBlock(Names.PLATE).setLightLevel(0.5f);
			starsteel.getBlock(Names.ORE).setLightLevel(0.5f);
			starsteel.getBlock(Names.BARS).setLightLevel(0.5f);
			starsteel.getBlock(Names.DOOR).setLightLevel(0.5f);
			starsteel.getBlock(Names.TRAPDOOR).setLightLevel(0.5f);
		}

		if (Options.materialEnabled(MaterialNames.STEEL)) {
			createBlocksFull(MaterialNames.STEEL, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.STONE)) {
			// Stub
		}

		if (Options.materialEnabled(MaterialNames.TIN)) {
			createBlocksFull(MaterialNames.TIN, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.WOOD)) {
			// Stub
		}

		if (Options.materialEnabled(MaterialNames.ZINC)) {
			createBlocksFull(MaterialNames.ZINC, myTabs);
		}

		humanDetector = addBlock(new BlockHumanDetector(), "human_detector", null, ItemGroups.blocksTab);

		initDone = true;
	}
}
