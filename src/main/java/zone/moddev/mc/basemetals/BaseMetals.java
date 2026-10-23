package zone.moddev.mc.basemetals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;
import zone.moddev.mc.basemetals.entity.ModEntities;
import zone.moddev.mc.basemetals.loot.ModLoot;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
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
        IEventBus modBus = context.getModEventBus();
        ModContent.register(modBus);
        ModTabs.register(modBus);
        zone.moddev.mc.basemetals.recipe.ContentRecipeCondition.SERIALIZERS.register(modBus);
        modBus.addListener(BaseMetalsConfig::onConfigLoading);
        modBus.addListener((net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) -> {
            event.enqueueWork(() -> {
                zone.moddev.mc.basemetals.network.ContentModeNetwork.register();
            });
        });
        CrushingRecipe.TYPES.register(modBus);
        CrushingRecipe.SERIALIZERS.register(modBus);
        ModEntities.register(modBus);
        ModLoot.register(modBus);
        context.registerConfig(ModConfig.Type.COMMON, BaseMetalsConfig.SPEC);
        MinecraftForge.EVENT_BUS.register(new BaseMetalsEvents());
    }
}
