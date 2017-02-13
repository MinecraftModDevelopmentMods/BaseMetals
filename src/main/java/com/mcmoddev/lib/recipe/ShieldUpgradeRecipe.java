package com.mcmoddev.lib.recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeRepairItem;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ShieldUpgradeRecipe extends RecipeRepairItem implements IRecipe {

	protected String matName;
	
	public ShieldUpgradeRecipe(MetalMaterial input) {
		matName = input.getName();
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack base;
		List<ItemStack> upgradeMats = new ArrayList<>();
		Collection<MetalMaterial> allmats = Materials.getAllMaterials();
		MetalMaterial input = Materials.getMaterialByName(matName);
		base = new ItemStack( input.shield, 1, 0);
		
		for( MetalMaterial mat : allmats ) {
			if( mat.hardness >= input.hardness && mat.getName() != matName ) {
				List<ItemStack> mats = OreDictionary.getOres(Oredicts.PLATE+mat.getCapitalizedName());
				upgradeMats.addAll(mats);
			}
		}
		
		boolean[] matches = new boolean[] { false, false };
		
		for( int i = 0; i < inv.getSizeInventory(); i++ ) {
			ItemStack curItem = inv.getStackInSlot(i);
			
			if( curItem != null ) {
				if( OreDictionary.itemMatches(base, curItem, false) ) {
					matches[0] = true;
				} else if( OreDictionary.containsMatch(false, upgradeMats, curItem) ) {
					matches[1] = true;
				}
			}
		}
		return matches[0]?matches[1]:false;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		Map<String,List<ItemStack>> plates = new HashMap<>();
		
		Collection<MetalMaterial> allmats = Materials.getAllMaterials();
		int hardness = ((Float)Materials.getMaterialByName(matName).hardness).intValue();
		
		for( MetalMaterial mat : allmats ) {
			if( mat.hardness >= hardness && mat.getName() != matName ) {
				List<ItemStack> mats = OreDictionary.getOres(Oredicts.PLATE+mat.getCapitalizedName());
				plates.put(mat.getName(), mats);
			}
		}
		
		ItemStack plateMatched = null;
		
		for( int i = 0; i < inv.getSizeInventory(); i++ ) {
			ItemStack curItem = inv.getStackInSlot(i);
			
			if( curItem != null ) {
				for( Entry<String, List<ItemStack>> ent : plates.entrySet() ) {
					if( OreDictionary.containsMatch(false, ent.getValue(), curItem) ) {
						plateMatched = new ItemStack( Materials.getMaterialByName(ent.getKey().toLowerCase()).shield, 1, 0 );
					}
				}
			}
		}

		return plateMatched;
	}
}
