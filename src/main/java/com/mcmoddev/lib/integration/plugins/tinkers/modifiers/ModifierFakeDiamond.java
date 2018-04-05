package com.mcmoddev.lib.integration.plugins.tinkers.modifiers;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;

public class ModifierFakeDiamond extends ToolModifier {

	/**
	 *
	 */
	public ModifierFakeDiamond() {
		super("fake-diamond", 0x00FF00);

		this.addAspects(new ModifierAspect.SingleAspect(this), new ModifierAspect.DataAspect(this),
				ModifierAspect.freeModifier);
		// add the modifier item here
	}

	@Override
	public void applyEffect(@Nonnull final NBTTagCompound rootCompound,
			@Nonnull final NBTTagCompound modifierTag) {
		final ToolNBT data = TagUtil.getToolStats(rootCompound);
		final ToolNBT base = TagUtil.getOriginalToolStats(rootCompound);

		data.durability += base.durability / 2;

		if (data.harvestLevel < HarvestLevels.DIAMOND) {
			data.harvestLevel++;
		}

		TagUtil.setToolTag(rootCompound, data.get());
	}

}
