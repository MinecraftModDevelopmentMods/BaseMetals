package com.mcmoddev.lib.init;

import java.util.*;

import com.google.common.collect.*;
import com.mcmoddev.basemetals.material.MetalMaterial;
import com.mcmoddev.basemetals.registry.IOreDictionaryEntry;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.blocks.*;

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

	private static boolean initDone = false;

	private static BiMap<String, Block> blockRegistry = HashBiMap.create(16);
	private static Map<MetalMaterial, List<Block>> blocksByMaterial = new HashMap<>();

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

	protected static void createBlocksBasic(MetalMaterial material) {
		createBlock(material); // Not Gold, Not Iron, Not Diamond, Not Stone
		createPlate(material);
		createOre(material); // Not Gold, Not Iron, Not Diamond, Not Stone
		createBars(material); // Not Iron
		createDoor(material); // Not Iron
		createTrapDoor(material); // Not Iron
	}

	protected static void createBlocksAdditional(MetalMaterial material) {
		createButton(material);
		createSlab(material);
		createDoubleSlab(material);
		createLever(material);
		createPressurePlate(material); // Not Iron, Not Gold
		createStairs(material);
		createWall(material);
	}

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
			fullName = material.getName() + "_" + name;
		} else {
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
			blocksByMaterial.computeIfAbsent(material, (MetalMaterial g) -> new ArrayList<>());
			blocksByMaterial.get(material).add(block);
		}

		// TODO: Make this support multiple oredicts
		if (block instanceof IOreDictionaryEntry) {
			OreDictionary.registerOre(((IOreDictionaryEntry) block).getOreDictionaryName(), block);
		}

		return block;
	}

	protected static Block createPlate(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_PLATE) && (material.plate == null)) {
			material.plate = addBlock(new BlockMetalPlate(material), "plate", material, ItemGroups.tab_blocks);
		}

		return material.plate;
	}

	protected static Block createBars(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_BARS) && (material.bars == null)) {
			material.bars = addBlock(new BlockMetalBars(material), "bars", material, ItemGroups.tab_blocks);
		}

		return material.bars;
	}

	protected static Block createBlock(MetalMaterial material) {
		return createBlock(material, false);
	}

	protected static Block createBlock(MetalMaterial material, boolean glow) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_BASICS) && (material.block == null)) {
			material.block = addBlock(new BlockMetalBlock(material, glow), "block", material, ItemGroups.tab_blocks);
		}
		return material.block;
	}

	protected static Block createButton(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_BUTTON) && (material.button == null)) {
			material.button = addBlock(new BlockButtonMetal(material), "button", material, ItemGroups.tab_blocks);
		}

		return material.button;
	}

	protected static Block createLever(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_LEVER) && (material.lever == null)) {
			material.lever = addBlock(new BlockMetalLever(material), "lever", material, ItemGroups.tab_blocks);
		}

		return material.lever;
	}

	protected static Block createPressurePlate(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_PRESSURE_PLATE) && (material.pressure_plate == null)) {
			material.pressure_plate = addBlock(new BlockMetalPressurePlate(material), "pressure_plate", material, ItemGroups.tab_blocks);
		}
		return material.pressure_plate;
	}

	protected static BlockSlab createSlab(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_SLAB) && (material.half_slab == null)) {
			material.half_slab = (BlockSlab) addBlock(new BlockHalfMetalSlab(material), "slab", material, ItemGroups.tab_blocks);
		}

		return material.half_slab;
	}

	protected static BlockSlab createDoubleSlab(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_SLAB) && (material.double_slab == null)) {
			material.double_slab = (BlockSlab) addBlock(new BlockDoubleMetalSlab(material), "slab", material, ItemGroups.tab_blocks);
		}

		return material.double_slab;
	}

	protected static Block createStairs(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_STAIRS) && (material.stairs == null)) {
			if (((material.block != null) || (material.isVanilla)) && (material.stairs == null)) {
				material.stairs = addBlock(new BlockMetalStairs(material), "stairs", material, ItemGroups.tab_blocks);
			}
		}

		return material.stairs;
	}

	protected static Block createWall(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_WALL) && (material.wall == null)) {
			if ((material.block != null) || (material.isVanilla)) {
				material.wall = addBlock(new BlockMetalWall(material), "wall", material, ItemGroups.tab_blocks);
			}
		}

		return material.wall;
	}

	protected static Block createOre(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_BASICS) && (material.hasOre) && (material.ore == null)) {
			material.ore = addBlock(new BlockMetalOre(material), "ore", material, ItemGroups.tab_blocks);
		}

		return material.ore;
	}

	protected static BlockDoor createDoor(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_DOOR) && (material.doorBlock == null)) {
			material.doorBlock = (BlockDoor) addBlock(new BlockMetalDoor(material), "door", material, null);
		}

		return material.doorBlock;
	}

	protected static Block createTrapDoor(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.ENABLE_TRAPDOOR) && (material.trapdoor == null)) {
			material.trapdoor = addBlock(new BlockMetalTrapDoor(material), "trapdoor", material, ItemGroups.tab_blocks);
		}

		return material.trapdoor;
	}

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
	 * Gets a map of all blocks added, sorted by material
	 *
	 * @return An unmodifiable map of added items catagorized by material
	 */
	public static Map<MetalMaterial, List<Block>> getBlocksByMaterial() {
		return Collections.unmodifiableMap(blocksByMaterial);
	}
}
