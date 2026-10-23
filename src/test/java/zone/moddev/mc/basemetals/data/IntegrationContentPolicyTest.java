package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class IntegrationContentPolicyTest {
    @Test
    void everyBundledMachineItemOutputHasAContentCondition() throws Exception {
        Path recipes = Path.of("src/generated/resources/data/basemetals/recipes/compat");
        int count = 0;
        try (var paths = Files.walk(recipes)) {
            for (Path file : paths.filter(path -> path.toString().endsWith(".json")).toList()) {
                var recipe = JsonParser.parseString(Files.readString(file)).getAsJsonObject();
                Set<String> outputs = new LinkedHashSet<>();
                for (String field : new String[] {"result", "output", "outputs"}) collect(recipe.get(field), outputs);
                Set<String> guarded = new LinkedHashSet<>();
                for (var condition : recipe.getAsJsonObject("forge:condition").getAsJsonArray("values")) {
                    var value = condition.getAsJsonObject();
                    if ("basemetals:content_mode".equals(value.get("type").getAsString())) {
                        guarded.add(value.get("item").getAsString());
                    }
                }
                outputs.removeIf(item -> !item.startsWith("basemetals:"));
                assertTrue(guarded.containsAll(outputs), file.toString());
                count++;
            }
        }
        assertEquals(734, count, "The existing optional recipe inventory must remain intact");
    }

    private static void collect(JsonElement value, Set<String> outputs) {
        if (value == null) return;
        if (value.isJsonArray()) value.getAsJsonArray().forEach(entry -> collect(entry, outputs));
        else if (value.isJsonObject() && value.getAsJsonObject().has("item")) {
            var item = value.getAsJsonObject().get("item");
            if (item.isJsonPrimitive()) outputs.add(item.getAsString());
            else collect(item, outputs);
        }
    }
}
