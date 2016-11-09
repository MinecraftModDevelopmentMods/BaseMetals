package cyano.basemetals.init;

import java.util.*;

import com.google.common.collect.*;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.blocks.*;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import cyano.basemetals.util.Config.Options;
import net.minecraft.block.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This class initializes all blocks in Base Metals and provides some utility
 * methods for looking up blocks.
 *
 * @author DrCyano
 *
 */
public abstract class Blocks {

	public static Block adamantine_bars;
	public static Block adamantine_block;
	public static Block adamantine_plate;
	public static BlockDoor adamantine_door;
	public static Block adamantine_ore;
	public static Block adamantine_trapdoor;

	public static Block adamantine_button;
	public static BlockSlab adamantine_slab;
	public static BlockSlab double_adamantine_slab;
	public static Block adamantine_lever;
	public static Block adamantine_pressure_plate;
	public static Block adamantine_stairs;
	public static Block adamantine_wall;

	public static Block antimony_bars;
	public static Block antimony_block;
	public static Block antimony_plate;
	public static BlockDoor antimony_door;
	public static Block antimony_ore;
	public static Block antimony_trapdoor;

	public static Block antimony_button;
	public static BlockSlab antimony_slab;
	public static BlockSlab double_antimony_slab;
	public static Block antimony_lever;
	public static Block antimony_pressure_plate;
	public static Block antimony_stairs;
	public static Block antimony_wall;

	public static Block aquarium_bars;
	public static Block aquarium_block;
	public static Block aquarium_plate;
	public static BlockDoor aquarium_door;
	public static Block aquarium_trapdoor;

	public static Block aquarium_button;
	public static BlockSlab aquarium_slab;
	public static BlockSlab double_aquarium_slab;
	public static Block aquarium_lever;
	public static Block aquarium_pressure_plate;
	public static Block aquarium_stairs;
	public static Block aquarium_wall;

	public static Block bismuth_bars;
	public static Block bismuth_block;
	public static Block bismuth_plate;
	public static BlockDoor bismuth_door;
	public static Block bismuth_ore;
	public static Block bismuth_trapdoor;

	public static Block bismuth_button;
	public static BlockSlab bismuth_slab;
	public static BlockSlab double_bismuth_slab;
	public static Block bismuth_lever;
	public static Block bismuth_pressure_plate;
	public static Block bismuth_stairs;
	public static Block bismuth_wall;

	public static Block brass_bars;
	public static Block brass_block;
	public static Block brass_plate;
	public static BlockDoor brass_door;
	public static Block brass_trapdoor;

	public static Block brass_button;
	public static BlockSlab brass_slab;
	public static BlockSlab double_brass_slab;
	public static Block brass_lever;
	public static Block brass_pressure_plate;
	public static Block brass_stairs;
	public static Block brass_wall;

	public static Block bronze_bars;
	public static Block bronze_block;
	public static Block bronze_plate;
	public static BlockDoor bronze_door;
	public static Block bronze_trapdoor;

	public static Block bronze_button;
	public static BlockSlab bronze_slab;
	public static BlockSlab double_bronze_slab;
	public static Block bronze_lever;
	public static Block bronze_pressure_plate;
	public static Block bronze_stairs;
	public static Block bronze_wall;

	public static Block coldiron_bars;
	public static Block coldiron_block;
	public static Block coldiron_plate;
	public static BlockDoor coldiron_door;
	public static Block coldiron_ore;
	public static Block coldiron_trapdoor;

	public static Block coldiron_button;
	public static BlockSlab coldiron_slab;
	public static BlockSlab double_coldiron_slab;
	public static Block coldiron_lever;
	public static Block coldiron_pressure_plate;
	public static Block coldiron_stairs;
	public static Block coldiron_wall;

	public static Block copper_bars;
	public static Block copper_block;
	public static Block copper_plate;
	public static BlockDoor copper_door;
	public static Block copper_ore;
	public static Block copper_trapdoor;

