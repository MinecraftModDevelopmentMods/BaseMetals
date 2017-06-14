package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class HelmetRepairRecipe extends ShapelessOreRecipe implements IRecipe {

	private ItemStack baseHelmet;
	private String matName;

	public HelmetRepairRecipe(MMDMaterial mat) {
		super(new ItemStack(mat.getItem(Names.HELMET), 1), new Object[] { mat.getName() + "_helmet", Oredicts.PLATE + mat.getCapitalizedName() });
		baseHelmet = new ItemStack(mat.getItem(Names.HELMET), 1, OreDictionary.WILDCARD_VALUE);
		matName = mat.getCapitalizedName();
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		NonNullList<ItemStack> repairMaterials = OreDictionary.getOres(Oredicts.PLATE + matName);
		boolean helmetMatched = false;
		boolean repairMatched = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack item = inv.getStackInSlot(i);
			if (!repairMatched) {
				repairMatched = OreDictionary.containsMatch(false, repairMaterials, item);
			}
			if (!helmetMatched) {
				boolean hasDamage = item.getItemDamage() > 0 ? true : false;
				helmetMatched = OreDictionary.itemMatches(baseHelmet, item, false) ? hasDamage : false;
			}
		}
		return helmetMatched ? repairMatched : false;
	}

	private ItemStack findBaseItem(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack a = inv.getStackInSlot(i);
			if (a != null) {
				ItemStack comp = new ItemStack(a.getItem(), 1, a.getMetadata());
				if (OreDictionary.itemMatches(baseHelmet, comp, false)) {
					return a;
				}
			}
		}
		return null;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack target = findBaseItem(inv);
		if (target == null) {
			return new ItemStack(baseHelmet.getItem(), 1, 0);
		} else {
			ItemStack rv = new ItemStack(target.getItem(), 1, target.getMetadata());
			EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(target), rv);
			rv.setItemDamage(0);
			return rv;
		}
	}

	@Override
	public int getRecipeSize() {
		int count = 1;
		count += OreDictionary.getOres(Oredicts.PLATE + matName).size();
		return count;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(baseHelmet.getItem(), 1, 0);
	}

	@Override
	public NonNullList<Object> getInput() {
		NonNullList<Object> inputs = NonNullList.create();
		inputs.add(baseHelmet);
		inputs.addAll(OreDictionary.getOres(Oredicts.PLATE + matName));
		return inputs;
	}
}
