package com.mcmoddev.basemetals.client.config;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigLocalizationContractTest {
	private static final Set<String> STANDARD_ENGLISH_LOCALES = new HashSet<>(Arrays.asList(
			"en_au.lang", "en_ca.lang", "en_en.lang", "en_gb.lang", "en_us.lang"));
	private static final List<String> MATERIALS = Arrays.asList(
			"adamantine", "antimony", "aquarium", "bismuth", "brass", "bronze",
			"coldiron", "copper", "cupronickel", "electrum", "invar", "lead",
			"mercury", "mithril", "nickel", "pewter", "platinum", "silver",
			"starsteel", "steel", "tin", "zinc", "charcoal", "coal", "diamond",
			"emerald", "gold", "iron", "stone", "wood", "ender", "quartz",
			"obsidian", "lapis", "prismarine", "redstone");

	@Test
	void allEighteenLocalesDefineTheCompleteConfigurationVocabulary() throws Exception {
		final Path langDirectory = Paths.get("src", "main", "resources", "assets",
				"basemetals", "lang");
		final List<Path> locales;
		try (java.util.stream.Stream<Path> files = Files.list(langDirectory)) {
			locales = files.filter(path -> path.getFileName().toString().endsWith(".lang"))
					.sorted().collect(java.util.stream.Collectors.toList());
		}
		assertEquals(18, locales.size());
		final Map<String, String> english = configurationValues(
				langDirectory.resolve("en_us.lang"));
		assertEquals(64, english.size());

		for (final Path locale : locales) {
			final String text = new String(Files.readAllBytes(locale), StandardCharsets.UTF_8);
			final Map<String, String> values = configurationValues(locale);
			assertEquals(english.keySet(), values.keySet(), locale.toString());
			for (final String key : Arrays.asList(
					"config.basemetals.title=", "config.basemetals.restart_guidance=",
					"config.basemetals.category.general=",
					"config.basemetals.category.metals=",
					"config.basemetals.category.vanilla=",
					"config.basemetals.category.fluids=",
					"config.basemetals.category.forced_traits=",
					"config.basemetals.option.achievements=",
					"config.basemetals.option.content_mode=",
					"config.basemetals.option.content_mode.tooltip=",
					"config.basemetals.content_mode.high_fantasy=",
					"config.basemetals.content_mode.high_fantasy.description=",
					"config.basemetals.content_mode.low_fantasy=",
					"config.basemetals.content_mode.low_fantasy.description=",
					"config.basemetals.content_mode.realism=",
					"config.basemetals.content_mode.realism.description=",
					"config.basemetals.warning.content_mode.title=",
					"config.basemetals.warning.content_mode.message=",
					"config.basemetals.warning.content_mode.realism=",
					"config.basemetals.warning.confirm=",
					"config.basemetals.warning.disable_ores.title=",
					"config.basemetals.warning.disable_ores.message=",
					"config.basemetals.warning.disable_ores.confirm=")) {
				assertEquals(1, occurrences(text, key), locale + " " + key);
			}
			for (final String material : MATERIALS) {
				final String key = "config.basemetals.material." + material + "=";
				assertEquals(1, occurrences(text, key), locale + " " + key);
			}
			assertTrue(text.contains("config.basemetals.category.metals.tooltip="),
					locale.toString());
			if (!STANDARD_ENGLISH_LOCALES.contains(locale.getFileName().toString())) {
				final long translated = english.entrySet().stream()
						.filter(entry -> !entry.getValue().equals(values.get(entry.getKey())))
						.count();
				assertTrue(translated >= 20,
						locale + " still contains an English fallback configuration block");
			}
		}
	}

	private static Map<String, String> configurationValues(final Path locale)
			throws Exception {
		final Map<String, String> values = new LinkedHashMap<>();
		for (final String line : Files.readAllLines(locale, StandardCharsets.UTF_8)) {
			if (!line.startsWith("config.basemetals.")) {
				continue;
			}
			final int separator = line.indexOf('=');
			assertTrue(separator > 0, locale + " malformed configuration translation: " + line);
			values.put(line.substring(0, separator), line.substring(separator + 1));
		}
		return values;
	}

	private static int occurrences(final String text, final String value) {
		int count = 0;
		for (int index = 0; (index = text.indexOf(value, index)) >= 0;
				index += value.length()) {
			count++;
		}
		return count;
	}
}
