package com.mcmoddev.lib.recipe;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeRepairItem;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ShieldUpgradeRecipe extends RecipeRepairItem {

	protected String matName;

	public ShieldUpgradeRecipe(MMDMaterial input) {
		matName = input.getName();
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack base;
		NonNullList<ItemStack> upgradeMats = NonNullList.create();
		Collection<MMDMaterial> allmats = Materials.getAllMaterials();
		MMDMaterial input = Materials.getMaterialByName(matName);
		base = new ItemStack(input.getItem(Names.SHIELD), 1, 0);

		for (MMDMaterial mat : allmats) {
			if (mat.getStat(MaterialStats.HARDNESS) >= input.getStat(MaterialStats.HARDNESS) && mat.getName().equals(matName)) {
				NonNullList<ItemStack> mats = OreDictionary.getOres(Oredicts.PLATE + mat.getCapitalizedName());
				upgradeMats.addAll(mats);
			}
		}

		boolean[] matches = new boolean[] { false, false };

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack curItem = inv.getStackInSlot(i);

			if (curItem != null) {
				ItemStack comp = new ItemStack(curItem.getItem(), 1, curItem.getItemDamage());
				if (OreDictionary.itemMatches(base, comp, false)) {
					matches[0] = true;
				} else if (OreDictionary.containsMatch(false, upgradeMats, comp)) {
					matches[1] = true;
				}
			}
		}
		return matches[0] ? matches[1] : false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		Map<String, NonNullList<ItemStack>> plates = new HashMap<>();

		Collection<MMDMaterial> allmats = Materials.getAllMaterials();
		int hardness = ((Float) Materials.getMaterialByName(matName).getStat(MaterialStats.HARDNESS)).intValue();

		for (MMDMaterial mat : allmats) {
			if (mat.getStat(MaterialStats.HARDNESS) >= hardness && (!mat.getName().equals(matName))) {
				NonNullList<ItemStack> mats = OreDictionary.getOres(Oredicts.PLATE + mat.getCapitalizedName());
				plates.put(mat.getName(), mats);
			}
		}

		ItemStack plateMatched = null;
		Map<Enchantment, Integer> enchants = Collections.emptyMap();
		ItemStack matcher = new ItemStack(Materials.getMaterialByName(matName).getItem(Names.SHIELD), 1, 0);

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack curItem = inv.getStackInSlot(i);

			if (curItem != null) {
				ItemStack comp = new ItemStack(curItem.getItem(), 1, curItem.getMetadata());
				for (Entry<String, NonNullList<ItemStack>> ent : plates.entrySet()) {
					if (OreDictionary.containsMatch(false, ent.getValue(), comp)) {
						plateMatched = new ItemStack(Materials.getMaterialByName(ent.getKey().toLowerCase()).getItem(Names.SHIELD), 1, 0);
					}
				}
				if (OreDictionary.itemMatches(matcher, comp, false)) {
					enchants = EnchantmentHelper.getEnchantments(curItem);
				}
			}
		}

		BaseMetals.logger.debug("Adding %d enchantments to output item", enchants.size());
		EnchantmentHelper.setEnchantments(enchants, plateMatched);
		return plateMatched;
	}

	private MMDMaterial getUpgradeMat(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack curItem = inv.getStackInSlot(i);

			if (curItem != null) {
				for (MMDMaterial e : Materials.getAllMaterials()) {
					if (OreDictionary.containsMatch(false, OreDictionary.getOres(Oredicts.PLATE + e.getName()), curItem)) {
						return e;
					}
				}
			}
		}
		return Materials.getMaterialByName(matName);
	}

	private ItemStack findBaseItem(InventoryCrafting inv) {
		ItemStack input = new ItemStack(Materials.getMaterialByName(matName).getItem(Names.SHIELD), 1, 0);

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack a = inv.getStackInSlot(i);
			if (a != null) {
				ItemStack comp = new ItemStack(a.getItem(), 1, a.getMetadata());
				if (OreDictionary.itemMatches(input, comp, false)) {
					return a;
				}
			}
		}
		return null;
	}

	private int getEnchantCount(InventoryCrafting inv) {
		ItemStack k = findBaseItem(inv);
		if (k != null) {
			return EnchantmentHelper.getEnchantments(findBaseItem(inv)).size();
		} else {
			return 0;
		}
	}

	public int getCost(InventoryCrafting inv) {
		MMDMaterial shieldMat = Materials.getMaterialByName(matName);
		MMDMaterial upgradeMat = getUpgradeMat(inv);

		float hardDiff = upgradeMat.getStat(MaterialStats.HARDNESS) - shieldMat.getStat(MaterialStats.HARDNESS);
		int enchantCount = getEnchantCount(inv);

		float diffVal = hardDiff * 5;
		float enchantVal = upgradeMat.getStat(MaterialStats.MAGICAFFINITY) * enchantCount;
		Float finalVal = diffVal + enchantVal;

		if (finalVal < 5.0f) {
			finalVal = 5.0f;
		}

		return finalVal.intValue();
	}
}
