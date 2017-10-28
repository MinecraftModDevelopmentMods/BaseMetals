package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.util.ConfigBase.Options;

public class TAIGABase implements IIntegration {

	public static final String PLUGIN_MODID = "taiga";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(PLUGIN_MODID)) {
			if (!Options.isModEnabled(TinkersConstructBase.PLUGIN_MODID)) {
				BaseMetals.logger.error("TAIGA Plugin requires the TinkersConstruct Plugin");
			}
			return;
		}

		initDone = true;
	}

}
