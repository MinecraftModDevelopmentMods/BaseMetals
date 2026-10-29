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

class Minecraft1211ResourceTest {
    @Test
    void resourceAndDataPacksUseThe262Formats() throws Exception {
        var pack = read("src/main/resources/pack.mcmeta").getAsJsonObject("pack");
        assertEquals("[107,1]", pack.get("min_format").toString());
        assertEquals("[107,1]", pack.get("max_format").toString());
        net.minecraft.SharedConstants.tryDetectVersion();
        var resources = net.minecraft.SharedConstants.getCurrentVersion()
                .packVersion(net.minecraft.server.packs.PackType.CLIENT_RESOURCES);
        assertEquals(88, resources.major());
        assertEquals(0, resources.minor());
    }

    @Test
    void dataRegistriesUseSingularDirectoryNames() throws Exception {
        for (String source : new String[] {"src/main/resources/data", "src/generated/resources/data"}) {
            try (var namespaces = Files.list(Path.of(source))) {
                for (Path namespace : namespaces.filter(Files::isDirectory).toList()) {
                    for (String oldName : new String[] {"recipes", "loot_tables", "advancements", "structures"}) {
                        assertFalse(Files.exists(namespace.resolve(oldName)), namespace + "/" + oldName);
                    }
                    for (String oldName : new String[] {"items", "blocks", "fluids", "entity_types"}) {
                        assertFalse(Files.exists(namespace.resolve("tags/" + oldName)), namespace + "/tags/" + oldName);
                    }
                }
            }
        }
    }

    @Test
    void silkTouchPredicatesUseTheEnchantmentSetField() throws Exception {
        var loot = read("src/generated/resources/data/basemetals/loot_table/blocks/adamantine_ore.json");
        var silk = loot.getAsJsonArray("pools").get(0).getAsJsonObject().getAsJsonArray("entries").get(0)
                .getAsJsonObject().getAsJsonArray("children").get(0).getAsJsonObject()
                .getAsJsonArray("conditions").get(0).getAsJsonObject().getAsJsonObject("predicate")
                .getAsJsonObject("predicates").getAsJsonArray("minecraft:enchantments").get(0).getAsJsonObject();

        assertEquals("minecraft:silk_touch", silk.get("enchantments").getAsString());
        assertFalse(silk.has("enchantment"));
        assertFalse(silk.has("predicates"));
    }

    @Test
    void scythesKeepTheirPlantAndCobwebHarvestingTag() throws Exception {
        var values = read("src/main/resources/data/basemetals/tags/block/scythe_harvestable.json")
                .getAsJsonArray("values");
        var blocks = new HashSet<String>();
        values.forEach(value -> blocks.add(value.getAsString()));

        for (String block : new String[] {"#minecraft:leaves", "#minecraft:flowers", "#minecraft:crops",
                "minecraft:cobweb", "minecraft:melon", "minecraft:pumpkin", "minecraft:moss_block"}) {
            assertTrue(blocks.contains(block), block);
        }

        assertFalse(blocks.contains("minecraft:stone"));
        assertFalse(Files.exists(Path.of(
                "src/generated/resources/data/basemetals/tags/block/scythe_harvestable.json")));
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
        var recipe = read("src/generated/resources/data/basemetals/recipe/compat/enderio/tin/ore_sag_milling.json");
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
