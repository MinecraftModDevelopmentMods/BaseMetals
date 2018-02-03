package com.mcmoddev.lib.init;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.block.*;
import com.mcmoddev.lib.data.ConfigKeys;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.integration.plugins.IC2Base;
import com.mcmoddev.lib.integration.plugins.MekanismBase;
import com.mcmoddev.lib.item.*;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * This class initializes all items in Base Metals and provides some utility
 * methods for looking up items.
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Items {

	private static BiMap<String, Item> itemRegistry = HashBiMap.create(34);
	private static Map<MMDMaterial, List<Item>> itemsByMaterial = new HashMap<>();

	private static EnumMap<Names, Class<? extends Item>> nameToClass = new EnumMap<>(Names.class);
	private static EnumMap<Names, String> nameToOredict = new EnumMap<>(Names.class);
	private static EnumMap<Names, Boolean> nameToEnabled = new EnumMap<>(Names.class);

	private static Map<Class<?>, Integer> classSortingValues = new HashMap<>();
	private static Map<MMDMaterial, Integer> materialSortingValues = new HashMap<>();

	// public static UniversalBucket universal_bucket; // now automatically added by
	// Forge

	protected Items() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		addItemType(Names.CRYSTAL, ItemMMDIngot.class, Options.isThingEnabled(ConfigKeys.BASICS), Oredicts.CRYSTAL);
		addItemType(Names.GEM, ItemMMDIngot.class, Options.isThingEnabled(ConfigKeys.BASICS), Oredicts.GEM);
		addItemType(Names.ANVIL, ItemMMDAnvilBlock.class, Options.isThingEnabled(ConfigKeys.ANVIL), null);
		addItemType(Names.ARROW, ItemMMDArrow.class, Options.isThingEnabled(ConfigKeys.BOW_AND_ARROW), Oredicts.ARROW);
		addItemType(Names.AXE, ItemMMDAxe.class, Options.isThingEnabled(ConfigKeys.BASIC_TOOLS), null);
		addItemType(Names.BLEND, ItemMMDBlend.class, Options.isThingEnabled(ConfigKeys.BASICS), Oredicts.DUST);
		addItemType(Names.BOLT, ItemMMDBolt.class, Options.isThingEnabled(ConfigKeys.CROSSBOW_AND_BOLT), null);
		addItemType(Names.BOOTS, ItemMMDArmor.class, Options.isThingEnabled(ConfigKeys.ARMOR), null);
		addItemType(Names.BOW, ItemMMDBow.class, Options.isThingEnabled(ConfigKeys.BOW_AND_ARROW), null);
		addItemType(Names.CHESTPLATE, ItemMMDArmor.class, Options.isThingEnabled(ConfigKeys.ARMOR), null);
		addItemType(Names.CRACKHAMMER, ItemMMDCrackHammer.class, Options.isThingEnabled(ConfigKeys.CRACKHAMMER), null);
		addItemType(Names.CROSSBOW, ItemMMDCrossbow.class, Options.isThingEnabled(ConfigKeys.CROSSBOW_AND_BOLT), null);
		addItemType(Names.DOOR, ItemMMDDoor.class, Options.isThingEnabled(ConfigKeys.DOOR), null);
		addItemType(Names.FISHING_ROD, ItemMMDFishingRod.class, Options.isThingEnabled(ConfigKeys.FISHING_ROD), null);
		addItemType(Names.HELMET, ItemMMDArmor.class, Options.isThingEnabled(ConfigKeys.ARMOR), null);
		addItemType(Names.HOE, ItemMMDHoe.class, Options.isThingEnabled(ConfigKeys.BASIC_TOOLS), null);
		addItemType(Names.HORSE_ARMOR, ItemMMDHorseArmor.class, Options.isThingEnabled(ConfigKeys.HORSE_ARMOR), null);
		addItemType(Names.INGOT, ItemMMDIngot.class, Options.isThingEnabled(ConfigKeys.BASICS), Oredicts.INGOT);
		addItemType(Names.LEGGINGS, ItemMMDArmor.class, Options.isThingEnabled(ConfigKeys.ARMOR), null);
		addItemType(Names.NUGGET, ItemMMDNugget.class, Options.isThingEnabled(ConfigKeys.BASICS), Oredicts.NUGGET);
		addItemType(Names.PICKAXE, ItemMMDPickaxe.class, Options.isThingEnabled(ConfigKeys.BASIC_TOOLS), null);
		addItemType(Names.POWDER, ItemMMDPowder.class, Options.isThingEnabled(ConfigKeys.BASICS), Oredicts.DUST);
		addItemType(Names.SHEARS, ItemMMDShears.class, Options.isThingEnabled(ConfigKeys.SHEARS), null);
		addItemType(Names.SHIELD, ItemMMDShield.class, Options.isThingEnabled(ConfigKeys.SHIELD), Oredicts.SHIELD);
		addItemType(Names.SHOVEL, ItemMMDShovel.class, Options.isThingEnabled(ConfigKeys.BASIC_TOOLS), null);
		addItemType(Names.SCYTHE, ItemMMDSickle.class, Options.isThingEnabled("Sickle"), null);
		addItemType(Names.SLAB, ItemMMDSlab.class, Options.isThingEnabled(ConfigKeys.SLAB), Oredicts.SLAB);
		addItemType(Names.SMALLBLEND, ItemMMDSmallBlend.class, Options.isThingEnabled(ConfigKeys.SMALL_DUST), Oredicts.DUST_TINY);
		addItemType(Names.SMALLPOWDER, ItemMMDSmallPowder.class, Options.isThingEnabled(ConfigKeys.SMALL_DUST), Oredicts.DUST_TINY);
		addItemType(Names.SWORD, ItemMMDSword.class, Options.isThingEnabled(ConfigKeys.BASIC_TOOLS), null);
		addItemType(Names.ROD, ItemMMDRod.class, Options.isThingEnabled(ConfigKeys.ROD), Oredicts.ROD);
		addItemType(Names.GEAR, ItemMMDGear.class, Options.isThingEnabled(ConfigKeys.GEAR), Oredicts.GEAR);

		addItemType(Names.CASING, GenericMMDItem.class, Options.enableModderSupportThings(), Oredicts.CASING);
		addItemType(Names.DENSE_PLATE, GenericMMDItem.class, Options.enableModderSupportThings(), Oredicts.PLATE_DENSE);

		addItemType(Names.CRUSHED, GenericMMDItem.class, Options.isModEnabled(IC2Base.PLUGIN_MODID), Oredicts.CRUSHED);
		addItemType(Names.CRUSHED_PURIFIED, GenericMMDItem.class, Options.isModEnabled(IC2Base.PLUGIN_MODID), Oredicts.CRUSHED_PURIFIED);

		addItemType(Names.SHARD, GenericMMDItem.class, Options.isModEnabled(MekanismBase.PLUGIN_MODID), Oredicts.SHARD);
		addItemType(Names.CLUMP, GenericMMDItem.class, Options.isModEnabled(MekanismBase.PLUGIN_MODID), Oredicts.CLUMP);
		addItemType(Names.POWDER_DIRTY, GenericMMDItem.class, Options.isModEnabled(MekanismBase.PLUGIN_MODID), Oredicts.DUST_DIRTY);

		try {
			expandCombatArrays(net.minecraft.item.ItemAxe.class);
		} catch (IllegalAccessException | NoSuchFieldException ex) {
			BaseMetals.logger.error("Error modifying item classes", ex);
		}

		setSortingList();
		addToMetList();
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
		classSortingValues.put(ItemMMDSickle.class, ++ss * 10000);
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

	protected static Item create(@Nonnull final Names name, @Nonnull final String materialName,
			final CreativeTabs tab) {
		return create(name, Materials.getMaterialByName(materialName), tab);
	}

	/**
	 * 
	 * @param name
	 *            Name of the requested item type
	 * @param material
	 *            The material this is made from
	 * @param tab
	 *            which creative tab is it on
	 * @return the block this function created
	 */
	protected static Item create(@Nonnull final Names name, @Nonnull final MMDMaterial material,
			final CreativeTabs tab) {
		if (sanityCheck(name, material)) {
			return null;
		}

		if (material.hasItem(name)) {
			return material.getItem(name);
		}

		if (isArmor(name)) {
			return createArmorItem(name, material, tab);
		}

		final Item item = createItem(material, name.toString(), getClassFromName(name), isNameEnabled(name), tab);

		final String oredict = getOredictFromName(name);
		setupOredict(item, oredict, name, material);

		return item;
	}

	private static boolean sanityCheck(Names name, MMDMaterial material) {
		return ((material == null) || (name == null) || isWrongThingToMake(name, material));
	}

	private static void setupOredict(Item item, String oredict, Names name, MMDMaterial material) {
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
	}

	private static boolean isWrongThingToMake(Names name, MMDMaterial material) {
		return ((((name.equals(Names.BLEND)) || (name.equals(Names.SMALLBLEND))) && (!material.hasBlend()))
				|| (name.equals(Names.ANVIL) && (!material.hasBlock(Names.ANVIL)))
				|| (name.equals(Names.DOOR) && (!material.hasBlock(Names.DOOR))) || (name.equals(Names.SLAB)
						&& (!material.hasBlock(Names.SLAB) && (!material.hasBlock(Names.DOUBLE_SLAB)))));
	}

	private static boolean isArmor(Names name) {
		return ((name.equals(Names.HELMET)) || (name.equals(Names.CHESTPLATE)) || (name.equals(Names.LEGGINGS))
				|| (name.equals(Names.BOOTS)));
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
	protected static Item addItem(@Nonnull final Item item, @Nonnull final String name, final MMDMaterial material,
			final CreativeTabs tab) {
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

	private static Item createItem(@Nonnull final MMDMaterial material, @Nonnull final String name,
			@Nonnull final Class<? extends Item> clazz, @Nonnull final boolean enabled, final CreativeTabs tab) {
		if (material.hasItem(name)) {
			return material.getItem(name);
		}

		if (enabled) {
			Constructor<?> ctor = null;
			Item inst = null;

			try {
				ctor = clazz.getConstructor(material.getClass());
			} catch (NoSuchMethodException | SecurityException ex) {
				BaseMetals.logger.error("Class for Item named %s does not have the correct constructor", name, ex);
				return null;
			}

			try {
				inst = (Item) ctor.newInstance(material);
			} catch (IllegalAccessException | IllegalArgumentException | InstantiationException
					| InvocationTargetException | ExceptionInInitializerError ex) {
				BaseMetals.logger.error("Unable to create new instance of Item class for item name %s of material %s",
						name, material.getCapitalizedName(), ex);
				return null;
			} catch (Exception ex) {
				BaseMetals.logger.error("Unable to create Item named %s for material %s", name,
						material.getCapitalizedName(), ex);
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

	private static Item createArmorItem(@Nonnull final Names name, @Nonnull final MMDMaterial material,
			final CreativeTabs tab) {
		if (!(isNameEnabled(name))) {
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
	 * @param material
	 *            The material base of this item
	 * @param tab
	 *            which creative tab it is in
	 * @return the item this function created
	 */
	protected static Item createMekCrystal(@Nonnull final MMDMaterial material, final CreativeTabs tab) {
		if (material.hasItem(Names.CRYSTAL)) {
			return material.getItem(Names.CRYSTAL);
		}

		final Item item = createItem(material, Names.CRYSTAL.toString(), GenericMMDItem.class,
				(Options.isModEnabled(MekanismBase.PLUGIN_MODID) && material.getType() != MaterialType.CRYSTAL), tab);
		if (item != null) {
			Oredicts.registerOre(Oredicts.CRYSTAL + material.getCapitalizedName(), item);
			return item;
		}

		return null;
	}

	/**
	 * Uses reflection to expand the size of the combat damage and attack speed
	 * arrays to prevent initialization index-out-of-bounds errors
	 *
	 * @param itemClass
	 *            The class to modify
	 */
	private static void expandCombatArrays(@Nonnull final Class<?> itemClass)
			throws IllegalAccessException, NoSuchFieldException {
		// WARNING: this method contains black magic
		final int expandedSize = 256;
		final Field[] fields = itemClass.getDeclaredFields();
		for (final Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()) && field.getType().isArray()
					&& field.getType().getComponentType().equals(float.class)) {
				BaseMetals.logger.info("%s: Expanding array variable %s.%s to size %d",
						Thread.currentThread().getStackTrace()[0].toString(), itemClass.getSimpleName(),
						field.getName(), expandedSize);
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
		if ((itemStack.getItem() instanceof ItemBlock)
				&& (((ItemBlock) itemStack.getItem()).getBlock() instanceof IMMDObject)) {
			classVal = classSortingValues.computeIfAbsent(((ItemBlock) itemStack.getItem()).getBlock().getClass(),
					(Class<?> clazz) -> 990000);
			materialVal = materialSortingValues.computeIfAbsent(
					((IMMDObject) ((ItemBlock) itemStack.getItem()).getBlock()).getMMDMaterial(),
					(MMDMaterial material) -> 9900);
		} else if (itemStack.getItem() instanceof IMMDObject) {
			classVal = classSortingValues.computeIfAbsent(itemStack.getItem().getClass(), (Class<?> clazz) -> 990000);
			materialVal = materialSortingValues.computeIfAbsent(((IMMDObject) itemStack.getItem()).getMMDMaterial(),
					(MMDMaterial material) -> 9900);
		}
		return classVal + materialVal + (itemStack.getMetadata() % 100);
	}

	protected static Class<? extends Item> getClassFromName(@Nonnull final Names name) {
		if (nameToClass.containsKey(name)) {
			return nameToClass.get(name);
		}
		return net.minecraft.item.Item.class;
	}

	protected static String getOredictFromName(@Nonnull final Names name) {
		if (nameToOredict.containsKey(name)) {
			return nameToOredict.get(name);
		}
		return null;
	}

	protected static boolean isNameEnabled(@Nonnull final Names name) {
		if (nameToEnabled.containsKey(name)) {
			return nameToEnabled.get(name);
		}
		return false;
	}

	protected static void addItemType(@Nonnull final Names name, @Nonnull final Class<? extends Item> clazz,
			@Nonnull final Boolean enabled) {
		addItemType(name, clazz, enabled, null);
	}

	protected static void addItemType(@Nonnull final Names name, @Nonnull final Class<? extends Item> clazz,
			@Nonnull final Boolean enabled, final String oredict) {
		if (!nameToClass.containsKey(name)) {
			nameToClass.put(name, clazz);
		}

		if (!nameToEnabled.containsKey(name)) {
			nameToEnabled.put(name, enabled);
		}

		if (!nameToOredict.containsKey(name)) {
			nameToOredict.put(name, oredict);
		}
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
	 * @return The name of the item, or null if the item is not a Base Metals item.
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
