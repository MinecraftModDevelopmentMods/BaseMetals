package zone.moddev.mc.basemetals.loot;

import zone.moddev.mc.basemetals.BaseMetals;

import com.mojang.serialization.Codec;
import net.neoforged.common.loot.IGlobalLootModifier;
import net.neoforged.eventbus.api.IEventBus;
import net.neoforged.registries.DeferredRegister;
import net.neoforged.registries.ForgeRegistries;
import net.neoforged.registries.RegistryObject;

public final class ModLoot {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BaseMetals.MOD_ID);
    public static final RegistryObject<Codec<AppendLootTableModifier>> ADD_TABLE =
            SERIALIZERS.register("add_table", () -> AppendLootTableModifier.CODEC);

    private ModLoot() {}
    public static void register(IEventBus bus) { SERIALIZERS.register(bus); }
}
