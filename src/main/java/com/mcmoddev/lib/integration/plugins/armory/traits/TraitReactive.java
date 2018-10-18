package com.mcmoddev.lib.integration.plugins.armory.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.utils.ToolHelper;

import javax.annotation.Nonnull;

/**
 * <h2><u>Reactive Armor Modifier:</u></h2>
 * <b>Name:</b> reactive
 * <br>
 * <b>Desc:</b>
 * Damages the armor by 5 durability if the armor is "wet" (the player is in water) and the armor is being used
 * by the player.
 * This modifier also also deals extra damage to entities that can breathe under water when they hurt the player.
 * The damage type is fire damage.
 * Amount dealt to target entity is 4.
 *
 * <br>
 * <b>String Reference:<br></b>
 * "reactive"
 * "mmd-reactive"
 */
public class TraitReactive extends AbstractArmorTrait {

	public TraitReactive() {
		super("mmd-reactive", TextFormatting.BLACK);
	}

	@Override
	public void onUpdate(@Nonnull final ItemStack tool, @Nonnull final World world,
			@Nonnull final Entity entity, @Nonnull final int itemSlot,
			@Nonnull final boolean isSelected) {
		if (!world.isRemote && (entity instanceof EntityPlayer) && entity.isWet()
				&& (((EntityPlayer) entity).getActiveItemStack() == tool)) {
			ToolHelper.damageTool(tool, 5, (EntityLivingBase) entity);
		}
	}

	@Override
	public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
		Entity entity = source.getTrueSource();
		if (entity instanceof EntityLivingBase && !(entity instanceof FakePlayer)) {
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			if (entityLivingBase.canBreatheUnderwater()){
				final DamageSource extraDamage = DamageSource.ON_FIRE;
				entityLivingBase.attackEntityFrom(extraDamage, 4f);
			}
		}
		return super.onHurt(armor, player, source, damage, newDamage, evt);
	}
}
