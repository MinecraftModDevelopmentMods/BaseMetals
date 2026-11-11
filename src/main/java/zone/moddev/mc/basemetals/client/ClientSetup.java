package zone.moddev.mc.basemetals.client;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.content.MetalFluidType;
import zone.moddev.mc.basemetals.content.ModContent;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

/**
 * Client hooks for Base Metals. MMDLib's runtime client registers projectile renderers,
 * bow/crossbow/rod/shield item properties and cutout layers for registrar content.
 */
@EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ModList.get().getModContainerById(BaseMetals.MOD_ID).orElseThrow()
                .registerExtensionPoint(IConfigScreenFactory.class,
                        (container, parent) -> new BaseMetalsConfigScreen(parent));
        event.enqueueWork(() -> {
            ModContent.blocksById().forEach((id, block) -> {
                if (id.endsWith("_ore")) ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
            });
            ModContent.fluids().values().forEach(fluid -> {
                ItemBlockRenderTypes.setRenderLayer(fluid.source().get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(fluid.flowing().get(), RenderType.translucent());
            });
        });
    }

    // MMDLib tints layer 0 of every registrar item and block with the material colour.
    // Base Metals artwork is already coloured; this mod's handlers register after MMDLib's.
    @SubscribeEvent
    public static void itemColours(RegisterColorHandlersEvent.Item event) {
        event.register((stack, layer) -> -1, ModContent.registrar().items().values().stream()
                .map(holder -> (Item) holder.get()).toArray(Item[]::new));
    }

    @SubscribeEvent
    public static void blockColours(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, layer) -> -1, ModContent.registrar().blocks().values().stream()
                .map(holder -> (Block) holder.get()).toArray(Block[]::new));
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        // Register each fluid extension once through the current event API.
        ModContent.FLUID_TYPES.getEntries().forEach(holder -> {
            var type = holder.get();
            if (type instanceof MetalFluidType metal) {
                metal.registerClientExtensions(extensions -> event.registerFluidType(extensions, type));
            }
        });
    }
}
