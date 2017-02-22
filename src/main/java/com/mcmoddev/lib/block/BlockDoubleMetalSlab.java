package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.MetalMaterial;

public class BlockDoubleMetalSlab extends BlockMetalSlab {

	public BlockDoubleMetalSlab(MetalMaterial material) {
		super(material);
	}

	@Override
	public boolean isDouble() {
		return true;
	}
}
