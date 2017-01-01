package cyano.basemetals.init;

import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;

import cyano.basemetals.blocks.*;
import cyano.basemetals.items.*;
import cyano.basemetals.material.*;
import cyano.basemetals.registry.IOreDictionaryEntry;
import cyano.basemetals.util.Config.Options;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
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

	public static Item charcoal_powder;
	public static Item coal_powder;

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
	public static Item diamond_rod;

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
	public static Item gold_smallpowder;
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

	private static BiMap<String, Item> itemRegistry = HashBiMap.create(34);
	private static Map<MetalMaterial, List<Item>> itemsByMaterial = new HashMap<>();

	private static Map<Class<?>, Integer> classSortingValues = new HashMap<>();
	private static Map<MetalMaterial, Integer> materialSortingValues = new HashMap<>();

	// public static UniversalBucket universal_bucket; // now automatically added by Forge

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Blocks.init();

		try {
			expandCombatArrays(net.minecraft.item.ItemAxe.class);
		}
		catch (IllegalAccessException | NoSuchFieldException ex) {
			FMLLog.severe("Error modifying item classes: %s", ex);
		}

		String materialName;
		if (Options.ENABLE_ADAMANTINE) {
			materialName = "adamantine";

			adamantine_arrow = createArrow(materialName);
			adamantine_axe = createAxe(materialName);
			adamantine_bolt = createBolt(materialName);
			adamantine_boots = createBoots(materialName);
			adamantine_bow = createBow(materialName);
			adamantine_chestplate = createChestplate(materialName);
			adamantine_crackhammer = createCrackhammer(materialName);
			adamantine_crossbow = createCrossbow(materialName);
			adamantine_door = createDoor(materialName);
			adamantine_fishing_rod = createFishingRod(materialName);
			adamantine_helmet = createHelmet(materialName);
			adamantine_hoe = createHoe(materialName);
			adamantine_horsearmor = createHorseArmor(materialName);
			adamantine_ingot = createIngot(materialName);
			adamantine_leggings = createLeggings(materialName);
			adamantine_nugget = createNugget(materialName);
			adamantine_pickaxe = createPickaxe(materialName);
			adamantine_powder = createPowder(materialName);
			adamantine_shears = createShears(materialName);
			adamantine_shovel = createShovel(materialName);
			adamantine_sword = createSword(materialName);
			adamantine_smallpowder = createSmallPowder(materialName);
			adamantine_rod = createRod(materialName);
			adamantine_gear = createGear(materialName);

			adamantine_slab = createSlab(materialName);
			adamantine_shield = createShield(materialName);

			adamantine_crystal = createCrystal(materialName);
			adamantine_shard = createShard(materialName);
			adamantine_clump = createClump(materialName);
			adamantine_powder_dirty = createDirtyPowder(materialName);

			adamantine_dense_plate = createDensePlate(materialName);
			adamantine_crushed = createCrushed(materialName);
			adamantine_crushed_purified = createCrushedPurified(materialName);
		}
		if (Options.ENABLE_ANTIMONY) {
			materialName = "antimony";

			antimony_arrow = createArrow(materialName);
			antimony_bolt = createBolt(materialName);
			antimony_bow = createBow(materialName);
			antimony_crossbow = createCrossbow(materialName);
			antimony_fishing_rod = createFishingRod(materialName);
			antimony_horsearmor = createHorseArmor(materialName);
			antimony_shears = createShears(materialName);
			antimony_smallpowder = createSmallPowder(materialName);

			antimony_axe = createAxe(materialName);
			antimony_boots = createBoots(materialName);
			antimony_chestplate = createChestplate(materialName);
			antimony_crackhammer = createCrackhammer(materialName);
			antimony_door = createDoor(materialName);
			antimony_helmet = createHelmet(materialName);
			antimony_hoe = createHoe(materialName);
			antimony_ingot = createIngot(materialName);
			antimony_leggings = createLeggings(materialName);
			antimony_nugget = createNugget(materialName);
			antimony_pickaxe = createPickaxe(materialName);
			antimony_powder = createPowder(materialName);
			antimony_shovel = createShovel(materialName);
			antimony_sword = createSword(materialName);
			antimony_rod = createRod(materialName);
			antimony_gear = createGear(materialName);

			antimony_slab = createSlab(materialName);
			antimony_shield = createShield(materialName);

			antimony_crystal = createCrystal(materialName);
			antimony_shard = createShard(materialName);
			antimony_clump = createClump(materialName);
			antimony_powder_dirty = createDirtyPowder(materialName);

			antimony_dense_plate = createDensePlate(materialName);
			antimony_crushed = createCrushed(materialName);
			antimony_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_AQUARIUM) {
			materialName = "aquarium";

			aquarium_arrow = createArrow(materialName);
			aquarium_bolt = createBolt(materialName);
			aquarium_bow = createBow(materialName);
			aquarium_crossbow = createCrossbow(materialName);
			aquarium_fishing_rod = createFishingRod(materialName);
			aquarium_horsearmor = createHorseArmor(materialName);
			aquarium_shears = createShears(materialName);
			aquarium_smallblend = createSmallBlend(materialName);
			aquarium_smallpowder = createSmallPowder(materialName);

			aquarium_axe = createAxe(materialName);
			aquarium_blend = createBlend(materialName);
			aquarium_boots = createBoots(materialName);
			aquarium_chestplate = createChestplate(materialName);
			aquarium_crackhammer = createCrackhammer(materialName);
			aquarium_door = createDoor(materialName);
			aquarium_helmet = createHelmet(materialName);
			aquarium_hoe = createHoe(materialName);
			aquarium_ingot = createIngot(materialName);
			aquarium_leggings = createLeggings(materialName);
			aquarium_nugget = createNugget(materialName);
			aquarium_pickaxe = createPickaxe(materialName);
			aquarium_powder = createPowder(materialName);
			aquarium_shovel = createShovel(materialName);
			aquarium_sword = createSword(materialName);
			aquarium_rod = createRod(materialName);
			aquarium_gear = createGear(materialName);

			aquarium_slab = createSlab(materialName);
			aquarium_shield = createShield(materialName);

			aquarium_crystal = createCrystal(materialName);
			aquarium_shard = createShard(materialName);
			aquarium_clump = createClump(materialName);
			aquarium_powder_dirty = createDirtyPowder(materialName);

			aquarium_dense_plate = createDensePlate(materialName);
			aquarium_crushed = createCrushed(materialName);
			aquarium_crushed_purified = createCrushedPurified(materialName);
		}
		if (Options.ENABLE_BISMUTH) {
			materialName = "bismuth";

			bismuth_arrow = createArrow(materialName);
			bismuth_bolt = createBolt(materialName);
			bismuth_bow = createBow(materialName);
			bismuth_crossbow = createCrossbow(materialName);
			bismuth_fishing_rod = createFishingRod(materialName);
			bismuth_horsearmor = createHorseArmor(materialName);
			bismuth_shears = createShears(materialName);
			bismuth_smallpowder = createSmallPowder(materialName);

			bismuth_axe = createAxe(materialName);
			bismuth_boots = createBoots(materialName);
			bismuth_chestplate = createChestplate(materialName);
			bismuth_crackhammer = createCrackhammer(materialName);
			bismuth_door = createDoor(materialName);
			bismuth_helmet = createHelmet(materialName);
			bismuth_hoe = createHoe(materialName);
			bismuth_ingot = createIngot(materialName);
			bismuth_leggings = createLeggings(materialName);
			bismuth_nugget = createNugget(materialName);
			bismuth_pickaxe = createPickaxe(materialName);
			bismuth_powder = createPowder(materialName);
			bismuth_shovel = createShovel(materialName);
			bismuth_sword = createSword(materialName);
			bismuth_rod = createRod(materialName);
			bismuth_gear = createGear(materialName);

			bismuth_slab = createSlab(materialName);
			bismuth_shield = createShield(materialName);

			bismuth_crystal = createCrystal(materialName);
			bismuth_shard = createShard(materialName);
			bismuth_clump = createClump(materialName);
			bismuth_powder_dirty = createDirtyPowder(materialName);

			bismuth_dense_plate = createDensePlate(materialName);
			bismuth_crushed = createCrushed(materialName);
			bismuth_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_BRASS) {
			materialName = "brass";

			brass_arrow = createArrow(materialName);
			brass_bolt = createBolt(materialName);
			brass_bow = createBow(materialName);
			brass_crossbow = createCrossbow(materialName);
			brass_fishing_rod = createFishingRod(materialName);
			brass_horsearmor = createHorseArmor(materialName);
			brass_shears = createShears(materialName);
			brass_smallblend = createSmallBlend(materialName);
			brass_smallpowder = createSmallPowder(materialName);

			brass_axe = createAxe(materialName);
			brass_blend = createBlend(materialName);
			brass_boots = createBoots(materialName);
			brass_chestplate = createChestplate(materialName);
			brass_crackhammer = createCrackhammer(materialName);
			brass_door = createDoor(materialName);
			brass_helmet = createHelmet(materialName);
			brass_hoe = createHoe(materialName);
			brass_ingot = createIngot(materialName);
			brass_leggings = createLeggings(materialName);
			brass_nugget = createNugget(materialName);
			brass_pickaxe = createPickaxe(materialName);
			brass_powder = createPowder(materialName);
			brass_shovel = createShovel(materialName);
			brass_sword = createSword(materialName);
			brass_rod = createRod(materialName);
			brass_gear = createGear(materialName);

			brass_slab = createSlab(materialName);
			brass_shield = createShield(materialName);

			brass_crystal = createCrystal(materialName);
			brass_shard = createShard(materialName);
			brass_clump = createClump(materialName);
			brass_powder_dirty = createDirtyPowder(materialName);

			brass_dense_plate = createDensePlate(materialName);
			brass_crushed = createCrushed(materialName);
			brass_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_BRONZE) {
			materialName = "bronze";

			bronze_arrow = createArrow(materialName);
			bronze_bolt = createBolt(materialName);
			bronze_bow = createBow(materialName);
			bronze_crossbow = createCrossbow(materialName);
			bronze_fishing_rod = createFishingRod(materialName);
			bronze_horsearmor = createHorseArmor(materialName);
			bronze_shears = createShears(materialName);
			bronze_smallblend = createSmallBlend(materialName);
			bronze_smallpowder = createSmallPowder(materialName);

			bronze_axe = createAxe(materialName);
			bronze_blend = createBlend(materialName);
			bronze_boots = createBoots(materialName);
			bronze_chestplate = createChestplate(materialName);
			bronze_crackhammer = createCrackhammer(materialName);
			bronze_door = createDoor(materialName);
			bronze_helmet = createHelmet(materialName);
			bronze_hoe = createHoe(materialName);
			bronze_ingot = createIngot(materialName);
			bronze_leggings = createLeggings(materialName);
			bronze_nugget = createNugget(materialName);
			bronze_pickaxe = createPickaxe(materialName);
			bronze_powder = createPowder(materialName);
			bronze_shovel = createShovel(materialName);
			bronze_sword = createSword(materialName);
			bronze_rod = createRod(materialName);
			bronze_gear = createGear(materialName);

			bronze_slab = createSlab(materialName);
			bronze_shield = createShield(materialName);

			bronze_crystal = createCrystal(materialName);
			bronze_shard = createShard(materialName);
			bronze_clump = createClump(materialName);
			bronze_powder_dirty = createDirtyPowder(materialName);

			bronze_dense_plate = createDensePlate(materialName);
			bronze_crushed = createCrushed(materialName);
			bronze_crushed_purified = createCrushedPurified(materialName);
		}
		if (Options.ENABLE_CHARCOAL) {
			charcoal_powder = addItem(new Item(), "charcoal_powder", null, ItemGroups.tab_items);
			itemRegistry.put("charcoal_powder", charcoal_powder);
			OreDictionary.registerOre("dustCharcoal", charcoal_powder);
		}
		if (Options.ENABLE_COAL) {
			coal_powder = addItem(new Item(), "coal_powder", null, ItemGroups.tab_items);
			itemRegistry.put("coal_powder", coal_powder);
			OreDictionary.registerOre("dustCoal", coal_powder);
		}

		if (Options.ENABLE_COLDIRON) {
			materialName = "coldiron";

			coldiron_arrow = createArrow(materialName);
			coldiron_bolt = createBolt(materialName);
			coldiron_bow = createBow(materialName);
			coldiron_crossbow = createCrossbow(materialName);
			coldiron_fishing_rod = createFishingRod(materialName);
			coldiron_horsearmor = createHorseArmor(materialName);
			coldiron_shears = createShears(materialName);
			coldiron_smallpowder = createSmallPowder(materialName);

			coldiron_axe = createAxe(materialName);
			coldiron_boots = createBoots(materialName);
			coldiron_chestplate = createChestplate(materialName);
			coldiron_crackhammer = createCrackhammer(materialName);
			coldiron_door = createDoor(materialName);
			coldiron_helmet = createHelmet(materialName);
			coldiron_hoe = createHoe(materialName);
			coldiron_ingot = createIngot(materialName);
			coldiron_leggings = createLeggings(materialName);
			coldiron_nugget = createNugget(materialName);
			coldiron_pickaxe = createPickaxe(materialName);
			coldiron_powder = createPowder(materialName);
			coldiron_shovel = createShovel(materialName);
			coldiron_sword = createSword(materialName);
			coldiron_rod = createRod(materialName);
			coldiron_gear = createGear(materialName);

			coldiron_slab = createSlab(materialName);
			coldiron_shield = createShield(materialName);

			coldiron_crystal = createCrystal(materialName);
			coldiron_shard = createShard(materialName);
			coldiron_clump = createClump(materialName);
			coldiron_powder_dirty = createDirtyPowder(materialName);

			coldiron_dense_plate = createDensePlate(materialName);
			coldiron_crushed = createCrushed(materialName);
			coldiron_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_COPPER) {
			materialName = "copper";

			copper_arrow = createArrow(materialName);
			copper_bolt = createBolt(materialName);
			copper_bow = createBow(materialName);
			copper_crossbow = createCrossbow(materialName);
			copper_fishing_rod = createFishingRod(materialName);
			copper_horsearmor = createHorseArmor(materialName);
			copper_shears = createShears(materialName);
			copper_smallpowder = createSmallPowder(materialName);

			copper_axe = createAxe(materialName);
			copper_boots = createBoots(materialName);
			copper_chestplate = createChestplate(materialName);
			copper_crackhammer = createCrackhammer(materialName);
			copper_door = createDoor(materialName);
			copper_helmet = createHelmet(materialName);
			copper_hoe = createHoe(materialName);
			copper_ingot = createIngot(materialName);
			copper_leggings = createLeggings(materialName);
			copper_nugget = createNugget(materialName);
			copper_pickaxe = createPickaxe(materialName);
			copper_powder = createPowder(materialName);
			copper_shovel = createShovel(materialName);
			copper_sword = createSword(materialName);
			copper_rod = createRod(materialName);
			copper_gear = createGear(materialName);

			copper_slab = createSlab(materialName);
			copper_shield = createShield(materialName);

			copper_dense_plate = createDensePlate(materialName);
			copper_crushed = createCrushed(materialName);
			copper_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_CUPRONICKEL) {
			materialName = "cupronickel";

			cupronickel_arrow = createArrow(materialName);
			cupronickel_bolt = createBolt(materialName);
			cupronickel_bow = createBow(materialName);
			cupronickel_crossbow = createCrossbow(materialName);
			cupronickel_fishing_rod = createFishingRod(materialName);
			cupronickel_horsearmor = createHorseArmor(materialName);
			cupronickel_shears = createShears(materialName);
			cupronickel_smallblend = createSmallBlend(materialName);
			cupronickel_smallpowder = createSmallPowder(materialName);

			cupronickel_axe = createAxe(materialName);
			cupronickel_blend = createBlend(materialName);
			cupronickel_boots = createBoots(materialName);
			cupronickel_chestplate = createChestplate(materialName);
			cupronickel_crackhammer = createCrackhammer(materialName);
			cupronickel_door = createDoor(materialName);
			cupronickel_helmet = createHelmet(materialName);
			cupronickel_hoe = createHoe(materialName);
			cupronickel_ingot = createIngot(materialName);
			cupronickel_leggings = createLeggings(materialName);
			cupronickel_nugget = createNugget(materialName);
			cupronickel_pickaxe = createPickaxe(materialName);
			cupronickel_powder = createPowder(materialName);
			cupronickel_shovel = createShovel(materialName);
			cupronickel_sword = createSword(materialName);
			cupronickel_rod = createRod(materialName);
			cupronickel_gear = createGear(materialName);

			cupronickel_slab = createSlab(materialName);
			cupronickel_shield = createShield(materialName);

			cupronickel_crystal = createCrystal(materialName);
			cupronickel_shard = createShard(materialName);
			cupronickel_clump = createClump(materialName);
			cupronickel_powder_dirty = createDirtyPowder(materialName);

			cupronickel_dense_plate = createDensePlate(materialName);
			cupronickel_crushed = createCrushed(materialName);
			cupronickel_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_DIAMOND) {
			materialName = "diamond";

			diamond_crackhammer = createCrackhammer(materialName);
			diamond_gear = createGear(materialName);
			//diamond_rod = createRod(materialName);
		}

		if (Options.ENABLE_ELECTRUM) {
			materialName = "electrum";

			electrum_arrow = createArrow(materialName);
			electrum_bolt = createBolt(materialName);
			electrum_bow = createBow(materialName);
			electrum_crossbow = createCrossbow(materialName);
			electrum_fishing_rod = createFishingRod(materialName);
			electrum_horsearmor = createHorseArmor(materialName);
			electrum_shears = createShears(materialName);
			electrum_smallblend = createSmallBlend(materialName);
			electrum_smallpowder = createSmallPowder(materialName);

			electrum_axe = createAxe(materialName);
			electrum_blend = createBlend(materialName);
			electrum_boots = createBoots(materialName);
			electrum_chestplate = createChestplate(materialName);
			electrum_crackhammer = createCrackhammer(materialName);
			electrum_door = createDoor(materialName);
			electrum_helmet = createHelmet(materialName);
			electrum_hoe = createHoe(materialName);
			electrum_ingot = createIngot(materialName);
			electrum_leggings = createLeggings(materialName);
			electrum_nugget = createNugget(materialName);
			electrum_pickaxe = createPickaxe(materialName);
			electrum_powder = createPowder(materialName);
			electrum_shovel = createShovel(materialName);
			electrum_sword = createSword(materialName);
			electrum_rod = createRod(materialName);
			electrum_gear = createGear(materialName);

			electrum_slab = createSlab(materialName);
			electrum_shield = createShield(materialName);

			electrum_crystal = createCrystal(materialName);
			electrum_shard = createShard(materialName);
			electrum_clump = createClump(materialName);
			electrum_powder_dirty = createDirtyPowder(materialName);

			electrum_dense_plate = createDensePlate(materialName);
			electrum_crushed = createCrushed(materialName);
			electrum_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_GOLD) {
			materialName = "gold";

			gold_crackhammer = createCrackhammer(materialName);
			gold_powder = createPowder(materialName);
//			gold_smallpowder = createSmallPowder(materialName);
			gold_rod = createRod(materialName);
			gold_gear = createGear(materialName);
		}

		if (Options.ENABLE_INVAR) {
			materialName = "invar";

			invar_arrow = createArrow(materialName);
			invar_bolt = createBolt(materialName);
			invar_bow = createBow(materialName);
			invar_crossbow = createCrossbow(materialName);
			invar_fishing_rod = createFishingRod(materialName);
			invar_horsearmor = createHorseArmor(materialName);
			invar_shears = createShears(materialName);
			invar_smallblend = createSmallBlend(materialName);
			invar_smallpowder = createSmallPowder(materialName);

			invar_axe = createAxe(materialName);
			invar_blend = createBlend(materialName);
			invar_boots = createBoots(materialName);
			invar_chestplate = createChestplate(materialName);
			invar_crackhammer = createCrackhammer(materialName);
			invar_door = createDoor(materialName);
			invar_helmet = createHelmet(materialName);
			invar_hoe = createHoe(materialName);
			invar_ingot = createIngot(materialName);
			invar_leggings = createLeggings(materialName);
			invar_nugget = createNugget(materialName);
			invar_pickaxe = createPickaxe(materialName);
			invar_powder = createPowder(materialName);
			invar_shovel = createShovel(materialName);
			invar_sword = createSword(materialName);
			invar_rod = createRod(materialName);
			invar_gear = createGear(materialName);

			invar_slab = createSlab(materialName);
			invar_shield = createShield(materialName);

			invar_crystal = createCrystal(materialName);
			invar_shard = createShard(materialName);
			invar_clump = createClump(materialName);
			invar_powder_dirty = createDirtyPowder(materialName);

			invar_dense_plate = createDensePlate(materialName);
			invar_crushed = createCrushed(materialName);
			invar_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_IRON) {
			materialName = "iron";

			iron_crackhammer = createCrackhammer(materialName);
			iron_nugget = createNugget(materialName);
			iron_powder = createPowder(materialName);
			iron_rod = createRod(materialName);
			iron_gear = createGear(materialName);
		}

		if (Options.ENABLE_LEAD) {
			materialName = "lead";

			lead_arrow = createArrow(materialName);
			lead_bolt = createBolt(materialName);
			lead_bow = createBow(materialName);
			lead_crossbow = createCrossbow(materialName);
			lead_fishing_rod = createFishingRod(materialName);
			lead_horsearmor = createHorseArmor(materialName);
			lead_shears = createShears(materialName);
			lead_smallpowder = createSmallPowder(materialName);

			lead_axe = createAxe(materialName);
			lead_boots = createBoots(materialName);
			lead_chestplate = createChestplate(materialName);
			lead_crackhammer = createCrackhammer(materialName);
			lead_door = createDoor(materialName);
			lead_helmet = createHelmet(materialName);
			lead_hoe = createHoe(materialName);
			lead_ingot = createIngot(materialName);
			lead_leggings = createLeggings(materialName);
			lead_nugget = createNugget(materialName);
			lead_pickaxe = createPickaxe(materialName);
			lead_powder = createPowder(materialName);
			lead_shovel = createShovel(materialName);
			lead_sword = createSword(materialName);
			lead_rod = createRod(materialName);
			lead_gear = createGear(materialName);

			lead_slab = createSlab(materialName);
			lead_shield = createShield(materialName);

			lead_dense_plate = createDensePlate(materialName);
			lead_crushed = createCrushed(materialName);
			lead_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_PLATINUM) {
			materialName = "platinum";

			platinum_arrow = createArrow(materialName);
			platinum_bolt = createBolt(materialName);
			platinum_bow = createBow(materialName);
			platinum_crossbow = createCrossbow(materialName);
			platinum_fishing_rod = createFishingRod(materialName);
			platinum_horsearmor = createHorseArmor(materialName);
			platinum_shears = createShears(materialName);
			platinum_smallpowder = createSmallPowder(materialName);

			platinum_axe = createAxe(materialName);
			platinum_boots = createBoots(materialName);
			platinum_chestplate = createChestplate(materialName);
			platinum_crackhammer = createCrackhammer(materialName);
			platinum_door = createDoor(materialName);
			platinum_helmet = createHelmet(materialName);
			platinum_hoe = createHoe(materialName);
			platinum_ingot = createIngot(materialName);
			platinum_leggings = createLeggings(materialName);
			platinum_nugget = createNugget(materialName);
			platinum_pickaxe = createPickaxe(materialName);
			platinum_powder = createPowder(materialName);
			platinum_shovel = createShovel(materialName);
			platinum_sword = createSword(materialName);
			platinum_rod = createRod(materialName);
			platinum_gear = createGear(materialName);

			platinum_slab = createSlab(materialName);
			platinum_shield = createShield(materialName);

			platinum_crystal = createCrystal(materialName);
			platinum_shard = createShard(materialName);
			platinum_clump = createClump(materialName);
			platinum_powder_dirty = createDirtyPowder(materialName);

			platinum_dense_plate = createDensePlate(materialName);
			platinum_crushed = createCrushed(materialName);
			platinum_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_MERCURY) {
			// mercury is special
			mercury_ingot = addItem(new Item(), "mercury_ingot", null, ItemGroups.tab_items);
//			itemRegistry.put("mercury_ingot", mercury_ingot);
			OreDictionary.registerOre("ingotMercury", mercury_ingot);
			OreDictionary.registerOre("quicksilver", mercury_ingot);

			mercury_powder = addItem(new Item(), "mercury_powder", null, ItemGroups.tab_items);
//			itemRegistry.put("mercury_powder", mercury_powder);
			OreDictionary.registerOre("dustMercury", mercury_powder);
		}

		if (Options.ENABLE_MITHRIL) {
			materialName = "mithril";

			mithril_arrow = createArrow(materialName);
			mithril_bolt = createBolt(materialName);
			mithril_bow = createBow(materialName);
			mithril_crossbow = createCrossbow(materialName);
			mithril_fishing_rod = createFishingRod(materialName);
			mithril_horsearmor = createHorseArmor(materialName);
			mithril_shears = createShears(materialName);
			mithril_smallblend = createSmallBlend(materialName);
			mithril_smallpowder = createSmallPowder(materialName);

			mithril_axe = createAxe(materialName);
			mithril_blend = createBlend(materialName);
			mithril_boots = createBoots(materialName);
			mithril_chestplate = createChestplate(materialName);
			mithril_crackhammer = createCrackhammer(materialName);
			mithril_door = createDoor(materialName);
			mithril_helmet = createHelmet(materialName);
			mithril_hoe = createHoe(materialName);
			mithril_ingot = createIngot(materialName);
			mithril_leggings = createLeggings(materialName);
			mithril_nugget = createNugget(materialName);
			mithril_pickaxe = createPickaxe(materialName);
			mithril_powder = createPowder(materialName);
			mithril_shovel = createShovel(materialName);
			mithril_sword = createSword(materialName);
			mithril_rod = createRod(materialName);
			mithril_gear = createGear(materialName);

			mithril_slab = createSlab(materialName);
			mithril_shield = createShield(materialName);

			mithril_crystal = createCrystal(materialName);
			mithril_shard = createShard(materialName);
			mithril_clump = createClump(materialName);
			mithril_powder_dirty = createDirtyPowder(materialName);

			mithril_dense_plate = createDensePlate(materialName);
			mithril_crushed = createCrushed(materialName);
			mithril_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_NICKEL) {
			materialName = "nickel";

			nickel_arrow = createArrow(materialName);
			nickel_bolt = createBolt(materialName);
			nickel_bow = createBow(materialName);
			nickel_crossbow = createCrossbow(materialName);
			nickel_fishing_rod = createFishingRod(materialName);
			nickel_horsearmor = createHorseArmor(materialName);
			nickel_shears = createShears(materialName);
			nickel_smallpowder = createSmallPowder(materialName);

			nickel_axe = createAxe(materialName);
			nickel_boots = createBoots(materialName);
			nickel_chestplate = createChestplate(materialName);
			nickel_crackhammer = createCrackhammer(materialName);
			nickel_door = createDoor(materialName);
			nickel_helmet = createHelmet(materialName);
			nickel_hoe = createHoe(materialName);
			nickel_ingot = createIngot(materialName);
			nickel_leggings = createLeggings(materialName);
			nickel_nugget = createNugget(materialName);
			nickel_pickaxe = createPickaxe(materialName);
			nickel_powder = createPowder(materialName);
			nickel_shovel = createShovel(materialName);
			nickel_sword = createSword(materialName);
			nickel_rod = createRod(materialName);
			nickel_gear = createGear(materialName);

			nickel_slab = createSlab(materialName);
			nickel_shield = createShield(materialName);

			nickel_crystal = createCrystal(materialName);
			nickel_shard = createShard(materialName);
			nickel_clump = createClump(materialName);
			nickel_powder_dirty = createDirtyPowder(materialName);

			nickel_dense_plate = createDensePlate(materialName);
			nickel_crushed = createCrushed(materialName);
			nickel_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_PEWTER) {
			materialName = "pewter";

			pewter_arrow = createArrow(materialName);
			pewter_bolt = createBolt(materialName);
			pewter_bow = createBow(materialName);
			pewter_crossbow = createCrossbow(materialName);
			pewter_fishing_rod = createFishingRod(materialName);
			pewter_horsearmor = createHorseArmor(materialName);
			pewter_shears = createShears(materialName);
			pewter_smallpowder = createSmallPowder(materialName);

			pewter_axe = createAxe(materialName);
			pewter_blend = createBlend(materialName);
			pewter_boots = createBoots(materialName);
			pewter_chestplate = createChestplate(materialName);
			pewter_crackhammer = createCrackhammer(materialName);
			pewter_door = createDoor(materialName);
			pewter_helmet = createHelmet(materialName);
			pewter_hoe = createHoe(materialName);
			pewter_ingot = createIngot(materialName);
			pewter_leggings = createLeggings(materialName);
			pewter_nugget = createNugget(materialName);
			pewter_pickaxe = createPickaxe(materialName);
			pewter_powder = createPowder(materialName);
			pewter_shovel = createShovel(materialName);
			pewter_sword = createSword(materialName);
			pewter_rod = createRod(materialName);
			pewter_gear = createGear(materialName);

			pewter_slab = createSlab(materialName);
			pewter_shield = createShield(materialName);

			// pewter_crystal = createCrystal(materialName);
			// pewter_shard = createShard(materialName);
			// pewter_clump = createClump(materialName);
			// pewter_powder_dirty = createDirtyPowder(materialName);

			// pewter_dense_plate = createDensePlate(materialName);
			// pewter_crushed = createCrushed(materialName);
			// pewter_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_SILVER) {
			materialName = "silver";

			silver_arrow = createArrow(materialName);
			silver_bolt = createBolt(materialName);
			silver_bow = createBow(materialName);
			silver_crossbow = createCrossbow(materialName);
			silver_fishing_rod = createFishingRod(materialName);
			silver_horsearmor = createHorseArmor(materialName);
			silver_shears = createShears(materialName);
			silver_smallpowder = createSmallPowder(materialName);

			silver_axe = createAxe(materialName);
			silver_boots = createBoots(materialName);
			silver_chestplate = createChestplate(materialName);
			silver_crackhammer = createCrackhammer(materialName);
			silver_door = createDoor(materialName);
			silver_helmet = createHelmet(materialName);
			silver_hoe = createHoe(materialName);
			silver_ingot = createIngot(materialName);
			silver_leggings = createLeggings(materialName);
			silver_nugget = createNugget(materialName);
			silver_pickaxe = createPickaxe(materialName);
			silver_powder = createPowder(materialName);
			silver_shovel = createShovel(materialName);
			silver_sword = createSword(materialName);
			silver_rod = createRod(materialName);
			silver_gear = createGear(materialName);

			silver_slab = createSlab(materialName);
			silver_shield = createShield(materialName);

			silver_dense_plate = createDensePlate(materialName);
			silver_crushed = createCrushed(materialName);
			silver_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_STARSTEEL) {
			materialName = "starsteel";

			starsteel_arrow = createArrow(materialName);
			starsteel_bolt = createBolt(materialName);
			starsteel_bow = createBow(materialName);
			starsteel_crossbow = createCrossbow(materialName);
			starsteel_fishing_rod = createFishingRod(materialName);
			starsteel_horsearmor = createHorseArmor(materialName);
			starsteel_shears = createShears(materialName);
			starsteel_smallpowder = createSmallPowder(materialName);

			starsteel_axe = createAxe(materialName);
			starsteel_boots = createBoots(materialName);
			starsteel_chestplate = createChestplate(materialName);
			starsteel_crackhammer = createCrackhammer(materialName);
			starsteel_door = createDoor(materialName);
			starsteel_helmet = createHelmet(materialName);
			starsteel_hoe = createHoe(materialName);
			starsteel_ingot = createIngot(materialName);
			starsteel_leggings = createLeggings(materialName);
			starsteel_nugget = createNugget(materialName);
			starsteel_pickaxe = createPickaxe(materialName);
			starsteel_powder = createPowder(materialName);
			starsteel_shovel = createShovel(materialName);
			starsteel_sword = createSword(materialName);
			starsteel_rod = createRod(materialName);
			starsteel_gear = createGear(materialName);

			starsteel_slab = createSlab(materialName);
			starsteel_shield = createShield(materialName);

			starsteel_crystal = createCrystal(materialName);
			starsteel_shard = createShard(materialName);
			starsteel_clump = createClump(materialName);
			starsteel_powder_dirty = createDirtyPowder(materialName);

			starsteel_dense_plate = createDensePlate(materialName);
			starsteel_crushed = createCrushed(materialName);
			starsteel_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_STONE) {
			materialName = "stone";

			stone_crackhammer = createCrackhammer(materialName);
			stone_rod = createRod(materialName);
			stone_gear = createGear(materialName);
		}

		if (Options.ENABLE_STEEL) {
			materialName = "steel";

			steel_arrow = createArrow(materialName);
			steel_bolt = createBolt(materialName);
			steel_bow = createBow(materialName);
			steel_crossbow = createCrossbow(materialName);
			steel_fishing_rod = createFishingRod(materialName);
			steel_horsearmor = createHorseArmor(materialName);
			steel_shears = createShears(materialName);
			steel_smallblend = createSmallBlend(materialName);
			steel_smallpowder = createSmallPowder(materialName);

			steel_axe = createAxe(materialName);
			steel_blend = createBlend(materialName);
			steel_boots = createBoots(materialName);
			steel_chestplate = createChestplate(materialName);
			steel_crackhammer = createCrackhammer(materialName);
			steel_door = createDoor(materialName);
			steel_helmet = createHelmet(materialName);
			steel_hoe = createHoe(materialName);
			steel_ingot = createIngot(materialName);
			steel_leggings = createLeggings(materialName);
			steel_nugget = createNugget(materialName);
			steel_pickaxe = createPickaxe(materialName);
			steel_powder = createPowder(materialName);
			steel_shovel = createShovel(materialName);
			steel_sword = createSword(materialName);
			steel_rod = createRod(materialName);
			steel_gear = createGear(materialName);

			steel_slab = createSlab(materialName);
			steel_shield = createShield(materialName);

			steel_crystal = createCrystal(materialName);
			steel_shard = createShard(materialName);
			steel_clump = createClump(materialName);
			steel_powder_dirty = createDirtyPowder(materialName);

			steel_dense_plate = createDensePlate(materialName);
			steel_crushed = createCrushed(materialName);
			steel_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_TIN) {
			materialName = "tin";

			tin_arrow = createArrow(materialName);
			tin_bolt = createBolt(materialName);
			tin_bow = createBow(materialName);
			tin_crossbow = createCrossbow(materialName);
			tin_fishing_rod = createFishingRod(materialName);
			tin_horsearmor = createHorseArmor(materialName);

			tin_axe = createAxe(materialName);
			tin_boots = createBoots(materialName);
			tin_chestplate = createChestplate(materialName);
			tin_crackhammer = createCrackhammer(materialName);
			tin_door = createDoor(materialName);
			tin_helmet = createHelmet(materialName);
			tin_hoe = createHoe(materialName);
			tin_ingot = createIngot(materialName);
			tin_leggings = createLeggings(materialName);
			tin_nugget = createNugget(materialName);
			tin_pickaxe = createPickaxe(materialName);
			tin_powder = createPowder(materialName);
			tin_shears = createShears(materialName);
			tin_shovel = createShovel(materialName);
			tin_smallpowder = createSmallPowder(materialName);
			tin_sword = createSword(materialName);
			tin_rod = createRod(materialName);
			tin_gear = createGear(materialName);

			tin_slab = createSlab(materialName);
			tin_shield = createShield(materialName);

			tin_dense_plate = createDensePlate(materialName);
			tin_crushed = createCrushed(materialName);
			tin_crushed_purified = createCrushedPurified(materialName);
		}

		if (Options.ENABLE_WOOD) {
			materialName = "wood";

			wood_crackhammer = createCrackhammer(materialName);
			wood_gear = createGear(materialName);
		}

		if (Options.ENABLE_ZINC) {
			materialName = "zinc";

			zinc_arrow = createArrow(materialName);
			zinc_axe = createAxe(materialName);
			zinc_bolt = createBolt(materialName);
			zinc_boots = createBoots(materialName);
			zinc_bow = createBow(materialName);
			zinc_chestplate = createChestplate(materialName);
			zinc_crackhammer = createCrackhammer(materialName);
			zinc_crossbow = createCrossbow(materialName);
			zinc_door = createDoor(materialName);
			zinc_fishing_rod = createFishingRod(materialName);
			zinc_helmet = createHelmet(materialName);
			zinc_hoe = createHoe(materialName);
			zinc_horsearmor = createHorseArmor(materialName);
			zinc_ingot = createIngot(materialName);
			zinc_leggings = createLeggings(materialName);
			zinc_nugget = createNugget(materialName);
			tin_pickaxe = createPickaxe(materialName);
			zinc_powder = createPowder(materialName);
			zinc_shears = createShears(materialName);
			zinc_shovel = createShovel(materialName);
			zinc_smallpowder = createSmallPowder(materialName);
			zinc_sword = createSword(materialName);
			zinc_rod = createRod(materialName);
			zinc_gear = createGear(materialName);

			zinc_slab = createSlab(materialName);
			zinc_shield = createShield(materialName);

			zinc_crystal = createCrystal(materialName);
			zinc_shard = createShard(materialName);
			zinc_clump = createClump(materialName);
			zinc_powder_dirty = createDirtyPowder(materialName);

			zinc_dense_plate = createDensePlate(materialName);
			zinc_crushed = createCrushed(materialName);
			zinc_crushed_purified = createCrushedPurified(materialName);
		}

		setSortingList();
		addToMetList();
		
		initDone = true;
	}

	private static void setSortingList() {
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
	}

	protected static void addToMetList() {
		final List<MetalMaterial> metlist = new ArrayList<>(Materials.getAllMaterials().size());
		metlist.addAll(Materials.getAllMaterials());
		metlist.sort((MetalMaterial a, MetalMaterial b) -> a.getName().compareToIgnoreCase(b.getName()));
		for (int i = 0; i < metlist.size(); i++) {
			materialSortingValues.put(metlist.get(i), i * 100);
		}
	}

	protected static Item addItem(Item item, String name, MetalMaterial material, CreativeTabs tab) {

		String fullName;
		if (material != null) {
			fullName = material.getName() + "_" + name;
		} else {
			fullName = name;
		}

		// Loader.instance().activeModContainer().getModId()
		item.setRegistryName(fullName);
		item.setUnlocalizedName(item.getRegistryName().getResourceDomain() + "." + fullName);
		GameRegistry.register(item);
		itemRegistry.put(fullName, item);

		if (tab != null) {
			item.setCreativeTab(tab);
		}

		if (material != null) {
			itemsByMaterial.computeIfAbsent(material, (MetalMaterial g) -> new ArrayList<>());
			itemsByMaterial.get(material).add(item);
		}

		if (item instanceof IOreDictionaryEntry) {
			OreDictionary.registerOre(((IOreDictionaryEntry) item).getOreDictionaryName(), item);
		}

		return item;
	}

	protected static Item createIngot(String materialName) {
		if (Options.ENABLE_BASICS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalIngot(material), "ingot", material, ItemGroups.tab_items);
			material.ingot = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createNugget(String materialName) {
		if (Options.ENABLE_BASICS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalNugget(material), "nugget", material, ItemGroups.tab_items);
			material.nugget = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createPowder(String materialName) {
		if (Options.ENABLE_BASICS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalPowder(material), "powder", material, ItemGroups.tab_items);
			material.powder = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createBlend(String materialName) {
		if (Options.ENABLE_BASICS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalBlend(material), "blend", material, ItemGroups.tab_items);
			material.blend = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createRod(String materialName) {
		if (Options.ENABLE_ROD) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Item i = addItem(new ItemMetalRod(material), "rod", material, ItemGroups.tab_items);
		material.rod = i;
		return i;
		} else {
			return null;
		}
	}

	protected static Item createGear(String materialName) {
		if (Options.ENABLE_GEAR) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Item i = addItem(new ItemMetalGear(material), "gear", material, ItemGroups.tab_items);
		material.gear = i;
		return i;
		} else {
			return null;
		}
	}

	protected static Item createAxe(String materialName) {
		if (Options.ENABLE_BASIC_TOOLS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalAxe(material), "axe", material, ItemGroups.tab_tools);
			material.axe = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createCrackhammer(String materialName) {
		if (Options.ENABLE_CRACKHAMMER) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalCrackHammer(material), "crackhammer", material, ItemGroups.tab_tools);
			material.crackhammer = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createHoe(String materialName) {
		if (Options.ENABLE_BASIC_TOOLS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalHoe(material), "hoe", material, ItemGroups.tab_tools);
			material.hoe = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createPickaxe(String materialName) {
		if (Options.ENABLE_BASIC_TOOLS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalPickaxe(material), "pickaxe", material, ItemGroups.tab_tools);
			material.pickaxe = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createShovel(String materialName) {
		if (Options.ENABLE_BASIC_TOOLS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalShovel(material), "shovel", material, ItemGroups.tab_tools);
			material.shovel = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createSword(String materialName) {
		if (Options.ENABLE_BASIC_TOOLS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalSword(material), "sword", material, ItemGroups.tab_tools);
			material.sword = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createHelmet(String materialName) {
		if (Options.ENABLE_ARMOR) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(ItemMetalArmor.createHelmet(material), "helmet", material, ItemGroups.tab_tools);
			material.helmet = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createChestplate(String materialName) {
		if (Options.ENABLE_ARMOR) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(ItemMetalArmor.createChestplate(material), "chestplate", material, ItemGroups.tab_tools);
			material.chestplate = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createLeggings(String materialName) {
		if (Options.ENABLE_ARMOR) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(ItemMetalArmor.createLeggings(material), "leggings", material, ItemGroups.tab_tools);
			material.leggings = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createBoots(String materialName) {
		if (Options.ENABLE_ARMOR) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(ItemMetalArmor.createBoots(material), "boots", material, ItemGroups.tab_tools);
			material.boots = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createHorseArmor(String materialName) {
		if (Options.ENABLE_HORSE_ARMOR) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalHorseArmor(material), "horsearmor", material, ItemGroups.tab_tools);
			material.horse_armor = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createArrow(String materialName) {
		if (Options.ENABLE_BOW_AND_ARROW) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalArrow(material), "arrow", material, ItemGroups.tab_tools);
			material.arrow = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createBolt(String materialName) {
		if (Options.ENABLE_CROSSBOW_AND_BOLT) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalBolt(material), "bolt", material, ItemGroups.tab_tools);
			material.bolt = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createBow(String materialName) {
		if (Options.ENABLE_BOW_AND_ARROW) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalBow(material), "bow", material, ItemGroups.tab_tools);
			material.bow = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createCrossbow(String materialName) {
		if (Options.ENABLE_CROSSBOW_AND_BOLT) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalCrossbow(material), "crossbow", material, ItemGroups.tab_tools);
			material.crossbow = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createShears(String materialName) {
		if (Options.ENABLE_SHEARS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalShears(material), "shears", material, ItemGroups.tab_tools);
			material.shears = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createSmallBlend(String materialName) {
		if (Options.ENABLE_SMALL_DUSTS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalSmallBlend(material), "smallblend", material, ItemGroups.tab_items);
			material.smallblend = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createFishingRod(String materialName) {
		if (Options.ENABLE_FISHING_ROD) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalFishingRod(material), "fishing_rod", material, ItemGroups.tab_tools);
			material.fishing_rod = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createSmallPowder(String materialName) {
		if (Options.ENABLE_SMALL_DUSTS) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalSmallPowder(material), "smallpowder", material, ItemGroups.tab_items);
			material.smallpowder = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createShield(String materialName) {
		if (Options.ENABLE_SHIELD) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalShield(material), "shield", material, ItemGroups.tab_items);
			material.shield = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createCrystal(String materialName) {
		if (Options.ENABLE_MEKANISM) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new GenericMetalItem(material), "crystal", material, ItemGroups.tab_items);
			OreDictionary.registerOre("crystal" + material.getCapitalizedName(), i);
			material.crystal = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createShard(String materialName) {
		if (Options.ENABLE_MEKANISM) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new GenericMetalItem(material), "shard", material, ItemGroups.tab_items);
			OreDictionary.registerOre("shard" + material.getCapitalizedName(), i);
			material.shard = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createClump(String materialName) {
		if (Options.ENABLE_MEKANISM) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new GenericMetalItem(material), "clump", material, ItemGroups.tab_items);
			OreDictionary.registerOre("clump" + material.getCapitalizedName(), i);
			material.clump = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createDirtyPowder(String materialName) {
		if (Options.ENABLE_MEKANISM) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new GenericMetalItem(material), "powder_dirty", material, ItemGroups.tab_items);
			OreDictionary.registerOre("dustDirty" + material.getCapitalizedName(), i);
			material.powder_dirty = i;
			return i;
		} else {
			return null;
		}
	}

	// TODO: Possibly make this a block, 1/2 of the normal plate.
	protected static Item createDensePlate(String materialName) {
		if (Options.ENABLE_IC2) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new GenericMetalItem(material), "dense_plate", material, ItemGroups.tab_items);
			OreDictionary.registerOre("plateDense" + material.getCapitalizedName(), i);
			material.dense_plate = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createCrushed(String materialName) {
		if (Options.ENABLE_IC2) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new GenericMetalItem(material), "crushed", material, ItemGroups.tab_items);
			OreDictionary.registerOre("crushed" + material.getCapitalizedName(), i);
			material.crushed = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createCrushedPurified(String materialName) {
		if (Options.ENABLE_IC2) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new GenericMetalItem(material), "crushed_purified", material, ItemGroups.tab_items);
			OreDictionary.registerOre("crushedPurified" + material.getCapitalizedName(), i);
			material.crushed_purified = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createSlab(String materialName) {
		if (Options.ENABLE_SLAB) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalSlab(material), "slab", material, ItemGroups.tab_blocks);
			material.slab = i;
			return i;
		} else {
			return null;
		}
	}

	protected static Item createDoor(String materialName) {
		if (Options.ENABLE_DOOR) {
			MetalMaterial material = Materials.getMaterialByName(materialName);
			final Item i = addItem(new ItemMetalDoor(material), "door", material, ItemGroups.tab_blocks);
			material.door = i;
			return i;
		} else {
			return null;
		}
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
		for (final Field f : fields) {
			if (Modifier.isStatic(f.getModifiers()) && f.getType().isArray() && f.getType().getComponentType().equals(float.class)) {
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
	}

	/**
	 *
	 * @param a The itemstack
	 * @return The output
	 */
	public static int getSortingValue(ItemStack a) {
		int classVal = 990000;
		int metalVal = 9900;
		if ((a.getItem() instanceof ItemBlock) && (((ItemBlock) a.getItem()).getBlock() instanceof IMetalObject)) {
			classVal = classSortingValues.computeIfAbsent(((ItemBlock) a.getItem()).getBlock().getClass(), (Class<?> c) -> 990000);
			metalVal = materialSortingValues.computeIfAbsent(((IMetalObject) ((ItemBlock) a.getItem()).getBlock()).getMaterial(), (MetalMaterial m) -> 9900);
		} else if (a.getItem() instanceof IMetalObject) {
			classVal = classSortingValues.computeIfAbsent(a.getItem().getClass(), (Class<?> c) -> 990000);
			metalVal = materialSortingValues.computeIfAbsent(((IMetalObject) a.getItem()).getMaterial(), (MetalMaterial m) -> 9900);
		}
		return classVal + metalVal + (a.getMetadata() % 100);
	}

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
		return itemRegistry.inverse().get(i);
	}

	public static Map<String, Item> getItemRegistry() {
		return itemRegistry;
	}

	/**
	 * Gets a map of all items added, sorted by material
	 *
	 * @return An unmodifiable map of added items catagorized by metal material
	 */
	public static Map<MetalMaterial, List<Item>> getItemsByMaterial() {
		return Collections.unmodifiableMap(itemsByMaterial);
	}

	/**
	 * Gets a map of all items added, sorted by material
	 *
	 * @return An unmodifiable map of added items catagorized by metal material
	 */
	@Deprecated
	public static Map<MetalMaterial, List<Item>> getItemsByMetal() {
		return Collections.unmodifiableMap(itemsByMaterial);
	}
}
