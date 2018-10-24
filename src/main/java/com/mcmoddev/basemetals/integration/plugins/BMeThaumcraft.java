package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.Thaumcraft;
import com.mcmoddev.lib.integration.plugins.thaumcraft.TCSyncEvent;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = BMeThaumcraft.PLUGIN_MODID)
public final class BMeThaumcraft extends com.mcmoddev.lib.integration.plugins.Thaumcraft {

	public static final String PLUGIN_MODID = Thaumcraft.PLUGIN_MODID;

	public BMeThaumcraft() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void init() {
		INSTANCE.init();
		if (!Config.Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void registerAspects(final TCSyncEvent ev){
		Materials.getAllMaterials().stream()
				.filter( mat -> !mat.isVanilla())
				.forEach( mat -> ev.register(createWithAspects(mat)));

		Materials.getAllMaterials().stream()
				.filter( mat -> mat.isVanilla())
				.forEach( mat -> ev.register(createPartsAspects(mat, Names.NUGGET.toString())));
	}
}
