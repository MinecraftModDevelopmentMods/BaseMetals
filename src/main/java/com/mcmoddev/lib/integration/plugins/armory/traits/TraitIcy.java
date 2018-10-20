package com.mcmoddev.lib.integration.plugins.armory.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

/**
 * <h2><u>Icy Armor Modifier</u></h2>
 * <b>Name:</b> icy
 * <br>
 * <b>Desc:</b>
 * Applies Fire Protection
 *
 * <br>
 * <b>String Reference:<br></b>
 * "icy"
 * "mmd-icy"
 */
public class TraitIcy extends AbstractArmorTrait {
    private static final int EFFECT_DURATION = 20;

    public TraitIcy() {
        super("mmd-icy", TextFormatting.GRAY);
    }

    private static int getAmountSuitPieces(final EntityPlayer player, final String materialName) {
        final MMDMaterial material = Materials.getMaterialByName(materialName);
        int pieces = 0;
        if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == material.getItem(Names.HELMET)){
            pieces++;
        }
        if (player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == material.getItem(Names.CHESTPLATE)){
            pieces++;
        }
        if (player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == material.getItem(Names.LEGGINGS)){
            pieces++;
        }
        if (player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == material.getItem(Names.BOOTS)){
            pieces++;
        }
        return pieces;
    }

    private static void applyFireProtection(EntityPlayer player){
        final PotionEffect fireProtection = new PotionEffect(MobEffects.FIRE_RESISTANCE,
                EFFECT_DURATION, getAmountSuitPieces(player, MaterialNames.COLDIRON)-1, false, false);
        player.addPotionEffect(fireProtection);
    }

    @Override
    public float onDamaged(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingDamageEvent evt) {
        float newNewDamage = newDamage;
        if(source.isFireDamage()){
            newNewDamage = 0f;
        }
        return newNewDamage;
    }
}
