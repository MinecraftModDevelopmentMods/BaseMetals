package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockMMDFence extends net.minecraft.block.BlockFence implements IMMDObject {

	final MMDMaterial material;

	public BlockMMDFence(MMDMaterial material) {
		super(Material.WOOD, MapColor.WOOD);
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
