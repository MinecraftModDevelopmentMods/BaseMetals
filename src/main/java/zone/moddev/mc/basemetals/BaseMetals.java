package zone.moddev.mc.basemetals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.basemetals.client.ClientSetup;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.entity.ModEntities;
import zone.moddev.mc.basemetals.migration.LegacyWorldDataHook;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;
import zone.moddev.mc.basemetals.loot.ContentModeLootCondition;
import zone.moddev.mc.basemetals.network.ContentModeNetwork;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BaseMetals.MOD_ID)
public final class BaseMetals {
    public static final String MOD_ID = "basemetals";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public BaseMetals() {
        CrushingRecipe.register();
        LootConditionManager.registerCondition(new ContentModeLootCondition.Serializer());
        ModEntities.initialize();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BaseMetalsConfig.SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BaseMetalsConfig::onConfigLoading);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(new BaseMetalsEvents());
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientSetup::register);
    }

    private void setup(FMLCommonSetupEvent event) {
        // Creating a channel grows Forge 25's shared listener tables.
        DeferredWorkQueue.runLater(ContentModeNetwork::register);
        // Forge's world-save hook list is shared and is not thread-safe.
        DeferredWorkQueue.runLater(LegacyWorldDataHook::register);
    }
}
