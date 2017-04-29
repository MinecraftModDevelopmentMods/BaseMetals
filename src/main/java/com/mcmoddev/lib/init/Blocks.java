package com.mcmoddev.lib.init;

import java.lang.reflect.Constructor;
import java.util.*;

import com.google.common.collect.*;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.block.*;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.TabContainer;

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
	 * @param tab Container of the CreativeTabs these will get registered in
	 */
	protected static void createBlocksBasic(MMDMaterial material, TabContainer tab) {
		createBlock(material, tab.blocksTab); // Not Gold, Not Iron, Not Diamond, Not Stone
		createPlate(material, tab.blocksTab);
		createOre(material, tab.blocksTab); // Not Gold, Not Iron, Not Diamond, Not Stone
		createBars(material, tab.blocksTab); // Not Iron
		createDoor(material, tab.blocksTab); // Not Iron
		createTrapDoor(material, tab.blocksTab); // Not Iron
	}

	/**
	 * 
	 * @param material The material of interest
	 * @param tab Container of the CreativeTabs these will get registered in
	 */
	protected static void createBlocksAdditional(MMDMaterial material, TabContainer tab) {
		createButton(material, tab.blocksTab);
		createSlab(material, tab.blocksTab);
		createDoubleSlab(material, tab.blocksTab);
		createLever(material, tab.blocksTab);
		createPressurePlate(material, tab.blocksTab); // Not Iron, Not Gold
		createStairs(material, tab.blocksTab);
		createWall(material, tab.blocksTab);
	}

	/**
	 * 
	 * @param material The material of interest
	 * @param tab Container of the CreativeTabs these will get registered in
	 */
	protected static void createBlocksFull(MMDMaterial material, TabContainer tab) {
		createBlock(material, tab.blocksTab);
		createPlate(material, tab.blocksTab);
		createOre(material, tab.blocksTab);
		createBars(material, tab.blocksTab);
		createDoor(material, tab.blocksTab);
		createTrapDoor(material, tab.blocksTab);

		createButton(material, tab.blocksTab);
		createSlab(material, tab.blocksTab);
		createDoubleSlab(material, tab.blocksTab);
		createLever(material, tab.blocksTab);
		createPressurePlate(material, tab.blocksTab);
		createStairs(material, tab.blocksTab);
		createWall(material, tab.blocksTab);
	}

	protected static Block addBlock(Block block, Names name, MMDMaterial material, CreativeTabs tab) {
		return addBlock(block, name.toString(), material, tab);
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

		return block;
	}

	private static Block createBlock(MMDMaterial material, Names name, Class<? extends Block> clazz, boolean enabled, boolean extra, CreativeTabs tab) {
		if( !material.hasBlock(name) ) {
			return createBlock( material, name.toString(), clazz, enabled, extra, tab );
		} else {
			return material.getBlock(name);
		}
	}
	
	private static Block createBlock(MMDMaterial material, String name, Class<? extends Block> clazz, boolean enabled, boolean extra, CreativeTabs tab) {
		if( material == null ) {
			return null;
		}

		if (enabled && extra) {
			Constructor<?> ctor = null;
			Block inst = null;
			try {
				ctor = clazz.getConstructor(material.getClass());
			} catch (Exception ex) {
				FMLLog.getLogger().fatal("Class for Block named %s does not have an accessible constructor or another exception occurred", name);
				FMLLog.getLogger().fatal(ex.toString());
				return null;
			}
			
			try {
				inst = (Block)ctor.newInstance(material);
			} catch (Exception ex) {
				FMLLog.getLogger().fatal("Unable to create Block named %s for material %s", name, material.getCapitalizedName());
			}
			
			if( inst != null ) {
				return addBlock( inst, name, material, tab );
			}
		}
		return null;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createBookshelf(MMDMaterial material, CreativeTabs tab) {
		return createBlock(material, Names.BOOKSHELF, BlockMMDBookshelf.class, Options.enableBookshelf, true, tab);
	}

	protected static Block createBookshelf(MMDMaterial material, boolean fullBlock, CreativeTabs tab) {
		BlockMMDBookshelf bs = (BlockMMDBookshelf)createBookshelf(material, tab);
		if( bs != null ) {
			bs.setFullBlock(fullBlock);
		}
		return bs;
	}
	
	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createPlate(MMDMaterial material, CreativeTabs tab) {
		final Block block = createBlock(material, Names.PLATE, BlockMMDPlate.class, Options.enablePlate, true, tab);
		OreDictionary.registerOre(Oredicts.PLATE + material.getCapitalizedName(), block);
		return block;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createBars(MMDMaterial material, CreativeTabs tab) {
		final Block block = createBlock(material, Names.BARS, BlockMMDBars.class, Options.enableBars, true, tab);
		OreDictionary.registerOre(Oredicts.BARS + material.getCapitalizedName(), block);
		return block;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createBlock(MMDMaterial material, CreativeTabs tab) {
		return createBlock(material, false, tab);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param glow Does it have a glow ?
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createBlock(MMDMaterial material, boolean glow, CreativeTabs tab) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (!material.hasBlock(Names.BLOCK))) {
			final Block block = addBlock(new BlockMMDBlock(material, glow, true), Names.BLOCK, material, tab);
			OreDictionary.registerOre(Oredicts.BLOCK + material.getCapitalizedName(), block);
		}
		return material.getBlock(Names.BLOCK);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createButton(MMDMaterial material, CreativeTabs tab) {
		final Block block = createBlock(material, Names.BUTTON, BlockMMDButton.class, Options.enableButton, true, tab);
		OreDictionary.registerOre(Oredicts.BUTTON + material.getCapitalizedName(), block);
		return block;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createLever(MMDMaterial material, CreativeTabs tab) {
		final Block block = createBlock(material, Names.LEVER, BlockMMDLever.class, Options.enableLever, true, tab);
		OreDictionary.registerOre(Oredicts.LEVER + material.getCapitalizedName(), block);
		return block;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createPressurePlate(MMDMaterial material, CreativeTabs tab) {
		return createBlock(material, Names.PRESSUREPLATE, BlockMMDPressurePlate.class, Options.enablePressurePlate, true, tab);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createSlab(MMDMaterial material, CreativeTabs tab) {
		// oreDict is handled in items
		return createBlock(material, Names.HALFSLAB, BlockMMDHalfSlab.class, Options.enableSlab, true, tab);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createDoubleSlab(MMDMaterial material, CreativeTabs tab) {
		// oreDict is handled in items
		return createBlock(material, Names.DOUBLESLAB, BlockMMDDoubleSlab.class, Options.enableSlab, true, tab);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createStairs(MMDMaterial material, CreativeTabs tab) {
		final Block block = createBlock(material, Names.STAIRS, BlockMMDStairs.class, Options.enableStairs, material.hasBlock(Names.BLOCK), tab);
		OreDictionary.registerOre(Oredicts.STAIRS + material.getCapitalizedName(), block);
		return block;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createWall(MMDMaterial material, CreativeTabs tab) {
		final Block block = createBlock(material, Names.WALL, BlockMMDWall.class, Options.enableWall, material.hasBlock(Names.BLOCK), tab);
		OreDictionary.registerOre(Oredicts.WALL + material.getCapitalizedName(), block);
		return block;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createFence(MMDMaterial material, CreativeTabs tab) {
		return createBlock(material, Names.FENCE, BlockMMDFence.class, Options.enableWall, material.hasBlock(Names.BLOCK), tab);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createFenceGate(MMDMaterial material, CreativeTabs tab) {
		return createBlock(material, Names.FENCEGATE, BlockMMDFenceGate.class, Options.enableWall, material.hasBlock(Names.BLOCK), tab);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createAnvil(MMDMaterial material, CreativeTabs tab) {
		return createBlock(material, Names.ANVILBLOCK, BlockMMDAnvil.class, Options.enableAnvil, material.hasBlock(Names.BLOCK), tab);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createFlowerPot(MMDMaterial material, CreativeTabs tab) {
		return createBlock(material, Names.FLOWERPOT, BlockMMDFlowerPot.class, Options.enableFlowerPot, material.hasBlock(Names.BLOCK), tab);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createLadder(MMDMaterial material, CreativeTabs tab) {
		return createBlock(material, Names.LADDER, BlockMMDLadder.class, Options.enableLadder, material.hasBlock(Names.BLOCK), tab);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createTripWireHook(MMDMaterial material, CreativeTabs tab) {
		return createBlock(material, Names.TRIPWIREHOOK, BlockMMDTripWireHook.class, Options.enableTripWire, material.hasBlock(Names.BLOCK), tab);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createOre(MMDMaterial material, CreativeTabs tab) {
		final Block block = createBlock(material, Names.ORE, BlockMMDOre.class, Options.enableBasics, !material.hasBlock(Names.ORE) && material.hasOre(), tab);
		OreDictionary.registerOre(Oredicts.ORE + material.getCapitalizedName(), block);
		return block;
	}

	/**
	 * This is here purely for End Metals
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createEndOre(MMDMaterial material, CreativeTabs tab) {
		final Block block = createBlock(material, Names.ENDORE, BlockMMDEndOre.class, Options.enableBasics, !material.hasBlock(Names.ORE) && material.hasOre(), tab);
		OreDictionary.registerOre(Oredicts.ORE_END + material.getCapitalizedName(), block);
		return block;
	}

	/**
	 * This is here purely for Nether Metals
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createNetherOre(MMDMaterial material, CreativeTabs tab) {
		final Block block = createBlock(material, Names.NETHERORE, BlockMMDNetherOre.class, Options.enableBasics, !material.hasBlock(Names.ORE) && material.hasOre(), tab);
		OreDictionary.registerOre(Oredicts.ORE_NETHER + material.getCapitalizedName(), block);
		return block;
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createDoor(MMDMaterial material, CreativeTabs tab) {
		// oreDict is handled in items
		return createBlock(material, Names.DOORBLOCK, BlockMMDDoor.class, Options.enableDoor, true, tab);
	}

	/**
	 * 
	 * @param material The material this is made from
	 * @param tab which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block createTrapDoor(MMDMaterial material, CreativeTabs tab) {
		final Block block = createBlock(material, Names.TRAPDOOR, BlockMMDTrapDoor.class, Options.enableTrapdoor, true, tab);
		OreDictionary.registerOre(Oredicts.TRAPDOOR + material.getCapitalizedName(), block);
		return block;
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
