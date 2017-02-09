package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.world.BlockEvent;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitBrittle extends AbstractTrait {

	TraitBrittle() {
		super("brittle", TextFormatting.RED);
	}

	@Override
	public void beforeBlockBreak(ItemStack tool, BlockEvent.BreakEvent event) {
		Block block = event.getState().getBlock();
		if (block.getDefaultState().getMaterial() == Material.ROCK) {
			Integer durability = ToolHelper.getCurrentDurability(tool);
			Integer damageDone = random.nextInt(Math.min(5, durability - 1));
			ToolHelper.damageTool(tool, damageDone.intValue(), event.getPlayer());
		}
	}
}

