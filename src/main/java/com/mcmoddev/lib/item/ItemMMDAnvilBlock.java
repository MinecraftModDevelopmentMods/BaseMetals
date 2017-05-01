package com.mcmoddev.lib.item;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.ItemAnvilBlock;

public class ItemMMDAnvilBlock extends ItemAnvilBlock implements IMMDObject {

	final MMDMaterial material;

	public ItemMMDAnvilBlock(MMDMaterial material) {
		super(material.getBlock(Names.ANVILBLOCK));
		this.material = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
