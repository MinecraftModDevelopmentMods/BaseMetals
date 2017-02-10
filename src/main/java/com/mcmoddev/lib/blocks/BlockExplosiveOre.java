package com.mcmoddev.lib.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;

public class BlockExplosiveOre extends BlockOre {

	private boolean explode;

	public BlockExplosiveOre() {
		super();
	}

	public Block explode() {
		this.explode = true;
		return this;
	}

	public boolean doesExplode() {
		return explode;
	}
}
