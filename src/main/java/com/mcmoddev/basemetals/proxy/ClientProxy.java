package com.mcmoddev.basemetals.proxy;

import com.mcmoddev.lib.client.registrations.RegistrationHelper;
import com.mcmoddev.lib.client.renderer.RenderCustomArrow;
import com.mcmoddev.lib.client.renderer.RenderCustomBolt;
import com.mcmoddev.lib.entity.EntityCustomArrow;
import com.mcmoddev.lib.entity.EntityCustomBolt;
import com.mcmoddev.lib.init.Blocks;
import com.mcmoddev.lib.init.Fluids;
import com.mcmoddev.lib.init.Items;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Base Metals Client Proxy
 *
 * @author Jasmine Iwanek
 *
 */
@EventBusSubscriber
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		RenderingRegistry.registerEntityRenderingHandler(EntityCustomArrow.class, RenderCustomArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCustomBolt.class, RenderCustomBolt::new);
		MinecraftForge.EVENT_BUS.register(this);
	}

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
		for (final String name : Items.getItemRegistry().keySet()) {
			RegistrationHelper.registerItemRender(name);
		}

		for (final String name : Blocks.getBlockRegistry().keySet()) {
			RegistrationHelper.registerBlockRender(name);
		}
    }

	@SubscribeEvent
	public void fluidRendering(RegistryEvent.Register<MMDMaterial> event) {
		for (final String name : Fluids.getFluidBlockRegistry().keySet()) {
			RegistrationHelper.registerFluidRender(name);
		}
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		if (Loader.isModLoaded("waila")) {
			com.mcmoddev.lib.waila.Waila.init();
		}
	}
}
