package com.mcmoddev.lib.block;

import net.minecraft.block.Block;

public class BlockExplosiveOre extends net.minecraft.block.BlockOre {

	private boolean explode;

	public BlockExplosiveOre() {
		super();
	}

	public Block explode() {
		this.explode = true;
		return this;
	}

	public boolean doesExplode() {
		return this.explode;
	}
}