	public static Block copper_button;
	public static BlockSlab copper_slab;
	public static BlockSlab double_copper_slab;
	public static Block copper_lever;
	public static Block copper_pressure_plate;
	public static Block copper_stairs;
	public static Block copper_wall;

	public static Block cupronickel_bars;
	public static Block cupronickel_block;
	public static Block cupronickel_plate;
	public static BlockDoor cupronickel_door;
	public static Block cupronickel_trapdoor;

	public static Block cupronickel_button;
	public static BlockSlab cupronickel_slab;
	public static BlockSlab double_cupronickel_slab;
	public static Block cupronickel_lever;
	public static Block cupronickel_pressure_plate;
	public static Block cupronickel_stairs;
	public static Block cupronickel_wall;

	public static Block electrum_bars;
	public static Block electrum_block;
	public static Block electrum_plate;
	public static BlockDoor electrum_door;
	public static Block electrum_trapdoor;

	public static Block electrum_button;
	public static BlockSlab electrum_slab;
	public static BlockSlab double_electrum_slab;
	public static Block electrum_lever;
	public static Block electrum_pressure_plate;
	public static Block electrum_stairs;
	public static Block electrum_wall;

	public static Block invar_bars;
	public static Block invar_block;
	public static Block invar_plate;
	public static BlockDoor invar_door;
	public static Block invar_trapdoor;

	public static Block invar_button;
	public static BlockSlab invar_slab;
	public static BlockSlab double_invar_slab;
	public static Block invar_lever;
	public static Block invar_pressure_plate;
	public static Block invar_stairs;
	public static Block invar_wall;

	public static Block lead_bars;
	public static Block lead_block;
	public static Block lead_plate;
	public static BlockDoor lead_door;
	public static Block lead_ore;
	public static Block lead_trapdoor;

	public static Block lead_button;
	public static BlockSlab lead_slab;
	public static BlockSlab double_lead_slab;
	public static Block lead_lever;
	public static Block lead_pressure_plate;
	public static Block lead_stairs;
	public static Block lead_wall;

	public static Block mercury_ore;

	public static Block mithril_bars;
	public static Block mithril_block;
	public static Block mithril_plate;
	public static BlockDoor mithril_door;
	public static Block mithril_trapdoor;

	public static Block mithril_button;
	public static BlockSlab mithril_slab;
	public static BlockSlab double_mithril_slab;
	public static Block mithril_lever;
	public static Block mithril_pressure_plate;
	public static Block mithril_stairs;
	public static Block mithril_wall;

	public static Block nickel_bars;
	public static Block nickel_block;
	public static Block nickel_plate;
	public static BlockDoor nickel_door;
	public static Block nickel_ore;
	public static Block nickel_trapdoor;

	public static Block nickel_button;
	public static BlockSlab nickel_slab;
	public static BlockSlab double_nickel_slab;
	public static Block nickel_lever;
	public static Block nickel_pressure_plate;
	public static Block nickel_stairs;
	public static Block nickel_wall;

	public static Block pewter_bars;
	public static Block pewter_block;
	public static Block pewter_plate;
	public static BlockDoor pewter_door;
	// public static Block pewter_ore;
	public static Block pewter_trapdoor;

	public static Block pewter_button;
	public static BlockSlab pewter_slab;
	public static BlockSlab double_pewter_slab;
	public static Block pewter_lever;
	public static Block pewter_pressure_plate;
	public static Block pewter_stairs;
	public static Block pewter_wall;

	public static Block platinum_bars;
	public static Block platinum_block;
	public static Block platinum_plate;
	public static BlockDoor platinum_door;
	public static Block platinum_ore;
	public static Block platinum_trapdoor;

	public static Block platinum_button;
	public static BlockSlab platinum_slab;
	public static BlockSlab double_platinum_slab;
	public static Block platinum_lever;
	public static Block platinum_pressure_plate;
	public static Block platinum_stairs;
	public static Block platinum_wall;

