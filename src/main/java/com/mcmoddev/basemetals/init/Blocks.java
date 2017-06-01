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

		if (Options.materialEnabled(MaterialNames.ADAMANTINE)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.ADAMANTINE), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.ANTIMONY)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.ANTIMONY), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.AQUARIUM)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.AQUARIUM), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.BISMUTH)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.BISMUTH), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.BRASS)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.BRASS), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.BRONZE)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.BRONZE), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.CHARCOAL)) {
			createBlock(Materials.getMaterialByName(MaterialNames.CHARCOAL), myTabs.blocksTab);
		}

		if (Options.materialEnabled(MaterialNames.COAL)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.COAL);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.COAL_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.COAL_ORE);
		}

		if (Options.materialEnabled(MaterialNames.COLDIRON)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.COLDIRON), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.COPPER)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.COPPER), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.CUPRONICKEL)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.CUPRONICKEL), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.DIAMOND)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.DIAMOND);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.DIAMOND_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.DIAMOND_ORE);

			createBars(material, myTabs.blocksTab);
			createDoor(material, myTabs.blocksTab);
			createTrapDoor(material, myTabs.blocksTab);

			createBlocksAdditional(material, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.ELECTRUM)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.ELECTRUM), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.EMERALD)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.EMERALD);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.EMERALD_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.EMERALD_ORE);

			createBars(material, myTabs.blocksTab);
			createDoor(material, myTabs.blocksTab);
			createTrapDoor(material, myTabs.blocksTab);

			createBlocksAdditional(material, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.GOLD)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.GOLD);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.GOLD_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.GOLD_ORE);
			material.addNewBlock(Names.PRESSUREPLATE, net.minecraft.init.Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);

			createPlate(material, myTabs.blocksTab);
			createBars(material, myTabs.blocksTab);
			createDoor(material, myTabs.blocksTab);
			createTrapDoor(material, myTabs.blocksTab);

			createBlocksAdditional(material, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.INVAR)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.INVAR), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.IRON)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.IRON);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.IRON_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.IRON_ORE);
			material.addNewBlock(Names.BARS, net.minecraft.init.Blocks.IRON_BARS);
			material.addNewBlock(Names.DOOR, net.minecraft.init.Blocks.IRON_DOOR);
			material.addNewBlock(Names.TRAPDOOR, net.minecraft.init.Blocks.IRON_TRAPDOOR);
			material.addNewBlock(Names.PRESSUREPLATE, net.minecraft.init.Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);

			createPlate(material, myTabs.blocksTab);

			createBlocksAdditional(material, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.LAPIS)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.LAPIS);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.LAPIS_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.LAPIS_ORE);
		}

		if (Options.materialEnabled(MaterialNames.LEAD)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.LEAD), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.MERCURY)) {
			createOre(Materials.getMaterialByName(MaterialNames.MERCURY), myTabs.blocksTab);
			Materials.getMaterialByName(MaterialNames.MERCURY).getBlock(Names.ORE).setHardness(3.0f).setResistance(5.0f);
		}

		if (Options.materialEnabled(MaterialNames.MITHRIL)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.MITHRIL), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.NICKEL)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.NICKEL), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.OBSIDIAN)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.OBSIDIAN);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.OBSIDIAN);

			createBars(material, myTabs.blocksTab);
			createDoor(material, myTabs.blocksTab);
			createTrapDoor(material, myTabs.blocksTab);

			createBlocksAdditional(material, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.PEWTER)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.PEWTER), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.PLATINUM)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.PLATINUM), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.QUARTZ)) {
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

		if (Options.materialEnabled(MaterialNames.REDSTONE)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.REDSTONE);

			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.REDSTONE_BLOCK);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.REDSTONE_ORE);
		}

		if (Options.materialEnabled(MaterialNames.SILVER)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.SILVER), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.STARSTEEL)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.STARSTEEL);

			createBlocksFull(material, myTabs);
			material.getBlock(Names.BLOCK).setLightLevel(0.5f);
			material.getBlock(Names.PLATE).setLightLevel(0.5f);
			material.getBlock(Names.ORE).setLightLevel(0.5f);
			material.getBlock(Names.BARS).setLightLevel(0.5f);
			material.getBlock(Names.DOOR).setLightLevel(0.5f);
			material.getBlock(Names.TRAPDOOR).setLightLevel(0.5f);
		}

		if (Options.materialEnabled(MaterialNames.STEEL)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.STEEL), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.STONE)) {
			// Stub
		}

		if (Options.materialEnabled(MaterialNames.TIN)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.TIN), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.WOOD)) {
			// Stub
		}

		if (Options.materialEnabled(MaterialNames.ZINC)) {
			createBlocksFull(Materials.getMaterialByName(MaterialNames.ZINC), myTabs);
		}

		humanDetector = addBlock(new BlockHumanDetector(), "human_detector", null, ItemGroups.blocksTab);

		initDone = true;
	}
}
