package com.mcmoddev.lib.recipe;

import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ChestplateRepairRecipe extends ShapelessOreRecipe implements IRecipe {

	private ItemStack baseChestplate;
	private String matName;
	
	public ChestplateRepairRecipe(MetalMaterial mat) {
		super(new ItemStack(mat.chestplate,1), new Object[] {mat.getName()+"_chestplate", Oredicts.PLATE+mat.getCapitalizedName()});
		baseChestplate = new ItemStack(mat.chestplate, 1, OreDictionary.WILDCARD_VALUE);
		matName = new String(mat.getCapitalizedName());
	}

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
    	List<ItemStack> repairMaterials = OreDictionary.getOres(Oredicts.PLATE+matName);
        boolean chestplateMatched = false;
        boolean repairMatched = false;
        
        for( int i = 0; i < inv.getSizeInventory(); i++ ) {
            ItemStack item = inv.getStackInSlot(i);
            if( !repairMatched ) {
            	repairMatched = OreDictionary.containsMatch(false, repairMaterials, item);
            }
            if( !chestplateMatched ) {
            	chestplateMatched = (OreDictionary.itemMatches(baseChestplate, item, false))?(item.getItemDamage()>0?true:false):false;
            }
        }
        return chestplateMatched?repairMatched:false;
    }

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return new ItemStack(baseChestplate.getItem(),1, 0);
	}

	@Override
	public int getRecipeSize() {
		int count = 1;
		count += OreDictionary.getOres(Oredicts.PLATE+matName).size();
		return count;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(baseChestplate.getItem(), 1, 0);
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		ItemStack[] outputStacks = new ItemStack[inv.getSizeInventory()];
    	List<ItemStack> repairMaterials = OreDictionary.getOres(Oredicts.PLATE+matName);
		
		for( int i = 0; i < inv.getSizeInventory(); i++ ) {
			ItemStack item = inv.getStackInSlot(i);
			if( OreDictionary.containsMatch(true, repairMaterials, item) ) {
				item.stackSize--;
				if( item.stackSize <= 0 ) {
					outputStacks[i] = null;
				} else {
					outputStacks[i] = item;
				}
			} else {
				outputStacks[i] = item;
			}
		}
		return outputStacks;
	}

	@Override
	public ArrayList<Object> getInput() {
		ArrayList<Object> inputs = new ArrayList<>();
		inputs.add(baseChestplate);
		inputs.addAll(OreDictionary.getOres(Oredicts.PLATE+matName));
		return inputs;
	}
}
