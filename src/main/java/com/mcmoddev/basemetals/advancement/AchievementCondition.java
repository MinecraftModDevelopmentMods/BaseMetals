package com.mcmoddev.basemetals.advancement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmoddev.lib.util.Config.Options;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/** Omits achievements whose settings or required items are disabled. */
public final class AchievementCondition implements IConditionFactory {

	@Override
	public BooleanSupplier parse(final JsonContext context, final JsonObject json) {
		final List<ResourceLocation> items = new ArrayList<>();

		for (final JsonElement item : json.getAsJsonArray("items")) {
			items.add(new ResourceLocation(item.getAsString()));
		}

		return () -> Options.enableAchievements()
				&& items.stream().allMatch(Item.REGISTRY::containsKey);
	}
}
