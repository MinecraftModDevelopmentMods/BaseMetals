package com.mcmoddev.basemetals.test;

import com.mcmoddev.basemetals.client.config.BaseMetalsConfigScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtension;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/** Test-only client probe. This class is packaged separately and never enters the release jar. */
@Mod(
		modid = BaseMetalsClientConfigRuntimeProbe.MODID,
		name = "Base Metals Client Configuration Probe",
		version = "1",
		clientSideOnly = true,
		dependencies = "required-after:basemetals;required-after:orespawn@[4.1.0.112021,5.0.0)",
		acceptedMinecraftVersions = "[1.12.2]")
public final class BaseMetalsClientConfigRuntimeProbe {
	public static final String MODID = "basemetals_client_config_probe";

	@EventHandler
	public void init(final FMLInitializationEvent event) throws IOException {
		final List<WorldSettingsExtension> extensions = WorldSettingsExtensionRegistry.extensions()
				.stream()
				.filter(extension -> "basemetals".equals(extension.id().getNamespace()))
				.collect(Collectors.toList());
		require(extensions.size() == 1, "Expected exactly one Base Metals client extension");
		final WorldSettingsExtension extension = extensions.get(0);
		require("basemetals:configuration".equals(extension.id().toString()),
				"Unexpected extension id " + extension.id());
		require("button.orespawn.mod.configure".equals(extension.buttonTranslationKey()),
				"Base Metals row does not use OreSpawn's Configure/cog action");

		final GuiScreen parent = new GuiScreen() { };
		final GuiScreen created = extension.createScreen(parent);
		require(created instanceof BaseMetalsConfigScreen,
				"Extension did not create BaseMetalsConfigScreen");
		final GuiConfig screen = (GuiConfig) created;
		require(screen.parentScreen == parent, "OreSpawn parent screen was not preserved");
		require(screen.configElements.size() == 5, "Expected five root categories");
		final int properties = screen.configElements.stream()
				.mapToInt(element -> element.getChildElements().size()).sum();
		require(properties == 93, "Expected 93 Base Metals options, found " + properties);

		final String markerPath = System.getProperty("basemetals.clientConfigProbe.marker");
		if (markerPath != null && !markerPath.trim().isEmpty()) {
			final Properties result = new Properties();
			result.setProperty("extension", extension.id().toString());
			result.setProperty("button", extension.buttonTranslationKey());
			result.setProperty("screen", created.getClass().getName());
			result.setProperty("parent", Boolean.toString(screen.parentScreen == parent));
			result.setProperty("categories", Integer.toString(screen.configElements.size()));
			result.setProperty("properties", Integer.toString(properties));
			final File marker = new File(markerPath);
			final File parentDirectory = marker.getParentFile();
			if (parentDirectory != null) {
				parentDirectory.mkdirs();
			}
			try (FileOutputStream output = new FileOutputStream(marker)) {
				result.store(output, "Base Metals OreSpawn client configuration probe");
			}
		}
	}

	private static void require(final boolean condition, final String message) {
		if (!condition) {
			throw new IllegalStateException(message);
		}
	}
}
