package com.mcmoddev.lib.integration.plugins.armory.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * <h2><u>Poisonous Armor Modifier:</u></h2>
 * <b>Name:</b> poisonous
 * <br>
 * <b>Desc:</b>
 * Applies poison for 5 seconds when a living entity hits the player.
 *
 * <br>
 * <b>String Reference:<br></b>
 * "poisonous"
 * "mmd-poisonous"
 */
public class TraitPoisonous extends AbstractArmorTrait {
    public TraitPoisonous() {
        super("mmd-poisonous", 16777215);
    }

    @Override
    public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
        Entity entity = source.getTrueSource();
        if (entity instanceof EntityLivingBase && !(entity instanceof FakePlayer)) {
            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.POISON, 101));
        }
        return super.onHurt(armor, player, source, damage, newDamage, evt);
    }
}
