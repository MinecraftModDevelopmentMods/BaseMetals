package zone.moddev.mc.basemetals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;
import zone.moddev.mc.basemetals.entity.ModEntities;
import zone.moddev.mc.basemetals.loot.ModLoot;
import zone.moddev.mc.basemetals.compat.MekanismCompat;

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

    public BaseMetals() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModContent.register(modBus);
        modBus.addListener(ModTabs::register);
        modBus.addListener(BaseMetalsConfig::onConfigLoading);
        modBus.addListener((net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) -> {
            zone.moddev.mc.basemetals.network.ContentModeNetwork.register();
            event.enqueueWork(() -> net.minecraftforge.common.crafting.CraftingHelper.register(
                    new zone.moddev.mc.basemetals.recipe.ContentRecipeCondition.Serializer()));
        });
        CrushingRecipe.TYPES.register(modBus);
        CrushingRecipe.SERIALIZERS.register(modBus);
        ModEntities.register(modBus);
        ModLoot.register(modBus);
        if (ModList.get().isLoaded("mekanism")) MekanismCompat.register(modBus);
        if (ModList.get().isLoaded("tconstruct")) {
            MinecraftForge.EVENT_BUS.addListener(net.minecraftforge.eventbus.api.EventPriority.LOWEST,
                    zone.moddev.mc.basemetals.compat.TinkersCompat::addReloadListener);
        }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BaseMetalsConfig.SPEC);
        MinecraftForge.EVENT_BUS.register(new BaseMetalsEvents());
    }
}
