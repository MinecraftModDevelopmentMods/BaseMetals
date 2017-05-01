package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockTripWireHook;

public class BlockMMDTripWireHook extends BlockTripWireHook implements IMMDObject {

	final MMDMaterial material;

	public BlockMMDTripWireHook(MMDMaterial material) {
		this.material = material;
		this.setSoundType(this.material.getSoundType());
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel("axe", this.material.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
