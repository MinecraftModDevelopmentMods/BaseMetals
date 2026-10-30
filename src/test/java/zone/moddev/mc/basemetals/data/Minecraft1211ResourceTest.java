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
    void resourceAndDataPacksUseThe263Formats() throws Exception {
        var pack = read("src/main/resources/pack.mcmeta").getAsJsonObject("pack");
        assertEquals("[121,0]", pack.get("min_format").toString());
        assertEquals("[121,0]", pack.get("max_format").toString());
        net.minecraft.SharedConstants.tryDetectVersion();
        var resources = net.minecraft.SharedConstants.getCurrentVersion()
                .packVersion(net.minecraft.server.packs.PackType.CLIENT_RESOURCES);
        assertEquals(97, resources.major());
        assertEquals(1, resources.minor());
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
                .getAsJsonObject("condition").getAsJsonObject("predicate")
                .getAsJsonObject("predicates").getAsJsonArray("minecraft:enchantments").get(0).getAsJsonObject();

        assertEquals("minecraft:silk_touch", silk.get("enchantments").getAsString());
        assertFalse(silk.has("enchantment"));
        assertFalse(silk.has("predicates"));
    }

    @Test
    void lootAndTradeDispatchKeysUseTheNativeTypeField() throws Exception {
        for (String directory : new String[] {"loot_table", "loot_modifiers", "villager_trade"}) {
            Path root = Path.of("src/generated/resources/data/basemetals", directory);

            try (var paths = Files.walk(root)) {
                for (Path path : paths.filter(file -> file.toString().endsWith(".json")).toList()) {
                    String json = Files.readString(path);
                    assertFalse(json.contains("\"condition\": \""), path.toString());
                    assertFalse(json.contains("\"function\":"), path.toString());
                    assertFalse(json.contains("\"functions\":"), path.toString());
                    if (directory.equals("loot_table")) {
                        assertFalse(json.contains("\"conditions\":"), path.toString());
                    }
                }
            }
        }

        var predicate = read("src/generated/resources/data/basemetals/villager_trade/weaponsmith/steel_sword.json")
                .getAsJsonObject("merchant_predicate");
        assertEquals("basemetals:trade_enabled", predicate.get("type").getAsString());
        var enchanted = read("src/generated/resources/data/basemetals/villager_trade/armorer/brass_helmet_enchanted.json");
        assertTrue(enchanted.has("given_item_modifier"));
        assertFalse(enchanted.has("given_item_modifiers"));

        var door = read("src/generated/resources/data/basemetals/loot_table/blocks/steel_door.json")
                .getAsJsonArray("pools").get(0).getAsJsonObject().getAsJsonObject("condition")
                .getAsJsonArray("terms").get(1).getAsJsonObject();
        assertEquals("minecraft:match_block", door.get("type").getAsString());
        assertEquals("basemetals:steel_door", door.get("blocks").getAsString());
        assertEquals("lower", door.getAsJsonObject("state").get("half").getAsString());

        var slab = read("src/generated/resources/data/basemetals/loot_table/blocks/steel_slab.json")
                .getAsJsonArray("pools").get(0).getAsJsonObject().getAsJsonArray("entries").get(0)
                .getAsJsonObject().getAsJsonArray("modifier").get(0).getAsJsonObject()
                .getAsJsonObject("condition");
        assertEquals("double", slab.getAsJsonObject("state").get("type").getAsString());
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
