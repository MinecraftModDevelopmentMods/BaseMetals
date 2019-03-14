package com.mcmoddev.basemetals.properties;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class AdamantineProperty extends BMEPropertyBase {
	@Override
	public void apply(ItemStack stack) {
		if(hasEffect(stack)) {
			// only apply if there is an effect
			// we should always double check here :)
		}
	}

	@Override
	public void apply(ItemStack stack, EntityPlayer player) {
		if(hasEffect(stack, player)) {
			final int level = countArmorPieces(Materials.getMaterialByName(MaterialNames.ADAMANTINE), player) / 2;
			if (level == 0) {
				return;
			}
			if (level > 0) {
				final PotionEffect protection = new PotionEffect(MobEffects.RESISTANCE, EFFECT_DURATION,
						level - 1, false, false);
				player.addPotionEffect(protection);
			}
		}
	}

	@Override
	public void apply(ItemStack stack, EntityLivingBase ent) {
		if(hasEffect(stack, ent) && ent instanceof EntityPlayer) {
			apply(stack, (EntityPlayer)ent);
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return false; // no effect for just the stack
	}

	@Override
	public boolean hasEffect(ItemStack stack, EntityPlayer player) {
		MMDMaterial mat = Materials.getMaterialByName(MaterialNames.ADAMANTINE);
		boolean rv = (stackIsArmorMaterial(stack, mat) 
				&& ((stack.getItem() instanceof IMMDObject) && 
						(((IMMDObject)stack.getItem()).getMMDMaterial() == mat))) && 
				(countArmorPieces(Materials.getMaterialByName(MaterialNames.ADAMANTINE),player) > 0);
 
		return rv;
	}

	@Override
	public boolean hasEffect(ItemStack stack, EntityLivingBase ent) {
		return ent instanceof EntityPlayer?hasEffect(stack, (EntityPlayer)ent):false;
	}
}
