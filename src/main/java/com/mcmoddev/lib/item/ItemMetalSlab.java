package com.mcmoddev.lib.item;

import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.util.Oredicts;

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
		super(material.halfSlab, material.halfSlab, material.doubleSlab);
		this.material = material;
		this.oreDict = Oredicts.SLAB + this.material.getCapitalizedName();
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
