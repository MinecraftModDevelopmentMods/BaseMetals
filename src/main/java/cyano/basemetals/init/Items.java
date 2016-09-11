package cyano.basemetals.init;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.blocks.*;
import cyano.basemetals.items.*;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * This class initializes all items in Base Metals and provides some utility 
 * methods for looking up items. 
 * @author DrCyano
 *
 */
public abstract class Items {

	public static Item adamantine_arrow;
	public static Item adamantine_axe;
	public static Item adamantine_boots;
	public static Item adamantine_bolt;
	public static Item adamantine_bow;
	public static Item adamantine_chestplate;
	public static Item adamantine_crackhammer;
	public static Item adamantine_crossbow;
	public static Item adamantine_door;
	public static Item adamantine_fishing_rod;
	public static Item adamantine_helmet;
	public static Item adamantine_hoe;
	public static Item adamantine_horsearmor;
	public static Item adamantine_ingot;
	public static Item adamantine_leggings;
	public static Item adamantine_nugget;
	public static Item adamantine_pickaxe;
	public static Item adamantine_powder;
	public static Item adamantine_shears;
	public static Item adamantine_shovel;
	public static Item adamantine_smallpowder;
	public static Item adamantine_sword;
	public static Item adamantine_rod;
	public static Item adamantine_gear;

	protected static Item adamantine_slab;
	//protected static Item adamantine_shield;

	protected static Item adamantine_crystal;
	protected static Item adamantine_shard;
	protected static Item adamantine_clump;
	protected static Item adamantine_powder_dirty;

	protected static Item adamantine_dense_plate;
	protected static Item adamantine_crushed;
	protected static Item adamantine_crushed_purified;

	public static Item aquarium_arrow;
	public static Item aquarium_axe;
	public static Item aquarium_blend;
	public static Item aquarium_boots;
	public static Item aquarium_bolt;
	public static Item aquarium_bow;
	public static Item aquarium_chestplate;
	public static Item aquarium_crackhammer;
	public static Item aquarium_crossbow;
	public static Item aquarium_door;
	public static Item aquarium_helmet;
	public static Item aquarium_hoe;
	public static Item aquarium_horsearmor;
	public static Item aquarium_ingot;
	public static Item aquarium_leggings;
	public static Item aquarium_nugget;
	public static Item aquarium_pickaxe;
	public static Item aquarium_powder;
	public static Item aquarium_shears;
	public static Item aquarium_shovel;
	public static Item aquarium_smallblend;
	public static Item aquarium_smallpowder;
	public static Item aquarium_sword;
	public static Item aquarium_rod;
	public static Item aquarium_gear;

	protected static Item aquarium_slab;
	//protected static Item aquarium_shield;

	protected static Item aquarium_crystal;
	protected static Item aquarium_shard;
	protected static Item aquarium_clump;
	protected static Item aquarium_powder_dirty;

	protected static Item aquarium_dense_plate;
	protected static Item aquarium_crushed;
	protected static Item aquarium_crushed_purified;

	public static Item brass_arrow;
	public static Item brass_axe;
	public static Item brass_blend;
	public static Item brass_boots;
	public static Item brass_bolt;
	public static Item brass_bow;
	public static Item brass_chestplate;
	public static Item brass_crackhammer;
	public static Item brass_crossbow;
	public static Item brass_door;
	public static Item brass_helmet;
	public static Item brass_hoe;
	public static Item brass_horsearmor;
	public static Item brass_ingot;
	public static Item brass_leggings;
	public static Item brass_nugget;
	public static Item brass_pickaxe;
	public static Item brass_powder;
	public static Item brass_shears;
	public static Item brass_shovel;
	public static Item brass_smallblend;
	public static Item brass_smallpowder;
	public static Item brass_sword;
	public static Item brass_rod;
	public static Item brass_gear;

	protected static Item brass_slab;
	//protected static Item brass_shield;

	protected static Item brass_crystal;
	protected static Item brass_shard;
	protected static Item brass_clump;
	protected static Item brass_powder_dirty;

	protected static Item brass_dense_plate;
	protected static Item brass_crushed;
	protected static Item brass_crushed_purified;

	public static Item bronze_arrow;
	public static Item bronze_axe;
	public static Item bronze_blend;
	public static Item bronze_boots;
	public static Item bronze_bolt;
	public static Item bronze_bow;
	public static Item bronze_chestplate;
	public static Item bronze_crackhammer;
	public static Item bronze_crossbow;
	public static Item bronze_door;
	public static Item bronze_helmet;
	public static Item bronze_hoe;
	public static Item bronze_horsearmor;
	public static Item bronze_ingot;
	public static Item bronze_leggings;
	public static Item bronze_nugget;
	public static Item bronze_pickaxe;
	public static Item bronze_powder;
	public static Item bronze_shears;
	public static Item bronze_shovel;
	public static Item bronze_smallblend;
	public static Item bronze_smallpowder;
	public static Item bronze_sword;
	public static Item bronze_rod;
	public static Item bronze_gear;

	protected static Item bronze_slab;
	//protected static Item bronze_shield;

	protected static Item bronze_crystal;
	protected static Item bronze_shard;
	protected static Item bronze_clump;
	protected static Item bronze_powder_dirty;

	protected static Item bronze_dense_plate;
	protected static Item bronze_crushed;
	protected static Item bronze_crushed_purified;

	public static Item carbon_powder;

	public static Item coldiron_arrow;
	public static Item coldiron_axe;
	public static Item coldiron_boots;
	public static Item coldiron_bolt;
	public static Item coldiron_bow;
	public static Item coldiron_chestplate;
	public static Item coldiron_crackhammer;
	public static Item coldiron_crossbow;
	public static Item coldiron_door;
	public static Item coldiron_helmet;
	public static Item coldiron_hoe;
	public static Item coldiron_horsearmor;
	public static Item coldiron_ingot;
	public static Item coldiron_leggings;
	public static Item coldiron_nugget;
	public static Item coldiron_pickaxe;
	public static Item coldiron_powder;
	public static Item coldiron_shears;
	public static Item coldiron_shovel;
	public static Item coldiron_smallpowder;
	public static Item coldiron_sword;
	public static Item coldiron_rod;
	public static Item coldiron_gear;

	protected static Item coldiron_slab;
	//protected static Item coldiron_shield;

	protected static Item coldiron_crystal;
	protected static Item coldiron_shard;
	protected static Item coldiron_clump;
	protected static Item coldiron_powder_dirty;

	protected static Item coldiron_dense_plate;
	protected static Item coldiron_crushed;
	protected static Item coldiron_crushed_purified;

	public static Item copper_arrow;
	public static Item copper_axe;
	public static Item copper_boots;
	public static Item copper_bolt;
	public static Item copper_bow;
	public static Item copper_chestplate;
	public static Item copper_crackhammer;
	public static Item copper_crossbow;
	public static Item copper_door;
	public static Item copper_helmet;
	public static Item copper_hoe;
	public static Item copper_horsearmor;
	public static Item copper_ingot;
	public static Item copper_leggings;
	public static Item copper_nugget;
	public static Item copper_pickaxe;
	public static Item copper_powder;
	public static Item copper_shears;
	public static Item copper_shovel;
	public static Item copper_smallpowder;
	public static Item copper_sword;
	public static Item copper_rod;
	public static Item copper_gear;

	protected static Item copper_slab;
	//protected static Item copper_shield;

	protected static Item copper_crystal;
	protected static Item copper_shard;
	protected static Item copper_clump;
	protected static Item copper_powder_dirty;

	protected static Item copper_dense_plate;
	protected static Item copper_crushed;
	protected static Item copper_crushed_purified;

	public static Item cupronickel_arrow;
	public static Item cupronickel_axe;
	public static Item cupronickel_boots;
	public static Item cupronickel_bolt;
	public static Item cupronickel_bow;
	public static Item cupronickel_blend;
	public static Item cupronickel_chestplate;
	public static Item cupronickel_crackhammer;
	public static Item cupronickel_crossbow;
	public static Item cupronickel_door;
	public static Item cupronickel_helmet;
	public static Item cupronickel_hoe;
	public static Item cupronickel_horsearmor;
	public static Item cupronickel_ingot;
	public static Item cupronickel_leggings;
	public static Item cupronickel_nugget;
	public static Item cupronickel_pickaxe;
	public static Item cupronickel_powder;
	public static Item cupronickel_shears;
	public static Item cupronickel_shovel;
	public static Item cupronickel_smallblend;
	public static Item cupronickel_smallpowder;
	public static Item cupronickel_sword;
	public static Item cupronickel_rod;
	public static Item cupronickel_gear;

	protected static Item cupronickel_slab;
	//protected static Item cupronickel_shield;

