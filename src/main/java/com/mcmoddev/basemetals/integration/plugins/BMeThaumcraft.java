package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.Thaumcraft;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;

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
}
