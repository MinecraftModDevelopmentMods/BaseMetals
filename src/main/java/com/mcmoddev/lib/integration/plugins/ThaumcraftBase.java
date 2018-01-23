package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.util.ConfigBase.Options;

public class ThaumcraftBase implements IIntegration {

	public static final String PLUGIN_MODID = "thaumcraft";

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
	}
}
