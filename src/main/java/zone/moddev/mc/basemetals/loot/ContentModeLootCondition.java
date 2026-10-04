package zone.moddev.mc.basemetals.loot;

import java.util.Random;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Filters only the auxiliary chest entries supplied by Base Metals. */
public final class ContentModeLootCondition implements LootCondition {
    private final String item;

    public ContentModeLootCondition(String item) {
        this.item = item;
    }

    @Override
    public boolean testCondition(Random random, LootContext context) {
        return ContentPolicy.active().allows(item);
    }

    public static final class Serializer extends LootCondition.Serializer<ContentModeLootCondition> {
        public Serializer() {
            super(new ResourceLocation("basemetals", "content_mode"), ContentModeLootCondition.class);
        }

        @Override
        public void serialize(JsonObject json, ContentModeLootCondition value, JsonSerializationContext context) {
            json.addProperty("item", value.item);
        }

        @Override
        public ContentModeLootCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return new ContentModeLootCondition(JsonUtils.getString(json, "item"));
        }
    }
}
