package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitReactive extends AbstractTrait {

	public TraitReactive() {
		super("mmd-reactive", TextFormatting.BLACK);
	}

	@Override
	public void onUpdate(@Nonnull final ItemStack tool, @Nonnull final World world, @Nonnull final Entity entity, @Nonnull final int itemSlot, @Nonnull final boolean isSelected) {
		if (!world.isRemote && entity instanceof EntityPlayer && entity.isWet() && ((EntityPlayer) entity).getActiveItemStack() == tool) {
			ToolHelper.damageTool(tool, 5, (EntityLivingBase) entity);
		}
	}

	@Override
	public void onHit(@Nonnull final ItemStack tool, @Nonnull final EntityLivingBase player, @Nonnull final EntityLivingBase target, @Nonnull final float damage, @Nonnull final boolean isCritical) {
		if (target.canBreatheUnderwater()) {
			// do extra damage
			final DamageSource extraDamage = DamageSource.ON_FIRE;
			target.attackEntityFrom(extraDamage, isCritical ? 8f : 4f);
		}
	}
}
