package com.mcmoddev.basemetals.data;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OreSpawnDimensionEntry {

	private final String dimension;
	private final String __comment;
	private final List<OreSpawnOreEntry> ores;

	public OreSpawnDimensionEntry(@Nonnull final String dimension, @Nonnull final List<OreSpawnOreEntry> ores, @Nullable final String comment) {
		this.dimension = dimension;
		this.ores = ores;
		this.__comment = comment;
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
