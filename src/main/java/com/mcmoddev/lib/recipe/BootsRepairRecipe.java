package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

public class BootsRepairRecipe extends RepairRecipeBase {

	public BootsRepairRecipe(MMDMaterial mat) {
		super(mat, Names.BOOTS.toString(), mat.getName() + "_boots", Oredicts.PLATE + mat.getCapitalizedName());
	}
}
