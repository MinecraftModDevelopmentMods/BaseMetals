package com.mcmoddev.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class RecipeBookAdvancementCoverageTest {

	private static final Path RECIPES = Paths.get(
			"src", "main", "resources", "assets", "basemetals", "recipes");
	private static final Path ADVANCEMENTS = Paths.get(
			"build", "generated-resources", "recipe-advancements", "assets",
			"basemetals", "advancements", "recipes", "materials");
	private static final Path FACTORIES = Paths.get(
			"src", "main", "resources", "assets", "basemetals", "advancements",
			"_factories.json");

	private static final Map<String, String> MATERIAL_ORES = materialOres();
	private static final Set<String> GENERIC_STEEL_RECIPES = new LinkedHashSet<>(Arrays.asList(
			"activator_rail", "detector_rail", "flint_and_steel", "human_detector",
			"minecart", "piston", "rail", "tripwire_hook"));

	@Test
	void everyRecipeHasAConditionAwareMaterialUnlock() throws IOException {
		assertTrue(Files.isRegularFile(FACTORIES),
				"Recipe advancements need their MMDLib condition factory");

		final JsonObject factories = read(FACTORIES);

		assertEquals("com.mcmoddev.lib.recipe.conditions.ConditionEnabled",
				factories.getAsJsonObject("conditions").get("enabled").getAsString());
		assertEquals("com.mcmoddev.lib.recipe.conditions.HammerEnabled",
				factories.getAsJsonObject("conditions").get("hammer").getAsString());
		assertEquals("com.mcmoddev.lib.recipe.conditions.PlateRepairEnabled",
				factories.getAsJsonObject("conditions").get("plate").getAsString());

		final Set<Path> expectedAdvancements = new LinkedHashSet<>();

		try (Stream<Path> stream = Files.list(RECIPES)) {
			for (final Path recipePath : stream.filter(path -> path.toString().endsWith(".json"))
					.filter(path -> !"_factories.json".equals(path.getFileName().toString()))
					.filter(path -> !path.getFileName().toString().endsWith("_plate_repair.json"))
					.sorted().collect(Collectors.toList())) {
				final String recipeName = withoutExtension(recipePath.getFileName().toString());
				final String material = materialFor(recipeName);

				assertNotNull(material, "No recipe-book discovery material for " + recipeName);

				final Path advancementPath = ADVANCEMENTS.resolve(material)
						.resolve(recipeName + ".json");

				expectedAdvancements.add(advancementPath.normalize());
				assertTrue(Files.isRegularFile(advancementPath),
						"Missing generated recipe advancement " + advancementPath);
				assertAdvancement(recipePath, advancementPath, recipeName, material);
			}
		}

		final Set<Path> actualAdvancements;

		try (Stream<Path> stream = Files.walk(ADVANCEMENTS)) {
			actualAdvancements = stream.filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".json"))
					.map(Path::normalize).collect(Collectors.toCollection(LinkedHashSet::new));
		}

		assertEquals(expectedAdvancements, actualAdvancements,
				"Generated recipe advancements must exactly follow the recipe catalogue");
	}

	private static void assertAdvancement(final Path recipePath, final Path advancementPath,
			final String recipeName, final String material) throws IOException {
		final JsonObject recipe = read(recipePath);
		final JsonObject advancement = read(advancementPath);

		assertEquals("minecraft:recipes/root", advancement.get("parent").getAsString());

		final JsonArray rewards = advancement.getAsJsonObject("rewards").getAsJsonArray("recipes");

		assertEquals(1, rewards.size());
		assertEquals("basemetals:" + recipeName, rewards.get(0).getAsString());

		final JsonObject criteria = advancement.getAsJsonObject("criteria");
		final JsonObject acquired = criteria.getAsJsonObject("has_material");

		assertEquals("minecraft:inventory_changed", acquired.get("trigger").getAsString());

		final JsonObject predicate = acquired.getAsJsonObject("conditions")
				.getAsJsonArray("items").get(0).getAsJsonObject();

		assertEquals("forge:ore_dict", predicate.get("type").getAsString());
		assertEquals(MATERIAL_ORES.get(material), predicate.get("ore").getAsString());

		final JsonObject unlocked = criteria.getAsJsonObject("has_the_recipe");

		assertEquals("minecraft:recipe_unlocked", unlocked.get("trigger").getAsString());
		assertEquals("basemetals:" + recipeName,
				unlocked.getAsJsonObject("conditions").get("recipe").getAsString());

		final JsonArray requirements = advancement.getAsJsonArray("requirements");

		assertEquals(1, requirements.size());
		assertEquals(new LinkedHashSet<>(Arrays.asList("has_material", "has_the_recipe")),
				strings(requirements.get(0).getAsJsonArray()));

		if (recipe.has("conditions")) {
			assertEquals(advancementConditions(recipe.get("conditions")),
					advancement.get("conditions"),
					"Advancement conditions must preserve " + recipeName);
		} else {
			assertFalse(advancement.has("conditions"),
					"Unconditional recipe gained conditions: " + recipeName);
		}
	}

	private static JsonElement advancementConditions(final JsonElement value) {
		if (value.isJsonArray()) {
			final JsonArray result = new JsonArray();

			for (final JsonElement child : value.getAsJsonArray()) {
				result.add(advancementConditions(child));
			}

			return result;
		}

		if (value.isJsonObject()) {
			final JsonObject result = new JsonObject();

			for (final Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
				JsonElement child = entry.getValue();

				if ("type".equals(entry.getKey()) && child.isJsonPrimitive()
						&& child.getAsString().startsWith("mmdlib:")) {
					child = new com.google.gson.JsonPrimitive("basemetals:"
							+ child.getAsString().substring("mmdlib:".length()));
				} else {
					child = advancementConditions(child);
				}

				result.add(entry.getKey(), child);
			}

			return result;
		}

		return value;
	}

	private static String materialFor(final String recipeName) {
		if (GENERIC_STEEL_RECIPES.contains(recipeName)) {
			return "steel";
		}

		for (final String material : MATERIAL_ORES.keySet()) {
			if (recipeName.equals(material) || recipeName.startsWith(material + "_")) {
				return material;
			}
		}

		return null;
	}

	private static String withoutExtension(final String fileName) {
		return fileName.substring(0, fileName.length() - ".json".length());
	}

	private static JsonObject read(final Path path) throws IOException {
		try (Reader reader = Files.newBufferedReader(path)) {
			return new JsonParser().parse(reader).getAsJsonObject();
		}
	}

	private static Set<String> strings(final JsonArray values) {
		final Set<String> result = new LinkedHashSet<>();

		for (final JsonElement value : values) {
			result.add(value.getAsString());
		}

		return result;
	}

	private static Map<String, String> materialOres() {
		final Map<String, String> ores = new LinkedHashMap<>();

		ores.put("adamantine", "ingotAdamantine");
		ores.put("antimony", "ingotAntimony");
		ores.put("aquarium", "ingotAquarium");
		ores.put("bismuth", "ingotBismuth");
		ores.put("brass", "ingotBrass");
		ores.put("bronze", "ingotBronze");
		ores.put("charcoal", "charcoal");
		ores.put("coal", "coal");
		ores.put("coldiron", "ingotColdiron");
		ores.put("copper", "ingotCopper");
		ores.put("cupronickel", "ingotCupronickel");
		ores.put("diamond", "gemDiamond");
		ores.put("electrum", "ingotElectrum");
		ores.put("emerald", "gemEmerald");
		ores.put("gold", "ingotGold");
		ores.put("invar", "ingotInvar");
		ores.put("iron", "ingotIron");
		ores.put("lead", "ingotLead");
		ores.put("mithril", "ingotMithril");
		ores.put("nickel", "ingotNickel");
		ores.put("obsidian", "ingotObsidian");
		ores.put("pewter", "ingotPewter");
		ores.put("platinum", "ingotPlatinum");
		ores.put("quartz", "gemQuartz");
		ores.put("redstone", "dustRedstone");
		ores.put("silver", "ingotSilver");
		ores.put("starsteel", "ingotStarsteel");
		ores.put("steel", "ingotSteel");
		ores.put("stone", "stone");
		ores.put("tin", "ingotTin");
		ores.put("wood", "logWood");
		ores.put("zinc", "ingotZinc");

		return ores;
	}
}
