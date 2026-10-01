package com.mcmoddev.basemetals.client.config;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.content.ContentMode;
import com.mcmoddev.basemetals.util.BMeConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Edits BaseMetals.cfg through Forge's configuration screen. */
public final class BaseMetalsConfigScreen extends GuiConfig implements GuiYesNoCallback {

	private static final int CONFIRM_CHANGES = 4701;

	private final Configuration configuration;
	private final Map<String, String> initialValues;
	private GuiButton pendingDoneButton;

	public BaseMetalsConfigScreen(final GuiScreen parent) {
		this(parent, BMeConfig.getConfiguration());
	}

	BaseMetalsConfigScreen(final GuiScreen parent, final Configuration configuration) {
		super(parent, categoryElements(configuration), BaseMetals.MODID, BaseMetals.MODID,
				false, true, I18n.format("config.basemetals.title"),
				I18n.format("config.basemetals.restart_guidance"));
		this.configuration = configuration;
		this.initialValues = snapshot(configuration);
	}

	static List<IConfigElement> categoryElements(final Configuration configuration) {
		final List<IConfigElement> elements = new ArrayList<>();

		for (final String categoryName : BMeConfig.GUI_CATEGORIES) {
			elements.add(new CategoryElement(configuration.getCategory(categoryName)));
		}

		return elements;
	}

	static Map<String, String> snapshot(final Configuration configuration) {
		final Map<String, String> values = new LinkedHashMap<>();

		for (final String categoryName : BMeConfig.GUI_CATEGORIES) {
			final ConfigCategory category = configuration.getCategory(categoryName);

			for (final Map.Entry<String, Property> property : category.getValues().entrySet()) {
				values.put(key(categoryName, property.getKey()), property.getValue().getString());
			}
		}

		return values;
	}

	static List<String> disabledGeneratedRules(final Map<String, String> before,
			final Configuration configuration) {
		final List<String> disabled = new ArrayList<>();

		for (final Map.Entry<String, String> generated : BMeConfig.getGeneratedOreRules().entrySet()) {
			final String propertyName = BMeConfig.getMaterialPropertyName(generated.getKey());
			final String snapshotKey = key(BMeConfig.MATERIALS_CAT, propertyName);
			final Property property = configuration.getCategory(BMeConfig.MATERIALS_CAT)
					.get(propertyName);

			if (Boolean.parseBoolean(before.get(snapshotKey)) && !property.getBoolean()) {
				disabled.add(generated.getValue());
			}
		}

		return disabled;
	}

	@Override
	protected void actionPerformed(final GuiButton button) {
		if (button.id == 2000 && entryList.hasChangedEntry(true)) {
			// Read the pending category edits to check whether a warning is needed.
			// Restore the original values while the player decides whether to save.

			entryList.saveConfigElements();

			final ContentMode initialMode = ContentMode.fromSerializedName(initialValues.get(
					key(BMeConfig.GENERAL_CAT, BMeConfig.CONTENT_MODE_PROPERTY)));
			final ContentMode selectedMode = BMeConfig.configuredContentMode(configuration);
			final List<String> disabledRules = disabledGeneratedRules(initialValues, configuration);

			if (initialMode != selectedMode || !disabledRules.isEmpty()) {
				restoreInitialValues();
				pendingDoneButton = button;

				final String title;
				String message;

				if (initialMode != selectedMode) {
					title = I18n.format("config.basemetals.warning.content_mode.title");
					message = I18n.format("config.basemetals.warning.content_mode.message",
							I18n.format(initialMode.translationKey()),
							I18n.format(selectedMode.translationKey()));

					if (!disabledRules.isEmpty()) {
						message += "\n\n" + I18n.format(
								"config.basemetals.warning.disable_ores.message",
								String.join(", ", disabledRules));
					}
				} else {
					title = I18n.format("config.basemetals.warning.disable_ores.title");
					message = I18n.format("config.basemetals.warning.disable_ores.message",
							String.join(", ", disabledRules));
				}

				mc.displayGuiScreen(new GuiYesNo(this,
						title, message,
						I18n.format("config.basemetals.warning.confirm"),
						I18n.format("gui.cancel"), CONFIRM_CHANGES));

				return;
			}
		}

		super.actionPerformed(button);
	}

	@Override
	public void confirmClicked(final boolean result, final int id) {
		if (id != CONFIRM_CHANGES) {
			return;
		}

		mc.displayGuiScreen(this);

		if (result && pendingDoneButton != null) {
			super.actionPerformed(pendingDoneButton);
		}

		pendingDoneButton = null;
	}

	@Override
	protected void keyTyped(final char eventChar, final int eventKey) {
		if (eventKey == Keyboard.KEY_ESCAPE) {
			entryList.undoAllChanges(true);
			restoreInitialValues();
			mc.displayGuiScreen(parentScreen);

			return;
		}

		super.keyTyped(eventChar, eventKey);
	}

	void restoreInitialValues() {
		restore(initialValues, configuration);
	}

	static void restore(final Map<String, String> values,
			final Configuration configuration) {
		for (final String categoryName : BMeConfig.GUI_CATEGORIES) {
			final ConfigCategory category = configuration.getCategory(categoryName);

			for (final Map.Entry<String, Property> property : category.getValues().entrySet()) {
				final String value = values.get(key(categoryName, property.getKey()));

				if (value != null) {
					property.getValue().set(value);
				}
			}
		}
	}

	private static String key(final String category, final String property) {
		return category + '\u0000' + property;
	}

	private static final class CategoryElement extends ConfigElement {
		CategoryElement(final ConfigCategory category) {
			super(category);
		}

		@Override
		public Class<? extends GuiConfigEntries.IConfigEntry> getConfigEntryClass() {
			return CategoryEntry.class;
		}
	}

	/** Opens a category page that lets Escape cancel its edits. */
	public static final class CategoryEntry extends GuiConfigEntries.CategoryEntry {
		public CategoryEntry(final GuiConfig owningScreen,
				final GuiConfigEntries owningEntryList,
				final IConfigElement configElement) {
			super(owningScreen, owningEntryList, configElement);
		}

		@Override
		protected GuiScreen buildChildScreen() {
			return new CategoryScreen(owningScreen, configElement.getChildElements(),
					owningScreen.modID,
					owningScreen.allRequireWorldRestart || configElement.requiresWorldRestart(),
					owningScreen.allRequireMcRestart || configElement.requiresMcRestart(),
					owningScreen.title,
					(owningScreen.titleLine2 == null ? "" : owningScreen.titleLine2)
							+ " > " + name);
		}
	}

	/** Cancels unsaved edits when the player presses Escape. */
	public static final class CategoryScreen extends GuiConfig {
		CategoryScreen(final GuiScreen parent, final List<IConfigElement> elements,
				final String modId, final boolean requiresWorldRestart,
				final boolean requiresMcRestart, final String title,
				final String titleLine2) {
			super(parent, elements, modId, null, requiresWorldRestart,
					requiresMcRestart, title, titleLine2);
		}

		@Override
		protected void keyTyped(final char eventChar, final int eventKey) {
			if (eventKey == Keyboard.KEY_ESCAPE) {
				entryList.undoAllChanges(true);
				mc.displayGuiScreen(parentScreen);

				return;
			}

			super.keyTyped(eventChar, eventKey);
		}
	}
}
