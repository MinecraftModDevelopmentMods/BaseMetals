package com.mcmoddev.lib.item;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDSlab extends net.minecraft.item.ItemSlab implements IMMDObject {

	final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material to make the slab from
	 */
	public ItemMMDSlab(final MMDMaterial material) {
		super((BlockSlab) material.getBlock(Names.SLAB), (BlockSlab) material.getBlock(Names.SLAB), (BlockSlab) material.getBlock(Names.DOUBLE_SLAB));
		this.material = material;
	}

	/**
	 *
	 * @param material
	 *            The material to make the slab from
	 * @param block
	 *            The block to use to make the slab
	 * @param slab
	 *            The half slab block to use to make the slab
	 * @param doubleslab
	 *            The double slab block to use to make the slab
	 */
	public ItemMMDSlab(final MMDMaterial material, final Block block, final BlockSlab slab, final BlockSlab doubleslab) {
		super(block, slab, doubleslab);
		this.material = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
