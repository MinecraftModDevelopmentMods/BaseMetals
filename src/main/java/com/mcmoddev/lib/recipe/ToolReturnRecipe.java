package com.mcmoddev.lib.recipe;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ToolReturnRecipe extends ShapelessOreRecipe {

	private final ItemStack theTool;
	private final List<ItemStack> ingredients;

	/**
	 *
	 * @param ident
	 * @param tool
	 * @param result
	 * @param ingredients
	 */
	public ToolReturnRecipe(final ResourceLocation ident, final ItemStack tool,
			final ItemStack result, final ItemStack... ingredients) {
		super(ident, result, (Object[]) ingredients);
		this.theTool = tool;
		this.ingredients = Lists.newArrayList(ingredients);
	}

	private boolean itemStackMatches(final ItemStack left, final ItemStack right) {
		return left.getItem().equals(right.getItem())
				&& (left.getMetadata() == OreDictionary.WILDCARD_VALUE
						|| (left.getMetadata() == right.getMetadata()));
	}

	@Override
	public boolean matches(final InventoryCrafting inv, final World worldIn) {
		Map<ItemStack, Boolean> matches = this.ingredients.stream()
				.collect(Collectors.toMap(x -> x, x -> false));
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack slot = inv.getStackInSlot(i);
			if (matches.keySet().stream().anyMatch(x -> itemStackMatches(x, slot))) {
				ItemStack k = matches.keySet().stream().filter(x -> itemStackMatches(x, slot))
						.distinct().findFirst().orElse(ItemStack.EMPTY);
				if (!k.isEmpty()) {
					matches.put(k, true);
				}
			}
		}

		return !matches.containsValue(false);
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
