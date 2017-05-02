package com.mcmoddev.basemetals.data;

public class OreSpawnOreEntry {

	private String blockID;
	private int size;
	private int variation;
	private int frequency;
	private int minHeight;
	private int maxHeight;

	public OreSpawnOreEntry(String blockID, int size, int variation, int frequency, int minHeight, int maxHeight) {
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

	public int getFrequency() {
		return frequency;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}
}
