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

public class HelmetRepairRecipe extends ShapelessOreRecipe implements IRecipe {

	private ItemStack baseHelmet;
	private String matName;
	
	public HelmetRepairRecipe(MetalMaterial mat) {
		super(new ItemStack(mat.helmet,1), new Object[] {mat.getName()+"_helmet", Oredicts.PLATE+mat.getCapitalizedName()});
		baseHelmet = new ItemStack(mat.helmet, 1, OreDictionary.WILDCARD_VALUE);
		matName = new String(mat.getCapitalizedName());
	}

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
    	List<ItemStack> repairMaterials = OreDictionary.getOres(Oredicts.PLATE+matName);
        boolean helmetMatched = false;
        boolean repairMatched = false;
        
        for( int i = 0; i < inv.getSizeInventory(); i++ ) {
            ItemStack item = inv.getStackInSlot(i);
            if( !repairMatched ) {
            	repairMatched = OreDictionary.containsMatch(false, repairMaterials, item);
            }
            if( !helmetMatched ) {
            	helmetMatched = (OreDictionary.itemMatches(baseHelmet, item, false))?(item.getItemDamage()>0?true:false):false;
            }
        }
        return helmetMatched?repairMatched:false;
    }

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return new ItemStack(baseHelmet.getItem(),1, 0);
	}

	@Override
	public int getRecipeSize() {
		int count = 1;
		count += OreDictionary.getOres(Oredicts.PLATE+matName).size();
		return count;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(baseHelmet.getItem(), 1, 0);
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
		inputs.add(baseHelmet);
		inputs.addAll(OreDictionary.getOres(Oredicts.PLATE+matName));
		return inputs;
	}
}
