package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.Thaumcraft;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = BMeThaumcraft.PLUGIN_MODID)
public final class BMeThaumcraft extends com.mcmoddev.lib.integration.plugins.Thaumcraft {

	public static final String PLUGIN_MODID = Thaumcraft.PLUGIN_MODID;

	public BMeThaumcraft() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void init() {
		INSTANCE.init();
		if (!Config.Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(this);
	}

//		Materials.getAllMaterials().stream()
//				.filter( mat -> !mat.isVanilla())
//				.forEach( mat -> registerAspects(ev, mat));
//
//		Materials.getAllMaterials().stream()
//				.filter( mat -> mat.isVanilla())
//				.forEach( mat -> registerAspects(ev, mat, Names.NUGGET));
//	}

//	protected void registerAspects(final AspectRegistryEvent ev, MMDMaterial mat){
//		registerAspects(ev, mat, Names.NUGGET.toString());
//		registerAspects(ev, mat, Names.INGOT.toString());
//		registerAspects(ev, mat, Names.ORE.toString());
//	}
//
//	protected void registerAspects(final AspectRegistryEvent ev, MMDMaterial mat, String name){
//		ev.register.registerComplexObjectTag(mat.getItemStack(name), addAspects(new AspectList(), mat, name));
//	}
}
