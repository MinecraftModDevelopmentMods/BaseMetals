package com.mcmoddev.lib.init;

import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;
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
import net.minecraft.item.*;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
	 * @param tab TabContainer covering the various CreativeTabs items might be on
	 */
	protected static void createItemsBasic(MMDMaterial material, TabContainer tab) {
		createBlend(material, tab.itemsTab);
		createIngot(material, tab.itemsTab);
		createNugget(material, tab.itemsTab);
		createPowder(material, tab.itemsTab);
		createSmallBlend(material, tab.itemsTab);
		createSmallPowder(material, tab.itemsTab);
	}

	/**
	 * 
	 * @param material The material base of these items
	 * @param tab TabContainer covering the various CreativeTabs items might be on
	 */
	protected static void createItemsFull(MMDMaterial material, TabContainer tab) {
		createArrow(material, tab.toolsTab);
		createAxe(material, tab.toolsTab);
		createBlend(material, tab.itemsTab);
		createBolt(material, tab.toolsTab);
		createBoots(material, tab.itemsTab);
		createBow(material, tab.toolsTab);
		createChestplate(material, tab.itemsTab);
		createCrackhammer(material, tab.toolsTab);
		createCrossbow(material, tab.toolsTab);
		createDoor(material, tab.blocksTab);
		createFishingRod(material, tab.toolsTab);
		createHelmet(material, tab.toolsTab);
		createHoe(material, tab.toolsTab);
		createHorseArmor(material, tab.itemsTab);
		createIngot(material, tab.itemsTab);
		createLeggings(material, tab.itemsTab);
		createNugget(material, tab.itemsTab);
		createPickaxe(material, tab.toolsTab);
		createPowder(material, tab.itemsTab);
		createShears(material, tab.toolsTab);
		createShield(material, tab.itemsTab);
		createShovel(material, tab.toolsTab);
		createSlab(material, tab.blocksTab);
		createSmallBlend(material, tab.itemsTab);
		createSmallPowder(material, tab.itemsTab);
		createSword(material, tab.itemsTab);
		createRod(material, tab.itemsTab);
		createGear(material, tab.itemsTab);
	}

	/**
	 * 
	 * @param material The material base of these items
	 * @param tab TabContainer covering the various CreativeTabs items might be on
	 */
	protected static void createItemsModSupport(MMDMaterial material, TabContainer tab) {
		if (Options.enableModderSupportThings) {
			createCasing(material, tab.itemsTab);
			createDensePlate(material, tab.itemsTab);
		}

		createItemsModMekanism(material, tab);
		createItemsModIC2(material, tab);
	}

	/**
	 * 
	 * @param material The material base of these items
	 * @param tab TabContainer covering the various CreativeTabs items might be on
	 */
	protected static void createItemsModIC2(MMDMaterial material, TabContainer tab) {
		if (material.hasOre()) {
			createCrushed(material, tab.itemsTab);
			createCrushedPurified(material, tab.itemsTab);
		}
	}

	/**
	 * 
	 * @param material The material base of these items
	 * @param tab TabContainer covering the various CreativeTabs items might be on
	 */
	protected static void createItemsModMekanism(MMDMaterial material, TabContainer tab) {
		if (material.hasOre()) {
			createMekCrystal(material, tab.itemsTab);
			createMekShard(material, tab.itemsTab);
			createMekClump(material, tab.itemsTab);
			createMekDirtyPowder(material, tab.itemsTab);
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

		return item;
	}

	private static Item createItem(MMDMaterial material, Names name, Class<? extends Item> clazz, boolean enabled, boolean extra, CreativeTabs tab) {
		if (enabled && extra && !material.hasItem(name)) {
			return createItem(material, name.toString(), clazz, enabled, extra, tab);
		}
		return material.getItem(name);
	}
	
	private static Item createItem(MMDMaterial material, String name, Class<? extends Item> clazz, boolean enabled, boolean extra, CreativeTabs tab) {
		if (material == null) {
			return null;
		}
		
		FMLLog.severe("enabled: %s :: extra: %s", enabled, extra);
		
		if( enabled && extra ) {
			Constructor<?> ctor = null;
			Item inst = null;
			try {
				ctor = clazz.getConstructor(material.getClass());
			} catch (NoSuchMethodException ex) {
				FMLLog.severe("Class for Item named %s does not have the correct constructor", name);
				FMLLog.severe(ex.toString());
				return null;
			} catch (SecurityException ex) {
				FMLLog.severe("Class for Item named %s does not have an accessible constructor", name);
				FMLLog.severe(ex.toString());
				return null;
			}
			
			try {
				inst = (Item)ctor.newInstance(material);
			} catch (IllegalAccessException ex ) { 
				FMLLog.severe("Unable to create new instance of Item class for item name %s of material %s", name, material.getCapitalizedName());
				FMLLog.severe(ex.toString());
				return null;
			} catch (IllegalArgumentException ex ) { 
				FMLLog.severe("Unable to create new instance of Item class for item name %s of material %s", name, material.getCapitalizedName());
				FMLLog.severe(ex.toString());
				return null;
			} catch (InstantiationException ex ) { 
				FMLLog.severe("Unable to create new instance of Item class for item name %s of material %s", name, material.getCapitalizedName());
				FMLLog.severe(ex.toString());
				return null;
			} catch (InvocationTargetException ex ) { 
				FMLLog.severe("Unable to create new instance of Item class for item name %s of material %s", name, material.getCapitalizedName());
				FMLLog.severe(ex.toString());
				return null;
			} catch (ExceptionInInitializerError ex ) { 
				FMLLog.severe("Unable to create new instance of Item class for item name %s of material %s", name, material.getCapitalizedName());
				FMLLog.severe(ex.toString());
				return null;
			} catch (Exception ex) {
				FMLLog.severe("Unable to create Item named %s for material %s", name, material.getCapitalizedName());
				FMLLog.severe(ex.toString());
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
	
	private static Item createArmorItem(MMDMaterial material, Names name, CreativeTabs tab) {
		if( !( Options.thingEnabled.get("Armor") && !material.hasItem(name) ) ) {
			return null;
		}
		
		Item item = null;
		switch(name) {
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
		addItem( item, name.toString(), material, tab );
		return item;
	}
	/**
	 * 
	 * @param material The material base of this crystal
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createCrystal(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.CRYSTAL, ItemMMDIngot.class, Options.thingEnabled.get("Basics"), true, tab);
		Oredicts.registerOre(Oredicts.CRYSTAL + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this gem
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createGem(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.GEM, ItemMMDIngot.class, Options.thingEnabled.get("Basics"), true, tab);
		Oredicts.registerOre(Oredicts.GEM + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this ingot
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createIngot(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.INGOT, ItemMMDIngot.class, Options.thingEnabled.get("Basics"), true, tab);
		Oredicts.registerOre(Oredicts.INGOT + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this nugget
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createNugget(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.NUGGET, ItemMMDNugget.class, Options.thingEnabled.get("Basics"), true, tab);
		Oredicts.registerOre(Oredicts.NUGGET + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this powder
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createPowder(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.POWDER, ItemMMDPowder.class, Options.thingEnabled.get("Basics"), true, tab);
		Oredicts.registerOre(Oredicts.DUST + material.getCapitalizedName(), item);
		return item;
		
	}

	/**
	 * 
	 * @param material The material base of this blend
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createBlend(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.BLEND, ItemMMDBlend.class, Options.thingEnabled.get("Basics"), material.hasBlend(), tab);
		if( item == null && material.hasBlend() ) {
			FMLLog.severe("Unable to create Blend for %s", material);
		}
		Oredicts.registerOre(Oredicts.DUST + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this rod
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createRod(MMDMaterial material, CreativeTabs tab) {
		Item item = createItem(material, Names.ROD, ItemMMDRod.class, Options.thingEnabled.get("Rod"), true, tab);
		Oredicts.registerOre(Oredicts.STICK + material.getCapitalizedName(), item);
		Oredicts.registerOre(Oredicts.ROD + material.getCapitalizedName(), item);
		Oredicts.registerOre(Oredicts.ROD, item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this gear
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createGear(MMDMaterial material, CreativeTabs tab) {
		Item item = createItem(material, Names.GEAR, ItemMMDGear.class, Options.thingEnabled.get("Gear"), true, tab);
		Oredicts.registerOre(Oredicts.GEAR + material.getCapitalizedName(), item);
		Oredicts.registerOre(Oredicts.GEAR, item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this axe
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createAxe(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.AXE, ItemMMDAxe.class, Options.thingEnabled.get("BasicTools"), true, tab);
	}

	/**
	 * 
	 * @param material The material base of this crackhammer
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createCrackhammer(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.CRACKHAMMER, ItemMMDCrackHammer.class, Options.thingEnabled.get("CrackHammer"), true, tab);
	}
	
	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createHoe(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.HOE, ItemMMDHoe.class, Options.thingEnabled.get("BasicTools"), true, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createPickaxe(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.PICKAXE, ItemMMDPickaxe.class, Options.thingEnabled.get("BasicTools"), true, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createShovel(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.SHOVEL, ItemMMDShovel.class, Options.thingEnabled.get("BasicTools"), true, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createSword(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.SWORD, ItemMMDSword.class, Options.thingEnabled.get("BasicTools"), true, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createHelmet(MMDMaterial material, CreativeTabs tab) {
		return createArmorItem(material, Names.HELMET, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createChestplate(MMDMaterial material, CreativeTabs tab) {
		return createArmorItem(material, Names.CHESTPLATE, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createLeggings(MMDMaterial material, CreativeTabs tab) {
		return createArmorItem(material, Names.LEGGINGS, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createBoots(MMDMaterial material, CreativeTabs tab) {
		return createArmorItem(material, Names.BOOTS, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createHorseArmor(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.HORSEARMOR, ItemMMDHorseArmor.class, Options.thingEnabled.get("HorseArmor"), true, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createArrow(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.ARROW, ItemMMDArrow.class, Options.thingEnabled.get("BowAndArrow"), true, tab);
		Oredicts.registerOre(Oredicts.ARROW + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createBolt(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.BOLT, ItemMMDBolt.class, Options.thingEnabled.get("CrossbowAndBolt"), true, tab);
		Oredicts.registerOre(Oredicts.AMMOBOLT, item);
		return item;

	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createBow(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.BOW, ItemMMDBow.class, Options.thingEnabled.get("BowAndArrow"), true, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createCrossbow(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.CROSSBOW, ItemMMDCrossbow.class, Options.thingEnabled.get("CrossbowAndBolt"), true, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createShears(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.SHEARS, ItemMMDShears.class, Options.thingEnabled.get("Shears"), true, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createSmallBlend(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.SMALLBLEND, ItemMMDSmallBlend.class, Options.thingEnabled.get("SmallDust"), material.hasBlend(), tab);
		Oredicts.registerOre(Oredicts.DUST_TINY + material.getCapitalizedName(), item);
		return item;

	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createFishingRod(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.FISHINGROD, ItemMMDFishingRod.class, Options.thingEnabled.get("FishingRod"), true, tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createSmallPowder(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.SMALLPOWDER, ItemMMDSmallPowder.class, Options.thingEnabled.get("SmallDust"), material.hasBlend(), tab);
		Oredicts.registerOre(Oredicts.DUST_TINY + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createShield(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.SHIELD, ItemMMDShield.class, Options.thingEnabled.get("Shield"), true, tab);
		Oredicts.registerOre(Oredicts.SHIELD + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createMekCrystal(MMDMaterial material, CreativeTabs tab) {
		Item item = createItem(material, Names.CRYSTAL, GenericMMDItem.class, Options.modEnabled.get("mekanism"), material.getType()!=MaterialType.CRYSTAL, tab);
		Oredicts.registerOre(Oredicts.CRYSTAL + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createMekShard(MMDMaterial material, CreativeTabs tab) {
		Item item = createItem(material, Names.SHARD, GenericMMDItem.class, Options.modEnabled.get("mekanism"), true, tab);
		Oredicts.registerOre(Oredicts.SHARD + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createMekClump(MMDMaterial material, CreativeTabs tab) {
		Item item = createItem(material, Names.CLUMP, GenericMMDItem.class, Options.modEnabled.get("mekanism"), true, tab);
		Oredicts.registerOre(Oredicts.CLUMP + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createMekDirtyPowder(MMDMaterial material, CreativeTabs tab) {
		Item item = createItem(material, Names.POWDERDIRTY, GenericMMDItem.class, Options.modEnabled.get("mekanism"), true, tab);
		Oredicts.registerOre(Oredicts.DUST_DIRTY + material.getCapitalizedName(), item);
		return item;
	}

	// TODO: Possibly make this a Block, double of the normal plate.
	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createCasing(MMDMaterial material, CreativeTabs tab) {
		Item item = createItem(material, Names.CASING, GenericMMDItem.class, Options.modEnabled.get("ic2"), true, tab);
		Oredicts.registerOre(Oredicts.CASING + material.getCapitalizedName(), item);
		return item;
	}

	// TODO: Possibly make this a Block, double of the normal plate.
	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createDensePlate(MMDMaterial material, CreativeTabs tab) {
		Item item = createItem(material, Names.DENSEPLATE, GenericMMDItem.class, Options.modEnabled.get("ic2"), true, tab);
		Oredicts.registerOre(Oredicts.PLATE_DENSE + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createCrushed(MMDMaterial material, CreativeTabs tab) {
		Item item = createItem(material, Names.CRUSHED, GenericMMDItem.class, Options.modEnabled.get("ic2"), true, tab);
		Oredicts.registerOre(Oredicts.CRUSHED + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createCrushedPurified(MMDMaterial material, CreativeTabs tab) {
		Item item = createItem(material, Names.CRUSHEDPURIFIED, GenericMMDItem.class, Options.modEnabled.get("ic2"), true, tab);
		Oredicts.registerOre(Oredicts.CRUSHED_PURIFIED + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createSlab(MMDMaterial material, CreativeTabs tab) {
		final Item item = createItem(material, Names.SLAB, ItemMMDSlab.class, Options.thingEnabled.get("Slab"), material.hasBlock(Names.HALFSLAB) && material.hasBlock(Names.DOUBLESLAB), tab);
		Oredicts.registerOre(Oredicts.SLAB + material.getCapitalizedName(), item);
		return item;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createAnvil(MMDMaterial material, CreativeTabs tab) {
		return createItem(material, Names.ANVIL, ItemMMDAnvilBlock.class, Options.thingEnabled.get("Anvil"), material.hasBlock(Names.ANVILBLOCK), tab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @param tab which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createDoor(MMDMaterial material, CreativeTabs tab) {
		Item item = createItem(material, Names.DOOR, ItemMMDDoor.class, Options.thingEnabled.get("Door"), material.hasBlock(Names.DOORBLOCK), tab);
		Oredicts.registerOre(Oredicts.DOOR, item);
		return item;
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
