package com.mcmoddev.basemetals.data;

import java.util.List;

public class OreSpawnEntry {

	private final String __comment;
	private final List<OreSpawnDimensionEntry> dimensions;

	public OreSpawnEntry(List<OreSpawnDimensionEntry> dimensions, String comment) {
		this.dimensions = dimensions;
		this.__comment = comment;
	}

	public String get__comment() {
		return __comment;
	}

	public List<OreSpawnDimensionEntry> getDimensions() {
		return dimensions;
	}
}
