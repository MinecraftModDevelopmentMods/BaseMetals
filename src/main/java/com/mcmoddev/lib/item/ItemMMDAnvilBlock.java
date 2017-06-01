package com.mcmoddev.lib.item;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

public class ItemMMDAnvilBlock extends net.minecraft.item.ItemAnvilBlock implements IMMDObject {

	final MMDMaterial material;

	public ItemMMDAnvilBlock(MMDMaterial material) {
		super(material.getBlock(Names.ANVIL));
		this.material = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
