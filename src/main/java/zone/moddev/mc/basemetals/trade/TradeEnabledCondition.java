package zone.moddev.mc.basemetals.trade;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Keeps the trade switch live without changing the data registry between content modes. */
public record TradeEnabledCondition(Identifier item) implements LootItemCondition {
    public static final MapCodec<TradeEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("item").forGetter(TradeEnabledCondition::item)
    ).apply(instance, TradeEnabledCondition::new));

    private static final DeferredRegister<MapCodec<? extends LootItemCondition>> CONDITIONS =
            DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, BaseMetals.MOD_ID);

    static {
        CONDITIONS.register("trade_enabled", () -> CODEC);
    }

    public static void register(BusGroup bus) {
        CONDITIONS.register(bus);
    }

    @Override
    public MapCodec<TradeEnabledCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(LootContext context) {
        return BaseMetalsConfig.VILLAGER_TRADES.get() && ContentPolicy.active().allows(item.toString());
    }
}
