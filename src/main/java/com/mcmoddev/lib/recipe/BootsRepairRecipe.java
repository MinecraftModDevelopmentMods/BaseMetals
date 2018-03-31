package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

public class BootsRepairRecipe extends RepairRecipeBase {

	public BootsRepairRecipe(final MMDMaterial material) {
		super(material, Names.BOOTS);
		// super(material, Names.BOOTS.toString(), material.getName() + "_" + Names.BOOTS,
		// Oredicts.PLATE + material.getCapitalizedName());
	}
}
