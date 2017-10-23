package com.mcmoddev.basemetals.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.item.ItemMMDNugget;
import com.mcmoddev.lib.item.ItemMMDPowder;
import com.mcmoddev.lib.item.ItemMMDSmallPowder;
import com.mcmoddev.lib.item.ItemMMDBlock;
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

		// create and register vanilla stuffs
		Map<String,MMDMaterial> vanillaMats = new HashMap<>();
		
		List<String> vanillaMatNames = Arrays.asList( MaterialNames.CHARCOAL, MaterialNames.COAL, MaterialNames.DIAMOND, 
				MaterialNames.EMERALD, MaterialNames.GOLD, MaterialNames.IRON, MaterialNames.LAPIS, MaterialNames.OBSIDIAN,
				MaterialNames.QUARTZ, MaterialNames.REDSTONE, MaterialNames.STONE, MaterialNames.WOOD );
		
		vanillaMatNames.forEach( name -> vanillaMats.put(name,  Materials.getMaterialByName(name)) );
		
		vanillaMats.get(MaterialNames.CHARCOAL).addNewItem(Names.INGOT, new ItemStack(net.minecraft.init.Items.COAL, 1, 1).getItem());
		vanillaMats.get(MaterialNames.COAL).addNewItem(Names.INGOT, new ItemStack(net.minecraft.init.Items.COAL, 1, 0).getItem());
		
		addDiamondBits(vanillaMats);
		addGoldBits(vanillaMats);
		addIronBits(vanillaMats);
		addStoneBits(vanillaMats);
		addWoodBits(vanillaMats);
		
		vanillaMats.get(MaterialNames.EMERALD).addNewItem(Names.INGOT, net.minecraft.init.Items.EMERALD);
		vanillaMats.get(MaterialNames.LAPIS).addNewItem(Names.POWDER, new ItemStack(net.minecraft.init.Items.DYE, 1, 4).getItem());
		vanillaMats.get(MaterialNames.QUARTZ).addNewItem(Names.INGOT, net.minecraft.init.Items.QUARTZ);
		vanillaMats.get(MaterialNames.REDSTONE).addNewItem(Names.POWDER, net.minecraft.init.Items.REDSTONE);

		doSpecialMats(vanillaMats);

		// create and register modded stuffs
		List<String> matsModSupport = Arrays.asList( MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY, MaterialNames.BISMUTH,
				MaterialNames.COLDIRON, MaterialNames.PLATINUM, MaterialNames.NICKEL, MaterialNames.STARSTEEL, MaterialNames.ZINC );

		List<String> myModMats = Arrays.asList( MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY, MaterialNames.AQUARIUM,
				MaterialNames.BISMUTH, MaterialNames.BRASS, MaterialNames.BRONZE, MaterialNames.COLDIRON, MaterialNames.COPPER,
				MaterialNames.CUPRONICKEL, MaterialNames.EMERALD, MaterialNames.ELECTRUM, MaterialNames.INVAR, MaterialNames.LEAD,
				MaterialNames.OBSIDIAN, MaterialNames.MITHRIL, MaterialNames.NICKEL, MaterialNames.PEWTER, MaterialNames.PLATINUM, 
				MaterialNames.QUARTZ, MaterialNames.SILVER, MaterialNames.STARSTEEL, MaterialNames.STEEL, MaterialNames.TIN,
				MaterialNames.ZINC);
		
		myModMats.stream()
		.filter( name -> Options.isMaterialEnabled(name) )
		.filter( name -> !Materials.getMaterialByName(name).equals(Materials.emptyMaterial))
		.forEach( name -> createItemsFull( Materials.getMaterialByName(name), myTabs ) );
		
		matsModSupport.stream()
		.filter( name -> Options.isMaterialEnabled(name) )
		.filter( name -> !Materials.getMaterialByName(name).equals(Materials.emptyMaterial))
		.forEach( name -> createItemsModSupport( Materials.getMaterialByName(name), myTabs ) );

		if (Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);

			create(Names.INGOT, mercury, myTabs.itemsTab);
			create(Names.NUGGET, mercury, myTabs.itemsTab);
			create(Names.POWDER, mercury, myTabs.itemsTab);
			create(Names.SMALLPOWDER, mercury, myTabs.itemsTab);
		}

		addToMetList();

		initDone = true;
	}

	private static void doSpecialMats(Map<String, MMDMaterial> mats) {
		if (Options.isMaterialEnabled(MaterialNames.CHARCOAL)) {
			create(Names.NUGGET, mats.get(MaterialNames.CHARCOAL), myTabs.itemsTab);
			create(Names.POWDER, mats.get(MaterialNames.CHARCOAL), myTabs.itemsTab);
			create(Names.SMALLPOWDER, mats.get(MaterialNames.CHARCOAL), myTabs.itemsTab);
			((ItemMMDNugget)mats.get(MaterialNames.CHARCOAL).getItem(Names.NUGGET)).setBurnTime(200);
			((ItemMMDPowder)mats.get(MaterialNames.CHARCOAL).getItem(Names.POWDER)).setBurnTime(1600);
			((ItemMMDSmallPowder)mats.get(MaterialNames.CHARCOAL).getItem(Names.SMALLPOWDER)).setBurnTime(200);
			((ItemMMDBlock)Item.getItemFromBlock(mats.get(MaterialNames.CHARCOAL).getBlock(Names.BLOCK))).setBurnTime(16000);
		}

		if (Options.isMaterialEnabled(MaterialNames.COAL)) {
			create(Names.NUGGET, mats.get(MaterialNames.COAL), myTabs.itemsTab);
			create(Names.POWDER, mats.get(MaterialNames.COAL), myTabs.itemsTab);
			create(Names.SMALLPOWDER, mats.get(MaterialNames.COAL), myTabs.itemsTab);
			((ItemMMDNugget)mats.get(MaterialNames.COAL).getItem(Names.NUGGET)).setBurnTime(200);
			((ItemMMDPowder)mats.get(MaterialNames.COAL).getItem(Names.POWDER)).setBurnTime(1600);
			((ItemMMDSmallPowder)mats.get(MaterialNames.COAL).getItem(Names.SMALLPOWDER)).setBurnTime(200);
		}

		if (Options.isMaterialEnabled(MaterialNames.REDSTONE)) {
			create(Names.INGOT, mats.get(MaterialNames.REDSTONE), myTabs.itemsTab);
			create(Names.SMALLPOWDER, mats.get(MaterialNames.REDSTONE), myTabs.itemsTab);
		}
		
		if (Options.isMaterialEnabled(MaterialNames.LAPIS)) {
			create(Names.SMALLPOWDER, mats.get(MaterialNames.LAPIS), myTabs.itemsTab);
		}

	}

	private static void addDiamondBits( Map<String, MMDMaterial> mats ) {
		mats.get(MaterialNames.DIAMOND).addNewItem(Names.AXE, net.minecraft.init.Items.DIAMOND_AXE);
		mats.get(MaterialNames.DIAMOND).addNewItem(Names.HOE, net.minecraft.init.Items.DIAMOND_HOE);
		mats.get(MaterialNames.DIAMOND).addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.DIAMOND_HORSE_ARMOR);
		mats.get(MaterialNames.DIAMOND).addNewItem(Names.PICKAXE, net.minecraft.init.Items.DIAMOND_PICKAXE);
		mats.get(MaterialNames.DIAMOND).addNewItem(Names.SHOVEL, net.minecraft.init.Items.DIAMOND_SHOVEL);
		mats.get(MaterialNames.DIAMOND).addNewItem(Names.SWORD, net.minecraft.init.Items.DIAMOND_SWORD);
		mats.get(MaterialNames.DIAMOND).addNewItem(Names.BOOTS, net.minecraft.init.Items.DIAMOND_BOOTS);
		mats.get(MaterialNames.DIAMOND).addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.DIAMOND_CHESTPLATE);
		mats.get(MaterialNames.DIAMOND).addNewItem(Names.HELMET, net.minecraft.init.Items.DIAMOND_HELMET);
		mats.get(MaterialNames.DIAMOND).addNewItem(Names.LEGGINGS, net.minecraft.init.Items.DIAMOND_LEGGINGS);
		mats.get(MaterialNames.DIAMOND).addNewItem(Names.INGOT, net.minecraft.init.Items.DIAMOND);
		
		if (Options.isMaterialEnabled(MaterialNames.DIAMOND)) {
			createItemsFull(mats.get(MaterialNames.DIAMOND), myTabs);
		}
	}

	private static void addGoldBits( Map<String, MMDMaterial> mats ) {
		mats.get(MaterialNames.GOLD).addNewItem(Names.AXE, net.minecraft.init.Items.GOLDEN_AXE);
		mats.get(MaterialNames.GOLD).addNewItem(Names.HOE, net.minecraft.init.Items.GOLDEN_HOE);
		mats.get(MaterialNames.GOLD).addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.GOLDEN_HORSE_ARMOR);
		mats.get(MaterialNames.GOLD).addNewItem(Names.PICKAXE, net.minecraft.init.Items.GOLDEN_PICKAXE);
		mats.get(MaterialNames.GOLD).addNewItem(Names.SHOVEL, net.minecraft.init.Items.GOLDEN_SHOVEL);
		mats.get(MaterialNames.GOLD).addNewItem(Names.SWORD, net.minecraft.init.Items.GOLDEN_SWORD);
		mats.get(MaterialNames.GOLD).addNewItem(Names.BOOTS, net.minecraft.init.Items.GOLDEN_BOOTS);
		mats.get(MaterialNames.GOLD).addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.GOLDEN_CHESTPLATE);
		mats.get(MaterialNames.GOLD).addNewItem(Names.HELMET, net.minecraft.init.Items.GOLDEN_HELMET);
		mats.get(MaterialNames.GOLD).addNewItem(Names.LEGGINGS, net.minecraft.init.Items.GOLDEN_LEGGINGS);
		mats.get(MaterialNames.GOLD).addNewItem(Names.INGOT, net.minecraft.init.Items.GOLD_INGOT);
		mats.get(MaterialNames.GOLD).addNewItem(Names.NUGGET, net.minecraft.init.Items.GOLD_NUGGET);

		if (Options.isMaterialEnabled(MaterialNames.GOLD)) {
			createItemsFull(mats.get(MaterialNames.GOLD), myTabs);
		}
	}

	private static void addIronBits( Map<String, MMDMaterial> mats ) {
		mats.get(MaterialNames.IRON).addNewItem(Names.AXE, net.minecraft.init.Items.IRON_AXE);
		mats.get(MaterialNames.IRON).addNewItem(Names.DOOR, net.minecraft.init.Items.IRON_DOOR);
		mats.get(MaterialNames.IRON).addNewItem(Names.HOE, net.minecraft.init.Items.IRON_HOE);
		mats.get(MaterialNames.IRON).addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.IRON_HORSE_ARMOR);
		mats.get(MaterialNames.IRON).addNewItem(Names.PICKAXE, net.minecraft.init.Items.IRON_PICKAXE);
		mats.get(MaterialNames.IRON).addNewItem(Names.SHOVEL, net.minecraft.init.Items.IRON_SHOVEL);
		mats.get(MaterialNames.IRON).addNewItem(Names.SWORD, net.minecraft.init.Items.IRON_SWORD);
		mats.get(MaterialNames.IRON).addNewItem(Names.BOOTS, net.minecraft.init.Items.IRON_BOOTS);
		mats.get(MaterialNames.IRON).addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.IRON_CHESTPLATE);
		mats.get(MaterialNames.IRON).addNewItem(Names.HELMET, net.minecraft.init.Items.IRON_HELMET);
		mats.get(MaterialNames.IRON).addNewItem(Names.LEGGINGS, net.minecraft.init.Items.IRON_LEGGINGS);
		mats.get(MaterialNames.IRON).addNewItem(Names.DOOR, net.minecraft.init.Items.IRON_DOOR);
		mats.get(MaterialNames.IRON).addNewItem(Names.INGOT, net.minecraft.init.Items.IRON_INGOT);
		mats.get(MaterialNames.IRON).addNewItem(Names.NUGGET, net.minecraft.init.Items.IRON_NUGGET);
		mats.get(MaterialNames.IRON).addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);

		if (Options.isMaterialEnabled(MaterialNames.IRON)) {
			createItemsFull(mats.get(MaterialNames.IRON), myTabs);
		}
	}
	
	private static void addStoneBits( Map<String, MMDMaterial> mats ) {
		mats.get(MaterialNames.STONE).addNewItem(Names.AXE, net.minecraft.init.Items.STONE_AXE);
		mats.get(MaterialNames.STONE).addNewItem(Names.HOE, net.minecraft.init.Items.STONE_HOE);
		mats.get(MaterialNames.STONE).addNewItem(Names.PICKAXE, net.minecraft.init.Items.STONE_PICKAXE);
		mats.get(MaterialNames.STONE).addNewItem(Names.SHOVEL, net.minecraft.init.Items.STONE_SHOVEL);
		mats.get(MaterialNames.STONE).addNewItem(Names.SWORD, net.minecraft.init.Items.STONE_SWORD);
		mats.get(MaterialNames.STONE).addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.STONE);
		mats.get(MaterialNames.STONE).addNewBlock(Names.SLAB, net.minecraft.init.Blocks.STONE_SLAB);
		mats.get(MaterialNames.STONE).addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.DOUBLE_STONE_SLAB);
		mats.get(MaterialNames.STONE).addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.STONE_STAIRS);

		if (Options.isMaterialEnabled(MaterialNames.STONE)) {
			create(Names.CRACKHAMMER, mats.get(MaterialNames.STONE), myTabs.toolsTab);
			create(Names.ROD, mats.get(MaterialNames.STONE), myTabs.itemsTab);
			create(Names.GEAR, mats.get(MaterialNames.STONE), myTabs.itemsTab);
		}
	}

	private static void addWoodBits( Map<String, MMDMaterial> mats ) {
		mats.get(MaterialNames.WOOD).addNewItem(Names.AXE, net.minecraft.init.Items.WOODEN_AXE);
		mats.get(MaterialNames.WOOD).addNewItem(Names.DOOR, net.minecraft.init.Items.OAK_DOOR);
		mats.get(MaterialNames.WOOD).addNewItem(Names.HOE, net.minecraft.init.Items.WOODEN_HOE);
		mats.get(MaterialNames.WOOD).addNewItem(Names.PICKAXE, net.minecraft.init.Items.WOODEN_PICKAXE);
		mats.get(MaterialNames.WOOD).addNewItem(Names.SHOVEL, net.minecraft.init.Items.WOODEN_SHOVEL);
		mats.get(MaterialNames.WOOD).addNewItem(Names.SWORD, net.minecraft.init.Items.WOODEN_SWORD);
		mats.get(MaterialNames.WOOD).addNewBlock(Names.DOOR, net.minecraft.init.Blocks.OAK_DOOR);
		mats.get(MaterialNames.WOOD).addNewBlock(Names.ORE, net.minecraft.init.Blocks.LOG);
		mats.get(MaterialNames.WOOD).addNewBlock(Names.TRAPDOOR, net.minecraft.init.Blocks.TRAPDOOR);
		mats.get(MaterialNames.WOOD).addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.PLANKS);
		mats.get(MaterialNames.WOOD).addNewBlock(Names.SLAB, net.minecraft.init.Blocks.WOODEN_SLAB);
		mats.get(MaterialNames.WOOD).addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.DOUBLE_WOODEN_SLAB);
		mats.get(MaterialNames.WOOD).addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.OAK_STAIRS);
		mats.get(MaterialNames.WOOD).addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);

		if (Options.isMaterialEnabled(MaterialNames.WOOD)) {
			create(Names.CRACKHAMMER, mats.get(MaterialNames.WOOD), myTabs.toolsTab);
			create(Names.GEAR, mats.get(MaterialNames.WOOD), myTabs.itemsTab);
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
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
