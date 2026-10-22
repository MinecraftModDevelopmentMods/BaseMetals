package zone.moddev.mc.basemetals.loot;

import zone.moddev.mc.basemetals.BaseMetals;

import com.mojang.serialization.MapCodec;

import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class ModLoot {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> SERIALIZERS = DeferredRegister.create(
            NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BaseMetals.MOD_ID);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AppendLootTableModifier>> ADD_TABLE =
            SERIALIZERS.register("add_table", () -> AppendLootTableModifier.CODEC);

    private ModLoot() {}
    public static void register(IEventBus bus) { SERIALIZERS.register(bus); }
}
