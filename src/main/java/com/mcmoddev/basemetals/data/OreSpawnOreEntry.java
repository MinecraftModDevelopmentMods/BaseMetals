package com.mcmoddev.basemetals.data;

import javax.annotation.Nonnull;

public class OreSpawnOreEntry {

	private final String blockID;
	private final int size;
	private final int variation;
	private final float frequency;
	private final int minHeight;
	private final int maxHeight;

	public OreSpawnOreEntry(@Nonnull final String blockID, @Nonnull final int size, @Nonnull final int variation, @Nonnull final float frequency, @Nonnull final int minHeight, @Nonnull final int maxHeight) {
		this.blockID = blockID;
		this.size = size;
		this.variation = variation;
		this.frequency = frequency;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}

	public String getBlockID() {
		return blockID;
	}
	
	public int getSize() {
		return size;
	}

	public int getVariation() {
		return variation;
	}

	public float getFrequency() {
		return frequency;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}
}
