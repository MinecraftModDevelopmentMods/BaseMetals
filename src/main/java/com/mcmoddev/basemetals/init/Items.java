package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.fuels.FuelRegistry;
import com.mcmoddev.lib.material.MMDMaterial;
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
			createItemsFull(Materials.getMaterialByName(MaterialNames.ADAMANTINE));
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.ADAMANTINE));
		}

		if (Options.enableAntimony) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.ANTIMONY));
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.ANTIMONY));
		}

		if (Options.enableAquarium) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.AQUARIUM));
		}

		if (Options.enableBismuth) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.BISMUTH));
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.BISMUTH));
		}

		if (Options.enableBrass) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.BRASS));
		}

		if (Options.enableBronze) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.BRONZE));
		}

		if (Options.enableCharcoal) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.CHARCOAL);
			material.addNewItem(Names.INGOT, new ItemStack(net.minecraft.init.Items.COAL, 1, 1).getItem());

			createNugget(material);
			createPowder(material);
			createSmallPowder(material);
			FuelRegistry.addFuel(Oredicts.NUGGET_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_CHARCOAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.BLOCK_CHARCOAL, 16000);
		}

		if (Options.enableCoal) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.COAL);
			material.addNewItem(Names.INGOT, net.minecraft.init.Items.COAL);

			createNugget(material);
			createPowder(material);
			createSmallPowder(material);
			FuelRegistry.addFuel(Oredicts.NUGGET_COAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_COAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_COAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_COAL, 200);
		}

		if (Options.enableColdIron) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.COLDIRON));
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.COLDIRON));
		}

		if (Options.enableCopper) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.COPPER));
		}

		if (Options.enableCupronickel) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.CUPRONICKEL));
		}

		if (Options.enableDiamond) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.DIAMOND);

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

			createItemsFull(material);
		}

		if (Options.enableEmerald) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.EMERALD);

			material.addNewItem(Names.INGOT, net.minecraft.init.Items.EMERALD);

			createItemsFull(material);
		}

		if (Options.enableElectrum) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.ELECTRUM));
		}

		if (Options.enableGold) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.GOLD);

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

			createItemsFull(material);
		}

		if (Options.enableInvar) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.INVAR));
		}

		if (Options.enableIron) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.IRON);
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

			createItemsFull(material);
		}

		if (Options.enableLapis) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.LAPIS);
			material.addNewItem(Names.POWDER, net.minecraft.init.Items.DYE);

			createSmallPowder(material);

		}

		if (Options.enableLead) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.LEAD));
		}

		if (Options.enableObsidian) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.OBSIDIAN);
			createItemsFull(material);
		}

		if (Options.enablePlatinum) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.PLATINUM));
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.PLATINUM));
		}

		if (Options.enableMercury) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.MERCURY);

			createIngot(material);
			createNugget(material);
			createPowder(material);
			createSmallPowder(material);
		}

		if (Options.enableMithril) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.MITHRIL));
		}

		if (Options.enableNickel) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.NICKEL));
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.NICKEL));
		}

		if (Options.enablePewter) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.PEWTER));
		}

		if (Options.enableRedstone) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.REDSTONE);
			material.addNewItem(Names.POWDER, net.minecraft.init.Items.REDSTONE);

			createIngot(material);
			createSmallPowder(material);
		}

		if (Options.enableQuartz) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.QUARTZ);
			material.addNewItem( Names.INGOT, net.minecraft.init.Items.QUARTZ);
			// material.slab = ;

			createItemsFull(material);
		}

		if (Options.enableSilver) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.SILVER));
		}

		if (Options.enableStarSteel) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.STARSTEEL));
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.STARSTEEL));
		}

		if (Options.enableStone) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.STONE);

			material.addNewItem(Names.AXE, net.minecraft.init.Items.STONE_AXE);
			material.addNewItem(Names.HOE, net.minecraft.init.Items.STONE_HOE);
			material.addNewItem(Names.PICKAXE, net.minecraft.init.Items.STONE_PICKAXE);
			material.addNewItem(Names.SHOVEL, net.minecraft.init.Items.STONE_SHOVEL);
			material.addNewItem(Names.SWORD, net.minecraft.init.Items.STONE_SWORD);
			material.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.STONE);
			material.addNewBlock(Names.HALFSLAB, net.minecraft.init.Blocks.STONE_SLAB);
			material.addNewBlock(Names.DOUBLESLAB, net.minecraft.init.Blocks.DOUBLE_STONE_SLAB);
			material.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.STONE_STAIRS);

			createCrackhammer(material);
			createRod(material);
			createGear(material);
		}

		if (Options.enableSteel) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.STEEL));
		}

		if (Options.enableTin) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.TIN));
		}

		if (Options.enableWood) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.WOOD);

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

			createCrackhammer(material);
			createGear(material);
		}

		if (Options.enableZinc) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.ZINC));
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.ZINC));
		}

		addToMetList(); // TODO

		initDone = true;
	}
}
