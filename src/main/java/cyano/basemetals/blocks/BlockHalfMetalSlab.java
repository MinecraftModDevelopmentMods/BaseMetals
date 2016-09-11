package cyano.basemetals.blocks;

import cyano.basemetals.material.MetalMaterial;

public class BlockHalfMetalSlab extends BlockMetalSlab {

	public BlockHalfMetalSlab(MetalMaterial metal) {
		super(metal);
	}

	@Override
	public boolean isDouble() {
		return false;
	}
}
