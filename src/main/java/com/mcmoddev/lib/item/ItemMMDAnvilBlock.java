package com.mcmoddev.lib.item;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.ItemStack;

public class ItemMMDAnvilBlock extends net.minecraft.item.ItemAnvilBlock implements IMMDObject {

	private final MMDMaterial material;

	public ItemMMDAnvilBlock(final MMDMaterial material) {
		super(material.getBlock(Names.ANVIL));
		this.material = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}

	@Override
	public int getMetadata(final int damage) {
		return damage << 2;
	}

	@Override
	public String getUnlocalizedName(final ItemStack stack) {
		return super.getUnlocalizedName(stack);
	}
}
