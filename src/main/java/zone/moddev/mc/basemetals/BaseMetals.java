package zone.moddev.mc.basemetals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.content.BaseMetalBucketItem;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.effects.MaterialEffects;
import zone.moddev.mc.basemetals.recipe.ModRecipes;
import zone.moddev.mc.basemetals.loot.ModLoot;
import zone.moddev.mc.basemetals.compat.MekanismCompat;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.common.Mod;

@Mod(BaseMetals.MOD_ID)
public final class BaseMetals {
    public static final String MOD_ID = "basemetals";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public BaseMetals(IEventBus modBus, ModContainer modContainer) {
        // MMDLib loads mmdlib/ definitions after mod construction; the event contract
        // and bucket implementation they name must already be registered.
        MaterialEffects.register();
        BaseMetalBucketItem.registerImplementation();
        ModContent.register(modBus);
        ModTabs.REGISTER.register(modBus);
        modBus.addListener(BaseMetalsConfig::onConfigLoading);
        ModRecipes.SERIALIZERS.register(modBus);
        ModLoot.register(modBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, BaseMetalsConfig.SPEC);
        if (ModList.get().isLoaded("mekanism")) {
            LOGGER.info("Loading Mekanism Compat");
            MekanismCompat.register(modBus);
        }
        NeoForge.EVENT_BUS.register(new BaseMetalsEvents());
    }
}
