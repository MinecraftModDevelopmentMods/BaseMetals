package com.mcmoddev.lib.item;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemMMDBlock extends ItemBlock implements IMMDObject {

	private MMDMaterial mmdMaterial;
	
	public ItemMMDBlock(MMDMaterial material, Block block) {
		super(block);
		this.mmdMaterial = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}
}
