package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitSparkly extends AbstractTrait {
	public TraitSparkly() {
		super("sparkly", TextFormatting.OBFUSCATED);
	}
	
	@Override
	public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
		if(!world.isRemote && entity instanceof EntityLivingBase && random.nextInt(200) == 0 ) {
			if(((EntityLivingBase)entity).getActiveItemStack() != tool) {
				ToolHelper.healTool(tool, 1, (EntityLivingBase) entity);
			}
		}
	}
}
