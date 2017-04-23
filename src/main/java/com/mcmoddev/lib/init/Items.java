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
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
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
		if (material.hasOre()) {
			createCrushed(material);
			createCrushedPurified(material);
		}
	}

	/**
	 * 
	 * @param material The material base of these items
	 */
	protected static void createItemsModMekanism(MMDMaterial material) {
		if (material.hasOre()) {
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

	private static Item createItem(MMDMaterial material, Names name, Class<? extends Item> clazz, boolean enabled, boolean extra, CreativeTabs tab) {
		if( enabled && extra && !material.hasItem(name) ) {
			return createItem(material, name.toString(), clazz, enabled, extra, tab);
		} else {
			return material.getItem(name);
		}
	}
	
	private static Item createItem(MMDMaterial material, String name, Class<? extends Item> clazz, boolean enabled, boolean extra, CreativeTabs tab) {
		if( material == null ) {
			return null;
		}
		
		if( enabled && extra ) {
			Constructor<?> ctor = null;
			Item inst = null;
			try {
				ctor = clazz.getConstructor(material.getClass());
			} catch( Exception e ) {
				FMLLog.getLogger().fatal("Class for Item named %s does not have an accessible constructor or another exception occurred", name);
				FMLLog.getLogger().fatal(e.toString());
				return null;
			}
			
			try {
				inst = (Item)ctor.newInstance(material);
			} catch( Exception e ) {
				FMLLog.getLogger().fatal("Unable to create Item named %s for material %s", name, material.getCapitalizedName());
			}
			
			if( inst != null ) {
				return addItem( inst, name, material, tab);
			}
		}
		return null;
	}
	
	private static Item createArmorItem(MMDMaterial material, Names name) {
		if( !( Options.enableArmor && !material.hasItem(name) ) ) {
			return null;
		}
		
		Item it = null;
		switch(name) {
		case HELMET:
			it = ItemMMDArmor.createHelmet(material);
			break;
		case CHESTPLATE:
			it = ItemMMDArmor.createChestplate(material);
			break;
		case LEGGINGS:
			it = ItemMMDArmor.createLeggings(material);
			break;
		case BOOTS:
			it = ItemMMDArmor.createBoots(material);
			break;
		default:
		}
		if( it == null ) {
			return null;
		}
		addItem( it, name.toString(), material, ItemGroups.toolsTab );
		return it;
	}
	/**
	 * 
	 * @param material The material base of this crystal
	 * @return the item this function created
	 */
	protected static Item createCrystal(MMDMaterial material) {
		return createItem(material, Names.CRYSTAL, ItemMMDIngot.class, Options.enableBasics, true, ItemGroups.itemsTab);
	}

	/**
	 * 
	 * @param material The material base of this gem
	 * @return the item this function created
	 */
	protected static Item createGem(MMDMaterial material) {
		return createItem(material, Names.GEM, ItemMMDIngot.class, Options.enableBasics, true, ItemGroups.itemsTab);
	}

	/**
	 * 
	 * @param material The material base of this ingot
	 * @return the item this function created
	 */
	protected static Item createIngot(MMDMaterial material) {
		return createItem(material, Names.INGOT, ItemMMDIngot.class, Options.enableBasics, true, ItemGroups.itemsTab);
	}

	/**
	 * 
	 * @param material The material base of this nugget
	 * @return the item this function created
	 */
	protected static Item createNugget(MMDMaterial material) {
		return createItem(material, Names.NUGGET, ItemMMDNugget.class, Options.enableBasics, true, ItemGroups.itemsTab);
	}

	/**
	 * 
	 * @param material The material base of this powder
	 * @return the item this function created
	 */
	protected static Item createPowder(MMDMaterial material) {
		return createItem(material, Names.POWDER, ItemMMDPowder.class, Options.enableBasics, true, ItemGroups.itemsTab);
	}

	/**
	 * 
	 * @param material The material base of this blend
	 * @return the item this function created
	 */
	protected static Item createBlend(MMDMaterial material) {
		return createItem(material, Names.BLEND, ItemMMDBlend.class, Options.enableBasics, material.hasBlend(), ItemGroups.itemsTab);
	}

	/**
	 * 
	 * @param material The material base of this rod
	 * @return the item this function created
	 */
	protected static Item createRod(MMDMaterial material) {
		Item it = createItem(material, Names.ROD, ItemMMDRod.class, Options.enableRod, true, ItemGroups.itemsTab);
		OreDictionary.registerOre(Oredicts.STICK + material.getCapitalizedName(), it);
		OreDictionary.registerOre(Oredicts.ROD, it);
		return it;
	}

	/**
	 * 
	 * @param material The material base of this gear
	 * @return the item this function created
	 */
	protected static Item createGear(MMDMaterial material) {
		Item it = createItem(material, Names.GEAR, ItemMMDGear.class, Options.enableGear, true, ItemGroups.itemsTab);
		OreDictionary.registerOre(Oredicts.GEAR, it);
		return it;
	}

	/**
	 * 
	 * @param material The material base of this axe
	 * @return the item this function created
	 */
	protected static Item createAxe(MMDMaterial material) {
		return createItem(material, Names.AXE, ItemMMDAxe.class, Options.enableBasicTools, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this crackhammer
	 * @return the item this function created
	 */
	protected static Item createCrackhammer(MMDMaterial material) {
		return createItem(material, Names.CRACKHAMMER, ItemMMDCrackHammer.class, Options.enableCrackHammer, true, ItemGroups.toolsTab);
	}
	
	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createHoe(MMDMaterial material) {
		return createItem(material, Names.HOE, ItemMMDHoe.class, Options.enableBasicTools, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createPickaxe(MMDMaterial material) {
		return createItem(material, Names.PICKAXE, ItemMMDPickaxe.class, Options.enableBasicTools, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createShovel(MMDMaterial material) {
		return createItem(material, Names.SHOVEL, ItemMMDShovel.class, Options.enableBasicTools, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createSword(MMDMaterial material) {
		return createItem(material, Names.SWORD, ItemMMDSword.class, Options.enableBasicTools, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createHelmet(MMDMaterial material) {
		return createArmorItem(material, Names.HELMET);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createChestplate(MMDMaterial material) {
		return createArmorItem(material, Names.CHESTPLATE);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createLeggings(MMDMaterial material) {
		return createArmorItem(material, Names.LEGGINGS);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createBoots(MMDMaterial material) {
		return createArmorItem(material, Names.BOOTS);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createHorseArmor(MMDMaterial material) {
		return createItem(material, Names.HORSEARMOR, ItemMMDHorseArmor.class, Options.enableHorseArmor, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createArrow(MMDMaterial material) {
		return createItem(material, Names.ARROW, ItemMMDArrow.class, Options.enableBowAndArrow, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createBolt(MMDMaterial material) {
		return createItem(material, Names.BOLT, ItemMMDBolt.class, Options.enableCrossbowAndBolt, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createBow(MMDMaterial material) {
		return createItem(material, Names.BOW, ItemMMDBow.class, Options.enableBowAndArrow, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createCrossbow(MMDMaterial material) {
		return createItem(material, Names.CROSSBOW, ItemMMDCrossbow.class, Options.enableCrossbowAndBolt, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createShears(MMDMaterial material) {
		return createItem(material, Names.SHEARS, ItemMMDShears.class, Options.enableShears, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createSmallBlend(MMDMaterial material) {
		return createItem(material, Names.SMALLBLEND, ItemMMDSmallBlend.class, Options.enableSmallDust, material.hasBlend(), ItemGroups.itemsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createFishingRod(MMDMaterial material) {
		return createItem(material, Names.FISHINGROD, ItemMMDFishingRod.class, Options.enableFishingRod, true, ItemGroups.toolsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createSmallPowder(MMDMaterial material) {
		return createItem(material, Names.SMALLPOWDER, ItemMMDSmallPowder.class, Options.enableSmallDust, material.hasBlend(), ItemGroups.itemsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createShield(MMDMaterial material) {
		return createItem(material, Names.SHIELD, ItemMMDShield.class, Options.enableShield, true, ItemGroups.itemsTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createMekCrystal(MMDMaterial material) {
		Item it = createItem(material, Names.CRYSTAL, GenericMMDItem.class, Options.enableMekanism, material.getType()!=MaterialType.CRYSTAL, ItemGroups.itemsTab);
		OreDictionary.registerOre(Oredicts.CRYSTAL + material.getCapitalizedName(), it);
		return it;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createMekShard(MMDMaterial material) {
		Item it = createItem(material, Names.SHARD, GenericMMDItem.class, Options.enableMekanism, true, ItemGroups.itemsTab);
		OreDictionary.registerOre(Oredicts.SHARD + material.getCapitalizedName(), it);
		return it;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createMekClump(MMDMaterial material) {
		Item it = createItem(material, Names.CLUMP, GenericMMDItem.class, Options.enableMekanism, true, ItemGroups.itemsTab);
		OreDictionary.registerOre(Oredicts.CLUMP + material.getCapitalizedName(), it);
		return it;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createMekDirtyPowder(MMDMaterial material) {
		Item it = createItem(material, Names.POWDERDIRTY, GenericMMDItem.class, Options.enableMekanism, true, ItemGroups.itemsTab);
		OreDictionary.registerOre(Oredicts.DUST_DIRTY + material.getCapitalizedName(), it);
		return it;
	}

	// TODO: Possibly make this a Block, double of the normal plate.
	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createCasing(MMDMaterial material) {
		Item it = createItem(material, Names.CASING, GenericMMDItem.class, Options.enableIC2, true, ItemGroups.itemsTab);
		OreDictionary.registerOre(Oredicts.CASING + material.getCapitalizedName(), it);
		return it;
	}

	// TODO: Possibly make this a Block, double of the normal plate.
	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createDensePlate(MMDMaterial material) {
		Item it = createItem(material, Names.DENSEPLATE, GenericMMDItem.class, Options.enableIC2, true, ItemGroups.itemsTab);
		OreDictionary.registerOre(Oredicts.PLATE_DENSE + material.getCapitalizedName(), it);
		return it;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createCrushed(MMDMaterial material) {
		Item it = createItem(material, Names.CRUSHED, GenericMMDItem.class, Options.enableIC2, true, ItemGroups.itemsTab);
		OreDictionary.registerOre(Oredicts.CRUSHED + material.getCapitalizedName(), it);
		return it;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createCrushedPurified(MMDMaterial material) {
		Item it = createItem(material, Names.CRUSHEDPURIFIED, GenericMMDItem.class, Options.enableIC2, true, ItemGroups.itemsTab);
		OreDictionary.registerOre(Oredicts.CRUSHED_PURIFIED + material.getCapitalizedName(), it);
		return it;
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createSlab(MMDMaterial material) {
		return createItem(material, Names.SLAB, ItemMMDSlab.class, Options.enableSlab, material.hasBlock(Names.HALFSLAB) && material.hasBlock(Names.DOUBLESLAB), ItemGroups.blocksTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createAnvil(MMDMaterial material) {
		return createItem(material, Names.ANVIL, ItemMMDAnvilBlock.class, Options.enableAnvil, material.hasBlock(Names.ANVILBLOCK), ItemGroups.blocksTab);
	}

	/**
	 * 
	 * @param material The material base of this item
	 * @return the item this function created
	 */
	protected static Item createDoor(MMDMaterial material) {
		Item it = createItem(material, Names.DOOR, ItemMMDDoor.class, Options.enableDoor, material.hasBlock(Names.DOORBLOCK), ItemGroups.blocksTab);
		OreDictionary.registerOre(Oredicts.DOOR, it);
		return it;
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
