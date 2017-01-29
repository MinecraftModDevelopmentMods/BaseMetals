package com.mcmoddev.lib.blocks;

import com.mcmoddev.basemetals.material.IMetalObject;
import com.mcmoddev.basemetals.material.MetalMaterial;
import com.mcmoddev.basemetals.registry.IOreDictionaryEntry;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Metal Bars
 * 
 * @author DrCyano
 *
 */
public class BlockMetalBars extends net.minecraft.block.BlockPane implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material The material the bars are made from
	 */
	public BlockMetalBars(MetalMaterial material) {
		super(Material.IRON, true);
		this.setSoundType(SoundType.METAL);
		this.material = material;
		this.blockHardness = material.getMetalBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
		this.oreDict = "bars" + material.getCapitalizedName();
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.material;
	}

	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.material;
	}
}
