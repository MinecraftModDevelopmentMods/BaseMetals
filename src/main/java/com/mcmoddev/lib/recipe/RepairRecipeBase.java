package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public abstract class RepairRecipeBase extends ShapelessOreRecipe {

	private final MMDMaterial material;
	private final ItemStack baseItem;
	private final String materialName;
	private final String itemName;
	private final NonNullList<ItemStack> repairMaterials;

	public RepairRecipeBase(final MMDMaterial material, final Names itemName) {
		this(material, itemName.toString(), material.getName() + "_" + itemName.toString(),
				Oredicts.PLATE + material.getCapitalizedName());
	}

	public RepairRecipeBase(final MMDMaterial material, final Names itemName,
			final Object... objects) {
		this(material, itemName.toString(), objects);
	}

	public RepairRecipeBase(final MMDMaterial material, final String itemName,
			final Object... objects) {
		super(material.getItemStack(itemName), objects);
		this.material = material;
		this.baseItem = material.getItemStack(itemName);
		this.materialName = material.getCapitalizedName();
		this.repairMaterials = OreDictionary.getOres(Oredicts.PLATE + this.materialName);
		this.itemName = itemName;
	}

	private boolean repairMaterialsDoesntContain(final ItemStack itemStack) {
		return !this.repairMaterials.contains(itemStack);
	}

	@Override
	public boolean matches(final InventoryCrafting inv, final World worldIn) {
		// make sure we have all the materials that can be used for repair, not just what was
		// available when we were initialized.
		OreDictionary.getOres(Oredicts.PLATE + this.materialName).stream()
				.filter(this::repairMaterialsDoesntContain).forEach(this.repairMaterials::add);

		boolean matched = false;
		boolean repairMatched = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack item = inv.getStackInSlot(i);
			if (!repairMatched) {
				repairMatched = OreDictionary.containsMatch(false, this.repairMaterials, item);
			}
			if (!matched) {
				final boolean hasDamage = item.getItemDamage() > 0 ? true : false;
				matched = OreDictionary.itemMatches(this.baseItem, item, false) ? hasDamage : false;
			}
		}
		return matched ? repairMatched : false;
	}

	private ItemStack findBaseItem(final InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack a = inv.getStackInSlot(i);
			if (a != null) {
				final ItemStack comp = new ItemStack(a.getItem(), 1, a.getMetadata());
				if (OreDictionary.itemMatches(this.baseItem, comp, false)) {
					return a;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inv) {
		final ItemStack target = this.findBaseItem(inv);
		if (target.isEmpty()) {
			return this.material.getItemStack(this.itemName);
		} else {
			final ItemStack rv = new ItemStack(target.getItem(), 1, target.getMetadata());
			EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(target), rv);
			rv.setItemDamage(0);
			return rv;
		}
	}

	@Override
	public int getRecipeSize() {
		int count = 1;
		count += OreDictionary.getOres(Oredicts.PLATE + this.materialName).size();
		return count;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.material.getItemStack(this.itemName);
	}

	@Override
	public NonNullList<Object> getInput() {
		final NonNullList<Object> inputs = NonNullList.create();
		inputs.add(this.baseItem);
		inputs.addAll(OreDictionary.getOres(Oredicts.PLATE + this.materialName));
		return inputs;
	}
}
