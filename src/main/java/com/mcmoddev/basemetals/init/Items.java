package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import cyano.basemetals.init.Materials;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This class initializes all items in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	public static Item charcoal_powder;
	public static Item charcoal_smallpowder;

	public static Item coal_powder;
	public static Item coal_smallpowder;

	public static Item mercury_ingot;
	public static Item mercury_nugget;
	public static Item mercury_powder;
	public static Item mercury_smallpowder;

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Blocks.init();
		com.mcmoddev.lib.init.Items.init();

		MetalMaterial material;

		if (Options.ENABLE_ADAMANTINE) {
			material = Materials.adamantine;
			createItemsFull(material);
			createItemsModSupport(material);
		}

		if (Options.ENABLE_ANTIMONY) {
			material = Materials.antimony;
			createItemsFull(material);
			createItemsModSupport(material);
		}

		if (Options.ENABLE_AQUARIUM) {
			material = Materials.aquarium;
			createItemsFull(material);
		}

		if (Options.ENABLE_BISMUTH) {
			material = Materials.bismuth;
			createItemsFull(material);
			createItemsModSupport(material);
		}

		if (Options.ENABLE_BRASS) {
			material = Materials.brass;
			createItemsFull(material);
		}

		if (Options.ENABLE_BRONZE) {
			material = Materials.bronze;
			createItemsFull(material);
		}

		if (Options.ENABLE_CHARCOAL) {
			charcoal_powder = addItem(new Item(), "charcoal_powder", null, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.DUSTCHARCOAL, charcoal_powder);

			charcoal_smallpowder = addItem(new Item(), "charcoal_smallpowder", null, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.DUSTTINYCHARCOAL, charcoal_smallpowder);
			OreDictionary.registerOre(Oredicts.DUSTSMALLCHARCOAL, charcoal_smallpowder);
		}

		if (Options.ENABLE_COAL) {
			coal_powder = addItem(new Item(), "coal_powder", null, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.DUSTCOAL, coal_powder);

			coal_smallpowder = addItem(new Item(), "coal_smallpowder", null, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.DUSTTINYCOAL, coal_smallpowder);
			OreDictionary.registerOre(Oredicts.DUSTSMALLCOAL, coal_smallpowder);
		}

		if (Options.ENABLE_COLDIRON) {
			material = Materials.coldiron;
			createItemsFull(material);
			createItemsModSupport(material);
		}

		if (Options.ENABLE_COPPER) {
			material = Materials.copper;
			createItemsFull(material);
		}

		if (Options.ENABLE_CUPRONICKEL) {
			material = Materials.cupronickel;
			createItemsFull(material);
		}

		if (Options.ENABLE_DIAMOND) {
			material = Materials.vanilla_diamond;

			material.axe = net.minecraft.init.Items.DIAMOND_AXE;
//			material.door = net.minecraft.init.Items.DIAMOND_DOOR;
			material.hoe = net.minecraft.init.Items.DIAMOND_HOE;
			material.horse_armor = net.minecraft.init.Items.DIAMOND_HORSE_ARMOR;
			material.pickaxe = net.minecraft.init.Items.DIAMOND_PICKAXE;
			material.shovel = net.minecraft.init.Items.DIAMOND_SHOVEL;
			material.sword = net.minecraft.init.Items.DIAMOND_SWORD;
			material.boots = net.minecraft.init.Items.DIAMOND_BOOTS;
			material.chestplate = net.minecraft.init.Items.DIAMOND_CHESTPLATE;
			material.helmet = net.minecraft.init.Items.DIAMOND_HELMET;
			material.leggings = net.minecraft.init.Items.DIAMOND_LEGGINGS;
//			material.doorBlock = net.minecraft.init.Blocks.DIAMOND_DOOR;
			material.ore = net.minecraft.init.Blocks.DIAMOND_ORE;
//			material.trapdoor = net.minecraft.init.Blocks.DIAMOND_TRAPDOOR;
			material.ingot = net.minecraft.init.Items.DIAMOND;
//			material.nugget = net.minecraft.init.Items.DIAMOND_NUGGET;
//			material.bars = net.minecraft.init.Blocks.DIAMOND_BARS;
			material.block = net.minecraft.init.Blocks.DIAMOND_BLOCK;

			createArrow(material);
			createBolt(material);
			createBow(material);
			createCrossbow(material);
			createFishingRod(material);
			createShears(material);
			createCrackhammer(material);
			createDoor(material);
			createGear(material);
			//diamond_rod = createRod(material);

			createSlab(material);
		}

		if (Options.ENABLE_ELECTRUM) {
			material = Materials.electrum;
			createItemsFull(material);
		}

		if (Options.ENABLE_GOLD) {
			material = Materials.vanilla_gold;

			material.axe = net.minecraft.init.Items.GOLDEN_AXE;
//			material.door = net.minecraft.init.Items.GOLDEN_DOOR;
			material.hoe = net.minecraft.init.Items.GOLDEN_HOE;
			material.horse_armor = net.minecraft.init.Items.GOLDEN_HORSE_ARMOR;
			material.pickaxe = net.minecraft.init.Items.GOLDEN_PICKAXE;
			material.shovel = net.minecraft.init.Items.GOLDEN_SHOVEL;
			material.sword = net.minecraft.init.Items.GOLDEN_SWORD;
			material.boots = net.minecraft.init.Items.GOLDEN_BOOTS;
			material.chestplate = net.minecraft.init.Items.GOLDEN_CHESTPLATE;
			material.helmet = net.minecraft.init.Items.GOLDEN_HELMET;
			material.leggings = net.minecraft.init.Items.GOLDEN_LEGGINGS;
//			material.doorBlock = net.minecraft.init.Blocks.GOLDEN_DOOR;
			material.ore = net.minecraft.init.Blocks.GOLD_ORE;
//			material.trapdoor = net.minecraft.init.Blocks.GOLDEN_TRAPDOOR;

			material.ingot = net.minecraft.init.Items.GOLD_INGOT;
			material.nugget = net.minecraft.init.Items.GOLD_NUGGET;
//			material.bars = net.minecraft.init.Blocks.GOLD_BARS;
			material.block = net.minecraft.init.Blocks.GOLD_BLOCK;

			createArrow(material);
			createBolt(material);
			createBow(material);
			createCrossbow(material);
			createFishingRod(material);
			createShears(material);
			createCrackhammer(material);
			createDoor(material);
			createPowder(material);
			createSmallPowder(material);
			createRod(material);
			createGear(material);

			createSlab(material);
		}

		if (Options.ENABLE_INVAR) {
			material = Materials.invar;
			createItemsFull(material);
		}

		if (Options.ENABLE_IRON) {
			material = Materials.vanilla_iron;
			material.axe = net.minecraft.init.Items.IRON_AXE;
			material.door = net.minecraft.init.Items.IRON_DOOR;
			material.hoe = net.minecraft.init.Items.IRON_HOE;
			material.horse_armor = net.minecraft.init.Items.IRON_HORSE_ARMOR;
			material.pickaxe = net.minecraft.init.Items.IRON_PICKAXE;
			material.shovel = net.minecraft.init.Items.IRON_SHOVEL;
			material.sword = net.minecraft.init.Items.IRON_SWORD;
			material.boots = net.minecraft.init.Items.IRON_BOOTS;
			material.chestplate = net.minecraft.init.Items.IRON_CHESTPLATE;
			material.helmet = net.minecraft.init.Items.IRON_HELMET;
			material.leggings = net.minecraft.init.Items.IRON_LEGGINGS;
			material.doorBlock = net.minecraft.init.Blocks.IRON_DOOR;
			material.ore = net.minecraft.init.Blocks.IRON_ORE;
			material.trapdoor = net.minecraft.init.Blocks.IRON_TRAPDOOR;
			material.ingot = net.minecraft.init.Items.IRON_INGOT;
			material.shears = net.minecraft.init.Items.SHEARS;
//			material.nugget = net.minecraft.init.Items.IRON_NUGGET; // Not till after 1.11
			material.bars = net.minecraft.init.Blocks.IRON_BARS;
			material.block = net.minecraft.init.Blocks.IRON_BLOCK;
			createArrow(material);
			createBolt(material);
			createBow(material);
			createCrossbow(material);
			createFishingRod(material);
			createCrackhammer(material);
			createGear(material);
			createNugget(material);
			createPowder(material);
			createRod(material);
			createSmallPowder(material);
			
			createSlab(material);
		}

		if (Options.ENABLE_LEAD) {
			material = Materials.lead;
			createItemsFull(material);
		}

		if (Options.ENABLE_PLATINUM) {
			material = Materials.platinum;
			createItemsFull(material);
			createItemsModSupport(material);
		}

		if (Options.ENABLE_MERCURY) {
			// mercury is special
			mercury_ingot = addItem(new Item(), "mercury_ingot", null, ItemGroups.tab_items);
//			itemRegistry.put("mercury_ingot", mercury_ingot);
			OreDictionary.registerOre(Oredicts.INGOTMERCURY, mercury_ingot);
			OreDictionary.registerOre(Oredicts.QUICKSILVER, mercury_ingot);

			mercury_nugget = addItem(new Item(), "mercury_nugget", null, ItemGroups.tab_items);
//			itemRegistry.put("mercury_ingot", mercury_nugget);
			OreDictionary.registerOre(Oredicts.NUGGETMERCURY, mercury_nugget);
//			OreDictionary.registerOre(Oredicts.QUICKSILVER, mercury_nugget);

			mercury_powder = addItem(new Item(), "mercury_powder", null, ItemGroups.tab_items);
//			itemRegistry.put("mercury_powder", mercury_powder);
			OreDictionary.registerOre(Oredicts.DUSTMERCURY, mercury_powder);

			mercury_smallpowder = addItem(new Item(), "mercury_smallpowder", null, ItemGroups.tab_items);
//			itemRegistry.put("mercury_smallpowder", mercury_smallpowder);
			OreDictionary.registerOre(Oredicts.DUSTTINYMERCURY, mercury_smallpowder);
			OreDictionary.registerOre(Oredicts.DUSTSMALLMERCURY, mercury_smallpowder);
		}

		if (Options.ENABLE_MITHRIL) {
			material = Materials.mithril;
			createItemsFull(material);
		}

		if (Options.ENABLE_NICKEL) {
			material = Materials.nickel;
			createItemsFull(material);
			createItemsModSupport(material);
		}

		if (Options.ENABLE_PEWTER) {
			material = Materials.pewter;
			createItemsFull(material);
		}

		if (Options.ENABLE_SILVER) {
			material = Materials.silver;
			createItemsFull(material);
		}

		if (Options.ENABLE_STARSTEEL) {
			material = Materials.starsteel;
			createItemsFull(material);
			createItemsModSupport(material);
		}

		if (Options.ENABLE_STONE) {
			material = Materials.vanilla_stone;

			material.axe = net.minecraft.init.Items.STONE_AXE;
//			material.door = net.minecraft.init.Items.STONE_DOOR;
			material.hoe = net.minecraft.init.Items.STONE_HOE;
//			material.horse_armor = net.minecraft.init.Items.STONE_HORSE_ARMOR;
			material.pickaxe = net.minecraft.init.Items.STONE_PICKAXE;
			material.shovel = net.minecraft.init.Items.STONE_SHOVEL;
			material.sword = net.minecraft.init.Items.STONE_SWORD;
//			material.boots = net.minecraft.init.Items.STONE_BOOTS;
//			material.chestplate = net.minecraft.init.Items.STONE_CHESTPLATE;
//			material.helmet = net.minecraft.init.Items.STONE_HELMET;
//			material.leggings = net.minecraft.init.Items.STONE_LEGGINGS;
//			material.doorBlock = net.minecraft.init.Blocks.STONE_DOOR;
//			material.ore = net.minecraft.init.Blocks.STONE_ORE;
//			material.trapdoor = net.minecraft.init.Blocks.STONE_TRAPDOOR;
//			material.ingot = net.minecraft.init.Items.STONE_INGOT;
//			material.shears = net.minecraft.init.Items.SHEARS;
//			material.nugget = net.minecraft.init.Items.STONE_NUGGET;
//			material.bars = net.minecraft.init.Blocks.STONE_BARS;
			material.block = net.minecraft.init.Blocks.STONE;
//			material.slab = net.minecraft.init.Items.SLAB;
			material.half_slab = net.minecraft.init.Blocks.STONE_SLAB;
			material.double_slab = net.minecraft.init.Blocks.DOUBLE_STONE_SLAB;
			material.stairs = net.minecraft.init.Blocks.STONE_STAIRS;

			createCrackhammer(material);
			createRod(material);
			createGear(material);
		}

		if (Options.ENABLE_STEEL) {
			material = Materials.steel;
			createItemsFull(material);
		}

		if (Options.ENABLE_TIN) {
			material = Materials.tin;
			createItemsFull(material);
		}

		if (Options.ENABLE_WOOD) {
			material = Materials.vanilla_wood;

			material.axe = net.minecraft.init.Items.WOODEN_AXE;
			material.door = net.minecraft.init.Items.OAK_DOOR;
			material.hoe = net.minecraft.init.Items.WOODEN_HOE;
//			material.horse_armor = net.minecraft.init.Items.WOODEN_HORSE_ARMOR;
			material.pickaxe = net.minecraft.init.Items.WOODEN_PICKAXE;
			material.shovel = net.minecraft.init.Items.WOODEN_SHOVEL;
			material.sword = net.minecraft.init.Items.WOODEN_SWORD;
//			material.boots = net.minecraft.init.Items.WOODEN_BOOTS;
//			material.chestplate = net.minecraft.init.Items.WOODEN_CHESTPLATE;
//			material.helmet = net.minecraft.init.Items.WOODEN_HELMET;
//			material.leggings = net.minecraft.init.Items.WOODEN_LEGGINGS;
			material.doorBlock = net.minecraft.init.Blocks.OAK_DOOR;
			material.ore = net.minecraft.init.Blocks.LOG;
			material.trapdoor = net.minecraft.init.Blocks.TRAPDOOR;
//			material.ingot = net.minecraft.init.Items.WOODEN_INGOT;
			material.shears = net.minecraft.init.Items.SHEARS;
//			material.nugget = net.minecraft.init.Items.WOOD_NUGGET; // Not till after 1.11
//			material.bars = net.minecraft.init.Blocks.WOODEN_BARS;
			material.block = net.minecraft.init.Blocks.PLANKS;
//			material.slab = net.minecraft.init.Items.SLA;
			material.half_slab = net.minecraft.init.Blocks.WOODEN_SLAB;
			material.double_slab = net.minecraft.init.Blocks.DOUBLE_WOODEN_SLAB;
			material.stairs = net.minecraft.init.Blocks.OAK_STAIRS;

			createCrackhammer(material);
			createGear(material);
		}

		if (Options.ENABLE_ZINC) {
			material = Materials.zinc;
			createItemsFull(material);
			createItemsModSupport(material);
		}

		addToMetList();
		
		initDone = true;
	}
}
