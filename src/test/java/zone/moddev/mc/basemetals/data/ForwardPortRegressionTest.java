package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

/** Recipes that were corrected while testing the earlier ports. */
class ForwardPortRegressionTest {
    @Test
    void doorsUseSixIngredientsAndRailsYieldSixteen() throws Exception {
        JsonObject door = recipe("diamond_door");
        assertEquals(3, door.getAsJsonArray("pattern").size());
        assertEquals(16, recipe("rail").getAsJsonObject("result").get("count").getAsInt());
    }

    @Test
    void obsidianNuggetsProduceAnIngotNotAWholeBlock() throws Exception {
        assertEquals("basemetals:obsidian_ingot",
                recipe("obsidian_ingot").getAsJsonObject("result").get("id").getAsString());
    }

    @Test
    void rawMetalsHaveOnePowderProcessing() throws Exception {
        for (String metal : java.util.List.of("adamantine", "antimony", "bismuth", "coldiron", "lead",
                "mercury", "nickel", "platinum", "silver", "starsteel", "tin", "zinc")) {
            JsonObject result = recipe(metal + "_raw_crushing").getAsJsonObject("result");
            assertEquals(1, result.has("count") ? result.get("count").getAsInt() : 1);
        }
    }

    private static JsonObject recipe(String name) throws Exception {
        JsonObject json = JsonParser.parseString(Files.readString(Path.of(
                "src/generated/resources/data/basemetals/recipes/" + name + ".json"))).getAsJsonObject();
        return json.has("recipe") ? json.getAsJsonObject("recipe") : json;
    }
}
