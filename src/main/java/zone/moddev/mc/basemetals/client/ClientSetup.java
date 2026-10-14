package zone.moddev.mc.basemetals.client;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.content.MaterialItems;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.entity.ModEntities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.searchtree.MutableSearchTree;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CUSTOM_ARROW.get(), MaterialProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.CUSTOM_BOLT.get(), MaterialProjectileRenderer::new);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        net.minecraftforge.fml.ModLoadingContext.get().registerExtensionPoint(
                net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory(
                        (minecraft, parent) -> new BaseMetalsConfigScreen(parent)));
        event.enqueueWork(() -> {
            zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry.registerConfigScreen(
                    BaseMetals.MOD_ID, BaseMetalsConfigScreen::new);
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
                    ItemProperties.register(item, new ResourceLocation("pull"), (stack, level, user, seed) ->
                            user == null || user.getUseItem() != stack ? 0.0F
                                    : (float) (stack.getUseDuration() - user.getUseItemRemainingTicks()) / 20.0F);
                    ItemProperties.register(item, new ResourceLocation("pulling"), (stack, level, user, seed) ->
                            user != null && user.isUsingItem() && user.getUseItem() == stack ? 1.0F : 0.0F);
                }
                if (item instanceof MaterialItems.Shield) {
                    ItemProperties.register(item, new ResourceLocation("blocking"), (stack, level, user, seed) ->
                            user != null && user.isUsingItem() && user.getUseItem() == stack ? 1.0F : 0.0F);
                }
                if (item instanceof MaterialItems.FishingRod) {
                    ItemProperties.register(item, new ResourceLocation("cast"), (stack, level, user, seed) -> {
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
    public static void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(ClientSetup::ensureBucketSearchIndex);
    }

    private static void ensureBucketSearchIndex() {
        Minecraft minecraft = Minecraft.getInstance();
        MutableSearchTree<ItemStack> names = minecraft.getSearchTree(SearchRegistry.CREATIVE_NAMES);
        MutableSearchTree<ItemStack> tags = minecraft.getSearchTree(SearchRegistry.CREATIVE_TAGS);
        if (names == null || tags == null || ModContent.fluids().values().stream().allMatch(fluid -> {
            Item bucket = fluid.bucket().get();
            return names.search("basemetals:" + bucket.getRegistryName().getPath())
                    .stream().anyMatch(stack -> stack.is(bucket));
        })) {
            return;
        }

        NonNullList<ItemStack> searchable = NonNullList.create();
        for (Item item : Registry.ITEM) {
            item.fillItemCategory(CreativeModeTab.TAB_SEARCH, searchable);
        }
        names.clear();
        tags.clear();
        searchable.forEach(stack -> {
            names.add(stack);
            tags.add(stack);
        });
        names.refresh();
        tags.refresh();
        BaseMetals.LOGGER.info(
                "Rebuilt the creative search index after detecting missing Base Metals fluid buckets");
    }

    private static void registerBlockRenderLayer(Block block) {
        if (block instanceof IronBarsBlock) {
            ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
        } else if (block instanceof DoorBlock || block instanceof TrapDoorBlock) {
            ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
        }
    }
}