	protected static Item cupronickel_crystal;
	protected static Item cupronickel_shard;
	protected static Item cupronickel_clump;
	protected static Item cupronickel_powder_dirty;

	protected static Item cupronickel_dense_plate;
	protected static Item cupronickel_crushed;
	protected static Item cupronickel_crushed_purified;

	public static Item diamond_crackhammer;
	public static Item diamond_gear;

	public static Item electrum_arrow;
	public static Item electrum_axe;
	public static Item electrum_blend;
	public static Item electrum_boots;
	public static Item electrum_bolt;
	public static Item electrum_bow;
	public static Item electrum_chestplate;
	public static Item electrum_crackhammer;
	public static Item electrum_crossbow;
	public static Item electrum_door;
	public static Item electrum_helmet;
	public static Item electrum_hoe;
	public static Item electrum_horsearmor;
	public static Item electrum_ingot;
	public static Item electrum_leggings;
	public static Item electrum_nugget;
	public static Item electrum_pickaxe;
	public static Item electrum_powder;
	public static Item electrum_shears;
	public static Item electrum_shovel;
	public static Item electrum_smallblend;
	public static Item electrum_smallpowder;
	public static Item electrum_sword;
	public static Item electrum_rod;
	public static Item electrum_gear;

	protected static Item electrum_slab;
	//protected static Item electrum_shield;

	protected static Item electrum_crystal;
	protected static Item electrum_shard;
	protected static Item electrum_clump;
	protected static Item electrum_powder_dirty;

	protected static Item electrum_dense_plate;
	protected static Item electrum_crushed;
	protected static Item electrum_crushed_purified;

	public static Item gold_crackhammer;
	public static Item gold_powder;
	public static Item gold_rod;
	public static Item gold_gear;

	public static Item invar_arrow;
	public static Item invar_axe;
	public static Item invar_blend;
	public static Item invar_boots;
	public static Item invar_bolt;
	public static Item invar_bow;
	public static Item invar_chestplate;
	public static Item invar_crackhammer;
	public static Item invar_crossbow;
	public static Item invar_door;
	public static Item invar_helmet;
	public static Item invar_hoe;
	public static Item invar_horsearmor;
	public static Item invar_ingot;
	public static Item invar_leggings;
	public static Item invar_nugget;
	public static Item invar_pickaxe;
	public static Item invar_powder;
	public static Item invar_shears;
	public static Item invar_shovel;
	public static Item invar_smallblend;
	public static Item invar_smallpowder;
	public static Item invar_sword;
	public static Item invar_rod;
	public static Item invar_gear;

	protected static Item invar_slab;
	//protected static Item invar_shield;

	protected static Item invar_crystal;
	protected static Item invar_shard;
	protected static Item invar_clump;
	protected static Item invar_powder_dirty;

	protected static Item invar_dense_plate;
	protected static Item invar_crushed;
	protected static Item invar_crushed_purified;

	public static Item iron_crackhammer;
	public static Item iron_nugget;
	public static Item iron_powder;
	public static Item iron_rod;
	public static Item iron_gear;

	public static Item lead_arrow;
	public static Item lead_axe;
	public static Item lead_boots;
	public static Item lead_bolt;
	public static Item lead_bow;
	public static Item lead_chestplate;
	public static Item lead_crackhammer;
	public static Item lead_crossbow;
	public static Item lead_door;
	public static Item lead_helmet;
	public static Item lead_hoe;
	public static Item lead_horsearmor;
	public static Item lead_ingot;
	public static Item lead_leggings;
	public static Item lead_nugget;
	public static Item lead_pickaxe;
	public static Item lead_powder;
	public static Item lead_shears;
	public static Item lead_shovel;
	public static Item lead_smallpowder;
	public static Item lead_sword;
	public static Item lead_rod;
	public static Item lead_gear;

	protected static Item lead_slab;
	//protected static Item lead_shield;

	protected static Item lead_crystal;
	protected static Item lead_shard;
	protected static Item lead_clump;
	protected static Item lead_powder_dirty;

	protected static Item lead_dense_plate;
	protected static Item lead_crushed;
	protected static Item lead_crushed_purified;

	public static Item mercury_ingot;
	public static Item mercury_powder;

	protected static Item mercury_crystal;
	protected static Item mercury_shard;
	protected static Item mercury_clump;
	protected static Item mercury_powder_dirty;

	protected static Item mercury_crushed;
	protected static Item mercury_crushed_purified;

	public static Item mithril_arrow;
	public static Item mithril_axe;
	public static Item mithril_blend;
	public static Item mithril_boots;
	public static Item mithril_bolt;
	public static Item mithril_bow;
	public static Item mithril_chestplate;
	public static Item mithril_crackhammer;
	public static Item mithril_crossbow;
	public static Item mithril_door;
	public static Item mithril_helmet;
	public static Item mithril_hoe;
	public static Item mithril_horsearmor;
	public static Item mithril_ingot;
	public static Item mithril_leggings;
	public static Item mithril_nugget;
	public static Item mithril_pickaxe;
	public static Item mithril_powder;
	public static Item mithril_shears;
	public static Item mithril_shovel;
	public static Item mithril_smallblend;
	public static Item mithril_smallpowder;
	public static Item mithril_sword;
	public static Item mithril_rod;
	public static Item mithril_gear;

	protected static Item mithril_slab;
	//protected static Item mithril_shield;

	protected static Item mithril_crystal;
	protected static Item mithril_shard;
	protected static Item mithril_clump;
	protected static Item mithril_powder_dirty;

	protected static Item mithril_dense_plate;
	protected static Item mithril_crushed;
	protected static Item mithril_crushed_purified;

	public static Item nickel_arrow;
	public static Item nickel_axe;
	public static Item nickel_boots;
	public static Item nickel_bolt;
	public static Item nickel_bow;
	public static Item nickel_chestplate;
	public static Item nickel_crackhammer;
	public static Item nickel_crossbow;
	public static Item nickel_door;
	public static Item nickel_helmet;
	public static Item nickel_hoe;
	public static Item nickel_horsearmor;
	public static Item nickel_ingot;
	public static Item nickel_leggings;
	public static Item nickel_nugget;
	public static Item nickel_pickaxe;
	public static Item nickel_powder;
	public static Item nickel_shears;
	public static Item nickel_shovel;
	public static Item nickel_smallpowder;
	public static Item nickel_sword;
	public static Item nickel_rod;
	public static Item nickel_gear;

	protected static Item nickel_slab;
	//protected static Item nickel_shield;

	protected static Item nickel_crystal;
	protected static Item nickel_shard;
	protected static Item nickel_clump;
	protected static Item nickel_powder_dirty;

	protected static Item nickel_dense_plate;
	protected static Item nickel_crushed;
	protected static Item nickel_crushed_purified;

	public static Item platinum_arrow;
	public static Item platinum_axe;
	public static Item platinum_boots;
	public static Item platinum_bolt;
	public static Item platinum_bow;
	public static Item platinum_chestplate;
	public static Item platinum_crackhammer;
	public static Item platinum_crossbow;
	public static Item platinum_door;
	public static Item platinum_helmet;
	public static Item platinum_hoe;
	public static Item platinum_horsearmor;
	public static Item platinum_ingot;
	public static Item platinum_leggings;
	public static Item platinum_nugget;
	public static Item platinum_pickaxe;
	public static Item platinum_powder;
	public static Item platinum_shears;
	public static Item platinum_shovel;
	public static Item platinum_smallpowder;
	public static Item platinum_sword;
	public static Item platinum_rod;
	public static Item platinum_gear;

	protected static Item platinum_slab;
	//protected static Item platinum_shield;

	protected static Item platinum_crystal;
	protected static Item platinum_shard;
	protected static Item platinum_clump;
	protected static Item platinum_powder_dirty;

	protected static Item platinum_dense_plate;
	protected static Item platinum_crushed;
	protected static Item platinum_crushed_purified;

	public static Item silver_arrow;
	public static Item silver_axe;
	public static Item silver_boots;
	public static Item silver_bolt;
	public static Item silver_bow;
	public static Item silver_chestplate;
	public static Item silver_crackhammer;
	public static Item silver_crossbow;
	public static Item silver_door;
	public static Item silver_helmet;
	public static Item silver_hoe;
	public static Item silver_horsearmor;
	public static Item silver_ingot;
	public static Item silver_leggings;
	public static Item silver_nugget;
	public static Item silver_pickaxe;
	public static Item silver_powder;
	public static Item silver_shears;
	public static Item silver_shovel;
	public static Item silver_smallpowder;
	public static Item silver_sword;
	public static Item silver_rod;
	public static Item silver_gear;

	protected static Item silver_slab;
	//protected static Item silver_shield;

	protected static Item silver_crystal;
	protected static Item silver_shard;
	protected static Item silver_clump;
	protected static Item silver_powder_dirty;

