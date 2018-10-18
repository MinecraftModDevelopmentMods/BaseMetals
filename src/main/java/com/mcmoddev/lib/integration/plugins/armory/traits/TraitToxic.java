package com.mcmoddev.lib.integration.plugins.armory.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * <h2><u>Toxic Armor Modifier:</u></h2>
 * <b>Name:</b> toxic
 * <br>
 * <b>Desc:</b>
 * Applies poison 2 for 5 seconds as well as blindness for 2.5 seconds when a living entity hits the player.
 *
 * <br>
 * <b>String Reference:<br></b>
 * "toxic"
 * "mmd-toxic"
 */
public class TraitToxic extends AbstractArmorTrait {

	public TraitToxic() {
		super("mmd-toxic", TextFormatting.GREEN);
	}

	@Override
	public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
		Entity entity = source.getTrueSource();
		if (entity instanceof EntityLivingBase && !(entity instanceof FakePlayer)) {
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 2));
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 50));
		}
		return super.onHurt(armor, player, source, damage, newDamage, evt);
	}
}
