package com.mcmoddev.basemetals.properties;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.properties.MMDMaterialPropertyBase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class AdamantineToolProperty extends MMDMaterialPropertyBase {

	@Override
	public void apply(ItemStack stack) {
		return;
	}

    @Override
    public void apply(ItemStack stack, EntityPlayer player) {
    	apply(stack, (EntityLivingBase)player);
    }

	@Override
	public void apply(ItemStack stack, EntityLivingBase ent) {
		if(hasEffect(stack, ent)) {
			if (ent.getMaxHealth() > 20f) {
				final DamageSource extraDamage = DamageSource.GENERIC;
				ent.attackEntityFrom(extraDamage, 4f);
			}
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return false; // no effect for just the stack
	}

	@Override
	public boolean hasEffect(ItemStack stack, EntityPlayer player) { 
		return hasEffect(stack, (EntityLivingBase)player);
	}

	@Override
	public boolean hasEffect(ItemStack stack, EntityLivingBase ent) {
		if(stack.getItem() instanceof IMMDObject) {
			MMDMaterial mat = ((IMMDObject)stack.getItem()).getMMDMaterial();
			return MaterialNames.ADAMANTINE.equals(mat.getName()) && (ent.getMaxHealth() > 20f);
		} else {
			return false;
		}
	}

}
