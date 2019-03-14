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

public class StarSteelProperty extends BMEPropertyBase {
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
			final int num = countArmorPieces(Materials.getMaterialByName(MaterialNames.STARSTEEL), player);
			if (num == 0) {
				return;
			}
			final PotionEffect jumpBoost = new PotionEffect(MobEffects.JUMP_BOOST, EFFECT_DURATION,
					num - 1, false, false);
			player.addPotionEffect(jumpBoost);
			if (num > 1) {
				final PotionEffect speedBoost = new PotionEffect(MobEffects.SPEED, EFFECT_DURATION,
						num - 2, false, false);
				player.addPotionEffect(speedBoost);
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
		MMDMaterial mat = Materials.getMaterialByName(MaterialNames.STARSTEEL);
		boolean rv = (stackIsArmorMaterial(stack, mat) 
				&& ((stack.getItem() instanceof IMMDObject) && 
						(((IMMDObject)stack.getItem()).getMMDMaterial() == mat))) && 
				(countArmorPieces(Materials.getMaterialByName(MaterialNames.STARSTEEL),player) > 0);
 
		return rv;
	}

	@Override
	public boolean hasEffect(ItemStack stack, EntityLivingBase ent) {
		return ent instanceof EntityPlayer?hasEffect(stack, (EntityPlayer)ent):false;
	}
}
