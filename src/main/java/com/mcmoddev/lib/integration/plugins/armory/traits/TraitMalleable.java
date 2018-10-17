package com.mcmoddev.lib.integration.plugins.armory.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

/**
 * <h2><u>Malleable Armor Modifier:</u></h2>
 * <b>Name:</b> soft
 * <br>
 * <b>Desc:</b> This modifier, when applied to armors, decreases the damage used by the armor when the armor is damaged by
 * a factor of 0.25. <br><em>(i.e. 0.25 less damage will be dealt to the armor's durability)</em>
 *
 * <br>
 * <b>String Reference:<br></b>
 * "malleable"<br>
 * "mmd-malleable"<br>
 */
public class TraitMalleable extends AbstractArmorTrait {
    public TraitMalleable() {
        super("mmd-malleable", TextFormatting.DARK_GRAY);
    }

    public float onDamaged(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingDamageEvent evt) {
        return super.onDamaged(armor, player, source, damage, newDamage - ((int) (damage * 0.25f)), evt);
    }
}
