package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockLadder;
import net.minecraft.block.SoundType;

public class BlockMMDLadder extends BlockLadder implements IMMDObject {

	final MMDMaterial material;

	public BlockMMDLadder(MMDMaterial material) {
        this.setSoundType(SoundType.METAL);
		this.material = material;
		this.blockHardness = material.getBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("axe", material.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMaterial() {
		return this.material;
	}

	@Override
	public MMDMaterial getMetalMaterial() {
		return this.material;
	}
}
