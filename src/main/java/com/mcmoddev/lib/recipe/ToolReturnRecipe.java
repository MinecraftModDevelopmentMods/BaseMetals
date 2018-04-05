package com.mcmoddev.lib.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ToolReturnRecipe extends ShapelessOreRecipe {

	private final ItemStack theTool;

	public ToolReturnRecipe(final ResourceLocation ident, final ItemStack tool,
			final ItemStack result, final ItemStack... ingredients) {
		super(ident, result, (Object[]) ingredients);
		this.theTool = tool;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
		final NonNullList<ItemStack> remnants = NonNullList.withSize(inv.getSizeInventory(),
				ItemStack.EMPTY);

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (OreDictionary.itemMatches(inv.getStackInSlot(i), this.theTool, false)) {
				final ItemStack recipeTool = inv.getStackInSlot(i);
				recipeTool.damageItem(1, null);
				remnants.set(i, recipeTool);
			} else {
				remnants.set(i, ForgeHooks.getContainerItem(inv.getStackInSlot(i)));
			}
		}

		return remnants;
	}
}
