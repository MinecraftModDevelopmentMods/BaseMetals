package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

public class HelmetRepairRecipe extends RepairRecipeBase {

	public HelmetRepairRecipe(final MMDMaterial material) {
		super(material, Names.HELMET);
		// super(material, Names.HELMET.toString(), material.getName() + "_" + Names.HELMET,
		// Oredicts.PLATE + material.getCapitalizedName());
	}
}
