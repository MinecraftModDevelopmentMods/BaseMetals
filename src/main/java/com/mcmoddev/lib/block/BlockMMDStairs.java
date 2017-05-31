package com.mcmoddev.lib.block;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMMDStairs extends net.minecraft.block.BlockStairs implements IMMDObject {

	final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material the stairs are made from
	 */
	public BlockMMDStairs(MMDMaterial material) {
		super(material.getBlock(Names.BLOCK).getDefaultState());
		this.material = material;
		this.setSoundType(this.material.getSoundType());
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel("pickaxe", this.material.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
