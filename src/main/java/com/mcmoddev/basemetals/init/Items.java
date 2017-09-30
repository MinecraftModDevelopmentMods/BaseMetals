package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.fuels.FuelRegistry;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.TabContainer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This class initializes all items in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	private static boolean initDone = false;
	private static TabContainer myTabs = ItemGroups.myTabs;

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

		com.mcmoddev.basemetals.util.Config.init();
		Blocks.init();
		com.mcmoddev.lib.init.Items.init();

		// Vanilla stuff always gets those bits that exist in vanilla assigned
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
		final MMDMaterial stone = Materials.getMaterialByName(MaterialNames.STONE);
		final MMDMaterial wood = Materials.getMaterialByName(MaterialNames.WOOD);

		charcoal.addNewItem(Names.INGOT, new ItemStack(net.minecraft.init.Items.COAL, 1, 1).getItem());

		coal.addNewItem(Names.INGOT, new ItemStack(net.minecraft.init.Items.COAL, 1, 0).getItem());

		diamond.addNewItem(Names.AXE, net.minecraft.init.Items.DIAMOND_AXE);
		diamond.addNewItem(Names.HOE, net.minecraft.init.Items.DIAMOND_HOE);
		diamond.addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.DIAMOND_HORSE_ARMOR);
		diamond.addNewItem(Names.PICKAXE, net.minecraft.init.Items.DIAMOND_PICKAXE);
		diamond.addNewItem(Names.SHOVEL, net.minecraft.init.Items.DIAMOND_SHOVEL);
		diamond.addNewItem(Names.SWORD, net.minecraft.init.Items.DIAMOND_SWORD);
		diamond.addNewItem(Names.BOOTS, net.minecraft.init.Items.DIAMOND_BOOTS);
		diamond.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.DIAMOND_CHESTPLATE);
		diamond.addNewItem(Names.HELMET, net.minecraft.init.Items.DIAMOND_HELMET);
		diamond.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.DIAMOND_LEGGINGS);
		diamond.addNewItem(Names.INGOT, net.minecraft.init.Items.DIAMOND);

		emerald.addNewItem(Names.INGOT, net.minecraft.init.Items.EMERALD);

		gold.addNewItem(Names.AXE, net.minecraft.init.Items.GOLDEN_AXE);
		gold.addNewItem(Names.HOE, net.minecraft.init.Items.GOLDEN_HOE);
		gold.addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.GOLDEN_HORSE_ARMOR);
		gold.addNewItem(Names.PICKAXE, net.minecraft.init.Items.GOLDEN_PICKAXE);
		gold.addNewItem(Names.SHOVEL, net.minecraft.init.Items.GOLDEN_SHOVEL);
		gold.addNewItem(Names.SWORD, net.minecraft.init.Items.GOLDEN_SWORD);
		gold.addNewItem(Names.BOOTS, net.minecraft.init.Items.GOLDEN_BOOTS);
		gold.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.GOLDEN_CHESTPLATE);
		gold.addNewItem(Names.HELMET, net.minecraft.init.Items.GOLDEN_HELMET);
		gold.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.GOLDEN_LEGGINGS);
		gold.addNewItem(Names.INGOT, net.minecraft.init.Items.GOLD_INGOT);
		gold.addNewItem(Names.NUGGET, net.minecraft.init.Items.GOLD_NUGGET);

		iron.addNewItem(Names.AXE, net.minecraft.init.Items.IRON_AXE);
		iron.addNewItem(Names.DOOR, net.minecraft.init.Items.IRON_DOOR);
		iron.addNewItem(Names.HOE, net.minecraft.init.Items.IRON_HOE);
		iron.addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.IRON_HORSE_ARMOR);
		iron.addNewItem(Names.PICKAXE, net.minecraft.init.Items.IRON_PICKAXE);
		iron.addNewItem(Names.SHOVEL, net.minecraft.init.Items.IRON_SHOVEL);
		iron.addNewItem(Names.SWORD, net.minecraft.init.Items.IRON_SWORD);
		iron.addNewItem(Names.BOOTS, net.minecraft.init.Items.IRON_BOOTS);
		iron.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.IRON_CHESTPLATE);
		iron.addNewItem(Names.HELMET, net.minecraft.init.Items.IRON_HELMET);
		iron.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.IRON_LEGGINGS);
		iron.addNewItem(Names.DOOR, net.minecraft.init.Items.IRON_DOOR);
		iron.addNewItem(Names.INGOT, net.minecraft.init.Items.IRON_INGOT);
		iron.addNewItem(Names.NUGGET, net.minecraft.init.Items.IRON_NUGGET);
		iron.addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);

		lapis.addNewItem(Names.POWDER, new ItemStack(net.minecraft.init.Items.DYE, 1, 4).getItem());

		quartz.addNewItem(Names.INGOT, net.minecraft.init.Items.QUARTZ);
		// material.slab = ;

		redstone.addNewItem(Names.POWDER, net.minecraft.init.Items.REDSTONE);

		stone.addNewItem(Names.AXE, net.minecraft.init.Items.STONE_AXE);
		stone.addNewItem(Names.HOE, net.minecraft.init.Items.STONE_HOE);
		stone.addNewItem(Names.PICKAXE, net.minecraft.init.Items.STONE_PICKAXE);
		stone.addNewItem(Names.SHOVEL, net.minecraft.init.Items.STONE_SHOVEL);
		stone.addNewItem(Names.SWORD, net.minecraft.init.Items.STONE_SWORD);
		stone.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.STONE);
		stone.addNewBlock(Names.SLAB, net.minecraft.init.Blocks.STONE_SLAB);
		stone.addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.DOUBLE_STONE_SLAB);
		stone.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.STONE_STAIRS);

		wood.addNewItem(Names.AXE, net.minecraft.init.Items.WOODEN_AXE);
		wood.addNewItem(Names.DOOR, net.minecraft.init.Items.OAK_DOOR);
		wood.addNewItem(Names.HOE, net.minecraft.init.Items.WOODEN_HOE);
		wood.addNewItem(Names.PICKAXE, net.minecraft.init.Items.WOODEN_PICKAXE);
		wood.addNewItem(Names.SHOVEL, net.minecraft.init.Items.WOODEN_SHOVEL);
		wood.addNewItem(Names.SWORD, net.minecraft.init.Items.WOODEN_SWORD);
		wood.addNewBlock(Names.DOOR, net.minecraft.init.Blocks.OAK_DOOR);
		wood.addNewBlock(Names.ORE, net.minecraft.init.Blocks.LOG);
		wood.addNewBlock(Names.TRAPDOOR, net.minecraft.init.Blocks.TRAPDOOR);
		wood.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.PLANKS);
		wood.addNewBlock(Names.SLAB, net.minecraft.init.Blocks.WOODEN_SLAB);
		wood.addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.DOUBLE_WOODEN_SLAB);
		wood.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.OAK_STAIRS);
		wood.addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);

		if (Options.isMaterialEnabled(MaterialNames.ADAMANTINE)) {
			createItemsFull(MaterialNames.ADAMANTINE, myTabs);
			createItemsModSupport(MaterialNames.ADAMANTINE, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.ANTIMONY)) {
			createItemsFull(MaterialNames.ANTIMONY, myTabs);
			createItemsModSupport(MaterialNames.ANTIMONY, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.AQUARIUM)) {
			createItemsFull(MaterialNames.AQUARIUM, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.BISMUTH)) {
			createItemsFull(MaterialNames.BISMUTH, myTabs);
			createItemsModSupport(MaterialNames.BISMUTH, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.BRASS)) {
			createItemsFull(MaterialNames.BRASS, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.BRONZE)) {
			createItemsFull(MaterialNames.BRONZE, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.CHARCOAL)) {

			create(Names.NUGGET, charcoal, myTabs.itemsTab);
			create(Names.POWDER, charcoal, myTabs.itemsTab);
			create(Names.SMALLPOWDER, charcoal, myTabs.itemsTab);
			FuelRegistry.addFuel(Oredicts.NUGGET_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_CHARCOAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.BLOCK_CHARCOAL, 16000);
		}

		if (Options.isMaterialEnabled(MaterialNames.COAL)) {
			create(Names.NUGGET, coal, myTabs.itemsTab);
			create(Names.POWDER, coal, myTabs.itemsTab);
			create(Names.SMALLPOWDER, coal, myTabs.itemsTab);
			FuelRegistry.addFuel(Oredicts.NUGGET_COAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_COAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_COAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_COAL, 200);
		}

		if (Options.isMaterialEnabled(MaterialNames.COLDIRON)) {
			createItemsFull(MaterialNames.COLDIRON, myTabs);
			createItemsModSupport(MaterialNames.COLDIRON, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.COPPER)) {
			createItemsFull(MaterialNames.COPPER, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.CUPRONICKEL)) {
			createItemsFull(MaterialNames.CUPRONICKEL, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.DIAMOND)) {
			createItemsFull(diamond, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.EMERALD)) {
			createItemsFull(emerald, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.ELECTRUM)) {
			createItemsFull(MaterialNames.ELECTRUM, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.GOLD)) {
			createItemsFull(gold, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.INVAR)) {
			createItemsFull(MaterialNames.INVAR, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.IRON)) {
			createItemsFull(iron, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.LAPIS)) {
			create(Names.SMALLPOWDER, lapis, myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.LEAD)) {
			createItemsFull(MaterialNames.LEAD, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.OBSIDIAN)) {
			createItemsFull(obsidian, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.PLATINUM)) {
			createItemsFull(MaterialNames.PLATINUM, myTabs);
			createItemsModSupport(MaterialNames.PLATINUM, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);

			create(Names.INGOT, mercury, myTabs.itemsTab);
			create(Names.NUGGET, mercury, myTabs.itemsTab);
			create(Names.POWDER, mercury, myTabs.itemsTab);
			create(Names.SMALLPOWDER, mercury, myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.MITHRIL)) {
			createItemsFull(MaterialNames.MITHRIL, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.NICKEL)) {
			createItemsFull(MaterialNames.NICKEL, myTabs);
			createItemsModSupport(MaterialNames.NICKEL, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.PEWTER)) {
			createItemsFull(MaterialNames.PEWTER, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.REDSTONE)) {
			create(Names.INGOT, redstone, myTabs.itemsTab);
			create(Names.SMALLPOWDER, redstone, myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.QUARTZ)) {
			createItemsFull(quartz, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.SILVER)) {
			createItemsFull(MaterialNames.SILVER, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.STARSTEEL)) {
			createItemsFull(MaterialNames.STARSTEEL, myTabs);
			createItemsModSupport(MaterialNames.STARSTEEL, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.STONE)) {
			create(Names.CRACKHAMMER, stone, myTabs.toolsTab);
			create(Names.ROD, stone, myTabs.itemsTab);
			create(Names.GEAR, stone, myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.STEEL)) {
			createItemsFull(MaterialNames.STEEL, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.TIN)) {
			createItemsFull(MaterialNames.TIN, myTabs);
		}

		if (Options.isMaterialEnabled(MaterialNames.WOOD)) {
			create(Names.CRACKHAMMER, wood, myTabs.toolsTab);
			create(Names.GEAR, wood, myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.ZINC)) {
			createItemsFull(MaterialNames.ZINC, myTabs);
			createItemsModSupport(MaterialNames.ZINC, myTabs);
		}

		addToMetList();

		initDone = true;
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		BaseMetals.logger.fatal("ITEM REGISTER!");
		for( MMDMaterial mat : Materials.getMaterialsByMod(BaseMetals.MODID) ) {
			for( Item item : mat.getItems() ) {
				if( item.getRegistryName().getResourceDomain().equals(BaseMetals.MODID) ) {
					event.getRegistry().register(item);
				}
			}
		}
		
		if( Blocks.humanDetector != null ) {
			final ItemBlock itemBlock = new ItemBlock(Blocks.humanDetector);
			itemBlock.setRegistryName("human_detector");
			itemBlock.setUnlocalizedName(Blocks.humanDetector.getRegistryName().getResourceDomain() + ".human_detector");
			event.getRegistry().register(itemBlock);
		}
		
		Oredicts.registerItemOreDictionaryEntries();
		Oredicts.registerBlockOreDictionaryEntries();
	}
}
