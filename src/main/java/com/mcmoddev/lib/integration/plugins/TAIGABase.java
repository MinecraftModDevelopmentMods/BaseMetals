package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.IIntegration;

public class TAIGABase implements IIntegration {

	public static final String PLUGIN_MODID = "taiga";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled(PLUGIN_MODID)) {
			if (!com.mcmoddev.basemetals.util.Config.Options.modEnabled(TinkersConstructBase.PLUGIN_MODID)) {
				BaseMetals.logger.error("TAIGA Plugin requires the TinkersConstruct Plugin");
			}
			return;
		}

		initDone = true;
	}

}
