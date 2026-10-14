package zone.moddev.mc.basemetals.loot;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public final class AppendLootTableModifier extends LootModifier {
    private final ResourceLocation table;

    public AppendLootTableModifier(LootItemCondition[] conditions, ResourceLocation table) {
        super(conditions);
        this.table = table;
    }

    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        context.getLootTable(table).getRandomItemsRaw(context, item -> {
            if (zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(item.getItem().getRegistryName().toString())) {
                generatedLoot.add(item);
            }
        });
        return generatedLoot;
    }

    public static final class Serializer extends GlobalLootModifierSerializer<AppendLootTableModifier> {
        @Override
        public AppendLootTableModifier read(ResourceLocation location, JsonObject object,
                LootItemCondition[] conditions) {
            return new AppendLootTableModifier(conditions, new ResourceLocation(object.get("table").getAsString()));
        }

        @Override
        public JsonObject write(AppendLootTableModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("table", instance.table.toString());
            return json;
        }
    }
}
