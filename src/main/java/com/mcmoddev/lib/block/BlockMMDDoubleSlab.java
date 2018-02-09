package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.MMDMaterial;

public class BlockMMDDoubleSlab extends BlockMMDSlab {

	public BlockMMDDoubleSlab(final MMDMaterial material) {
		super(material);
	}

	@Override
	public boolean isDouble() {
		return true;
	}
}
