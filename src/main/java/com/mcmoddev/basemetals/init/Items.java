package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.fuels.FuelRegistry;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.ItemStack;

/**
 * This class initializes all items in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	private static boolean initDone = false;

	protected Items() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Blocks.init();
		com.mcmoddev.lib.init.Items.init(); // TODO

		if (Options.enableAdamantine) {
			createItemsFull(Materials.adamantine);
			createItemsModSupport(Materials.adamantine);
		}

		if (Options.enableAntimony) {
			createItemsFull(Materials.antimony);
			createItemsModSupport(Materials.antimony);
		}

		if (Options.enableAquarium) {
			createItemsFull(Materials.aquarium);
		}

		if (Options.enableBismuth) {
			createItemsFull(Materials.bismuth);
			createItemsModSupport(Materials.bismuth);
		}

		if (Options.enableBrass) {
			createItemsFull(Materials.brass);
		}

		if (Options.enableBronze) {
			createItemsFull(Materials.bronze);
		}

		if (Options.enableCharcoal) {
			final MetalMaterial material = Materials.vanilla_charcoal;
			material.ingot = new ItemStack(net.minecraft.init.Items.COAL, 1, 1).getItem();

			createNugget(material);
			createPowder(material);
			createSmallPowder(material);
			FuelRegistry.addFuel(Oredicts.NUGGETCHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUSTCHARCOAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUSTSMALLCHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUSTTINYCHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.BLOCK+"Charcoal", 16000);
		}

		if (Options.enableCoal) {
			final MetalMaterial material = Materials.vanilla_coal;
			material.ingot = net.minecraft.init.Items.COAL;

			createNugget(material);
			createPowder(material);
			createSmallPowder(material);
			FuelRegistry.addFuel(Oredicts.NUGGETCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUSTCOAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUSTSMALLCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUSTTINYCOAL, 200);
		}

		if (Options.enableColdIron) {
			createItemsFull(Materials.coldiron);
			createItemsModSupport(Materials.coldiron);
		}

		if (Options.enableCopper) {
			createItemsFull(Materials.copper);
		}

		if (Options.enableCupronickel) {
			createItemsFull(Materials.cupronickel);
		}

		if (Options.enableDiamond) {
			final MetalMaterial material = Materials.vanilla_diamond;

			material.axe = net.minecraft.init.Items.DIAMOND_AXE;
			material.hoe = net.minecraft.init.Items.DIAMOND_HOE;
			material.horseArmor = net.minecraft.init.Items.DIAMOND_HORSE_ARMOR;
			material.pickaxe = net.minecraft.init.Items.DIAMOND_PICKAXE;
			material.shovel = net.minecraft.init.Items.DIAMOND_SHOVEL;
			material.sword = net.minecraft.init.Items.DIAMOND_SWORD;
			material.boots = net.minecraft.init.Items.DIAMOND_BOOTS;
			material.chestplate = net.minecraft.init.Items.DIAMOND_CHESTPLATE;
			material.helmet = net.minecraft.init.Items.DIAMOND_HELMET;
			material.leggings = net.minecraft.init.Items.DIAMOND_LEGGINGS;
			material.ingot = net.minecraft.init.Items.DIAMOND;

			createItemsFull(material);
		}

		if (Options.enableEmerald) {
			final MetalMaterial material = Materials.vanilla_emerald;

			material.ingot = net.minecraft.init.Items.EMERALD;

			createItemsFull(material);
		}

		if (Options.enableElectrum) {
			createItemsFull(Materials.electrum);
		}

		if (Options.enableGold) {
			final MetalMaterial material = Materials.vanilla_gold;

			material.axe = net.minecraft.init.Items.GOLDEN_AXE;
			material.hoe = net.minecraft.init.Items.GOLDEN_HOE;
			material.horseArmor = net.minecraft.init.Items.GOLDEN_HORSE_ARMOR;
			material.pickaxe = net.minecraft.init.Items.GOLDEN_PICKAXE;
			material.shovel = net.minecraft.init.Items.GOLDEN_SHOVEL;
			material.sword = net.minecraft.init.Items.GOLDEN_SWORD;
			material.boots = net.minecraft.init.Items.GOLDEN_BOOTS;
			material.chestplate = net.minecraft.init.Items.GOLDEN_CHESTPLATE;
			material.helmet = net.minecraft.init.Items.GOLDEN_HELMET;
			material.leggings = net.minecraft.init.Items.GOLDEN_LEGGINGS;
			material.ingot = net.minecraft.init.Items.GOLD_INGOT;
			material.nugget = net.minecraft.init.Items.GOLD_NUGGET;

			createItemsFull(material);
		}

		if (Options.enableInvar) {
			createItemsFull(Materials.invar);
		}

		if (Options.enableIron) {
			final MetalMaterial material = Materials.vanilla_iron;
			material.axe = net.minecraft.init.Items.IRON_AXE;
			material.door = net.minecraft.init.Items.IRON_DOOR;
			material.hoe = net.minecraft.init.Items.IRON_HOE;
			material.horseArmor = net.minecraft.init.Items.IRON_HORSE_ARMOR;
			material.pickaxe = net.minecraft.init.Items.IRON_PICKAXE;
			material.shovel = net.minecraft.init.Items.IRON_SHOVEL;
			material.sword = net.minecraft.init.Items.IRON_SWORD;
			material.boots = net.minecraft.init.Items.IRON_BOOTS;
			material.chestplate = net.minecraft.init.Items.IRON_CHESTPLATE;
			material.helmet = net.minecraft.init.Items.IRON_HELMET;
			material.leggings = net.minecraft.init.Items.IRON_LEGGINGS;
			material.door = net.minecraft.init.Items.IRON_DOOR;
			material.ingot = net.minecraft.init.Items.IRON_INGOT;
			material.shears = net.minecraft.init.Items.SHEARS;
			// material.nugget = net.minecraft.init.Items.IRON_NUGGET; // Not till after 1.11

			createItemsFull(material);
		}

		if (Options.enableLapis) {
			final MetalMaterial material = Materials.vanilla_lapis;
			material.powder = net.minecraft.init.Items.DYE;

			createSmallPowder(material);

		}

		if (Options.enableLead) {
			createItemsFull(Materials.lead);
		}

		if (Options.enableObsidian) {
			final MetalMaterial material = Materials.vanilla_obsidian;
			createItemsFull(material);
		}

		if (Options.enablePlatinum) {
			createItemsFull(Materials.platinum);
			createItemsModSupport(Materials.platinum);
		}

		if (Options.enableMercury) {
			final MetalMaterial material = Materials.mercury;

			createIngot(material);
			createNugget(material);
			createPowder(material);
			createSmallPowder(material);
		}

		if (Options.enableMithril) {
			createItemsFull(Materials.mithril);
		}

		if (Options.enableNickel) {
			createItemsFull(Materials.nickel);
			createItemsModSupport(Materials.nickel);
		}

		if (Options.enablePewter) {
			createItemsFull(Materials.pewter);
		}

		if (Options.enableRedstone) {
			final MetalMaterial material = Materials.vanilla_obsidian;
			material.powder = net.minecraft.init.Items.REDSTONE;

			createIngot(material);
			createSmallPowder(material);
		}

		if (Options.enableQuartz) {
			final MetalMaterial material = Materials.vanilla_quartz;
			material.ingot = net.minecraft.init.Items.QUARTZ;
			// material.slab = ;

			createItemsFull(material);
		}

		if (Options.enableSilver) {
			createItemsFull(Materials.silver);
		}

		if (Options.enableStarSteel) {
			createItemsFull(Materials.starsteel);
			createItemsModSupport(Materials.starsteel);
		}

		if (Options.enableStone) {
			final MetalMaterial material = Materials.vanilla_stone;

			material.axe = net.minecraft.init.Items.STONE_AXE;
			material.hoe = net.minecraft.init.Items.STONE_HOE;
			material.pickaxe = net.minecraft.init.Items.STONE_PICKAXE;
			material.shovel = net.minecraft.init.Items.STONE_SHOVEL;
			material.sword = net.minecraft.init.Items.STONE_SWORD;
			material.block = net.minecraft.init.Blocks.STONE;
			material.halfSlab = net.minecraft.init.Blocks.STONE_SLAB;
			material.doubleSlab = net.minecraft.init.Blocks.DOUBLE_STONE_SLAB;
			material.stairs = net.minecraft.init.Blocks.STONE_STAIRS;

			createCrackhammer(material);
			createRod(material);
			createGear(material);
		}

		if (Options.enableSteel) {
			createItemsFull(Materials.steel);
		}

		if (Options.enableTin) {
			createItemsFull(Materials.tin);
		}

		if (Options.enableWood) {
			final MetalMaterial material = Materials.vanilla_wood;

			material.axe = net.minecraft.init.Items.WOODEN_AXE;
			material.door = net.minecraft.init.Items.OAK_DOOR;
			material.hoe = net.minecraft.init.Items.WOODEN_HOE;
			material.pickaxe = net.minecraft.init.Items.WOODEN_PICKAXE;
			material.shovel = net.minecraft.init.Items.WOODEN_SHOVEL;
			material.sword = net.minecraft.init.Items.WOODEN_SWORD;
			material.doorBlock = net.minecraft.init.Blocks.OAK_DOOR;
			material.ore = net.minecraft.init.Blocks.LOG;
			material.trapdoor = net.minecraft.init.Blocks.TRAPDOOR;
			material.shears = net.minecraft.init.Items.SHEARS;
			material.block = net.minecraft.init.Blocks.PLANKS;
			material.halfSlab = net.minecraft.init.Blocks.WOODEN_SLAB;
			material.doubleSlab = net.minecraft.init.Blocks.DOUBLE_WOODEN_SLAB;
			material.stairs = net.minecraft.init.Blocks.OAK_STAIRS;

			createCrackhammer(material);
			createGear(material);
		}

		if (Options.enableZinc) {
			createItemsFull(Materials.zinc);
			createItemsModSupport(Materials.zinc);
		}

		addToMetList(); // TODO

		initDone = true;
	}
}
