package com.mcmoddev.basemetals.data;

public class OreSpawnOreEntry {

	private final String blockID;
	private final int size;
	private final int variation;
	private final float frequency;
	private final int minHeight;
	private final int maxHeight;

	public OreSpawnOreEntry(String blockID, int size, int variation, float frequency, int minHeight, int maxHeight) {
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
