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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This class initializes all items in Base Metals and provides some utility
 * methods for looking up items.
 *
 * @author Jasmine Iwanek
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
		} catch (IllegalAccessException | NoSuchFieldException ex) {
			BaseMetals.logger.error("Error modifying item classes", ex);
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
		classSortingValues.put(BlockMetalDoor.class, ++ss * 10000); // TODO: Is this needed? (ItemMetalDoor)
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
		classSortingValues.put(BlockMetalSlab.class, ++ss * 10000); // TODO: Is this needed? (ItemMetalSlab)
		classSortingValues.put(BlockDoubleMetalSlab.class, ++ss * 10000); // TODO: Probably not needed (ItemMetalSlab)
		classSortingValues.put(BlockHalfMetalSlab.class, ++ss * 10000); // TODO: Probably not needed (ItemMetalSlab)
		classSortingValues.put(BlockMetalLever.class, ++ss * 10000);
		classSortingValues.put(BlockMetalPressurePlate.class, ++ss * 10000);
		classSortingValues.put(BlockMetalStairs.class, ++ss * 10000); // TODO: Is this needed?
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

	/**
	 * 
	 * @param material
	 */
	protected static void createItemsBasic(MetalMaterial material) {
		createBlend(material);
		createIngot(material);
		createNugget(material);
		createPowder(material);
		createSmallBlend(material);
		createSmallPowder(material);
	}

	/**
	 * 
	 * @param material
	 */
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

	/**
	 * 
	 * @param material
	 */
	protected static void createItemsModSupport(MetalMaterial material) {
		if (Options.enableModderSupportThings) {
			createCasing(material);
			createDensePlate(material);
		}

		createItemsModMekanism(material);
		createItemsModIC2(material);
	}

	/**
	 * 
	 * @param material
	 */
	protected static void createItemsModIC2(MetalMaterial material) {
		if (material.hasOre) {
			createCrushed(material);
			createCrushedPurified(material);
		}
	}

	/**
	 * 
	 * @param material
	 */
	protected static void createItemsModMekanism(MetalMaterial material) {
		if (material.hasOre) {
			createCrystal(material);
			createShard(material);
			createClump(material);
			createDirtyPowder(material);
		}
	}

	/**
	 * 
	 * @param item
	 * @param name
	 * @param material
	 * @param tab
	 * @return
	 */
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

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createIngot(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.ingot == null)) {
			material.ingot = addItem(new ItemMetalIngot(material), "ingot", material, ItemGroups.itemsTab);
		}

		return material.ingot;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createNugget(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.nugget == null)) {
			material.nugget = addItem(new ItemMetalNugget(material), "nugget", material, ItemGroups.itemsTab);
		}

		return material.nugget;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createPowder(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.powder == null)) {
			material.powder = addItem(new ItemMetalPowder(material), "powder", material, ItemGroups.itemsTab);
		}

		return material.powder;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createBlend(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.hasBlend) && (material.blend == null)) {
			material.blend = addItem(new ItemMetalBlend(material), "blend", material, ItemGroups.itemsTab);
		}

		return material.blend;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createRod(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableRod) && (material.rod == null)) {
			material.rod = addItem(new ItemMetalRod(material), "rod", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.STICK + material.getCapitalizedName(), material.rod);
			OreDictionary.registerOre(Oredicts.ROD, material.rod);
		}

		return material.rod;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createGear(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableGear) && (material.gear == null)) {
			material.gear = addItem(new ItemMetalGear(material), "gear", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.GEAR, material.gear);
		}

		return material.gear;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createAxe(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.axe == null)) {
			material.axe = addItem(new ItemMetalAxe(material), "axe", material, ItemGroups.toolsTab);
		}

		return material.axe;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createCrackhammer(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableCrackHammer) && (material.crackhammer == null)) {
			material.crackhammer = addItem(new ItemMetalCrackHammer(material), "crackhammer", material, ItemGroups.toolsTab);
		}

		return material.crackhammer;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createHoe(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.hoe == null)) {
			material.hoe = addItem(new ItemMetalHoe(material), "hoe", material, ItemGroups.toolsTab);
		}

		return material.hoe;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createPickaxe(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.pickaxe == null)) {
			material.pickaxe = addItem(new ItemMetalPickaxe(material), "pickaxe", material, ItemGroups.toolsTab);
		}

		return material.pickaxe;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createShovel(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.shovel == null)) {
			material.shovel = addItem(new ItemMetalShovel(material), "shovel", material, ItemGroups.toolsTab);
		}

		return material.shovel;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createSword(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.sword == null)) {
			material.sword = addItem(new ItemMetalSword(material), "sword", material, ItemGroups.toolsTab);
		}

		return material.sword;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createHelmet(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.helmet == null)) {
			material.helmet = addItem(ItemMetalArmor.createHelmet(material), "helmet", material, ItemGroups.toolsTab);
		}

		return material.helmet;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createChestplate(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.chestplate == null)) {
			material.chestplate = addItem(ItemMetalArmor.createChestplate(material), "chestplate", material, ItemGroups.toolsTab);
		}

		return material.chestplate;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createLeggings(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.leggings == null)) {
			material.leggings = addItem(ItemMetalArmor.createLeggings(material), "leggings", material, ItemGroups.toolsTab);
		}

		return material.leggings;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createBoots(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.boots == null)) {
			material.boots = addItem(ItemMetalArmor.createBoots(material), "boots", material, ItemGroups.toolsTab);
		}

		return material.boots;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createHorseArmor(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableHorseArmor) && (material.horseArmor == null)) {
			material.horseArmor = addItem(new ItemMetalHorseArmor(material), "horsearmor", material, ItemGroups.toolsTab);
		}

		return material.horseArmor;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createArrow(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBowAndArrow) && (material.arrow == null)) {
			material.arrow = addItem(new ItemMetalArrow(material), "arrow", material, ItemGroups.toolsTab);
		}

		return material.arrow;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createBolt(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableCrossbowAndBolt) && (material.bolt == null)) {
			material.bolt = addItem(new ItemMetalBolt(material), "bolt", material, ItemGroups.toolsTab);
		}

		return material.bolt;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createBow(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBowAndArrow) && (material.bow == null)) {
			material.bow = addItem(new ItemMetalBow(material), "bow", material, ItemGroups.toolsTab);
		}

		return material.bow;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createCrossbow(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableCrossbowAndBolt) && (material.crossbow == null)) {
			material.crossbow = addItem(new ItemMetalCrossbow(material), "crossbow", material, ItemGroups.toolsTab);
		}

		return material.crossbow;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createShears(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableShears) && (material.shears == null)) {
			material.shears = addItem(new ItemMetalShears(material), "shears", material, ItemGroups.toolsTab);
		}

		return material.shears;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createSmallBlend(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableSmallDust) && (material.hasBlend) && (material.smallblend == null)) {
			material.smallblend = addItem(new ItemMetalSmallBlend(material), "smallblend", material, ItemGroups.itemsTab);
		}

		return material.smallblend;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createFishingRod(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableFishingRod) && (material.fishingRod == null)) {
			material.fishingRod = addItem(new ItemMetalFishingRod(material), "fishing_rod", material, ItemGroups.toolsTab);
		}

		return material.fishingRod;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createSmallPowder(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableSmallDust) && (material.smallpowder == null)) {
			material.smallpowder = addItem(new ItemMetalSmallPowder(material), "smallpowder", material, ItemGroups.itemsTab);
		}

		return material.smallpowder;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createShield(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableShield) && (material.shield == null)) {
			material.shield = addItem(new ItemMetalShield(material), "shield", material, ItemGroups.itemsTab);
		}

		return material.shield;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createCrystal(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.crystal == null)) {
			material.crystal = addItem(new GenericMetalItem(material), "crystal", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.CRYSTAL + material.getCapitalizedName(), material.crystal);
		}

		return material.crystal;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createShard(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.shard == null)) {
			material.shard = addItem(new GenericMetalItem(material), "shard", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.SHARD + material.getCapitalizedName(), material.shard);
		}

		return material.shard;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createClump(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.clump == null)) {
			material.clump = addItem(new GenericMetalItem(material), "clump", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.CLUMP + material.getCapitalizedName(), material.clump);
		}

		return material.clump;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createDirtyPowder(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.powderDirty == null)) {
			material.powderDirty = addItem(new GenericMetalItem(material), "powder_dirty", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.DUSTDIRTY + material.getCapitalizedName(), material.powderDirty);
		}

		return material.powderDirty;
	}

	// TODO: Possibly make this a Block, double of the normal plate.
	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createCasing(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableIC2) && (material.casing == null)) {
			material.casing = addItem(new GenericMetalItem(material), "casing", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.CASING + material.getCapitalizedName(), material.casing);
		}

		return material.casing;
	}

	// TODO: Possibly make this a Block, double of the normal plate.
	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createDensePlate(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableIC2) && (material.densePlate == null)) {
			material.densePlate = addItem(new GenericMetalItem(material), "dense_plate", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.PLATEDENSE + material.getCapitalizedName(), material.densePlate);
		}

		return material.densePlate;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createCrushed(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableIC2) && (material.crushed == null)) {
			material.crushed = addItem(new GenericMetalItem(material), "crushed", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.CRUSHED + material.getCapitalizedName(), material.crushed);
		}

		return material.crushed;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createCrushedPurified(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableIC2) && (material.crushedPurified == null)) {
			material.crushedPurified = addItem(new GenericMetalItem(material), "crushed_purified", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.CRUSHEDPURIFIED + material.getCapitalizedName(), material.crushedPurified);
		}

		return material.crushedPurified;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createSlab(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		// TODO: Do we need to check for the block too?
		if ((Options.enableSlab) && (material.slab == null) && (material.halfSlab != null) && (material.doubleSlab != null)) {
			material.slab = addItem(new ItemMetalSlab(material), "slab", material, ItemGroups.blocksTab);
		}

		return material.slab;
	}

	/**
	 * 
	 * @param material
	 * @return
	 */
	protected static Item createDoor(MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if (((Options.enableDoor) && (material.door == null)) && (material.doorBlock != null)) {
			material.door = addItem(new ItemMetalDoor(material), "door", material, ItemGroups.blocksTab);
			OreDictionary.registerOre(Oredicts.DOOR, material.door);
		}

		return material.door;
	}

	/**
	 * Uses reflection to expand the size of the combat damage and attack speed
	 * arrays to prevent initialization index-out-of-bounds errors
	 *
	 * @param itemClass
	 *            The class to modify
	 */
	private static void expandCombatArrays(Class<?> itemClass) throws IllegalAccessException, NoSuchFieldException {
		// WARNING: this method contains black magic
		final int expandedSize = 256;
		final Field[] fields = itemClass.getDeclaredFields();
		for (final Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()) && field.getType().isArray() && field.getType().getComponentType().equals(float.class)) {
				BaseMetals.logger.info("%s: Expanding array variable %s.%s to size %d", Thread.currentThread().getStackTrace()[0].toString(), itemClass.getSimpleName(), field.getName(), expandedSize);
				field.setAccessible(true); // bypass 'private' key word
				final Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL); // bypass 'final' keyword
				final float[] newArray = new float[expandedSize];
				Arrays.fill(newArray, 0F);
				System.arraycopy(field.get(null), 0, newArray, 0, Array.getLength(field.get(null)));
				field.set(null, newArray);
			}
		}
	}

	/**
	 *
	 * @param itemStack
	 *            The ItemStack
	 * @return The output
	 */
	public static int getSortingValue(ItemStack itemStack) {
		int classVal = 990000;
		int materialVal = 9900;
		if ((itemStack.getItem() instanceof ItemBlock) && (((ItemBlock) itemStack.getItem()).getBlock() instanceof IMetalObject)) {
			classVal = classSortingValues.computeIfAbsent(((ItemBlock) itemStack.getItem()).getBlock().getClass(), (Class<?> clazz) -> 990000);
			materialVal = materialSortingValues.computeIfAbsent(((IMetalObject) ((ItemBlock) itemStack.getItem()).getBlock()).getMaterial(), (MetalMaterial material) -> 9900);
		} else if (itemStack.getItem() instanceof IMetalObject) {
			classVal = classSortingValues.computeIfAbsent(itemStack.getItem().getClass(), (Class<?> clazz) -> 990000);
			materialVal = materialSortingValues.computeIfAbsent(((IMetalObject) itemStack.getItem()).getMaterial(), (MetalMaterial material) -> 9900);
		}
		return classVal + materialVal + (itemStack.getMetadata() % 100);
	}

	/**
	 * Gets an item by its name. The name is the name as it is registered in the
	 * GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "basemetals.")
	 *
	 * @param name
	 *            The name of the item in question
	 * @return The item matching that name, or null if there isn't one
	 */
	public static Item getItemByName(String name) {
		return itemRegistry.get(name);
	}

	/**
	 * This is the reverse of the getItemByName(...) method, returning the
	 * registered name of an item instance (Base Metals items only).
	 *
	 * @param item
	 *            The item in question
	 * @return The name of the item, or null if the item is not a Base Metals
	 *         item.
	 */
	public static String getNameOfItem(Item item) {
		return itemRegistry.inverse().get(item);
	}

	public static Map<String, Item> getItemRegistry() {
		return itemRegistry;
	}

	/**
	 * Gets a map of all items added, sorted by material
	 *
	 * @return An unmodifiable map of added items categorized by metal material
	 */
	public static Map<MetalMaterial, List<Item>> getItemsByMaterial() {
		return Collections.unmodifiableMap(itemsByMaterial);
	}
}
