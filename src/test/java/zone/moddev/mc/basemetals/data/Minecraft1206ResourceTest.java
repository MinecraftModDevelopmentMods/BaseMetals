package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class Minecraft1206ResourceTest {
    @Test
    void resourceAndDataPacksUseThe1206Formats() throws Exception {
        var pack = read("src/main/resources/pack.mcmeta").getAsJsonObject("pack");
        assertEquals(32, pack.get("pack_format").getAsInt());
        var formats = pack.getAsJsonObject("supported_formats");
        assertEquals(32, formats.get("min_inclusive").getAsInt());
        assertEquals(41, formats.get("max_inclusive").getAsInt());
    }

    @Test
    void scythesKeepTheirPlantAndCobwebHarvestingTag() throws Exception {
        var values = read("src/main/resources/data/basemetals/tags/blocks/scythe_harvestable.json")
                .getAsJsonArray("values");
        var blocks = new HashSet<String>();
        values.forEach(value -> blocks.add(value.getAsString()));

        for (String block : new String[] {"#minecraft:leaves", "#minecraft:flowers", "#minecraft:crops",
                "minecraft:cobweb", "minecraft:melon", "minecraft:pumpkin", "minecraft:moss_block"}) {
            assertTrue(blocks.contains(block), block);
        }

        assertFalse(blocks.contains("minecraft:stone"));
        assertFalse(Files.exists(Path.of(
                "src/generated/resources/data/basemetals/tags/blocks/scythe_harvestable.json")));
    }

    @Test
    void dormantTinkersStatsRetainTheirSourceFormat() throws Exception {
        var stats = read("src/generated/resources/data/basemetals/tinkering/materials/stats/steel.json")
                .getAsJsonObject("stats");
        assertFalse(stats.has("tconstruct:extra"));
        for (String type : new String[] {"head", "handle", "limb", "grip"}) {
            assertTrue(stats.has("tconstruct:" + type), type);
        }
    }

    @Test
    void enderIoUsesItsNativeRecipeAndOutputNames() throws Exception {
        var recipe = read("src/generated/resources/data/basemetals/recipes/compat/enderio/tin/ore_sag_milling.json");
        assertEquals("enderio:sag_milling", recipe.get("type").getAsString());

        var output = recipe.getAsJsonArray("outputs").get(0).getAsJsonObject();
        assertEquals(1.0F, output.get("chance").getAsFloat());
        assertFalse(output.get("optional").getAsBoolean());
        assertEquals("basemetals:tin_powder", output.getAsJsonObject("item").get("item").getAsString());
        assertEquals(2, output.getAsJsonObject("item").get("count").getAsInt());
    }

    private static JsonObject read(String path) throws Exception {
        return JsonParser.parseString(Files.readString(Path.of(path))).getAsJsonObject();
    }
}