	protected static Item silver_dense_plate;
	protected static Item silver_crushed;
	protected static Item silver_crushed_purified;

	public static Item starsteel_arrow;
	public static Item starsteel_axe;
	public static Item starsteel_boots;
	public static Item starsteel_bolt;
	public static Item starsteel_bow;
	public static Item starsteel_chestplate;
	public static Item starsteel_crackhammer;
	public static Item starsteel_crossbow;
	public static Item starsteel_door;
	public static Item starsteel_helmet;
	public static Item starsteel_hoe;
	public static Item starsteel_horsearmor;
	public static Item starsteel_ingot;
	public static Item starsteel_leggings;
	public static Item starsteel_nugget;
	public static Item starsteel_pickaxe;
	public static Item starsteel_powder;
	public static Item starsteel_shears;
	public static Item starsteel_shovel;
	public static Item starsteel_smallpowder;
	public static Item starsteel_sword;
	public static Item starsteel_rod;
	public static Item starsteel_gear;

	protected static Item starsteel_slab;
	//protected static Item starsteel_shield;

	protected static Item starsteel_crystal;
	protected static Item starsteel_shard;
	protected static Item starsteel_clump;
	protected static Item starsteel_powder_dirty;

	protected static Item starsteel_dense_plate;
	protected static Item starsteel_crushed;
	protected static Item starsteel_crushed_purified;

	public static Item steel_arrow;
	public static Item steel_axe;
	public static Item steel_blend;
	public static Item steel_boots;
	public static Item steel_bolt;
	public static Item steel_bow;
	public static Item steel_chestplate;
	public static Item steel_crackhammer;
	public static Item steel_crossbow;
	public static Item steel_door;
	public static Item steel_helmet;
	public static Item steel_hoe;
	public static Item steel_horsearmor;
	public static Item steel_ingot;
	public static Item steel_leggings;
	public static Item steel_nugget;
	public static Item steel_pickaxe;
	public static Item steel_powder;
	public static Item steel_shears;
	public static Item steel_shovel;
	public static Item steel_smallblend;
	public static Item steel_smallpowder;
	public static Item steel_sword;
	public static Item steel_rod;
	public static Item steel_gear;

	protected static Item steel_slab;
	//protected static Item steel_shield;

	protected static Item steel_crystal;
	protected static Item steel_shard;
	protected static Item steel_clump;
	protected static Item steel_powder_dirty;

	protected static Item steel_dense_plate;
	protected static Item steel_crushed;
	protected static Item steel_crushed_purified;

	public static Item stone_crackhammer;
	public static Item stone_rod;
	public static Item stone_gear;

	public static Item tin_arrow;
	public static Item tin_axe;
	public static Item tin_boots;
	public static Item tin_bolt;
	public static Item tin_bow;
	public static Item tin_chestplate;
	public static Item tin_crackhammer;
	public static Item tin_crossbow;
	public static Item tin_door;
	public static Item tin_helmet;
	public static Item tin_hoe;
	public static Item tin_horsearmor;
	public static Item tin_ingot;
	public static Item tin_leggings;
	public static Item tin_nugget;
	public static Item tin_pickaxe;
	public static Item tin_powder;
	public static Item tin_shears;
	public static Item tin_shovel;
	public static Item tin_smallpowder;
	public static Item tin_sword;
	public static Item tin_rod;
	public static Item tin_gear;

	protected static Item tin_slab;
	//protected static Item tin_shield;

	protected static Item tin_crystal;
	protected static Item tin_shard;
	protected static Item tin_clump;
	protected static Item tin_powder_dirty;

	protected static Item tin_dense_plate;
	protected static Item tin_crushed;
	protected static Item tin_crushed_purified;

	public static Item wood_crackhammer;
	public static Item wood_rod;
	public static Item wood_gear;

	public static Item zinc_arrow;
	public static Item zinc_axe;
	public static Item zinc_boots;
	public static Item zinc_bolt;
	public static Item zinc_bow;
	public static Item zinc_chestplate;
	public static Item zinc_crackhammer;
	public static Item zinc_crossbow;
	public static Item zinc_door;
	public static Item zinc_helmet;
	public static Item zinc_hoe;
	public static Item zinc_horsearmor;
	public static Item zinc_ingot;
	public static Item zinc_leggings;
	public static Item zinc_nugget;
	public static Item zinc_powder;
	public static Item zinc_shears;
	public static Item zinc_shovel;
	public static Item zinc_smallpowder;
	public static Item zinc_sword;
	public static Item zinc_rod;
	public static Item zinc_gear;

	protected static Item zinc_slab;
	//protected static Item zinc_shield;

	protected static Item zinc_crystal;
	protected static Item zinc_shard;
	protected static Item zinc_clump;
	protected static Item zinc_powder_dirty;

	protected static Item zinc_dense_plate;
	protected static Item zinc_crushed;
	protected static Item zinc_crushed_purified;

	private static boolean initDone = false;

	private static Map<Item, String> itemRegistry = new HashMap<>();
	private static Map<String, Item> allItems = new HashMap<>();
	private static Map<MetalMaterial, List<Item>> itemsByMetal = new HashMap<>();

	private static Map<BlockDoor, Item> doorMap = new HashMap<>();

	private static Map<Class<?>, Integer> classSortingValues = new HashMap<>();
	private static Map<MetalMaterial, Integer> materialSortingValues = new HashMap<>();

	/**
	 * Gets an item by its name. The name is the name as it is registered in the
	 * GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "basemetals.")
	 * @param name The name of the item in question
	 * @return The item matching that name, or null if there isn't one
	 */
	public static Item getItemByName(String name) {
		return allItems.get(name);
	}

	/**
	 * This is the reverse of the getItemByName(...) method, returning the
	 * registered name of an item instance (Base Metals items only).
	 * @param i The item in question
	 * @return The name of the item, or null if the item is not a Base Metals
	 * item.
	 */
	public static String getNameOfItem(Item i) {
		return itemRegistry.get(i);
	}

	/**
	 * Gets a map of all items added, sorted by metal
	 * @return An unmodifiable map of added items catagorized by metal material
	 */
	public static Map<MetalMaterial, List<Item>> getItemsByMetal() {
		return Collections.unmodifiableMap(itemsByMetal);
	}

	//public static UniversalBucket universal_bucket; // now automatically added by Forge

	/**
	 * Gets the inventory item corresponding to a given door block
	 *
	 * @param b The door block
	 * @return The item that the player should use to place that kind of door
	 */
	public static Item getDoorItemForBlock(BlockMetalDoor b) {
		return doorMap.get(b);
	}

