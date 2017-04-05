package com.mcmoddev.lib.init;

import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.block.*;
import com.mcmoddev.lib.item.*;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
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
	private static Map<MMDMaterial, List<Item>> itemsByMaterial = new HashMap<>();

	private static Map<Class<?>, Integer> classSortingValues = new HashMap<>();
	private static Map<MMDMaterial, Integer> materialSortingValues = new HashMap<>();

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
		classSortingValues.put(BlockMMDOre.class, ++ss * 10000);
		classSortingValues.put(BlockMMDBlock.class, ++ss * 10000);
		classSortingValues.put(BlockMMDPlate.class, ++ss * 10000);
		classSortingValues.put(BlockMMDBars.class, ++ss * 10000);
		classSortingValues.put(BlockMMDDoor.class, ++ss * 10000); // TODO: Is this needed? (ItemMetalDoor)
		classSortingValues.put(BlockMMDTrapDoor.class, ++ss * 10000);
		classSortingValues.put(InteractiveFluidBlock.class, ++ss * 10000);
		classSortingValues.put(BlockMoltenFluid.class, ++ss * 10000);
		classSortingValues.put(ItemMMDIngot.class, ++ss * 10000);
		classSortingValues.put(ItemMMDNugget.class, ++ss * 10000);
		classSortingValues.put(ItemMMDPowder.class, ++ss * 10000);
		classSortingValues.put(ItemMMDBlend.class, classSortingValues.get(ItemMMDPowder.class));
		classSortingValues.put(ItemMMDSmallPowder.class, ++ss * 10000);
		classSortingValues.put(ItemMMDSmallBlend.class, classSortingValues.get(ItemMMDSmallPowder.class));
		classSortingValues.put(ItemMMDCrackHammer.class, ++ss * 10000);
		classSortingValues.put(ItemMMDPickaxe.class, ++ss * 10000);
		classSortingValues.put(ItemMMDShovel.class, ++ss * 10000);
		classSortingValues.put(ItemMMDAxe.class, ++ss * 10000);
		classSortingValues.put(ItemMMDHoe.class, ++ss * 10000);
		classSortingValues.put(ItemMMDSword.class, ++ss * 10000);
		classSortingValues.put(ItemMMDArmor.class, ++ss * 10000);
		classSortingValues.put(ItemMMDArrow.class, ++ss * 10000);
		classSortingValues.put(ItemMMDBolt.class, ++ss * 10000);
		classSortingValues.put(ItemMMDBow.class, ++ss * 10000);
		classSortingValues.put(ItemMMDCrossbow.class, ++ss * 10000);
		classSortingValues.put(ItemMMDFishingRod.class, ++ss * 10000);
		classSortingValues.put(ItemMMDHorseArmor.class, ++ss * 10000);
		classSortingValues.put(ItemMMDShears.class, ++ss * 10000);
		classSortingValues.put(ItemMMDShield.class, ++ss * 10000);
		classSortingValues.put(ItemMMDGear.class, ++ss * 10000);
		classSortingValues.put(ItemMMDRod.class, ++ss * 10000);
		classSortingValues.put(ItemMMDDoor.class, classSortingValues.get(BlockMMDDoor.class));
		classSortingValues.put(GenericMMDItem.class, ++ss * 10000);

		classSortingValues.put(BlockMMDButton.class, ++ss * 10000);
		classSortingValues.put(BlockMMDSlab.class, ++ss * 10000); // TODO: Is this needed? (ItemMetalSlab)
		classSortingValues.put(BlockMMDDoubleSlab.class, ++ss * 10000); // TODO: Probably not needed (ItemMetalSlab)
		classSortingValues.put(BlockMMDHalfSlab.class, ++ss * 10000); // TODO: Probably not needed (ItemMetalSlab)
		classSortingValues.put(BlockMMDLever.class, ++ss * 10000);
		classSortingValues.put(BlockMMDPressurePlate.class, ++ss * 10000);
		classSortingValues.put(BlockMMDStairs.class, ++ss * 10000); // TODO: Is this needed?
		classSortingValues.put(BlockMMDWall.class, ++ss * 10000);
		classSortingValues.put(BlockMoltenFluid.class, ++ss * 10000);
		classSortingValues.put(ItemMMDSlab.class, classSortingValues.get(BlockMMDSlab.class));
	}

	protected static void addToMetList() {
		final List<MMDMaterial> metlist = new ArrayList<>(Materials.getAllMaterials().size());
		metlist.addAll(Materials.getAllMaterials());
		metlist.sort((MMDMaterial a, MMDMaterial b) -> a.getName().compareToIgnoreCase(b.getName()));
		for (int i = 0; i < metlist.size(); i++) {
			materialSortingValues.put(metlist.get(i), i * 100);
		}
	}

	/**
	 * 
	 * @param material The material base of these items
	 */
	protected static void createItemsBasic(MMDMaterial material) {
		createBlend(material);
		createIngot(material);
		createNugget(material);
		createPowder(material);
		createSmallBlend(material);
		createSmallPowder(material);
	}

	/**
	 * 
	 * @param material The material base of these items
	 */
	protected static void createItemsFull(MMDMaterial material) {
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
	 * @param material The material base of these items
	 */
	protected static void createItemsModSupport(MMDMaterial material) {
		if (Options.enableModderSupportThings) {
			createCasing(material);
			createDensePlate(material);
		}

		createItemsModMekanism(material);
		createItemsModIC2(material);
	}

	/**
	 * 
	 * @param material The material base of these items
	 */
	protected static void createItemsModIC2(MMDMaterial material) {
		if (material.hasOre) {
			createCrushed(material);
			createCrushedPurified(material);
		}
	}

	/**
	 * 
	 * @param material The material base of these items
	 */
	protected static void createItemsModMekanism(MMDMaterial material) {
		if (material.hasOre) {
			createMekCrystal(material);
			createMekShard(material);
			createMekClump(material);
			createMekDirtyPowder(material);
		}
	}

	/**
	 * 
	 * @param item The item to add
	 * @param name Name of the item
	 * @param material Material it is made from
	 * @param tab which creative tab it is in
	 * @return the item that was added
	 */
	protected static Item addItem(Item item, String name, MMDMaterial material, CreativeTabs tab) {

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
			itemsByMaterial.computeIfAbsent(material, (MMDMaterial g) -> new ArrayList<>());
			itemsByMaterial.get(material).add(item);
		}

		if (item instanceof IOreDictionaryEntry) {
			OreDictionary.registerOre(((IOreDictionaryEntry) item).getOreDictionaryName(), item);
		}

		return item;
	}

	/**
	 * 
	 * @param material The material base of this crystal
	 * @return the item this function created
	 */
	protected static Item createCrystal(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.ingot == null)) {
			material.ingot = addItem(new ItemMMDIngot(material), "crystal", material, ItemGroups.itemsTab);
		}

		return material.ingot;
	}

	/**
	 * 
	 * @param material The material base of this gem
	 * @return the item this function created
	 */
	protected static Item createGem(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.ingot == null)) {
			material.ingot = addItem(new ItemMMDIngot(material), "gem", material, ItemGroups.itemsTab);
		}

		return material.ingot;
	}

	/**
	 * 
	 * @param material The material base of this ingot
	 * @return the item this function created
	 */
	protected static Item createIngot(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.ingot == null)) {
			material.ingot = addItem(new ItemMMDIngot(material), "ingot", material, ItemGroups.itemsTab);
		}

		return material.ingot;
	}

	/**
	 * 
	 * @param material The material base of this nugget
	 * @return the item this function created
	 */
	protected static Item createNugget(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.nugget == null)) {
			material.nugget = addItem(new ItemMMDNugget(material), "nugget", material, ItemGroups.itemsTab);
		}

		return material.nugget;
	}

	/**
	 * 
	 * @param material The material base of this powder
	 * @return the item this function created
	 */
	protected static Item createPowder(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.powder == null)) {
			material.powder = addItem(new ItemMMDPowder(material), "powder", material, ItemGroups.itemsTab);
		}

		return material.powder;
	}

	/**
	 * 
	 * @param material The material base of this blend
	 * @return the item this function created
	 */
	protected static Item createBlend(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasics) && (material.hasBlend) && (material.blend == null)) {
			material.blend = addItem(new ItemMMDBlend(material), "blend", material, ItemGroups.itemsTab);
		}

		return material.blend;
	}

	/**
	 * 
	 * @param material The material base of this rod
	 * @return the item this function created
	 */
	protected static Item createRod(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableRod) && (material.rod == null)) {
			material.rod = addItem(new ItemMMDRod(material), "rod", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.STICK + material.getCapitalizedName(), material.rod);
			OreDictionary.registerOre(Oredicts.ROD, material.rod);
		}

		return material.rod;
	}

	/**
	 * 
	 * @param material The material base of this gear
	 * @return the item this function created
	 */
	protected static Item createGear(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableGear) && (material.gear == null)) {
			material.gear = addItem(new ItemMMDGear(material), "gear", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.GEAR, material.gear);
		}

		return material.gear;
	}

	/**
	 * 
	 * @param material The material base of this axe
	 * @return the item this function created
	 */
	protected static Item createAxe(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.axe == null)) {
			material.axe = addItem(new ItemMMDAxe(material), "axe", material, ItemGroups.toolsTab);
		}

		return material.axe;
	}

	/**
	 * 
	 * @param material The material base of this crackhammer
	 * @return the item this function created
	 */
	protected static Item createCrackhammer(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableCrackHammer) && (material.crackhammer == null)) {
			material.crackhammer = addItem(new ItemMMDCrackHammer(material), "crackhammer", material, ItemGroups.toolsTab);
		}

		return material.crackhammer;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createHoe(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.hoe == null)) {
			material.hoe = addItem(new ItemMMDHoe(material), "hoe", material, ItemGroups.toolsTab);
		}

		return material.hoe;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createPickaxe(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.pickaxe == null)) {
			material.pickaxe = addItem(new ItemMMDPickaxe(material), "pickaxe", material, ItemGroups.toolsTab);
		}

		return material.pickaxe;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createShovel(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.shovel == null)) {
			material.shovel = addItem(new ItemMMDShovel(material), "shovel", material, ItemGroups.toolsTab);
		}

		return material.shovel;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createSword(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBasicTools) && (material.sword == null)) {
			material.sword = addItem(new ItemMMDSword(material), "sword", material, ItemGroups.toolsTab);
		}

		return material.sword;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createHelmet(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.helmet == null)) {
			material.helmet = addItem(ItemMMDArmor.createHelmet(material), "helmet", material, ItemGroups.toolsTab);
		}

		return material.helmet;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createChestplate(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.chestplate == null)) {
			material.chestplate = addItem(ItemMMDArmor.createChestplate(material), "chestplate", material, ItemGroups.toolsTab);
		}

		return material.chestplate;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createLeggings(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.leggings == null)) {
			material.leggings = addItem(ItemMMDArmor.createLeggings(material), "leggings", material, ItemGroups.toolsTab);
		}

		return material.leggings;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createBoots(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableArmor) && (material.boots == null)) {
			material.boots = addItem(ItemMMDArmor.createBoots(material), "boots", material, ItemGroups.toolsTab);
		}

		return material.boots;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createHorseArmor(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableHorseArmor) && (material.horseArmor == null)) {
			material.horseArmor = addItem(new ItemMMDHorseArmor(material), "horsearmor", material, ItemGroups.toolsTab);
		}

		return material.horseArmor;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createArrow(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBowAndArrow) && (material.arrow == null)) {
			material.arrow = addItem(new ItemMMDArrow(material), "arrow", material, ItemGroups.toolsTab);
		}

		return material.arrow;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createBolt(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableCrossbowAndBolt) && (material.bolt == null)) {
			material.bolt = addItem(new ItemMMDBolt(material), "bolt", material, ItemGroups.toolsTab);
		}

		return material.bolt;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createBow(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableBowAndArrow) && (material.bow == null)) {
			material.bow = addItem(new ItemMMDBow(material), "bow", material, ItemGroups.toolsTab);
		}

		return material.bow;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createCrossbow(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableCrossbowAndBolt) && (material.crossbow == null)) {
			material.crossbow = addItem(new ItemMMDCrossbow(material), "crossbow", material, ItemGroups.toolsTab);
		}

		return material.crossbow;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createShears(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableShears) && (material.shears == null)) {
			material.shears = addItem(new ItemMMDShears(material), "shears", material, ItemGroups.toolsTab);
		}

		return material.shears;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createSmallBlend(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableSmallDust) && (material.hasBlend) && (material.smallblend == null)) {
			material.smallblend = addItem(new ItemMMDSmallBlend(material), "smallblend", material, ItemGroups.itemsTab);
		}

		return material.smallblend;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createFishingRod(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableFishingRod) && (material.fishingRod == null)) {
			material.fishingRod = addItem(new ItemMMDFishingRod(material), "fishing_rod", material, ItemGroups.toolsTab);
		}

		return material.fishingRod;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createSmallPowder(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableSmallDust) && (material.smallpowder == null)) {
			material.smallpowder = addItem(new ItemMMDSmallPowder(material), "smallpowder", material, ItemGroups.itemsTab);
		}

		return material.smallpowder;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createShield(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableShield) && (material.shield == null)) {
			material.shield = addItem(new ItemMMDShield(material), "shield", material, ItemGroups.itemsTab);
		}

		return material.shield;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createMekCrystal(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.crystal == null) && (material.getType() != MaterialType.CRYSTAL)) {
			material.crystal = addItem(new GenericMMDItem(material), "crystal", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.CRYSTAL + material.getCapitalizedName(), material.crystal);
		}

		return material.crystal;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createMekShard(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.shard == null)) {
			material.shard = addItem(new GenericMMDItem(material), "shard", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.SHARD + material.getCapitalizedName(), material.shard);
		}

		return material.shard;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createMekClump(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.clump == null)) {
			material.clump = addItem(new GenericMMDItem(material), "clump", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.CLUMP + material.getCapitalizedName(), material.clump);
		}

		return material.clump;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createMekDirtyPowder(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableMekanism) && (material.powderDirty == null)) {
			material.powderDirty = addItem(new GenericMMDItem(material), "powder_dirty", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.DUSTDIRTY + material.getCapitalizedName(), material.powderDirty);
		}

		return material.powderDirty;
	}

	// TODO: Possibly make this a Block, double of the normal plate.
	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createCasing(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableIC2) && (material.casing == null)) {
			material.casing = addItem(new GenericMMDItem(material), "casing", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.CASING + material.getCapitalizedName(), material.casing);
		}

		return material.casing;
	}

	// TODO: Possibly make this a Block, double of the normal plate.
	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createDensePlate(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableIC2) && (material.densePlate == null)) {
			material.densePlate = addItem(new GenericMMDItem(material), "dense_plate", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.PLATEDENSE + material.getCapitalizedName(), material.densePlate);
		}

		return material.densePlate;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createCrushed(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableIC2) && (material.crushed == null)) {
			material.crushed = addItem(new GenericMMDItem(material), "crushed", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.CRUSHED + material.getCapitalizedName(), material.crushed);
		}

		return material.crushed;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createCrushedPurified(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if ((Options.enableIC2) && (material.crushedPurified == null)) {
			material.crushedPurified = addItem(new GenericMMDItem(material), "crushed_purified", material, ItemGroups.itemsTab);
			OreDictionary.registerOre(Oredicts.CRUSHEDPURIFIED + material.getCapitalizedName(), material.crushedPurified);
		}

		return material.crushedPurified;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createSlab(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		// TODO: Do we need to check for the block too?
		if ((Options.enableSlab) && (material.slab == null) && (material.halfSlab != null) && (material.doubleSlab != null)) {
			material.slab = addItem(new ItemMMDSlab(material), "slab", material, ItemGroups.blocksTab);
		}

		return material.slab;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createAnvil(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if (((Options.enableAnvil) && (material.anvil == null)) && (material.anvilBlock != null)) {
			material.anvil = addItem(new ItemMMDAnvilBlock(material), "anvil", material, ItemGroups.blocksTab);
		}

		return material.anvil;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createDoor(MMDMaterial material) {
		if (material == null) {
			return null;
		}

		if (((Options.enableDoor) && (material.door == null)) && (material.doorBlock != null)) {
			material.door = addItem(new ItemMMDDoor(material), "door", material, ItemGroups.blocksTab);
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
		if ((itemStack.getItem() instanceof ItemBlock) && (((ItemBlock) itemStack.getItem()).getBlock() instanceof IMMDObject)) {
			classVal = classSortingValues.computeIfAbsent(((ItemBlock) itemStack.getItem()).getBlock().getClass(), (Class<?> clazz) -> 990000);
			materialVal = materialSortingValues.computeIfAbsent(((IMMDObject) ((ItemBlock) itemStack.getItem()).getBlock()).getMaterial(), (MMDMaterial material) -> 9900);
		} else if (itemStack.getItem() instanceof IMMDObject) {
			classVal = classSortingValues.computeIfAbsent(itemStack.getItem().getClass(), (Class<?> clazz) -> 990000);
			materialVal = materialSortingValues.computeIfAbsent(((IMMDObject) itemStack.getItem()).getMaterial(), (MMDMaterial material) -> 9900);
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
	public static Map<MMDMaterial, List<Item>> getItemsByMaterial() {
		return Collections.unmodifiableMap(itemsByMaterial);
	}
}
