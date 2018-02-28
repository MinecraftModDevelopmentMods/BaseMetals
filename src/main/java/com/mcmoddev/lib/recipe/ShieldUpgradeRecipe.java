package com.mcmoddev.lib.recipe;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

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

	private String materialName;

	public ShieldUpgradeRecipe(final MMDMaterial input) {
		materialName = input.getName();
	}

	@Override
	public boolean matches(final InventoryCrafting inv, final World worldIn) {
		final NonNullList<ItemStack> upgradeMats = NonNullList.create();
		final Collection<MMDMaterial> allmats = Materials.getAllMaterials();
		final MMDMaterial input = Materials.getMaterialByName(materialName);
		final ItemStack base = new ItemStack(input.getItem(Names.SHIELD), 1, 0);

		for (final MMDMaterial mat : allmats) {
			if (mat.getStat(MaterialStats.HARDNESS) >= input.getStat(MaterialStats.HARDNESS) && mat.getName().equals(materialName)) {
				final NonNullList<ItemStack> mats = OreDictionary.getOres(Oredicts.PLATE + mat.getCapitalizedName());
				upgradeMats.addAll(mats);
			}
		}

		boolean[] matches = new boolean[] { false, false };

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack curItem = inv.getStackInSlot(i);

			if (!curItem.isEmpty()) {
				final ItemStack comp = new ItemStack(curItem.getItem(), 1, curItem.getItemDamage());
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
	public ItemStack getCraftingResult(final InventoryCrafting inv) {
		final Map<String, NonNullList<ItemStack>> plates = getPlates();

		Pair<Map<Enchantment, Integer>, ItemStack> matchedBits = Pair.of(Collections.emptyMap(), ItemStack.EMPTY);

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack curItem = inv.getStackInSlot(i);

			if (curItem != null) {
				matchedBits = matchAndFind(curItem, plates);
			}
		}

		BaseMetals.logger.debug("Adding %d enchantments to output item", matchedBits.getLeft().size());
		if (!(matchedBits.getRight().isEmpty())) {
			EnchantmentHelper.setEnchantments(matchedBits.getLeft(), matchedBits.getRight());
		}

		return matchedBits.getRight();
	}

	private Pair<Map<Enchantment, Integer>, ItemStack> matchAndFind(final ItemStack curItem,
			final Map<String, NonNullList<ItemStack>> plates) {
		final ItemStack comp = new ItemStack(curItem.getItem(), 1, curItem.getMetadata());
		Map<Enchantment, Integer> enchants = Collections.emptyMap();
		ItemStack plateMatched = null;
		final ItemStack matcher = new ItemStack(Materials.getMaterialByName(materialName).getItem(Names.SHIELD), 1, 0);

		for (final Entry<String, NonNullList<ItemStack>> ent : plates.entrySet()) {
			if (OreDictionary.containsMatch(false, ent.getValue(), comp)) {
				plateMatched = new ItemStack(Materials.getMaterialByName(ent.getKey().toLowerCase()).getItem(Names.SHIELD), 1, 0);
			}
		}

		if (OreDictionary.itemMatches(matcher, comp, false)) {
			enchants = EnchantmentHelper.getEnchantments(curItem);
		}
		return Pair.of(enchants, plateMatched);
	}

	private Map<String, NonNullList<ItemStack>> getPlates() {
		final Collection<MMDMaterial> allmats = Materials.getAllMaterials();
		final int hardness = ((Float) Materials.getMaterialByName(materialName).getStat(MaterialStats.HARDNESS)).intValue();
		final Map<String, NonNullList<ItemStack>> plates = new TreeMap<>();

		for (final MMDMaterial mat : allmats) {
			if (mat.getStat(MaterialStats.HARDNESS) >= hardness && (!mat.getName().equals(materialName))) {
				final NonNullList<ItemStack> mats = OreDictionary.getOres(Oredicts.PLATE + mat.getCapitalizedName());
				plates.put(mat.getName(), mats);
			}
		}
		return plates;
	}

	private MMDMaterial getUpgradeMat(final InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack curItem = inv.getStackInSlot(i);

			if (!curItem.isEmpty()) {
				for (final MMDMaterial e : Materials.getAllMaterials()) {
					if (OreDictionary.containsMatch(false, OreDictionary.getOres(Oredicts.PLATE + e.getName()), curItem)) {
						return e;
					}
				}
			}
		}
		return Materials.getMaterialByName(materialName);
	}

	private ItemStack findBaseItem(final InventoryCrafting inv) {
		final ItemStack input = new ItemStack(Materials.getMaterialByName(materialName).getItem(Names.SHIELD), 1, 0);

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack a = inv.getStackInSlot(i);
			if (!a.isEmpty()) {
				final ItemStack comp = new ItemStack(a.getItem(), 1, a.getMetadata());
				if (OreDictionary.itemMatches(input, comp, false)) {
					return a;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	private int getEnchantCount(final InventoryCrafting inv) {
		ItemStack k = findBaseItem(inv);
		if (!k.isEmpty()) {
			return EnchantmentHelper.getEnchantments(findBaseItem(inv)).size();
		} else {
			return 0;
		}
	}

	/**
	 *
	 * @param inv
	 * @return
	 */
	public int getCost(final InventoryCrafting inv) {
		final MMDMaterial shieldMat = Materials.getMaterialByName(materialName);
		final MMDMaterial upgradeMat = getUpgradeMat(inv);

		final float hardDiff = upgradeMat.getStat(MaterialStats.HARDNESS) - shieldMat.getStat(MaterialStats.HARDNESS);
		final int enchantCount = getEnchantCount(inv);

		final float diffVal = hardDiff * 5;
		final float enchantVal = upgradeMat.getStat(MaterialStats.MAGICAFFINITY) * enchantCount;
		Float finalVal = diffVal + enchantVal;

		if (finalVal < 5.0f) {
			finalVal = 5.0f;
		}

		return finalVal.intValue();
	}
}