	/**
	 *
	 */
	public static void init() {
		if(initDone)
			return;

		cyano.basemetals.init.Blocks.init();

		try {
			expandCombatArrays(net.minecraft.item.ItemAxe.class);
		} catch (IllegalAccessException | NoSuchFieldException ex) {
			FMLLog.severe("Error modifying item classes: %s", ex);
		}

		adamantine_axe = createAxe(Materials.adamantine);
		adamantine_boots = createBoots(Materials.adamantine);
		adamantine_chestplate = createChestplate(Materials.adamantine);
		adamantine_crackhammer = createCrackhammer(Materials.adamantine);
		adamantine_door = createDoor(Materials.adamantine, Blocks.adamantine_door);
		adamantine_helmet = createHelmet(Materials.adamantine);
		adamantine_hoe = createHoe(Materials.adamantine);
		adamantine_ingot = createIngot(Materials.adamantine);
		adamantine_leggings = createLeggings(Materials.adamantine);
		adamantine_nugget = createNugget(Materials.adamantine);
		adamantine_pickaxe = createPickaxe(Materials.adamantine);
		adamantine_powder = createPowder(Materials.adamantine);
		adamantine_shovel = createShovel(Materials.adamantine);
		adamantine_sword = createSword(Materials.adamantine);
		adamantine_rod = createRod(Materials.adamantine);
		adamantine_gear = createGear(Materials.adamantine);

		adamantine_slab = createSlab(Materials.adamantine, Blocks.adamantine_slab, Blocks.adamantine_slab, Blocks.double_adamantine_slab);
		//adamantine_shield = createShield(Materials.);

		adamantine_crystal = createCrystal(Materials.adamantine);
		adamantine_shard = createShard(Materials.adamantine);
		adamantine_clump = createClump(Materials.adamantine);
		adamantine_powder_dirty = createDirtyPowder(Materials.adamantine);

		adamantine_dense_plate = createDensePlate(Materials.adamantine);
		adamantine_crushed = createCrushed(Materials.adamantine);
		adamantine_crushed_purified = createCrushedPurified(Materials.adamantine);

		aquarium_axe = createAxe(Materials.aquarium);
		aquarium_blend = createBlend(Materials.aquarium);
		aquarium_boots = createBoots(Materials.aquarium);
		aquarium_chestplate = createChestplate(Materials.aquarium);
		aquarium_crackhammer = createCrackhammer(Materials.aquarium);
		aquarium_door = createDoor(Materials.aquarium, Blocks.aquarium_door);
		aquarium_helmet = createHelmet(Materials.aquarium);
		aquarium_hoe = createHoe(Materials.aquarium);
		aquarium_ingot = createIngot(Materials.aquarium);
		aquarium_leggings = createLeggings(Materials.aquarium);
		aquarium_nugget = createNugget(Materials.aquarium);
		aquarium_pickaxe = createPickaxe(Materials.aquarium);
		aquarium_powder = createPowder(Materials.aquarium);
		aquarium_shovel = createShovel(Materials.aquarium);
		aquarium_sword = createSword(Materials.aquarium);
		aquarium_rod = createRod(Materials.aquarium);
		aquarium_gear = createGear(Materials.aquarium);

		aquarium_slab = createSlab(Materials.aquarium, Blocks.aquarium_slab, Blocks.aquarium_slab, Blocks.double_aquarium_slab);
		//aquarium_shield = createShield(Materials.aquarium);

		aquarium_crystal = createCrystal(Materials.aquarium);
		aquarium_shard = createShard(Materials.aquarium);
		aquarium_clump = createClump(Materials.aquarium);
		aquarium_powder_dirty = createDirtyPowder(Materials.aquarium);

		aquarium_dense_plate = createDensePlate(Materials.aquarium);
		aquarium_crushed = createCrushed(Materials.aquarium);
		aquarium_crushed_purified = createCrushedPurified(Materials.aquarium);

		brass_axe = createAxe(Materials.brass);
		brass_blend = createBlend(Materials.brass);
		brass_boots = createBoots(Materials.brass);
		brass_chestplate = createChestplate(Materials.brass);
		brass_crackhammer = createCrackhammer(Materials.brass);
		brass_door = createDoor(Materials.brass,Blocks.brass_door);
		brass_helmet = createHelmet(Materials.brass);
		brass_hoe = createHoe(Materials.brass);
		brass_ingot = createIngot(Materials.brass);
		brass_leggings = createLeggings(Materials.brass);
		brass_nugget = createNugget(Materials.brass);
		brass_pickaxe = createPickaxe(Materials.brass);
		brass_powder = createPowder(Materials.brass);
		brass_shovel = createShovel(Materials.brass);
		brass_sword = createSword(Materials.brass);
		brass_rod = createRod(Materials.brass);
		brass_gear = createGear(Materials.brass);

		brass_slab = createSlab(Materials.brass, Blocks.brass_slab, Blocks.brass_slab, Blocks.double_brass_slab);
		//brass_shield = createShield(Materials.brass);

		brass_crystal = createCrystal(Materials.brass);
		brass_shard = createShard(Materials.brass);
		brass_clump = createClump(Materials.brass);
		brass_powder_dirty = createDirtyPowder(Materials.brass);

		brass_dense_plate = createDensePlate(Materials.brass);
		brass_crushed = createCrushed(Materials.brass);
		brass_crushed_purified = createCrushedPurified(Materials.brass);

		bronze_axe = createAxe(Materials.bronze);
		bronze_blend = createBlend(Materials.bronze);
		bronze_boots = createBoots(Materials.bronze);
		bronze_chestplate = createChestplate(Materials.bronze);
		bronze_crackhammer = createCrackhammer(Materials.bronze);
		bronze_door = createDoor(Materials.bronze,Blocks.bronze_door);
		bronze_helmet = createHelmet(Materials.bronze);
		bronze_hoe = createHoe(Materials.bronze);
		bronze_ingot = createIngot(Materials.bronze);
		bronze_leggings = createLeggings(Materials.bronze);
		bronze_nugget = createNugget(Materials.bronze);
		bronze_pickaxe = createPickaxe(Materials.bronze);
		bronze_powder = createPowder(Materials.bronze);
		bronze_shovel = createShovel(Materials.bronze);
		bronze_sword = createSword(Materials.bronze);
		bronze_rod = createRod(Materials.bronze);
		bronze_gear = createGear(Materials.bronze);

		bronze_slab = createSlab(Materials.bronze, Blocks.bronze_slab, Blocks.bronze_slab, Blocks.double_bronze_slab);
		//bronze_shield = createShield(Materials.bronze);

		bronze_crystal = createCrystal(Materials.bronze);
		bronze_shard = createShard(Materials.bronze);
		bronze_clump = createClump(Materials.bronze);
		bronze_powder_dirty = createDirtyPowder(Materials.bronze);

		bronze_dense_plate = createDensePlate(Materials.bronze);
		bronze_crushed = createCrushed(Materials.bronze);
		bronze_crushed_purified = createCrushedPurified(Materials.bronze);
		
		carbon_powder = new Item().setRegistryName(BaseMetals.MODID, "carbon_powder").setUnlocalizedName(BaseMetals.MODID + "." + "carbon_powder").setCreativeTab(ItemGroups.tab_items);
		GameRegistry.register(carbon_powder);
		itemRegistry.put(carbon_powder, "carbon_powder");
		OreDictionary.registerOre("dustCoal", carbon_powder);
		OreDictionary.registerOre("dustCarbon", carbon_powder);
		
		coldiron_axe = createAxe(Materials.coldiron);
		coldiron_boots = createBoots(Materials.coldiron);
		coldiron_chestplate = createChestplate(Materials.coldiron);
		coldiron_crackhammer = createCrackhammer(Materials.coldiron);
		coldiron_door = createDoor(Materials.coldiron,Blocks.coldiron_door);
		coldiron_helmet = createHelmet(Materials.coldiron);
		coldiron_hoe = createHoe(Materials.coldiron);
		coldiron_ingot = createIngot(Materials.coldiron);
		coldiron_leggings = createLeggings(Materials.coldiron);
		coldiron_nugget = createNugget(Materials.coldiron);
		coldiron_pickaxe = createPickaxe(Materials.coldiron);
		coldiron_powder = createPowder(Materials.coldiron);
		coldiron_shovel = createShovel(Materials.coldiron);
		coldiron_sword = createSword(Materials.coldiron);
		coldiron_rod = createRod(Materials.coldiron);
		coldiron_gear = createGear(Materials.coldiron);

		coldiron_slab = createSlab(Materials.coldiron, Blocks.coldiron_slab, Blocks.coldiron_slab, Blocks.double_coldiron_slab);
		//coldiron_shield = createShield(Materials.coldiron);

		coldiron_crystal = createCrystal(Materials.coldiron);
		coldiron_shard = createShard(Materials.coldiron);
		coldiron_clump = createClump(Materials.coldiron);
		coldiron_powder_dirty = createDirtyPowder(Materials.coldiron);

		coldiron_dense_plate = createDensePlate(Materials.coldiron);
		coldiron_crushed = createCrushed(Materials.coldiron);
		coldiron_crushed_purified = createCrushedPurified(Materials.coldiron);

		copper_axe = createAxe(Materials.copper);
		copper_boots = createBoots(Materials.copper);
		copper_chestplate = createChestplate(Materials.copper);
		copper_crackhammer = createCrackhammer(Materials.copper);
		copper_door = createDoor(Materials.copper,Blocks.copper_door);
		copper_helmet = createHelmet(Materials.copper);
		copper_hoe = createHoe(Materials.copper);
		copper_ingot = createIngot(Materials.copper);
		copper_leggings = createLeggings(Materials.copper);
		copper_nugget = createNugget(Materials.copper);
		copper_pickaxe = createPickaxe(Materials.copper);
		copper_powder = createPowder(Materials.copper);
		copper_shovel = createShovel(Materials.copper);
		copper_sword = createSword(Materials.copper);
		copper_rod = createRod(Materials.copper);
		copper_gear = createGear(Materials.copper);

		copper_slab = createSlab(Materials.copper, Blocks.copper_slab, Blocks.copper_slab, Blocks.double_copper_slab);
		//copper_shield = createShield(Materials.copper);

		copper_crystal = createCrystal(Materials.copper);
		copper_shard = createShard(Materials.copper);
		copper_clump = createClump(Materials.copper);
		copper_powder_dirty = createDirtyPowder(Materials.copper);

		copper_dense_plate = createDensePlate(Materials.copper);
		copper_crushed = createCrushed(Materials.copper);
		copper_crushed_purified = createCrushedPurified(Materials.copper);

		cupronickel_axe = createAxe(Materials.cupronickel);
		cupronickel_blend = createBlend(Materials.cupronickel);
		cupronickel_boots = createBoots(Materials.cupronickel);
		cupronickel_chestplate = createChestplate(Materials.cupronickel);
		cupronickel_crackhammer = createCrackhammer(Materials.cupronickel);
		cupronickel_door = createDoor(Materials.cupronickel, Blocks.cupronickel_door);
		cupronickel_helmet = createHelmet(Materials.cupronickel);
		cupronickel_hoe = createHoe(Materials.cupronickel);
		cupronickel_ingot = createIngot(Materials.cupronickel);
		cupronickel_leggings = createLeggings(Materials.cupronickel);
		cupronickel_nugget = createNugget(Materials.cupronickel);
		cupronickel_pickaxe = createPickaxe(Materials.cupronickel);
		cupronickel_powder = createPowder(Materials.cupronickel);
		cupronickel_shovel = createShovel(Materials.cupronickel);
		cupronickel_sword = createSword(Materials.cupronickel);
		cupronickel_rod = createRod(Materials.cupronickel);
		cupronickel_gear = createGear(Materials.cupronickel);

		cupronickel_slab = createSlab(Materials.cupronickel, Blocks.cupronickel_slab, Blocks.cupronickel_slab, Blocks.double_cupronickel_slab);
		//cupronickel_shield = createShield(Materials.cupronickel);

		cupronickel_crystal = createCrystal(Materials.cupronickel);
		cupronickel_shard = createShard(Materials.cupronickel);
		cupronickel_clump = createClump(Materials.cupronickel);
		cupronickel_powder_dirty = createDirtyPowder(Materials.cupronickel);

		cupronickel_dense_plate = createDensePlate(Materials.cupronickel);
		cupronickel_crushed = createCrushed(Materials.cupronickel);
		cupronickel_crushed_purified = createCrushedPurified(Materials.cupronickel);

		diamond_crackhammer = createCrackhammer(Materials.vanilla_diamond);
		diamond_gear = createGear(Materials.vanilla_diamond);

		electrum_axe = createAxe(Materials.electrum);
		electrum_blend = createBlend(Materials.electrum);
		electrum_boots = createBoots(Materials.electrum);
		electrum_chestplate = createChestplate(Materials.electrum);
		electrum_crackhammer = createCrackhammer(Materials.electrum);
		electrum_door = createDoor(Materials.electrum,Blocks.electrum_door);
		electrum_helmet = createHelmet(Materials.electrum);
		electrum_hoe = createHoe(Materials.electrum);
		electrum_ingot = createIngot(Materials.electrum);
		electrum_leggings = createLeggings(Materials.electrum);
		electrum_nugget = createNugget(Materials.electrum);
		electrum_pickaxe = createPickaxe(Materials.electrum);
		electrum_powder = createPowder(Materials.electrum);
		electrum_shovel = createShovel(Materials.electrum);
		electrum_sword = createSword(Materials.electrum);
		electrum_rod = createRod(Materials.electrum);
		electrum_gear = createGear(Materials.electrum);

		electrum_slab = createSlab(Materials.electrum, Blocks.electrum_slab, Blocks.electrum_slab, Blocks.double_electrum_slab);
		//electrum_shield = createShield(Materials.electrum);

		electrum_crystal = createCrystal(Materials.electrum);
		electrum_shard = createShard(Materials.electrum);
		electrum_clump = createClump(Materials.electrum);
		electrum_powder_dirty = createDirtyPowder(Materials.electrum);

		electrum_dense_plate = createDensePlate(Materials.electrum);
		electrum_crushed = createCrushed(Materials.electrum);
		electrum_crushed_purified = createCrushedPurified(Materials.electrum);

		gold_crackhammer = createCrackhammer(Materials.vanilla_gold);
		gold_powder = createPowder(Materials.vanilla_gold);
		gold_rod = createRod(Materials.vanilla_gold);
		gold_gear = createGear(Materials.vanilla_gold);

		invar_axe = createAxe(Materials.invar);
		invar_blend = createBlend(Materials.invar);
		invar_boots = createBoots(Materials.invar);
		invar_chestplate = createChestplate(Materials.invar);
		invar_crackhammer = createCrackhammer(Materials.invar);
		invar_door = createDoor(Materials.invar, Blocks.invar_door);
		invar_helmet = createHelmet(Materials.invar);
		invar_hoe = createHoe(Materials.invar);
		invar_ingot = createIngot(Materials.invar);
		invar_leggings = createLeggings(Materials.invar);
		invar_nugget = createNugget(Materials.invar);
		invar_pickaxe = createPickaxe(Materials.invar);
		invar_powder = createPowder(Materials.invar);
		invar_shovel = createShovel(Materials.invar);
		invar_sword = createSword(Materials.invar);
		invar_rod = createRod(Materials.invar);
		invar_gear = createGear(Materials.invar);

		invar_slab = createSlab(Materials.invar, Blocks.invar_slab, Blocks.invar_slab, Blocks.double_invar_slab);
		//invar_shield = createShield(Materials.invar);

		invar_crystal = createCrystal(Materials.invar);
		invar_shard = createShard(Materials.invar);
		invar_clump = createClump(Materials.invar);
		invar_powder_dirty = createDirtyPowder(Materials.invar);

		invar_dense_plate = createDensePlate(Materials.invar);
		invar_crushed = createCrushed(Materials.invar);
		invar_crushed_purified = createCrushedPurified(Materials.invar);

		iron_crackhammer = createCrackhammer(Materials.vanilla_iron);
		iron_nugget = createNugget(Materials.vanilla_iron);
		iron_powder = createPowder(Materials.vanilla_iron);
		iron_rod = createRod(Materials.vanilla_iron);
		iron_gear = createGear(Materials.vanilla_iron);

		lead_axe = createAxe(Materials.lead);
		lead_boots = createBoots(Materials.lead);
		lead_chestplate = createChestplate(Materials.lead);
		lead_crackhammer = createCrackhammer(Materials.lead);
		lead_door = createDoor(Materials.lead,Blocks.lead_door);
		lead_helmet = createHelmet(Materials.lead);
		lead_hoe = createHoe(Materials.lead);
		lead_ingot = createIngot(Materials.lead);
		lead_leggings = createLeggings(Materials.lead);
		lead_nugget = createNugget(Materials.lead);
		lead_pickaxe = createPickaxe(Materials.lead);
		lead_powder = createPowder(Materials.lead);
		lead_shovel = createShovel(Materials.lead);
		lead_sword = createSword(Materials.lead);
		lead_rod = createRod(Materials.lead);
		lead_gear = createGear(Materials.lead);

		lead_slab = createSlab(Materials.lead, Blocks.lead_slab, Blocks.lead_slab, Blocks.double_lead_slab);
		//lead_shield = createShield(Materials.lead);

		lead_crystal = createCrystal(Materials.lead);
		lead_shard = createShard(Materials.lead);
		lead_clump = createClump(Materials.lead);
		lead_powder_dirty = createDirtyPowder(Materials.lead);

		lead_dense_plate = createDensePlate(Materials.lead);
		lead_crushed = createCrushed(Materials.lead);
		lead_crushed_purified = createCrushedPurified(Materials.lead);

		platinum_axe = createAxe(Materials.platinum);
		platinum_boots = createBoots(Materials.platinum);
		platinum_chestplate = createChestplate(Materials.platinum);
		platinum_crackhammer = createCrackhammer(Materials.platinum);
		platinum_door = createDoor(Materials.platinum, Blocks.platinum_door);
		platinum_helmet = createHelmet(Materials.platinum);
		platinum_hoe = createHoe(Materials.platinum);
		platinum_ingot = createIngot(Materials.platinum);
		platinum_leggings = createLeggings(Materials.platinum);
		platinum_nugget = createNugget(Materials.platinum);
		platinum_pickaxe = createPickaxe(Materials.platinum);
		platinum_powder = createPowder(Materials.platinum);
		platinum_shovel = createShovel(Materials.platinum);
		platinum_sword = createSword(Materials.platinum);
		platinum_rod = createRod(Materials.platinum);
		platinum_gear = createGear(Materials.platinum);

		platinum_slab = createSlab(Materials.platinum, Blocks.platinum_slab, Blocks.platinum_slab, Blocks.double_platinum_slab);
		//platinum_shield = createShield(Materials.platinum);

		platinum_crystal = createCrystal(Materials.platinum);
		platinum_shard = createShard(Materials.platinum);
		platinum_clump = createClump(Materials.platinum);
		platinum_powder_dirty = createDirtyPowder(Materials.platinum);

		platinum_dense_plate = createDensePlate(Materials.platinum);
		platinum_crushed = createCrushed(Materials.platinum);
		platinum_crushed_purified = createCrushedPurified(Materials.platinum);

		stone_crackhammer = createCrackhammer(Materials.vanilla_stone);
		stone_rod = createRod(Materials.vanilla_stone);
		stone_gear = createGear(Materials.vanilla_stone);

		wood_crackhammer = createCrackhammer(Materials.vanilla_wood);
		wood_gear = createGear(Materials.vanilla_wood);
		
		// mercury is special
		mercury_ingot = new Item().setRegistryName(BaseMetals.MODID, "mercury_ingot").setUnlocalizedName(BaseMetals.MODID + "." + "mercury_ingot").setCreativeTab(ItemGroups.tab_items);
		GameRegistry.register(mercury_ingot);
		itemRegistry.put(mercury_ingot, "mercury_ingot");
		OreDictionary.registerOre("ingotMercury", mercury_ingot);
		OreDictionary.registerOre("quicksilver", mercury_ingot);
		mercury_powder = new Item().setRegistryName(BaseMetals.MODID, "mercury_powder").setUnlocalizedName(BaseMetals.MODID + "." + "mercury_powder").setCreativeTab(ItemGroups.tab_items);
		GameRegistry.register(mercury_powder);
		itemRegistry.put(mercury_powder, "mercury_powder");
		OreDictionary.registerOre("dustMercury", mercury_powder);

		//mercury_crystal = createCrystal(Materials.mercury);
		//mercury_shard = createShard(Materials.mercury);
		//mercury_clump = createClump(Materials.mercury);
		//mercury_powder_dirty = createDirtyPowder(Materials.mercury);

		//mercury_crushed = createCrushed(Materials.mercury);
		//mercury_crushed_purified = createCrushedPurified(Materials.mercury);
		
		mithril_axe = createAxe(Materials.mithril);
		mithril_blend = createBlend(Materials.mithril);
		mithril_boots = createBoots(Materials.mithril);
		mithril_chestplate = createChestplate(Materials.mithril);
		mithril_crackhammer = createCrackhammer(Materials.mithril);
		mithril_door = createDoor(Materials.mithril, Blocks.mithril_door);
		mithril_helmet = createHelmet(Materials.mithril);
		mithril_hoe = createHoe(Materials.mithril);
		mithril_ingot = createIngot(Materials.mithril);
		mithril_leggings = createLeggings(Materials.mithril);
		mithril_nugget = createNugget(Materials.mithril);
		mithril_pickaxe = createPickaxe(Materials.mithril);
		mithril_powder = createPowder(Materials.mithril);
		mithril_shovel = createShovel(Materials.mithril);
		mithril_sword = createSword(Materials.mithril);
		mithril_rod = createRod(Materials.mithril);
		mithril_gear = createGear(Materials.mithril);

		mithril_slab = createSlab(Materials.mithril, Blocks.mithril_slab, Blocks.mithril_slab, Blocks.double_mithril_slab);
		//mithril_shield = createShield(Materials.mithril);

		mithril_crystal = createCrystal(Materials.mithril);
		mithril_shard = createShard(Materials.mithril);
		mithril_clump = createClump(Materials.mithril);
		mithril_powder_dirty = createDirtyPowder(Materials.mithril);

		mithril_dense_plate = createDensePlate(Materials.mithril);
		mithril_crushed = createCrushed(Materials.mithril);
		mithril_crushed_purified = createCrushedPurified(Materials.mithril);

		nickel_axe = createAxe(Materials.nickel);
		nickel_boots = createBoots(Materials.nickel);
		nickel_chestplate = createChestplate(Materials.nickel);
		nickel_crackhammer = createCrackhammer(Materials.nickel);
		nickel_door = createDoor(Materials.nickel, Blocks.nickel_door);
		nickel_helmet = createHelmet(Materials.nickel);
		nickel_hoe = createHoe(Materials.nickel);
		nickel_ingot = createIngot(Materials.nickel);
		nickel_leggings = createLeggings(Materials.nickel);
		nickel_nugget = createNugget(Materials.nickel);
		nickel_pickaxe = createPickaxe(Materials.nickel);
		nickel_powder = createPowder(Materials.nickel);
		nickel_shovel = createShovel(Materials.nickel);
		nickel_sword = createSword(Materials.nickel);
		nickel_rod = createRod(Materials.nickel);
		nickel_gear = createGear(Materials.nickel);

		nickel_slab = createSlab(Materials.nickel, Blocks.nickel_slab, Blocks.nickel_slab, Blocks.double_nickel_slab);
		//nickel_shield = createShield(Materials.nickel);

		nickel_crystal = createCrystal(Materials.nickel);
		nickel_shard = createShard(Materials.nickel);
		nickel_clump = createClump(Materials.nickel);
		nickel_powder_dirty = createDirtyPowder(Materials.nickel);

		nickel_dense_plate = createDensePlate(Materials.nickel);
		nickel_crushed = createCrushed(Materials.nickel);
		nickel_crushed_purified = createCrushedPurified(Materials.nickel);

		silver_axe = createAxe(Materials.silver);
		silver_boots = createBoots(Materials.silver);
		silver_chestplate = createChestplate(Materials.silver);
		silver_crackhammer = createCrackhammer(Materials.silver);
		silver_door = createDoor(Materials.silver,Blocks.silver_door);
		silver_helmet = createHelmet(Materials.silver);
		silver_hoe = createHoe(Materials.silver);
		silver_ingot = createIngot(Materials.silver);
		silver_leggings = createLeggings(Materials.silver);
		silver_nugget = createNugget(Materials.silver);
		silver_pickaxe = createPickaxe(Materials.silver);
		silver_powder = createPowder(Materials.silver);
		silver_shovel = createShovel(Materials.silver);
		silver_sword = createSword(Materials.silver);
		silver_rod = createRod(Materials.silver);
		silver_gear = createGear(Materials.silver);

		silver_slab = createSlab(Materials.silver, Blocks.silver_slab, Blocks.silver_slab, Blocks.double_silver_slab);
		//silver_shield = createShield(Materials.silver);

		silver_crystal = createCrystal(Materials.silver);
		silver_shard = createShard(Materials.silver);
		silver_clump = createClump(Materials.silver);
		silver_powder_dirty = createDirtyPowder(Materials.silver);

		silver_dense_plate = createDensePlate(Materials.silver);
		silver_crushed = createCrushed(Materials.silver);
		silver_crushed_purified = createCrushedPurified(Materials.silver);

		starsteel_axe = createAxe(Materials.starsteel);
		starsteel_boots = createBoots(Materials.starsteel);
		starsteel_chestplate = createChestplate(Materials.starsteel);
		starsteel_crackhammer = createCrackhammer(Materials.starsteel);
		starsteel_door = createDoor(Materials.starsteel,Blocks.starsteel_door);
		starsteel_helmet = createHelmet(Materials.starsteel);
		starsteel_hoe = createHoe(Materials.starsteel);
		starsteel_ingot = createIngot(Materials.starsteel);
		starsteel_leggings = createLeggings(Materials.starsteel);
		starsteel_nugget = createNugget(Materials.starsteel);
		starsteel_pickaxe = createPickaxe(Materials.starsteel);
		starsteel_powder = createPowder(Materials.starsteel);
		starsteel_shovel = createShovel(Materials.starsteel);
		starsteel_sword = createSword(Materials.starsteel);
		starsteel_rod = createRod(Materials.starsteel);
		starsteel_gear = createGear(Materials.starsteel);

		starsteel_slab = createSlab(Materials.starsteel, Blocks.starsteel_slab, Blocks.starsteel_slab, Blocks.double_starsteel_slab);
		//starsteel_shield = createShield(Materials.starsteel);

		starsteel_crystal = createCrystal(Materials.starsteel);
		starsteel_shard = createShard(Materials.starsteel);
		starsteel_clump = createClump(Materials.starsteel);
		starsteel_powder_dirty = createDirtyPowder(Materials.starsteel);

		starsteel_dense_plate = createDensePlate(Materials.starsteel);
		starsteel_crushed = createCrushed(Materials.starsteel);
		starsteel_crushed_purified = createCrushedPurified(Materials.starsteel);

		steel_axe = createAxe(Materials.steel);
		steel_blend = createBlend(Materials.steel);
		steel_boots = createBoots(Materials.steel);
		steel_chestplate = createChestplate(Materials.steel);
		steel_crackhammer = createCrackhammer(Materials.steel);
		steel_door = createDoor(Materials.steel,Blocks.steel_door);
		steel_helmet = createHelmet(Materials.steel);
		steel_hoe = createHoe(Materials.steel);
		steel_ingot = createIngot(Materials.steel);
		steel_leggings = createLeggings(Materials.steel);
		steel_nugget = createNugget(Materials.steel);
		steel_pickaxe = createPickaxe(Materials.steel);
		steel_powder = createPowder(Materials.steel);
		steel_shovel = createShovel(Materials.steel);
		steel_sword = createSword(Materials.steel);
		steel_rod = createRod(Materials.steel);
		steel_gear = createGear(Materials.steel);

		steel_slab = createSlab(Materials.steel, Blocks.steel_slab, Blocks.steel_slab, Blocks.double_steel_slab);
		//steel_shield = createShield(Materials.steel);

		steel_crystal = createCrystal(Materials.steel);
		steel_shard = createShard(Materials.steel);
		steel_clump = createClump(Materials.steel);
		steel_powder_dirty = createDirtyPowder(Materials.steel);

		steel_dense_plate = createDensePlate(Materials.steel);
		steel_crushed = createCrushed(Materials.steel);
		steel_crushed_purified = createCrushedPurified(Materials.steel);

		tin_axe = createAxe(Materials.tin);
		tin_boots = createBoots(Materials.tin);
		tin_chestplate = createChestplate(Materials.tin);
		tin_crackhammer = createCrackhammer(Materials.tin);
		tin_door = createDoor(Materials.tin, Blocks.tin_door);
		tin_helmet = createHelmet(Materials.tin);
		tin_hoe = createHoe(Materials.tin);
		tin_ingot = createIngot(Materials.tin);
		tin_leggings = createLeggings(Materials.tin);
		tin_nugget = createNugget(Materials.tin);
		tin_pickaxe = createPickaxe(Materials.tin);
		tin_powder = createPowder(Materials.tin);
		tin_shovel = createShovel(Materials.tin);
		tin_sword = createSword(Materials.tin);
		tin_rod = createRod(Materials.tin);
		tin_gear = createGear(Materials.tin);

		tin_slab = createSlab(Materials.tin, Blocks.tin_slab, Blocks.tin_slab, Blocks.double_tin_slab);
		//tin_shield = createShield(Materials.tin);

		tin_crystal = createCrystal(Materials.tin);
		tin_shard = createShard(Materials.tin);
		tin_clump = createClump(Materials.tin);
		tin_powder_dirty = createDirtyPowder(Materials.tin);

		tin_dense_plate = createDensePlate(Materials.tin);
		tin_crushed = createCrushed(Materials.tin);
		tin_crushed_purified = createCrushedPurified(Materials.tin);

		zinc_axe = createAxe(Materials.zinc);
		zinc_boots = createBoots(Materials.zinc);
		zinc_chestplate = createChestplate(Materials.zinc);
		zinc_crackhammer = createCrackhammer(Materials.zinc);
		zinc_door = createDoor(Materials.zinc, Blocks.zinc_door);
		zinc_helmet = createHelmet(Materials.zinc);
		zinc_hoe = createHoe(Materials.zinc);
		zinc_ingot = createIngot(Materials.zinc);
		zinc_leggings = createLeggings(Materials.zinc);
		zinc_nugget = createNugget(Materials.zinc);
		tin_pickaxe = createPickaxe(Materials.zinc);
		zinc_powder = createPowder(Materials.zinc);
		zinc_shovel = createShovel(Materials.zinc);
		zinc_sword = createSword(Materials.zinc);
		zinc_rod = createRod(Materials.zinc);
		zinc_gear = createGear(Materials.zinc);

		zinc_slab = createSlab(Materials.zinc, Blocks.zinc_slab, Blocks.zinc_slab, Blocks.double_zinc_slab);
		//zinc_shield = createShield(Materials.zinc);

		zinc_crystal = createCrystal(Materials.zinc);
		zinc_shard = createShard(Materials.zinc);
		zinc_clump = createClump(Materials.zinc);
		zinc_powder_dirty = createDirtyPowder(Materials.zinc);

		zinc_dense_plate = createDensePlate(Materials.zinc);
		zinc_crushed = createCrushed(Materials.zinc);
		zinc_crushed_purified = createCrushedPurified(Materials.zinc);

		// TODO: Make this support multiple oredicts
		for(final Item i : itemRegistry.keySet()) {
			allItems.put(itemRegistry.get(i), i);
			if(i instanceof IOreDictionaryEntry)
				OreDictionary.registerOre(((IOreDictionaryEntry)i).getOreDictionaryName(), i);
		}

		int ss = 0;
		classSortingValues.put(BlockMetalOre.class, ++ss * 10000);
		classSortingValues.put(BlockMetalBlock.class, ++ss * 10000);
		classSortingValues.put(BlockMetalPlate.class, ++ss * 10000);
		classSortingValues.put(BlockMetalBars.class, ++ss * 10000);
		classSortingValues.put(BlockMetalDoor.class, ++ss * 10000);
		classSortingValues.put(BlockMetalTrapDoor.class, ++ss * 10000);
		classSortingValues.put(InteractiveFluidBlock.class, ++ss * 10000);
		classSortingValues.put(ItemMetalIngot.class, ++ss * 10000);
		classSortingValues.put(ItemMetalNugget.class, ++ss * 10000);
		classSortingValues.put(ItemMetalPowder.class, ++ss * 10000);
		classSortingValues.put(ItemMetalBlend.class, classSortingValues.get(ItemMetalPowder.class));
		classSortingValues.put(ItemMetalSmallPowder.class, ++ss * 10000);
		classSortingValues.put(ItemMetalSmallBlend.class, classSortingValues.get(ItemMetalSmallPowder.class));
		classSortingValues.put(ItemMetalCrackHammer.class, ++ss * 10000);
		classSortingValues.put(ItemMetalPickaxe.class, ++ss * 10000);
		classSortingValues.put(ItemMetalShovel.class, ++ss * 10000);
		classSortingValues.put(ItemMetalAxe.class, ++ss * 10000);
		classSortingValues.put(ItemMetalHoe.class, ++ss * 10000);
		classSortingValues.put(ItemMetalSword.class, ++ss * 10000);
		classSortingValues.put(ItemMetalArmor.class, ++ss * 10000);
		classSortingValues.put(GenericMetalItem.class, ++ss * 10000);
		classSortingValues.put(ItemMetalArrow.class, ++ss * 10000);
		classSortingValues.put(ItemMetalBolt.class, ++ss * 10000);
		classSortingValues.put(ItemMetalBow.class, ++ss * 10000);
		classSortingValues.put(ItemMetalCrossbow.class, ++ss * 10000);
		classSortingValues.put(ItemMetalFishingRod.class, ++ss * 10000);
		classSortingValues.put(ItemMetalHorseArmor.class, ++ss * 10000);
		classSortingValues.put(ItemMetalShears.class, ++ss * 10000);
		classSortingValues.put(ItemMetalShield.class, ++ss * 10000);
		classSortingValues.put(ItemMetalDoor.class, classSortingValues.get(BlockMetalDoor.class));

		classSortingValues.put(BlockButtonMetal.class, ++ss * 10000);
		classSortingValues.put(BlockMetalSlab.class, ++ss * 10000);
		classSortingValues.put(BlockDoubleMetalSlab.class, ++ss * 10000); // Probably not needed
		classSortingValues.put(BlockHalfMetalSlab.class, ++ss * 10000); // Probably not needed
		classSortingValues.put(BlockMetalLever.class, ++ss * 10000);
		classSortingValues.put(BlockMetalPressurePlate.class, ++ss * 10000);
		classSortingValues.put(BlockMetalStairs.class, ++ss * 10000);
		classSortingValues.put(BlockMetalWall.class, ++ss * 10000);
		classSortingValues.put(BlockMoltenFluid.class, ++ss * 10000);
		classSortingValues.put(ItemMetalSlab.class, ++ss * 10000);

		List<MetalMaterial> metlist = new ArrayList<>(Materials.getAllMetals().size());
		metlist.addAll(Materials.getAllMetals());
		metlist.sort((MetalMaterial a, MetalMaterial b)-> a.getName().compareToIgnoreCase(b.getName()));
		for(int i = 0; i < metlist.size(); i++)
			materialSortingValues.put(metlist.get(i), i * 100);

		initDone = true;
	}

