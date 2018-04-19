package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerMaterial;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * TiC Plugin, redesigned.
 *
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 *
 */
public class TinkersConstructBase implements IIntegration {

	public static final String PLUGIN_MODID = "tconstruct";
	private static final IForgeRegistry<TinkerMaterial> registry = new RegistryBuilder<TinkerMaterial>()
			.disableSaving()
			.setMaxID(Integer.MAX_VALUE)
			.setName(new ResourceLocation("mmdlib", "tinker_registry"))
			.setType(TinkerMaterial.class)
			.create();

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
	}

	public TinkerMaterial newMaterial(@Nonnull MMDMaterial material) {
		return new TinkerMaterial(material);
	}
	
	public void registerMaterial(@Nonnull TinkerMaterial material) {
		String activeMod = Loader.instance().activeModContainer().getModId();
		ResourceLocation name = new ResourceLocation(activeMod, material.getName().toLowerCase());
		if(material.getRegistryName()==null)
			material.setRegistryName(name);
		registry.register(material);
	}
	
	public TinkerMaterial getMaterial(@Nonnull String name) {
		if (name.matches(":")) {
			return this.getMaterial(new ResourceLocation(name));
		}
		
		return this.getMaterial(Loader.instance().activeModContainer().getModId(), name);
	}
	
	public TinkerMaterial getMaterial(@Nonnull String modId, @Nonnull String name) {
		return this.getMaterial(new ResourceLocation(modId, name));
	}
	
	public TinkerMaterial getMaterial(@Nonnull ResourceLocation key) {
		return registry.getValue(key);
	}
	
	public void addExtraMelting(@Nonnull String materialName, @Nonnull String name, int amount) {
		this.addExtraMelting(this.getMaterial(materialName), name, amount);
	}
	
	public void addExtraMelting(@Nonnull String modId, @Nonnull String materialName, @Nonnull String name, int amount) {
		this.addExtraMelting(this.getMaterial(modId, materialName), name, amount);
	}
	
	public void addExtraMelting(@Nonnull ResourceLocation loc, @Nonnull String name, int amount) {
		this.addExtraMelting(this.getMaterial(loc), name, amount);
	}
	
	public void addExtraMelting(TinkerMaterial material, @Nonnull String name, int amount) {
		if(material == null) return;
		
		material.addExtraMelting(name, amount);
	}
	
	public void registerMaterial(@Nonnull String materialName, boolean craftable, boolean castable, Object...traits) {
		registerMaterial(materialName, craftable, castable, true, traits);
	}
	
	public void registerMaterial(@Nonnull String materialName, boolean craftable, boolean castable,
			boolean toolForge, Object...traits) {
		MMDMaterial material = Materials.getMaterialByName(materialName);
		TinkerMaterial mat = newMaterial(material);
		mat.setCastable(castable);
		mat.setCraftable(craftable);
		mat.setToolForge(toolForge);
		
		int i = 0;
		while (i < traits.length) {
			if (traits[i] instanceof TinkerMaterial.TinkersTraitLocation) {
				mat.addTrait((String)traits[i+1], (TinkerMaterial.TinkersTraitLocation)traits[i]);
				i++;
			} else {
				mat.addTrait((String)traits[i], TinkerMaterial.TinkersTraitLocation.GENERAL);
			}
			i++;
		}
		
		mat.setRegistryName(materialName);
		registerMaterial(mat);
	}
	
	public void registerMaterial(@Nonnull String materialName, boolean craftable, boolean castable, boolean toolForge) {
		MMDMaterial material = Materials.getMaterialByName(materialName);
		TinkerMaterial mat = newMaterial(material);
		mat.setCastable(castable);
		mat.setCraftable(craftable);
		mat.setToolForge(toolForge);
		
		mat.setRegistryName(materialName);
		registerMaterial(mat);		
	}
	
}
