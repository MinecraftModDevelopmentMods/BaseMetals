package com.mcmoddev.orespawn;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.orespawn.api.os3.OS3API;
import com.mcmoddev.orespawn.api.plugin.IOreSpawnPlugin;
import com.mcmoddev.orespawn.api.plugin.OreSpawnPlugin;

@OreSpawnPlugin(modid = BaseMetals.MODID, resourcePath = SharedStrings.ORESPAWN_MODID)
public class BaseMetalsOreSpawn implements IOreSpawnPlugin {

	@Override
	public void register(OS3API apiInterface) {
		// nothing for us to do - all of our ores are in the
		// jar and the code handles that
	}
}
