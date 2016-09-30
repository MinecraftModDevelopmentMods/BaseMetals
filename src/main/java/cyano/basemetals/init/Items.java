package cyano.basemetals.init;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.blocks.*;
import cyano.basemetals.items.*;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.BlockDoor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This class initializes all items in Base Metals and provides some utility
 * methods for looking up items.
 * 
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

	public static Item adamantine_slab;
	public static Item adamantine_shield;

	public static Item adamantine_crystal;
	public static Item adamantine_shard;
	public static Item adamantine_clump;
	public static Item adamantine_powder_dirty;

	public static Item adamantine_dense_plate;
	public static Item adamantine_crushed;
	public static Item adamantine_crushed_purified;

	public static Item antimony_arrow;
	public static Item antimony_axe;
	public static Item antimony_boots;
	public static Item antimony_bolt;
	public static Item antimony_bow;
	public static Item antimony_chestplate;
	public static Item antimony_crackhammer;
	public static Item antimony_crossbow;
	public static Item antimony_door;
	public static Item antimony_fishing_rod;
	public static Item antimony_helmet;
	public static Item antimony_hoe;
	public static Item antimony_horsearmor;
	public static Item antimony_ingot;
	public static Item antimony_leggings;
	public static Item antimony_nugget;
	public static Item antimony_pickaxe;
	public static Item antimony_powder;
	public static Item antimony_shears;
	public static Item antimony_shovel;
	public static Item antimony_smallpowder;
	public static Item antimony_sword;
	public static Item antimony_rod;
	public static Item antimony_gear;

	public static Item antimony_slab;
	public static Item antimony_shield;

	public static Item antimony_crystal;
	public static Item antimony_shard;
	public static Item antimony_clump;
	public static Item antimony_powder_dirty;

	public static Item antimony_dense_plate;
	public static Item antimony_crushed;
	public static Item antimony_crushed_purified;

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
	public static Item aquarium_fishing_rod;
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

	public static Item aquarium_slab;
	public static Item aquarium_shield;

	public static Item aquarium_crystal;
	public static Item aquarium_shard;
	public static Item aquarium_clump;
	public static Item aquarium_powder_dirty;

	public static Item aquarium_dense_plate;
	public static Item aquarium_crushed;
	public static Item aquarium_crushed_purified;

	public static Item bismuth_arrow;
	public static Item bismuth_axe;
	public static Item bismuth_boots;
	public static Item bismuth_bolt;
	public static Item bismuth_bow;
	public static Item bismuth_chestplate;
	public static Item bismuth_crackhammer;
	public static Item bismuth_crossbow;
	public static Item bismuth_door;
	public static Item bismuth_fishing_rod;
	public static Item bismuth_helmet;
	public static Item bismuth_hoe;
	public static Item bismuth_horsearmor;
	public static Item bismuth_ingot;
	public static Item bismuth_leggings;
	public static Item bismuth_nugget;
	public static Item bismuth_pickaxe;
	public static Item bismuth_powder;
	public static Item bismuth_shears;
	public static Item bismuth_shovel;
	public static Item bismuth_smallpowder;
	public static Item bismuth_sword;
	public static Item bismuth_rod;
	public static Item bismuth_gear;

	public static Item bismuth_slab;
	public static Item bismuth_shield;

	public static Item bismuth_crystal;
	public static Item bismuth_shard;
	public static Item bismuth_clump;
	public static Item bismuth_powder_dirty;

	public static Item bismuth_dense_plate;
	public static Item bismuth_crushed;
	public static Item bismuth_crushed_purified;

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
	public static Item brass_fishing_rod;
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

	public static Item brass_slab;
	public static Item brass_shield;

	public static Item brass_crystal;
	public static Item brass_shard;
	public static Item brass_clump;
	public static Item brass_powder_dirty;

	public static Item brass_dense_plate;
	public static Item brass_crushed;
	public static Item brass_crushed_purified;

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
	public static Item bronze_fishing_rod;
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

	public static Item bronze_slab;
	public static Item bronze_shield;

	public static Item bronze_crystal;
	public static Item bronze_shard;
	public static Item bronze_clump;
	public static Item bronze_powder_dirty;

	public static Item bronze_dense_plate;
	public static Item bronze_crushed;
	public static Item bronze_crushed_purified;

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
	public static Item coldiron_fishing_rod;
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

	public static Item coldiron_slab;
	public static Item coldiron_shield;

	public static Item coldiron_crystal;
	public static Item coldiron_shard;
	public static Item coldiron_clump;
	public static Item coldiron_powder_dirty;

	public static Item coldiron_dense_plate;
	public static Item coldiron_crushed;
	public static Item coldiron_crushed_purified;

	public static Item copper_arrow;
	public static Item copper_axe;
	public static Item copper_boots;
	public static Item copper_bolt;
	public static Item copper_bow;
	public static Item copper_chestplate;
	public static Item copper_crackhammer;
	public static Item copper_crossbow;
	public static Item copper_door;
	public static Item copper_fishing_rod;
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

	public static Item copper_slab;
	public static Item copper_shield;

	public static Item copper_dense_plate;
	public static Item copper_crushed;
	public static Item copper_crushed_purified;

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
	public static Item cupronickel_fishing_rod;
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

	public static Item cupronickel_slab;
	public static Item cupronickel_shield;

	public static Item cupronickel_crystal;
	public static Item cupronickel_shard;
	public static Item cupronickel_clump;
	public static Item cupronickel_powder_dirty;

	public static Item cupronickel_dense_plate;
	public static Item cupronickel_crushed;
	public static Item cupronickel_crushed_purified;

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
	public static Item electrum_fishing_rod;
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

	public static Item electrum_slab;
	public static Item electrum_shield;

	public static Item electrum_crystal;
	public static Item electrum_shard;
	public static Item electrum_clump;
	public static Item electrum_powder_dirty;

	public static Item electrum_dense_plate;
	public static Item electrum_crushed;
	public static Item electrum_crushed_purified;

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
	public static Item invar_fishing_rod;
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

	public static Item invar_slab;
	public static Item invar_shield;

	public static Item invar_crystal;
	public static Item invar_shard;
	public static Item invar_clump;
	public static Item invar_powder_dirty;

	public static Item invar_dense_plate;
	public static Item invar_crushed;
	public static Item invar_crushed_purified;

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
	public static Item lead_fishing_rod;
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

	public static Item lead_slab;
	public static Item lead_shield;

	public static Item lead_dense_plate;
	public static Item lead_crushed;
	public static Item lead_crushed_purified;

	public static Item mercury_ingot;
	public static Item mercury_powder;

	public static Item mercury_crystal;
	public static Item mercury_shard;
	public static Item mercury_clump;
	public static Item mercury_powder_dirty;

	public static Item mercury_crushed;
	public static Item mercury_crushed_purified;

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
	public static Item mithril_fishing_rod;
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

	public static Item mithril_slab;
	public static Item mithril_shield;

	public static Item mithril_crystal;
	public static Item mithril_shard;
	public static Item mithril_clump;
	public static Item mithril_powder_dirty;

	public static Item mithril_dense_plate;
	public static Item mithril_crushed;
	public static Item mithril_crushed_purified;

	public static Item nickel_arrow;
	public static Item nickel_axe;
	public static Item nickel_boots;
	public static Item nickel_bolt;
	public static Item nickel_bow;
	public static Item nickel_chestplate;
	public static Item nickel_crackhammer;
	public static Item nickel_crossbow;
	public static Item nickel_door;
	public static Item nickel_fishing_rod;
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

	public static Item nickel_slab;
	public static Item nickel_shield;

	public static Item nickel_crystal;
	public static Item nickel_shard;
	public static Item nickel_clump;
	public static Item nickel_powder_dirty;

	public static Item nickel_dense_plate;
	public static Item nickel_crushed;
	public static Item nickel_crushed_purified;

	public static Item pewter_arrow;
	public static Item pewter_axe;
	public static Item pewter_blend;
	public static Item pewter_boots;
	public static Item pewter_bolt;
	public static Item pewter_bow;
	public static Item pewter_chestplate;
	public static Item pewter_crackhammer;
	public static Item pewter_crossbow;
	public static Item pewter_door;
	public static Item pewter_fishing_rod;
	public static Item pewter_helmet;
	public static Item pewter_hoe;
	public static Item pewter_horsearmor;
	public static Item pewter_ingot;
	public static Item pewter_leggings;
	public static Item pewter_nugget;
	public static Item pewter_pickaxe;
	public static Item pewter_powder;
	public static Item pewter_shears;
	public static Item pewter_shovel;
	public static Item pewter_smallpowder;
	public static Item pewter_sword;
	public static Item pewter_rod;
	public static Item pewter_gear;

	public static Item pewter_slab;
	public static Item pewter_shield;

	public static Item pewter_crystal;
	public static Item pewter_shard;
	public static Item pewter_clump;
	public static Item pewter_powder_dirty;

	public static Item pewter_dense_plate;
	public static Item pewter_crushed;
	public static Item pewter_crushed_purified;

	public static Item platinum_arrow;
	public static Item platinum_axe;
	public static Item platinum_boots;
	public static Item platinum_bolt;
	public static Item platinum_bow;
	public static Item platinum_chestplate;
	public static Item platinum_crackhammer;
	public static Item platinum_crossbow;
	public static Item platinum_door;
	public static Item platinum_fishing_rod;
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

	public static Item platinum_slab;
	public static Item platinum_shield;

	public static Item platinum_crystal;
	public static Item platinum_shard;
	public static Item platinum_clump;
	public static Item platinum_powder_dirty;

	public static Item platinum_dense_plate;
	public static Item platinum_crushed;
	public static Item platinum_crushed_purified;

	public static Item silver_arrow;
	public static Item silver_axe;
	public static Item silver_boots;
	public static Item silver_bolt;
	public static Item silver_bow;
	public static Item silver_chestplate;
	public static Item silver_crackhammer;
	public static Item silver_crossbow;
	public static Item silver_door;
	public static Item silver_fishing_rod;
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

	public static Item silver_slab;
	public static Item silver_shield;

	public static Item silver_dense_plate;
	public static Item silver_crushed;
	public static Item silver_crushed_purified;

	public static Item starsteel_arrow;
	public static Item starsteel_axe;
	public static Item starsteel_boots;
	public static Item starsteel_bolt;
	public static Item starsteel_bow;
	public static Item starsteel_chestplate;
	public static Item starsteel_crackhammer;
	public static Item starsteel_crossbow;
	public static Item starsteel_door;
	public static Item starsteel_fishing_rod;
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

	public static Item starsteel_slab;
	public static Item starsteel_shield;

	public static Item starsteel_crystal;
	public static Item starsteel_shard;
	public static Item starsteel_clump;
	public static Item starsteel_powder_dirty;

	public static Item starsteel_dense_plate;
	public static Item starsteel_crushed;
	public static Item starsteel_crushed_purified;

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
	public static Item steel_fishing_rod;
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

	public static Item steel_slab;
	public static Item steel_shield;

	public static Item steel_crystal;
	public static Item steel_shard;
	public static Item steel_clump;
	public static Item steel_powder_dirty;

	public static Item steel_dense_plate;
	public static Item steel_crushed;
	public static Item steel_crushed_purified;

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
	public static Item tin_fishing_rod;
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

	public static Item tin_slab;
	public static Item tin_shield;

	public static Item tin_dense_plate;
	public static Item tin_crushed;
	public static Item tin_crushed_purified;

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
	public static Item zinc_fishing_rod;
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

	public static Item zinc_slab;
	public static Item zinc_shield;

	public static Item zinc_crystal;
	public static Item zinc_shard;
	public static Item zinc_clump;
	public static Item zinc_powder_dirty;

	public static Item zinc_dense_plate;
	public static Item zinc_crushed;
	public static Item zinc_crushed_purified;

	private static boolean initDone = false;

	private static Map<String, Item> itemRegistry = new HashMap<>();
	private static Map<Item, String> allItems = new HashMap<>();
	private static Map<MetalMaterial, List<Item>> itemsByMetal = new HashMap<>();

	private static Map<BlockDoor, Item> doorMap = new HashMap<>();

	private static Map<Class<?>, Integer> classSortingValues = new HashMap<>();
	private static Map<MetalMaterial, Integer> materialSortingValues = new HashMap<>();

	/**
	 * Gets an item by its name. The name is the name as it is registered in the
	 * GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "basemetals.")
	 *
	 * @param name The name of the item in question
	 * @return The item matching that name, or null if there isn't one
	 */
	public static Item getItemByName(String name) {
		return itemRegistry.get(name);
	}

	/**
	 * This is the reverse of the getItemByName(...) method, returning the
	 * registered name of an item instance (Base Metals items only).
	 *
	 * @param i The item in question
	 * @return The name of the item, or null if the item is not a Base Metals
	 * item.
	 */
	public static String getNameOfItem(Item i) {
		return allItems.get(i);
	}

	/**
	 * Gets a map of all items added, sorted by metal
	 *
	 * @return An unmodifiable map of added items catagorized by metal material
	 */
	public static Map<MetalMaterial, List<Item>> getItemsByMetal() {
		return Collections.unmodifiableMap(itemsByMetal);
	}

	// public static UniversalBucket universal_bucket; // now automatically added by Forge

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
		if (initDone)
			return;

		cyano.basemetals.init.Blocks.init();

		try {
			expandCombatArrays(net.minecraft.item.ItemAxe.class);
		} catch (IllegalAccessException | NoSuchFieldException ex) {
			FMLLog.severe("Error modifying item classes: %s", ex);
		}

		adamantine_arrow = createArrow(Materials.adamantine);
		adamantine_axe = createAxe(Materials.adamantine);
		adamantine_bolt = createBolt(Materials.adamantine);
		adamantine_boots = createBoots(Materials.adamantine);
		adamantine_bow = createBow(Materials.adamantine);
		adamantine_chestplate = createChestplate(Materials.adamantine);
		adamantine_crackhammer = createCrackhammer(Materials.adamantine);
		adamantine_crossbow = createCrossbow(Materials.adamantine);
		adamantine_door = createDoor(Materials.adamantine);
		adamantine_fishing_rod = createFishingRod(Materials.adamantine);
		adamantine_helmet = createHelmet(Materials.adamantine);
		adamantine_hoe = createHoe(Materials.adamantine);
		adamantine_horsearmor = createHorseArmor(Materials.adamantine);
		adamantine_ingot = createIngot(Materials.adamantine);
		adamantine_leggings = createLeggings(Materials.adamantine);
		adamantine_nugget = createNugget(Materials.adamantine);
		adamantine_pickaxe = createPickaxe(Materials.adamantine);
		adamantine_powder = createPowder(Materials.adamantine);
		adamantine_shears = createShears(Materials.adamantine);
		adamantine_shovel = createShovel(Materials.adamantine);
		adamantine_sword = createSword(Materials.adamantine);
		adamantine_smallpowder = createSmallPowder(Materials.adamantine);
		adamantine_rod = createRod(Materials.adamantine);
		adamantine_gear = createGear(Materials.adamantine);

		adamantine_slab = createSlab(Materials.adamantine);
		adamantine_shield = createShield(Materials.adamantine);

		adamantine_crystal = createCrystal(Materials.adamantine);
		adamantine_shard = createShard(Materials.adamantine);
		adamantine_clump = createClump(Materials.adamantine);
		adamantine_powder_dirty = createDirtyPowder(Materials.adamantine);

		adamantine_dense_plate = createDensePlate(Materials.adamantine);
		adamantine_crushed = createCrushed(Materials.adamantine);
		adamantine_crushed_purified = createCrushedPurified(Materials.adamantine);

		antimony_arrow = createArrow(Materials.antimony);
		antimony_bolt = createBolt(Materials.antimony);
		antimony_bow = createBow(Materials.antimony);
		antimony_crossbow = createCrossbow(Materials.antimony);
		antimony_fishing_rod = createFishingRod(Materials.antimony);
		antimony_horsearmor = createHorseArmor(Materials.antimony);
		antimony_shears = createShears(Materials.antimony);
		antimony_smallpowder = createSmallPowder(Materials.antimony);

		antimony_axe = createAxe(Materials.antimony);
		antimony_boots = createBoots(Materials.antimony);
		antimony_chestplate = createChestplate(Materials.antimony);
		antimony_crackhammer = createCrackhammer(Materials.antimony);
		antimony_door = createDoor(Materials.antimony);
		antimony_helmet = createHelmet(Materials.antimony);
		antimony_hoe = createHoe(Materials.antimony);
		antimony_ingot = createIngot(Materials.antimony);
		antimony_leggings = createLeggings(Materials.antimony);
		antimony_nugget = createNugget(Materials.antimony);
		antimony_pickaxe = createPickaxe(Materials.antimony);
		antimony_powder = createPowder(Materials.antimony);
		antimony_shovel = createShovel(Materials.antimony);
		antimony_sword = createSword(Materials.antimony);
		antimony_rod = createRod(Materials.antimony);
		antimony_gear = createGear(Materials.antimony);

		antimony_slab = createSlab(Materials.antimony);
		antimony_shield = createShield(Materials.antimony);

		antimony_crystal = createCrystal(Materials.antimony);
		antimony_shard = createShard(Materials.antimony);
		antimony_clump = createClump(Materials.antimony);
		antimony_powder_dirty = createDirtyPowder(Materials.antimony);

		antimony_dense_plate = createDensePlate(Materials.antimony);
		antimony_crushed = createCrushed(Materials.antimony);
		antimony_crushed_purified = createCrushedPurified(Materials.antimony);

		aquarium_arrow = createArrow(Materials.aquarium);
		aquarium_bolt = createBolt(Materials.aquarium);
		aquarium_bow = createBow(Materials.aquarium);
		aquarium_crossbow = createCrossbow(Materials.aquarium);
		aquarium_fishing_rod = createFishingRod(Materials.aquarium);
		aquarium_horsearmor = createHorseArmor(Materials.aquarium);
		aquarium_shears = createShears(Materials.aquarium);
		aquarium_smallblend = createSmallBlend(Materials.aquarium);
		aquarium_smallpowder = createSmallPowder(Materials.aquarium);

		aquarium_axe = createAxe(Materials.aquarium);
		aquarium_blend = createBlend(Materials.aquarium);
		aquarium_boots = createBoots(Materials.aquarium);
		aquarium_chestplate = createChestplate(Materials.aquarium);
		aquarium_crackhammer = createCrackhammer(Materials.aquarium);
		aquarium_door = createDoor(Materials.aquarium);
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

		aquarium_slab = createSlab(Materials.aquarium);
		aquarium_shield = createShield(Materials.aquarium);

		aquarium_crystal = createCrystal(Materials.aquarium);
		aquarium_shard = createShard(Materials.aquarium);
		aquarium_clump = createClump(Materials.aquarium);
		aquarium_powder_dirty = createDirtyPowder(Materials.aquarium);

		aquarium_dense_plate = createDensePlate(Materials.aquarium);
		aquarium_crushed = createCrushed(Materials.aquarium);
		aquarium_crushed_purified = createCrushedPurified(Materials.aquarium);

		bismuth_arrow = createArrow(Materials.bismuth);
		bismuth_bolt = createBolt(Materials.bismuth);
		bismuth_bow = createBow(Materials.bismuth);
		bismuth_crossbow = createCrossbow(Materials.bismuth);
		bismuth_fishing_rod = createFishingRod(Materials.bismuth);
		bismuth_horsearmor = createHorseArmor(Materials.bismuth);
		bismuth_shears = createShears(Materials.bismuth);
		bismuth_smallpowder = createSmallPowder(Materials.bismuth);

		bismuth_axe = createAxe(Materials.bismuth);
		bismuth_boots = createBoots(Materials.bismuth);
		bismuth_chestplate = createChestplate(Materials.bismuth);
		bismuth_crackhammer = createCrackhammer(Materials.bismuth);
		bismuth_door = createDoor(Materials.bismuth);
		bismuth_helmet = createHelmet(Materials.bismuth);
		bismuth_hoe = createHoe(Materials.bismuth);
		bismuth_ingot = createIngot(Materials.bismuth);
		bismuth_leggings = createLeggings(Materials.bismuth);
		bismuth_nugget = createNugget(Materials.bismuth);
		bismuth_pickaxe = createPickaxe(Materials.bismuth);
		bismuth_powder = createPowder(Materials.bismuth);
		bismuth_shovel = createShovel(Materials.bismuth);
		bismuth_sword = createSword(Materials.bismuth);
		bismuth_rod = createRod(Materials.bismuth);
		bismuth_gear = createGear(Materials.bismuth);

		bismuth_slab = createSlab(Materials.bismuth);
		bismuth_shield = createShield(Materials.bismuth);

		bismuth_crystal = createCrystal(Materials.bismuth);
		bismuth_shard = createShard(Materials.bismuth);
		bismuth_clump = createClump(Materials.bismuth);
		bismuth_powder_dirty = createDirtyPowder(Materials.bismuth);

		bismuth_dense_plate = createDensePlate(Materials.bismuth);
		bismuth_crushed = createCrushed(Materials.bismuth);
		bismuth_crushed_purified = createCrushedPurified(Materials.bismuth);

		brass_arrow = createArrow(Materials.brass);
		brass_bolt = createBolt(Materials.brass);
		brass_bow = createBow(Materials.brass);
		brass_crossbow = createCrossbow(Materials.brass);
		brass_fishing_rod = createFishingRod(Materials.brass);
		brass_horsearmor = createHorseArmor(Materials.brass);
		brass_shears = createShears(Materials.brass);
		brass_smallblend = createSmallBlend(Materials.brass);
		brass_smallpowder = createSmallPowder(Materials.brass);

		brass_axe = createAxe(Materials.brass);
		brass_blend = createBlend(Materials.brass);
		brass_boots = createBoots(Materials.brass);
		brass_chestplate = createChestplate(Materials.brass);
		brass_crackhammer = createCrackhammer(Materials.brass);
		brass_door = createDoor(Materials.brass);
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

		brass_slab = createSlab(Materials.brass);
		brass_shield = createShield(Materials.brass);

		brass_crystal = createCrystal(Materials.brass);
		brass_shard = createShard(Materials.brass);
		brass_clump = createClump(Materials.brass);
		brass_powder_dirty = createDirtyPowder(Materials.brass);

		brass_dense_plate = createDensePlate(Materials.brass);
		brass_crushed = createCrushed(Materials.brass);
		brass_crushed_purified = createCrushedPurified(Materials.brass);

		bronze_arrow = createArrow(Materials.bronze);
		bronze_bolt = createBolt(Materials.bronze);
		bronze_bow = createBow(Materials.bronze);
		bronze_crossbow = createCrossbow(Materials.bronze);
		bronze_fishing_rod = createFishingRod(Materials.bronze);
		bronze_horsearmor = createHorseArmor(Materials.bronze);
		bronze_shears = createShears(Materials.bronze);
		bronze_smallblend = createSmallBlend(Materials.bronze);
		bronze_smallpowder = createSmallPowder(Materials.bronze);

		bronze_axe = createAxe(Materials.bronze);
		bronze_blend = createBlend(Materials.bronze);
		bronze_boots = createBoots(Materials.bronze);
		bronze_chestplate = createChestplate(Materials.bronze);
		bronze_crackhammer = createCrackhammer(Materials.bronze);
		bronze_door = createDoor(Materials.bronze);
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

		bronze_slab = createSlab(Materials.bronze);
		bronze_shield = createShield(Materials.bronze);

		bronze_crystal = createCrystal(Materials.bronze);
		bronze_shard = createShard(Materials.bronze);
		bronze_clump = createClump(Materials.bronze);
		bronze_powder_dirty = createDirtyPowder(Materials.bronze);

		bronze_dense_plate = createDensePlate(Materials.bronze);
		bronze_crushed = createCrushed(Materials.bronze);
		bronze_crushed_purified = createCrushedPurified(Materials.bronze);

		carbon_powder = new Item().setRegistryName(BaseMetals.MODID, "carbon_powder").setUnlocalizedName(BaseMetals.MODID + "." + "carbon_powder").setCreativeTab(ItemGroups.tab_items);
		GameRegistry.register(carbon_powder);
		itemRegistry.put("carbon_powder", carbon_powder);
		OreDictionary.registerOre("dustCoal", carbon_powder);
		OreDictionary.registerOre("dustCarbon", carbon_powder);

		coldiron_arrow = createArrow(Materials.coldiron);
		coldiron_bolt = createBolt(Materials.coldiron);
		coldiron_bow = createBow(Materials.coldiron);
		coldiron_crossbow = createCrossbow(Materials.coldiron);
		coldiron_fishing_rod = createFishingRod(Materials.coldiron);
		coldiron_horsearmor = createHorseArmor(Materials.coldiron);
		coldiron_shears = createShears(Materials.coldiron);
		coldiron_smallpowder = createSmallPowder(Materials.coldiron);

		coldiron_axe = createAxe(Materials.coldiron);
		coldiron_boots = createBoots(Materials.coldiron);
		coldiron_chestplate = createChestplate(Materials.coldiron);
		coldiron_crackhammer = createCrackhammer(Materials.coldiron);
		coldiron_door = createDoor(Materials.coldiron);
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

		coldiron_slab = createSlab(Materials.coldiron);
		coldiron_shield = createShield(Materials.coldiron);

		coldiron_crystal = createCrystal(Materials.coldiron);
		coldiron_shard = createShard(Materials.coldiron);
		coldiron_clump = createClump(Materials.coldiron);
		coldiron_powder_dirty = createDirtyPowder(Materials.coldiron);

		coldiron_dense_plate = createDensePlate(Materials.coldiron);
		coldiron_crushed = createCrushed(Materials.coldiron);
		coldiron_crushed_purified = createCrushedPurified(Materials.coldiron);

		copper_arrow = createArrow(Materials.copper);
		copper_bolt = createBolt(Materials.copper);
		copper_bow = createBow(Materials.copper);
		copper_crossbow = createCrossbow(Materials.copper);
		copper_fishing_rod = createFishingRod(Materials.copper);
		copper_horsearmor = createHorseArmor(Materials.copper);
		copper_shears = createShears(Materials.copper);
		copper_smallpowder = createSmallPowder(Materials.copper);

		copper_axe = createAxe(Materials.copper);
		copper_boots = createBoots(Materials.copper);
		copper_chestplate = createChestplate(Materials.copper);
		copper_crackhammer = createCrackhammer(Materials.copper);
		copper_door = createDoor(Materials.copper);
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

		copper_slab = createSlab(Materials.copper);
		copper_shield = createShield(Materials.copper);

		copper_dense_plate = createDensePlate(Materials.copper);
		copper_crushed = createCrushed(Materials.copper);
		copper_crushed_purified = createCrushedPurified(Materials.copper);

		cupronickel_arrow = createArrow(Materials.cupronickel);
		cupronickel_bolt = createBolt(Materials.cupronickel);
		cupronickel_bow = createBow(Materials.cupronickel);
		cupronickel_crossbow = createCrossbow(Materials.cupronickel);
		cupronickel_fishing_rod = createFishingRod(Materials.cupronickel);
		cupronickel_horsearmor = createHorseArmor(Materials.cupronickel);
		cupronickel_shears = createShears(Materials.cupronickel);
		cupronickel_smallblend = createSmallBlend(Materials.cupronickel);
		cupronickel_smallpowder = createSmallPowder(Materials.cupronickel);

		cupronickel_axe = createAxe(Materials.cupronickel);
		cupronickel_blend = createBlend(Materials.cupronickel);
		cupronickel_boots = createBoots(Materials.cupronickel);
		cupronickel_chestplate = createChestplate(Materials.cupronickel);
		cupronickel_crackhammer = createCrackhammer(Materials.cupronickel);
		cupronickel_door = createDoor(Materials.cupronickel);
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

		cupronickel_slab = createSlab(Materials.cupronickel);
		cupronickel_shield = createShield(Materials.cupronickel);

		cupronickel_crystal = createCrystal(Materials.cupronickel);
		cupronickel_shard = createShard(Materials.cupronickel);
		cupronickel_clump = createClump(Materials.cupronickel);
		cupronickel_powder_dirty = createDirtyPowder(Materials.cupronickel);

		cupronickel_dense_plate = createDensePlate(Materials.cupronickel);
		cupronickel_crushed = createCrushed(Materials.cupronickel);
		cupronickel_crushed_purified = createCrushedPurified(Materials.cupronickel);

		diamond_crackhammer = createCrackhammer(Materials.vanilla_diamond);
		diamond_gear = createGear(Materials.vanilla_diamond);

		electrum_arrow = createArrow(Materials.electrum);
		electrum_bolt = createBolt(Materials.electrum);
		electrum_bow = createBow(Materials.electrum);
		electrum_crossbow = createCrossbow(Materials.electrum);
		electrum_fishing_rod = createFishingRod(Materials.electrum);
		electrum_horsearmor = createHorseArmor(Materials.electrum);
		electrum_shears = createShears(Materials.electrum);
		electrum_smallblend = createSmallBlend(Materials.electrum);
		electrum_smallpowder = createSmallPowder(Materials.electrum);

		electrum_axe = createAxe(Materials.electrum);
		electrum_blend = createBlend(Materials.electrum);
		electrum_boots = createBoots(Materials.electrum);
		electrum_chestplate = createChestplate(Materials.electrum);
		electrum_crackhammer = createCrackhammer(Materials.electrum);
		electrum_door = createDoor(Materials.electrum);
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

		electrum_slab = createSlab(Materials.electrum);
		electrum_shield = createShield(Materials.electrum);

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

		invar_arrow = createArrow(Materials.invar);
		invar_bolt = createBolt(Materials.invar);
		invar_bow = createBow(Materials.invar);
		invar_crossbow = createCrossbow(Materials.invar);
		invar_fishing_rod = createFishingRod(Materials.invar);
		invar_horsearmor = createHorseArmor(Materials.invar);
		invar_shears = createShears(Materials.invar);
		invar_smallblend = createSmallBlend(Materials.invar);
		invar_smallpowder = createSmallPowder(Materials.invar);

		invar_axe = createAxe(Materials.invar);
		invar_blend = createBlend(Materials.invar);
		invar_boots = createBoots(Materials.invar);
		invar_chestplate = createChestplate(Materials.invar);
		invar_crackhammer = createCrackhammer(Materials.invar);
		invar_door = createDoor(Materials.invar);
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

		invar_slab = createSlab(Materials.invar);
		invar_shield = createShield(Materials.invar);

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

		lead_arrow = createArrow(Materials.lead);
		lead_bolt = createBolt(Materials.lead);
		lead_bow = createBow(Materials.lead);
		lead_crossbow = createCrossbow(Materials.lead);
		lead_fishing_rod = createFishingRod(Materials.lead);
		lead_horsearmor = createHorseArmor(Materials.lead);
		lead_shears = createShears(Materials.lead);
		lead_smallpowder = createSmallPowder(Materials.lead);

		lead_axe = createAxe(Materials.lead);
		lead_boots = createBoots(Materials.lead);
		lead_chestplate = createChestplate(Materials.lead);
		lead_crackhammer = createCrackhammer(Materials.lead);
		lead_door = createDoor(Materials.lead);
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

		lead_slab = createSlab(Materials.lead);
		lead_shield = createShield(Materials.lead);

		lead_dense_plate = createDensePlate(Materials.lead);
		lead_crushed = createCrushed(Materials.lead);
		lead_crushed_purified = createCrushedPurified(Materials.lead);

		platinum_arrow = createArrow(Materials.platinum);
		platinum_bolt = createBolt(Materials.platinum);
		platinum_bow = createBow(Materials.platinum);
		platinum_crossbow = createCrossbow(Materials.platinum);
		platinum_fishing_rod = createFishingRod(Materials.platinum);
		platinum_horsearmor = createHorseArmor(Materials.platinum);
		platinum_shears = createShears(Materials.platinum);
		platinum_smallpowder = createSmallPowder(Materials.platinum);

		platinum_axe = createAxe(Materials.platinum);
		platinum_boots = createBoots(Materials.platinum);
		platinum_chestplate = createChestplate(Materials.platinum);
		platinum_crackhammer = createCrackhammer(Materials.platinum);
		platinum_door = createDoor(Materials.platinum);
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

		platinum_slab = createSlab(Materials.platinum);
		platinum_shield = createShield(Materials.platinum);

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
		mercury_ingot = registerItem(new Item(), "mercury_ingot", null, ItemGroups.tab_items);
		itemRegistry.put("mercury_ingot", mercury_ingot);
		OreDictionary.registerOre("ingotMercury", mercury_ingot);
		OreDictionary.registerOre("quicksilver", mercury_ingot);

		mercury_powder = registerItem(new Item(), "mercury_powder", null, ItemGroups.tab_items);
		itemRegistry.put("mercury_powder", mercury_powder);
		OreDictionary.registerOre("dustMercury", mercury_powder);

		// mercury_crystal = createCrystal(Materials.mercury);
		// mercury_shard = createShard(Materials.mercury);
		// mercury_clump = createClump(Materials.mercury);
		// mercury_powder_dirty = createDirtyPowder(Materials.mercury);

		// mercury_crushed = createCrushed(Materials.mercury);
		// mercury_crushed_purified = createCrushedPurified(Materials.mercury);

		mithril_arrow = createArrow(Materials.mithril);
		mithril_bolt = createBolt(Materials.mithril);
		mithril_bow = createBow(Materials.mithril);
		mithril_crossbow = createCrossbow(Materials.mithril);
		mithril_fishing_rod = createFishingRod(Materials.mithril);
		mithril_horsearmor = createHorseArmor(Materials.mithril);
		mithril_shears = createShears(Materials.mithril);
		mithril_smallblend = createSmallBlend(Materials.mithril);
		mithril_smallpowder = createSmallPowder(Materials.mithril);

		mithril_axe = createAxe(Materials.mithril);
		mithril_blend = createBlend(Materials.mithril);
		mithril_boots = createBoots(Materials.mithril);
		mithril_chestplate = createChestplate(Materials.mithril);
		mithril_crackhammer = createCrackhammer(Materials.mithril);
		mithril_door = createDoor(Materials.mithril);
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

		mithril_slab = createSlab(Materials.mithril);
		mithril_shield = createShield(Materials.mithril);

		mithril_crystal = createCrystal(Materials.mithril);
		mithril_shard = createShard(Materials.mithril);
		mithril_clump = createClump(Materials.mithril);
		mithril_powder_dirty = createDirtyPowder(Materials.mithril);

		mithril_dense_plate = createDensePlate(Materials.mithril);
		mithril_crushed = createCrushed(Materials.mithril);
		mithril_crushed_purified = createCrushedPurified(Materials.mithril);

		nickel_arrow = createArrow(Materials.nickel);
		nickel_bolt = createBolt(Materials.nickel);
		nickel_bow = createBow(Materials.nickel);
		nickel_crossbow = createCrossbow(Materials.nickel);
		nickel_fishing_rod = createFishingRod(Materials.nickel);
		nickel_horsearmor = createHorseArmor(Materials.nickel);
		nickel_shears = createShears(Materials.nickel);
		nickel_smallpowder = createSmallPowder(Materials.nickel);

		nickel_axe = createAxe(Materials.nickel);
		nickel_boots = createBoots(Materials.nickel);
		nickel_chestplate = createChestplate(Materials.nickel);
		nickel_crackhammer = createCrackhammer(Materials.nickel);
		nickel_door = createDoor(Materials.nickel);
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

		nickel_slab = createSlab(Materials.nickel);
		nickel_shield = createShield(Materials.nickel);

		nickel_crystal = createCrystal(Materials.nickel);
		nickel_shard = createShard(Materials.nickel);
		nickel_clump = createClump(Materials.nickel);
		nickel_powder_dirty = createDirtyPowder(Materials.nickel);

		nickel_dense_plate = createDensePlate(Materials.nickel);
		nickel_crushed = createCrushed(Materials.nickel);
		nickel_crushed_purified = createCrushedPurified(Materials.nickel);

		pewter_arrow = createArrow(Materials.pewter);
		pewter_bolt = createBolt(Materials.pewter);
		pewter_bow = createBow(Materials.pewter);
		pewter_crossbow = createCrossbow(Materials.pewter);
		pewter_fishing_rod = createFishingRod(Materials.pewter);
		pewter_horsearmor = createHorseArmor(Materials.pewter);
		pewter_shears = createShears(Materials.pewter);
		pewter_smallpowder = createSmallPowder(Materials.pewter);

		pewter_axe = createAxe(Materials.pewter);
		pewter_blend = createBlend(Materials.pewter);
		pewter_boots = createBoots(Materials.pewter);
		pewter_chestplate = createChestplate(Materials.pewter);
		pewter_crackhammer = createCrackhammer(Materials.pewter);
		pewter_door = createDoor(Materials.pewter);
		pewter_helmet = createHelmet(Materials.pewter);
		pewter_hoe = createHoe(Materials.pewter);
		pewter_ingot = createIngot(Materials.pewter);
		pewter_leggings = createLeggings(Materials.pewter);
		pewter_nugget = createNugget(Materials.pewter);
		pewter_pickaxe = createPickaxe(Materials.pewter);
		pewter_powder = createPowder(Materials.pewter);
		pewter_shovel = createShovel(Materials.pewter);
		pewter_sword = createSword(Materials.pewter);
		pewter_rod = createRod(Materials.pewter);
		pewter_gear = createGear(Materials.pewter);

		pewter_slab = createSlab(Materials.pewter);
		pewter_shield = createShield(Materials.pewter);

		// pewter_crystal = createCrystal(Materials.pewter);
		// pewter_shard = createShard(Materials.pewter);
		// pewter_clump = createClump(Materials.pewter);
		// pewter_powder_dirty = createDirtyPowder(Materials.pewter);

		// pewter_dense_plate = createDensePlate(Materials.pewter);
		// pewter_crushed = createCrushed(Materials.pewter);
		// pewter_crushed_purified = createCrushedPurified(Materials.pewter);

		silver_arrow = createArrow(Materials.silver);
		silver_bolt = createBolt(Materials.silver);
		silver_bow = createBow(Materials.silver);
		silver_crossbow = createCrossbow(Materials.silver);
		silver_fishing_rod = createFishingRod(Materials.silver);
		silver_horsearmor = createHorseArmor(Materials.silver);
		silver_shears = createShears(Materials.silver);
		silver_smallpowder = createSmallPowder(Materials.silver);

		silver_axe = createAxe(Materials.silver);
		silver_boots = createBoots(Materials.silver);
		silver_chestplate = createChestplate(Materials.silver);
		silver_crackhammer = createCrackhammer(Materials.silver);
		silver_door = createDoor(Materials.silver);
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

		silver_slab = createSlab(Materials.silver);
		silver_shield = createShield(Materials.silver);

		silver_dense_plate = createDensePlate(Materials.silver);
		silver_crushed = createCrushed(Materials.silver);
		silver_crushed_purified = createCrushedPurified(Materials.silver);

		starsteel_arrow = createArrow(Materials.starsteel);
		starsteel_bolt = createBolt(Materials.starsteel);
		starsteel_bow = createBow(Materials.starsteel);
		starsteel_crossbow = createCrossbow(Materials.starsteel);
		starsteel_fishing_rod = createFishingRod(Materials.starsteel);
		starsteel_horsearmor = createHorseArmor(Materials.starsteel);
		starsteel_shears = createShears(Materials.starsteel);
		starsteel_smallpowder = createSmallPowder(Materials.starsteel);

		starsteel_axe = createAxe(Materials.starsteel);
		starsteel_boots = createBoots(Materials.starsteel);
		starsteel_chestplate = createChestplate(Materials.starsteel);
		starsteel_crackhammer = createCrackhammer(Materials.starsteel);
		starsteel_door = createDoor(Materials.starsteel);
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

		starsteel_slab = createSlab(Materials.starsteel);
		starsteel_shield = createShield(Materials.starsteel);

		starsteel_crystal = createCrystal(Materials.starsteel);
		starsteel_shard = createShard(Materials.starsteel);
		starsteel_clump = createClump(Materials.starsteel);
		starsteel_powder_dirty = createDirtyPowder(Materials.starsteel);

		starsteel_dense_plate = createDensePlate(Materials.starsteel);
		starsteel_crushed = createCrushed(Materials.starsteel);
		starsteel_crushed_purified = createCrushedPurified(Materials.starsteel);

		steel_arrow = createArrow(Materials.steel);
		steel_bolt = createBolt(Materials.steel);
		steel_bow = createBow(Materials.steel);
		steel_crossbow = createCrossbow(Materials.steel);
		steel_fishing_rod = createFishingRod(Materials.steel);
		steel_horsearmor = createHorseArmor(Materials.steel);
		steel_shears = createShears(Materials.steel);
		steel_smallblend = createSmallBlend(Materials.steel);
		steel_smallpowder = createSmallPowder(Materials.steel);

		steel_axe = createAxe(Materials.steel);
		steel_blend = createBlend(Materials.steel);
		steel_boots = createBoots(Materials.steel);
		steel_chestplate = createChestplate(Materials.steel);
		steel_crackhammer = createCrackhammer(Materials.steel);
		steel_door = createDoor(Materials.steel);
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

		steel_slab = createSlab(Materials.steel);
		steel_shield = createShield(Materials.steel);

		steel_crystal = createCrystal(Materials.steel);
		steel_shard = createShard(Materials.steel);
		steel_clump = createClump(Materials.steel);
		steel_powder_dirty = createDirtyPowder(Materials.steel);

		steel_dense_plate = createDensePlate(Materials.steel);
		steel_crushed = createCrushed(Materials.steel);
		steel_crushed_purified = createCrushedPurified(Materials.steel);

		tin_arrow = createArrow(Materials.tin);
		tin_bolt = createBolt(Materials.tin);
		tin_bow = createBow(Materials.tin);
		tin_crossbow = createCrossbow(Materials.tin);
		tin_fishing_rod = createFishingRod(Materials.tin);
		tin_horsearmor = createHorseArmor(Materials.tin);

		tin_axe = createAxe(Materials.tin);
		tin_boots = createBoots(Materials.tin);
		tin_chestplate = createChestplate(Materials.tin);
		tin_crackhammer = createCrackhammer(Materials.tin);
		tin_door = createDoor(Materials.tin);
		tin_helmet = createHelmet(Materials.tin);
		tin_hoe = createHoe(Materials.tin);
		tin_ingot = createIngot(Materials.tin);
		tin_leggings = createLeggings(Materials.tin);
		tin_nugget = createNugget(Materials.tin);
		tin_pickaxe = createPickaxe(Materials.tin);
		tin_powder = createPowder(Materials.tin);
		tin_shears = createShears(Materials.tin);
		tin_shovel = createShovel(Materials.tin);
		tin_smallpowder = createSmallPowder(Materials.tin);
		tin_sword = createSword(Materials.tin);
		tin_rod = createRod(Materials.tin);
		tin_gear = createGear(Materials.tin);

		tin_slab = createSlab(Materials.tin);
		tin_shield = createShield(Materials.tin);

		tin_dense_plate = createDensePlate(Materials.tin);
		tin_crushed = createCrushed(Materials.tin);
		tin_crushed_purified = createCrushedPurified(Materials.tin);

		zinc_arrow = createArrow(Materials.zinc);
		zinc_axe = createAxe(Materials.zinc);
		zinc_bolt = createBolt(Materials.zinc);
		zinc_boots = createBoots(Materials.zinc);
		zinc_bow = createBow(Materials.zinc);
		zinc_chestplate = createChestplate(Materials.zinc);
		zinc_crackhammer = createCrackhammer(Materials.zinc);
		zinc_crossbow = createCrossbow(Materials.zinc);
		zinc_door = createDoor(Materials.zinc);
		zinc_fishing_rod = createFishingRod(Materials.zinc);
		zinc_helmet = createHelmet(Materials.zinc);
		zinc_hoe = createHoe(Materials.zinc);
		zinc_horsearmor = createHorseArmor(Materials.zinc);
		zinc_ingot = createIngot(Materials.zinc);
		zinc_leggings = createLeggings(Materials.zinc);
		zinc_nugget = createNugget(Materials.zinc);
		tin_pickaxe = createPickaxe(Materials.zinc);
		zinc_powder = createPowder(Materials.zinc);
		zinc_shears = createShears(Materials.zinc);
		zinc_shovel = createShovel(Materials.zinc);
		zinc_smallpowder = createSmallPowder(Materials.zinc);
		zinc_sword = createSword(Materials.zinc);
		zinc_rod = createRod(Materials.zinc);
		zinc_gear = createGear(Materials.zinc);

		zinc_slab = createSlab(Materials.zinc);
		zinc_shield = createShield(Materials.zinc);

		zinc_crystal = createCrystal(Materials.zinc);
		zinc_shard = createShard(Materials.zinc);
		zinc_clump = createClump(Materials.zinc);
		zinc_powder_dirty = createDirtyPowder(Materials.zinc);

		zinc_dense_plate = createDensePlate(Materials.zinc);
		zinc_crushed = createCrushed(Materials.zinc);
		zinc_crushed_purified = createCrushedPurified(Materials.zinc);

		// TODO: Make this support multiple oredicts
		for (final Item i : allItems.keySet())
			if (i instanceof IOreDictionaryEntry)
				OreDictionary.registerOre(((IOreDictionaryEntry) i).getOreDictionaryName(), i);

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

		final List<MetalMaterial> metlist = new ArrayList<>(Materials.getAllMetals().size());
		metlist.addAll(Materials.getAllMetals());
		metlist.sort((MetalMaterial a, MetalMaterial b) -> a.getName().compareToIgnoreCase(b.getName()));
		for (int i = 0; i < metlist.size(); i++)
			materialSortingValues.put(metlist.get(i), i * 100);

		initDone = true;
	}

	private static Item registerItem(Item item, String name, MetalMaterial metal, CreativeTabs tab) {
		String fullName = null;
		if (metal != null)
			fullName = metal.getName() + "_" + name;
		else
			fullName = name;

		item.setRegistryName(fullName);
		item.setUnlocalizedName(item.getRegistryName().getResourceDomain() + "." + fullName);
		GameRegistry.register(item);
		itemRegistry.put(fullName, item);
		allItems.put(item, fullName);

		if (tab != null)
			item.setCreativeTab(tab);

		if (metal != null) {
			itemsByMetal.computeIfAbsent(metal, (MetalMaterial g) -> new ArrayList<>());
			itemsByMetal.get(metal).add(item);
		}

		if (item instanceof IOreDictionaryEntry)
			OreDictionary.registerOre(((IOreDictionaryEntry) item).getOreDictionaryName(), item);

		return item;
	}

	private static Item createIngot(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalIngot(metal), "ingot", metal, ItemGroups.tab_items);
		metal.ingot = i;
		return i;
	}

	private static Item createNugget(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalNugget(metal), "nugget", metal, ItemGroups.tab_items);
		metal.nugget = i;
		return i;
	}

	private static Item createPowder(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalPowder(metal), "powder", metal, ItemGroups.tab_items);
		metal.powder = i;
		return i;
	}

	private static Item createBlend(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalBlend(metal), "blend", metal, ItemGroups.tab_items);
		metal.blend = i;
		return i;
	}

	private static Item createRod(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalRod(metal), "rod", metal, ItemGroups.tab_items);
		metal.rod = i;
		return i;
	}

	private static Item createGear(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalGear(metal), "gear", metal, ItemGroups.tab_items);
		metal.gear = i;
		return i;
	}

	private static Item createAxe(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalAxe(metal), "axe", metal, ItemGroups.tab_tools);
		metal.axe = i;
		return i;
	}

	private static Item createCrackhammer(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalCrackHammer(metal), "crackhammer", metal, ItemGroups.tab_tools);
		metal.crackhammer = i;
		return i;
	}

	private static Item createHoe(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalHoe(metal), "hoe", metal, ItemGroups.tab_tools);
		metal.hoe = i;
		return i;
	}

	private static Item createPickaxe(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalPickaxe(metal), "pickaxe", metal, ItemGroups.tab_tools);
		metal.pickaxe = i;
		return i;
	}

	private static Item createShovel(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalShovel(metal), "shovel", metal, ItemGroups.tab_tools);
		metal.shovel = i;
		return i;
	}

	private static Item createSword(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalSword(metal), "sword", metal, ItemGroups.tab_tools);
		metal.sword = i;
		return i;
	}

	private static Item createHelmet(MetalMaterial metal) {
		final Item i = registerItem(ItemMetalArmor.createHelmet(metal), "helmet", metal, ItemGroups.tab_tools);
		metal.helmet = i;
		return i;
	}

	private static Item createChestplate(MetalMaterial metal) {
		final Item i = registerItem(ItemMetalArmor.createChestplate(metal), "chestplate", metal, ItemGroups.tab_tools);
		metal.chestplate = i;
		return i;
	}

	private static Item createLeggings(MetalMaterial metal) {
		final Item i = registerItem(ItemMetalArmor.createLeggings(metal), "leggings", metal, ItemGroups.tab_tools);
		metal.leggings = i;
		return i;
	}

	private static Item createBoots(MetalMaterial metal) {
		final Item i = registerItem(ItemMetalArmor.createBoots(metal), "boots", metal, ItemGroups.tab_tools);
		metal.boots = i;
		return i;
	}

	private static Item createHorseArmor(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalHorseArmor(metal), "horsearmor", metal, ItemGroups.tab_tools);
		metal.horsearmor = i;
		return i;
	}

	private static Item createArrow(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalArrow(metal), "arrow", metal, ItemGroups.tab_tools);
		metal.arrow = i;
		return i;
	}

	private static Item createBolt(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalBolt(metal), "bolt", metal, ItemGroups.tab_tools);
		metal.bolt = i;
		return i;
	}

	private static Item createBow(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalBow(metal), "bow", metal, ItemGroups.tab_tools);
		metal.bow = i;
		return i;
	}

	private static Item createCrossbow(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalCrossbow(metal), "crossbow", metal, ItemGroups.tab_tools);
		metal.crossbow = i;
		return i;
	}

	private static Item createShears(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalShears(metal), "shears", metal, ItemGroups.tab_tools);
		metal.shears = i;
		return i;
	}

	private static Item createSmallBlend(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalSmallBlend(metal), "smallblend", metal, ItemGroups.tab_items);
		metal.smallblend = i;
		return i;
	}

	private static Item createFishingRod(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalFishingRod(metal), "fishing_rod", metal, ItemGroups.tab_tools);
		metal.fishing_rod = i;
		return i;
	}

	private static Item createSmallPowder(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalSmallPowder(metal), "smallpowder", metal, ItemGroups.tab_items);
		metal.smallpowder = i;
		return i;
	}

	private static Item createShield(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalShield(metal), "shield", metal, ItemGroups.tab_items);
		metal.shield = i;
		return i;
	}

	private static Item createCrystal(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), "crystal", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("crystal" + metal.getCapitalizedName(), i);
		metal.crystal = i;
		return i;
	}

	private static Item createShard(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), "shard", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("shard" + metal.getCapitalizedName(), i);
		metal.shard = i;
		return i;
	}

	private static Item createClump(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), "clump", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("clump" + metal.getCapitalizedName(), i);
		metal.clump = i;
		return i;
	}

	private static Item createDirtyPowder(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), "powder_dirty", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("dustDirty" + metal.getCapitalizedName(), i);
		metal.dirtypowder = i;
		return i;
	}

	// TODO: Possibly make this a block, 1/2 of the normal plate.
	private static Item createDensePlate(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), "dense_plate", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("plateDense" + metal.getCapitalizedName(), i);
		metal.denseplate = i;
		return i;
	}

	private static Item createCrushed(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), "crushed", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("crushed" + metal.getCapitalizedName(), i);
		metal.crushed = i;
		return i;
	}

	private static Item createCrushedPurified(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), "crushed_purified", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("crushedPurified" + metal.getCapitalizedName(), i);
