package com.mcmoddev.basemetals.proxy;

import javax.annotation.Nullable;

import com.mcmoddev.basemetals.BaseMetals;
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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Base Metals Client Proxy.
 *
 * @author Jasmine Iwanek
 *
 */
@EventBusSubscriber
public final class ClientProxy extends CommonProxy {

	@Override
	public void preInit(final FMLPreInitializationEvent event) {
		super.preInit(event);

		RenderingRegistry.registerEntityRenderingHandler(EntityCustomArrow.class,
				RenderCustomArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCustomBolt.class,
				RenderCustomBolt::new);
		MinecraftForge.EVENT_BUS.register(this);
	}

	/**
	 * Registers Block and Item models for this mod.
	 *
	 * @param event The Event.
	 */
	@SubscribeEvent
	public static void registerModels(final ModelRegistryEvent event) {
		for (final String name : Items.getItemRegistry().keySet()) {
			if (!name.endsWith(Names.ANVIL.toString())) {
				RegistrationHelper.registerItemRender(name);
			} else {
				final String[] names = new String[] { "intact", "slightly_damaged",
						"very_damaged" };
				final Item item = Items.getItemByName(name);
				for (int i = 0; i < 3; i++) {
					final ResourceLocation rl = new ResourceLocation(BaseMetals.MODID,
							String.format("%s_%s", name, names[i]));
					ModelLoader.setCustomModelResourceLocation(item, i,
							new ModelResourceLocation(rl, "inventory"));
				}
			}
		}

		for (final String name : Blocks.getBlockRegistry().keySet()) {
			RegistrationHelper.registerBlockRender(name);
		}
	}

	/**
	 * Registers Fluid models for this mod.
	 *
	 * @param event The Event.
	 */
	@SubscribeEvent
	public void fluidRendering(final RegistryEvent.Register<MMDMaterial> event) {
		for (final String name : Fluids.getFluidBlockRegistry().keySet()) {
			RegistrationHelper.registerFluidRender(name);
		}
	}

	@Override
	public void init(final FMLInitializationEvent event) {
		super.init(event);
		if (Loader.isModLoaded("waila")) {
			com.mcmoddev.lib.waila.Waila.init();
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public World getWorld(@Nullable final int dimension) {
		return Minecraft.getMinecraft().world;
	}
}
