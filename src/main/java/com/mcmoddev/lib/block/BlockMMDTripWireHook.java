package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.SoundType;

public class BlockMMDTripWireHook extends BlockTripWireHook implements IMMDObject {

	final MMDMaterial material;

	public BlockMMDTripWireHook(MMDMaterial material) {
        this.setSoundType(SoundType.METAL);
		this.material = material;
		this.blockHardness = material.getBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("axe", material.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