	public static Block silver_bars;
	public static Block silver_block;
	public static Block silver_plate;
	public static BlockDoor silver_door;
	public static Block silver_ore;
	public static Block silver_trapdoor;

	public static Block silver_button;
	public static BlockSlab silver_slab;
	public static BlockSlab double_silver_slab;
	public static Block silver_lever;
	public static Block silver_pressure_plate;
	public static Block silver_stairs;
	public static Block silver_wall;

	public static Block starsteel_bars;
	public static Block starsteel_block;
	public static Block starsteel_plate;
	public static BlockDoor starsteel_door;
	public static Block starsteel_ore;
	public static Block starsteel_trapdoor;

	public static Block starsteel_button;
	public static BlockSlab starsteel_slab;
	public static BlockSlab double_starsteel_slab;
	public static Block starsteel_lever;
	public static Block starsteel_pressure_plate;
	public static Block starsteel_stairs;
	public static Block starsteel_wall;

	public static Block steel_bars;
	public static Block steel_block;
	public static Block steel_plate;
	public static BlockDoor steel_door;
	public static Block steel_trapdoor;

	public static Block steel_button;
	public static BlockSlab steel_slab;
	public static BlockSlab double_steel_slab;
	public static Block steel_lever;
	public static Block steel_pressure_plate;
	public static Block steel_stairs;
	public static Block steel_wall;

	public static Block tin_bars;
	public static Block tin_block;
	public static Block tin_plate;
	public static BlockDoor tin_door;
	public static Block tin_ore;
	public static Block tin_trapdoor;

	public static Block tin_button;
	public static BlockSlab tin_slab;
	public static BlockSlab double_tin_slab;
	public static Block tin_lever;
	public static Block tin_pressure_plate;
	public static Block tin_stairs;
	public static Block tin_wall;

	public static Block zinc_bars;
	public static Block zinc_block;
	public static Block zinc_plate;
	public static BlockDoor zinc_door;
	public static Block zinc_ore;
	public static Block zinc_trapdoor;

	public static Block zinc_button;
	public static BlockSlab zinc_slab;
	public static BlockSlab double_zinc_slab;
	public static Block zinc_lever;
	public static Block zinc_pressure_plate;
	public static Block zinc_stairs;
	public static Block zinc_wall;

	public static Block iron_plate;

	public static Block gold_bars;
	public static Block gold_plate;
	public static BlockDoor gold_door;
	public static Block gold_trapdoor;

	public static Block human_detector;

	private static boolean initDone = false;

	private static BiMap<String, Block> blockRegistry = HashBiMap.create(16);
	private static Map<MetalMaterial, List<Block>> blocksByMetal = new HashMap<>();

	/**
	 * Gets an block by its name. The name is the name as it is registered in
	 * the GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "basemetals.")
	 *
	 * @param name The name of the block in question
	 * @return The block matching that name, or null if there isn't one
	 */
	public static Block getBlockByName(String name) {
		return blockRegistry.get(name);
	}

	/**
	 * This is the reverse of the getBlockByName(...) method, returning the
	 * registered name of an block instance (Base Metals blocks only).
	 *
	 * @param b The item in question
	 * @return The name of the item, or null if the item is not a Base Metals
	 * block.
	 */
	public static String getNameOfBlock(Block b) {
		return blockRegistry.inverse().get(b);
	}

	public static Map<String, Block> getBlockRegistry() {
		return blockRegistry;
	}

