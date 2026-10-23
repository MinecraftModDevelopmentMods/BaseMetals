package zone.moddev.mc.basemetals.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Applies the selected mode to Base Metals outputs made by optional machines. */
public record ContentRecipeCondition(String item) implements ICondition {
    public static final MapCodec<ContentRecipeCondition> CODEC =
            Codec.STRING.fieldOf("item").xmap(ContentRecipeCondition::new, ContentRecipeCondition::item);
    public static final DeferredRegister<MapCodec<? extends ICondition>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.CONDITION_SERIALIZERS, BaseMetals.MOD_ID);

    static {
        SERIALIZERS.register("content_mode", () -> CODEC);
    }

    @Override
    public boolean test(IContext context, DynamicOps<?> ops) {
        return ContentPolicy.active().allows(item);
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
