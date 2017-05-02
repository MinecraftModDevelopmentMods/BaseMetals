package com.mcmoddev.basemetals.data;

import java.util.List;

public class OreSpawnDimensionEntry {

	private final String dimension;
	private final String __comment;
	private final List<OreSpawnOreEntry> ores;

	public OreSpawnDimensionEntry(String dimension, String comment, List<OreSpawnOreEntry> ores) {
		this.dimension = dimension;
		this.__comment = comment;
		this.ores = ores;
	}

	public String getDimension() {
		return dimension;
	}

	public String get__comment() {
		return __comment;
	}

	public List<OreSpawnOreEntry> getOres() {
		return ores;
	}
}
