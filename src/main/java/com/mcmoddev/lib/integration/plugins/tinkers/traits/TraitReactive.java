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

/**
 * <h2><u>Reactive Tool Modifier:</u></h2>
 * <b>Name:</b> reactive
 * <br>
 * <b>Desc:</b>
 * Damages the tool by 5 durability if the tool is "wet" (the player is in water) and the tool is being used
 * by the player.
 * This modifier also also deals extra damage to entities that can breathe under water.
 * The damage type is fire damage.
 * Amount dealt to target entity is 4, and 8 on critical strikes.
 *
 * <br>
 * <b>String Reference:<br></b>
 * "reactive"
 * "mmd-reactive"
 *
 * @author Java doc author: Vase of Petunias
 */
public class TraitReactive extends AbstractTrait {

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
	public void onHit(@Nonnull final ItemStack tool, @Nonnull final EntityLivingBase player,
			@Nonnull final EntityLivingBase target, @Nonnull final float damage,
			@Nonnull final boolean isCritical) {
		if (target.canBreatheUnderwater()) {
			// do extra damage
			final DamageSource extraDamage = DamageSource.ON_FIRE;
			target.attackEntityFrom(extraDamage, isCritical ? 8f : 4f);
		}
	}
}
