package com.mcmoddev.basemetals.vanillabits;

import java.util.Arrays;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.VanillaMaterialNames;
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
		Arrays.asList(VanillaMaterialNames.CHARCOAL, VanillaMaterialNames.COAL,
				VanillaMaterialNames.DIAMOND, VanillaMaterialNames.EMERALD, VanillaMaterialNames.ENDER, 
				VanillaMaterialNames.GOLD, VanillaMaterialNames.IRON, VanillaMaterialNames.LAPIS,
				VanillaMaterialNames.OBSIDIAN, VanillaMaterialNames.PRISMARINE, VanillaMaterialNames.QUARTZ,
				VanillaMaterialNames.WOOD, VanillaMaterialNames.REDSTONE, VanillaMaterialNames.STONE, 
				VanillaMaterialNames.REDSTONE).stream()
		.filter(Options::isMaterialEnabled)
		.filter(Options::isFluidEnabled).forEach(materialName -> {
			addFluid(materialName, 2000, 10000, 769, 10);
			addFluidBlock(materialName);
		});
	}
}
