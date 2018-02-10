package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

public class ChestplateRepairRecipe extends RepairRecipeBase {

	public ChestplateRepairRecipe(MMDMaterial mat) {
		super(mat, Names.CHESTPLATE);
//		super(mat, Names.CHESTPLATE.toString(), mat.getName() + "_" + Names.CHESTPLATE,
//				Oredicts.PLATE + mat.getCapitalizedName());
	}
}