	private static Item registerItem(Item item, String name, MetalMaterial metal, CreativeTabs tab) {
		final ResourceLocation location = new ResourceLocation(BaseMetals.MODID, name);
		item.setRegistryName(location);
		item.setUnlocalizedName(location.toString());
		GameRegistry.register(item);
		itemRegistry.put(item, name);

		if(tab != null) {
			item.setCreativeTab(tab);
		}

		if(metal != null) {
			itemsByMetal.computeIfAbsent(metal, (MetalMaterial g)->new ArrayList<>());
			itemsByMetal.get(metal).add(item);
		}

		return item;
	}

	private static Item createIngot(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalIngot(metal), metal.getName() + "_ingot", metal, ItemGroups.tab_items);
		return i;
	}

	private static Item createNugget(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalNugget(metal), metal.getName() + "_nugget", metal, ItemGroups.tab_items);
		return i;
	}

	private static Item createPowder(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalPowder(metal), metal.getName() + "_powder", metal, ItemGroups.tab_items);
		return i;
	}

	private static Item createBlend(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalBlend(metal), metal.getName() + "_blend", metal, ItemGroups.tab_items);
		return i;
	}

	private static Item createRod(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_rod", metal, ItemGroups.tab_items);
		return i;
	}

	private static Item createGear(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_gear", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("gear" + metal.getCapitalizedName(), i);
		return i;
	}

	private static Item createAxe(MetalMaterial metal) {
		return registerItem(new ItemMetalAxe(metal), metal.getName() + "_axe", metal, ItemGroups.tab_tools);
	}

	private static Item createCrackhammer(MetalMaterial metal) {
		return registerItem(new ItemMetalCrackHammer(metal), metal.getName() + "_crackhammer", metal, ItemGroups.tab_tools);
	}

	private static Item createHoe(MetalMaterial metal) {
		return registerItem(new ItemMetalHoe(metal), metal.getName() + "_hoe", metal, ItemGroups.tab_tools);
	}

	private static Item createPickaxe(MetalMaterial metal) {
		return registerItem(new ItemMetalPickaxe(metal), metal.getName() + "_pickaxe", metal, ItemGroups.tab_tools);
	}

	private static Item createShovel(MetalMaterial metal) {
		return registerItem(new ItemMetalShovel(metal), metal.getName() + "_shovel", metal, ItemGroups.tab_tools);
	}

	private static Item createSword(MetalMaterial metal) {
		return registerItem(new ItemMetalSword(metal), metal.getName() + "_sword", metal, ItemGroups.tab_tools);
	}

	private static Item createHelmet(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createHelmet(metal), metal.getName() + "_helmet", metal, ItemGroups.tab_tools);
	}

	private static Item createChestplate(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createChestplate(metal), metal.getName() + "_chestplate", metal, ItemGroups.tab_tools);
	}

	private static Item createLeggings(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createLeggings(metal), metal.getName() + "_leggings", metal, ItemGroups.tab_tools);
	}

	private static Item createBoots(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createBoots(metal), metal.getName() + "_boots", metal, ItemGroups.tab_tools);
	}

	private static Item createHorsearmor(MetalMaterial metal) {
		return registerItem(new ItemMetalHorseArmor(metal), metal.getName() + "_horsearmor", metal, ItemGroups.tab_tools);
	}

	private static Item createArrow(MetalMaterial metal) {
		return registerItem(new ItemMetalArrow(metal), metal.getName() + "_arrow", metal, ItemGroups.tab_tools);
	}

	private static Item createBolt(MetalMaterial metal) {
		return registerItem(new ItemMetalBolt(metal), metal.getName() + "_bolt", metal, ItemGroups.tab_tools);
	}

	private static Item createBow(MetalMaterial metal) {
		return registerItem(new ItemMetalBow(metal), metal.getName() + "_bow", metal, ItemGroups.tab_tools);
	}

	private static Item createCrossbow(MetalMaterial metal) {
		return registerItem(new ItemMetalCrossbow(metal), metal.getName() + "_crossbow", metal, ItemGroups.tab_tools);
	}

	private static Item createShears(MetalMaterial metal) {
		return registerItem(new ItemMetalShears(metal), metal.getName() + "_shears", metal, ItemGroups.tab_tools);
	}

	private static Item createSmallBlend(MetalMaterial metal) {
		//return registerItem(new ItemMetalSmallBlend(metal), metal.getName() + "_smallblend", metal, ItemGroups.tab_items);
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_smallblend", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("dustTiny" + metal.getCapitalizedName(), i);
		return i;
	}

	private static Item createFishingRod(MetalMaterial metal) {
		return registerItem(new ItemMetalFishingRod(metal), metal.getName() + "_fishing_rod", metal, ItemGroups.tab_tools);
	}

	private static Item createSmallPowder(MetalMaterial metal) {
		//return registerItem(new ItemMetalSmallPowder(metal), metal.getName() + "_smallpowder", metal, ItemGroups.tab_items);
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_smallpowder", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("dustTiny" + metal.getCapitalizedName(), i);
		return i;
	}

	@SuppressWarnings("unused")
	private static Item createShield(MetalMaterial metal) {
		return registerItem(new ItemMetalShield(metal), metal.getName() + "_shield", metal, ItemGroups.tab_items);
	}

	private static Item createCrystal(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_crystal", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("crystal" + metal.getCapitalizedName(), i);
		return i;
	}

	private static Item createShard(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_shard", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("shard" + metal.getCapitalizedName(), i);
		return i;
	}

	private static Item createClump(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_clump", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("clump" + metal.getCapitalizedName(), i);
		return i;
	}

	private static Item createDirtyPowder(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_powder_dirty", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("dustDirty" + metal.getCapitalizedName(), i);
		return i;
	}

	// TODO: Possibly make this a block, 1/2 of the normal plate.
	private static Item createDensePlate(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_dense_plate", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("plateDense" + metal.getCapitalizedName(), i);
		return i;
	}

	private static Item createCrushed(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_crushed", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("crushed" + metal.getCapitalizedName(), i);
		return i;
	}

	private static Item createCrushedPurified(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_crushed_purified", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("crushedPurified" + metal.getCapitalizedName(), i);
		return i;
	}

	private static Item createSlab(MetalMaterial metal, Block block, BlockSlab slab, BlockSlab doubleslab) {
		final ResourceLocation location = new ResourceLocation(BaseMetals.MODID, metal.getName() + "_slab");
		final Item item = new ItemMetalSlab(metal, block, slab, doubleslab);
		registerItem(item, location.getResourcePath(), metal, ItemGroups.tab_blocks);
		//item.setUnlocalizedName(location.toString()); // Hack to set name right
		return item;
	}

	private static Item createDoor(MetalMaterial metal, BlockDoor door) {
		final ResourceLocation location = new ResourceLocation(BaseMetals.MODID, metal.getName() + "_door");
		final Item item = new ItemMetalDoor(door, metal);
		registerItem(item, location.getResourcePath() + "_item", metal, ItemGroups.tab_blocks);
		item.setUnlocalizedName(location.toString()); // Hack to set name right
		doorMap.put(door, item);
		return item;
	}

	/**
	 * Uses reflection to expand the size of the combat damage and attack speed arrays to prevent initialization
	 * index-out-of-bounds errors
	 * @param itemClass The class to modify
	 */
	private static void expandCombatArrays(Class<?> itemClass) throws IllegalAccessException, NoSuchFieldException {
        // WARNING: this method contains black magic
        final int expandedSize = 256;
        Field[] fields = itemClass.getDeclaredFields();
        for(Field f : fields) {
            if(Modifier.isStatic(f.getModifiers())
                    && f.getType().isArray()
                    && f.getType().getComponentType().equals(float.class)) {
                FMLLog.info("%s: Expanding array variable %s.%s to size %s", Thread.currentThread().getStackTrace()[0], itemClass.getSimpleName(), f.getName(), expandedSize);
                f.setAccessible(true); // bypass 'private' key word
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL); // bypass 'final' key word
                float[] newArray = new float[expandedSize];
                Arrays.fill(newArray,0F);
                System.arraycopy(f.get(null), 0, newArray, 0, Array.getLength(f.get(null)));
                f.set(null,newArray);
            }
        }
    }

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static int getSortingValue(ItemStack a) {
		int classVal = 990000;
		int metalVal = 9900;
		if(a.getItem() instanceof ItemBlock && ((ItemBlock)a.getItem()).getBlock() instanceof IMetalObject) {
			classVal = classSortingValues.computeIfAbsent(((ItemBlock)a.getItem()).getBlock().getClass(),
					(Class<?> c)->990000);
			metalVal = materialSortingValues.computeIfAbsent(((IMetalObject)((ItemBlock)a.getItem()).getBlock()).getMetalMaterial(),
					(MetalMaterial m)->9900);
		} else if(a.getItem() instanceof IMetalObject) {
			classVal = classSortingValues.computeIfAbsent(a.getItem().getClass(),
					(Class<?> c)->990000);
			metalVal = materialSortingValues.computeIfAbsent(((IMetalObject)a.getItem()).getMetalMaterial(),
					(MetalMaterial m)->9900);
		}
		return classVal + metalVal + (a.getMetadata() % 100);
	}

	/**
	 * 
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	public static void registerItemRenders(FMLInitializationEvent event) {
		for(final Item i : itemRegistry.keySet()) {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
			.register(i, 0, 
				new ModelResourceLocation(new ResourceLocation(BaseMetals.MODID, itemRegistry.get(i)), "inventory"));
		}
	}

	public static Map<Item, String> getItemRegistry() {
		return itemRegistry;
	}
}
