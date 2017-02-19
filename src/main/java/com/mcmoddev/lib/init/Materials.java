package com.mcmoddev.lib.init;

import java.util.*;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.material.MetalMaterial.MaterialType;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

/**
 * This class initializes all of the materials in Base Metals. It also
 * contains utility methods for looking up materials by name and finding the
 * tool and armor material equivalents for a given material.
 *
 * @author DrCyano
 *
 */
public abstract class Materials {

	private static boolean initDone = false;

	private static Map<String, MetalMaterial> allMaterials = new HashMap<>();
	private static Map<MetalMaterial, ArmorMaterial> armorMaterialMap = new HashMap<>();
	private static Map<MetalMaterial, ToolMaterial> toolMaterialMap = new HashMap<>();
	private static List<MetalMaterial> materials = new LinkedList<>();

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
	 * @param name
	 * @param hardness
	 * @param strength
	 * @param magic
	 * @param tintColor
	 * @return
	 */
	protected static MetalMaterial createOrelessMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial material = new MetalMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, false, false);

		return registerMaterial(material);
	}

	/**
	 * Create a standard material
	 * 
	 * @param name
	 * @param hardness
	 * @param strength
	 * @param magic
	 * @param tintColor
	 * @return
	 */
	protected static MetalMaterial createMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial material = new MetalMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, true, false);

		return registerMaterial(material);
	}

	/**
	 * Create an alloy material
	 * 
	 * @param name
	 * @param hardness
	 * @param strength
	 * @param magic
	 * @param tintColor
	 * @return
	 */
	protected static MetalMaterial createAlloyMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial material = new MetalMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, false, true);

		return registerMaterial(material);
	}

	/**
	 * Create a special alloy material which has an ore block
	 * 
	 * @param name
	 * @param hardness
	 * @param strength
	 * @param magic
	 * @param tintColor
	 * @return
	 */
	protected static MetalMaterial createSpecialMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial material = new MetalMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, true, true);

		return registerMaterial(material);
	}

	/**
	 * Create a rare, oreless  material
	 * 
	 * @param name
	 * @param hardness
	 * @param strength
	 * @param magic
	 * @param tintColor
	 * @return
	 */
	protected static MetalMaterial createOrelessRareMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial material = new MetalMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, false, false);

		return registerMaterial(material);
	}

	/**
	 * Create a rare material
	 * 
	 * @param name
	 * @param hardness
	 * @param strength
	 * @param magic
	 * @param tintColor
	 * @return
	 */
	protected static MetalMaterial createRareMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial material = new MetalMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, true, false);

		return registerMaterial(material);
	}

	/**
	 * Create a rare alloy material
	 * 
	 * @param name
	 * @param hardness
	 * @param strength
	 * @param magic
	 * @param tintColor
	 * @return
	 */
	protected static MetalMaterial createRareAlloyMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial material = new MetalMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, false, true);

		return registerMaterial(material);
	}

	/**
	 * Create a special rare alloy material
	 * 
	 * @param name
	 * @param hardness
	 * @param strength
	 * @param magic
	 * @param tintColor
	 * @return
	 */
	protected static MetalMaterial createRareSpecialMaterial(String name, MaterialType type, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial material = new MetalMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, true, true);

		return registerMaterial(material);
	}


	/**
	 * Register a material
	 * 
	 * @param material
	 * @return
	 */
	protected static MetalMaterial registerMaterial(MetalMaterial material) {

		if (material == null) {
			BaseMetals.logger.error("Null material passed to registermaterial(), Don't do that!");
			return (MetalMaterial) null;
		}

		if (Materials.getAllMaterials().contains(material)) {
			BaseMetals.logger.error("You asked registermaterial() to register an existing material, Don't do that! (Returning pre existing material instead");
			return Materials.getMaterialByName(material.getName());
		}

		allMaterials.put(material.getName(), material);
		materials.add(material);

		final String enumName = material.getEnumName();
		final String texName = material.getName();
		final int[] protection = material.getDamageReductionArray();
		final int durability = material.getArmorMaxDamageFactor();
		final ArmorMaterial armorMaterial = EnumHelper.addArmorMaterial(enumName, texName, durability, protection, material.getEnchantability(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, material.hardness > 10 ? (int) (material.hardness / 5) : 0);
		if (armorMaterial == null) {
			// uh-oh
			BaseMetals.logger.error("Failed to create armor material enum for " + material);
//			FMLLog.severe("Failed to create armor material enum for " + material);
		}
		armorMaterialMap.put(material, armorMaterial);

		final ToolMaterial toolMaterial = EnumHelper.addToolMaterial(enumName, material.getToolHarvestLevel(), material.getToolDurability(), material.getToolEfficiency(), material.getBaseAttackDamage(), material.getEnchantability());
		if (toolMaterial == null) {
			// uh-oh
			BaseMetals.logger.error("Failed to create tool material enum for " + material);
//			FMLLog.severe("Failed to create tool material enum for " + material);
		}
		toolMaterialMap.put(material, toolMaterial);

		return material;
	}

	/**
	 * Gets the armor material for a given metal
	 *
	 * @param m
	 *            The metal of interest
	 * @return The armor material for this metal, or null if there isn't one
	 */
	public static ArmorMaterial getArmorMaterialFor(MetalMaterial material) {
		return armorMaterialMap.get(material);
	}

	/**
	 * Gets the tool material for a given metal
	 *
	 * @param m
	 *            The metal of interest
	 * @return The tool material for this metal, or null if there isn't one
	 */
	public static ToolMaterial getToolMaterialFor(MetalMaterial material) {
		return toolMaterialMap.get(material);
	}

	/**
	 * Returns a list of all materials in Base Metals. All of the materials
	 * in this list are also available as static public members of this class.
	 *
	 * @return A Collection of MetalMaterial instances.
	 */
	public static Collection<MetalMaterial> getAllMaterials() {
		return allMaterials.values();
	}

	/**
	 * Gets a material by its name (e.g. "copper").
	 *
	 * @param materialName
	 *            The name of a material
	 * @return The material representing the named material, or null if no materials
	 *         have been registered under that name.
	 */
	public static MetalMaterial getMaterialByName(String materialName) {
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
	public static Collection<MetalMaterial> getAllMetals() {
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
	public static MetalMaterial getMetalByName(String materialName) {
		return allMaterials.get(materialName);
	}
}
