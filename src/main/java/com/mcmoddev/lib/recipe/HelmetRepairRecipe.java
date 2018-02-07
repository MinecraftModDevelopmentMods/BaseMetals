package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

public class HelmetRepairRecipe extends RepairRecipeBase {

	public HelmetRepairRecipe(MMDMaterial mat) {
		super(mat, Names.HELMET);
//		super(mat, Names.HELMET.toString(), mat.getName() + "_" + Names.HELMET, Oredicts.PLATE + mat.getCapitalizedName());
	}
}
