package zone.moddev.mc.basemetals.loot;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import com.mojang.serialization.MapCodec;

import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.minecraft.core.registries.BuiltInRegistries;
public final class AppendLootTableModifier extends LootModifier {
    public static final MapCodec<AppendLootTableModifier> CODEC =
            RecordCodecBuilder.<AppendLootTableModifier>mapCodec(instance ->
                    codecStart(instance)
                            .and(ResourceLocation.CODEC.fieldOf("table")
                                    .forGetter(modifier -> modifier.table))
                            .apply(instance, AppendLootTableModifier::new));

    private final ResourceLocation table;

    public AppendLootTableModifier(LootItemCondition[] conditions, ResourceLocation table) {
        super(conditions);
        this.table = table;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        context.getLevel().getServer().reloadableRegistries()
                .getLootTable(ResourceKey.create(Registries.LOOT_TABLE, table))
                .getRandomItemsRaw(context, item -> {
            if (zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(BuiltInRegistries.ITEM.getKey(item.getItem()).toString())) {
                generatedLoot.add(item);
            }
        });
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
