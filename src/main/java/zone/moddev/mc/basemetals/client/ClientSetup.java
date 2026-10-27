package zone.moddev.mc.basemetals.client;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.content.MaterialItems;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.entity.ModEntities;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;

public final class ClientSetup {
    private ClientSetup() {}

    public static void register(net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext context) {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener(ClientSetup::registerRenderers);
        FMLClientSetupEvent.getBus(context.getModBusGroup()).addListener(ClientSetup::clientSetup);
        context.registerExtensionPoint(
                net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> new BaseMetalsConfigScreen(parent)));
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CUSTOM_ARROW.get(), MaterialProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.CUSTOM_BOLT.get(), MaterialProjectileRenderer::new);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry.registerConfigScreen(
                    BaseMetals.MOD_ID, BaseMetalsConfigScreen::new);
            ModContent.blocksById().forEach((id, block) -> {
                if (id.endsWith("_ore")) ItemBlockRenderTypes.setRenderLayer(block.get(), ChunkSectionLayer.CUTOUT);
            });
            ModContent.fluids().values().forEach(fluid -> {
                ItemBlockRenderTypes.setRenderLayer(fluid.source().get(), ChunkSectionLayer.TRANSLUCENT);
                ItemBlockRenderTypes.setRenderLayer(fluid.flowing().get(), ChunkSectionLayer.TRANSLUCENT);
            });
            ModContent.blocksById().values().stream()
                    .map(entry -> entry.get())
                    .forEach(ClientSetup::registerBlockRenderLayer);


        });
    }

    private static void registerBlockRenderLayer(Block block) {
        if (block instanceof IronBarsBlock) {
            ItemBlockRenderTypes.setRenderLayer(block, ChunkSectionLayer.CUTOUT);
        } else if (block instanceof DoorBlock || block instanceof TrapDoorBlock) {
            ItemBlockRenderTypes.setRenderLayer(block, ChunkSectionLayer.CUTOUT);
        }
    }
}
