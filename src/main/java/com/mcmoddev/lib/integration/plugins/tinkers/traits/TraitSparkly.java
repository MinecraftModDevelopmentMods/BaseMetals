package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitSparkly extends AbstractTrait {

	private static final int REGEN_INTERVAL = 200;

	public TraitSparkly() {
		super("mmd-sparkly", TextFormatting.OBFUSCATED);
	}

	@Override
	public void onUpdate(@Nonnull final ItemStack tool, @Nonnull final World world, @Nonnull final Entity entity, @Nonnull final int itemSlot, @Nonnull final boolean isHeld) {
		if (!world.isRemote && isHeld && tool.isItemDamaged() && ((world.getTotalWorldTime() % REGEN_INTERVAL) == 0)) {
			ToolHelper.healTool(tool, 1, (EntityLivingBase) entity);
		}
	}
}
