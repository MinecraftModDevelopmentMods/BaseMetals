package com.mcmoddev.basemetals.test;

import com.mcmoddev.basemetals.client.config.BaseMetalsConfigScreen;
import com.mcmoddev.basemetals.client.config.BaseMetalsGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtension;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/** Checks both configuration-screen entry points in a running client. */
@Mod(
		modid = BaseMetalsClientConfigRuntimeProbe.MODID,
		name = "Base Metals Client Configuration Probe",
		version = "1",
		clientSideOnly = true,
		dependencies = "required-after:basemetals;required-after:orespawn@[4.1.6.112021,5.0.0)",
		acceptedMinecraftVersions = "[1.12.2]")
public final class BaseMetalsClientConfigRuntimeProbe {
	public static final String MODID = "basemetals_client_config_probe";
	private final Properties result = new Properties();
	private boolean checkedModsButton;
	private boolean connecting;
	private int connectedTicks;

	@EventHandler
	public void init(final FMLInitializationEvent event) throws IOException {
		if (Integer.getInteger("basemetals.connectionProbe.port", 0) != 0) {
			MinecraftForge.EVENT_BUS.register(this);
			return;
		}

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

		final GuiScreen parent = new GuiScreen() {
		};
		final GuiScreen created = extension.createScreen(parent);

		require(created instanceof BaseMetalsConfigScreen,
				"Extension did not create BaseMetalsConfigScreen");

		final GuiConfig screen = (GuiConfig) created;

		require(screen.parentScreen == parent, "OreSpawn parent screen was not preserved");
		require(screen.configElements.size() == 5, "Expected five root categories");

		final int properties = screen.configElements.stream()
				.mapToInt(element -> element.getChildElements().size()).sum();

		require(properties == 93, "Expected 93 Base Metals options, found " + properties);

		result.setProperty("extension", extension.id().toString());
		result.setProperty("button", extension.buttonTranslationKey());
		result.setProperty("screen", created.getClass().getName());
		result.setProperty("parent", Boolean.toString(screen.parentScreen == parent));
		result.setProperty("categories", Integer.toString(screen.configElements.size()));
		result.setProperty("properties", Integer.toString(properties));
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void checkModsButton(final TickEvent.ClientTickEvent event) throws IOException, IllegalAccessException {
		if (checkedModsButton || event.phase != TickEvent.Phase.END) {
			return;
		}

		final int port = Integer.getInteger("basemetals.connectionProbe.port", 0);
		if (port != 0) {
			checkConnection(port);
			return;
		}

		// Forge creates GUI factories after mod initialization, before the first client tick.
		final IModGuiFactory factory = FMLClientHandler.instance().getGuiFactoryFor(
				Loader.instance().getIndexedModList().get("basemetals"));

		require(factory instanceof BaseMetalsGuiFactory,
				"Forge did not load the Base Metals Mods-list GUI factory");
		require(factory.hasConfigGui(), "Forge's Config button is disabled for Base Metals");

		final GuiModList parent = new GuiModList(new GuiMainMenu());
		final GuiScreen created = factory.createConfigGui(parent);

		require(created instanceof BaseMetalsConfigScreen,
				"Mods-list Config button did not create BaseMetalsConfigScreen");

		final GuiConfig screen = (GuiConfig) created;

		require(screen.parentScreen == parent, "Mods-list parent screen was not preserved");
		require(screen.configElements.size() == 5, "Mods-list screen lost config categories");
		Minecraft.getMinecraft().displayGuiScreen(created);
		require(Minecraft.getMinecraft().currentScreen == screen,
				"Mods-list config screen did not initialize for display");
		result.setProperty("modsButton", Boolean.toString(factory.hasConfigGui()));
		result.setProperty("modsParent", Boolean.toString(screen.parentScreen == parent));
		result.setProperty("modsScreen", created.getClass().getName());

		final String markerPath = System.getProperty("basemetals.clientConfigProbe.marker");

		if (markerPath != null && !markerPath.trim().isEmpty()) {
			final File marker = new File(markerPath);
			final File parentDirectory = marker.getParentFile();

			if (parentDirectory != null) {
				parentDirectory.mkdirs();
			}

			try (FileOutputStream output = new FileOutputStream(marker)) {
				result.store(output, "Base Metals client configuration probe");
			}
		}

		checkedModsButton = true;

		if (Boolean.getBoolean("basemetals.clientConfigProbe.exit")) {
			Minecraft.getMinecraft().shutdown();
		}
	}

	private void checkConnection(final int port) throws IOException, IllegalAccessException {
		final Minecraft client = Minecraft.getMinecraft();
		if (!connecting && client.currentScreen instanceof GuiMainMenu) {
			connecting = true;
			client.displayGuiScreen(new GuiConnecting(client.currentScreen, client, "127.0.0.1", port));
			return;
		}

		if (connecting && client.currentScreen instanceof GuiDisconnected) {
			for (final Field field : GuiDisconnected.class.getDeclaredFields()) {
				if (field.getType() == ITextComponent.class) {
					field.setAccessible(true);
					final ITextComponent message = (ITextComponent) field.get(client.currentScreen);
					finishConnectionCheck(client, message.getUnformattedText());
					return;
				}
			}

			throw new AssertionError("The disconnect screen has no message");
		}

		if (!Boolean.getBoolean("basemetals.connectionProbe.expectDisconnect")
				&& client.world != null && client.player != null && ++connectedTicks >= 10) {
			finishConnectionCheck(client, "joined");
		}
	}

	private void finishConnectionCheck(final Minecraft client, final String message) throws IOException {
		final File marker = new File(System.getProperty("basemetals.connectionProbe.marker"));
		Files.write(marker.toPath(), message.getBytes(StandardCharsets.UTF_8));
		checkedModsButton = true;
		client.shutdown();
	}

	private static void require(final boolean condition, final String message) {
		if (!condition) {
			throw new IllegalStateException(message);
		}
	}
}
