package com.mcmoddev.lib.init;

import java.lang.reflect.Constructor;
import java.util.*;

import com.google.common.collect.*;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.block.*;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;

import net.minecraft.block.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.FMLLog;
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
	private static Map<MMDMaterial, List<Block>> blocksByMaterial = new HashMap<>();

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
	protected static void createBlocksBasic(MMDMaterial material) {
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
	protected static void createBlocksAdditional(MMDMaterial material) {
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
	protected static void createBlocksFull(MMDMaterial material) {
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
	protected static Block addBlock(Block block, String name, MMDMaterial material, CreativeTabs tab) {

		String fullName;

		if ((block == null) || (name == null)) {
			return null;
		}

		if ((block instanceof BlockMMDDoubleSlab) && (material != null)) {
			fullName = "double_" + material.getName() + "_" + name;
		} else if (block instanceof BlockMMDDoubleSlab) {
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

		if (!(block instanceof BlockAnvil) && !(block instanceof BlockDoor) && !(block instanceof BlockSlab)) {
			final ItemBlock itemBlock = new ItemBlock(block);
			itemBlock.setRegistryName(fullName);
			itemBlock.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + fullName);
			GameRegistry.register(itemBlock);
		}

		if (tab != null) {
			block.setCreativeTab(tab);
		}

		if (material != null) {
			blocksByMaterial.computeIfAbsent(material, (MMDMaterial g) -> new ArrayList<>());
			blocksByMaterial.get(material).add(block);
		}

		// TODO: Make this support multiple oredicts
		if (block instanceof IOreDictionaryEntry) {
			OreDictionary.registerOre(((IOreDictionaryEntry) block).getOreDictionaryName(), block);
		}

		return block;
	}

	private static Block createBlock(MMDMaterial material, String name, Class<? extends Block> clazz, boolean enabled, boolean extra) {
		if( material == null ) {
			return null;
		}
		
		if( enabled && extra && !material.hasBlock(name)) {
			Constructor<?> ctor = null;
			Block inst = null;
			try {
				ctor = clazz.getConstructor(material.getClass());
			} catch( Exception e ) {
				FMLLog.getLogger().fatal("Class for Block named %s does not have an accessible constructor or another exception occurred", name);
				FMLLog.getLogger().fatal(e.toString());
				return null;
			}
			
			try {
				inst = (Block)ctor.newInstance(material);
			} catch( Exception e ) {
				FMLLog.getLogger().fatal("Unable to create Block named %s for material %s", name, material.getCapitalizedName());
			}
			
			if( inst != null ) {
				addBlock( inst, name, material, ItemGroups.blocksTab );
				return material.getBlock(name);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createBookshelf(MMDMaterial material) {
		return createBlock(material, Names.BOOKSHELF, BlockMMDBookshelf.class, Options.enableBookshelf, true);
	}

	protected static Block createBookshelf(MMDMaterial material, boolean fullBlock) {
		BlockMMDBookshelf bs = (BlockMMDBookshelf)createBookshelf(material);
		if( bs != null ) {
			bs.setFullBlock(fullBlock);
		}
		return bs;
	}
	
	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createPlate(MMDMaterial material) {
		return createBlock(material, Names.PLATE, BlockMMDPlate.class, Options.enablePlate, true);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createBars(MMDMaterial material) {
		return createBlock(material, Names.BARS, BlockMMDBars.class, Options.enableBars, true);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createBlock(MMDMaterial material) {
		return createBlock(material, false);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param glow Does it have a glow ?
	 * @return the block this function created
	 */
	protected static Block createBlock(MMDMaterial material, boolean glow) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (!material.hasBlock(Names.BLOCK))) {
				addBlock(new BlockMMDBlock(material, glow, true), Names.BLOCK, material, ItemGroups.blocksTab);
		}
		return material.getBlock(Names.BLOCK);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createButton(MMDMaterial material) {
		return createBlock(material, Names.BUTTON, BlockMMDButton.class, Options.enableButton, true);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createLever(MMDMaterial material) {
		return createBlock(material, Names.LEVER, BlockMMDLever.class, Options.enableLever, true);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createPressurePlate(MMDMaterial material) {
		return createBlock(material, Names.PRESSUREPLATE, BlockMMDPressurePlate.class, Options.enablePressurePlate, true);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static BlockSlab createSlab(MMDMaterial material) {
		return (BlockSlab)createBlock(material, Names.HALFSLAB, BlockMMDHalfSlab.class, Options.enableSlab, true);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static BlockSlab createDoubleSlab(MMDMaterial material) {
		return (BlockSlab)createBlock(material, Names.DOUBLESLAB, BlockMMDDoubleSlab.class, Options.enableSlab, true);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createStairs(MMDMaterial material) {
		return createBlock(material, Names.STAIRS, BlockMMDStairs.class, Options.enableStairs, material.hasBlock(Names.BLOCK));
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createWall(MMDMaterial material) {
		return createBlock(material, Names.WALL, BlockMMDWall.class, Options.enableWall, material.hasBlock(Names.BLOCK));
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createFence(MMDMaterial material) {
		return createBlock(material, Names.FENCE, BlockMMDFence.class, Options.enableWall, material.hasBlock(Names.BLOCK));
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createFenceGate(MMDMaterial material) {
		return createBlock(material, Names.FENCEGATE, BlockMMDFenceGate.class, Options.enableWall, material.hasBlock(Names.BLOCK));
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createAnvil(MMDMaterial material) {
		return createBlock(material, Names.ANVILBLOCK, BlockMMDAnvil.class, Options.enableAnvil, material.hasBlock(Names.BLOCK));
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createFlowerPot(MMDMaterial material) {
		return createBlock(material, Names.FLOWERPOT, BlockMMDFlowerPot.class, Options.enableFlowerPot, material.hasBlock(Names.BLOCK));
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createLadder(MMDMaterial material) {
		return createBlock(material, Names.LADDER, BlockMMDLadder.class, Options.enableLadder, material.hasBlock(Names.BLOCK));
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createTripWireHook(MMDMaterial material) {
		return createBlock(material, Names.TRIPWIREHOOK, BlockMMDTripWireHook.class, Options.enableTripWire, material.hasBlock(Names.BLOCK));
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createOre(MMDMaterial material) {
		return createBlock(material, Names.ORE, BlockMMDOre.class, Options.enableBasics, !material.hasBlock(Names.ORE) && material.hasOre());
	}

	/**
	 * This is here purely for End Metals
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createEndOre(MMDMaterial material) {
		return createBlock(material, Names.ENDORE, BlockMMDEndOre.class, Options.enableBasics, !material.hasBlock(Names.ENDORE) && material.hasOre());
	}

	/**
	 * This is here purely for Nether Metals
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createNetherOre(MMDMaterial material) {
		return createBlock(material, Names.NETHERORE, BlockMMDNetherOre.class, Options.enableBasics, !material.hasBlock(Names.NETHERORE) && material.hasOre());
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static BlockDoor createDoor(MMDMaterial material) {
		return (BlockDoor)createBlock(material, Names.DOORBLOCK, BlockMMDDoor.class, Options.enableDoor, true);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @return the block this function created
	 */
	protected static Block createTrapDoor(MMDMaterial material) {
		return createBlock(material, Names.TRAPDOOR, BlockMMDTrapDoor.class, Options.enableTrapdoor, true);
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
	public static Map<MMDMaterial, List<Block>> getBlocksByMaterial() {
		return Collections.unmodifiableMap(blocksByMaterial);
	}
}
