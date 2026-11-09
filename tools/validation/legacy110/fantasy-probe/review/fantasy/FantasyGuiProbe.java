package review.fantasy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import zone.moddev.mc.basemetals.content.ContentMode;
import zone.moddev.mc.basemetals.util.Config;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry;

@Mod(modid = "basemetalsfantasyguiprobe", name = "Base Metals configuration checks", version = "1",
		clientSideOnly = true, dependencies = "required-after:basemetals")
public final class FantasyGuiProbe {
	private boolean checked;

	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
		if (Boolean.getBoolean("fantasy.guiProbe") || Boolean.getBoolean("fantasy.mismatchProbe")) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	@SubscribeEvent
	public void tick(final TickEvent.ClientTickEvent event) throws Exception {
		final Minecraft client = Minecraft.getMinecraft();
		if (checked || event.phase != TickEvent.Phase.END) {
			return;
		}
		if (Boolean.getBoolean("fantasy.mismatchProbe")) {
			if (client.currentScreen instanceof GuiDisconnected) {
				checkMismatch(client);
			}
			return;
		}
		if (!(client.currentScreen instanceof GuiMainMenu)) {
			return;
		}
		checked = true;
		final GuiScreen parent = client.currentScreen;
		final GuiConfig forge = (GuiConfig) FMLClientHandler.instance()
				.getGuiFactoryFor(Loader.instance().getIndexedModList().get("basemetals"))
				.createConfigGui(parent);
		final GuiConfig oreSpawn = (GuiConfig) WorldSettingsExtensionRegistry.extensions().stream()
				.filter(extension -> extension.id().equals(new ResourceLocation("basemetals", "configuration")))
				.findFirst().get().createScreen(parent);
		require(forge.configElements.size() == 7 && oreSpawn.configElements.size() == 7, "Expected seven categories");
		require(forge.allRequireMcRestart && oreSpawn.allRequireMcRestart, "Expected restart guidance");

		final GuiConfig cancelled = openGeneral(client, oreSpawn);
		selector(cancelled).valueButtonPressed(0);
		escape(cancelled);
		escape(oreSpawn);
		require(client.currentScreen == parent, "Escape did not return to the parent");
		require("high_fantasy".equals(savedMode()), "Escape saved a pending change");

		final GuiConfig general = openGeneral(client, forge);
		final GuiConfigEntries.CycleValueEntry mode = selector(general);
		require(Arrays.equals(new String[] { "high_fantasy", "low_fantasy" },
				mode.getConfigElement().getValidValues()), "Expected two choices");
		require(!I18n.format(mode.getConfigElement().getLanguageKey())
				.equals(mode.getConfigElement().getLanguageKey()), "Missing Fantasy Level label");
		mode.valueButtonPressed(0);
		require("low_fantasy".equals(mode.getCurrentValue()), "Low Fantasy was not selected");
		mode.undoChanges();
		require("high_fantasy".equals(mode.getCurrentValue()), "Undo did not restore High Fantasy");
		mode.valueButtonPressed(0);
		mode.setToDefault();
		require("high_fantasy".equals(mode.getCurrentValue()), "Defaults did not restore High Fantasy");
		mode.valueButtonPressed(0);

		done(general);
		require("high_fantasy".equals(savedMode()), "A category saved before the root screen");
		require(client.currentScreen == forge, "Done did not return to the root screen");
		done(forge);
		require("low_fantasy".equals(savedMode()), "Done did not save Low Fantasy");
		require(Config.getActiveContentMode() == ContentMode.HIGH_FANTASY, "Fantasy Level changed before restart");
		Files.write(Paths.get("fantasy-gui-pass.txt"), "Both config routes passed.\n".getBytes(StandardCharsets.UTF_8));
		client.shutdown();
	}

	private void checkMismatch(final Minecraft client) throws Exception {
		checked = true;
		for (final Field field : GuiDisconnected.class.getDeclaredFields()) {
			if (field.getType() == ITextComponent.class) {
				field.setAccessible(true);
				final String message = ((ITextComponent) field.get(client.currentScreen)).getUnformattedText();
				require(message.contains("The server uses Low Fantasy, but your game uses High Fantasy."), message);
				Files.write(Paths.get("fantasy-mismatch-pass.txt"), message.getBytes(StandardCharsets.UTF_8));
				client.shutdown();
				return;
			}
		}
		throw new AssertionError("The disconnect screen had no reason");
	}

	private static GuiConfig openGeneral(final Minecraft client, final GuiConfig root) throws Exception {
		client.displayGuiScreen(root);
		final Field child = GuiConfigEntries.CategoryEntry.class.getDeclaredField("childScreen");
		child.setAccessible(true);
		final GuiConfig general = (GuiConfig) child.get(root.entryList.listEntries.get(0));
		client.displayGuiScreen(general);
		return general;
	}

	private static GuiConfigEntries.CycleValueEntry selector(final GuiConfig general) {
		return (GuiConfigEntries.CycleValueEntry) general.entryList.listEntries.stream()
				.filter(entry -> "contentMode".equals(entry.getConfigElement().getName())).findFirst().get();
	}

	private static String savedMode() {
		return Config.getConfiguration().getCategory("General").get("contentMode").getString();
	}

	private static void escape(final GuiConfig screen) throws Exception {
		callback("keyTyped", "func_73869_a", char.class, int.class).invoke(screen, '\u001b', 1);
	}

	private static void done(final GuiConfig screen) throws Exception {
		callback("actionPerformed", "func_146284_a", GuiButton.class)
				.invoke(screen, new GuiButton(2000, 0, 0, "Done"));
	}

	private static Method callback(final String mapped, final String runtime, final Class<?>... parameters)
			throws Exception {
		Method method;
		try {
			method = GuiConfig.class.getDeclaredMethod(mapped, parameters);
		} catch (NoSuchMethodException missing) {
			method = GuiConfig.class.getDeclaredMethod(runtime, parameters);
		}
		method.setAccessible(true);
		return method;
	}

	private static void require(final boolean condition, final String reason) {
		if (!condition) {
			throw new AssertionError(reason);
		}
	}
}
