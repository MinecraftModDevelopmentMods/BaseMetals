package com.mcmoddev.lib.integration.plugins.tinkers.modifiers;

import javax.annotation.Nonnull;

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
	public void afterHit(@Nonnull final ItemStack tool, @Nonnull final EntityLivingBase player,
			@Nonnull final EntityLivingBase target, @Nonnull final float damageDealt,
			@Nonnull final boolean wasCritical, @Nonnull final boolean wasHit) {
		if (wasHit && target.isEntityAlive()) {
			target.addPotionEffect(new PotionEffect(MobEffects.POISON, 50, 1));
			target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 10));
		}
	}
}
