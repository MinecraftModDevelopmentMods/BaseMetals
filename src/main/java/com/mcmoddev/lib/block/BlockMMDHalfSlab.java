package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.MMDMaterial;

public class BlockMMDHalfSlab extends BlockMMDSlab {

	public BlockMMDHalfSlab(MMDMaterial material) {
		super(material);
	}

	@Override
	public boolean isDouble() {
		return false;
	}
}
