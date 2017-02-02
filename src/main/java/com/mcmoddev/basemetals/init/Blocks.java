package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.blocks.BlockHumanDetector;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import cyano.basemetals.init.Materials;
import net.minecraft.block.*;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This class initializes all blocks in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Blocks extends com.mcmoddev.lib.init.Blocks {

	public static Block charcoal_block;

	public static Block mercury_ore;

	public static Block human_detector;

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Materials.init();
		ItemGroups.init();

		MetalMaterial material;

		if (Options.ENABLE_ADAMANTINE) {
			material = Materials.adamantine;
			createBlocksFull(material);
		}

		if (Options.ENABLE_ANTIMONY) {
			material = Materials.antimony;
			createBlocksFull(material);
		}

		if (Options.ENABLE_AQUARIUM) {
			material = Materials.aquarium;
			createBlocksFull(material);
		}

		if (Options.ENABLE_BISMUTH) {
			material = Materials.bismuth;
			createBlocksFull(material);
		}

		if (Options.ENABLE_BRASS) {
			material = Materials.brass;
			createBlocksFull(material);
		}

		if (Options.ENABLE_BRONZE) {
			material = Materials.bronze;
			createBlocksFull(material);
		}

		if (Options.ENABLE_CHARCOAL) {
//			material = Materials.charcoal;

//			charcoal_block = createBlock(material);
//			charcoal_plate = createPlate(material); // Dubious
//			charcoal_bars = createBars(material); // Dubious
//			charcoal_door = createDoor(material); // Dubious
//			charcoal_trapdoor = createTrapDoor(material); // Dubious

//			createBlocksAdditional(material);
		}

		if (Options.ENABLE_COAL) {
//			material = Materials.coal;

//			coal_block = createBlock(material); // In Vanilla
//			coal_plate = createPlate(material); // Dubious
//			coal_bars = createBars(material); // Dubious
//			coal_door = createDoor(material); // Dubious
//			coal_trapdoor = createTrapDoor(material); // Dubious

//			createBlocksAdditional(material);
		}

		if (Options.ENABLE_COLDIRON) {
			material = Materials.coldiron;
			createBlocksFull(material);
		}

		if (Options.ENABLE_COPPER) {
			material = Materials.copper;
			createBlocksFull(material);
		}

		if (Options.ENABLE_CUPRONICKEL) {
			material = Materials.cupronickel;
			createBlocksFull(material);
		}
		
		if (Options.ENABLE_DIAMOND) {
			material = Materials.vanilla_diamond;

//			diamond_block = createBlock(material); // In Vanilla
			Materials.vanilla_diamond.block = net.minecraft.init.Blocks.DIAMOND_BLOCK;
//			createPlate(material); // Dubious
			// diamond_ore = createOre(material); // In Vanilla
			Materials.vanilla_diamond.ore = net.minecraft.init.Blocks.DIAMOND_ORE;
			createBars(material);
			createDoor(material);
			createTrapDoor(material);

			createBlocksAdditional(material);
		}

		if (Options.ENABLE_ELECTRUM) {
			material = Materials.electrum;
			createBlocksFull(material);
		}

		if (Options.ENABLE_GOLD) {

			material = Materials.vanilla_gold;

//			gold_block = createBlock(material); // In Vanilla
			Materials.vanilla_gold.block = net.minecraft.init.Blocks.GOLD_BLOCK;
			createPlate(material);
			// gold_ore = createOre(material); // In Vanilla
			Materials.vanilla_gold.ore = net.minecraft.init.Blocks.GOLD_ORE;
			createBars(material);
			createDoor(material);
			createTrapDoor(material);

			createButton(material);
			createSlab(material);
			createDoubleSlab(material);
			createLever(material);
//			createPressurePlate(material); // In Vanilla
			createStairs(material);
			createWall(material);
		}

		if (Options.ENABLE_INVAR) {
			material = Materials.invar;
			createBlocksFull(material);
		}

		if (Options.ENABLE_IRON) {
			material = Materials.vanilla_iron;

//			iron_block = createBlock(material); // In Vanilla
			Materials.vanilla_iron.block = net.minecraft.init.Blocks.IRON_BLOCK;
			createPlate(material);
//			iron_ore = createBlock(material); // In Vanilla
			Materials.vanilla_iron.ore = net.minecraft.init.Blocks.IRON_ORE;
//			createBars(material); // In Vanilla
//			createDoor(material); // In Vanilla
//			createTrapDoor(material); // In Vanilla

			createButton(material);
			createSlab(material);
			createDoubleSlab(material);
			createLever(material);
//			createPressurePlate(material); // In Vanilla
			createStairs(material);
			createWall(material);
		}

		if (Options.ENABLE_LEAD) {
			material = Materials.lead;
			createBlocksFull(material);
		}

		if (Options.ENABLE_MERCURY) {
			mercury_ore = addBlock(new BlockOre(), "mercury_ore", null, ItemGroups.tab_blocks);
			mercury_ore.setHardness(3.0f).setResistance(5.0f);
			OreDictionary.registerOre(Oredicts.OREMERCURY, mercury_ore);
		}

		if (Options.ENABLE_MITHRIL) {
			material = Materials.mithril;
			createBlocksFull(material);
		}

		if (Options.ENABLE_NICKEL) {
			material = Materials.nickel;
			createBlocksFull(material);
		}

		if (Options.ENABLE_PEWTER) {
			material = Materials.pewter;
			createBlocksFull(material);
		}

		if (Options.ENABLE_PLATINUM) {
			material = Materials.platinum;
			createBlocksFull(material);
		}

		if (Options.ENABLE_SILVER) {
			material = Materials.silver;
			createBlocksFull(material);
		}

		if (Options.ENABLE_STARSTEEL) {
			material = Materials.starsteel;
			createBlocksFull(material);
			material.block.setLightLevel(0.5f);
			material.plate.setLightLevel(0.5f);
			material.ore.setLightLevel(0.5f);
			material.bars.setLightLevel(0.5f);
			material.doorBlock.setLightLevel(0.5f);
			material.trapdoor.setLightLevel(0.5f);
		}

		if (Options.ENABLE_STEEL) {
			material = Materials.steel;
			createBlocksFull(material);
		}

		if (Options.ENABLE_STONE) {
			material = Materials.vanilla_stone;

//			stone_block = createBlock(material);
//			createPlate(material);
//			createBars(material);
//			createDoor(material);
//			createTrapDoor(material);

//			createBlocksAdditional(material);
		}

		if (Options.ENABLE_TIN) {
			material = Materials.tin;
			createBlocksFull(material);
		}

		if (Options.ENABLE_WOOD) {
			material = Materials.vanilla_wood;

//			wood_block = createBlock(material);
//			createPlate(material);
//			createBars(material);
//			createDoor(material);
//			createTrapDoor(material);

//			createBlocksAdditional(material);
		}

		if (Options.ENABLE_ZINC) {
			material = Materials.zinc;
			createBlocksFull(material);
		}

		human_detector = addBlock(new BlockHumanDetector(), "human_detector", null, ItemGroups.tab_blocks);

		initDone = true;
	}
}
