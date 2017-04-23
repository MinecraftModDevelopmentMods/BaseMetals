package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;
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

	public static Block human_detector;

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

		Materials.init();
		ItemGroups.init();

		if (Options.enableAdamantine) {
			createBlocksFull(Materials.adamantine);
		}

		if (Options.enableAntimony) {
			createBlocksFull(Materials.antimony);
		}

		if (Options.enableAquarium) {
			createBlocksFull(Materials.aquarium);
		}

		if (Options.enableBismuth) {
			createBlocksFull(Materials.bismuth);
		}

		if (Options.enableBrass) {
			createBlocksFull(Materials.brass);
		}

		if (Options.enableBronze) {
			createBlocksFull(Materials.bronze);
		}

		if (Options.enableCharcoal) {
			createBlock(Materials.vanilla_charcoal);
		}

		if (Options.enableCoal) {
			final MMDMaterial material = Materials.vanilla_coal;

			material.addNewBlock( Names.BLOCK, net.minecraft.init.Blocks.COAL_BLOCK );
			material.addNewBlock( Names.ORE, net.minecraft.init.Blocks.COAL_ORE );
		}

		if (Options.enableColdIron) {
			createBlocksFull(Materials.coldiron);
		}

		if (Options.enableCopper) {
			createBlocksFull(Materials.copper);
		}

		if (Options.enableCupronickel) {
			createBlocksFull(Materials.cupronickel);
		}

		if (Options.enableDiamond) {
			final MMDMaterial material = Materials.vanilla_diamond;

			material.addNewBlock( Names.BLOCK, net.minecraft.init.Blocks.DIAMOND_BLOCK);
			material.addNewBlock( Names.ORE, net.minecraft.init.Blocks.DIAMOND_ORE);

			createBars(material);
			createDoor(material);
			createTrapDoor(material);

			createBlocksAdditional(material);
		}

		if (Options.enableElectrum) {
			createBlocksFull(Materials.electrum);
		}

		if (Options.enableEmerald) {
			final MMDMaterial material = Materials.vanilla_emerald;

			material.addNewBlock( Names.BLOCK, net.minecraft.init.Blocks.EMERALD_BLOCK);
			material.addNewBlock( Names.ORE, net.minecraft.init.Blocks.EMERALD_ORE);

			createBars(material);
			createDoor(material);
			createTrapDoor(material);

			createBlocksAdditional(material);
		}

		if (Options.enableGold) {
			final MMDMaterial material = Materials.vanilla_gold;

			material.addNewBlock( Names.BLOCK, net.minecraft.init.Blocks.GOLD_BLOCK);
			material.addNewBlock( Names.ORE, net.minecraft.init.Blocks.GOLD_ORE);
			material.addNewBlock( Names.PRESSUREPLATE, net.minecraft.init.Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE );

			createPlate(material);
			createBars(material);
			createDoor(material);
			createTrapDoor(material);

			createBlocksAdditional(material);
		}

		if (Options.enableInvar) {
			createBlocksFull(Materials.invar);
		}

		if (Options.enableIron) {
			final MMDMaterial material = Materials.vanilla_iron;

			material.addNewBlock( Names.BLOCK, net.minecraft.init.Blocks.IRON_BLOCK);
			material.addNewBlock( Names.ORE, net.minecraft.init.Blocks.IRON_ORE);
			material.addNewBlock( Names.BARS, net.minecraft.init.Blocks.IRON_BARS );
			material.addNewBlock( Names.DOORBLOCK, net.minecraft.init.Blocks.IRON_DOOR );
			material.addNewBlock( Names.TRAPDOOR, net.minecraft.init.Blocks.IRON_TRAPDOOR );
			material.addNewBlock( Names.PRESSUREPLATE, net.minecraft.init.Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE );

			createPlate(material);

			createBlocksAdditional(material);
		}

		if (Options.enableLapis) {
			final MMDMaterial material = Materials.vanilla_lapis;

			material.addNewBlock( Names.BLOCK, net.minecraft.init.Blocks.LAPIS_BLOCK);
			material.addNewBlock( Names.ORE, net.minecraft.init.Blocks.LAPIS_ORE);
		}

		if (Options.enableLead) {
			createBlocksFull(Materials.lead);
		}

		if (Options.enableMercury) {
			createOre(Materials.mercury);
			// TODO: Use this info in the Material
			// Materials.mercury.ore.setHardness(3.0f).setResistance(5.0f);
		}

		if (Options.enableMithril) {
			createBlocksFull(Materials.mithril);
		}

		if (Options.enableNickel) {
			createBlocksFull(Materials.nickel);
		}

		if (Options.enableObsidian) {
			final MMDMaterial material = Materials.vanilla_obsidian;

			material.addNewBlock( Names.BLOCK, net.minecraft.init.Blocks.OBSIDIAN);

			createBars(material);
			createDoor(material);
			createTrapDoor(material);

			createBlocksAdditional(material);
		}

		if (Options.enablePewter) {
			createBlocksFull(Materials.pewter);
		}

		if (Options.enablePlatinum) {
			createBlocksFull(Materials.platinum);
		}

		if (Options.enableQuartz) {
			final MMDMaterial material = Materials.vanilla_quartz;

			material.addNewBlock( Names.BLOCK, net.minecraft.init.Blocks.QUARTZ_BLOCK);
			material.addNewBlock( Names.ORE, net.minecraft.init.Blocks.QUARTZ_ORE);
			// material.halfSlab = ;
			// material.doubleSlab = ;
			// material.stairs = ;

			createBars(material);
			createDoor(material);
			createTrapDoor(material);

			createButton(material);
			createLever(material);
			createPressurePlate(material);
			createWall(material);
		}

		if (Options.enableRedstone) {
			final MMDMaterial material = Materials.vanilla_redstone;

			material.addNewBlock( Names.BLOCK, net.minecraft.init.Blocks.REDSTONE_BLOCK);
			material.addNewBlock( Names.ORE, net.minecraft.init.Blocks.REDSTONE_ORE);
		}

		if (Options.enableSilver) {
			createBlocksFull(Materials.silver);
		}

		if (Options.enableStarSteel) {
			createBlocksFull(Materials.starsteel);
			Materials.starsteel.getBlock(Names.BLOCK).setLightLevel(0.5f);
			Materials.starsteel.getBlock(Names.PLATE).setLightLevel(0.5f);
			Materials.starsteel.getBlock(Names.ORE).setLightLevel(0.5f);
			Materials.starsteel.getBlock(Names.BARS).setLightLevel(0.5f);
			Materials.starsteel.getBlock(Names.DOORBLOCK).setLightLevel(0.5f);
			Materials.starsteel.getBlock(Names.TRAPDOOR).setLightLevel(0.5f);
		}

		if (Options.enableSteel) {
			createBlocksFull(Materials.steel);
		}

		if (Options.enableStone) {
			// Stub
		}

		if (Options.enableTin) {
			createBlocksFull(Materials.tin);
		}

		if (Options.enableWood) {
			// Stub
		}

		if (Options.enableZinc) {
			createBlocksFull(Materials.zinc);
		}

		human_detector = addBlock(new BlockHumanDetector(), "human_detector", null, ItemGroups.blocksTab);

		initDone = true;
	}
}
