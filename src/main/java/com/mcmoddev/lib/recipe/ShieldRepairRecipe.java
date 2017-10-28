package com.mcmoddev.lib.recipe;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

public class ShieldRepairRecipe extends RepairRecipeBase {

	public ShieldRepairRecipe(MMDMaterial mat) {
		super( mat, Names.SHIELD.toString(), Oredicts.SHIELD + mat.getCapitalizedName(), Oredicts.PLATE + mat.getCapitalizedName());
	}
}
