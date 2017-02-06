package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This class initializes all items in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	public static Item charcoal_nugget;
	public static Item charcoal_powder;
	public static Item charcoal_smallpowder;

	public static Item coal_nugget;
	public static Item coal_powder;
	public static Item coal_smallpowder;

	public static Item mercury_ingot;
	public static Item mercury_nugget;
	public static Item mercury_powder;
	public static Item mercury_smallpowder;

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
			charcoal_nugget = addItem(new Item(), "charcoal_nugget", null, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.NUGGETCHARCOAL, charcoal_nugget);

			charcoal_powder = addItem(new Item(), "charcoal_powder", null, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.DUSTCHARCOAL, charcoal_powder);

			charcoal_smallpowder = addItem(new Item(), "charcoal_smallpowder", null, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.DUSTTINYCHARCOAL, charcoal_smallpowder);
			OreDictionary.registerOre(Oredicts.DUSTSMALLCHARCOAL, charcoal_smallpowder);
		}

		if (Options.enableCoal) {
			coal_nugget = addItem(new Item(), "coal_nugget", null, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.NUGGETCOAL, coal_nugget);

			coal_powder = addItem(new Item(), "coal_powder", null, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.DUSTCOAL, coal_powder);

			coal_smallpowder = addItem(new Item(), "coal_smallpowder", null, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.DUSTTINYCOAL, coal_smallpowder);
			OreDictionary.registerOre(Oredicts.DUSTSMALLCOAL, coal_smallpowder);
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
			material.horse_armor = net.minecraft.init.Items.DIAMOND_HORSE_ARMOR;
			material.pickaxe = net.minecraft.init.Items.DIAMOND_PICKAXE;
			material.shovel = net.minecraft.init.Items.DIAMOND_SHOVEL;
			material.sword = net.minecraft.init.Items.DIAMOND_SWORD;
			material.boots = net.minecraft.init.Items.DIAMOND_BOOTS;
			material.chestplate = net.minecraft.init.Items.DIAMOND_CHESTPLATE;
			material.helmet = net.minecraft.init.Items.DIAMOND_HELMET;
			material.leggings = net.minecraft.init.Items.DIAMOND_LEGGINGS;
			material.ingot = net.minecraft.init.Items.DIAMOND;

			createItemsFull(material);
//			createArrow(material);
//			createBolt(material);
//			createBow(material);
//			createCrossbow(material);
//			createFishingRod(material);
//			createShears(material);
//			createCrackhammer(material);
//			createDoor(material);
//			createGear(material);
//			createRod(material);

//			createSlab(material);
		}

		if (Options.enableElectrum) {
			createItemsFull(Materials.electrum);
		}

		if (Options.enableGold) {
			final MetalMaterial material = Materials.vanilla_gold;

			material.axe = net.minecraft.init.Items.GOLDEN_AXE;
			material.hoe = net.minecraft.init.Items.GOLDEN_HOE;
			material.horse_armor = net.minecraft.init.Items.GOLDEN_HORSE_ARMOR;
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
//			createArrow(material);
//			createBolt(material);
//			createBow(material);
//			createCrossbow(material);
//			createFishingRod(material);
//			createShears(material);
//			createCrackhammer(material);
//			createDoor(material);
//			createPowder(material);
//			createSmallPowder(material);
//			createRod(material);
//			createGear(material);

//			createSlab(material);
		}

		if (Options.enableInvar) {
			createItemsFull(Materials.invar);
		}

		if (Options.enableIron) {
			final MetalMaterial material = Materials.vanilla_iron;
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
			material.door = net.minecraft.init.Items.IRON_DOOR;
			material.ingot = net.minecraft.init.Items.IRON_INGOT;
			material.shears = net.minecraft.init.Items.SHEARS;
//			material.nugget = net.minecraft.init.Items.IRON_NUGGET; // Not till after 1.11

			createItemsFull(material);
//			createArrow(material);
//			createBolt(material);
//			createBow(material);
//			createCrossbow(material);
//			createFishingRod(material);
//			createCrackhammer(material);
//			createGear(material);
//			createNugget(material);
//			createPowder(material);
//			createRod(material);
//			createSmallPowder(material);
			
//			createSlab(material);
		}

		if (Options.enableLead) {
			createItemsFull(Materials.lead);
		}

		if (Options.enablePlatinum) {
			createItemsFull(Materials.platinum);
			createItemsModSupport(Materials.platinum);
		}

		if (Options.enableMercury) {
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
			material.half_slab = net.minecraft.init.Blocks.STONE_SLAB;
			material.double_slab = net.minecraft.init.Blocks.DOUBLE_STONE_SLAB;
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
			material.half_slab = net.minecraft.init.Blocks.WOODEN_SLAB;
			material.double_slab = net.minecraft.init.Blocks.DOUBLE_WOODEN_SLAB;
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
