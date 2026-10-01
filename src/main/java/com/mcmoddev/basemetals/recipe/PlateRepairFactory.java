package com.mcmoddev.basemetals.recipe;

import java.util.Locale;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

/** Reads the material and armor type from a plate-repair recipe. */
public final class PlateRepairFactory implements IRecipeFactory {
	@Override
	public IRecipe parse(final JsonContext context, final JsonObject json) {
		final String materialName = JsonUtils.getString(json, "material");
		final MMDMaterial material = Materials.getMaterialByName(
				materialName.toLowerCase(Locale.ROOT));

		if (material == null || material.isEmpty()) {
			throw new JsonSyntaxException("Unknown plate-repair material " + materialName);
		}

		final String armorType = JsonUtils.getString(json, "armorType").toLowerCase(Locale.ROOT);
		final Names form;

		switch (armorType) {
			case "boots" :
				form = Names.BOOTS;
				break;
			case "chestplate" :
				form = Names.CHESTPLATE;
				break;
			case "helmet" :
				form = Names.HELMET;
				break;
			case "leggings" :
				form = Names.LEGGINGS;
				break;
			case "shield" :
				form = Names.SHIELD;
				break;
			default :
				throw new JsonSyntaxException("Unknown plate-repair armor type " + armorType);
		}

		final ItemStack target = material.getItemStack(form);

		if (target.isEmpty()) {
			throw new JsonSyntaxException("Missing plate-repair target " + materialName
					+ " " + armorType);
		}

		return new PlateRepairRecipe(material, form, target.getItem(),
				Oredicts.PLATE + material.getCapitalizedName());
	}
}
