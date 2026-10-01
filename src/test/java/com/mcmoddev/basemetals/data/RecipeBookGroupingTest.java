package com.mcmoddev.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class RecipeBookGroupingTest {

	private static final Path RECIPES = Paths.get(
			"src", "main", "resources", "assets", "basemetals", "recipes");

	@Test
	void eachCraftedOutputHasItsOwnRecipeBookCell() throws IOException {
		int checked = 0;

		try (Stream<Path> stream = Files.list(RECIPES)) {
			final List<Path> recipes = stream
					.filter(path -> path.toString().endsWith(".json"))
					.filter(path -> !"_factories.json".equals(path.getFileName().toString()))
					.sorted().collect(Collectors.toList());

			for (final Path recipePath : recipes) {
				final JsonObject recipe = read(recipePath);

				if (!recipe.has("result") || !recipe.get("result").isJsonObject()) {
					continue;
				}

				final JsonObject result = recipe.getAsJsonObject("result");

				if (!result.has("item")) {
					continue;
				}

				final String output = result.get("item").getAsString();

				assertTrue(recipe.has("group"),
						"Recipe has no recipe-book group: " + recipePath.getFileName());
				assertEquals(output, recipe.get("group").getAsString(),
						"Recipe-book group must match its crafted output: "
								+ recipePath.getFileName());
				checked++;
			}
		}

		assertTrue(checked > 1000, "Expected to check the complete recipe catalogue");
	}

	private static JsonObject read(final Path path) throws IOException {
		try (Reader reader = Files.newBufferedReader(path)) {
			return new JsonParser().parse(reader).getAsJsonObject();
		}
	}
}
