package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.world.BlockEvent;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

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
 *
 * @author Java doc author: Vase of Petunias
 */
public class TraitBrittle extends AbstractTrait {

	public TraitBrittle() {
		super("mmd-brittle", TextFormatting.RED);
	}

	@Override
	public void beforeBlockBreak(@Nonnull final ItemStack tool,
			@Nonnull final BlockEvent.BreakEvent event) {
		final Block block = event.getState().getBlock();
		if (block.getDefaultState().getMaterial() == Material.ROCK) {
			final Integer durability = ToolHelper.getCurrentDurability(tool);
			final Integer damageDone = random.nextInt(Math.min(5, durability - 1));
			ToolHelper.damageTool(tool, damageDone.intValue(), event.getPlayer());
		}
	}
}