	/**
	 * Gets a map of all blocks added, sorted by metal
	 *
	 * @return An unmodifiable map of added items catagorized by metal material
	 */
	public static Map<MetalMaterial, List<Block>> getBlocksByMetal() {
		return Collections.unmodifiableMap(blocksByMetal);
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		cyano.basemetals.init.Materials.init();
		cyano.basemetals.init.ItemGroups.init();

		String materialName;
		if (Options.ENABLE_ADAMANTINE) {
			materialName = "adamantine";

			adamantine_block = createBlock(materialName);
			adamantine_plate = createPlate(materialName);
			adamantine_ore = createOre(materialName);
			adamantine_bars = createBars(materialName);
			adamantine_door = createDoor(materialName);
			adamantine_trapdoor = createTrapDoor(materialName);

			adamantine_button = createButton(materialName);
			adamantine_slab = createSlab(materialName);
			double_adamantine_slab = createDoubleSlab(materialName);
			adamantine_lever = createLever(materialName);
			adamantine_pressure_plate = createPressurePlate(materialName);
			adamantine_stairs = createStairs(materialName);
			adamantine_wall = createWall(materialName);
		}

		if (Options.ENABLE_ANTIMONY) {
			materialName = "antimony";

			antimony_block = createBlock(materialName);
			antimony_plate = createPlate(materialName);
			antimony_ore = createOre(materialName);
			antimony_bars = createBars(materialName);
			antimony_door = createDoor(materialName);
			antimony_trapdoor = createTrapDoor(materialName);

			antimony_button = createButton(materialName);
			antimony_slab = createSlab(materialName);
			double_antimony_slab = createDoubleSlab(materialName);
			antimony_lever = createLever(materialName);
			antimony_pressure_plate = createPressurePlate(materialName);
			antimony_stairs = createStairs(materialName);
			antimony_wall = createWall(materialName);
		}

		if (Options.ENABLE_AQUARIUM) {
			materialName = "aquarium";

			aquarium_block = createBlock(materialName);
			aquarium_plate = createPlate(materialName);
			aquarium_bars = createBars(materialName);
			aquarium_door = createDoor(materialName);
			aquarium_trapdoor = createTrapDoor(materialName);

			aquarium_button = createButton(materialName);
			aquarium_slab = createSlab(materialName);
			double_aquarium_slab = createDoubleSlab(materialName);
			aquarium_lever = createLever(materialName);
			aquarium_pressure_plate = createPressurePlate(materialName);
			aquarium_stairs = createStairs(materialName);
			aquarium_wall = createWall(materialName);
		}

		if (Options.ENABLE_BISMUTH) {
			materialName = "bismuth";

			bismuth_block = createBlock(materialName);
			bismuth_plate = createPlate(materialName);
			bismuth_ore = createOre(materialName);
			bismuth_bars = createBars(materialName);
			bismuth_door = createDoor(materialName);
			bismuth_trapdoor = createTrapDoor(materialName);

			bismuth_button = createButton(materialName);
			bismuth_slab = createSlab(materialName);
			double_bismuth_slab = createDoubleSlab(materialName);
			bismuth_lever = createLever(materialName);
			bismuth_pressure_plate = createPressurePlate(materialName);
			bismuth_stairs = createStairs(materialName);
			bismuth_wall = createWall(materialName);
		}

		if (Options.ENABLE_BRASS) {
			materialName = "brass";

			brass_block = createBlock(materialName);
			brass_plate = createPlate(materialName);
			brass_bars = createBars(materialName);
			brass_door = createDoor(materialName);
			brass_trapdoor = createTrapDoor(materialName);

			brass_button = createButton(materialName);
			brass_slab = createSlab(materialName);
			double_brass_slab = createDoubleSlab(materialName);
			brass_lever = createLever(materialName);
			brass_pressure_plate = createPressurePlate(materialName);
			brass_stairs = createStairs(materialName);
			brass_wall = createWall(materialName);
		}

		if (Options.ENABLE_BRONZE) {
			materialName = "bronze";

			bronze_block = createBlock(materialName);
			bronze_plate = createPlate(materialName);
			bronze_bars = createBars(materialName);
			bronze_door = createDoor(materialName);
			bronze_trapdoor = createTrapDoor(materialName);

			bronze_button = createButton(materialName);
			bronze_slab = createSlab(materialName);
			double_bronze_slab = createDoubleSlab(materialName);
			bronze_lever = createLever(materialName);
			bronze_pressure_plate = createPressurePlate(materialName);
			bronze_stairs = createStairs(materialName);
			bronze_wall = createWall(materialName);
		}

		if (Options.ENABLE_COLDIRON) {
			materialName = "coldiron";

			coldiron_block = createBlock(materialName);
			coldiron_plate = createPlate(materialName);
			coldiron_ore = createOre(materialName);
			coldiron_bars = createBars(materialName);
			coldiron_door = createDoor(materialName);
			coldiron_trapdoor = createTrapDoor(materialName);

			coldiron_button = createButton(materialName);
			coldiron_slab = createSlab(materialName);
			double_coldiron_slab = createDoubleSlab(materialName);
			coldiron_lever = createLever(materialName);
			coldiron_pressure_plate = createPressurePlate(materialName);
			coldiron_stairs = createStairs(materialName);
			coldiron_wall = createWall(materialName);
		}

		if (Options.ENABLE_COPPER) {
			materialName = "copper";

			copper_block = createBlock(materialName);
			copper_plate = createPlate(materialName);
			copper_ore = createOre(materialName);
			copper_bars = createBars(materialName);
			copper_door = createDoor(materialName);
			copper_trapdoor = createTrapDoor(materialName);

			copper_button = createButton(materialName);
			copper_slab = createSlab(materialName);
			double_copper_slab = createDoubleSlab(materialName);
			copper_lever = createLever(materialName);
			copper_pressure_plate = createPressurePlate(materialName);
			copper_stairs = createStairs(materialName);
			copper_wall = createWall(materialName);
		}

		if (Options.ENABLE_CUPRONICKEL) {

			materialName = "cupronickel";

			cupronickel_block = createBlock(materialName);
			cupronickel_plate = createPlate(materialName);
			cupronickel_bars = createBars(materialName);
			cupronickel_door = createDoor(materialName);
			cupronickel_trapdoor = createTrapDoor(materialName);

			cupronickel_button = createButton(materialName);
			cupronickel_slab = createSlab(materialName);
			double_cupronickel_slab = createDoubleSlab(materialName);
			cupronickel_lever = createLever(materialName);
			cupronickel_pressure_plate = createPressurePlate(materialName);
			cupronickel_stairs = createStairs(materialName);
			cupronickel_wall = createWall(materialName);
		}

		if (Options.ENABLE_ELECTRUM) {
			materialName = "electrum";

			electrum_block = createBlock(materialName);
			electrum_plate = createPlate(materialName);
			electrum_bars = createBars(materialName);
			electrum_door = createDoor(materialName);
			electrum_trapdoor = createTrapDoor(materialName);

			electrum_button = createButton(materialName);
			electrum_slab = createSlab(materialName);
			double_electrum_slab = createDoubleSlab(materialName);
			electrum_lever = createLever(materialName);
			electrum_pressure_plate = createPressurePlate(materialName);
			electrum_stairs = createStairs(materialName);
			electrum_wall = createWall(materialName);
		}

		if (Options.ENABLE_INVAR) {
			materialName = "invar";

			invar_block = createBlock(materialName);
			invar_plate = createPlate(materialName);
			invar_bars = createBars(materialName);
			invar_door = createDoor(materialName);
			invar_trapdoor = createTrapDoor(materialName);

			invar_button = createButton(materialName);
			invar_slab = createSlab(materialName);
			double_invar_slab = createDoubleSlab(materialName);
			invar_lever = createLever(materialName);
			invar_pressure_plate = createPressurePlate(materialName);
			invar_stairs = createStairs(materialName);
			invar_wall = createWall(materialName);
		}

		if (Options.ENABLE_LEAD) {
			materialName = "lead";

			lead_block = createBlock(materialName);
			lead_plate = createPlate(materialName);
			lead_ore = createOre(materialName);
			lead_bars = createBars(materialName);
			lead_door = createDoor(materialName);
			lead_trapdoor = createTrapDoor(materialName);

			lead_button = createButton(materialName);
			lead_slab = createSlab(materialName);
			double_lead_slab = createDoubleSlab(materialName);
			lead_lever = createLever(materialName);
			lead_pressure_plate = createPressurePlate(materialName);
			lead_stairs = createStairs(materialName);
			lead_wall = createWall(materialName);
		}

		if (Options.ENABLE_MERCURY) {
			mercury_ore = new BlockOre().setHardness(3.0f).setResistance(5.0f).setRegistryName(BaseMetals.MODID, "mercury_ore").setUnlocalizedName(BaseMetals.MODID + ".mercury_ore");
			GameRegistry.register(mercury_ore);
			final ItemBlock mercury_ore_item = new ItemBlock(mercury_ore);
			mercury_ore_item.setRegistryName(BaseMetals.MODID, "mercury_ore");
			GameRegistry.register(mercury_ore_item);
			blockRegistry.put("mercury_ore", mercury_ore);
			OreDictionary.registerOre("oreMercury", mercury_ore);
		}

		if (Options.ENABLE_MITHRIL) {
			materialName = "mithril";

			mithril_block = createBlock(materialName);
			mithril_plate = createPlate(materialName);
			mithril_bars = createBars(materialName);
			mithril_door = createDoor(materialName);
			mithril_trapdoor = createTrapDoor(materialName);

			mithril_button = createButton(materialName);
			mithril_slab = createSlab(materialName);
			double_mithril_slab = createDoubleSlab(materialName);
			mithril_lever = createLever(materialName);
			mithril_pressure_plate = createPressurePlate(materialName);
			mithril_stairs = createStairs(materialName);
			mithril_wall = createWall(materialName);
		}

		if (Options.ENABLE_NICKEL) {
			materialName = "nickel";

			nickel_block = createBlock(materialName);
			nickel_plate = createPlate(materialName);
			nickel_ore = createOre(materialName);
			nickel_bars = createBars(materialName);
			nickel_door = createDoor(materialName);
			nickel_trapdoor = createTrapDoor(materialName);

			nickel_button = createButton(materialName);
			nickel_slab = createSlab(materialName);
			double_nickel_slab = createDoubleSlab(materialName);
			nickel_lever = createLever(materialName);
			nickel_pressure_plate = createPressurePlate(materialName);
			nickel_stairs = createStairs(materialName);
			nickel_wall = createWall(materialName);
		}
		if (Options.ENABLE_PEWTER) {
			materialName = "pewter";

			pewter_block = createBlock(materialName);
			pewter_plate = createPlate(materialName);
			// pewter_ore = createOre(materialName);
			pewter_bars = createBars(materialName);
			pewter_door = createDoor(materialName);
			pewter_trapdoor = createTrapDoor(materialName);

			pewter_button = createButton(materialName);
			pewter_slab = createSlab(materialName);
			double_pewter_slab = createDoubleSlab(materialName);
			pewter_lever = createLever(materialName);
			pewter_pressure_plate = createPressurePlate(materialName);
			pewter_stairs = createStairs(materialName);
			pewter_wall = createWall(materialName);
		}

		if (Options.ENABLE_PLATINUM) {
			materialName = "platinum";

			platinum_block = createBlock(materialName);
			platinum_plate = createPlate(materialName);
			platinum_ore = createOre(materialName);
			platinum_bars = createBars(materialName);
			platinum_door = createDoor(materialName);
			platinum_trapdoor = createTrapDoor(materialName);

			platinum_button = createButton(materialName);
			platinum_slab = createSlab(materialName);
			double_platinum_slab = createDoubleSlab(materialName);
			platinum_lever = createLever(materialName);
			platinum_pressure_plate = createPressurePlate(materialName);
			platinum_stairs = createStairs(materialName);
			platinum_wall = createWall(materialName);
		}

		if (Options.ENABLE_SILVER) {
			materialName = "silver";

			silver_block = createBlock(materialName);
			silver_plate = createPlate(materialName);
			silver_ore = createOre(materialName);
			silver_bars = createBars(materialName);
			silver_door = createDoor(materialName);
			silver_trapdoor = createTrapDoor(materialName);

			silver_button = createButton(materialName);
			silver_slab = createSlab(materialName);
			double_silver_slab = createDoubleSlab(materialName);
			silver_lever = createLever(materialName);
			silver_pressure_plate = createPressurePlate(materialName);
			silver_stairs = createStairs(materialName);
			silver_wall = createWall(materialName);
		}

		if (Options.ENABLE_STARSTEEL) {
			materialName = "starsteel";

			starsteel_block = createBlock(materialName);
			starsteel_plate = createPlate(materialName);
			starsteel_ore = createOre(materialName);
			starsteel_bars = createBars(materialName);
			starsteel_door = createDoor(materialName);
			starsteel_trapdoor = createTrapDoor(materialName);
			starsteel_block.setLightLevel(0.5f);
			starsteel_plate.setLightLevel(0.5f);
			starsteel_ore.setLightLevel(0.5f);
			starsteel_bars.setLightLevel(0.5f);
			starsteel_door.setLightLevel(0.5f);
			starsteel_trapdoor.setLightLevel(0.5f);

			starsteel_button = createButton(materialName);
			starsteel_slab = createSlab(materialName);
			double_starsteel_slab = createDoubleSlab(materialName);
			starsteel_lever = createLever(materialName);
			starsteel_pressure_plate = createPressurePlate(materialName);
			starsteel_stairs = createStairs(materialName);
			starsteel_wall = createWall(materialName);
		}

		if (Options.ENABLE_STEEL) {
			materialName = "steel";

			steel_block = createBlock(materialName);
			steel_plate = createPlate(materialName);
			steel_bars = createBars(materialName);
			steel_door = createDoor(materialName);
			steel_trapdoor = createTrapDoor(materialName);

			steel_button = createButton(materialName);
			steel_slab = createSlab(materialName);
			double_steel_slab = createDoubleSlab(materialName);
			steel_lever = createLever(materialName);
			steel_pressure_plate = createPressurePlate(materialName);
			steel_stairs = createStairs(materialName);
			steel_wall = createWall(materialName);
		}
		if (Options.ENABLE_TIN) {
			materialName = "tin";

			tin_block = createBlock(materialName);
			tin_plate = createPlate(materialName);
			tin_ore = createOre(materialName);
			tin_bars = createBars(materialName);
			tin_door = createDoor(materialName);
			tin_trapdoor = createTrapDoor(materialName);

			tin_button = createButton(materialName);
			tin_slab = createSlab(materialName);
			double_tin_slab = createDoubleSlab(materialName);
			tin_lever = createLever(materialName);
			tin_pressure_plate = createPressurePlate(materialName);
			tin_stairs = createStairs(materialName);
			tin_wall = createWall(materialName);
		}

		if (Options.ENABLE_ZINC) {
			materialName = "zinc";

			zinc_block = createBlock(materialName);
			zinc_plate = createPlate(materialName);
			zinc_ore = createOre(materialName);
			zinc_bars = createBars(materialName);
			zinc_door = createDoor(materialName);
			zinc_trapdoor = createTrapDoor(materialName);

			zinc_button = createButton(materialName);
			zinc_slab = createSlab(materialName);
			double_zinc_slab = createDoubleSlab(materialName);
			zinc_lever = createLever(materialName);
			zinc_pressure_plate = createPressurePlate(materialName);
			zinc_stairs = createStairs(materialName);
			zinc_wall = createWall(materialName);
		}
		materialName = "iron";

		iron_plate = createPlate(materialName);

		materialName = "gold";

		gold_plate = createPlate(materialName);

		human_detector = addBlock(new BlockHumanDetector(), "human_detector", null, null);

		initDone = true;
	}

