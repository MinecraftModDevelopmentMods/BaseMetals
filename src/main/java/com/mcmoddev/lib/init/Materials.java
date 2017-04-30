package com.mcmoddev.lib.init;

import java.util.*;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;

/**
 * This class initializes all of the materials in Base Metals. It also contains
 * utility methods for looking up materials by name and finding the tool and
 * armor material equivalents for a given material.
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Materials {

	private static boolean initDone = false;

	private static Map<String, MMDMaterial> allMaterials = new HashMap<>();
	private static Map<MMDMaterial, ArmorMaterial> armorMaterialMap = new HashMap<>();
	private static Map<MMDMaterial, ToolMaterial> toolMaterialMap = new HashMap<>();
	private static Map<String, Set<MMDMaterial>> modSourceMaterialMap = new HashMap<>();
	
	protected Materials() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		initDone = true;
	}

	/**
	 * Create a oreless material
	 * 
	 * @param name Name of the material
	 * @param type the type of the material (metal, gem, mineral, etc...)
	 * @param hardness Scaled hardness of the material, based on the Mohs scale
	 * @param strength material strength
	 * @param magic material magic affinity
	 * @param tintColor material tint color - used in several places, including in the TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createOrelessMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, false, false);

		return registerMaterial(material);
	}

	/**
	 * Create a standard material
	 * 
	 * @param name Name of the material
	 * @param type the type of the material (metal, gem, mineral, etc...)
	 * @param hardness Scaled hardness of the material, based on the Mohs scale
	 * @param strength material strength
	 * @param magic material magic affinity
	 * @param tintColor material tint color - used in several places, including in the TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, true, false);

		return registerMaterial(material);
	}

	/**
	 * Create an alloy material
	 * 
	 * @param name Name of the material
	 * @param type the type of the material (metal, gem, mineral, etc...)
	 * @param hardness Scaled hardness of the material, based on the Mohs scale
	 * @param strength material strength
	 * @param magic material magic affinity
	 * @param tintColor material tint color - used in several places, including in the TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createAlloyMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, false, true);

		return registerMaterial(material);
	}

	/**
	 * Create a special alloy material which has an ore block
	 * 
	 * @param name Name of the material
	 * @param type the type of the material (metal, gem, mineral, etc...)
	 * @param hardness Scaled hardness of the material, based on the Mohs scale
	 * @param strength material strength
	 * @param magic material magic affinity
	 * @param tintColor material tint color - used in several places, including in the TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createSpecialMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, true, true);

		return registerMaterial(material);
	}

	/**
	 * Create a rare, oreless material
	 * 
	 * @param name Name of the material
	 * @param type the type of the material (metal, gem, mineral, etc...)
	 * @param hardness Scaled hardness of the material, based on the Mohs scale
	 * @param strength material strength
	 * @param magic material magic affinity
	 * @param tintColor material tint color - used in several places, including in the TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createOrelessRareMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, false, false);

		return registerMaterial(material);
	}

	/**
	 * Create a rare material
	 * 
	 * @param name Name of the material
	 * @param type the type of the material (metal, gem, mineral, etc...)
	 * @param hardness Scaled hardness of the material, based on the Mohs scale
	 * @param strength material strength
	 * @param magic material magic affinity
	 * @param tintColor material tint color - used in several places, including in the TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createRareMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, true, false);

		return registerMaterial(material);
	}

	/**
	 * Create a rare alloy material
	 * 
	 * @param name Name of the material
	 * @param type the type of the material (metal, gem, mineral, etc...)
	 * @param hardness Scaled hardness of the material, based on the Mohs scale
	 * @param strength material strength
	 * @param magic material magic affinity
	 * @param tintColor material tint color - used in several places, including in the TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createRareAlloyMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, false, true);

		return registerMaterial(material);
	}

	/**
	 * Create a special rare alloy material
	 * 
	 * @param name Name of the material
	 * @param type the type of the material (metal, gem, mineral, etc...)
	 * @param hardness Scaled hardness of the material, based on the Mohs scale
	 * @param strength material strength
	 * @param magic material magic affinity
	 * @param tintColor material tint color - used in several places, including in the TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createRareSpecialMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, true, true);

		return registerMaterial(material);
	}

	/**
	 * Register a material
	 * 
	 * @param material the material to register
	 * @return the material
	 */
	protected static MMDMaterial registerMaterial(MMDMaterial material) {

		if (material == null) {
			BaseMetals.logger.error("Null material passed to registermaterial(), Don't do that!");
			return null;
		}

		if (Materials.getAllMaterials().contains(material)) {
			BaseMetals.logger.error("You asked registermaterial() to register an existing material, Don't do that! (Returning pre existing material instead");
			return Materials.getMaterialByName(material.getName());
		}

		allMaterials.put(material.getName(), material);

		final String enumName = material.getEnumName();
		final String texName = material.getName();
		final int[] protection = material.getDamageReductionArray();
		final int durability = material.getArmorMaxDamageFactor();
		final ArmorMaterial armorMaterial = EnumHelper.addArmorMaterial(enumName, texName, durability, protection, material.getEnchantability(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, material.getStat(MaterialStats.HARDNESS) > 10 ? (int) (material.getStat(MaterialStats.HARDNESS) / 5) : 0);
		if (armorMaterial == null) {
			// uh-oh
			BaseMetals.logger.error("Failed to create armor material enum for " + material);
		}
		armorMaterialMap.put(material, armorMaterial);

		final ToolMaterial toolMaterial = EnumHelper.addToolMaterial(enumName, material.getToolHarvestLevel(), material.getToolDurability(), material.getToolEfficiency(), material.getBaseAttackDamage(), material.getEnchantability());
		if (toolMaterial == null) {
			// uh-oh
			BaseMetals.logger.error("Failed to create tool material enum for " + material);
		}
		toolMaterialMap.put(material, toolMaterial);

		if (modSourceMaterialMap.containsKey(Loader.instance().activeModContainer().getModId())) {
			modSourceMaterialMap.get(Loader.instance().activeModContainer().getModId()).add(material);
		} else {
			Set<MMDMaterial> newSet = new HashSet<>();
			newSet.add(material);
			modSourceMaterialMap.put(Loader.instance().activeModContainer().getModId(), newSet);
			
		}
		return material;
	}

	/**
	 * Gets the armor material for a given metal
	 *
	 * @param material
	 *            The metal of interest
	 * @return The armor material for this metal, or null if there isn't one
	 */
	public static ArmorMaterial getArmorMaterialFor(MMDMaterial material) {
		return armorMaterialMap.get(material);
	}

	/**
	 * Gets the tool material for a given metal
	 *
	 * @param material
	 *            The metal of interest
	 * @return The tool material for this metal, or null if there isn't one
	 */
	public static ToolMaterial getToolMaterialFor(MMDMaterial material) {
		return toolMaterialMap.get(material);
	}

	/**
	 * Returns a list of all materials in Base Metals. All of the materials in
	 * this list are also available as static public members of this class.
	 *
	 * @return A Collection of MetalMaterial instances.
	 */
	public static Collection<MMDMaterial> getAllMaterials() {
		return allMaterials.values();
	}

	/**
	 * Gets a material by its name (e.g. "copper").
	 *
	 * @param materialName
	 *            The name of a material
	 * @return The material representing the named material, or null if no
	 *         materials have been registered under that name.
	 */
	public static MMDMaterial getMaterialByName(String materialName) {
		return allMaterials.get(materialName);
	}

	/**
	 * Returns a list of all metal materials in Base Metals. All of the metals
	 * in this list are also available as static public members of this class.
	 * 
	 * @deprecated
	 * @return A Collection of MetalMaterial instances.
	 */
	@Deprecated
	public static Collection<MMDMaterial> getAllMetals() {
		return allMaterials.values();
	}

	/**
	 * Gets a metal material by its name (e.g. "copper").
	 *
	 * @param materialName
	 *            The name of a metal
	 * 
	 * @deprecated
	 * @return The material representing the named metal, or null if no metals
	 *         have been registered under that name.
	 */
	@Deprecated
	public static MMDMaterial getMetalByName(String materialName) {
		return allMaterials.get(materialName);
	}
	
	/**
	 * Gets all materials from a given mod
	 * 
	 * @param modId the ModID of the mod
	 * @return an immutable collection representing all the materials registered by a given mod
	 *         or the "empty set" if the modId is not recorded.
	 */
	public static Collection<MMDMaterial> getMaterialsByMod(String modId) {
		if (modSourceMaterialMap.containsKey(modId)) {
			return Collections.unmodifiableSet(modSourceMaterialMap.get(modId));
		} else {
			return Collections.emptySet();
		}
	}
}
