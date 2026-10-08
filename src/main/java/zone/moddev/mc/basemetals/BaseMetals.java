package zone.moddev.mc.basemetals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.basemetals.client.ClientSetup;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.entity.ModEntities;
import zone.moddev.mc.basemetals.migration.LegacyWorldDataHook;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;
import zone.moddev.mc.basemetals.loot.ContentModeLootCondition;
import zone.moddev.mc.basemetals.network.ContentModeNetwork;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BaseMetals.MOD_ID)
public final class BaseMetals {
    public static final String MOD_ID = "basemetals";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public BaseMetals() {
        ModTags.initialize();
        LegacyWorldDataHook.register();
        ContentModeNetwork.register();
        ModContent.initializeFluids();
        ContentModeLootCondition.register();
        ModEntities.initialize();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BaseMetalsConfig.SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BaseMetalsConfig::onConfigLoading);
        MinecraftForge.EVENT_BUS.register(new BaseMetalsEvents());
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientSetup::register);
    }
}
