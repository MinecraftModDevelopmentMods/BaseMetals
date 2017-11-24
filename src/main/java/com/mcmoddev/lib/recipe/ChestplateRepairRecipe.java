package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

public class ChestplateRepairRecipe extends RepairRecipeBase {

	public ChestplateRepairRecipe(MMDMaterial mat) {
		super(mat, Names.CHESTPLATE.toString(), mat.getName() + "_chestplate",
				Oredicts.PLATE + mat.getCapitalizedName());
	}

}
