package com.mcmoddev.lib.blocks;

import com.mcmoddev.lib.material.MetalMaterial;

public class BlockHalfMetalSlab extends BlockMetalSlab {

	public BlockHalfMetalSlab(MetalMaterial material) {
		super(material);
	}

	@Override
	public boolean isDouble() {
		return false;
	}
}
