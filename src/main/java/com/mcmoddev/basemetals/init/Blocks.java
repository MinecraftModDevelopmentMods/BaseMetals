package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.block.BlockHumanDetector;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

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

		com.mcmoddev.basemetals.util.Config.init();
		com.mcmoddev.lib.init.Blocks.init();
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

		if (Options.isMaterialEnabled(MaterialNames.ADAMANTINE)) {
			createBlocksFull(MaterialNames.ADAMANTINE, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.ANTIMONY)) {
			createBlocksFull(MaterialNames.ANTIMONY, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.AQUARIUM)) {
			createBlocksFull(MaterialNames.AQUARIUM, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.BISMUTH)) {
			createBlocksFull(MaterialNames.BISMUTH, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.BRASS)) {
			createBlocksFull(MaterialNames.BRASS, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.BRONZE)) {
			createBlocksFull(MaterialNames.BRONZE, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.CHARCOAL)) {
			create(Names.BLOCK, charcoal, ItemGroups.myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.COLDIRON)) {
			createBlocksFull(MaterialNames.COLDIRON, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.COPPER)) {
			createBlocksFull(MaterialNames.COPPER, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.CUPRONICKEL)) {
			createBlocksFull(MaterialNames.CUPRONICKEL, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.DIAMOND)) {
			create(Names.BARS, diamond, ItemGroups.myTabs.blocksTab);
			create(Names.DOOR, diamond, ItemGroups.myTabs.blocksTab);
			create(Names.TRAPDOOR, diamond, ItemGroups.myTabs.blocksTab);

			createBlocksAdditional(diamond, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.ELECTRUM)) {
			createBlocksFull(MaterialNames.ELECTRUM, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.EMERALD)) {
			create(Names.BARS, emerald, ItemGroups.myTabs.blocksTab);
			create(Names.DOOR, emerald, ItemGroups.myTabs.blocksTab);
			create(Names.TRAPDOOR, emerald, ItemGroups.myTabs.blocksTab);

			createBlocksAdditional(emerald, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.GOLD)) {
			create(Names.PLATE, gold, ItemGroups.myTabs.blocksTab);
			create(Names.BARS, gold, ItemGroups.myTabs.blocksTab);
			create(Names.DOOR, gold, ItemGroups.myTabs.blocksTab);
			create(Names.TRAPDOOR, gold, ItemGroups.myTabs.blocksTab);

			createBlocksAdditional(gold, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.INVAR)) {
			createBlocksFull(MaterialNames.INVAR, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.IRON)) {
			create(Names.PLATE, iron, ItemGroups.myTabs.blocksTab);

			createBlocksAdditional(iron, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.LEAD)) {
			createBlocksFull(MaterialNames.LEAD, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);
			create(Names.ORE, mercury, ItemGroups.myTabs.blocksTab);
			mercury.getBlock(Names.ORE).setHardness(3.0f).setResistance(5.0f);
		}

		if (Options.isMaterialEnabled(MaterialNames.MITHRIL)) {
			createBlocksFull(MaterialNames.MITHRIL, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.NICKEL)) {
			createBlocksFull(MaterialNames.NICKEL, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.OBSIDIAN)) {
			create(Names.BARS, obsidian, ItemGroups.myTabs.blocksTab);
			create(Names.DOOR, obsidian, ItemGroups.myTabs.blocksTab);
			create(Names.TRAPDOOR, obsidian, ItemGroups.myTabs.blocksTab);

			createBlocksAdditional(obsidian, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.PEWTER)) {
			createBlocksFull(MaterialNames.PEWTER, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.PLATINUM)) {
			createBlocksFull(MaterialNames.PLATINUM, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.QUARTZ)) {

			create(Names.BARS, quartz, ItemGroups.myTabs.blocksTab);
			create(Names.DOOR, quartz, ItemGroups.myTabs.blocksTab);
			create(Names.TRAPDOOR, quartz, ItemGroups.myTabs.blocksTab);

			create(Names.BUTTON, quartz, ItemGroups.myTabs.blocksTab);
			create(Names.LEVER, quartz, ItemGroups.myTabs.blocksTab);
			create(Names.PRESSURE_PLATE, quartz, ItemGroups.myTabs.blocksTab);
			create(Names.WALL, quartz, ItemGroups.myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.SILVER)) {
			createBlocksFull(MaterialNames.SILVER, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.STARSTEEL)) {
			final MMDMaterial starsteel = Materials.getMaterialByName(MaterialNames.STARSTEEL);

			createBlocksFull(starsteel, ItemGroups.myTabs);
			starsteel.getBlock(Names.BLOCK).setLightLevel(0.5f);
			starsteel.getBlock(Names.PLATE).setLightLevel(0.5f);
			starsteel.getBlock(Names.ORE).setLightLevel(0.5f);
			starsteel.getBlock(Names.BARS).setLightLevel(0.5f);
			starsteel.getBlock(Names.DOOR).setLightLevel(0.5f);
			starsteel.getBlock(Names.TRAPDOOR).setLightLevel(0.5f);
		}

		if (Options.isMaterialEnabled(MaterialNames.STEEL)) {
			createBlocksFull(MaterialNames.STEEL, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.STONE)) {
			// Stub
		}

		if (Options.isMaterialEnabled(MaterialNames.TIN)) {
			createBlocksFull(MaterialNames.TIN, ItemGroups.myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.WOOD)) {
			// Stub
		}

		if (Options.isMaterialEnabled(MaterialNames.ZINC)) {
			createBlocksFull(MaterialNames.ZINC, ItemGroups.myTabs);
		}

		humanDetector = addBlock(new BlockHumanDetector(), "human_detector", ItemGroups.myTabs.blocksTab);

		initDone = true;
	}
}
