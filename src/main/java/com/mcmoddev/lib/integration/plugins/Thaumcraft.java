package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

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
	}

    @SubscribeEvent
    public void registerAspects(final AspectRegistryEvent ev) {
        Materials.getAllMaterials().stream()
        .filter( mat -> mat.hasOre())
        .forEach( mat -> ev.register.registerComplexObjectTag(mat.getBlockItemStack(Names.ORE), (new AspectList()).add(Aspect.METAL, 33)) );
    }
}
