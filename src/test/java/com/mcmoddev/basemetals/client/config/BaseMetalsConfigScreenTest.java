package com.mcmoddev.basemetals.client.config;

import com.mcmoddev.basemetals.util.BMeConfig;
import com.mcmoddev.basemetals.util.TestConfigFactory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseMetalsConfigScreenTest {
	@TempDir
	Path tempDirectory;

	@Test
	void rootContainsOnlyTheFiveHistoricalCategoriesInStableOrder() {
		final Configuration configuration = create();
		final List<IConfigElement> elements = BaseMetalsConfigScreen.categoryElements(configuration);

		assertEquals(BMeConfig.GUI_CATEGORIES.size(), elements.size());

		for (int index = 0; index < elements.size(); index++) {
			assertEquals(BMeConfig.GUI_CATEGORIES.get(index).toLowerCase(java.util.Locale.ROOT),
					elements.get(index).getName());
			assertFalse(elements.get(index).isProperty());
			assertTrue(elements.get(index).requiresMcRestart());
			assertEquals(BaseMetalsConfigScreen.CategoryEntry.class,
					elements.get(index).getConfigEntryClass());
		}
	}

	@Test
	void warningReportsOnlyNewlyDisabledGeneratedMaterials() {
		final Configuration configuration = create();
		final Map<String, String> before = BaseMetalsConfigScreen.snapshot(configuration);

		configuration.getCategory(BMeConfig.MATERIALS_CAT).get("EnableColdiron").set(false);
		configuration.getCategory(BMeConfig.MATERIALS_CAT).get("EnableTin").set(false);
		configuration.getCategory(BMeConfig.MATERIALS_CAT).get("EnableAntimony").set(false);

		assertEquals(java.util.Arrays.asList(
				"basemetals:legacy/coldiron_ore", "basemetals:legacy/tin_ore"),
				BaseMetalsConfigScreen.disabledGeneratedRules(before, configuration));

		BaseMetalsConfigScreen.restore(before, configuration);
		assertTrue(configuration.getCategory(BMeConfig.MATERIALS_CAT)
				.get("EnableColdiron").getBoolean());
		assertTrue(BaseMetalsConfigScreen.disabledGeneratedRules(before, configuration).isEmpty());
	}

	@Test
	void disposableConfigurationPersistsDoneValuesWhileRestoreModelsEscape() {
		final Configuration configuration = create();
		final Map<String, String> before = BaseMetalsConfigScreen.snapshot(configuration);

		configuration.getCategory(BMeConfig.VANILLA_CAT).get("EnableGold").set(false);
		configuration.getCategory(BMeConfig.GENERAL_CAT)
				.get(BMeConfig.CONTENT_MODE_PROPERTY).set("low_fantasy");
		configuration.save();

		final Configuration reloaded = create();

		assertFalse(reloaded.getCategory(BMeConfig.VANILLA_CAT).get("EnableGold").getBoolean());
		assertEquals("low_fantasy", reloaded.getCategory(BMeConfig.GENERAL_CAT)
				.get(BMeConfig.CONTENT_MODE_PROPERTY).getString());

		BaseMetalsConfigScreen.restore(before, configuration);
		assertTrue(configuration.getCategory(BMeConfig.VANILLA_CAT).get("EnableGold").getBoolean());
		assertEquals("high_fantasy", configuration.getCategory(BMeConfig.GENERAL_CAT)
				.get(BMeConfig.CONTENT_MODE_PROPERTY).getString());
	}

	private Configuration create() {
		return TestConfigFactory.create(tempDirectory.resolve("BaseMetals.cfg").toFile());
	}
}
