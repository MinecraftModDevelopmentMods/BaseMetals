package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.util.Config;
import com.mcmoddev.lib.util.Config.Options;
import com.mcmoddev.lib.util.Oredicts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectEventProxy;
import thaumcraft.api.aspects.AspectList;

import java.rmi.registry.Registry;

public class Thaumcraft implements IIntegration {

	public static final String PLUGIN_MODID = "thaumcraft";

	private static boolean initDone = false;

	public static final Thaumcraft INSTANCE = new Thaumcraft();

	@Override
	public void init() {
		if (!Config.Options.isModEnabled(PLUGIN_MODID) || initDone) {
			return;
		}
		initDone = true;

		MinecraftForge.EVENT_BUS.register(this);
		registerAspectsForOreDictEntries();
	}

	private void registerAspectsForOreDictEntries() {
		ItemStack itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(BaseMetals.MODID + Oredicts.INGOT + MaterialNames.ADAMANTINE)));
		ThaumcraftApi.registerComplexObjectTag(itemStack, (new AspectList()).add(Aspect.METAL, 33));
	}
}
