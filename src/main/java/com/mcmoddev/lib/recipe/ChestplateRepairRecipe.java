package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

public class ChestplateRepairRecipe extends RepairRecipeBase {

	public ChestplateRepairRecipe(final MMDMaterial material) {
		super(material, Names.CHESTPLATE);
		// super(material, Names.CHESTPLATE.toString(), material.getName() + "_" + Names.CHESTPLATE,
		// Oredicts.PLATE + material.getCapitalizedName());
	}
}
