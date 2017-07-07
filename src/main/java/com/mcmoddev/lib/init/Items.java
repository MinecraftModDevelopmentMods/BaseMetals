package com.mcmoddev.lib.init;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.block.*;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.item.*;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.TabContainer;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This class initializes all items in Base Metals and provides some utility
 * methods for looking up items.
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Items {

	private static final String ERROR_ONE_PART1 = "Class for Item named ";
	private static final String ERROR_ONE_PART2 = " does not have the correct constructor";
	private static final String ERROR_TWO_PART1 = "Unable to create new instance of Item class for item name ";
	private static final String ERROR_TWO_PART2 = " of material ";

	private static boolean initDone = false;

	private static BiMap<String, Item> itemRegistry = HashBiMap.create(34);
	private static Map<MMDMaterial, List<Item>> itemsByMaterial = new HashMap<>();

	protected static Map<Names, Class<? extends Item>> nameToClass = new HashMap<>();
	protected static Map<Names, String> nameToOredict = new HashMap<>();
	protected static Map<Names, Boolean> nameToEnabled = new HashMap<>();

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

		com.mcmoddev.basemetals.util.Config.init();
		Blocks.init();

		nameToClass.put(Names.CRYSTAL, ItemMMDIngot.class);
		nameToClass.put(Names.GEM, ItemMMDIngot.class);
		nameToClass.put(Names.ANVIL, ItemMMDAnvilBlock.class);
		nameToClass.put(Names.ARROW, ItemMMDArrow.class);
		nameToClass.put(Names.AXE, ItemMMDAxe.class);
		nameToClass.put(Names.BLEND, ItemMMDBlend.class);
		nameToClass.put(Names.BOLT, ItemMMDBolt.class);
		nameToClass.put(Names.BOOTS, ItemMMDArmor.class);
		nameToClass.put(Names.BOW, ItemMMDBow.class);
		nameToClass.put(Names.CHESTPLATE, ItemMMDArmor.class);
		nameToClass.put(Names.CRACKHAMMER, ItemMMDCrackHammer.class);
		nameToClass.put(Names.CROSSBOW, ItemMMDCrossbow.class);
		nameToClass.put(Names.DOOR, ItemMMDDoor.class);
		nameToClass.put(Names.FISHING_ROD, ItemMMDFishingRod.class);
		nameToClass.put(Names.HELMET, ItemMMDArmor.class);
		nameToClass.put(Names.HOE, ItemMMDHoe.class);
		nameToClass.put(Names.HORSE_ARMOR, ItemMMDHorseArmor.class);
		nameToClass.put(Names.INGOT, ItemMMDIngot.class);
		nameToClass.put(Names.LEGGINGS, ItemMMDArmor.class);
		nameToClass.put(Names.NUGGET, ItemMMDNugget.class);
		nameToClass.put(Names.PICKAXE, ItemMMDPickaxe.class);
		nameToClass.put(Names.POWDER, ItemMMDPowder.class);
		nameToClass.put(Names.SHEARS, ItemMMDShears.class);
		nameToClass.put(Names.SHIELD, ItemMMDShield.class);
		nameToClass.put(Names.SHOVEL, ItemMMDShovel.class);
		nameToClass.put(Names.SLAB, ItemMMDSlab.class);
		nameToClass.put(Names.SMALLBLEND, ItemMMDSmallBlend.class);
		nameToClass.put(Names.SMALLPOWDER, ItemMMDSmallPowder.class);
		nameToClass.put(Names.SWORD, ItemMMDSword.class);
		nameToClass.put(Names.ROD, ItemMMDRod.class);
		nameToClass.put(Names.GEAR, ItemMMDGear.class);

		nameToClass.put(Names.CASING, GenericMMDItem.class);
		nameToClass.put(Names.DENSE_PLATE, GenericMMDItem.class);

		nameToClass.put(Names.CRUSHED, GenericMMDItem.class);
		nameToClass.put(Names.CRUSHED_PURIFIED, GenericMMDItem.class);

//		nameToClass.put(Names.CRYSTAL, GenericMMDItem.class);
		nameToClass.put(Names.SHARD, GenericMMDItem.class);
		nameToClass.put(Names.CLUMP, GenericMMDItem.class);
		nameToClass.put(Names.POWDER_DIRTY, GenericMMDItem.class);

		final String armor = "Armor";
		final String basics = "Basics";
		final String basicTools = "BasicTools";
		final String smallDust = "SmallDust";
		final String crossbowAndBolt = "CrossbowAndBolt";
		final String bowAndArrow = "BowAndArrow";

		nameToEnabled.put(Names.CRYSTAL, Options.thingEnabled(basics));
		nameToEnabled.put(Names.GEM, Options.thingEnabled(basics));
		nameToEnabled.put(Names.ANVIL, Options.thingEnabled("Anvil"));
		nameToEnabled.put(Names.ARROW, Options.thingEnabled(bowAndArrow));
		nameToEnabled.put(Names.AXE, Options.thingEnabled(basicTools));
		nameToEnabled.put(Names.BLEND, Options.thingEnabled(basics));
		nameToEnabled.put(Names.BOLT, Options.thingEnabled(crossbowAndBolt));
		nameToEnabled.put(Names.BOOTS, Options.thingEnabled(armor));
		nameToEnabled.put(Names.BOW, Options.thingEnabled(bowAndArrow));
		nameToEnabled.put(Names.CHESTPLATE, Options.thingEnabled(armor));
		nameToEnabled.put(Names.CRACKHAMMER, Options.thingEnabled("CrackHammer"));
		nameToEnabled.put(Names.CROSSBOW, Options.thingEnabled(crossbowAndBolt));
		nameToEnabled.put(Names.DOOR, Options.thingEnabled("Door"));
		nameToEnabled.put(Names.FISHING_ROD, Options.thingEnabled("FishingRod"));
		nameToEnabled.put(Names.HELMET, Options.thingEnabled(armor));
		nameToEnabled.put(Names.HOE, Options.thingEnabled(basicTools));
		nameToEnabled.put(Names.HORSE_ARMOR, Options.thingEnabled("HorseArmor"));
		nameToEnabled.put(Names.INGOT, Options.thingEnabled(basics));
		nameToEnabled.put(Names.LEGGINGS, Options.thingEnabled(armor));
		nameToEnabled.put(Names.NUGGET, Options.thingEnabled(basics));
		nameToEnabled.put(Names.PICKAXE, Options.thingEnabled(basicTools));
		nameToEnabled.put(Names.POWDER, Options.thingEnabled(basics));
		nameToEnabled.put(Names.SHEARS, Options.thingEnabled("Shears"));
		nameToEnabled.put(Names.SHIELD, Options.thingEnabled("Shield"));
		nameToEnabled.put(Names.SHOVEL, Options.thingEnabled(basicTools));
		nameToEnabled.put(Names.SLAB, Options.thingEnabled("Slab"));
		nameToEnabled.put(Names.SMALLBLEND, Options.thingEnabled(smallDust));
		nameToEnabled.put(Names.SMALLPOWDER, Options.thingEnabled(smallDust));
		nameToEnabled.put(Names.SWORD, Options.thingEnabled(basicTools));
		nameToEnabled.put(Names.ROD, Options.thingEnabled("Rod"));
		nameToEnabled.put(Names.GEAR, Options.thingEnabled("Gear"));

		nameToEnabled.put(Names.CASING, Options.enableModderSupportThings());
		nameToEnabled.put(Names.DENSE_PLATE, Options.enableModderSupportThings());

		final String ic2 = "ic2";
//		nameToEnabled.put(Names.CASING, Options.modEnabled(ic2));
//		nameToEnabled.put(Names.DENSE_PLATE, Options.modEnabled(ic2));

		nameToEnabled.put(Names.CRUSHED, Options.modEnabled(ic2));
		nameToEnabled.put(Names.CRUSHED_PURIFIED, Options.modEnabled(ic2));

		final String mekanism = "mekanism";
//		nameToEnabled.put(Names.CRYSTAL, Options.modEnabled(mekanism));
		nameToEnabled.put(Names.SHARD, Options.modEnabled(mekanism));
		nameToEnabled.put(Names.CLUMP, Options.modEnabled(mekanism));
		nameToEnabled.put(Names.POWDER_DIRTY, Options.modEnabled(mekanism));

		nameToOredict.put(Names.CRYSTAL, Oredicts.CRYSTAL);
		nameToOredict.put(Names.GEM, Oredicts.GEM);
		nameToOredict.put(Names.ANVIL, null);
		nameToOredict.put(Names.ARROW, Oredicts.ARROW);
		nameToOredict.put(Names.AXE, null);
		nameToOredict.put(Names.BLEND, Oredicts.DUST);
		nameToOredict.put(Names.BOLT, null);
		nameToOredict.put(Names.BOOTS, null);
		nameToOredict.put(Names.BOW, null);
		nameToOredict.put(Names.CHESTPLATE, null);
		nameToOredict.put(Names.CRACKHAMMER, null);
		nameToOredict.put(Names.CROSSBOW, null);
		nameToOredict.put(Names.DOOR, null);
		nameToOredict.put(Names.FISHING_ROD, null);
		nameToOredict.put(Names.HELMET, null);
		nameToOredict.put(Names.HOE, null);
		nameToOredict.put(Names.HORSE_ARMOR, null);
		nameToOredict.put(Names.INGOT, Oredicts.INGOT);
		nameToOredict.put(Names.LEGGINGS, null);
		nameToOredict.put(Names.NUGGET, Oredicts.NUGGET);
		nameToOredict.put(Names.PICKAXE, null);
		nameToOredict.put(Names.POWDER, Oredicts.DUST);
		nameToOredict.put(Names.SHEARS, null);
		nameToOredict.put(Names.SHIELD, Oredicts.SHIELD);
		nameToOredict.put(Names.SHOVEL, null);
		nameToOredict.put(Names.SLAB, Oredicts.SLAB);
		nameToOredict.put(Names.SMALLBLEND, Oredicts.DUST_TINY);
		nameToOredict.put(Names.SMALLPOWDER, Oredicts.DUST_TINY);
		nameToOredict.put(Names.SWORD, null);
		nameToOredict.put(Names.ROD, Oredicts.ROD);
		nameToOredict.put(Names.GEAR, Oredicts.GEAR);

		nameToOredict.put(Names.CASING, Oredicts.CASING);
		nameToOredict.put(Names.DENSE_PLATE, Oredicts.PLATE_DENSE);

		nameToOredict.put(Names.CRUSHED, Oredicts.CRUSHED);
		nameToOredict.put(Names.CRUSHED_PURIFIED, Oredicts.CRUSHED_PURIFIED);

//		nameToOredict.put(Names.CRYSTAL, Oredicts.CRUSHED);
		nameToOredict.put(Names.SHARD, Oredicts.SHARD);
		nameToOredict.put(Names.CLUMP, Oredicts.CLUMP);
		nameToOredict.put(Names.POWDER_DIRTY, Oredicts.DUST_DIRTY);

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
		classSortingValues.put(BlockMMDDoor.class, ++ss * 10000);
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
		classSortingValues.put(BlockMMDSlab.class, ++ss * 10000);
		classSortingValues.put(BlockMMDDoubleSlab.class, ++ss * 10000);
		classSortingValues.put(BlockMMDHalfSlab.class, ++ss * 10000);
		classSortingValues.put(BlockMMDLever.class, ++ss * 10000);
		classSortingValues.put(BlockMMDPressurePlate.class, ++ss * 10000);
		classSortingValues.put(BlockMMDStairs.class, ++ss * 10000);
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

	protected static void createItemsBasic(@Nonnull final String materialName, @Nonnull final TabContainer tabs) {
		createItemsBasic(Materials.getMaterialByName(materialName), tabs);
	}
	/**
	 * 
	 * @param material
	 *            The material base of these items
	 * @param tabs
	 *            TabContainer covering the various CreativeTabs items might be
	 *            on
	 */
	protected static void createItemsBasic(@Nonnull final MMDMaterial material, @Nonnull final TabContainer tabs) {
		if ((material == null) || (tabs == null)) {
			return;
		}

		create(Names.BLEND, material, tabs.itemsTab);
		create(Names.INGOT, material, tabs.itemsTab);
		create(Names.NUGGET, material, tabs.itemsTab);
		create(Names.POWDER, material, tabs.itemsTab);
		create(Names.SMALLBLEND, material, tabs.itemsTab);
		create(Names.SMALLPOWDER, material, tabs.itemsTab);
	}

	protected static void createItemsAdditional(@Nonnull final String materialName, @Nonnull final TabContainer tabs) {
		createItemsAdditional(Materials.getMaterialByName(materialName), tabs);
	}

	protected static void createItemsAdditional(@Nonnull final MMDMaterial material, @Nonnull final TabContainer tabs) {
		if ((material == null) || (tabs == null)) {
			return;
		}
		create(Names.ARROW, material, tabs.toolsTab);
		create(Names.AXE, material, tabs.toolsTab);
		create(Names.BOLT, material, tabs.toolsTab);
		create(Names.BOOTS, material, tabs.itemsTab);
		create(Names.BOW, material, tabs.toolsTab);
		create(Names.CHESTPLATE, material, tabs.itemsTab);
		create(Names.CRACKHAMMER, material, tabs.toolsTab);
		create(Names.CROSSBOW, material, tabs.toolsTab);
		create(Names.DOOR, material, tabs.blocksTab);
		create(Names.FISHING_ROD, material, tabs.toolsTab);
		create(Names.HELMET, material, tabs.toolsTab);
		create(Names.HOE, material, tabs.toolsTab);
		create(Names.HORSE_ARMOR, material, tabs.itemsTab);
		create(Names.LEGGINGS, material, tabs.itemsTab);
		create(Names.PICKAXE, material, tabs.toolsTab);
		create(Names.SHEARS, material, tabs.toolsTab);
		create(Names.SHIELD, material, tabs.itemsTab);
		create(Names.SHOVEL, material, tabs.toolsTab);
		create(Names.SLAB, material, tabs.blocksTab);
		create(Names.SWORD, material, tabs.itemsTab);
		create(Names.ROD, material, tabs.itemsTab);
		create(Names.GEAR, material, tabs.itemsTab);
	}

	protected static void createItemsFull(@Nonnull final String materialName, @Nonnull final TabContainer tabs) {
		createItemsFull(Materials.getMaterialByName(materialName), tabs);
	}

	/**
	 * 
	 * @param material
	 *            The material base of these items
	 * @param tabs
	 *            TabContainer covering the various CreativeTabs items might be
	 *            on
	 */
	protected static void createItemsFull(@Nonnull final MMDMaterial material, @Nonnull final TabContainer tabs) {
		if ((material == null) || (tabs == null)) {
			return;
		}

		create(Names.ARROW, material, tabs.toolsTab);
		create(Names.AXE, material, tabs.toolsTab);
		create(Names.BLEND, material, tabs.itemsTab);
		create(Names.BOLT, material, tabs.toolsTab);
		create(Names.BOOTS, material, tabs.itemsTab);
		create(Names.BOW, material, tabs.toolsTab);
		create(Names.CHESTPLATE, material, tabs.itemsTab);
		create(Names.CRACKHAMMER, material, tabs.toolsTab);
		create(Names.CROSSBOW, material, tabs.toolsTab);
		create(Names.DOOR, material, tabs.blocksTab);
		create(Names.FISHING_ROD, material, tabs.toolsTab);
		create(Names.HELMET, material, tabs.toolsTab);
		create(Names.HOE, material, tabs.toolsTab);
		create(Names.HORSE_ARMOR, material, tabs.itemsTab);
		create(Names.INGOT, material, tabs.itemsTab);
		create(Names.LEGGINGS, material, tabs.itemsTab);
		create(Names.NUGGET, material, tabs.itemsTab);
		create(Names.PICKAXE, material, tabs.toolsTab);
		create(Names.POWDER, material, tabs.itemsTab);
		create(Names.SHEARS, material, tabs.toolsTab);
		create(Names.SHIELD, material, tabs.itemsTab);
		create(Names.SHOVEL, material, tabs.toolsTab);
		create(Names.SLAB, material, tabs.blocksTab);
		create(Names.SMALLBLEND, material, tabs.itemsTab);
		create(Names.SMALLPOWDER, material, tabs.itemsTab);
		create(Names.SWORD, material, tabs.itemsTab);
		create(Names.ROD, material, tabs.itemsTab);
		create(Names.GEAR, material, tabs.itemsTab);
	}

	protected static void createItemsModSupport(@Nonnull final String materialName, @Nonnull final TabContainer tabs) {
		createItemsModSupport(Materials.getMaterialByName(materialName), tabs);
	}

	/**
	 * 
	 * @param material
	 *            The material base of these items
	 * @param tab
	 *            TabContainer covering the various CreativeTabs items might be
	 *            on
	 */
	protected static void createItemsModSupport(@Nonnull final MMDMaterial material, @Nonnull final TabContainer tabs) {
		if ((material == null) || (tabs == null)) {
			return;
		}

		if (Options.enableModderSupportThings()) {
			create(Names.CASING, material, tabs.itemsTab);
			create(Names.DENSE_PLATE, material, tabs.itemsTab);
		}

		createItemsModMekanism(material, tabs);
		createItemsModIC2(material, tabs);
	}

	protected static void createItemsModIC2(@Nonnull final String materialName, @Nonnull final TabContainer tabs) {
		createItemsModIC2(Materials.getMaterialByName(materialName), tabs);
	}

	/**
	 * 
	 * @param material
	 *            The material base of these items
	 * @param tab
	 *            TabContainer covering the various CreativeTabs items might be
	 *            on
	 */
	protected static void createItemsModIC2(@Nonnull final MMDMaterial material, @Nonnull final TabContainer tabs) {
		if ((material == null) || (tabs == null)) {
			return;
		}

		if (material.hasOre()) {
			create(Names.CRUSHED, material, tabs.itemsTab);
			create(Names.CRUSHED_PURIFIED, material, tabs.itemsTab);
		}
	}

	protected static void createItemsModMekanism(@Nonnull final String materialName, @Nonnull final TabContainer tabs) {
		createItemsModMekanism(Materials.getMaterialByName(materialName), tabs);
	}

	/**
	 * 
	 * @param material
	 *            The material base of these items
	 * @param tabs
	 *            TabContainer covering the various CreativeTabs items might be
	 *            on
	 */
	protected static void createItemsModMekanism(@Nonnull final MMDMaterial material, @Nonnull final TabContainer tabs) {
		if ((material == null) || (tabs == null)) {
			return;
		}

		if (material.hasOre()) {
			createMekCrystal(material, tabs.itemsTab);
			create(Names.SHARD, material, tabs.itemsTab);
			create(Names.CLUMP, material, tabs.itemsTab);
			create(Names.POWDER_DIRTY, material, tabs.itemsTab);
		}
	}
	
	/**
	 * 
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	protected static Item create(@Nonnull final Names name, @Nonnull final MMDMaterial material, final CreativeTabs tab) {
		if ((material == null) || (name == null)) {
			return null;
		}

		if (material.hasItem(name)) {
			return material.getItem(name);
		}

		if ((name.equals(Names.HELMET)) || (name.equals(Names.CHESTPLATE)) || (name.equals(Names.LEGGINGS)) || (name.equals(Names.BOOTS))) {
			return createArmorItem(name, material, tab);
		}

		if (((name.equals(Names.BLEND)) || name.equals(Names.SMALLBLEND)) && (!material.hasBlend())) {
			return null;
		} else if (name.equals(Names.ANVIL) && (!material.hasBlock(Names.ANVIL))) {
			return null;
		} else if (name.equals(Names.DOOR) && (!material.hasBlock(Names.DOOR))) {
			return null;
		} else if (name.equals(Names.SLAB) && (!material.hasBlock(Names.SLAB) && (!material.hasBlock(Names.DOUBLE_SLAB)))) {
			return null;				
		}

		final Item item = createItem(material, name.toString(), nameToClass.get(name), nameToEnabled.get(name), tab);

		final String oredict = nameToOredict.get(name);
		if (item != null) {
			if (oredict != null) {
				Oredicts.registerOre(oredict + material.getCapitalizedName(), item);
				if (name.equals(Names.ROD)) {
					Oredicts.registerOre(Oredicts.STICK + material.getCapitalizedName(), item);
					Oredicts.registerOre(Oredicts.ROD, item);
				} else if (name.equals(Names.GEAR)) {
					Oredicts.registerOre(Oredicts.GEAR, item);
				}
			}
			if (name.equals(Names.DOOR)) {
				Oredicts.registerOre(Oredicts.DOOR, item);
			}
		}

		return null;
	}

	protected static Item addItem(@Nonnull final Item item, @Nonnull final Names name, final CreativeTabs tab) {
		return addItem(item, name.toString(), null, tab);
	}

	protected static Item addItem(@Nonnull final Item item, @Nonnull final String name, final CreativeTabs tab) {
		return addItem(item, name, null, tab);
	}

	/**
	 * 
	 * @param item
	 *            The item to add
	 * @param name
	 *            Name of the item
	 * @param material
	 *            Material it is made from
	 * @param tab
	 *            which creative tab it is in
	 * @return the item that was added
	 */
	protected static Item addItem(@Nonnull final Item item, @Nonnull final String name, final MMDMaterial material, final CreativeTabs tab) {
		if ((item == null) || (name == null)) {
			return null;
		}
		
		String fullName;
		if (material != null) {
			if (material.hasItem(name)) {
				return material.getItem(name);
			}
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

		return item;
	}

	private static Item createItem(@Nonnull final MMDMaterial material, @Nonnull final String name, @Nonnull final Class<? extends Item> clazz, @Nonnull final boolean enabled, final CreativeTabs tab) {
		if ((material == null) || (name == null)) {
			return null;
		}

		if (material.hasItem(name)) {
			return material.getItem(name);
		}

		if (enabled) {
			Constructor<?> ctor = null;
			Item inst = null;

			try {
				ctor = clazz.getConstructor(material.getClass());
			} catch (NoSuchMethodException ex) {
				BaseMetals.logger.error(ERROR_ONE_PART1 + name + ERROR_ONE_PART2, ex);
				return null;
			} catch (SecurityException ex) {
				BaseMetals.logger.error(ERROR_ONE_PART1 + name + ERROR_ONE_PART2, ex);
				return null;
			}

			try {
				inst = (Item) ctor.newInstance(material);
			} catch (IllegalAccessException ex) {
				BaseMetals.logger.error(ERROR_TWO_PART1 + name + ERROR_TWO_PART2 + material.getCapitalizedName(), ex);
				return null;
			} catch (IllegalArgumentException ex) {
				BaseMetals.logger.error(ERROR_TWO_PART1 + name + ERROR_TWO_PART2 + material.getCapitalizedName(), ex);
				return null;
			} catch (InstantiationException ex) {
				BaseMetals.logger.error(ERROR_TWO_PART1 + name + ERROR_TWO_PART2 + material.getCapitalizedName(), ex);
				return null;
			} catch (InvocationTargetException ex) {
				BaseMetals.logger.error(ERROR_TWO_PART1 + name + ERROR_TWO_PART2 + material.getCapitalizedName(), ex);
				return null;
			} catch (ExceptionInInitializerError ex) {
				BaseMetals.logger.error(ERROR_TWO_PART1 + name + ERROR_TWO_PART2 + material.getCapitalizedName(), ex);
				return null;
			} catch (Exception ex) {
				BaseMetals.logger.error("Unable to create Item named " + name + " for material " + material.getCapitalizedName(), ex);
				return null;
			}

			if (inst != null) {
				addItem(inst, name, material, tab);
				material.addNewItem(name, inst);
				return inst;
			}
		}
		return null;
	}

	private static Item createArmorItem(@Nonnull final Names name, @Nonnull final MMDMaterial material, final CreativeTabs tab) {
		if ((!(nameToEnabled.get(name))) || (material == null) || name == null) {
			return null;
		}

		if (material.hasItem(name)) {
			return material.getItem(name);
		}

		Item item = null;
		switch (name) {
		case HELMET:
			item = ItemMMDArmor.createHelmet(material);
			break;
		case CHESTPLATE:
			item = ItemMMDArmor.createChestplate(material);
			break;
		case LEGGINGS:
			item = ItemMMDArmor.createLeggings(material);
			break;
		case BOOTS:
			item = ItemMMDArmor.createBoots(material);
			break;
		default:
		}

		if (item == null) {
			return null;
		}
		Item finalItem = addItem(item, name.toString(), material, tab);
		material.addNewItem(name, finalItem);
		return finalItem;
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this crystal
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createCrystal(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.CRYSTAL, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this gem
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createGem(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.GEM, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this ingot
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createIngot(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.INGOT, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this nugget
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createNugget(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.NUGGET, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this powder
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createPowder(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.POWDER, material, tab);

	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this blend
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createBlend(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.BLEND, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this rod
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createRod(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.ROD, material, tab);
	}

	// TODO: Possibly make this a placeable Block
	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this gear
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createGear(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.GEAR, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this axe
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createAxe(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.AXE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this crackhammer
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createCrackhammer(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.CRACKHAMMER, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createHoe(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.HOE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createPickaxe(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.PICKAXE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createShovel(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.SHOVEL, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createSword(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.SWORD, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createHelmet(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.HELMET, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createChestplate(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.CHESTPLATE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createLeggings(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.LEGGINGS, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createBoots(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.BOOTS, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createHorseArmor(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.HORSE_ARMOR, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createArrow(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.ARROW, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createBolt(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.BOLT, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createBow(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.BOW, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createCrossbow(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.CROSSBOW, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createShears(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.SHEARS, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createSmallBlend(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.SMALLBLEND, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createFishingRod(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.FISHING_ROD, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createSmallPowder(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.SMALLPOWDER, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createShield(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.SHIELD, material, tab);
	}

	/**
	 * 
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createMekCrystal(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		if (material == null) {
			return null;
		}

		if (material.hasItem(Names.CRYSTAL)) {
			return material.getItem(Names.CRYSTAL);
		}

		final Item item = createItem(material, Names.CRYSTAL.toString(), GenericMMDItem.class, (Options.modEnabled("mekanism") && material.getType() != MaterialType.CRYSTAL), tab);
		if (item != null) {
			Oredicts.registerOre(Oredicts.CRYSTAL + material.getCapitalizedName(), item);
			return item;
		}

		return null;
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createMekShard(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.SHARD, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createMekClump(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.CLUMP, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createMekDirtyPowder(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.POWDER_DIRTY, material, tab);
	}

	// TODO: Possibly make this a placable Block
	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createCasing(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.CASING, material, tab);
	}

	// TODO: Possibly make this a Block, double of the normal plate.
	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createDensePlate(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.DENSE_PLATE, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createCrushed(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.CRUSHED, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createCrushedPurified(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.CRUSHED_PURIFIED, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createSlab(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.SLAB, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createAnvil(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.ANVIL, material, tab);
	}

	/**
	 * 
	 * @deprecated
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	@Deprecated
	protected static Item createDoor(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		return create(Names.DOOR, material, tab);
	}

	/**
	 * Uses reflection to expand the size of the combat damage and attack speed
	 * arrays to prevent initialization index-out-of-bounds errors
	 *
	 * @param itemClass
	 *            The class to modify
	 */
	private static void expandCombatArrays(@Nonnull final Class<?> itemClass) throws IllegalAccessException, NoSuchFieldException {
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
	public static int getSortingValue(@Nonnull final ItemStack itemStack) {
		int classVal = 990000;
		int materialVal = 9900;
		if ((itemStack.getItem() instanceof ItemBlock) && (((ItemBlock) itemStack.getItem()).getBlock() instanceof IMMDObject)) {
			classVal = classSortingValues.computeIfAbsent(((ItemBlock) itemStack.getItem()).getBlock().getClass(), (Class<?> clazz) -> 990000);
			materialVal = materialSortingValues.computeIfAbsent(((IMMDObject) ((ItemBlock) itemStack.getItem()).getBlock()).getMMDMaterial(), (MMDMaterial material) -> 9900);
		} else if (itemStack.getItem() instanceof IMMDObject) {
			classVal = classSortingValues.computeIfAbsent(itemStack.getItem().getClass(), (Class<?> clazz) -> 990000);
			materialVal = materialSortingValues.computeIfAbsent(((IMMDObject) itemStack.getItem()).getMMDMaterial(), (MMDMaterial material) -> 9900);
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
	public static Item getItemByName(@Nonnull final String name) {
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
	public static String getNameOfItem(@Nonnull final Item item) {
		return itemRegistry.inverse().get(item);
	}

	public static Map<String, Item> getItemRegistry() {
		return Collections.unmodifiableMap(itemRegistry);
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
