package zone.moddev.mc.basemetals.client;

import zone.moddev.mc.basemetals.content.FluidContent;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.entity.ModEntities;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry;

public final class ClientSetup {
    private ClientSetup() {}

    public static void register() {
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory((minecraft, parent) -> new BaseMetalsConfigScreen(parent)));
        WorldSettingsExtensionRegistry.registerConfigScreen("basemetals", BaseMetalsConfigScreen::new);
        net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus()
                .addListener(ClientSetup::setupRendering);
        net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus()
                .addListener(ClientSetup::registerEntityRenderers);
    }

    private static void setupRendering(FMLClientSetupEvent event) {
        event.enqueueWork(ClientSetup::registerRenderLayers);
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CUSTOM_ARROW.get(), MaterialProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.CUSTOM_BOLT.get(), MaterialProjectileRenderer::new);
    }

    private static void registerRenderLayers() {
        ModContent.blocksById().forEach((name, handle) -> {
            Block block = handle.get();
            if (name.endsWith("_ore") || block instanceof DoorBlock
                    || block instanceof TrapDoorBlock || block instanceof IronBarsBlock) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
            }
        });

        for (FluidContent fluid : ModContent.fluids().values()) {
            ItemBlockRenderTypes.setRenderLayer(fluid.source().get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(fluid.flowing().get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(fluid.block().get(), RenderType.translucent());
        }
    }

}
