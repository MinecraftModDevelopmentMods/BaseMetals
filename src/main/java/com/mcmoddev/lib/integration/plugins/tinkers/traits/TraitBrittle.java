package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.world.BlockEvent;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

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
