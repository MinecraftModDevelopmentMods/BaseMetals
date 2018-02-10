package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

public class BootsRepairRecipe extends RepairRecipeBase {

	public BootsRepairRecipe(MMDMaterial mat) {
		super(mat, Names.BOOTS);
//		super(mat, Names.BOOTS.toString(), mat.getName() + "_" + Names.BOOTS, Oredicts.PLATE + mat.getCapitalizedName());
	}
}
