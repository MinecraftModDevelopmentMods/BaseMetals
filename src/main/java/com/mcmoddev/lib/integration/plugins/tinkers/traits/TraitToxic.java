package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;

/**
 * <h2><u>Toxic Tool Modifier:</u></h2>
 * <b>Name:</b> toxic
 * <br>
 * <b>Desc:</b>
 * Applies poison 2 for 5 seconds as well as blindness for 2.5 seconds when a living entity is hit.
 *
 * <br>
 * <b>String Reference:<br></b>
 * "toxic"
 * "mmd-toxic"
 *
 * @author Java doc author: Vase of Petunias
 */
public class TraitToxic extends AbstractTrait {

	public TraitToxic() {
		super("mmd-toxic", TextFormatting.GREEN);
	}

	@Override
	public void afterHit(@Nonnull final ItemStack tool, @Nonnull final EntityLivingBase player,
			@Nonnull final EntityLivingBase target, @Nonnull final float damageDealt,
			@Nonnull final boolean wasCritical, @Nonnull final boolean wasHit) {
		if (wasHit && target.isEntityAlive()) {
			target.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 2));
			target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 50));
		}
	}
}
