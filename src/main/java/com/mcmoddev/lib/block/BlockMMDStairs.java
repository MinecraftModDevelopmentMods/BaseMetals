package com.mcmoddev.lib.block;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMMDStairs extends BlockStairs implements IMMDObject {

	final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material the stairs are made from
	 */
	public BlockMMDStairs(MMDMaterial material) {
		super(material.getBlock(Names.BLOCK).getDefaultState());
		this.setSoundType(SoundType.METAL);
		this.material = material;
		this.blockHardness = material.getBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
	}

	/**
	 *
	 * @param material
	 *            The material the stairs are made from
	 * @param modelBlock
	 *            The block to use for the model
	 */
	public BlockMMDStairs(MMDMaterial material, Block modelBlock) {
		super(modelBlock.getDefaultState());
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
