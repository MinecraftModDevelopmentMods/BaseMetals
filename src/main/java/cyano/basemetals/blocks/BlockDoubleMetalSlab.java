package cyano.basemetals.blocks;

import cyano.basemetals.material.MetalMaterial;

public class BlockDoubleMetalSlab extends BlockMetalSlab {

	public BlockDoubleMetalSlab(MetalMaterial material) {
		super(material);
	}

	@Override
	public boolean isDouble() {
		return true;
	}
}
