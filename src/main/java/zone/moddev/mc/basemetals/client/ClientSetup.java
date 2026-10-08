package zone.moddev.mc.basemetals.client;

import zone.moddev.mc.basemetals.content.FluidContent;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.entity.ModEntities;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.TrapDoorBlock;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry;

public final class ClientSetup {
    private ClientSetup() {}

    public static void register() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
                () -> (minecraft, parent) -> new BaseMetalsConfigScreen(parent));
        WorldSettingsExtensionRegistry.registerConfigScreen("basemetals", BaseMetalsConfigScreen::new);
        net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus()
                .addListener(ClientSetup::setupRendering);
    }

    private static void setupRendering(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.CUSTOM_ARROW.get(), MaterialProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.CUSTOM_BOLT.get(), MaterialProjectileRenderer::new);

        event.enqueueWork(ClientSetup::registerRenderLayers);
    }

    private static void registerRenderLayers() {
        ModContent.blocksById().forEach((name, handle) -> {
            Block block = handle.get();
            if (name.endsWith("_ore") || block instanceof DoorBlock
                    || block instanceof TrapDoorBlock || block instanceof PaneBlock) {
                RenderTypeLookup.setRenderLayer(block, RenderType.cutoutMipped());
            }
        });

        for (FluidContent fluid : ModContent.fluids().values()) {
            RenderTypeLookup.setRenderLayer(fluid.source().get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(fluid.flowing().get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(fluid.block().get(), RenderType.translucent());
        }
    }

}
