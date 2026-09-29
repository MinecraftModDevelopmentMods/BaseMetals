package com.mcmoddev.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.mcmoddev.basemetals.content.ContentMode;
import com.mcmoddev.basemetals.content.ContentPolicy;
import com.mcmoddev.basemetals.worldgen.BaseMetalsWorldgenProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class OreSpawnProviderContractTest {

	private static final Path DISABLED_EXAMPLE = java.nio.file.Paths.get(
			"docs", "examples", "basemetals-orespawn-disabled.json");

	@Test
	void nativeProviderMatchesTheStableTranslatedContract() {
		final JsonObject provider = BaseMetalsWorldgenProvider
				.build(ContentPolicy.forMode(ContentMode.HIGH_FANTASY)).toJson();
		assertProviderIdentity(provider);

		final JsonObject ores = provider.getAsJsonObject("ores");
		assertEquals(expectedRules().keySet(), keys(ores));
		for (final Map.Entry<String, Rule> expected : expectedRules().entrySet()) {
			assertRule(ores.getAsJsonObject(expected.getKey()), expected.getKey(), expected.getValue(), true);
		}
	}

	@Test
	void contentModesOnlyChangeTheThreeFreshWorldFantasyDefaults() {
		final JsonObject high = provider(ContentMode.HIGH_FANTASY);
		final JsonObject low = provider(ContentMode.LOW_FANTASY);
		final JsonObject realism = provider(ContentMode.REALISM);
		for (final String id : expectedRules().keySet()) {
			assertTrue(high.getAsJsonObject("ores").getAsJsonObject(id).get("enabled").getAsBoolean(), id);
			assertTrue(low.getAsJsonObject("ores").getAsJsonObject(id).get("enabled").getAsBoolean(), id);
			final boolean mythical = id.contains("adamantine") || id.contains("coldiron")
					|| id.contains("starsteel");
			assertEquals(!mythical, realism.getAsJsonObject("ores").getAsJsonObject(id)
					.get("enabled").getAsBoolean(), id);
		}
	}

	@Test
	void disabledExampleRetainsEveryRuleButDisablesAllOfThem() throws IOException {
		final JsonObject example = read(DISABLED_EXAMPLE);
		assertProviderIdentity(example);

		final JsonObject ores = example.getAsJsonObject("ores");
		assertEquals(expectedRules().keySet(), keys(ores));
		for (final Map.Entry<String, Rule> expected : expectedRules().entrySet()) {
			assertRule(ores.getAsJsonObject(expected.getKey()), expected.getKey(), expected.getValue(), false);
		}
	}

	private static void assertProviderIdentity(final JsonObject provider) {
		assertEquals(5, provider.get("schema_version").getAsInt());
		assertEquals("basemetals", provider.get("provider_modid").getAsString());
		assertEquals(2, provider.get("provider_revision").getAsInt());
		assertTrue(provider.get("merge_new_entries_into_existing_worlds").getAsBoolean());
		assertTrue(provider.getAsJsonObject("rocks").entrySet().isEmpty());
		for (final String section : Arrays.asList("fluid_deposits", "geomes", "biome_rules",
				"terrain_dimensions", "biome_palettes", "dimension_materials", "templates")) {
			assertTrue(provider.getAsJsonObject(section).entrySet().isEmpty(), section + " must remain empty");
		}
	}

	private static void assertRule(final JsonObject rule, final String id,
			final Rule expected, final boolean enabled) {
		assertEquals(enabled, rule.get("enabled").getAsBoolean(), id);
		assertEquals("basemetals:" + expected.ore, rule.get("block").getAsString(), id);
		assertFalse(rule.get("native_generation").getAsBoolean(), id);
		assertFalse(rule.get("retrogen").getAsBoolean(), id);

		final JsonArray outputs = rule.getAsJsonArray("outputs");
		assertEquals(1, outputs.size(), id);
		assertEquals("basemetals:" + expected.ore,
				outputs.get(0).getAsJsonObject().get("block").getAsString(), id);
		assertEquals(100, outputs.get(0).getAsJsonObject().get("weight").getAsInt(), id);
		assertEquals(-2048, outputs.get(0).getAsJsonObject().get("min_y").getAsInt(), id);
		assertEquals(2048, outputs.get(0).getAsJsonObject().get("max_y").getAsInt(), id);

		final String containerName = expected.dimension.startsWith("minecraft:")
				? "dimensions" : "dimension_selectors";
		final JsonObject settings = rule.getAsJsonObject(containerName)
				.getAsJsonObject(expected.dimension);
		assertTrue(settings.get("enabled").getAsBoolean(), id);
		assertEquals(expected.minY, settings.get("min_y").getAsInt(), id);
		assertEquals(expected.maxY, settings.get("max_y").getAsInt(), id);
		assertEquals(expected.frequency, settings.get("frequency").getAsDouble(), 0.0D, id);
		assertEquals(4, settings.get("min_quantity").getAsInt(), id);
		assertEquals(11, settings.get("max_quantity").getAsInt(), id);
		assertEquals("default", settings.get("pattern").getAsString(), id);
		assertEquals("orespawn:standard", settings.get("placement_channel").getAsString(), id);
		assertEquals("uniform", settings.get("height_distribution").getAsString(), id);
		assertEquals(0.0D, settings.get("discard_chance_on_air_exposure").getAsDouble(), 0.0D, id);
		assertEquals(8, settings.get("spread").getAsInt(), id);
		assertEquals(4, settings.get("vertical_spread").getAsInt(), id);
		assertEquals(8, settings.get("node_size").getAsInt(), id);
		assertEquals(Arrays.asList("minecraft:stone", "minecraft:netherrack", "minecraft:end_stone"),
				weightedIds(settings.getAsJsonArray("host_blocks"), "block"), id);
		for (final String empty : Arrays.asList("host_tags", "host_families", "biome_ids",
				"excluded_biome_ids", "biome_dictionary", "excluded_biome_dictionary")) {
			assertTrue(settings.getAsJsonArray(empty).size() == 0, id + " " + empty);
		}
		assertTrue(settings.getAsJsonObject("geomes").entrySet().isEmpty(), id);
	}

	private static JsonObject provider(final ContentMode mode) {
		return BaseMetalsWorldgenProvider.build(ContentPolicy.forMode(mode)).toJson();
	}

	private static Map<String, Rule> expectedRules() {
		final Map<String, Rule> result = new LinkedHashMap<>();
		result.put("basemetals:legacy/coldiron_ore",
				new Rule("coldiron_ore", "minecraft:the_nether", 0, 127, 5.0D));
		result.put("basemetals:legacy/adamantine_ore",
				new Rule("adamantine_ore", "minecraft:the_nether", 0, 127, 2.0D));
		result.put("basemetals:legacy/starsteel_ore",
				new Rule("starsteel_ore", "minecraft:the_end", 0, 254, 5.0D));
		result.put("basemetals:legacy/copper_ore",
				new Rule("copper_ore", "orespawn:all_except_nether_end", 0, 95, 10.0D));
		result.put("basemetals:legacy/silver_ore",
				new Rule("silver_ore", "orespawn:all_except_nether_end", 0, 31, 4.0D));
		result.put("basemetals:legacy/tin_ore",
				new Rule("tin_ore", "orespawn:all_except_nether_end", 0, 127, 10.0D));
		result.put("basemetals:legacy/lead_ore",
				new Rule("lead_ore", "orespawn:all_except_nether_end", 0, 63, 5.0D));
		result.put("basemetals:legacy/zinc_ore",
				new Rule("zinc_ore", "orespawn:all_except_nether_end", 0, 95, 5.0D));
		result.put("basemetals:legacy/mercury_ore",
				new Rule("mercury_ore", "orespawn:all_except_nether_end", 0, 31, 3.0D));
		result.put("basemetals:legacy/nickel_ore",
				new Rule("nickel_ore", "orespawn:all_except_nether_end", 32, 95, 1.0D));
		result.put("basemetals:legacy/platinum_ore",
				new Rule("platinum_ore", "orespawn:all_except_nether_end", 1, 31, 0.125D));
		return Collections.unmodifiableMap(result);
	}

	private static java.util.List<String> strings(final JsonArray values) {
		final java.util.List<String> result = new java.util.ArrayList<>();
		for (final JsonElement value : values) {
			result.add(value.getAsString());
		}
		return result;
	}

	private static java.util.List<String> weightedIds(final JsonArray values, final String key) {
		final java.util.List<String> result = new java.util.ArrayList<>();
		for (final JsonElement value : values) {
			result.add(value.isJsonPrimitive() ? value.getAsString()
					: value.getAsJsonObject().get(key).getAsString());
		}
		return result;
	}

	private static java.util.Set<String> keys(final JsonObject object) {
		final java.util.Set<String> result = new java.util.LinkedHashSet<>();
		for (final Map.Entry<String, JsonElement> entry : object.entrySet()) {
			result.add(entry.getKey());
		}
		return result;
	}

	private static JsonObject read(final Path path) throws IOException {
		assertTrue(Files.isRegularFile(path), "Missing JSON contract " + path);
		try (Reader reader = Files.newBufferedReader(path)) {
			return new JsonParser().parse(reader).getAsJsonObject();
		}
	}

	private static final class Rule {
		private final String ore;
		private final String dimension;
		private final int minY;
		private final int maxY;
		private final double frequency;

		private Rule(final String ore, final String dimension,
				final int minY, final int maxY, final double frequency) {
			this.ore = ore;
			this.dimension = dimension;
			this.minY = minY;
			this.maxY = maxY;
			this.frequency = frequency;
		}
	}
}
