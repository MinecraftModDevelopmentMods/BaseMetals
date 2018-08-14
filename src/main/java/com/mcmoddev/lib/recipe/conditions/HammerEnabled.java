package com.mcmoddev.lib.recipe.conditions;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;
import com.mcmoddev.lib.data.ConfigKeys;
import com.mcmoddev.lib.util.Config.Options;

import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class HammerEnabled implements IConditionFactory {

	@Override
	public BooleanSupplier parse(final JsonContext context, final JsonObject json) {
		return () -> !Options.disableAllHammerRecipes()
				&& Options.isThingEnabled(ConfigKeys.CRACKHAMMER);
	}

}
