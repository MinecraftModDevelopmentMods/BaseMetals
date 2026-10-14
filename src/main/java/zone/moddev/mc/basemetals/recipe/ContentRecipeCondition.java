package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Applies the selected mode to Base Metals outputs made by optional machines. */
public record ContentRecipeCondition(String item) implements ICondition {
    private static final ResourceLocation ID = new ResourceLocation("basemetals", "content_mode");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test() {
        return ContentPolicy.active().allows(item);
    }

    public static final class Serializer implements IConditionSerializer<ContentRecipeCondition> {
        @Override
        public ResourceLocation getID() {
            return ID;
        }

        @Override
        public void write(JsonObject json, ContentRecipeCondition condition) {
            json.addProperty("item", condition.item());
        }
        @Override
        public ContentRecipeCondition read(JsonObject json) {
            return new ContentRecipeCondition(GsonHelper.getAsString(json, "item"));
        }
    }
}
