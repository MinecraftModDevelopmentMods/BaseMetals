package com.mcmoddev.lib.init;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;



/**
 * This class initializes all of the materials in Base Metals. It also contains
 * utility methods for looking up materials by name and finding the tool and
 * armor material equivalents for a given material.
 *
 * @author Jasmine Iwanek
 *
 */
public class Materials {

	private static boolean initDone = false;

	private final IForgeRegistry<MMDMaterial> REGISTRY;
	public final static Materials instance = new Materials();
	private static final Map<MMDMaterial, ArmorMaterial> armorMaterialMap = new HashMap<>();
	private static final Map<MMDMaterial, ToolMaterial> toolMaterialMap = new HashMap<>();

	protected Materials() {
		this.REGISTRY = new RegistryBuilder<MMDMaterial>()
				.setName(new ResourceLocation("mmdlib","materials_registry"))
			    .setType(MMDMaterial.class)
			    .setMaxID(Integer.MAX_VALUE >> 4)
			    .create();
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
	 *            Name of the material
	 * @param type
	 *            the type of the material (metal, gem, mineral, etc...)
	 * @param hardness
	 *            Scaled hardness of the material, based on the Mohs scale
	 * @param strength
	 *            material strength
	 * @param magic
	 *            material magic affinity
	 * @param tintColor
	 *            material tint color - used in several places, including in the
	 *            TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createOrelessMaterial(@Nonnull final String name, @Nonnull final MaterialType type, @Nonnull final double hardness, @Nonnull final double strength, @Nonnull final double magic, @Nonnull final int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, false, false);

		return registerMaterial(material);
	}

	/**
	 * Create a standard material
	 * 
	 * @param name
	 *            Name of the material
	 * @param type
	 *            the type of the material (metal, gem, mineral, etc...)
	 * @param hardness
	 *            Scaled hardness of the material, based on the Mohs scale
	 * @param strength
	 *            material strength
	 * @param magic
	 *            material magic affinity
	 * @param tintColor
	 *            material tint color - used in several places, including in the
	 *            TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createMaterial(@Nonnull final String name, @Nonnull final MaterialType type, @Nonnull final double hardness, @Nonnull final double strength, @Nonnull final double magic, @Nonnull final int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, true, false);

		return registerMaterial(material);
	}

	/**
	 * Create an alloy material
	 * 
	 * @param name
	 *            Name of the material
	 * @param type
	 *            the type of the material (metal, gem, mineral, etc...)
	 * @param hardness
	 *            Scaled hardness of the material, based on the Mohs scale
	 * @param strength
	 *            material strength
	 * @param magic
	 *            material magic affinity
	 * @param tintColor
	 *            material tint color - used in several places, including in the
	 *            TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createAlloyMaterial(@Nonnull final String name, @Nonnull final MaterialType type, @Nonnull final double hardness, @Nonnull final double strength, @Nonnull final double magic, @Nonnull final int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, false, true);

		return registerMaterial(material);
	}

	/**
	 * Create a special alloy material which has an ore block
	 * 
	 * @param name
	 *            Name of the material
	 * @param type
	 *            the type of the material (metal, gem, mineral, etc...)
	 * @param hardness
	 *            Scaled hardness of the material, based on the Mohs scale
	 * @param strength
	 *            material strength
	 * @param magic
	 *            material magic affinity
	 * @param tintColor
	 *            material tint color - used in several places, including in the
	 *            TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createSpecialMaterial(@Nonnull final String name, @Nonnull final MaterialType type, @Nonnull final double hardness, @Nonnull final double strength, @Nonnull final double magic, @Nonnull final int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, false, true, true);

		return registerMaterial(material);
	}

	/**
	 * Create a rare, oreless material
	 * 
	 * @param name
	 *            Name of the material
	 * @param type
	 *            the type of the material (metal, gem, mineral, etc...)
	 * @param hardness
	 *            Scaled hardness of the material, based on the Mohs scale
	 * @param strength
	 *            material strength
	 * @param magic
	 *            material magic affinity
	 * @param tintColor
	 *            material tint color - used in several places, including in the
	 *            TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createOrelessRareMaterial(@Nonnull final String name, @Nonnull final MaterialType type, @Nonnull final double hardness, @Nonnull final double strength, @Nonnull final double magic, @Nonnull final int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, false, false);

		return registerMaterial(material);
	}

	/**
	 * Create a rare material
	 * 
	 * @param name
	 *            Name of the material
	 * @param type
	 *            the type of the material (metal, gem, mineral, etc...)
	 * @param hardness
	 *            Scaled hardness of the material, based on the Mohs scale
	 * @param strength
	 *            material strength
	 * @param magic
	 *            material magic affinity
	 * @param tintColor
	 *            material tint color - used in several places, including in the
	 *            TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createRareMaterial(@Nonnull final String name, @Nonnull final MaterialType type, @Nonnull final double hardness, @Nonnull final double strength, @Nonnull final double magic, @Nonnull final int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, true, false);

		return registerMaterial(material);
	}

	/**
	 * Create a rare alloy material
	 * 
	 * @param name
	 *            Name of the material
	 * @param type
	 *            the type of the material (metal, gem, mineral, etc...)
	 * @param hardness
	 *            Scaled hardness of the material, based on the Mohs scale
	 * @param strength
	 *            material strength
	 * @param magic
	 *            material magic affinity
	 * @param tintColor
	 *            material tint color - used in several places, including in the
	 *            TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createRareAlloyMaterial(@Nonnull final String name, @Nonnull final MaterialType type, final double hardness, @Nonnull final double strength, @Nonnull final double magic, @Nonnull final int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, false, true);

		return registerMaterial(material);
	}

	/**
	 * Create a special rare alloy material
	 * 
	 * @param name
	 *            Name of the material
	 * @param type
	 *            the type of the material (metal, gem, mineral, etc...)
	 * @param hardness
	 *            Scaled hardness of the material, based on the Mohs scale
	 * @param strength
	 *            material strength
	 * @param magic
	 *            material magic affinity
	 * @param tintColor
	 *            material tint color - used in several places, including in the
	 *            TiC plugin, where it determines tool-part color
	 * @return the new material
	 */
	protected static MMDMaterial createRareSpecialMaterial(@Nonnull final String name, @Nonnull final MaterialType type, @Nonnull final double hardness, @Nonnull final double strength, @Nonnull final double magic, @Nonnull final int tintColor) {
		final MMDMaterial material = new MMDMaterial(name, type, (float) hardness, (float) strength, (float) magic, tintColor, true, true, true);

		return registerMaterial(material);
	}

