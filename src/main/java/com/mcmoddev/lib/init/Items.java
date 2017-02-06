package com.mcmoddev.lib.init;

import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.registry.IOreDictionaryEntry;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.blocks.*;
import com.mcmoddev.lib.items.*;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

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

	private static boolean initDone = false;

	private static BiMap<String, Item> itemRegistry = HashBiMap.create(34);
	private static Map<MetalMaterial, List<Item>> itemsByMaterial = new HashMap<>();

	private static Map<Class<?>, Integer> classSortingValues = new HashMap<>();
	private static Map<MetalMaterial, Integer> materialSortingValues = new HashMap<>();

	// public static UniversalBucket universal_bucket; // now automatically added by Forge

	protected Items() {
		throw new IllegalAccessError("Not a instantiable class");
	}

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
			BaseMetals.logger.error("Error modifying item classes", ex);
//			FMLLog.severe("Error modifying item classes: %s", ex);
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
		classSortingValues.put(BlockMetalDoor.class, ++ss * 10000); // Is this needed? (ItemMetalDoor)
		classSortingValues.put(BlockMetalTrapDoor.class, ++ss * 10000);
		classSortingValues.put(InteractiveFluidBlock.class, ++ss * 10000);
		classSortingValues.put(BlockMoltenFluid.class, ++ss * 10000);
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
		classSortingValues.put(ItemMetalArrow.class, ++ss * 10000);
		classSortingValues.put(ItemMetalBolt.class, ++ss * 10000);
		classSortingValues.put(ItemMetalBow.class, ++ss * 10000);
		classSortingValues.put(ItemMetalCrossbow.class, ++ss * 10000);
		classSortingValues.put(ItemMetalFishingRod.class, ++ss * 10000);
		classSortingValues.put(ItemMetalHorseArmor.class, ++ss * 10000);
		classSortingValues.put(ItemMetalShears.class, ++ss * 10000);
		classSortingValues.put(ItemMetalShield.class, ++ss * 10000);
		classSortingValues.put(ItemMetalGear.class, ++ss * 10000);
		classSortingValues.put(ItemMetalRod.class, ++ss * 10000);
		classSortingValues.put(ItemMetalDoor.class, classSortingValues.get(BlockMetalDoor.class));
		classSortingValues.put(GenericMetalItem.class, ++ss * 10000);

		classSortingValues.put(BlockButtonMetal.class, ++ss * 10000);
		classSortingValues.put(BlockMetalSlab.class, ++ss * 10000); // Is this needed? (ItemMetalSlab)
		classSortingValues.put(BlockDoubleMetalSlab.class, ++ss * 10000); // Probably not needed (ItemMetalSlab)
		classSortingValues.put(BlockHalfMetalSlab.class, ++ss * 10000); // Probably not needed (ItemMetalSlab)
		classSortingValues.put(BlockMetalLever.class, ++ss * 10000);
		classSortingValues.put(BlockMetalPressurePlate.class, ++ss * 10000);
		classSortingValues.put(BlockMetalStairs.class, ++ss * 10000); // Is this needed?
		classSortingValues.put(BlockMetalWall.class, ++ss * 10000);
		classSortingValues.put(BlockMoltenFluid.class, ++ss * 10000);
		classSortingValues.put(ItemMetalSlab.class, classSortingValues.get(BlockMetalSlab.class));
	}

	protected static void addToMetList() {
		final List<MetalMaterial> metlist = new ArrayList<>(Materials.getAllMaterials().size());
		metlist.addAll(Materials.getAllMaterials());
		metlist.sort((MetalMaterial a, MetalMaterial b) -> a.getName().compareToIgnoreCase(b.getName()));
		for (int i = 0; i < metlist.size(); i++) {
			materialSortingValues.put(metlist.get(i), i * 100);
		}
	}

	protected static void createItemsBasic(MetalMaterial material) {
		createBlend(material);
		createIngot(material);
		createNugget(material);
		createPowder(material);
		createSmallBlend(material);
		createSmallPowder(material);
	}

	protected static void createItemsFull(MetalMaterial material) {
		createArrow(material);
		createAxe(material);
		createBlend(material);
		createBolt(material);
		createBoots(material);
		createBow(material);
		createChestplate(material);
		createCrackhammer(material);
		createCrossbow(material);
		createDoor(material);
		createFishingRod(material);
		createHelmet(material);
		createHoe(material);
		createHorseArmor(material);
		createIngot(material);
		createLeggings(material);
		createNugget(material);
		createPickaxe(material);
		createPowder(material);
		createShears(material);
		createShield(material);
		createShovel(material);
		createSlab(material);
		createSmallBlend(material);
		createSmallPowder(material);
		createSword(material);
		createRod(material);
		createGear(material);
	}

	protected static void createItemsModSupport(MetalMaterial material) {
		createItemsModMekanism(material);
		createItemsModIC2(material);
	}

	protected static void createItemsModIC2(MetalMaterial material) {
		createDensePlate(material);
		createCrushed(material);
		createCrushedPurified(material);
	}

	protected static void createItemsModMekanism(MetalMaterial material) {
		createCrystal(material);
		createShard(material);
		createClump(material);
		createDirtyPowder(material);
	}

	protected static Item addItem(Item item, String name, MetalMaterial material, CreativeTabs tab) {

		String fullName;
		if (material != null) {
			fullName = material.getName() + "_" + name;
		} else {
			fullName = name;
		}

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

	protected static Item createIngot(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.ingot == null)) {
			material.ingot = addItem(new ItemMetalIngot(material), "ingot", material, ItemGroups.tab_items);
		}

		return material.ingot;
	}

	protected static Item createNugget(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.nugget == null)) {
			material.nugget = addItem(new ItemMetalNugget(material), "nugget", material, ItemGroups.tab_items);
		}

		return material.nugget;
	}

	protected static Item createPowder(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.powder == null)) {
			material.powder = addItem(new ItemMetalPowder(material), "powder", material, ItemGroups.tab_items);
		}

		return material.powder;
	}

	protected static Item createBlend(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.hasBlend) && (material.blend == null)) {
			material.blend = addItem(new ItemMetalBlend(material), "blend", material, ItemGroups.tab_items);
		}

		return material.blend;
	}

	protected static Item createRod(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableRod) && (material.rod == null)) {
			material.rod = addItem(new ItemMetalRod(material), "rod", material, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.STICK + material.getCapitalizedName(), material.rod);
			OreDictionary.registerOre(Oredicts.ROD, material.rod);
		}

		return material.rod;
	}

	protected static Item createGear(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableGear) && (material.gear == null)) {
			material.gear = addItem(new ItemMetalGear(material), "gear", material, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.GEAR, material.gear);
		}

		return material.gear;
	}

	protected static Item createAxe(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.axe == null)) {
			material.axe = addItem(new ItemMetalAxe(material), "axe", material, ItemGroups.tab_tools);
		}

		return material.axe;
	}

	protected static Item createCrackhammer(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableCrackHammer) && (material.crackhammer == null)) {
			material.crackhammer = addItem(new ItemMetalCrackHammer(material), "crackhammer", material, ItemGroups.tab_tools);
		}

		return material.crackhammer;
	}

	protected static Item createHoe(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.hoe == null)) {
			material.hoe = addItem(new ItemMetalHoe(material), "hoe", material, ItemGroups.tab_tools);
		}

		return material.hoe;
	}

	protected static Item createPickaxe(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.pickaxe == null)) {
			material.pickaxe = addItem(new ItemMetalPickaxe(material), "pickaxe", material, ItemGroups.tab_tools);
		}

		return material.pickaxe;
	}

	protected static Item createShovel(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.shovel == null)) {
			material.shovel = addItem(new ItemMetalShovel(material), "shovel", material, ItemGroups.tab_tools);
		}

		return material.shovel;
	}

	protected static Item createSword(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.sword == null)) {
			material.sword = addItem(new ItemMetalSword(material), "sword", material, ItemGroups.tab_tools);
		}

		return material.sword;
	}

	protected static Item createHelmet(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.helmet == null)) {
			material.helmet = addItem(ItemMetalArmor.createHelmet(material), "helmet", material, ItemGroups.tab_tools);
		}

		return material.helmet;
	}

	protected static Item createChestplate(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.chestplate == null)) {
			material.chestplate = addItem(ItemMetalArmor.createChestplate(material), "chestplate", material, ItemGroups.tab_tools);
		}

		return material.chestplate;
	}

	protected static Item createLeggings(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.leggings == null)) {
			material.leggings = addItem(ItemMetalArmor.createLeggings(material), "leggings", material, ItemGroups.tab_tools);
		}

		return material.leggings;
	}

	protected static Item createBoots(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.boots == null)) {
			material.boots = addItem(ItemMetalArmor.createBoots(material), "boots", material, ItemGroups.tab_tools);
		}

		return material.boots;
	}

	protected static Item createHorseArmor(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableHorseArmor) && (material.horse_armor == null)) {
			material.horse_armor = addItem(new ItemMetalHorseArmor(material), "horsearmor", material, ItemGroups.tab_tools);
		}

		return material.horse_armor;
	}

	protected static Item createArrow(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBowAndArrow) && (material.arrow == null)) {
			material.arrow = addItem(new ItemMetalArrow(material), "arrow", material, ItemGroups.tab_tools);
		}

		return material.arrow;
	}

	protected static Item createBolt(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableCrossbowAndBolt) && (material.bolt == null)) {
			material.bolt = addItem(new ItemMetalBolt(material), "bolt", material, ItemGroups.tab_tools);
		}

		return material.bolt;
	}

	protected static Item createBow(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBowAndArrow) && (material.bow == null)) {
			material.bow = addItem(new ItemMetalBow(material), "bow", material, ItemGroups.tab_tools);
		}

		return material.bow;
	}

	protected static Item createCrossbow(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableCrossbowAndBolt) && (material.crossbow == null)) {
			material.crossbow = addItem(new ItemMetalCrossbow(material), "crossbow", material, ItemGroups.tab_tools);
		}

		return material.crossbow;
	}

	protected static Item createShears(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableShears) && (material.shears == null)) {
			material.shears = addItem(new ItemMetalShears(material), "shears", material, ItemGroups.tab_tools);
		}

		return material.shears;
	}

	protected static Item createSmallBlend(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableSmallDust) && (material.hasBlend) && (material.smallblend == null)) {
			material.smallblend = addItem(new ItemMetalSmallBlend(material), "smallblend", material, ItemGroups.tab_items);
		}

		return material.smallblend;
	}

	protected static Item createFishingRod(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableFishingRod) && (material.fishing_rod == null)) {
			material.fishing_rod = addItem(new ItemMetalFishingRod(material), "fishing_rod", material, ItemGroups.tab_tools);
		}

		return material.fishing_rod;
	}

	protected static Item createSmallPowder(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableSmallDust) && (material.smallpowder == null)) {
			material.smallpowder = addItem(new ItemMetalSmallPowder(material), "smallpowder", material, ItemGroups.tab_items);
		}

		return material.smallpowder;
	}

	protected static Item createShield(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableShield) && (material.shield == null)) {
			material.shield = addItem(new ItemMetalShield(material), "shield", material, ItemGroups.tab_items);
		}

		return material.shield;
	}

	protected static Item createCrystal(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.crystal == null)) {
			material.crystal = addItem(new GenericMetalItem(material), "crystal", material, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.CRYSTAL + material.getCapitalizedName(), material.crystal);
		}

		return material.crystal;
	}

	protected static Item createShard(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.shard == null)) {
			material.shard = addItem(new GenericMetalItem(material), "shard", material, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.SHARD + material.getCapitalizedName(), material.shard);
		}

		return material.shard;
	}

	protected static Item createClump(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.clump == null)) {
			material.clump = addItem(new GenericMetalItem(material), "clump", material, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.CLUMP + material.getCapitalizedName(), material.clump);
		}

		return material.clump;
	}

	protected static Item createDirtyPowder(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.powder_dirty == null)) {
			material.powder_dirty = addItem(new GenericMetalItem(material), "powder_dirty", material, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.DUSTDIRTY + material.getCapitalizedName(), material.powder_dirty);
		}

		return material.powder_dirty;
	}

	// TODO: Possibly make this a block, 1/2 of the normal plate.
	protected static Item createDensePlate(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableIC2) && (material.dense_plate == null)) {
			material.dense_plate = addItem(new GenericMetalItem(material), "dense_plate", material, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.PLATEDENSE + material.getCapitalizedName(), material.dense_plate);
		}

		return material.dense_plate;
	}

	protected static Item createCrushed(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableIC2) && (material.crushed == null)) {
			material.crushed = addItem(new GenericMetalItem(material), "crushed", material, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.CRUSHED + material.getCapitalizedName(), material.crushed);
		}

		return material.crushed;
	}

	protected static Item createCrushedPurified(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableIC2) && (material.crushed_purified == null)) {
			material.crushed_purified = addItem(new GenericMetalItem(material), "crushed_purified", material, ItemGroups.tab_items);
			OreDictionary.registerOre(Oredicts.CRUSHEDPURIFIED + material.getCapitalizedName(), material.crushed_purified);
		}

		return material.crushed_purified;
	}

	protected static Item createSlab(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableSlab) && (material.slab == null)) {
			// TODO: Do we need to check for the block too?
			if ((material.half_slab != null) && (material.double_slab != null)) {
				material.slab = addItem(new ItemMetalSlab(material), "slab", material, ItemGroups.tab_blocks);
			}
		}

		return material.slab;
	}

	protected static Item createDoor(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableDoor) && (material.door == null)) {
			if (material.doorBlock != null) {
				material.door = addItem(new ItemMetalDoor(material), "door", material, ItemGroups.tab_blocks);
				OreDictionary.registerOre(Oredicts.DOOR, material.door);
			}
		}

		return material.door;
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
				BaseMetals.logger.info("%s: Expanding array variable %s.%s to size %d", Thread.currentThread().getStackTrace()[0].toString(), itemClass.getSimpleName(), f.getName(), expandedSize);
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
		int materialVal = 9900;
		if ((a.getItem() instanceof ItemBlock) && (((ItemBlock) a.getItem()).getBlock() instanceof IMetalObject)) {
			classVal = classSortingValues.computeIfAbsent(((ItemBlock) a.getItem()).getBlock().getClass(), (Class<?> c) -> 990000);
			materialVal = materialSortingValues.computeIfAbsent(((IMetalObject) ((ItemBlock) a.getItem()).getBlock()).getMaterial(), (MetalMaterial m) -> 9900);
		} else if (a.getItem() instanceof IMetalObject) {
			classVal = classSortingValues.computeIfAbsent(a.getItem().getClass(), (Class<?> c) -> 990000);
			materialVal = materialSortingValues.computeIfAbsent(((IMetalObject) a.getItem()).getMaterial(), (MetalMaterial m) -> 9900);
		}
		return classVal + materialVal + (a.getMetadata() % 100);
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
}
