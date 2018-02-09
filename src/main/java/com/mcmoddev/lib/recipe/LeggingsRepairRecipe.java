package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

public class LeggingsRepairRecipe extends RepairRecipeBase {

	public LeggingsRepairRecipe(MMDMaterial mat) {
		super(mat, Names.LEGGINGS);
//		super(mat, Names.LEGGINGS.toString(), mat.getName() + "_" + Names.LEGGINGS, Oredicts.PLATE + mat.getCapitalizedName());
	}
}
