package com.mcmoddev.lib.item;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.ItemStack;

public class ItemMMDAnvilBlock extends net.minecraft.item.ItemMultiTexture implements IMMDObject {

	final MMDMaterial material;

	public ItemMMDAnvilBlock(MMDMaterial material) {
		super(material.getBlock(Names.ANVIL), material.getBlock(Names.ANVIL), new String[] {"intact", "slightlyDamaged", "veryDamaged"});
		this.material = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}

	@Override
    public int getMetadata(int damage)
    {
        return damage << 2;
    }

	@Override
    public String getUnlocalizedName(ItemStack stack)
    {
    	final String unloc = super.getUnlocalizedName(stack);
        return unloc;
    }
}
