package com.mcmoddev.orespawn;

import com.mcmoddev.orespawn.api.os3.*;
import com.mcmoddev.orespawn.api.plugin.IOreSpawnPlugin;
import com.mcmoddev.orespawn.api.plugin.OreSpawnPlugin;

@OreSpawnPlugin(modid = "basemetals", resourcePath = "orespawn")
public class BaseMetalsOreSpawn implements IOreSpawnPlugin {
	
	@Override
	public void register(OS3API apiInterface) {
		// nothing for us to do - all of our ores are in the
		// jar and the code handles that
	}
	
}
