package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.block.BlockHumanDetector;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import com.mcmoddev.lib.util.TabContainer;
import com.mcmoddev.basemetals.init.ItemGroups;

/**
 * This class initializes all blocks in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Blocks extends com.mcmoddev.lib.init.Blocks {

	public static Block humanDetector;

	private static boolean initDone = false;
	private static TabContainer myTabs = new TabContainer( ItemGroups.blocksTab, ItemGroups.itemsTab, ItemGroups.toolsTab );

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

		if (Options.enableAdamantine) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.ADAMANTINE), myTabs);
		}

		if (Options.enableAntimony) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.ANTIMONY), myTabs);
		}

		if (Options.enableAquarium) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.AQUARIUM), myTabs);
		}

		if (Options.enableBismuth) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.BISMUTH), myTabs);
		}

		if (Options.enableBrass) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.BRASS), myTabs);
		}

		if (Options.enableBronze) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.BRONZE), myTabs);
		}

		if (Options.enableCharcoal) {
			createBlock(Materials.getMaterialByName(MaterialNames.CHARCOAL), myTabs.blocksTab);
		}

		if (Options.enableCoal) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.COAL);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.COAL_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.COAL_ORE);
		}

		if (Options.enableColdIron) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.COLDIRON), myTabs);
		}

		if (Options.enableCopper) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.COPPER), myTabs);
		}

		if (Options.enableCupronickel) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.CUPRONICKEL), myTabs);
		}

		if (Options.enableDiamond) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.DIAMOND);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.DIAMOND_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.DIAMOND_ORE);

			createBars(material, myTabs.blocksTab);
			createDoor(material, myTabs.blocksTab);
			createTrapDoor(material, myTabs.blocksTab);

			createBlocksAdditional(material, myTabs);
		}

		if (Options.enableElectrum) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.ELECTRUM), myTabs);
		}

		if (Options.enableEmerald) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.EMERALD);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.EMERALD_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.EMERALD_ORE);

			createBars(material, myTabs.blocksTab);
			createDoor(material, myTabs.blocksTab);
			createTrapDoor(material, myTabs.blocksTab);

			createBlocksAdditional(material, myTabs);
		}

		if (Options.enableGold) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.GOLD);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.GOLD_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.GOLD_ORE);
			material.addNewBlock(Names.PRESSUREPLATE, net.minecraft.init.Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE );

			createPlate(material, myTabs.blocksTab);
			createBars(material, myTabs.blocksTab);
			createDoor(material, myTabs.blocksTab);
			createTrapDoor(material, myTabs.blocksTab);

			createBlocksAdditional(material, myTabs);
		}

		if (Options.enableInvar) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.INVAR), myTabs);
		}

		if (Options.enableIron) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.IRON);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.IRON_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.IRON_ORE);
			material.addNewBlock(Names.BARS, net.minecraft.init.Blocks.IRON_BARS);
			material.addNewBlock(Names.DOORBLOCK, net.minecraft.init.Blocks.IRON_DOOR);
			material.addNewBlock(Names.TRAPDOOR, net.minecraft.init.Blocks.IRON_TRAPDOOR);
			material.addNewBlock(Names.PRESSUREPLATE, net.minecraft.init.Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);

			createPlate(material, myTabs.blocksTab);

			createBlocksAdditional(material, myTabs);
		}

		if (Options.enableLapis) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.LAPIS);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.LAPIS_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.LAPIS_ORE);
		}

		if (Options.enableLead) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.LEAD), myTabs);
		}

		if (Options.enableMercury) {
			createOre(Materials.getMaterialByName(MaterialNames.MERCURY), myTabs.blocksTab);
			// TODO: Use this info in the Material
			// Materials.mercury.ore.setHardness(3.0f).setResistance(5.0f);
		}

		if (Options.enableMithril) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.MITHRIL), myTabs);
		}

		if (Options.enableNickel) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.NICKEL), myTabs);
		}

		if (Options.enableObsidian) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.OBSIDIAN);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.OBSIDIAN);

			createBars(material, myTabs.blocksTab);
			createDoor(material, myTabs.blocksTab);
			createTrapDoor(material, myTabs.blocksTab);

			createBlocksAdditional(material, myTabs);
		}

		if (Options.enablePewter) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.PEWTER), myTabs);
		}

		if (Options.enablePlatinum) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.PLATINUM), myTabs);
		}

		if (Options.enableQuartz) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.QUARTZ);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.QUARTZ_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.QUARTZ_ORE);
			// material.halfSlab = ;
			// material.doubleSlab = ;
			// material.stairs = ;

			createBars(material, myTabs.blocksTab);
			createDoor(material, myTabs.blocksTab);
			createTrapDoor(material, myTabs.blocksTab);

			createButton(material, myTabs.blocksTab);
			createLever(material, myTabs.blocksTab);
			createPressurePlate(material, myTabs.blocksTab);
			createWall(material, myTabs.blocksTab);
		}

		if (Options.enableRedstone) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.REDSTONE);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.REDSTONE_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.REDSTONE_ORE);
		}

		if (Options.enableSilver) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.SILVER), myTabs);
		}

		if (Options.enableStarSteel) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.STARSTEEL);

			createBlocksFull(material, myTabs);
			material.getBlock(Names.BLOCK).setLightLevel(0.5f);
			material.getBlock(Names.PLATE).setLightLevel(0.5f);
			material.getBlock(Names.ORE).setLightLevel(0.5f);
			material.getBlock(Names.BARS).setLightLevel(0.5f);
			material.getBlock(Names.DOORBLOCK).setLightLevel(0.5f);
			material.getBlock(Names.TRAPDOOR).setLightLevel(0.5f);
		}

		if (Options.enableSteel) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.STEEL), myTabs);
		}

		if (Options.enableStone) {
			// Stub
		}

		if (Options.enableTin) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.TIN), myTabs);
		}

		if (Options.enableWood) {
			// Stub
		}

		if (Options.enableZinc) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.ZINC), myTabs);
		}

		humanDetector = addBlock(new BlockHumanDetector(), "human_detector", null, ItemGroups.blocksTab);

		initDone = true;
	}
}
