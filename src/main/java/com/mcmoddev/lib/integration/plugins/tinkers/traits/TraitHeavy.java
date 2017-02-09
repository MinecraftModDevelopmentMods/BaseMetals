package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitHeavy extends AbstractTrait {
	TraitHeavy() {
		super("heavy", TextFormatting.BLACK);
	}
	
	@Override
	public float knockBack(ItemStack tool, EntityLivingBase player, EntityLivingBase tarrget, float damage, float knockback, float newKnockback, boolean isCritical) {
		return newKnockback + ToolHelper.getAttackStat(tool);
	}
}