	private static Block addBlock(Block block, String name, MetalMaterial material, CreativeTabs tab) {

		String fullName;

		if ((block instanceof BlockDoubleMetalSlab) && (material != null)) {
			fullName = "double_" + material.getName() + "_" + name;
		}
		else if (block instanceof BlockDoubleMetalSlab) {
			fullName = "double_" + name;
		}
		else if (material != null) {
			fullName = material.getName() + "_" + name;
		}
		else {
			fullName = name;
		}

		// Loader.instance().activeModContainer().getModId()
		block.setRegistryName(fullName);
		block.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + fullName);
		GameRegistry.register(block);
		blockRegistry.put(fullName, block);

		if (!(block instanceof BlockMetalDoor) && !(block instanceof BlockMetalSlab)) {
			final ItemBlock itemBlock = new ItemBlock(block);
			itemBlock.setRegistryName(fullName);
			itemBlock.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + fullName);
			GameRegistry.register(itemBlock);
		}

		if (tab != null) {
			block.setCreativeTab(tab);
		}

		if (material != null) {
			blocksByMetal.computeIfAbsent(material, (MetalMaterial g) -> new ArrayList<>());
			blocksByMetal.get(material).add(block);
		}

		// TODO: Make this support multiple oredicts
		if (block instanceof IOreDictionaryEntry) {
			OreDictionary.registerOre(((IOreDictionaryEntry) block).getOreDictionaryName(), block);
		}

		return block;
	}

	private static Block createPlate(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Block b = addBlock(new BlockMetalPlate(material), "plate", material, ItemGroups.tab_blocks);
		material.plate = b;
		return b;
	}

	private static Block createBars(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Block b = addBlock(new BlockMetalBars(material), "bars", material, ItemGroups.tab_blocks);
		material.bars = b;
		return b;
	}

	private static Block createBlock(String materialName) {
		return createBlock(materialName, false);
	}

	private static Block createBlock(String materialName, boolean glow) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Block b = addBlock(new BlockMetalBlock(material, glow), "block", material, ItemGroups.tab_blocks);
		material.block = b;
		return b;
	}

	private static Block createButton(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Block b = addBlock(new BlockButtonMetal(material), "button", material, ItemGroups.tab_blocks);
		material.button = b;
		return b;
	}

	private static Block createLever(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Block b = addBlock(new BlockMetalLever(material), "lever", material, ItemGroups.tab_blocks);
		material.lever = b;
		return b;
	}

	private static Block createPressurePlate(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Block b = addBlock(new BlockMetalPressurePlate(material), "pressure_plate", material, ItemGroups.tab_blocks);
		material.pressure_plate = b;
		return b;
	}

	private static BlockSlab createSlab(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final BlockSlab b = (BlockSlab) addBlock(new BlockHalfMetalSlab(material), "slab", material, ItemGroups.tab_blocks);
		material.half_slab = b;
		return b;
	}

	private static BlockSlab createDoubleSlab(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final BlockSlab b = (BlockSlab) addBlock(new BlockDoubleMetalSlab(material), "slab", material, ItemGroups.tab_blocks);
		material.double_slab = b;
		return b;
	}

	private static Block createStairs(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Block b = addBlock(new BlockMetalStairs(material), "stairs", material, ItemGroups.tab_blocks);
		material.stairs = b;
		return b;
	}

	private static Block createWall(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Block b = addBlock(new BlockMetalWall(material), "wall", material, ItemGroups.tab_blocks);
		material.wall = b;
		return b;
	}

	private static Block createOre(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Block b = addBlock(new BlockMetalOre(material), "ore", material, ItemGroups.tab_blocks);
		material.ore = b;
		return b;
	}

	private static BlockDoor createDoor(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final BlockDoor b = (BlockDoor) addBlock(new BlockMetalDoor(material), "door", material, null);
		material.doorBlock = b;
		return b;
	}

	private static Block createTrapDoor(String materialName) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		final Block b = addBlock(new BlockMetalTrapDoor(material), "trapdoor", material, ItemGroups.tab_blocks);
		material.trapdoor = b;
		return b;
	}
}
