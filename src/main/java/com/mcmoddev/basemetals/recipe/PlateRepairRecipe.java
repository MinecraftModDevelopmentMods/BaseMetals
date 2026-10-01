package com.mcmoddev.basemetals.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.recipe.RepairRecipeBase;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

/** Fully repairs one damaged armor piece or shield using one matching plate. */
public final class PlateRepairRecipe extends RepairRecipeBase {
	private final Item target;
	private final String plateOre;
	private final NonNullList<Ingredient> ingredients;

	public PlateRepairRecipe(final MMDMaterial material, final Names form,
			final Item target, final String plateOre) {
		// Extending RepairRecipeBase keeps MMDLib from counting a repair as
		// a way to produce new material.
		super(material, form);
		this.target = target;
		this.plateOre = plateOre;
		this.ingredients = NonNullList.create();
		this.ingredients.add(Ingredient.fromStacks(new ItemStack(target)));
		this.ingredients.add(new OreIngredient(plateOre));
	}

	@Override
	public boolean matches(final InventoryCrafting inventory, final World world) {
		boolean foundTarget = false;
		boolean foundPlate = false;

		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			final ItemStack stack = inventory.getStackInSlot(slot);

			if (stack.isEmpty()) {
				continue;
			}

			if (stack.getItem() == target && stack.getItemDamage() > 0 && !foundTarget) {
				foundTarget = true;
			} else if (!foundPlate && OreDictionary.containsMatch(false,
					OreDictionary.getOres(plateOre), stack)) {
				foundPlate = true;
			} else {
				return false;
			}
		}

		return foundTarget && foundPlate;
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inventory) {
		if (!matches(inventory, null)) {
			return ItemStack.EMPTY;
		}

		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			final ItemStack stack = inventory.getStackInSlot(slot);

			if (!stack.isEmpty() && stack.getItem() == target) {
				final ItemStack repaired = stack.copy();

				repaired.setCount(1);
				repaired.setItemDamage(0);

				return repaired;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(final int width, final int height) {
		return width * height >= 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(target);
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}
}
