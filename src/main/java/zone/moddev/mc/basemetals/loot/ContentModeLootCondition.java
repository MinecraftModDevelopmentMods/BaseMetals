package zone.moddev.mc.basemetals.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.core.Registry;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Filters only the auxiliary chest entries supplied by Base Metals. */
public final class ContentModeLootCondition implements LootItemCondition {
    private static LootItemConditionType type;
    private final String item;

    public static void register() {
        type = Registry.register(Registry.LOOT_CONDITION_TYPE,
                new ResourceLocation("basemetals", "content_mode"), new LootItemConditionType(new Serializer()));
    }

    @Override
    public LootItemConditionType getType() {
        return type;
    }

    public ContentModeLootCondition(String item) {
        this.item = item;
    }

    @Override
    public boolean test(LootContext context) {
        return ContentPolicy.active().allows(item);
    }

    public static final class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ContentModeLootCondition> {
        @Override
        public void serialize(JsonObject json, ContentModeLootCondition value, JsonSerializationContext context) {
            json.addProperty("item", value.item);
        }

        @Override
        public ContentModeLootCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return new ContentModeLootCondition(GsonHelper.getAsString(json, "item"));
        }
    }
}
