package com.mcmoddev.basemetals.init;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.events.MMDLibRegisterFluids;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.util.Config.Options;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Creates Base Metals fluids and their blocks. */
@Mod.EventBusSubscriber(modid = BaseMetals.MODID)
public final class Fluids extends com.mcmoddev.lib.init.Fluids {

	private Fluids() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/** Creates enabled molten-metal fluids during MMDLib registration. */
	@SubscribeEvent
	public static void registerEvent(MMDLibRegisterFluids event) {
		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY, MaterialNames.AQUARIUM, MaterialNames.BISMUTH,
				MaterialNames.BRASS, MaterialNames.BRONZE, MaterialNames.COLDIRON,
				MaterialNames.COPPER, MaterialNames.CUPRONICKEL, MaterialNames.ELECTRUM,
				MaterialNames.INVAR, MaterialNames.LEAD, MaterialNames.MITHRIL,
				MaterialNames.NICKEL, MaterialNames.PEWTER, MaterialNames.PLATINUM,
				MaterialNames.SILVER, MaterialNames.STARSTEEL, MaterialNames.STEEL,
				MaterialNames.TIN, MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial).filter(Options::isFluidEnabled)
				.forEach(materialName -> {
					addFluid(materialName, 2000, 10000, 769, 10);
					addFluidBlock(materialName);
				});

		if (Materials.hasMaterial(MaterialNames.MERCURY) && Options.isFluidEnabled(MaterialNames.MERCURY)) {
			addFluid(MaterialNames.MERCURY, 13594, 2000, 769, 0);
			addFluidBlock(MaterialNames.MERCURY);
		}
	}
}
