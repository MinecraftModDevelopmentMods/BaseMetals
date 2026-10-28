package zone.moddev.mc.basemetals.client;

import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.entity.ModEntities;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry;

public final class ClientSetup {
    private ClientSetup() {}

    public static void register(FMLJavaModLoadingContext context) {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener(ClientSetup::registerRenderers);
        ModelEvent.BakeFluidModels.BUS.addListener(ClientSetup::bakeFluidModels);
        FMLClientSetupEvent.getBus(context.getModBusGroup()).addListener(ClientSetup::clientSetup);
        context.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> new BaseMetalsConfigScreen(parent)));
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CUSTOM_ARROW.get(), MaterialProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.CUSTOM_BOLT.get(), MaterialProjectileRenderer::new);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> WorldSettingsExtensionRegistry.registerConfigScreen(
                BaseMetals.MOD_ID, BaseMetalsConfigScreen::new));
    }

    public static void bakeFluidModels(ModelEvent.BakeFluidModels event) {
        ModContent.fluids().forEach((name, fluid) -> {
            var appearance = IClientFluidTypeExtensions.of(fluid.source().get());
            var unbaked = new FluidModel.Unbaked(
                    new Material(appearance.getStillTexture(), true),
                    new Material(appearance.getFlowingTexture(), true),
                    null, state -> appearance.getTintColor());
            var model = unbaked.bake(event.materials(), () -> "Base Metals " + name);

            event.register(fluid.source().get(), model);
            event.register(fluid.flowing().get(), model);
        });
    }
}
