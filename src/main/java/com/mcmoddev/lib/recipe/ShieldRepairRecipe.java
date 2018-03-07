package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

public class ShieldRepairRecipe extends RepairRecipeBase {

	public ShieldRepairRecipe(final MMDMaterial material) {
		super(material, Names.SHIELD, Oredicts.SHIELD + material.getCapitalizedName(), Oredicts.PLATE + material.getCapitalizedName());
	}
}
