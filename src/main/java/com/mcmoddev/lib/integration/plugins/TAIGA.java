package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.integration.IIntegration;

import net.minecraftforge.fml.common.FMLLog;

public class TAIGA implements IIntegration {

	public static final String PLUGIN_MODID = "taiga";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableTAIGA) {
			if (!com.mcmoddev.basemetals.util.Config.Options.enableTinkersConstruct) {
				FMLLog.severe("BASEMETALS TAIGA Plugin requires the TinkersConstruct Plugin");
			}
			return;
		}

		initDone = true;
	}

}
