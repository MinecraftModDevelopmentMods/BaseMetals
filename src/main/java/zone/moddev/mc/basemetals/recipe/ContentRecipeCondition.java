package zone.moddev.mc.basemetals.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.minecraft.resources.ResourceLocation;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Applies the selected mode to outputs made by optional machines. */
@EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public record ContentRecipeCondition(String item) implements ICondition {
    public static final MapCodec<ContentRecipeCondition> CODEC = Codec.STRING.fieldOf("item")
            .xmap(ContentRecipeCondition::new, ContentRecipeCondition::item);

    @Override public boolean test(IContext context) { return ContentPolicy.active().allows(item); }
    @Override public MapCodec<? extends ICondition> codec() { return CODEC; }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(NeoForgeRegistries.Keys.CONDITION_CODECS,
                ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, "content_mode"), () -> CODEC);
    }
}
