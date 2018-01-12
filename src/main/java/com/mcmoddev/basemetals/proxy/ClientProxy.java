package com.mcmoddev.basemetals.proxy;

import com.mcmoddev.lib.client.registrations.RegistrationHelper;
import com.mcmoddev.lib.client.renderer.RenderCustomArrow;
import com.mcmoddev.lib.client.renderer.RenderCustomBolt;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.entity.EntityCustomArrow;
import com.mcmoddev.lib.entity.EntityCustomBolt;
import com.mcmoddev.lib.init.Blocks;
import com.mcmoddev.lib.init.Fluids;
import com.mcmoddev.lib.init.Items;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
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
			if (!name.endsWith(Names.ANVIL.toString()))
				RegistrationHelper.registerItemRender(name);
			else {
				final String[] names = new String[] { "intact", "slightly_damaged", "very_damaged" };
				final Item item = Items.getItemByName(name);
				for (int i = 0; i < 3; i++) {
					final ResourceLocation rl = new ResourceLocation("basemetals", String.format("%s_%s", name, names[i]));
					ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(rl, "inventory"));
				}
			}
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
