package cyano.basemetals.init;

import java.util.HashMap;
import java.util.Map;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.blocks.*;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockSlab;
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

	protected static Block adamantine_button;
	protected static BlockSlab adamantine_slab;
	protected static BlockSlab double_adamantine_slab;
	protected static Block adamantine_lever;
	protected static Block adamantine_pressure_plate;
	protected static Block adamantine_stairs;
	protected static Block adamantine_wall;

	public static Block antimony_bars;
	public static Block antimony_block;
	public static Block antimony_plate;
	public static BlockDoor antimony_door;
	public static Block antimony_ore;
	public static Block antimony_trapdoor;

	protected static Block antimony_button;
	protected static BlockSlab antimony_slab;
	protected static BlockSlab double_antimony_slab;
	protected static Block antimony_lever;
	protected static Block antimony_pressure_plate;
	protected static Block antimony_stairs;
	protected static Block antimony_wall;

	public static Block aquarium_bars;
	public static Block aquarium_block;
	public static Block aquarium_plate;
	public static BlockDoor aquarium_door;
	public static Block aquarium_trapdoor;

	protected static Block aquarium_button;
	protected static BlockSlab aquarium_slab;
	protected static BlockSlab double_aquarium_slab;
	protected static Block aquarium_lever;
	protected static Block aquarium_pressure_plate;
	protected static Block aquarium_stairs;
	protected static Block aquarium_wall;

	public static Block bismuth_bars;
	public static Block bismuth_block;
	public static Block bismuth_plate;
	public static BlockDoor bismuth_door;
	public static Block bismuth_ore;
	public static Block bismuth_trapdoor;

	protected static Block bismuth_button;
	protected static BlockSlab bismuth_slab;
	protected static BlockSlab double_bismuth_slab;
	protected static Block bismuth_lever;
	protected static Block bismuth_pressure_plate;
	protected static Block bismuth_stairs;
	protected static Block bismuth_wall;

	public static Block brass_bars;
	public static Block brass_block;
	public static Block brass_plate;
	public static BlockDoor brass_door;
	public static Block brass_trapdoor;

	protected static Block brass_button;
	protected static BlockSlab brass_slab;
	protected static BlockSlab double_brass_slab;
	protected static Block brass_lever;
	protected static Block brass_pressure_plate;
	protected static Block brass_stairs;
	protected static Block brass_wall;

	public static Block bronze_bars;
	public static Block bronze_block;
	public static Block bronze_plate;
	public static BlockDoor bronze_door;
	public static Block bronze_trapdoor;

	protected static Block bronze_button;
	protected static BlockSlab bronze_slab;
	protected static BlockSlab double_bronze_slab;
	protected static Block bronze_lever;
	protected static Block bronze_pressure_plate;
	protected static Block bronze_stairs;
	protected static Block bronze_wall;

	public static Block coldiron_bars;
	public static Block coldiron_block;
	public static Block coldiron_plate;
	public static BlockDoor coldiron_door;
	public static Block coldiron_ore;
	public static Block coldiron_trapdoor;

	protected static Block coldiron_button;
	protected static BlockSlab coldiron_slab;
	protected static BlockSlab double_coldiron_slab;
	protected static Block coldiron_lever;
	protected static Block coldiron_pressure_plate;
	protected static Block coldiron_stairs;
	protected static Block coldiron_wall;

	public static Block copper_bars;
	public static Block copper_block;
	public static Block copper_plate;
	public static BlockDoor copper_door;
	public static Block copper_ore;
	public static Block copper_trapdoor;

	protected static Block copper_button;
	protected static BlockSlab copper_slab;
	protected static BlockSlab double_copper_slab;
	protected static Block copper_lever;
	protected static Block copper_pressure_plate;
	protected static Block copper_stairs;
	protected static Block copper_wall;

	public static Block cupronickel_bars;
	public static Block cupronickel_block;
	public static Block cupronickel_plate;
	public static BlockDoor cupronickel_door;
	public static Block cupronickel_trapdoor;

	protected static Block cupronickel_button;
	protected static BlockSlab cupronickel_slab;
	protected static BlockSlab double_cupronickel_slab;
	protected static Block cupronickel_lever;
	protected static Block cupronickel_pressure_plate;
	protected static Block cupronickel_stairs;
	protected static Block cupronickel_wall;

	public static Block electrum_bars;
	public static Block electrum_block;
	public static Block electrum_plate;
	public static BlockDoor electrum_door;
	public static Block electrum_trapdoor;

	protected static Block electrum_button;
	protected static BlockSlab electrum_slab;
	protected static BlockSlab double_electrum_slab;
	protected static Block electrum_lever;
	protected static Block electrum_pressure_plate;
	protected static Block electrum_stairs;
	protected static Block electrum_wall;

	public static Block invar_bars;
	public static Block invar_block;
	public static Block invar_plate;
	public static BlockDoor invar_door;
	public static Block invar_trapdoor;

	protected static Block invar_button;
	protected static BlockSlab invar_slab;
	protected static BlockSlab double_invar_slab;
	protected static Block invar_lever;
	protected static Block invar_pressure_plate;
	protected static Block invar_stairs;
	protected static Block invar_wall;

	public static Block lead_bars;
	public static Block lead_block;
	public static Block lead_plate;
	public static BlockDoor lead_door;
	public static Block lead_ore;
	public static Block lead_trapdoor;

	protected static Block lead_button;
	protected static BlockSlab lead_slab;
	protected static BlockSlab double_lead_slab;
	protected static Block lead_lever;
	protected static Block lead_pressure_plate;
	protected static Block lead_stairs;
	protected static Block lead_wall;

	public static Block mercury_ore;

	public static Block mithril_bars;
	public static Block mithril_block;
	public static Block mithril_plate;
	public static BlockDoor mithril_door;
	public static Block mithril_trapdoor;

	protected static Block mithril_button;
	protected static BlockSlab mithril_slab;
	protected static BlockSlab double_mithril_slab;
	protected static Block mithril_lever;
	protected static Block mithril_pressure_plate;
	protected static Block mithril_stairs;
	protected static Block mithril_wall;

	public static Block nickel_bars;
	public static Block nickel_block;
	public static Block nickel_plate;
	public static BlockDoor nickel_door;
	public static Block nickel_ore;
	public static Block nickel_trapdoor;

	protected static Block nickel_button;
	protected static BlockSlab nickel_slab;
	protected static BlockSlab double_nickel_slab;
	protected static Block nickel_lever;
	protected static Block nickel_pressure_plate;
	protected static Block nickel_stairs;
	protected static Block nickel_wall;

	public static Block pewter_bars;
	public static Block pewter_block;
	public static Block pewter_plate;
	public static BlockDoor pewter_door;
	// public static Block pewter_ore;
	public static Block pewter_trapdoor;

	protected static Block pewter_button;
	protected static BlockSlab pewter_slab;
	protected static BlockSlab double_pewter_slab;
	protected static Block pewter_lever;
	protected static Block pewter_pressure_plate;
	protected static Block pewter_stairs;
	protected static Block pewter_wall;

	public static Block platinum_bars;
	public static Block platinum_block;
	public static Block platinum_plate;
	public static BlockDoor platinum_door;
	public static Block platinum_ore;
	public static Block platinum_trapdoor;

	protected static Block platinum_button;
	protected static BlockSlab platinum_slab;
	protected static BlockSlab double_platinum_slab;
	protected static Block platinum_lever;
	protected static Block platinum_pressure_plate;
	protected static Block platinum_stairs;
	protected static Block platinum_wall;

	public static Block silver_bars;
	public static Block silver_block;
	public static Block silver_plate;
	public static BlockDoor silver_door;
	public static Block silver_ore;
	public static Block silver_trapdoor;

	protected static Block silver_button;
	protected static BlockSlab silver_slab;
	protected static BlockSlab double_silver_slab;
	protected static Block silver_lever;
	protected static Block silver_pressure_plate;
	protected static Block silver_stairs;
	protected static Block silver_wall;

	public static Block starsteel_bars;
	public static Block starsteel_block;
	public static Block starsteel_plate;
	public static BlockDoor starsteel_door;
	public static Block starsteel_ore;
	public static Block starsteel_trapdoor;

	protected static Block starsteel_button;
	protected static BlockSlab starsteel_slab;
	protected static BlockSlab double_starsteel_slab;
	protected static Block starsteel_lever;
	protected static Block starsteel_pressure_plate;
	protected static Block starsteel_stairs;
	protected static Block starsteel_wall;

	public static Block steel_bars;
	public static Block steel_block;
	public static Block steel_plate;
	public static BlockDoor steel_door;
	public static Block steel_trapdoor;

	protected static Block steel_button;
	protected static BlockSlab steel_slab;
	protected static BlockSlab double_steel_slab;
	protected static Block steel_lever;
	protected static Block steel_pressure_plate;
	protected static Block steel_stairs;
	protected static Block steel_wall;

	public static Block tin_bars;
	public static Block tin_block;
	public static Block tin_plate;
	public static BlockDoor tin_door;
	public static Block tin_ore;
	public static Block tin_trapdoor;

	protected static Block tin_button;
	protected static BlockSlab tin_slab;
	protected static BlockSlab double_tin_slab;
	protected static Block tin_lever;
	protected static Block tin_pressure_plate;
	protected static Block tin_stairs;
	protected static Block tin_wall;

	public static Block zinc_bars;
	public static Block zinc_block;
	public static Block zinc_plate;
	public static BlockDoor zinc_door;
	public static Block zinc_ore;
	public static Block zinc_trapdoor;

	protected static Block zinc_button;
	protected static BlockSlab zinc_slab;
	protected static BlockSlab double_zinc_slab;
	protected static Block zinc_lever;
	protected static Block zinc_pressure_plate;
	protected static Block zinc_stairs;
	protected static Block zinc_wall;

	public static Block iron_plate;

	public static Block gold_bars;
	public static Block gold_plate;
	public static BlockDoor gold_door;
	public static Block gold_trapdoor;

	public static Block human_detector;

	private static boolean initDone = false;

	private static Map<Block, String> allBlocks = new HashMap<>();
	private static final Map<String, Block> blockRegistry = new HashMap<>();
	// private static Map<MetalMaterial, List<Block>> blocksByMetal = new HashMap<>();

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
	public static String getNameOfItem(Block b) {
		return allBlocks.get(b);
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone)
			return;

		cyano.basemetals.init.Materials.init();
		cyano.basemetals.init.ItemGroups.init();

		adamantine_block = createBlock(Materials.adamantine);
		adamantine_plate = createPlate(Materials.adamantine);
		adamantine_ore = createOre(Materials.adamantine);
		adamantine_bars = createBars(Materials.adamantine);
		adamantine_door = createDoor(Materials.adamantine);
		adamantine_trapdoor = createTrapDoor(Materials.adamantine);

		adamantine_button = createButton(Materials.adamantine);
		adamantine_slab = createSlab(Materials.adamantine);
		double_adamantine_slab = createDoubleSlab(Materials.adamantine);
		adamantine_lever = createLever(Materials.adamantine);
		adamantine_pressure_plate = createPressurePlate(Materials.adamantine);
		adamantine_stairs = createStairs(Materials.adamantine);
		adamantine_wall = createWall(Materials.adamantine);

		antimony_block = createBlock(Materials.antimony);
		antimony_plate = createPlate(Materials.antimony);
		antimony_ore = createOre(Materials.antimony);
		antimony_bars = createBars(Materials.antimony);
		antimony_door = createDoor(Materials.antimony);
		antimony_trapdoor = createTrapDoor(Materials.antimony);

		antimony_button = createButton(Materials.antimony);
		antimony_slab = createSlab(Materials.antimony);
		double_antimony_slab = createDoubleSlab(Materials.antimony);
		antimony_lever = createLever(Materials.antimony);
		antimony_pressure_plate = createPressurePlate(Materials.antimony);
		antimony_stairs = createStairs(Materials.antimony);
		antimony_wall = createWall(Materials.antimony);

		aquarium_block = createBlock(Materials.aquarium);
		aquarium_plate = createPlate(Materials.aquarium);
		aquarium_bars = createBars(Materials.aquarium);
		aquarium_door = createDoor(Materials.aquarium);
		aquarium_trapdoor = createTrapDoor(Materials.aquarium);

		aquarium_button = createButton(Materials.aquarium);
		aquarium_slab = createSlab(Materials.aquarium);
		double_aquarium_slab = createDoubleSlab(Materials.aquarium);
		aquarium_lever = createLever(Materials.aquarium);
		aquarium_pressure_plate = createPressurePlate(Materials.aquarium);
		aquarium_stairs = createStairs(Materials.aquarium);
		aquarium_wall = createWall(Materials.aquarium);

		bismuth_block = createBlock(Materials.bismuth);
		bismuth_plate = createPlate(Materials.bismuth);
		bismuth_ore = createOre(Materials.bismuth);
		bismuth_bars = createBars(Materials.bismuth);
		bismuth_door = createDoor(Materials.bismuth);
		bismuth_trapdoor = createTrapDoor(Materials.bismuth);

		bismuth_button = createButton(Materials.bismuth);
		bismuth_slab = createSlab(Materials.bismuth);
		double_bismuth_slab = createDoubleSlab(Materials.bismuth);
		bismuth_lever = createLever(Materials.bismuth);
		bismuth_pressure_plate = createPressurePlate(Materials.bismuth);
		bismuth_stairs = createStairs(Materials.bismuth);
		bismuth_wall = createWall(Materials.bismuth);

		brass_block = createBlock(Materials.brass);
		brass_plate = createPlate(Materials.brass);
		brass_bars = createBars(Materials.brass);
		brass_door = createDoor(Materials.brass);
		brass_trapdoor = createTrapDoor(Materials.brass);

		brass_button = createButton(Materials.brass);
		brass_slab = createSlab(Materials.brass);
		double_brass_slab = createDoubleSlab(Materials.brass);
		brass_lever = createLever(Materials.brass);
		brass_pressure_plate = createPressurePlate(Materials.brass);
		brass_stairs = createStairs(Materials.brass);
		brass_wall = createWall(Materials.brass);

		bronze_block = createBlock(Materials.bronze);
		bronze_plate = createPlate(Materials.bronze);
		bronze_bars = createBars(Materials.bronze);
		bronze_door = createDoor(Materials.bronze);
		bronze_trapdoor = createTrapDoor(Materials.bronze);

		bronze_button = createButton(Materials.bronze);
		bronze_slab = createSlab(Materials.bronze);
		double_bronze_slab = createDoubleSlab(Materials.bronze);
		bronze_lever = createLever(Materials.bronze);
		bronze_pressure_plate = createPressurePlate(Materials.bronze);
		bronze_stairs = createStairs(Materials.bronze);
		bronze_wall = createWall(Materials.bronze);

		coldiron_block = createBlock(Materials.coldiron);
		coldiron_plate = createPlate(Materials.coldiron);
		coldiron_ore = createOre(Materials.coldiron);
		coldiron_bars = createBars(Materials.coldiron);
		coldiron_door = createDoor(Materials.coldiron);
		coldiron_trapdoor = createTrapDoor(Materials.coldiron);

		coldiron_button = createButton(Materials.coldiron);
		coldiron_slab = createSlab(Materials.coldiron);
		double_coldiron_slab = createDoubleSlab(Materials.coldiron);
		coldiron_lever = createLever(Materials.coldiron);
		coldiron_pressure_plate = createPressurePlate(Materials.coldiron);
		coldiron_stairs = createStairs(Materials.coldiron);
		coldiron_wall = createWall(Materials.coldiron);

		copper_block = createBlock(Materials.copper);
		copper_plate = createPlate(Materials.copper);
		copper_ore = createOre(Materials.copper);
		copper_bars = createBars(Materials.copper);
		copper_door = createDoor(Materials.copper);
		copper_trapdoor = createTrapDoor(Materials.copper);

		copper_button = createButton(Materials.copper);
		copper_slab = createSlab(Materials.copper);
		double_copper_slab = createDoubleSlab(Materials.copper);
		copper_lever = createLever(Materials.copper);
		copper_pressure_plate = createPressurePlate(Materials.copper);
		copper_stairs = createStairs(Materials.copper);
		copper_wall = createWall(Materials.copper);

		cupronickel_block = createBlock(Materials.cupronickel);
		cupronickel_plate = createPlate(Materials.cupronickel);
		cupronickel_bars = createBars(Materials.cupronickel);
		cupronickel_door = createDoor(Materials.cupronickel);
		cupronickel_trapdoor = createTrapDoor(Materials.cupronickel);

		cupronickel_button = createButton(Materials.cupronickel);
		cupronickel_slab = createSlab(Materials.cupronickel);
		double_cupronickel_slab = createDoubleSlab(Materials.cupronickel);
		cupronickel_lever = createLever(Materials.cupronickel);
		cupronickel_pressure_plate = createPressurePlate(Materials.cupronickel);
		cupronickel_stairs = createStairs(Materials.cupronickel);
		cupronickel_wall = createWall(Materials.cupronickel);

		electrum_block = createBlock(Materials.electrum);
		electrum_plate = createPlate(Materials.electrum);
		electrum_bars = createBars(Materials.electrum);
		electrum_door = createDoor(Materials.electrum);
		electrum_trapdoor = createTrapDoor(Materials.electrum);

		electrum_button = createButton(Materials.electrum);
		electrum_slab = createSlab(Materials.electrum);
		double_electrum_slab = createDoubleSlab(Materials.electrum);
		electrum_lever = createLever(Materials.electrum);
		electrum_pressure_plate = createPressurePlate(Materials.electrum);
		electrum_stairs = createStairs(Materials.electrum);
		electrum_wall = createWall(Materials.electrum);

		invar_block = createBlock(Materials.invar);
		invar_plate = createPlate(Materials.invar);
		invar_bars = createBars(Materials.invar);
		invar_door = createDoor(Materials.invar);
		invar_trapdoor = createTrapDoor(Materials.invar);

		invar_button = createButton(Materials.invar);
		invar_slab = createSlab(Materials.invar);
		double_invar_slab = createDoubleSlab(Materials.invar);
		invar_lever = createLever(Materials.invar);
		invar_pressure_plate = createPressurePlate(Materials.invar);
		invar_stairs = createStairs(Materials.invar);
		invar_wall = createWall(Materials.invar);

		lead_block = createBlock(Materials.lead);
		lead_plate = createPlate(Materials.lead);
		lead_ore = createOre(Materials.lead);
		lead_bars = createBars(Materials.lead);
		lead_door = createDoor(Materials.lead);
		lead_trapdoor = createTrapDoor(Materials.lead);

		lead_button = createButton(Materials.lead);
		lead_slab = createSlab(Materials.lead);
		double_lead_slab = createDoubleSlab(Materials.lead);
		lead_lever = createLever(Materials.lead);
		lead_pressure_plate = createPressurePlate(Materials.lead);
		lead_stairs = createStairs(Materials.lead);
		lead_wall = createWall(Materials.lead);

		mercury_ore = new BlockOre().setHardness(3.0f).setResistance(5.0f).setRegistryName(BaseMetals.MODID, "mercury_ore").setUnlocalizedName(BaseMetals.MODID + ".mercury_ore");
		GameRegistry.register(mercury_ore);
		final ItemBlock mercury_ore_item = new ItemBlock(mercury_ore);
		mercury_ore_item.setRegistryName(BaseMetals.MODID, "mercury_ore");
		GameRegistry.register(mercury_ore_item);
		blockRegistry.put("mercury_ore", mercury_ore);
		OreDictionary.registerOre("oreMercury", mercury_ore);

		mithril_block = createBlock(Materials.mithril);
		mithril_plate = createPlate(Materials.mithril);
		mithril_bars = createBars(Materials.mithril);
		mithril_door = createDoor(Materials.mithril);
		mithril_trapdoor = createTrapDoor(Materials.mithril);

		mithril_button = createButton(Materials.mithril);
		mithril_slab = createSlab(Materials.mithril);
		double_mithril_slab = createDoubleSlab(Materials.mithril);
		mithril_lever = createLever(Materials.mithril);
		mithril_pressure_plate = createPressurePlate(Materials.mithril);
		mithril_stairs = createStairs(Materials.mithril);
		mithril_wall = createWall(Materials.mithril);

		nickel_block = createBlock(Materials.nickel);
		nickel_plate = createPlate(Materials.nickel);
		nickel_ore = createOre(Materials.nickel);
		nickel_bars = createBars(Materials.nickel);
		nickel_door = createDoor(Materials.nickel);
		nickel_trapdoor = createTrapDoor(Materials.nickel);

		nickel_button = createButton(Materials.nickel);
		nickel_slab = createSlab(Materials.nickel);
		double_nickel_slab = createDoubleSlab(Materials.nickel);
		nickel_lever = createLever(Materials.nickel);
		nickel_pressure_plate = createPressurePlate(Materials.nickel);
		nickel_stairs = createStairs(Materials.nickel);
		nickel_wall = createWall(Materials.nickel);

		pewter_block = createBlock(Materials.pewter);
		pewter_plate = createPlate(Materials.pewter);
		// pewter_ore = createOre(Materials.pewter);
		pewter_bars = createBars(Materials.pewter);
		pewter_door = createDoor(Materials.pewter);
		pewter_trapdoor = createTrapDoor(Materials.pewter);

		pewter_button = createButton(Materials.pewter);
		pewter_slab = createSlab(Materials.pewter);
		double_pewter_slab = createDoubleSlab(Materials.pewter);
		pewter_lever = createLever(Materials.pewter);
		pewter_pressure_plate = createPressurePlate(Materials.pewter);
		pewter_stairs = createStairs(Materials.pewter);
		pewter_wall = createWall(Materials.pewter);

		platinum_block = createBlock(Materials.platinum);
		platinum_plate = createPlate(Materials.platinum);
		platinum_ore = createOre(Materials.platinum);
		platinum_bars = createBars(Materials.platinum);
		platinum_door = createDoor(Materials.platinum);
		platinum_trapdoor = createTrapDoor(Materials.platinum);

		platinum_button = createButton(Materials.platinum);
		platinum_slab = createSlab(Materials.platinum);
		double_platinum_slab = createDoubleSlab(Materials.platinum);
		platinum_lever = createLever(Materials.platinum);
		platinum_pressure_plate = createPressurePlate(Materials.platinum);
		platinum_stairs = createStairs(Materials.platinum);
		platinum_wall = createWall(Materials.platinum);

		silver_block = createBlock(Materials.silver);
		silver_plate = createPlate(Materials.silver);
		silver_ore = createOre(Materials.silver);
		silver_bars = createBars(Materials.silver);
		silver_door = createDoor(Materials.silver);
		silver_trapdoor = createTrapDoor(Materials.silver);

		silver_button = createButton(Materials.silver);
		silver_slab = createSlab(Materials.silver);
		double_silver_slab = createDoubleSlab(Materials.silver);
		silver_lever = createLever(Materials.silver);
		silver_pressure_plate = createPressurePlate(Materials.silver);
		silver_stairs = createStairs(Materials.silver);
		silver_wall = createWall(Materials.silver);

		starsteel_block = createBlock(Materials.starsteel);
		starsteel_plate = createPlate(Materials.starsteel);
		starsteel_ore = createOre(Materials.starsteel);
		starsteel_bars = createBars(Materials.starsteel);
		starsteel_door = createDoor(Materials.starsteel);
		starsteel_trapdoor = createTrapDoor(Materials.starsteel);
		starsteel_block.setLightLevel(0.5f);
		starsteel_plate.setLightLevel(0.5f);
		starsteel_ore.setLightLevel(0.5f);
		starsteel_bars.setLightLevel(0.5f);
		starsteel_door.setLightLevel(0.5f);
		starsteel_trapdoor.setLightLevel(0.5f);

		starsteel_button = createButton(Materials.starsteel);
		starsteel_slab = createSlab(Materials.starsteel);
		double_starsteel_slab = createDoubleSlab(Materials.starsteel);
		starsteel_lever = createLever(Materials.starsteel);
		starsteel_pressure_plate = createPressurePlate(Materials.starsteel);
		starsteel_stairs = createStairs(Materials.starsteel);
		starsteel_wall = createWall(Materials.starsteel);

		steel_block = createBlock(Materials.steel);
		steel_plate = createPlate(Materials.steel);
		steel_bars = createBars(Materials.steel);
		steel_door = createDoor(Materials.steel);
		steel_trapdoor = createTrapDoor(Materials.steel);

		steel_button = createButton(Materials.steel);
		steel_slab = createSlab(Materials.steel);
		double_steel_slab = createDoubleSlab(Materials.steel);
		steel_lever = createLever(Materials.steel);
		steel_pressure_plate = createPressurePlate(Materials.steel);
		steel_stairs = createStairs(Materials.steel);
		steel_wall = createWall(Materials.steel);

		tin_block = createBlock(Materials.tin);
		tin_plate = createPlate(Materials.tin);
		tin_ore = createOre(Materials.tin);
		tin_bars = createBars(Materials.tin);
		tin_door = createDoor(Materials.tin);
		tin_trapdoor = createTrapDoor(Materials.tin);

		tin_button = createButton(Materials.tin);
		tin_slab = createSlab(Materials.tin);
		double_tin_slab = createDoubleSlab(Materials.tin);
		tin_lever = createLever(Materials.tin);
		tin_pressure_plate = createPressurePlate(Materials.tin);
		tin_stairs = createStairs(Materials.tin);
		tin_wall = createWall(Materials.tin);

		zinc_block = createBlock(Materials.zinc);
		zinc_plate = createPlate(Materials.zinc);
		zinc_ore = createOre(Materials.zinc);
		zinc_bars = createBars(Materials.zinc);
		zinc_door = createDoor(Materials.zinc);
		zinc_trapdoor = createTrapDoor(Materials.zinc);

		zinc_button = createButton(Materials.zinc);
		zinc_slab = createSlab(Materials.zinc);
		double_zinc_slab = createDoubleSlab(Materials.zinc);
		zinc_lever = createLever(Materials.zinc);
		zinc_pressure_plate = createPressurePlate(Materials.zinc);
		zinc_stairs = createStairs(Materials.zinc);
		zinc_wall = createWall(Materials.zinc);

		iron_plate = createPlate(Materials.vanilla_iron);
		gold_plate = createPlate(Materials.vanilla_gold);

		human_detector = addBlock(new BlockHumanDetector(), "human_detector", null);

		initDone = true;
	}

	private static Block addBlock(Block block, String name, MetalMaterial metal) {
		String fullName = null;

		if ((block instanceof BlockDoubleMetalSlab) && (metal != null))
			fullName = "double_" + metal.getName() + "_" + name;
		else if (block instanceof BlockDoubleMetalSlab)
			fullName = "double_" + name;
		else if (metal != null)
			fullName = metal.getName() + "_" + name;
		else
			fullName = name;

		block.setRegistryName(fullName);
		block.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + fullName);
		GameRegistry.register(block);
		blockRegistry.put(fullName, block);
		allBlocks.put(block, fullName);

		if (!(block instanceof BlockMetalDoor) && !(block instanceof BlockMetalSlab)) {
			final ItemBlock itemBlock = new ItemBlock(block);
			itemBlock.setRegistryName(fullName);
			itemBlock.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + fullName);
			GameRegistry.register(itemBlock);
		}

		if (!(block instanceof BlockMetalDoor))
			block.setCreativeTab(ItemGroups.tab_blocks);

		// TODO: Make this support multiple oredicts
		if (block instanceof IOreDictionaryEntry)
			OreDictionary.registerOre(((IOreDictionaryEntry) block).getOreDictionaryName(), block);

		return block;
	}

	private static Block createPlate(MetalMaterial metal) {
		final Block b = addBlock(new BlockMetalPlate(metal), "plate", metal);
		metal.plate = b;
		return b;
	}

	private static Block createBars(MetalMaterial metal) {
		final Block b = addBlock(new BlockMetalBars(metal), "bars", metal);
		metal.bars = b;
		return b;
	}

	private static Block createBlock(MetalMaterial metal) {
		return createBlock(metal, false);
	}

	private static Block createBlock(MetalMaterial metal, boolean glow) {
		final Block b = addBlock(new BlockMetalBlock(metal, glow), "block", metal);
		metal.block = b;
		return b;
	}

	private static Block createButton(MetalMaterial metal) {
		final Block b = addBlock(new BlockButtonMetal(metal), "button", metal);
		metal.button = b;
		return b;
	}

	private static Block createLever(MetalMaterial metal) {
		final Block b = addBlock(new BlockMetalLever(metal), "lever", metal);
		metal.lever = b;
		return b;
	}

	private static Block createPressurePlate(MetalMaterial metal) {
		final Block b = addBlock(new BlockMetalPressurePlate(metal), "pressure_plate", metal);
		metal.pressure_plate = b;
		return b;
	}

	private static BlockSlab createSlab(MetalMaterial metal) {
		final BlockSlab b = (BlockSlab) addBlock(new BlockHalfMetalSlab(metal), "slab", metal);
		metal.half_slab = b;
		return b;
	}

	private static BlockSlab createDoubleSlab(MetalMaterial metal) {
		final BlockSlab b = (BlockSlab) addBlock(new BlockDoubleMetalSlab(metal), "slab", metal);
		metal.double_slab = b;
		return b;
	}

	private static Block createStairs(MetalMaterial metal) {
		final Block b = addBlock(new BlockMetalStairs(metal, Blocks.getBlockByName(metal.getName() + "_block")), "stairs", metal);
		metal.stairs = b;
		return b;
	}

	private static Block createWall(MetalMaterial metal) {
		final Block b = addBlock(new BlockMetalWall(metal, Blocks.getBlockByName(metal.getName() + "_block")), "wall", metal);
		metal.wall = b;
		return b;
	}

	private static Block createOre(MetalMaterial metal) {
		final Block b = addBlock(new BlockMetalOre(metal), "ore", metal);
		metal.ore = b;
		return b;
	}

	private static BlockDoor createDoor(MetalMaterial metal) {
		final BlockDoor b = (BlockDoor) addBlock(new BlockMetalDoor(metal), "door", metal);
		metal.doorBlock = b;
		return b;
	}

	private static Block createTrapDoor(MetalMaterial metal) {
		final Block b = addBlock(new BlockMetalTrapDoor(metal), "trapdoor", metal);
		metal.trapdoor = b;
		return b;
	}

	public static Map<String, Block> getBlockRegistry() {
		return blockRegistry;
	}
}
