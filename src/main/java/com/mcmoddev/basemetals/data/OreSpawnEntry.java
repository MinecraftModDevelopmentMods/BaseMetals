package com.mcmoddev.basemetals.data;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OreSpawnEntry {

	private final String __comment;
	private final List<OreSpawnDimensionEntry> dimensions;

	public OreSpawnEntry(@Nonnull final List<OreSpawnDimensionEntry> dimensions, @Nullable final String comment) {
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
