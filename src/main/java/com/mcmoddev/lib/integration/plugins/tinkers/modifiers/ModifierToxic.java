package com.mcmoddev.lib.integration.plugins.tinkers.modifiers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;

public class ModifierToxic extends ModifierTrait {

	public ModifierToxic() {
		super("toxic-modifier", 0xFFFFFF, 1, 0);
	}

	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
		if (wasHit && target.isEntityAlive()) {
			target.addPotionEffect(new PotionEffect(MobEffects.POISON, 50, 1));
			target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 10));
		}
	}
}
