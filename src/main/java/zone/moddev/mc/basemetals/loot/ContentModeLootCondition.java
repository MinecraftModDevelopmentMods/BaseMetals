package zone.moddev.mc.basemetals.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.registry.Registry;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Filters only the auxiliary chest entries supplied by Base Metals. */
public final class ContentModeLootCondition implements ILootCondition {
    private static LootConditionType type;
    private final String item;

    public static void register() {
        type = Registry.register(Registry.LOOT_CONDITION_TYPE,
                new ResourceLocation("basemetals", "content_mode"), new LootConditionType(new Serializer()));
    }

    @Override
    public LootConditionType getType() {
        return type;
    }

    public ContentModeLootCondition(String item) {
        this.item = item;
    }

    @Override
    public boolean test(LootContext context) {
        return ContentPolicy.active().allows(item);
    }

    public static final class Serializer implements ILootSerializer<ContentModeLootCondition> {
        @Override
        public void serialize(JsonObject json, ContentModeLootCondition value, JsonSerializationContext context) {
            json.addProperty("item", value.item);
        }

        @Override
        public ContentModeLootCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return new ContentModeLootCondition(JSONUtils.getAsString(json, "item"));
        }
    }
}
