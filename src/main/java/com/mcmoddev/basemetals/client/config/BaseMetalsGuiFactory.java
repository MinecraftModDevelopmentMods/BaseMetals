package com.mcmoddev.basemetals.client.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Collections;
import java.util.Set;

/** Opens the existing configuration screen from Forge's Mods list. */
public final class BaseMetalsGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(final Minecraft minecraftInstance) {
		// The screen uses the configuration already loaded by Base Metals.
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(final GuiScreen parentScreen) {
		return new BaseMetalsConfigScreen(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return Collections.emptySet();
	}
}
