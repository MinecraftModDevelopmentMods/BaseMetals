package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;


public class LeggingsRepairRecipe extends RepairRecipeBase {

	public LeggingsRepairRecipe(MMDMaterial mat) {
		super( mat, Names.LEGGINGS.toString(), mat.getName() + "_leggings", Oredicts.PLATE + mat.getCapitalizedName());
	}
}
