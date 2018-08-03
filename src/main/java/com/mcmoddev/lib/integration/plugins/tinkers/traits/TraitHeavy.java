package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

/**
 * <h2><u>Heavy Tool Modifier:</u></h2>
 * <b>Name:</b> heavy
 * <br>
 * <b>Desc:</b>
 * Increases the knockback of the tool by the factor of the tool's attack stat.
 *
 * <br>
 * <b>String Reference:<br></b>
 * "heavy"<br>
 * "mmd-heavy"<br>
 *
 * @author Java doc author: Vase of Petunias
 */
public class TraitHeavy extends AbstractTrait {

	public TraitHeavy() {
		super("mmd-heavy", TextFormatting.BLACK);
	}

	@Override
	public float knockBack(@Nonnull final ItemStack tool, @Nonnull final EntityLivingBase player,
			@Nonnull final EntityLivingBase target, @Nonnull final float damage,
			@Nonnull final float knockback, @Nonnull final float newKnockback,
			@Nonnull final boolean isCritical) {
		return newKnockback + ToolHelper.getAttackStat(tool);
	}
}
