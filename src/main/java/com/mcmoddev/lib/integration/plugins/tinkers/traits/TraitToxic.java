package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitToxic extends AbstractTrait {

	public TraitToxic() {
		super("toxic", TextFormatting.GREEN);
	}

	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
		if (wasHit && target.isEntityAlive()) {
			target.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 2));
			target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 50));
		}
	}
}
