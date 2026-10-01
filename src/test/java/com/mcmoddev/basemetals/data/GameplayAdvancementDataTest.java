package com.mcmoddev.basemetals.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameplayAdvancementDataTest {
	private static final Path ROOT = Paths.get("build", "generated-resources",
			"gameplay-advancements", "assets", "basemetals", "advancements");
	private static final Set<String> IDS = new HashSet<>(Arrays.asList(
			"this_is_new", "blocktastic", "geologist", "metallurgy", "aquarium_maker",
			"brass_maker", "bronze_maker", "cupronickel_maker", "electrum_maker",
			"invar_maker", "mithril_maker", "pewter_maker", "steel_maker",
			"angel_of_death", "scuba_diver", "demon_slayer", "juggernaut", "moon_boots"));

	@Test
	void restoresEveryHistoricalAchievementWithVisibleNotificationsAndSafeConditions() throws Exception {
		try (Stream<Path> files = Files.list(ROOT)) {
			assertEquals(IDS, files.map(path -> path.getFileName().toString().replace(".json", ""))
					.collect(Collectors.toSet()));
		}

		for (final String id : IDS) {
			final JsonObject json = read(ROOT.resolve(id + ".json"));
			final JsonObject display = json.getAsJsonObject("display");

			assertTrue(display.get("show_toast").getAsBoolean(), id);
			assertTrue(display.get("announce_to_chat").getAsBoolean(), id);
			assertEquals("achievement." + id,
					display.getAsJsonObject("title").get("translate").getAsString());
			assertEquals("achievement." + id + ".desc",
					display.getAsJsonObject("description").get("translate").getAsString());
			assertEquals("minecraft:impossible", json.getAsJsonObject("criteria")
					.getAsJsonObject("event").get("trigger").getAsString());

			final JsonObject condition = json.getAsJsonArray("conditions").get(0).getAsJsonObject();

			assertEquals("basemetals:achievement", condition.get("type").getAsString());
			assertTrue(strings(condition.getAsJsonArray("items")).contains(
					display.getAsJsonObject("icon").get("item").getAsString()), id);

			final String parent = json.get("parent").getAsString();

			if (parent.startsWith("basemetals:")) {
				final String parentId = parent.substring("basemetals:".length());

				assertTrue(IDS.contains(parentId), "Missing parent for " + id);

				final JsonObject parentJson = read(ROOT.resolve(parentId + ".json"));

				assertTrue(strings(condition.getAsJsonArray("items")).contains(
						parentJson.getAsJsonObject("display").getAsJsonObject("icon")
								.get("item").getAsString()),
						"Unsafe disabled parent for " + id);
			}
		}

		final JsonObject factories = read(Paths.get("src", "main", "resources", "assets",
				"basemetals", "advancements", "_factories.json"));

		assertEquals("com.mcmoddev.basemetals.advancement.AchievementCondition",
				factories.getAsJsonObject("conditions").get("achievement").getAsString());
	}

	@Test
	void everyLocaleContainsAllAchievementNamesAndDescriptions() throws Exception {
		final List<Path> locales;

		try (Stream<Path> files = Files.list(Paths.get("src", "main", "resources",
				"assets", "basemetals", "lang"))) {
			locales = files.filter(path -> path.toString().endsWith(".lang")).collect(Collectors.toList());
		}

		assertEquals(18, locales.size());

		for (final Path locale : locales) {
			final Properties translations = new Properties();

			try (Reader reader = Files.newBufferedReader(locale, StandardCharsets.UTF_8)) {
				translations.load(reader);
			}

			for (final String id : IDS) {
				for (final String key : Arrays.asList("achievement." + id, "achievement." + id + ".desc")) {
					assertTrue(translations.containsKey(key), locale + " is missing " + key);
					assertTrue(!translations.getProperty(key).trim().isEmpty(), locale + " has empty " + key);
				}
			}
		}
	}

	private static Set<String> strings(final JsonArray array) {
		final Set<String> result = new HashSet<>();

		for (final JsonElement value : array) {
			result.add(value.getAsString());
		}

		return result;
	}

	private static JsonObject read(final Path path) throws Exception {
		try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			return new JsonParser().parse(reader).getAsJsonObject();
		}
	}
}
