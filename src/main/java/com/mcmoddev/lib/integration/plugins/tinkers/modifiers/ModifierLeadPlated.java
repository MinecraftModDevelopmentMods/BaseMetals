package com.mcmoddev.lib.integration.plugins.tinkers.modifiers;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;

// We really need nothing here other than to know its been applied.

public class ModifierLeadPlated extends ToolModifier {

	/**
	 *
	 */
	public ModifierLeadPlated() {
		super("lead-plated", 0xFFFFFF);

		this.addAspects(new ModifierAspect.SingleAspect(this), new ModifierAspect.DataAspect(this),
				ModifierAspect.freeModifier);
	}

	@Override
	public void applyEffect(@Nonnull final NBTTagCompound rootCompound,
			@Nonnull final NBTTagCompound modifierTag) {
		modifierTag.setBoolean("plated", true);
		TagUtil.setExtraTag(rootCompound, modifierTag);
	}
}
