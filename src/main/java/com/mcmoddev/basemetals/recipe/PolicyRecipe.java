package com.mcmoddev.basemetals.recipe;

import com.mcmoddev.basemetals.content.ContentPolicy;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/** Keeps recipe registry identity stable while disabling acquisition in restricted modes. */
final class PolicyRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	private final IRecipe delegate;
	private final boolean allowed;

	PolicyRecipe(final IRecipe delegate, final ContentPolicy policy) {
		this.delegate = delegate;
		final ItemStack output = delegate.getRecipeOutput();
		this.allowed = !output.isEmpty() ? policy.allows(output)
				: policy.allowsRecipeId(delegate.getRegistryName());
		setRegistryName(delegate.getRegistryName());
	}

	boolean isAllowed() {
		return allowed;
	}

	@Override
	public boolean matches(final InventoryCrafting inventory, final World world) {
		return allowed && delegate.matches(inventory, world);
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inventory) {
		return allowed ? delegate.getCraftingResult(inventory) : ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(final int width, final int height) {
		return allowed && delegate.canFit(width, height);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return allowed ? delegate.getRecipeOutput() : ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inventory) {
		return delegate.getRemainingItems(inventory);
	}

	@Override
	public NonNullList<net.minecraft.item.crafting.Ingredient> getIngredients() {
		return delegate.getIngredients();
	}

	@Override
	public boolean isDynamic() {
		return !allowed || delegate.isDynamic();
	}

	@Override
	public String getGroup() {
		return delegate.getGroup();
	}
}
