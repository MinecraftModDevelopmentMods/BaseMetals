package cyano.basemetals.blocks;

import cyano.basemetals.material.MetalMaterial;

public class BlockHalfMetalSlab extends BlockMetalSlab {

	public BlockHalfMetalSlab(MetalMaterial material) {
		super(material);
	}

	@Override
	public boolean isDouble() {
		return false;
	}
}
