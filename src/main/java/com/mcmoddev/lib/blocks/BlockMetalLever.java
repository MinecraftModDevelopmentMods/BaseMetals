package com.mcmoddev.lib.blocks;

import com.mcmoddev.basemetals.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.BlockLever;
import net.minecraft.block.SoundType;

public class BlockMetalLever extends BlockLever implements IOreDictionaryEntry, IMetalObject {

	final MetalMaterial material;
	private final String oreDict;

	public BlockMetalLever(MetalMaterial material) {
		this.setSoundType(SoundType.METAL);
		this.material = material;
		this.oreDict = Oredicts.LEVER + this.material.getCapitalizedName();
		this.blockHardness = material.getMetalBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.material;
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}
}
