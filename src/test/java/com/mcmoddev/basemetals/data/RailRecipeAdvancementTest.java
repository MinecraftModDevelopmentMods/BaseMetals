package com.mcmoddev.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class RailRecipeAdvancementTest {

	private static final Path ADVANCEMENT = Paths.get(
			"src", "main", "resources", "assets", "basemetals", "advancements",
			"recipes", "transportation", "rail.json");

	@Test
	void ironOrSteelUnlocksBothRailRecipes() throws IOException {
		assertTrue(Files.isRegularFile(ADVANCEMENT),
				"Missing recipe-book advancement " + ADVANCEMENT);

		final JsonObject advancement = read(ADVANCEMENT);
		assertEquals("minecraft:recipes/root", advancement.get("parent").getAsString());
		assertEquals(setOf("minecraft:rail", "basemetals:rail"),
				strings(advancement.getAsJsonObject("rewards").getAsJsonArray("recipes")));

		final JsonObject criteria = advancement.getAsJsonObject("criteria");
		assertInventoryCriterion(criteria.getAsJsonObject("has_iron_ingot"),
				"minecraft:iron_ingot");
		assertInventoryCriterion(criteria.getAsJsonObject("has_steel_ingot"),
				"basemetals:steel_ingot");
		final JsonObject unlocked = criteria.getAsJsonObject("has_the_recipe");
		assertEquals("minecraft:recipe_unlocked", unlocked.get("trigger").getAsString());
		assertEquals("basemetals:rail",
				unlocked.getAsJsonObject("conditions").get("recipe").getAsString());

		final JsonArray requirements = advancement.getAsJsonArray("requirements");
		assertEquals(1, requirements.size(), "The three discovery paths must be alternatives");
		assertEquals(setOf("has_iron_ingot", "has_steel_ingot", "has_the_recipe"),
				strings(requirements.get(0).getAsJsonArray()));
	}

	private static void assertInventoryCriterion(final JsonObject criterion, final String item) {
		assertEquals("minecraft:inventory_changed", criterion.get("trigger").getAsString());
		final JsonArray items = criterion.getAsJsonObject("conditions").getAsJsonArray("items");
		assertEquals(1, items.size());
		assertEquals(item, items.get(0).getAsJsonObject().get("item").getAsString());
	}

	private static JsonObject read(final Path path) throws IOException {
		try (Reader reader = Files.newBufferedReader(path)) {
			return new JsonParser().parse(reader).getAsJsonObject();
		}
	}

	private static Set<String> strings(final JsonArray values) {
		final Set<String> result = new HashSet<>();
		for (final JsonElement value : values) {
			result.add(value.getAsString());
		}
		return result;
	}

	private static Set<String> setOf(final String... values) {
		return new HashSet<>(Arrays.asList(values));
	}
}
