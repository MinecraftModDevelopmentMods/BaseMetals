package zone.moddev.mc.basemetals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;
import zone.moddev.mc.basemetals.entity.ModEntities;
import zone.moddev.mc.basemetals.loot.ModLoot;

import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BaseMetals.MOD_ID)
public final class BaseMetals {
    public static final String MOD_ID = "basemetals";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public BaseMetals(FMLJavaModLoadingContext context) {
        BusGroup modBus = context.getModBusGroup();
        ModContent.register(modBus);
        ModTabs.register(modBus);
        zone.moddev.mc.basemetals.recipe.ContentRecipeCondition.SERIALIZERS.register(modBus);
        net.minecraftforge.fml.event.config.ModConfigEvent.Loading.getBus(modBus)
                .addListener(BaseMetalsConfig::onConfigLoading);
        FMLCommonSetupEvent.getBus(modBus).addListener(event -> {
            event.enqueueWork(() -> {
                zone.moddev.mc.basemetals.network.ContentModeNetwork.register();
            });
        });
        CrushingRecipe.TYPES.register(modBus);
        CrushingRecipe.SERIALIZERS.register(modBus);
        ModEntities.register(modBus);
        ModLoot.register(modBus);
        context.registerConfig(ModConfig.Type.COMMON, BaseMetalsConfig.SPEC);
        if (net.minecraftforge.fml.loading.FMLEnvironment.dist == net.minecraftforge.api.distmarker.Dist.CLIENT) {
            zone.moddev.mc.basemetals.client.ClientSetup.register(context);
        }
        BaseMetalsEvents.register();
    }
}
