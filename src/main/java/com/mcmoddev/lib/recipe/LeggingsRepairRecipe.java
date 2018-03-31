package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

public class LeggingsRepairRecipe extends RepairRecipeBase {

	public LeggingsRepairRecipe(final MMDMaterial material) {
		super(material, Names.LEGGINGS);
		// super(material, Names.LEGGINGS.toString(), material.getName() + "_" + Names.LEGGINGS,
		// Oredicts.PLATE + material.getCapitalizedName());
	}
}
