package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

public class BlockMMDFlowerPot extends net.minecraft.block.BlockFlowerPot implements IMMDObject {

	private final MMDMaterial material;

	public BlockMMDFlowerPot(final MMDMaterial material) {
		this.material = material;
		this.setSoundType(this.material.getSoundType());
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel(this.material.getHarvestTool(),
				this.material.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
