package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockFlowerPot;

public class BlockMMDFlowerPot extends net.minecraft.block.BlockFlowerPot implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 */
	public BlockMMDFlowerPot(final MMDMaterial material) {
		super();
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(CONTENTS, BlockFlowerPot.EnumFlowerType.EMPTY)
				.withProperty(LEGACY_DATA, Integer.valueOf(0)));
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
