package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

/**
 * <h2><u>Sparkly Tool Modifier:</u></h2>
 * <b>Name:</b> sparkly
 * <br>
 * <b>Desc:</b> This modifier, when applied to tools, has an automatic durability repair effect. Heals the tool for
 * 1 durability every 200 ticks. (10 seconds)
 *
 * <br>
 * <b>String Reference:<br></b>
 * TraitNames.SPARKLY<br>
 * "sparkly"<br>
 * "mmd-sparkly"<br>
 * {@link com.mcmoddev.basemetals.data.TraitNames}
 *
 * @author Java doc author: Vase of Petunias
 */
public class TraitSparkly extends AbstractTrait {

	/**
	 * <b>Units of game ticks:</b> 200 ticks (10 seconds)
	 */
	private static final int REGEN_INTERVAL = 200;

	public TraitSparkly() {
		super("mmd-sparkly", TextFormatting.OBFUSCATED);
	}

	@Override
	public void onUpdate(@Nonnull final ItemStack tool, @Nonnull final World world,
			@Nonnull final Entity entity, @Nonnull final int itemSlot,
			@Nonnull final boolean isHeld) {
		if (!world.isRemote && isHeld && tool.isItemDamaged()
				&& ((world.getTotalWorldTime() % REGEN_INTERVAL) == 0)) {
			ToolHelper.healTool(tool, 1, (EntityLivingBase) entity);
		}
	}
}
