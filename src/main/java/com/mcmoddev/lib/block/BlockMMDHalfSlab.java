package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.MMDMaterial;

public class BlockMMDHalfSlab extends BlockMMDSlab {

	public BlockMMDHalfSlab(final MMDMaterial material) {
		super(material);
	}

	@Override
	public boolean isDouble() {
		return false;
	}
}
