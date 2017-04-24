package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.fuels.FuelRegistry;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.TabContainer;
import com.mcmoddev.basemetals.init.ItemGroups;

import net.minecraft.item.ItemStack;

/**
 * This class initializes all items in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	private static boolean initDone = false;
	private static TabContainer myTabs = new TabContainer( ItemGroups.blocksTab, ItemGroups.itemsTab, ItemGroups.toolsTab );
	
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
			createItemsFull(Materials.adamantine, myTabs);
			createItemsModSupport(Materials.adamantine, myTabs);
		}

		if (Options.enableAntimony) {
			createItemsFull(Materials.antimony, myTabs);
			createItemsModSupport(Materials.antimony, myTabs);
		}

		if (Options.enableAquarium) {
			createItemsFull(Materials.aquarium, myTabs);
		}

		if (Options.enableBismuth) {
			createItemsFull(Materials.bismuth, myTabs);
			createItemsModSupport(Materials.bismuth, myTabs);
		}

		if (Options.enableBrass) {
			createItemsFull(Materials.brass, myTabs);
		}

		if (Options.enableBronze) {
			createItemsFull(Materials.bronze, myTabs);
		}

		if (Options.enableCharcoal) {
			final MMDMaterial material = Materials.vanillaCharcoal;
			material.addNewItem(Names.INGOT, new ItemStack(net.minecraft.init.Items.COAL, 1, 1).getItem());

			createNugget(material, myTabs.itemsTab);
			createPowder(material, myTabs.itemsTab);
			createSmallPowder(material, myTabs.itemsTab);
			FuelRegistry.addFuel(Oredicts.NUGGET+"_charcoal", 200);
			FuelRegistry.addFuel(Oredicts.DUST_CHARCOAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.BLOCK_CHARCOAL, 16000);
		}

		if (Options.enableCoal) {
			final MMDMaterial material = Materials.vanillaCoal;
			material.addNewItem(Names.INGOT, net.minecraft.init.Items.COAL);

			createNugget(material, myTabs.itemsTab);
			createPowder(material, myTabs.itemsTab);
			createSmallPowder(material, myTabs.itemsTab);
			FuelRegistry.addFuel(Oredicts.NUGGET+"_coal", 200);
			FuelRegistry.addFuel(Oredicts.DUST_COAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_COAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_COAL, 200);
		}

		if (Options.enableColdIron) {
			createItemsFull(Materials.coldiron, myTabs);
			createItemsModSupport(Materials.coldiron, myTabs);
		}

		if (Options.enableCopper) {
			createItemsFull(Materials.copper, myTabs);
		}

		if (Options.enableCupronickel) {
			createItemsFull(Materials.cupronickel, myTabs);
		}

		if (Options.enableDiamond) {
			final MMDMaterial material = Materials.vanillaDiamond;

			material.addNewItem(Names.AXE, net.minecraft.init.Items.DIAMOND_AXE);
			material.addNewItem(Names.HOE, net.minecraft.init.Items.DIAMOND_HOE);
			material.addNewItem(Names.HORSEARMOR, net.minecraft.init.Items.DIAMOND_HORSE_ARMOR);
			material.addNewItem(Names.PICKAXE, net.minecraft.init.Items.DIAMOND_PICKAXE);
			material.addNewItem(Names.SHOVEL, net.minecraft.init.Items.DIAMOND_SHOVEL);
			material.addNewItem(Names.SWORD, net.minecraft.init.Items.DIAMOND_SWORD);
			material.addNewItem(Names.BOOTS, net.minecraft.init.Items.DIAMOND_BOOTS);
			material.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.DIAMOND_CHESTPLATE);
			material.addNewItem(Names.HELMET, net.minecraft.init.Items.DIAMOND_HELMET);
			material.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.DIAMOND_LEGGINGS);
			material.addNewItem(Names.INGOT, net.minecraft.init.Items.DIAMOND);

			createItemsFull(material, myTabs);
		}

		if (Options.enableEmerald) {
			final MMDMaterial material = Materials.vanillaEmerald;

			material.addNewItem(Names.INGOT, net.minecraft.init.Items.EMERALD);

			createItemsFull(material, myTabs);
		}

		if (Options.enableElectrum) {
			createItemsFull(Materials.electrum, myTabs);
		}

		if (Options.enableGold) {
			final MMDMaterial material = Materials.vanillaGold;

			material.addNewItem(Names.AXE, net.minecraft.init.Items.GOLDEN_AXE);
			material.addNewItem(Names.HOE, net.minecraft.init.Items.GOLDEN_HOE);
			material.addNewItem(Names.HORSEARMOR, net.minecraft.init.Items.GOLDEN_HORSE_ARMOR);
			material.addNewItem(Names.PICKAXE, net.minecraft.init.Items.GOLDEN_PICKAXE);
			material.addNewItem(Names.SHOVEL, net.minecraft.init.Items.GOLDEN_SHOVEL);
			material.addNewItem(Names.SWORD, net.minecraft.init.Items.GOLDEN_SWORD);
			material.addNewItem(Names.BOOTS, net.minecraft.init.Items.GOLDEN_BOOTS);
			material.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.GOLDEN_CHESTPLATE);
			material.addNewItem(Names.HELMET, net.minecraft.init.Items.GOLDEN_HELMET);
			material.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.GOLDEN_LEGGINGS);
			material.addNewItem(Names.INGOT, net.minecraft.init.Items.GOLD_INGOT);
			material.addNewItem(Names.NUGGET, net.minecraft.init.Items.GOLD_NUGGET);

			createItemsFull(material, myTabs);
		}

		if (Options.enableInvar) {
			createItemsFull(Materials.invar, myTabs);
		}

		if (Options.enableIron) {
			final MMDMaterial material = Materials.vanillaIron;
			material.addNewItem(Names.AXE, net.minecraft.init.Items.IRON_AXE);
			material.addNewItem(Names.DOOR, net.minecraft.init.Items.IRON_DOOR);
			material.addNewItem(Names.HOE, net.minecraft.init.Items.IRON_HOE);
			material.addNewItem(Names.HORSEARMOR, net.minecraft.init.Items.IRON_HORSE_ARMOR);
			material.addNewItem(Names.PICKAXE, net.minecraft.init.Items.IRON_PICKAXE);
			material.addNewItem(Names.SHOVEL, net.minecraft.init.Items.IRON_SHOVEL);
			material.addNewItem(Names.SWORD, net.minecraft.init.Items.IRON_SWORD);
			material.addNewItem(Names.BOOTS, net.minecraft.init.Items.IRON_BOOTS);
			material.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.IRON_CHESTPLATE);
			material.addNewItem(Names.HELMET, net.minecraft.init.Items.IRON_HELMET);
			material.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.IRON_LEGGINGS);
			material.addNewItem(Names.DOOR, net.minecraft.init.Items.IRON_DOOR);
			material.addNewItem(Names.INGOT, net.minecraft.init.Items.IRON_INGOT);
			material.addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);
			// material.addNewItem(Names.NUGGET, net.minecraft.init.Items.IRON_NUGGET); // Not till after 1.11

			createItemsFull(material, myTabs);
		}

		if (Options.enableLapis) {
			final MMDMaterial material = Materials.vanillaLapis;
			material.addNewItem(Names.POWDER, net.minecraft.init.Items.DYE);

			createSmallPowder(material, myTabs.itemsTab);

		}

		if (Options.enableLead) {
			createItemsFull(Materials.lead, myTabs);
		}

		if (Options.enableObsidian) {
			final MMDMaterial material = Materials.vanillaObsidian;
			createItemsFull(material, myTabs);
		}

		if (Options.enablePlatinum) {
			createItemsFull(Materials.platinum, myTabs);
			createItemsModSupport(Materials.platinum, myTabs);
		}

		if (Options.enableMercury) {
			final MMDMaterial material = Materials.mercury;

			createIngot(material, myTabs.itemsTab);
			createNugget(material, myTabs.itemsTab);
			createPowder(material, myTabs.itemsTab);
			createSmallPowder(material, myTabs.itemsTab);
		}

		if (Options.enableMithril) {
			createItemsFull(Materials.mithril, myTabs);
		}

		if (Options.enableNickel) {
			createItemsFull(Materials.nickel, myTabs);
			createItemsModSupport(Materials.nickel, myTabs);
		}

		if (Options.enablePewter) {
			createItemsFull(Materials.pewter, myTabs);
		}

		if (Options.enableRedstone) {
			final MMDMaterial material = Materials.vanillaRedstone;
			material.addNewItem(Names.POWDER, net.minecraft.init.Items.REDSTONE);

			createIngot(material, myTabs.itemsTab);
			createSmallPowder(material, myTabs.itemsTab);
		}

		if (Options.enableQuartz) {
			final MMDMaterial material = Materials.vanillaQuartz;
			material.addNewItem( Names.INGOT, net.minecraft.init.Items.QUARTZ);
			// material.slab = ;

			createItemsFull(material, myTabs);
		}

		if (Options.enableSilver) {
			createItemsFull(Materials.silver, myTabs);
		}

		if (Options.enableStarSteel) {
			createItemsFull(Materials.starsteel, myTabs);
			createItemsModSupport(Materials.starsteel, myTabs);
		}

		if (Options.enableStone) {
			final MMDMaterial material = Materials.vanillaStone;

			material.addNewItem(Names.AXE, net.minecraft.init.Items.STONE_AXE);
			material.addNewItem(Names.HOE, net.minecraft.init.Items.STONE_HOE);
			material.addNewItem(Names.PICKAXE, net.minecraft.init.Items.STONE_PICKAXE);
			material.addNewItem(Names.SHOVEL, net.minecraft.init.Items.STONE_SHOVEL);
			material.addNewItem(Names.SWORD, net.minecraft.init.Items.STONE_SWORD);
			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.STONE);
			material.addNewBlock(Names.HALFSLAB, net.minecraft.init.Blocks.STONE_SLAB);
			material.addNewBlock(Names.DOUBLESLAB, net.minecraft.init.Blocks.DOUBLE_STONE_SLAB);
			material.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.STONE_STAIRS);

			createCrackhammer(material, myTabs.toolsTab);
			createRod(material, myTabs.itemsTab);
			createGear(material, myTabs.itemsTab);
		}

		if (Options.enableSteel) {
			createItemsFull(Materials.steel, myTabs);
		}

		if (Options.enableTin) {
			createItemsFull(Materials.tin, myTabs);
		}

		if (Options.enableWood) {
			final MMDMaterial material = Materials.vanillaWood;

			material.addNewItem(Names.AXE, net.minecraft.init.Items.WOODEN_AXE);
			material.addNewItem(Names.DOOR, net.minecraft.init.Items.OAK_DOOR);
			material.addNewItem(Names.HOE, net.minecraft.init.Items.WOODEN_HOE);
			material.addNewItem(Names.PICKAXE, net.minecraft.init.Items.WOODEN_PICKAXE);
			material.addNewItem(Names.SHOVEL, net.minecraft.init.Items.WOODEN_SHOVEL);
			material.addNewItem(Names.SWORD, net.minecraft.init.Items.WOODEN_SWORD);
			material.addNewBlock(Names.DOORBLOCK, net.minecraft.init.Blocks.OAK_DOOR);
			material.addNewBlock(Names.ORE, net.minecraft.init.Blocks.LOG);
			material.addNewBlock(Names.TRAPDOOR, net.minecraft.init.Blocks.TRAPDOOR);
			material.addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);
			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.PLANKS);
			material.addNewBlock(Names.HALFSLAB, net.minecraft.init.Blocks.WOODEN_SLAB);
			material.addNewBlock(Names.DOUBLESLAB, net.minecraft.init.Blocks.DOUBLE_WOODEN_SLAB);
			material.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.OAK_STAIRS);

			createCrackhammer(material, myTabs.toolsTab);
			createGear(material, myTabs.itemsTab);
		}

		if (Options.enableZinc) {
			createItemsFull(Materials.zinc, myTabs);
			createItemsModSupport(Materials.zinc, myTabs);
		}

		addToMetList(); // TODO

		initDone = true;
	}
}
