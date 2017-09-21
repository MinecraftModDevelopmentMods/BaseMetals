package com.mcmoddev.lib.recipe.conditions;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class ConditionEnabled implements IConditionFactory {

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		String optionName = JsonUtils.getString(json, "optionName").toLowerCase();
		String optionValue = JsonUtils.getString(json, "optionValue");
		
		switch(optionName) {
		case "material":
			return () -> Options.isMaterialEnabled(optionValue);
		case "mod":
			return () -> Options.isModEnabled(optionValue);
		case "thing":
			return () -> Options.isThingEnabled(optionValue);
		}
		return () -> false;
	}

}
