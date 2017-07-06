package com.mcmoddev.lib.init;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.block.*;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.TabContainer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This class initializes all blocks in Base Metals and provides some utility
 * methods for looking up blocks.
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Blocks {

	private static boolean initDone = false;

	private static final BiMap<String, Block> blockRegistry = HashBiMap.create(16);
	private static final BiMap<String, BlockFluidBase> fluidBlockRegistry = HashBiMap.create(16);
	private static final Map<MMDMaterial, List<Block>> blocksByMaterial = new HashMap<>();

	protected static final Map<Names, Class<? extends Block>> nameToBlock = new HashMap<>();
	protected static final Map<Names, String> nameToOredict = new HashMap<>();
	protected static final Map<Names, Boolean> nameToEnabled = new HashMap<>();

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
		com.mcmoddev.basemetals.util.Config.init();
		
		nameToBlock.put(Names.ANVIL, BlockMMDAnvil.class);
		nameToBlock.put(Names.BARS, BlockMMDBars.class);
		nameToBlock.put(Names.BLOCK, BlockMMDBlock.class);
		nameToBlock.put(Names.BOOKSHELF, BlockMMDBookshelf.class);
		nameToBlock.put(Names.BUTTON, BlockMMDButton.class);
		nameToBlock.put(Names.DOOR, BlockMMDDoor.class);
		nameToBlock.put(Names.DOUBLE_SLAB, BlockMMDSlab.class);
		nameToBlock.put(Names.FLOWER_POT, BlockMMDFlowerPot.class);
		nameToBlock.put(Names.LADDER, BlockMMDLadder.class);
		nameToBlock.put(Names.LEVER, BlockMMDLever.class);
		nameToBlock.put(Names.PLATE, BlockMMDPlate.class);
		nameToBlock.put(Names.PRESSURE_PLATE, BlockMMDPressurePlate.class);
		nameToBlock.put(Names.SLAB, BlockMMDSlab.class);
		nameToBlock.put(Names.STAIRS, BlockMMDStairs.class);
		nameToBlock.put(Names.TRAPDOOR, BlockMMDTrapDoor.class);
		nameToBlock.put(Names.TRIPWIRE_HOOK, BlockMMDTripWireHook.class);
		nameToBlock.put(Names.WALL, BlockMMDWall.class);
		nameToBlock.put(Names.FENCE, BlockMMDFence.class);
		nameToBlock.put(Names.FENCE_GATE, BlockMMDFenceGate.class);
		nameToBlock.put(Names.ENDORE, BlockMMDOre.class);
		nameToBlock.put(Names.NETHERORE, BlockMMDNetherOre.class);
		nameToBlock.put(Names.ENDORE, BlockMMDEndOre.class);

		nameToOredict.put(Names.ANVIL, null);
		nameToOredict.put(Names.BARS, Oredicts.BARS);
		nameToOredict.put(Names.BLOCK, Oredicts.BLOCK);
		nameToOredict.put(Names.BOOKSHELF, null);
		nameToOredict.put(Names.BUTTON, Oredicts.BUTTON);
		nameToOredict.put(Names.DOOR, null);
		nameToOredict.put(Names.DOUBLE_SLAB, null);
		nameToOredict.put(Names.FLOWER_POT, null);
		nameToOredict.put(Names.LADDER, null);
		nameToOredict.put(Names.LEVER, Oredicts.LEVER);
		nameToOredict.put(Names.PLATE, Oredicts.PLATE);
		nameToOredict.put(Names.PRESSURE_PLATE, null);
		nameToOredict.put(Names.SLAB, null);
		nameToOredict.put(Names.STAIRS, Oredicts.STAIRS);
		nameToOredict.put(Names.TRAPDOOR, Oredicts.TRAPDOOR);
		nameToOredict.put(Names.TRIPWIRE_HOOK, null);
		nameToOredict.put(Names.WALL, Oredicts.WALL);
		nameToOredict.put(Names.FENCE, null);
		nameToOredict.put(Names.FENCE_GATE, null);
		nameToOredict.put(Names.ORE, Oredicts.ORE);
		nameToOredict.put(Names.NETHERORE, Oredicts.ORE_NETHER);
		nameToOredict.put(Names.ENDORE, Oredicts.ORE_END);

		final String basics = "Basics";
		final String wall = "Wall";

		nameToEnabled.put(Names.ANVIL, Options.thingEnabled("Anvil"));
		nameToEnabled.put(Names.BARS, Options.thingEnabled("Bars"));
		nameToEnabled.put(Names.BLOCK, Options.thingEnabled(basics));
		nameToEnabled.put(Names.BOOKSHELF, Options.thingEnabled("Bookshelf"));
		nameToEnabled.put(Names.BUTTON, Options.thingEnabled("Button"));
		nameToEnabled.put(Names.DOOR, Options.thingEnabled("Door"));
		nameToEnabled.put(Names.DOUBLE_SLAB, Options.thingEnabled("Slab"));
		nameToEnabled.put(Names.FLOWER_POT, Options.thingEnabled("FlowerPot"));
		nameToEnabled.put(Names.LADDER, Options.thingEnabled("Ladder"));
		nameToEnabled.put(Names.LEVER, Options.thingEnabled("Lever"));
		nameToEnabled.put(Names.PLATE, Options.thingEnabled("Plate"));
		nameToEnabled.put(Names.PRESSURE_PLATE, Options.thingEnabled("PressurePlate"));
		nameToEnabled.put(Names.SLAB, Options.thingEnabled("Slab"));
		nameToEnabled.put(Names.STAIRS, Options.thingEnabled("Stairs"));
		nameToEnabled.put(Names.TRAPDOOR, Options.thingEnabled("Trapdoor"));
		nameToEnabled.put(Names.TRIPWIRE_HOOK, Options.thingEnabled("TripWire"));
		nameToEnabled.put(Names.WALL, Options.thingEnabled(wall));
		nameToEnabled.put(Names.FENCE, Options.thingEnabled(wall));
		nameToEnabled.put(Names.FENCE_GATE, Options.thingEnabled(wall));
		nameToEnabled.put(Names.ORE, Options.thingEnabled(basics));
		nameToEnabled.put(Names.NETHERORE, Options.thingEnabled(basics));
		nameToEnabled.put(Names.ENDORE, Options.thingEnabled(basics));

		Materials.init();
		ItemGroups.init();

		initDone = true;
	}

	/**
	 * 
	 * @param materialName
	 *            The material of interest
	 * @param tabs
	 *            Container of the CreativeTabs these will get registered in
	 */
	protected static void createBlocksBasic(@Nonnull final String materialName, @Nonnull final TabContainer tabs) {
		createBlocksBasic(Materials.getMaterialByName(materialName), tabs);
	}

	/**
	 * 
	 * @param material
	 *            The material of interest
	 * @param tabs
	 *            Container of the CreativeTabs these will get registered in
	 */
	protected static void createBlocksBasic(@Nonnull final MMDMaterial material, @Nonnull final TabContainer tabs) {
		if ((material == null) || (tabs == null)) {
			return;
		}

		create(Names.BLOCK, material, tabs.blocksTab); // Not Gold, Not Iron, Not Diamond, Not Stone
		create(Names.PLATE, material, tabs.blocksTab);
		create(Names.ORE, material, tabs.blocksTab); // Not Gold, Not Iron, Not Diamond, Not Stone
		create(Names.BARS, material, tabs.blocksTab); // Not Iron
		create(Names.DOOR, material, tabs.blocksTab); // Not Iron
		create(Names.TRAPDOOR, material, tabs.blocksTab); // Not Iron
	}

	/**
	 * 
	 * @param materialName
	 *            The material of interest
	 * @param tabs
	 *            Container of the CreativeTabs these will get registered in
	 */
	protected static void createBlocksAdditional(@Nonnull final String materialName, @Nonnull final TabContainer tabs) {
		createBlocksAdditional(Materials.getMaterialByName(materialName), tabs);
	}

	/**
	 * 
	 * @param material
	 *            The material of interest
	 * @param tabs
	 *            Container of the CreativeTabs these will get registered in
	 */
	protected static void createBlocksAdditional(@Nonnull final MMDMaterial material, @Nonnull final TabContainer tabs) {
		if ((material == null) || (tabs == null)) {
			return;
		}

		create(Names.BUTTON, material, tabs.blocksTab);
		create(Names.SLAB, material, tabs.blocksTab);
		create(Names.DOUBLE_SLAB, material, tabs.blocksTab);
		create(Names.LEVER, material, tabs.blocksTab);
		create(Names.PRESSURE_PLATE, material, tabs.blocksTab); // Not Iron, Not Gold
		create(Names.STAIRS, material, tabs.blocksTab);
		create(Names.WALL, material, tabs.blocksTab);
	}

	/**
	 * 
	 * @param materialName
	 *            The material of interest
	 * @param tabs
	 *            Container of the CreativeTabs these will get registered in
	 */
	protected static void createBlocksFull(@Nonnull final String materialName, @Nonnull final TabContainer tabs) {
		createBlocksFull(Materials.getMaterialByName(materialName), tabs);
	}

	/**
	 * 
	 * @param material
	 *            The material of interest
	 * @param tabs
	 *            Container of the CreativeTabs these will get registered in
	 */
	protected static void createBlocksFull(@Nonnull final MMDMaterial material, @Nonnull final TabContainer tabs) {
		if ((material == null) || (tabs == null)) {
			return;
		}

		create(Names.BLOCK, material, tabs.blocksTab);
		create(Names.PLATE, material, tabs.blocksTab);
		create(Names.ORE, material, tabs.blocksTab);
		create(Names.BARS, material, tabs.blocksTab);
		create(Names.DOOR, material, tabs.blocksTab);
		create(Names.TRAPDOOR, material, tabs.blocksTab);

		create(Names.BUTTON, material, tabs.blocksTab);
		create(Names.SLAB, material, tabs.blocksTab);
		create(Names.DOUBLE_SLAB, material, tabs.blocksTab);
		create(Names.LEVER, material, tabs.blocksTab);
		create(Names.PRESSURE_PLATE, material, tabs.blocksTab);
		create(Names.STAIRS, material, tabs.blocksTab);
		create(Names.WALL, material, tabs.blocksTab);
	}

	/**
	 * 
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block create(@Nonnull final Names name, @Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(name, material, false, tab);
	}

	/**
	 * 
	 * @param material
	 *            The material this is made from
	 * @param glow
	 *            Does it have a glow ?
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	protected static Block create(@Nonnull final Names name, @Nonnull final MMDMaterial material, @Nonnull final boolean glow, final CreativeTabs tab) {
		if ((material == null) || (name == null)) {
			return null;
		}

		if (material.hasBlock(name)) {
			return material.getBlock(name);
		}

		if ((name.equals(Names.BLOCK)) && (nameToEnabled.get(name))) {
			material.addNewBlock(name, addBlock(new BlockMMDBlock(material, glow, true), name.toString(), material, tab));
			return material.getBlock(name);
		}

		if (((name.equals(Names.ANVIL)) || (name.equals(Names.FENCE)) || (name.equals(Names.FENCE_GATE)) || (name.equals(Names.FLOWER_POT))
				|| (name.equals(Names.LADDER)) || (name.equals(Names.STAIRS)) || (name.equals(Names.TRIPWIRE_HOOK)) || (name.equals(Names.WALL)))
				&& (!material.hasBlock(Names.BLOCK))) {
			return null;
		}

		final Block block = createBlock(material, name.toString(), nameToBlock.get(name), nameToEnabled.get(name), tab);

		final String oredict = nameToOredict.get(name);
		if ((oredict != null) && (block != null)) {
			Oredicts.registerOre(oredict + material.getCapitalizedName(), block);
		}

		return block;
	}

	protected static Block addBlock(@Nonnull final Block block, @Nonnull final Names name, final CreativeTabs tab) {
		return addBlock(block, name.toString(), null, tab);
	}

	protected static Block addBlock(@Nonnull final Block block, @Nonnull final String name, final CreativeTabs tab) {
		return addBlock(block, name, null, tab);
	}

	/**
	 * 
	 * @param block
	 *            the block of interest
	 * @param name
	 *            name of the thing
	 * @param material
	 *            the material the thing is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return a new block
	 */
	protected static Block addBlock(@Nonnull final Block block, @Nonnull final Names name, final MMDMaterial material, final CreativeTabs tab) {
		return addBlock(block, name.toString(), material, tab);
	}

	/**
	 * 
	 * @param block
	 *            the block of interest
	 * @param name
	 *            name of the thing
	 * @param material
	 *            the material the thing is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return a new block
	 */
	protected static Block addBlock(@Nonnull final Block block, @Nonnull final String name, final MMDMaterial material, final CreativeTabs tab) {

		if ((block == null) || (name == null)) {
			return null;
		}

		String fullName;

		if (material != null) {
			if (block instanceof BlockMMDDoubleSlab) {
				fullName = "double_" + material.getName() + "_" + Names.SLAB;
			} else if ((name.startsWith("nether")) || (name.startsWith("end"))) {
				String neededBit = name.substring(0, name.length() - 3);
				fullName = neededBit + "_" + material.getName() + "_" + "ore";
			} else {
				fullName = material.getName() + "_" + name;
			}
		} else if (block instanceof BlockMMDDoubleSlab) { // FIXME: this should be checking for any Double Slab derivative
			fullName = "double_" + name;
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

	private static Block createBlock(@Nonnull final MMDMaterial material, @Nonnull final String name, @Nonnull final Class<? extends Block> clazz, @Nonnull final boolean enabled, final CreativeTabs tab) {
		if ((material == null) || name == null) {
			return null;
		}

		if (enabled) {
			Constructor<?> ctor = null;
			Block inst = null;
			try {
				ctor = clazz.getConstructor(material.getClass());
			} catch (Exception ex) {
				BaseMetals.logger.fatal("Class for Block named " + name + " does not have an accessible constructor or another exception occurred", ex);
				return null;
			}

			try {
				inst = (Block) ctor.newInstance(material);
			} catch (Exception ex) {
				BaseMetals.logger.fatal("Unable to create Block named " + name + " for material " + material.getCapitalizedName(), ex);
			}

			if (inst != null) {
				material.addNewBlock(name, addBlock(inst, name, material, tab));
				return inst;
			}
		}

		return null;
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createBookshelf(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.BOOKSHELF, material, tab);
	}

	protected static Block createBookshelf(@Nonnull final MMDMaterial material, @Nonnull final boolean fullBlock, final CreativeTabs tab) {
		if (material == null) {
			return null;
		}

		BlockMMDBookshelf bs = (BlockMMDBookshelf) create(Names.BOOKSHELF, material, tab);
		if (bs != null) {
			bs.setFullBlock(fullBlock);
		}
		return bs;
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createPlate(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.PLATE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createBars(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.BARS, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createBlock(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.BLOCK, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createBlock(@Nonnull final MMDMaterial material, @Nonnull final boolean glow, final CreativeTabs tab) {
		return create(Names.BLOCK, material, glow, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createButton(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.BUTTON, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createLever(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.LEVER, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createPressurePlate(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.PRESSURE_PLATE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createSlab(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.SLAB, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createDoubleSlab(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.DOUBLE_SLAB, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createStairs(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.STAIRS, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createWall(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.WALL, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createFence(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.FENCE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createFenceGate(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.FENCE_GATE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createAnvil(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.ANVIL, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createFlowerPot(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.FLOWER_POT, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createLadder(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.LADDER, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createTripWireHook(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.TRIPWIRE_HOOK, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createOre(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.ORE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * This is here purely for End Metals
	 * 
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createEndOre(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.ENDORE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * This is here purely for Nether Metals
	 * 
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createNetherOre(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.NETHERORE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createDoor(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.DOOR, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	@Deprecated
	protected static Block createTrapDoor(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.TRAPDOOR, material, tab);
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
	public static Block getBlockByName(@Nonnull final String name) {
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
	public static String getNameOfBlock(@Nonnull final Block b) {
		return blockRegistry.inverse().get(b);
	}

	public static Map<String, Block> getBlockRegistry() {
		return Collections.unmodifiableMap(blockRegistry);
	}

	/**
	 * Gets a map of all blocks added, sorted by material
	 *
	 * @return An unmodifiable map of added items categorized by material
	 */
	public static Map<MMDMaterial, List<Block>> getBlocksByMaterial() {
		return Collections.unmodifiableMap(blocksByMaterial);
	}
}
