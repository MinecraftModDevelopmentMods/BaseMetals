package com.mcmoddev.lib.integration.plugins.armory.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.util.text.TextFormatting;

/**
 * <h2><u>Brittle Tool Modifier:</u></h2>
 * <b>Name:</b> brittle
 * <br>
 * <b>Desc:</b>
 * Tools will lose 0 or 1 durability if the durability of the tool minus one is less than 5.
 * Otherwise, the tool will lose a minimum of 0 to a maximum of 5 durability at random.
 * Durability loss is applied and calculated if the block that is being broken is a stone block.
 *
 * <br>
 * <b>String Reference:<br></b>
 * "brittle"<br>
 * "mmd-brittle"<br>
 */
public class TraitBrittle extends AbstractArmorTrait {

	public TraitBrittle() {
		super("mmd-brittle", TextFormatting.RED);
	}

//	@Override
//	public void beforeBlockBreak(@Nonnull final ItemStack tool,
//			@Nonnull final BlockEvent.BreakEvent event) {
//		final Block block = event.getState().getBlock();
//		if (block.getDefaultState().getMaterial() == Material.ROCK) {
//			final Integer durability = ToolHelper.getCurrentDurability(tool);
//			final Integer damageDone = random.nextInt(Math.min(5, durability - 1));
//			ToolHelper.damageTool(tool, damageDone.intValue(), event.getPlayer());
//		}
//	}
}
