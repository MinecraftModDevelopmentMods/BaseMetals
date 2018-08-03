package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.traits.AbstractTrait;

/**
 * <h2><u>Soft Tool Modifier:</u></h2>
 * <b>Name:</b> soft
 * <br>
 * <b>Desc:</b> This modifier, when applied to tools, increases the damage used by the tool when the tool is damaged by
 * a factor of 1.25. <br><em>(i.e. 1.25 more damage will be dealt to the tool's durability)</em>
 *
 * <br>
 * <b>String Reference:<br></b>
 * TraitNames.SOFT<br>
 * "soft"<br>
 * "mmd-soft"<br>
 * {@link com.mcmoddev.basemetals.data.TraitNames}
 *
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 * @author Java doc author: Vase of Petunias
 */
public class TraitSoft extends AbstractTrait {

	public TraitSoft() {
		super("mmd-soft", 0xffffff);
	}

	@Override
	public int onToolDamage(@Nonnull final ItemStack tool, @Nonnull final int damage,
			@Nonnull final int newDamage, @Nonnull final EntityLivingBase entity) {
		return super.onToolDamage(tool, damage, newDamage + ((int) (damage * 1.25f)), entity);
	}
}