//		metal.crushedpurified = i;
		return i;
	}

	private static Item createSlab(MetalMaterial metal) {
		final Item i = registerItem(new ItemMetalSlab(metal), "slab", metal, ItemGroups.tab_blocks);
		metal.slab = i;
		return i;
	}

	private static Item createDoor(MetalMaterial metal) {
		final Item i = new ItemMetalDoor(metal);
		registerItem(i, "door", metal, ItemGroups.tab_blocks);
		metal.door = i;
		doorMap.put(metal.doorBlock, i);
		return i;
	}

	/**
	 * Uses reflection to expand the size of the combat damage and attack speed
	 * arrays to prevent initialization index-out-of-bounds errors
	 * 
	 * @param itemClass The class to modify
	 */
	private static void expandCombatArrays(Class<?> itemClass) throws IllegalAccessException, NoSuchFieldException {
		// WARNING: this method contains black magic
		final int expandedSize = 256;
		final Field[] fields = itemClass.getDeclaredFields();
		for (final Field f : fields)
			if (Modifier.isStatic(f.getModifiers())
					&& f.getType().isArray()
					&& f.getType().getComponentType().equals(float.class)) {
				FMLLog.info("%s: Expanding array variable %s.%s to size %s", Thread.currentThread().getStackTrace()[0], itemClass.getSimpleName(), f.getName(), expandedSize);
				f.setAccessible(true); // bypass 'private' key word
				final Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL); // bypass 'final' key word
				final float[] newArray = new float[expandedSize];
				Arrays.fill(newArray, 0F);
				System.arraycopy(f.get(null), 0, newArray, 0, Array.getLength(f.get(null)));
				f.set(null, newArray);
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
		if ((a.getItem() instanceof ItemBlock) && (((ItemBlock) a.getItem()).getBlock() instanceof IMetalObject)) {
			classVal = classSortingValues.computeIfAbsent(((ItemBlock) a.getItem()).getBlock().getClass(),
					(Class<?> c) -> 990000);
			metalVal = materialSortingValues.computeIfAbsent(
					((IMetalObject) ((ItemBlock) a.getItem()).getBlock()).getMetalMaterial(),
					(MetalMaterial m) -> 9900);
		} else if (a.getItem() instanceof IMetalObject) {
			classVal = classSortingValues.computeIfAbsent(a.getItem().getClass(),
					(Class<?> c) -> 990000);
			metalVal = materialSortingValues.computeIfAbsent(((IMetalObject) a.getItem()).getMetalMaterial(),
					(MetalMaterial m) -> 9900);
		}
		return classVal + metalVal + (a.getMetadata() % 100);
	}

	public static Map<String, Item> getItemRegistry() {
		return itemRegistry;
	}
}
