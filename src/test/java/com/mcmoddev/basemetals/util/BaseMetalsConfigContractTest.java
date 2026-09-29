package com.mcmoddev.basemetals.util;

import com.mcmoddev.basemetals.content.ContentMode;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.text.WordUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseMetalsConfigContractTest {
	private static final String[] METALS = {
			"adamantine", "antimony", "aquarium", "bismuth", "brass", "bronze",
			"coldiron", "copper", "cupronickel", "electrum", "invar", "lead",
			"mercury", "mithril", "nickel", "pewter", "platinum", "silver",
			"starsteel", "steel", "tin", "zinc"
	};
	private static final String[] VANILLA = {
			"charcoal", "coal", "diamond", "emerald", "gold", "iron", "stone",
			"wood", "ender", "quartz", "obsidian", "lapis", "prismarine", "redstone"
	};
	private static final Set<String> VANILLA_FLUIDS = new LinkedHashSet<>(Arrays.asList(
			"charcoal", "coal", "diamond", "emerald", "gold", "iron", "ender",
			"quartz", "obsidian", "prismarine", "redstone"));

	@TempDir
	Path tempDirectory;

	@Test
	void exposesTheFiveHistoricalCategoriesAndNinetyThreeSettings() {
		final Configuration configuration = create();
		final Set<String> expectedCategories = BMeConfig.GUI_CATEGORIES.stream()
				.map(name -> name.toLowerCase(java.util.Locale.ROOT))
				.collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
		assertEquals(expectedCategories, new LinkedHashSet<>(configuration.getCategoryNames()));
		assertEquals(2, configuration.getCategory(BMeConfig.GENERAL_CAT).size());
		assertEquals(22, configuration.getCategory(BMeConfig.MATERIALS_CAT).size());
		assertEquals(14, configuration.getCategory(BMeConfig.VANILLA_CAT).size());
		assertEquals(33, configuration.getCategory(BMeConfig.FLUIDS_CAT).size());
		assertEquals(22, configuration.getCategory(BMeConfig.FORCED_TRAITS_CAT).size());
		assertEquals(93, configuration.getCategoryNames().stream()
				.mapToInt(name -> configuration.getCategory(name).size()).sum());
	}

	@Test
	void preservesEveryKeyDefaultCommentAndMarksEveryOptionForRestart() {
		final Configuration configuration = create();
		assertProperty(configuration, BMeConfig.GENERAL_CAT, "achievements", true,
				"If false, then Base Metals Achievements will be disabled (This is currently required if you disable any metals",
				"config.basemetals.option.achievements");
		final Property contentMode = configuration.getCategory(BMeConfig.GENERAL_CAT)
				.get(BMeConfig.CONTENT_MODE_PROPERTY);
		assertEquals(Property.Type.STRING, contentMode.getType());
		assertEquals(ContentMode.HIGH_FANTASY.serializedName(), contentMode.getString());
		assertEquals(Arrays.asList(ContentMode.serializedNames()),
				Arrays.asList(contentMode.getValidValues()));
		assertEquals(Arrays.asList(ContentMode.translationKeys()),
				Arrays.asList(contentMode.getValidValuesDisplay()));
		assertTrue(contentMode.requiresMcRestart());

		for (final String material : METALS) {
			final String display = WordUtils.capitalizeFully(material);
			assertProperty(configuration, BMeConfig.MATERIALS_CAT, "Enable" + display, true,
					"Enable " + display + " Items and Materials", materialKey(material));
			assertProperty(configuration, BMeConfig.FLUIDS_CAT, "Enabled " + display, true,
					"Enable the molten fluid of " + material, materialKey(material));
			assertProperty(configuration, BMeConfig.FORCED_TRAITS_CAT,
					"Force" + display + "TraitRegistration", false,
					"Enable the forced registration of traits for " + material,
					materialKey(material));
		}

		for (final String material : VANILLA) {
			final String display = WordUtils.capitalizeFully(material);
			assertProperty(configuration, BMeConfig.VANILLA_CAT, "Enable" + display, true,
					"Enable " + display + " Additions like Walls, Slabs and Pressure-plates",
					materialKey(material));
			if (VANILLA_FLUIDS.contains(material)) {
				assertProperty(configuration, BMeConfig.FLUIDS_CAT, "Enabled " + display, true,
						"Enable the molten fluid of " + material, materialKey(material));
			}
		}

		for (final String categoryName : BMeConfig.GUI_CATEGORIES) {
			final ConfigCategory category = configuration.getCategory(categoryName);
			assertTrue(category.requiresMcRestart(), categoryName);
			assertTrue(category.requiresWorldRestart(), categoryName);
			assertNotNull(category.getLanguagekey(), categoryName);
			for (final Property property : category.getValues().values()) {
				if (!BMeConfig.CONTENT_MODE_PROPERTY.equals(property.getName())) {
					assertEquals(Property.Type.BOOLEAN, property.getType(), property.getName());
				}
			}
		}
	}

	@Test
	void missingLegacyModeAndExplicitHighFantasyHaveTheSameEffectiveValue() {
		final Configuration legacy = new Configuration(
				tempDirectory.resolve("LegacyBaseMetals.cfg").toFile());
		legacy.get(BMeConfig.GENERAL_CAT, "achievements", false);
		assertEquals(ContentMode.HIGH_FANTASY, BMeConfig.configuredContentMode(legacy));
		assertFalse(legacy.getCategory(BMeConfig.GENERAL_CAT).containsKey(
				BMeConfig.CONTENT_MODE_PROPERTY));

		BMeConfig.populateConfiguration(legacy);
		assertEquals(ContentMode.HIGH_FANTASY, BMeConfig.configuredContentMode(legacy));
		assertFalse(legacy.getCategory(BMeConfig.GENERAL_CAT).get("achievements").getBoolean());
	}

	@Test
	void missingPropertyDefaultsToHighFantasyAndInvalidValuesAreCorrectedOnPopulate() {
		final Configuration absent = create();
		absent.getCategory(BMeConfig.GENERAL_CAT).remove(BMeConfig.CONTENT_MODE_PROPERTY);
		assertEquals(ContentMode.HIGH_FANTASY, BMeConfig.configuredContentMode(absent));

		final Configuration invalid = create();
		invalid.getCategory(BMeConfig.GENERAL_CAT).get(BMeConfig.CONTENT_MODE_PROPERTY)
				.set("not_a_mode");
		BMeConfig.populateConfiguration(invalid);
		assertEquals(ContentMode.HIGH_FANTASY, BMeConfig.configuredContentMode(invalid));
		assertEquals(ContentMode.HIGH_FANTASY.serializedName(),
				invalid.getCategory(BMeConfig.GENERAL_CAT)
						.get(BMeConfig.CONTENT_MODE_PROPERTY).getString());
	}

	@Test
	void legacyUpgradePreservesEveryExistingBooleanValue() {
		final Configuration legacy = create();
		final Map<String, Boolean> expected = new LinkedHashMap<>();
		int index = 0;
		for (final String categoryName : BMeConfig.GUI_CATEGORIES) {
			for (final Property property : legacy.getCategory(categoryName).getValues().values()) {
				if (property.getType() == Property.Type.BOOLEAN) {
					final boolean value = (index++ & 1) == 0;
					property.set(value);
					expected.put(categoryName + "\u0000" + property.getName(), value);
				}
			}
		}
		legacy.getCategory(BMeConfig.GENERAL_CAT).remove(BMeConfig.CONTENT_MODE_PROPERTY);
		legacy.save();

		final Configuration upgraded = create();
		assertEquals(ContentMode.HIGH_FANTASY, BMeConfig.configuredContentMode(upgraded));
		for (final Map.Entry<String, Boolean> entry : expected.entrySet()) {
			final String[] key = entry.getKey().split("\u0000", 2);
			assertEquals(entry.getValue().booleanValue(),
					upgraded.getCategory(key[0]).get(key[1]).getBoolean(), entry.getKey());
		}
	}

	@Test
	void generatedMaterialMapMatchesTheElevenStableProviderRules() {
		assertEquals(11, BMeConfig.getGeneratedOreRules().size());
		assertEquals("basemetals:legacy/coldiron_ore",
				BMeConfig.getGeneratedOreRules().get("coldiron"));
		assertEquals("basemetals:legacy/platinum_ore",
				BMeConfig.getGeneratedOreRules().get("platinum"));
		assertFalse(BMeConfig.getGeneratedOreRules().containsKey("antimony"));
		assertFalse(BMeConfig.getGeneratedOreRules().containsKey("bismuth"));
	}

	private Configuration create() {
		return TestConfigFactory.create(tempDirectory.resolve("BaseMetals.cfg").toFile());
	}

	private static void assertProperty(final Configuration configuration, final String category,
			final String name, final boolean expectedDefault, final String expectedComment,
			final String expectedLanguageKey) {
		final Property property = configuration.getCategory(category).get(name);
		assertNotNull(property, category + "/" + name);
		assertEquals(expectedDefault, Boolean.parseBoolean(property.getDefault()), name);
		assertEquals(expectedComment + " [default: " + expectedDefault + "]",
				property.getComment(), name);
		assertEquals(expectedLanguageKey, property.getLanguageKey(), name);
		assertTrue(property.requiresMcRestart(), name);
		assertTrue(property.requiresWorldRestart(), name);
	}

	private static String materialKey(final String material) {
		return "config.basemetals.material." + material;
	}
}
