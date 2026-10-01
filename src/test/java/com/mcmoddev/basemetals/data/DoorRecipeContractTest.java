package com.mcmoddev.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class DoorRecipeContractTest {

	private static final Path RECIPES = Paths.get(
			"src", "main", "resources", "assets", "basemetals", "recipes");

	@Test
	void doorsAndTrapdoorsHaveDistinctCanonicalRecipes() throws IOException {
		final List<String> collisions = new ArrayList<>();
		final List<String> invalidDoorShapes = new ArrayList<>();
		final List<String> invalidTrapdoorShapes = new ArrayList<>();
		final List<String> invalidTrapdoorIngredients = new ArrayList<>();
		final List<String> invalidYields = new ArrayList<>();
		int pairs = 0;

		try (DirectoryStream<Path> doors = Files.newDirectoryStream(RECIPES, "*_door.json")) {
			for (final Path doorPath : doors) {
				final String fileName = doorPath.getFileName().toString();
				final String material = fileName.substring(0, fileName.length() - "_door.json".length());
				final Path trapdoorPath = RECIPES.resolve(material + "_trapdoor.json");

				assertTrue(Files.isRegularFile(trapdoorPath),
						"Missing trapdoor recipe for " + material);

				final JsonObject door = read(doorPath);
				final JsonObject trapdoor = read(trapdoorPath);
				final List<String> doorPattern = pattern(door);
				final List<String> trapdoorPattern = pattern(trapdoor);

				if (doorPattern.equals(trapdoorPattern)
						&& ingredient(door).equals(ingredient(trapdoor))) {
					collisions.add(material);
				}

				if (!doorPattern.equals(Arrays.asList("xx", "xx", "xx"))) {
					invalidDoorShapes.add(material + "=" + String.join("/", doorPattern));
				}

				final List<String> expectedTrapdoorPattern = "quartz".equals(material)
						? Collections.singletonList("x")
						: Arrays.asList("xx", "xx");

				if (!trapdoorPattern.equals(expectedTrapdoorPattern)) {
					invalidTrapdoorShapes.add(material + "=" + String.join("/", trapdoorPattern));
				}

				final String expectedTrapdoorOre = "quartz".equals(material)
						? "blockQuartz"
						: ingredient(door).get("ore").getAsString();

				if (!expectedTrapdoorOre.equals(ingredient(trapdoor).get("ore").getAsString())) {
					invalidTrapdoorIngredients.add(material + "=" + ingredient(trapdoor));
				}

				if (resultCount(door) != 3 || resultCount(trapdoor) != 1) {
					invalidYields.add(material + "=door:" + resultCount(door)
							+ ",trapdoor:" + resultCount(trapdoor));
				}

				pairs++;
			}
		}

		assertEquals(26, pairs, "Unexpected number of door/trapdoor recipe pairs");
		assertEquals(Collections.emptyList(), collisions,
				"Door and trapdoor recipes must not have identical inputs");
		assertEquals(Collections.emptyList(), invalidDoorShapes,
				"Doors must use six materials in the canonical 2x3 pattern");
		assertEquals(Collections.emptyList(), invalidTrapdoorShapes,
				"Trapdoors must use their canonical non-conflicting pattern");
		assertEquals(Collections.emptyList(), invalidTrapdoorIngredients,
				"Trapdoor ingredients changed");
		assertEquals(Collections.emptyList(), invalidYields,
				"Door and trapdoor crafting yields changed");
	}

	private static JsonObject read(final Path path) throws IOException {
		try (Reader reader = Files.newBufferedReader(path)) {
			return new JsonParser().parse(reader).getAsJsonObject();
		}
	}

	private static List<String> pattern(final JsonObject recipe) {
		final List<String> result = new ArrayList<>();
		final JsonArray pattern = recipe.getAsJsonArray("pattern");

		for (final JsonElement row : pattern) {
			result.add(row.getAsString());
		}

		return result;
	}

	private static JsonObject ingredient(final JsonObject recipe) {
		return recipe.getAsJsonObject("key").getAsJsonObject("x");
	}

	private static int resultCount(final JsonObject recipe) {
		final JsonObject result = recipe.getAsJsonObject("result");

		return result.has("count") ? result.get("count").getAsInt() : 1;
	}
}
