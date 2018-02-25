package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public abstract class RepairRecipeBase extends ShapelessOreRecipe {

	private static final String ARMOR = "armor";
	public final MMDMaterial material;
	public final ItemStack baseItem;
	public final String materialName;
	public final String itemName;
	public final NonNullList<ItemStack> repairMaterials;

	public RepairRecipeBase(MMDMaterial material, Names itemName) {
		super(new ResourceLocation(ARMOR), material.getItemStack(itemName), material.getName() + "_" + itemName.toString(), Oredicts.PLATE + material.getCapitalizedName());
		this.material = material;
		this.baseItem = material.getItemStack(itemName);
		this.materialName = material.getCapitalizedName();
		this.repairMaterials = OreDictionary.getOres(Oredicts.PLATE + this.materialName);
		this.itemName = itemName.toString();
	}

	public RepairRecipeBase(MMDMaterial material, Names itemName, Object...objects) {
		super(new ResourceLocation(ARMOR), material.getItemStack(itemName), objects);
		this.material = material;
		this.baseItem = material.getItemStack(itemName);
		this.materialName = material.getCapitalizedName();
		this.repairMaterials = OreDictionary.getOres(Oredicts.PLATE + this.materialName);
		this.itemName = itemName.toString();
	}

	public RepairRecipeBase(MMDMaterial material, String itemName, Object...objects) {
		super(new ResourceLocation(ARMOR), material.getItemStack(itemName), objects);
		this.material = material;
		this.baseItem = material.getItemStack(itemName);
		this.materialName = material.getCapitalizedName();
		this.repairMaterials = OreDictionary.getOres(Oredicts.PLATE + this.materialName);
		this.itemName = itemName;
	}

	private boolean repairMaterialsDoesntContain(ItemStack itemStack) {
		return !repairMaterials.contains(itemStack);
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		// make sure we have all the materials that can be used for repair, not just what was
		// available when we were initialized.
		OreDictionary.getOres(Oredicts.PLATE + this.materialName).stream()
		.filter(this::repairMaterialsDoesntContain)
		.forEach(repairMaterials::add);
		
		boolean matched = false;
		boolean repairMatched = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack item = inv.getStackInSlot(i);
			if (!repairMatched) {
				repairMatched = OreDictionary.containsMatch(false, repairMaterials, item);
			}
			if (!matched) {
				final boolean hasDamage = item.getItemDamage() > 0 ? true : false;
				matched = OreDictionary.itemMatches(this.baseItem, item, false) ? hasDamage : false;
			}
		}
		return matched ? repairMatched : false;
	}

	private ItemStack findBaseItem(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack a = inv.getStackInSlot(i);
			if (a != null) {
				ItemStack comp = new ItemStack(a.getItem(), 1, a.getMetadata());
				if (OreDictionary.itemMatches(this.baseItem, comp, false)) {
					return a;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack target = findBaseItem(inv);
		if (target.isEmpty()) {
			return this.material.getItemStack(this.itemName);
		} else {
			final ItemStack rv = new ItemStack(target.getItem(), 1, target.getMetadata());
			EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(target), rv);
			rv.setItemDamage(0);
			return rv;
		}
	}

	public int getRecipeSize() {
		int count = 1;
		count += OreDictionary.getOres(Oredicts.PLATE + this.materialName).size();
		return count;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.material.getItemStack(this.itemName);
	}

	public NonNullList<Object> getInput() {
		final NonNullList<Object> inputs = NonNullList.create();
		inputs.add(this.baseItem);
		inputs.addAll(OreDictionary.getOres(Oredicts.PLATE + this.materialName));
		return inputs;
	}
}
