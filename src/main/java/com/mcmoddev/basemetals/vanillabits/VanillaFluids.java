package com.mcmoddev.basemetals.vanillabits;

import java.util.Arrays;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.MaterialNames;
import com.mcmoddev.lib.events.MMDLibRegisterFluids;
import com.mcmoddev.lib.init.Fluids;
import com.mcmoddev.lib.util.Config.Options;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=BaseMetals.MODID)
public class VanillaFluids extends Fluids {
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void registerFluids(MMDLibRegisterFluids ev) {
		// Vanilla Materials need to always have fluids available in case of tie-in mods
		Arrays.asList(MaterialNames.CHARCOAL, MaterialNames.COAL,
				MaterialNames.DIAMOND, MaterialNames.EMERALD, MaterialNames.ENDER, 
				MaterialNames.GOLD, MaterialNames.IRON, MaterialNames.LAPIS,
				MaterialNames.OBSIDIAN, MaterialNames.PRISMARINE, MaterialNames.QUARTZ,
				MaterialNames.WOOD, MaterialNames.REDSTONE, MaterialNames.STONE, 
				MaterialNames.REDSTONE).stream()
		.filter(Options::isMaterialEnabled)
		.filter(Options::isFluidEnabled).forEach(materialName -> {
			addFluid(materialName, 2000, 10000, 769, 10);
			addFluidBlock(materialName);
		});
	}
}
