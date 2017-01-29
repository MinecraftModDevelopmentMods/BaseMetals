package com.mcmoddev.lib.items;

import com.mcmoddev.basemetals.material.IMetalObject;
import com.mcmoddev.basemetals.material.MetalMaterial;
import com.mcmoddev.basemetals.registry.IOreDictionaryEntry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMetalSlab extends ItemSlab implements IOreDictionaryEntry, IMetalObject {

	final MetalMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material The material to make the slab from
	 */
	public ItemMetalSlab(MetalMaterial material) {
		super(material.half_slab, material.half_slab, material.double_slab);
		this.material = material;
		this.oreDict = "slab" + this.material.getCapitalizedName();
	}

	/**
	 *
	 * @param metal The material to make the slab from
	 * @param block The block to use to make the slab
	 * @param slab The half slab block to use to make the slab
	 * @param doubleslab The double slab block to use to make the slab
	 */
	public ItemMetalSlab(MetalMaterial metal, Block block, BlockSlab slab, BlockSlab doubleslab) {
		super(block, slab, doubleslab);
		this.material = metal;
		this.oreDict = "slab" + this.material.getCapitalizedName();
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

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}
}
