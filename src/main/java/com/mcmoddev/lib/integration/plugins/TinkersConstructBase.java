package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.tinkers.NewTCMaterial;
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
	private static final IForgeRegistry<NewTCMaterial> registry = new RegistryBuilder<NewTCMaterial>()
			.disableSaving()
			.setMaxID(Integer.MAX_VALUE)
			.setName(new ResourceLocation("mmdlib", "tinker_registry"))
			.setType(NewTCMaterial.class)
			.create();

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
	}

	public NewTCMaterial newMaterial(@Nonnull MMDMaterial material) {
		return new NewTCMaterial(material);
	}
	
	public void registerMaterial(@Nonnull NewTCMaterial material) {
		String activeMod = Loader.instance().activeModContainer().getModId();
		ResourceLocation name = new ResourceLocation(activeMod, material.getName().toLowerCase());
		if(material.getRegistryName()==null)
			material.setRegistryName(name);
		registry.register(material);
	}
	
	public NewTCMaterial getMaterial(@Nonnull String name) {
		if (name.matches(":")) {
			return this.getMaterial(new ResourceLocation(name));
		}
		
		return this.getMaterial(Loader.instance().activeModContainer().getModId(), name);
	}
	
	public NewTCMaterial getMaterial(@Nonnull String modId, @Nonnull String name) {
		return this.getMaterial(new ResourceLocation(modId, name));
	}
	
	public NewTCMaterial getMaterial(@Nonnull ResourceLocation key) {
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
	
	public void addExtraMelting(NewTCMaterial material, @Nonnull String name, int amount) {
		if(material == null) return;
		
		material.addExtraMelting(name, amount);
	}
}
