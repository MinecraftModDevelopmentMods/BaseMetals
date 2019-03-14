package com.mcmoddev.basemetals.properties;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.properties.MMDMaterialPropertyBase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class MithrilToolProperty extends MMDMaterialPropertyBase {

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
			if (ent.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
				final PotionEffect wither = new PotionEffect(MobEffects.WITHER, 60, 3);
				final PotionEffect blind = new PotionEffect(MobEffects.BLINDNESS, 60, 1);
				ent.addPotionEffect(wither);
				ent.addPotionEffect(blind);
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
			return MaterialNames.MITHRIL.equals(mat.getName()) && (ent.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD);
		} else {
			return false;
		}
	}

}
