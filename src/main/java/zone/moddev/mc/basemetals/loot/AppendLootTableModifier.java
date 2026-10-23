package zone.moddev.mc.basemetals.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public final class AppendLootTableModifier extends LootModifier {
    public static final MapCodec<AppendLootTableModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            codecStart(instance).and(ResourceLocation.CODEC.fieldOf("table").forGetter(modifier -> modifier.table))
                    .apply(instance, AppendLootTableModifier::new));
    private final ResourceLocation table;

    public AppendLootTableModifier(LootItemCondition[] conditions, ResourceLocation table) {
        super(conditions);
        this.table = table;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        context.getResolver().get(net.minecraft.core.registries.Registries.LOOT_TABLE, net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.LOOT_TABLE, table)).orElseThrow().value().getRandomItemsRaw(context, item -> {
            if (zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(item.getItem()).toString())) {
                generatedLoot.add(item);
            }
        });
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() { return CODEC; }
}
