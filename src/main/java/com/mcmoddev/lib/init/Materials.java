package com.mcmoddev.lib.init;

import java.util.*;

import com.mcmoddev.basemetals.material.MetalMaterial;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLLog;

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

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		initDone = true;
	}

	/*
	 * Create a oreless material
	 */
	protected static MetalMaterial createOrelessMaterial(String name, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial m = new MetalMaterial(name, (float) hardness, (float) strength, (float) magic, tintColor, false, false, false);

		return registerMaterial(name, m);
	}

	/*
	 * Create a standard material
	 */
	protected static MetalMaterial createMaterial(String name, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial m = new MetalMaterial(name, (float) hardness, (float) strength, (float) magic, tintColor, false, true, false);

		return registerMaterial(name, m);
	}

	/*
	 * Create an alloy material
	 */
	protected static MetalMaterial createAlloyMaterial(String name, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial m = new MetalMaterial(name, (float) hardness, (float) strength, (float) magic, tintColor, false, false, true);

		return registerMaterial(name, m);
	}

	/*
	 * Create a special alloy material which has an ore block
	 */
	protected static MetalMaterial createSpecialMaterial(String name, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial m = new MetalMaterial(name, (float) hardness, (float) strength, (float) magic, tintColor, false, true, true);

		return registerMaterial(name, m);
	}

	/*
	 * Create a rare, oreless  material
	 */
	protected static MetalMaterial createOrelessRareMaterial(String name, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial m = new MetalMaterial(name, (float) hardness, (float) strength, (float) magic, tintColor, true, false, false);

		return registerMaterial(name, m);
	}

	/*
	 * Create a rare material
	 */
	protected static MetalMaterial createRareMaterial(String name, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial m = new MetalMaterial(name, (float) hardness, (float) strength, (float) magic, tintColor, true, true, false);

		return registerMaterial(name, m);
	}

	/*
	 * Create a rare alloy material
	 */
	protected static MetalMaterial createRareAlloyMaterial(String name, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial m = new MetalMaterial(name, (float) hardness, (float) strength, (float) magic, tintColor, true, false, true);

		return registerMaterial(name, m);
	}

	/*
	 * Create a special rare alloy material
	 */
	protected static MetalMaterial createRareSpecialMaterial(String name, double hardness, double strength, double magic, int tintColor) {
		final MetalMaterial m = new MetalMaterial(name, (float) hardness, (float) strength, (float) magic, tintColor, true, true, true);

		return registerMaterial(name, m);
	}

	protected static MetalMaterial registerMaterial(String name, MetalMaterial m) {

		allMaterials.put(name, m);
		materials.add(m);

		final String enumName = m.getEnumName();
		final String texName = m.getName();
		final int[] protection = m.getDamageReductionArray();
		final int durability = m.getArmorMaxDamageFactor();
		final ArmorMaterial am = EnumHelper.addArmorMaterial(enumName, texName, durability, protection, m.getEnchantability(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, m.hardness > 10 ? (int) (m.hardness / 5) : 0);
		if (am == null) {
			// uh-oh
			FMLLog.severe("Failed to create armor material enum for " + m);
		}
		armorMaterialMap.put(m, am);

		final ToolMaterial tm = EnumHelper.addToolMaterial(enumName, m.getToolHarvestLevel(), m.getToolDurability(), m.getToolEfficiency(), m.getBaseAttackDamage(), m.getEnchantability());
		if (tm == null) {
			// uh-oh
			FMLLog.severe("Failed to create tool material enum for " + m);
		}
		toolMaterialMap.put(m, tm);

		return m;
	}

	/**
	 * Gets the armor material for a given metal
	 *
	 * @param m
	 *            The metal of interest
	 * @return The armor material for this metal, or null if there isn't one
	 */
	public static ArmorMaterial getArmorMaterialFor(MetalMaterial m) {
		return armorMaterialMap.get(m);
	}

	/**
	 * Gets the tool material for a given metal
	 *
	 * @param m
	 *            The metal of interest
	 * @return The tool material for this metal, or null if there isn't one
	 */
	public static ToolMaterial getToolMaterialFor(MetalMaterial m) {
		return toolMaterialMap.get(m);
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
	 * @param metalName
	 *            The name of a metal
	 * 
	 * @deprecated
	 * @return The material representing the named metal, or null if no metals
	 *         have been registered under that name.
	 */
	@Deprecated
	public static MetalMaterial getMetalByName(String metalName) {
		return allMaterials.get(metalName);
	}
}
