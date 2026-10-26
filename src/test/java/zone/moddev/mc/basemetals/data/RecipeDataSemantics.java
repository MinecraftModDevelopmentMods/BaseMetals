package zone.moddev.mc.basemetals.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/** Lets historical quantity checks compare ingredients independently of their JSON spelling. */
final class RecipeDataSemantics {
    private RecipeDataSemantics() {}

    static JsonElement normalize(JsonElement value) {
        if (!value.isJsonObject()) return value;
        JsonObject recipe = value.getAsJsonObject();
        if (recipe.has("recipe")) normalize(recipe.get("recipe"));

        for (String field : new String[] {"ingredient", "ingredients", "plate"}) {
            if (recipe.has(field)) recipe.add(field, ingredient(recipe.get(field)));
        }
        if (recipe.has("key") && recipe.get("key").isJsonObject()) {
            JsonObject key = recipe.getAsJsonObject("key");
            for (String symbol : new java.util.ArrayList<>(key.keySet())) {
                key.add(symbol, ingredient(key.get(symbol)));
            }
        }

        return value;
    }

    private static JsonElement ingredient(JsonElement value) {
        if (value.isJsonPrimitive()) {
            String id = value.getAsString();
            JsonObject result = new JsonObject();
            result.addProperty(id.startsWith("#") ? "tag" : "item", id.startsWith("#") ? id.substring(1) : id);
            return result;
        }
        if (value.isJsonArray()) {
            JsonArray result = new JsonArray();
            value.getAsJsonArray().forEach(entry -> result.add(ingredient(entry)));
            return result;
        }

        return value;
    }
}
