package zone.moddev.mc.basemetals.client;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.content.MaterialItems;
import zone.moddev.mc.basemetals.content.MetalFluidType;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.entity.ModEntities;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CUSTOM_ARROW.get(), MaterialProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.CUSTOM_BOLT.get(), MaterialProjectileRenderer::new);
    }

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
            ModContent.blocksById().values().stream()
                    .map(entry -> entry.get())
                    .forEach(ClientSetup::registerBlockRenderLayer);

            ModContent.itemsById().values().forEach(entry -> {
                net.minecraft.world.item.Item item = entry.get();
                if (item instanceof MaterialItems.Bow || item instanceof MaterialItems.Crossbow) {
                    ItemProperties.register(item, ResourceLocation.withDefaultNamespace("pull"), (stack, level, user, seed) ->
                            user == null || user.getUseItem() != stack ? 0.0F
                                    : (float) (stack.getUseDuration(user) - user.getUseItemRemainingTicks()) / 20.0F);
                    ItemProperties.register(item, ResourceLocation.withDefaultNamespace("pulling"), (stack, level, user, seed) ->
                            user != null && user.isUsingItem() && user.getUseItem() == stack ? 1.0F : 0.0F);
                }
                if (item instanceof MaterialItems.Shield) {
                    ItemProperties.register(item, ResourceLocation.withDefaultNamespace("blocking"), (stack, level, user, seed) ->
                            user != null && user.isUsingItem() && user.getUseItem() == stack ? 1.0F : 0.0F);
                }
                if (item instanceof MaterialItems.FishingRod) {
                    ItemProperties.register(item, ResourceLocation.withDefaultNamespace("cast"), (stack, level, user, seed) -> {
                        if (user == null) return 0.0F;
                        boolean main = user.getMainHandItem() == stack;
                        boolean off = user.getOffhandItem() == stack;
                        if (user.getMainHandItem().getItem() instanceof FishingRodItem) off = false;
                        return (main || off) && user instanceof Player player && player.fishing != null ? 1.0F : 0.0F;
                    });
                }
            });
        });
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

    private static void registerBlockRenderLayer(Block block) {
        if (block instanceof IronBarsBlock) {
            ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
        } else if (block instanceof DoorBlock || block instanceof TrapDoorBlock) {
            ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
        }
    }
}
