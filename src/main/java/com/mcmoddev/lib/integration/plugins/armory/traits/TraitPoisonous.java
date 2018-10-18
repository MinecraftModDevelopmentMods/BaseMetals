package com.mcmoddev.lib.integration.plugins.armory.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class TraitPoisonous extends AbstractArmorTrait {
    public TraitPoisonous() {
        super("mmd-poisonous", 16777215);
    }

//    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
//        if (wasHit && target.isEntityAlive()) {
//            target.addPotionEffect(new PotionEffect(MobEffects.POISON, 101));
//        }
//    }
}
