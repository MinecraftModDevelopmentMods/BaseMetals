package com.mcmoddev.lib.block;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMMDStairs extends BlockStairs implements IOreDictionaryEntry, IMMDObject {

	final MMDMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material
	 *            The material the stairs are made from
	 */
	public BlockMMDStairs(MMDMaterial material) {
		super(material.getBlock(Names.BLOCK).getDefaultState());
		this.setSoundType(SoundType.METAL);
		this.material = material;
		this.oreDict = Oredicts.STAIRS + this.material.getCapitalizedName();
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
		this.oreDict = Oredicts.STAIRS + this.material.getCapitalizedName();
		this.blockHardness = material.getBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MMDMaterial getMetalMaterial() {
		return this.material;
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}
}
