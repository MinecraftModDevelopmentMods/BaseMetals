package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockLever;
import net.minecraft.block.SoundType;

public class BlockMMDLever extends BlockLever implements IMMDObject {

	final MMDMaterial material;

	public BlockMMDLever(MMDMaterial material) {
		this.setSoundType(SoundType.METAL);
		this.material = material;
		this.blockHardness = material.getBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
