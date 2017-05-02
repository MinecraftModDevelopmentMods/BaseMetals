package com.mcmoddev.basemetals.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcmoddev.basemetals.BaseMetals;

import net.minecraftforge.fml.common.FMLLog;

public class JSONHandler {
	
	public static void init() {
		List<OreSpawnOreEntry> ores = new ArrayList<>();
		List<OreSpawnOreEntry> netherores = new ArrayList<>();
		List<OreSpawnOreEntry> endores = new ArrayList<>();
		List<OreSpawnDimensionEntry> dimensions = new ArrayList<>();

		ores.add(new OreSpawnOreEntry("basemetals:copper_ore", 8, 4, 10, 0, 32));
		ores.add(new OreSpawnOreEntry("basemetals:silver_ore", 8, 4, 10, 0, 32));
		ores.add(new OreSpawnOreEntry("basemetals:tin_ore", 8, 12, 20, 0, 96));
		ores.add(new OreSpawnOreEntry("basemetals:lead_ore", 8, 12, 20, 0, 96));
		ores.add(new OreSpawnOreEntry("basemetals:zinc_ore", 8, 4, 10, 0, 32));
		ores.add(new OreSpawnOreEntry("basemetals:mercury_ore", 8, 4, 10, 0, 96));
		ores.add(new OreSpawnOreEntry("basemetals:nickel_ore", 8, 4, 10, 0, 32));
		ores.add(new OreSpawnOreEntry("basemetals:platinum_ore", 8, 8, 10, 0, 32));

		dimensions.add(new OreSpawnDimensionEntry("+", "All Dimensions", ores));

		ores.add(new OreSpawnOreEntry("basemetals:coldiron_ore", 8, 12, 20, 0, 96));
		ores.add(new OreSpawnOreEntry("basemetals:adamantine_ore", 8, 12, 20, 0, 96));

		dimensions.add(new OreSpawnDimensionEntry("-1", "The Nether", netherores));

		ores.add(new OreSpawnOreEntry("basemetals:starsteel_ore", 8, 12, 20, 0, 96));

		dimensions.add(new OreSpawnDimensionEntry("1", "The End", endores));

		OreSpawnEntry ose = new OreSpawnEntry(dimensions, "dimension 0 for overworld, -1 for the nether, 1 for the end, other numbers for dimensions added by other mods, and + for any dimension not already described by this file.");

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonInString = gson.toJson(ose);
		FMLLog.severe(BaseMetals.MODID + ": DEBUG: " + jsonInString);
	}
}
