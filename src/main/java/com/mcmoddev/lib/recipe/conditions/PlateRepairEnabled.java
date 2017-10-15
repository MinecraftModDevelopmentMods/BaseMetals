package com.mcmoddev.lib.recipe.conditions;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class PlateRepairEnabled implements IConditionFactory {

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return () -> Options.enablePlateRepairs();
	}

}
