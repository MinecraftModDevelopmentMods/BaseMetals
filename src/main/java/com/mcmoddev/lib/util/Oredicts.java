package com.mcmoddev.lib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class Oredicts {
	private static Map<String, List<Item>> oreDictItemMap = new HashMap<>();
	private static Map<String, List<Block>> oreDictBlockMap = new HashMap<>();
	private static Map<String, List<ItemStack>> oreDictItemStackMap = new HashMap<>();
	
	// See net.minecraftforge.oredict.OreDictionary.initVanillaEntries() for Vanilla oreDict names

	// tree- and wood-related things
	public static final String LOG_WOOD = "logWood";
	public static final String PLANK_WOOD = "plankWood";
	public static final String SLAB_WOOD = "slabWood";
	public static final String STAIR_WOOD = "stairWood";
	public static final String STICK_WOOD = "stickWood";
	public static final String TREE_SAPLING = "treeSapling";
	public static final String TREE_LEAVES = "treeLeaves";
	public static final String VINE = "vine";

	// Ores
	public static final String ORE_GOLD = "oreGold";
	public static final String ORE_IRON = "oreIron";
	public static final String ORE_LAPIS = "oreLapis";
	public static final String ORE_DIAMOND = "oreDiamond";
	public static final String ORE_REDSTONE = "oreRedstone";
	public static final String ORE_EMERALD = "oreEmerald";
	public static final String ORE_QUARTZ = "oreQuartz";
	public static final String ORE_COAL = "oreCoal";

	// Ingots/Nuggets
	public static final String INGOT_IRON = "ingotIron";
	public static final String INGOT_GOLD = "ingotGold";
	public static final String INGOT_BRICK = "IngotBrick";
	public static final String INGOT_BRICK_NETHER = "IngotBrickNether";
	public static final String NUGGET_IRON = "nuggetIron";
	public static final String NUGGET_GOLD = "nuggetGold";

	// Gems/Dusts
	public static final String GEM_DIAMOND = "gemDiamond";
	public static final String GEM_EMERALD = "gemEmerald";
	public static final String GEM_QUARTZ = "gemQuartz";
	public static final String GEM_PRISMARINE = "gemPrismarine";
	public static final String DUST_PRISMARINE = "dustPrismarine";
	public static final String DUST_REDSTONE = "dustRedstone";
	public static final String DUST_GLOWSTONE = "dustGlowstone";
	public static final String GEM_LAPIS = "gemLapis";

	// Storage Blocks
	public static final String BLOCK_GOLD = "blockGold";
	public static final String BLOCK_IRON = "blockIron";
	public static final String BLOCK_LAPIS = "blockLapis";
	public static final String BLOCK_DIAMOND = "blockDiamond";
	public static final String BLOCK_REDSTONE = "blockRedstone";
	public static final String BLOCK_EMERALD = "blockEmerald";
	public static final String BLOCK_QUARTZ = "blockQuartz";
	public static final String BLOCK_COAL = "blockCoal";

	// Crops
	public static final String CROP_WHEAT = "cropWheat";
	public static final String CROP_POTATO = "cropPotato";
	public static final String CROP_CARROT = "cropCarrot";
	public static final String CROP_NETHERWART = "cropNetherWart";
	public static final String SUGARCANE = "sugarcane";
	public static final String BLOCK_CACTUS = "blockCactus";

	// Miscellaneous Materials
	public static final String DYE = "dye";
	public static final String PAPER = "paper";

	// Mob Drops
	public static final String SLIMEBALL = "slimeball";
	public static final String ENDERPEARL = "enderpearl";
	public static final String BONE = "bone";
	public static final String GUNPOWDER = "gunpowder";
	public static final String STRING = "string";
	public static final String NETHER_STAR = "netherStar";
	public static final String LEATHER = "leather";
	public static final String FEATHER = "feather";
	public static final String EGG = "egg";

	// Records
	public static final String RECORD = "record";

	// Blocks
	public static final String DIRT = "dirt";
	public static final String GRASS = "grass";
	public static final String STONE = "stone";
	public static final String COBBLESTONE = "cobblestone";
	public static final String GRAVEL = "gravel";
	public static final String SAND = "sand";
	public static final String SANDSTONE = "sandstone";
	public static final String NETHERRACK = "netherrack";
	public static final String OBSIDIAN = "obsidian";
	public static final String GLOWSTONE = "glowstone";
	public static final String ENDSTONE = "endstone";
	public static final String TORCH = "torch";
	public static final String WORKBENCH = "workbench";
	public static final String BLOCK_SLIME = "blockSlime";
	public static final String BLOCK_PRISMARINE = "blockPrismarine";
	public static final String BLOCK_PRISMARINE_BRICK = "blockPrismarineBrick";
	public static final String BLOCK_PRISMARINE_DARK = "blockPrismarineDark";
	public static final String STONE_GRANITE = "stoneGranite";
	public static final String STONE_GRANITE_POLISHED = "stoneGranitePolished";
	public static final String STONE_DIORITE = "stoneDiorite";
	public static final String STONE_DIORITE_POLISHED = "stoneDioritePolished";
	public static final String STONE_ANDESITE = "stoneAndesite";
	public static final String STONE_ANDESITE_POLISHED = "stoneAndesitePolished";
	public static final String BLOCK_GLASS_COLORLESS = "blockGlassColorless";
	public static final String BLOCK_GLASS = "blockGlass";
	public static final String BLOCK_GLASS_BLACK = "blockGlassBlack";
	public static final String BLOCK_GLASS_RED = "blockGlassRed";
	public static final String BLOCK_GLASS_GREEN = "blockGlassGreen";
	public static final String BLOCK_GLASS_BROWN = "blockGlassBrown";
	public static final String BLOCK_GLASS_BLUE = "blockGlassBlue";
	public static final String BLOCK_GLASS_PURPLE = "blockGlassPurple";
	public static final String BLOCK_GLASS_CYAN = "blockGlassCyan";
	public static final String BLOCK_GLASS_LIGHT_GRAY = "blockGlassLightGray";
	public static final String BLOCK_GLASS_GRAY = "blockGlassGray";
	public static final String BLOCK_GLASS_PINK = "blockGlassPink";
	public static final String BLOCK_GLASS_LIME = "blockGlassLime";
	public static final String BLOCK_GLASS_YELLOW = "blockGlassYellow";
	public static final String BLOCK_GLASS_LIGHT_BLUE = "blockGlassLightBlue";
	public static final String BLOCK_GLASS_MAGENTA = "blockGlassMagenta";
	public static final String BLOCK_GLASS_ORANGE = "blockGlassOrange";
	public static final String BLOCK_GLASS_WHITE = "blockGlassWhite";
	public static final String PANE_GLASS_COLORLESS = "paneGlassColorless";
	public static final String PANE_GLASS = "paneGlass";
	public static final String PANE_GLASS_BLACK = "paneGlassBlack";
	public static final String PANE_GLASS_RED = "paneGlassRed";
	public static final String PANE_GLASS_GREEN = "paneGlassGreen";
	public static final String PANE_GLASS_BROWN = "paneGlassBrown";
	public static final String PANE_GLASS_BLUE = "paneGlassBlue";
	public static final String PANE_GLASS_PURPLE = "paneGlassPurple";
	public static final String PANE_GLASS_CYAN = "paneGlassCyan";
	public static final String PANE_GLASS_LIGHT_GRAY = "paneGlassLightGray";
	public static final String PANE_GLASS_GRAY = "paneGlassGray";
	public static final String PANE_GLASS_PINK = "paneGlassPink";
	public static final String PANE_GLASS_LIME = "paneGlassLime";
	public static final String PANE_GLASS_YELLOW = "paneGlassYellow";
	public static final String PANE_GLASS_LIGHT_BLUE = "paneGlassLightBlue";
	public static final String PANE_GLASS_MAGENTA = "paneGlassMagenta";
	public static final String PANE_GLASS_ORANGE = "paneGlassOrange";
	public static final String PANE_GLASS_WHITE = "paneGlassWhite";

	// Chests
	public static final String CHEST = "chest";
	public static final String CHEST_WOOD = "chestWood";
	public static final String CHEST_ENDER = "chestEnder";
	public static final String CHEST_TRAPPED = "chestTrapped";

	// Dyes
	public static final String DYE_BLACK = "dyeBlack";
	public static final String DYE_RED = "dyeRed";
	public static final String DYE_GREEN = "dyeGreen";
	public static final String DYE_BROWN = "dyeBrown";
	public static final String DYE_BLUE = "dyeBlue";
	public static final String DYE_PURPLE = "dyePurple";
	public static final String DYE_CYAN = "dyeCyan";
	public static final String DYE_LIGHT_GRAY = "dyeLightGray";
	public static final String DYE_GRAY = "dyeGray";
	public static final String DYE_PINK = "dyePink";
	public static final String DYE_LIME = "dyeLime";
	public static final String DYE_YELLOW = "dyeYellow";
	public static final String DYE_LIGHT_BLUE = "dyeLightBlue";
	public static final String DYE_MAGENTA = "dyeMagenta";
	public static final String DYE_ORANGE = "dyeOrange";
	public static final String DYE_WHITE = "dyeWhite";

	// Custom
	public static final String ARROW = "arrow";
	public static final String AMMOBOLT = "ammoBolt";
	public static final String BUTTON = "button";
	public static final String BLEND = "blend";
	public static final String BLEND_SMALL = "blendSmall";
	public static final String BLEND_TINY = "blendTiny";
	public static final String DUST = "dust";
	public static final String DUST_SMALL = "dustSmall";
	public static final String DUST_TINY = "dustTiny";
	public static final String BLOCK = "block";
	public static final String INGOT = "ingot";
	public static final String NUGGET = "nugget";
	public static final String ORE = "ore";
	public static final String ORE_END = "oreEnd";
	public static final String ORE_NETHER = "oreNether";
	public static final String LEVER = "lever";
	public static final String DOOR = "door";
	public static final String PLATE = "plate";
	public static final String STICK = "stick";
	public static final String STAIRS = "stairs";
	public static final String SHIELD = "shield";
	public static final String SLAB = "slab";
	public static final String SPROCKET = "sprocket";
	public static final String TRAPDOOR = "trapdoor";
	public static final String ROD = "rod";
	public static final String GEAR = "gear";
	public static final String GEM = "gem";
	public static final String BARS = "bars";
	public static final String WALL = "wall";

	public static final String CLUMP = "clump";
	public static final String DUST_DIRTY = "dustDirty";
	public static final String CRYSTAL = "crystal";
	public static final String SHARD = "shard";

	public static final String CASING = "casing";
	public static final String PLATE_DENSE = "plateDense";
	public static final String CRUSHED = "crushed";
	public static final String CRUSHED_PURIFIED = "crushedPurified";

	public static final String DOOR_WOOD = "doorWood";
	public static final String DOOR_IRON = "doorIron";

	public static final String BARS_IRON = "barsIron";

	public static final String BLOCK_CHARCOAL = "blockCharcoal";

	public static final String DUST_CARBON = "dustCarbon";

	public static final String DUST_CHARCOAL = "dustCharcoal";
	public static final String DUST_SMALL_CHARCOAL = "dustSmallCharcoal";
	public static final String DUST_TINY_CHARCOAL = "dustTinyCharcoal";

	public static final String DUST_COAL = "dustCoal";
	public static final String DUST_SMALL_COAL = "dustSmallCoal";
	public static final String DUST_TINY_COAL = "dustTinyCoal";

	public static final String INGOT_MERCURY = "ingotMercury";
	public static final String INGOT_STEEL = "ingotSteel";

	public static final String NUGGET_CHARCOAL = "nuggetCharcoal";
	public static final String NUGGET_COAL = "nuggetCoal";

	public static void registerOre(String name, Block block) {
		if (block != null) {
			if( oreDictBlockMap.containsKey(name) ) {
				oreDictBlockMap.get(name).add(block);
			} else {
				List<Block> nl = new ArrayList<>();
				nl.add(block);
				oreDictBlockMap.put(name, nl);
			}
		}
	}

	public static void registerOre(String name, Item item) {
		if (item != null) {
			if( oreDictItemMap.containsKey(name) ) {
				oreDictItemMap.get(name).add(item);
			} else {
				List<Item> nl = new ArrayList<>();
				nl.add(item);
				oreDictItemMap.put(name, nl);
			}
		}

	}

	public static void registerOre(String name, ItemStack itemStack) {
		if (itemStack != null) {
			if( oreDictItemStackMap.containsKey(name) ) {
				oreDictItemStackMap.get(name).add(itemStack);
			} else {
				List<ItemStack> nl = new ArrayList<>();
				nl.add(itemStack);
				oreDictItemStackMap.put(name, nl);
			}
		}

	}

	public static void registerItemOreDictionaryEntries() {
		for( Entry<String,List<Item>> ent : oreDictItemMap.entrySet() ) {
			for( Item i : ent.getValue() ) {
				if( i.getRegistryName().getResourceDomain().equals(Loader.instance().activeModContainer().getModId())) {
					OreDictionary.registerOre(ent.getKey(), i);
				}
			}
		}
		for( Entry<String,List<ItemStack>> ent : oreDictItemStackMap.entrySet() ) {
			for( ItemStack is : ent.getValue() ) {
				if( is.getItem().getRegistryName().getResourceDomain().equals(Loader.instance().activeModContainer().getModId())) {
					OreDictionary.registerOre(ent.getKey(), is);
				}
			}
		}		
	}
	
	public static void registerBlockOreDictionaryEntries() {
		for( Entry<String,List<Block>> ent : oreDictBlockMap.entrySet() ) {
			for( Block b : ent.getValue() ) {
				if( b.getRegistryName().getResourceDomain().equals(Loader.instance().activeModContainer().getModId())) {
					OreDictionary.registerOre(ent.getKey(), b);
				}
			}
		}
	}
	
	private Oredicts() {
		throw new IllegalAccessError("Not a instantiable class");
	}
}
