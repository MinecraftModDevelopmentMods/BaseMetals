package com.mcmoddev.lib.integration.plugins.tinkers.traits;

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
		super("reactive", TextFormatting.BLACK);
	}

	@Override
	public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!world.isRemote && entity instanceof EntityPlayer && entity.isWet()) {
			if (((EntityPlayer) entity).getActiveItemStack() == tool) {
				ToolHelper.damageTool(tool, 5, (EntityLivingBase) entity);
			}
		}
	}

	@Override
	public void onHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean isCritical) {
		if (target.canBreatheUnderwater()) {
			// do extra damage
			final DamageSource extraDamage = DamageSource.ON_FIRE;
			target.attackEntityFrom(extraDamage, isCritical ? 8f : 4f);
		}
	}
}
