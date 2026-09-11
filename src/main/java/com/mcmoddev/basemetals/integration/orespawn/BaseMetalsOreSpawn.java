package com.mcmoddev.basemetals.integration.orespawn;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.orespawn.api.os3.OS3API;
import com.mcmoddev.orespawn.api.plugin.IOreSpawnPlugin;
import com.mcmoddev.orespawn.api.plugin.OreSpawnPlugin;

/**
 * Declares Base Metals' packaged OreSpawn 3 data.
 *
 * <p>The plugin deliberately lives outside {@code com.mcmoddev.orespawn}.
 * OreSpawn 3 releases sign that package, so putting an unsigned add-on class
 * in it causes the JVM's package signer check to reject the integration.</p>
 */
@OreSpawnPlugin(modid = BaseMetals.MODID, resourcePath = SharedStrings.ORESPAWN_MODID)
public class BaseMetalsOreSpawn implements IOreSpawnPlugin {

	@Override
	public void register(final OS3API apiInterface) {
		// The annotation points OreSpawn at the packaged JSON configuration.
	}
}
