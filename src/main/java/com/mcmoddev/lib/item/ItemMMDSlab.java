package com.mcmoddev.lib.item;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
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
public class ItemMMDSlab extends ItemSlab implements IOreDictionaryEntry, IMMDObject {

	final MMDMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material
	 *            The material to make the slab from
	 */
	public ItemMMDSlab(MMDMaterial material) {
		super(material.halfSlab, material.halfSlab, material.doubleSlab);
		this.material = material;
		this.oreDict = Oredicts.SLAB + this.material.getCapitalizedName();
	}

	/**
	 *
	 * @param metal
	 *            The material to make the slab from
	 * @param block
	 *            The block to use to make the slab
	 * @param slab
	 *            The half slab block to use to make the slab
	 * @param doubleslab
	 *            The double slab block to use to make the slab
	 */
	public ItemMMDSlab(MMDMaterial metal, Block block, BlockSlab slab, BlockSlab doubleslab) {
		super(block, slab, doubleslab);
		this.material = metal;
		this.oreDict = "slab" + this.material.getCapitalizedName();
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
