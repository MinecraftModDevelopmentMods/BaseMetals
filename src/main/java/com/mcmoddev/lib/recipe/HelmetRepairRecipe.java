package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

public class HelmetRepairRecipe extends RepairRecipeBase {

	public HelmetRepairRecipe(MMDMaterial mat) {
		super(mat, Names.HELMET.toString(), mat.getName() + "_helmet", Oredicts.PLATE + mat.getCapitalizedName());
	}
}
