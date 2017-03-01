package com.mcmoddev.lib.init;

import java.util.*;

import com.google.common.collect.*;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.block.*;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This class initializes all blocks in Base Metals and provides some utility
 * methods for looking up blocks.
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Blocks {

	private static boolean initDone = false;

	private static BiMap<String, Block> blockRegistry = HashBiMap.create(16);
	private static BiMap<String, BlockFluidBase> fluidBlockRegistry = HashBiMap.create(16);
	private static Map<MetalMaterial, List<Block>> blocksByMaterial = new HashMap<>();

	protected Blocks() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Materials.init();
		ItemGroups.init();

		initDone = true;
	}

	/**
	 * 
	 * @param material The material of interest
	 */
	protected static void createBlocksBasic(MetalMaterial material) {
		createBlock(material); // Not Gold, Not Iron, Not Diamond, Not Stone
		createPlate(material);
		createOre(material); // Not Gold, Not Iron, Not Diamond, Not Stone
		createBars(material); // Not Iron
		createDoor(material); // Not Iron
		createTrapDoor(material); // Not Iron
	}

	/**
	 * 
	 * @param material The material of interest
	 */
	protected static void createBlocksAdditional(MetalMaterial material) {
		createButton(material);
		createSlab(material);
		createDoubleSlab(material);
		createLever(material);
		createPressurePlate(material); // Not Iron, Not Gold
		createStairs(material);
		createWall(material);
	}

	/**
	 * 
	 * @param material The material of interest
	 */
	protected static void createBlocksFull(MetalMaterial material) {
		createBlock(material);
		createPlate(material);
		createOre(material);
		createBars(material);
		createDoor(material);
		createTrapDoor(material);

		createButton(material);
		createSlab(material);
		createDoubleSlab(material);
		createLever(material);
		createPressurePlate(material);
		createStairs(material);
		createWall(material);
	}

	/**
	 * 
	 * @param block the block of interest
	 * @param name name of the thing
	 * @param material the material the thing is made from
	 * @param tab which creative tab is it on
	 * @return a new block
	 */
	protected static Block addBlock(Block block, String name, MetalMaterial material, CreativeTabs tab) {

		String fullName;

		if ((block == null) || (name == null)) {
			return null;
		}

		if ((block instanceof BlockDoubleMetalSlab) && (material != null)) {
			fullName = "double_" + material.getName() + "_" + name;
		} else if (block instanceof BlockDoubleMetalSlab) {
			fullName = "double_" + name;
		} else if (material != null) {
			if ((name == "nether") || (name == "end")) {
				fullName = name + "_" + material.getName() + "_" + "ore";
			} else {
				fullName = material.getName() + "_" + name;
			}
		} else {
			fullName = name;
		}

		block.setRegistryName(fullName);
		block.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + fullName);
		GameRegistry.register(block);
		if (block instanceof BlockFluidBase) {
			fluidBlockRegistry.put(fullName, (BlockFluidBase) block);
		} else {
			blockRegistry.put(fullName, block);
		}

		if (!(block instanceof BlockDoor) && !(block instanceof BlockSlab)) {
			final ItemBlock itemBlock = new ItemBlock(block);
			itemBlock.setRegistryName(fullName);
			itemBlock.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + fullName);
			GameRegistry.register(itemBlock);
		}

		if (tab != null) {
			block.setCreativeTab(tab);
		}

		if (material != null) {
			blocksByMaterial.computeIfAbsent(material, (MetalMaterial g) -> new ArrayList<>());
			blocksByMaterial.get(material).add(block);
		}

		// TODO: Make this support multiple oredicts
		if (block instanceof IOreDictionaryEntry) {
			OreDictionary.registerOre(((IOreDictionaryEntry) block).getOreDictionaryName(), block);
		}

		return block;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createPlate(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enablePlate) && (material.plate == null)) {
			material.plate = addBlock(new BlockMetalPlate(material), "plate", material, ItemGroups.blocksTab);
		}

		return material.plate;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createBars(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBars) && (material.bars == null)) {
			material.bars = addBlock(new BlockMetalBars(material), "bars", material, ItemGroups.blocksTab);
			OreDictionary.registerOre(Oredicts.BARS, material.bars);
		}

		return material.bars;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createBlock(MetalMaterial material) {
		return createBlock(material, false);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param glow Does it have a glow ?
	 * @return the block this function created
	 */
	protected static Block createBlock(MetalMaterial material, boolean glow) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.block == null)) {
			material.block = addBlock(new BlockMetalBlock(material, glow, true), "block", material, ItemGroups.blocksTab);
		}
		return material.block;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createButton(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableButton) && (material.button == null)) {
			material.button = addBlock(new BlockButtonMetal(material), "button", material, ItemGroups.blocksTab);
		}

		return material.button;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createLever(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableLever) && (material.lever == null)) {
			material.lever = addBlock(new BlockMetalLever(material), "lever", material, ItemGroups.blocksTab);
		}

		return material.lever;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createPressurePlate(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enablePressurePlate) && (material.pressurePlate == null)) {
			material.pressurePlate = addBlock(new BlockMetalPressurePlate(material), "pressure_plate", material, ItemGroups.blocksTab);
		}
		return material.pressurePlate;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static BlockSlab createSlab(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableSlab) && (material.halfSlab == null)) {
			material.halfSlab = (BlockSlab) addBlock(new BlockHalfMetalSlab(material), "slab", material, ItemGroups.blocksTab);
		}

		return material.halfSlab;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static BlockSlab createDoubleSlab(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableSlab) && (material.doubleSlab == null)) {
			material.doubleSlab = (BlockSlab) addBlock(new BlockDoubleMetalSlab(material), "slab", material, ItemGroups.blocksTab);
		}

		return material.doubleSlab;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createStairs(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableStairs) && (material.stairs == null) && (material.block != null)) {
			material.stairs = addBlock(new BlockMetalStairs(material), "stairs", material, ItemGroups.blocksTab);
		}

		return material.stairs;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createWall(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableWall) && (material.wall == null) && (material.block != null)) {
			material.wall = addBlock(new BlockMetalWall(material), "wall", material, ItemGroups.blocksTab);
		}

		return material.wall;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createOre(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.hasOre) && (material.ore == null)) {
			material.ore = addBlock(new BlockMetalOre(material), "ore", material, ItemGroups.blocksTab);
		}

		return material.ore;
	}

	/**
	 * This is here purely for End Metals
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createEndOre(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.hasOre) && (material.oreEnd == null)) {
			material.oreEnd = addBlock(new BlockMetalEndOre(material), "end", material, ItemGroups.blocksTab);
		}

		return material.oreEnd;
	}

	/**
	 * This is here purely for Nether Metals
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createNetherOre(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.hasOre) && (material.oreNether == null)) {
			material.oreNether = addBlock(new BlockMetalNetherOre(material), "nether", material, ItemGroups.blocksTab);
		}

		return material.oreNether;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static BlockDoor createDoor(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableDoor) && (material.doorBlock == null)) {
			material.doorBlock = (BlockDoor) addBlock(new BlockMetalDoor(material), "door", material, null);
		}

		return material.doorBlock;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createTrapDoor(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableTrapdoor) && (material.trapdoor == null)) {
			material.trapdoor = addBlock(new BlockMetalTrapDoor(material), "trapdoor", material, ItemGroups.blocksTab);
			OreDictionary.registerOre(Oredicts.TRAPDOOR, material.trapdoor);
		}

		return material.trapdoor;
	}

	/**
	 * Gets an block by its name. The name is the name as it is registered in
	 * the GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "basemetals.")
	 *
	 * @param name
	 *            The name of the block in question
	 * @return The block matching that name, or null if there isn't one
	 */
	public static Block getBlockByName(String name) {
		return blockRegistry.get(name);
	}

	/**
	 * This is the reverse of the getBlockByName(...) method, returning the
	 * registered name of an block instance (Base Metals blocks only).
	 *
	 * @param b
	 *            The item in question
	 * @return The name of the item, or null if the item is not a Base Metals
	 *         block.
	 */
	public static String getNameOfBlock(Block b) {
		return blockRegistry.inverse().get(b);
	}

	public static Map<String, Block> getBlockRegistry() {
		return blockRegistry;
	}

	/**
	 * Gets a map of all blocks added, sorted by material
	 *
	 * @return An unmodifiable map of added items catagorized by material
	 */
	public static Map<MetalMaterial, List<Block>> getBlocksByMaterial() {
		return Collections.unmodifiableMap(blocksByMaterial);
	}
}
