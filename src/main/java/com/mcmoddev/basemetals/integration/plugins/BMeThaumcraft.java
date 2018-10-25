package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.Thaumcraft;
import com.mcmoddev.lib.integration.plugins.thaumcraft.TCSyncEvent;
import com.mcmoddev.lib.material.MMDMaterialType;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = BMeThaumcraft.PLUGIN_MODID, versions = BMeThaumcraft.PLUGIN_MODID
		+ "@[1.12-6.1.BETA,)")
public final class 	BMeThaumcraft implements IIntegration {

	public static final String PLUGIN_MODID = Thaumcraft.PLUGIN_MODID;

	public BMeThaumcraft() {
		if (!Config.Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		
		MinecraftForge.EVENT_BUS.register(this.getClass());
	}

	@Override
	public void init() {
		Thaumcraft.INSTANCE.init();
		if (!Config.Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		
	}

	@SubscribeEvent
	public static void registerAspects(final TCSyncEvent ev){
		Materials.getAllMaterials().stream()
				.filter( mat -> !mat.isVanilla())
				.filter( mat -> !mat.isEmpty())
				.forEach( mat -> ev.register(Thaumcraft.createWithAspects(mat)));

		Materials.getAllMaterials().stream()
				.filter( mat -> mat.isVanilla())
				.filter( mat -> !mat.isEmpty())
				.filter( mat -> mat.getType() == MMDMaterialType.MaterialType.MINERAL
						|| mat.getType() == MMDMaterialType.MaterialType.ROCK)
				.forEach( mat -> ev.register(Thaumcraft.createVanillaIngotWithAspects(mat)));
	}
}
