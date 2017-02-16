package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitSparkly extends AbstractTrait {

	private final static int REGEN_INTERVAL = 200;
	
	public TraitSparkly() {
		super("sparkly", TextFormatting.OBFUSCATED);
	}

	@Override
	public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isHeld) {
		if ( !world.isRemote && isHeld && tool.isItemDamaged() && ((world.getTotalWorldTime() % REGEN_INTERVAL) == 0)) {
			ToolHelper.healTool(tool, 1, (EntityLivingBase) entity);
		}
	}
}