	/**
	 * Register a material
	 * 
	 * @param material
	 *            the material to register
	 * @return the material
	 */
	protected static MMDMaterial registerMaterial(@Nonnull final MMDMaterial material) {
		String modId = Loader.instance().activeModContainer().getModId();
		ResourceLocation loc = new ResourceLocation( modId, material.getName());
		if( instance.REGISTRY.containsKey(loc)) {
			BaseMetals.logger.error("You asked registermaterial() to register an existing material, Don't do that! (Returning pre existing material instead");
			return Materials.getMaterialByName(material.getName());
		}

		material.setRegistryName(loc);
		instance.REGISTRY.register(material);
		
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

		return material;
	}

	/**
	 * Gets the armor material for a given material
	 *
	 * @param material
	 *            The material of interest
	 * @return The armor material for this material, or null if there isn't one
	 */
	public static ArmorMaterial getArmorMaterialFor(@Nonnull final MMDMaterial material) {
		return armorMaterialMap.get(material);
	}

	/**
	 * Gets the tool material for a given material
	 *
	 * @param material
	 *            The metal of interest
	 * @return The tool material for this material, or null if there isn't one
	 */
	public static ToolMaterial getToolMaterialFor(@Nonnull final MMDMaterial material) {
		return toolMaterialMap.get(material);
	}

	/**
	 * Returns a list of all materials in Base Metals.
	 *
	 * @return A Collection of MMDMaterial instances.
	 */
	public static Collection<MMDMaterial> getAllMaterials() {
		return Collections.unmodifiableList(instance.REGISTRY.getValues());
	}

	public static final MMDMaterial emptyMaterial = createOrelessMaterial("empty",MaterialType.METAL,0,0,0,0);
	/**
	 * Gets a material by its name (e.g. "copper").
	 *
	 * @param materialName
	 *            The name of a material
	 * @return The material representing the named material, or null if no
	 *         materials have been registered under that name.
	 */
	public static @Nonnull MMDMaterial getMaterialByName(@Nonnull final String materialName) {
		for( ResourceLocation key : instance.REGISTRY.getKeys() ) {
			if( key.getResourcePath().equals(materialName)) {
				return instance.REGISTRY.getValue(key);
			}
		}
		return emptyMaterial;
	}

	/**
	 * Gets all materials from a given mod
	 * 
	 * @param modId
	 *            the ModID of the mod
	 * @return an immutable collection representing all the materials registered
	 *         by a given mod or the "empty set" if the modId is not recorded.
	 */
	public static Collection<MMDMaterial> getMaterialsByMod(@Nonnull final String modId) {
		return Lists.newArrayList(instance.REGISTRY.getEntries().stream()
				.filter((ent) -> ent.getKey().getResourceDomain().equals(modId) )
				.map((ent) -> ent.getValue()).iterator()); 
	}

	
	public static boolean hasMaterialFromMod(@Nonnull final String modId) {
		for( ResourceLocation rl : instance.REGISTRY.getKeys() ) {
			if( rl.getResourceDomain().equals(modId) ) {
				return true;
			}
		}
		return false;
	}
}
