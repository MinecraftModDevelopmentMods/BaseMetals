package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

/** Resource contract for the flattened Minecraft 1.13.2 data pack. */
class ResourceIntegrityTest {
    private static final Path MAIN = Paths.get("src", "main", "resources");
    private static final Path GENERATED = Paths.get("src", "generated", "resources");

    @Test
    void everyJsonResourceParses() throws Exception {
        int count = 0;
        for (Path root : Arrays.asList(MAIN, GENERATED)) {
            try (Stream<Path> paths = Files.walk(root)) {
                for (Path path : iterable(paths.filter(Files::isRegularFile)
                        .filter(file -> file.toString().endsWith(".json")))) {
                    assertNotNull(read(path), path.toString());
                    count++;
                }
            }
        }
        assertTrue(count > 3_000, "Expected the complete generated data set, found " + count);
    }

    @Test
    void manifestHasModelsAndTranslationsForEveryRegisteredId() throws Exception {
        JsonObject manifest = manifest();
        Set<String> blocks = strings(manifest.getAsJsonArray("blocks"));
        Set<String> items = strings(manifest.getAsJsonArray("items"));
        Set<String> fluids = strings(manifest.getAsJsonArray("fluids"));
        assertEquals("Base Metals 1.13.2 catalogue", manifest.get("source").getAsString());
        assertEquals(360, blocks.size());
        assertEquals(1115, items.size());
        assertEquals(72, fluids.size());
        assertEquals(2, manifest.getAsJsonArray("entities").size());
        assertTrue(manifest.getAsJsonArray("loot_modifier_serializers").size() == 0);

        JsonObject language = read(resource("assets/basemetals/lang/en_us.json")).getAsJsonObject();
        for (String block : blocks) {
            String id = path(block);
            assertResource("assets/basemetals/blockstates/" + id + ".json");
            assertTrue(language.has("block.basemetals." + id), "Missing block translation for " + block);
        }
        for (String item : items) {
            String id = path(item);
            assertResource("assets/basemetals/models/item/" + id + ".json");
            assertTrue(language.has("item.basemetals." + id), "Missing item translation for " + item);
        }
        assertFalse(Files.exists(GENERATED.resolve("data/basemetals/loot_tables/blocks")),
                "Minecraft 1.13 block drops are code-driven, not block loot tables");
    }

    @Test
    void registryManifestMatchesTheAudited112BaselinePlusExplicitNewIds() throws Exception {
        JsonObject legacy = read(resource("data/basemetals/registry_manifest_1_12.json")).getAsJsonObject();
        JsonObject current = manifest();
        assertEquals(2, legacy.get("format").getAsInt());
        assertEquals(6_851, legacy.get("block_state_count").getAsInt());
        assertEquals(354, legacy.getAsJsonArray("blocks").size());
        assertEquals(1_038, legacy.getAsJsonArray("items").size());
        assertEquals(33, legacy.getAsJsonArray("fluids").size());
        assertBaselinePlusAdditions(legacy, current, "blocks", "restored_blocks");
        assertBaselinePlusAdditions(legacy, current, "items", "restored_items", "new_items");
        assertBaselinePlusAdditions(legacy, current, "fluids", "restored_fluids", "new_fluids");
        assertEquals(strings(current.getAsJsonArray("entities")), strings(current.getAsJsonArray("new_entities")));
        assertEquals(strings(current.getAsJsonArray("recipe_serializers")),
                strings(current.getAsJsonArray("new_recipe_serializers")));
        assertTrue(current.getAsJsonArray("new_loot_modifier_serializers").size() == 0);
    }

    @Test
    void packaged112NumericMapCoversEveryCapturedLegacyBlock() throws Exception {
        JsonObject legacy = read(resource("data/basemetals/registry_manifest_1_12.json")).getAsJsonObject();
        JsonObject ids = read(MAIN.resolve("data/basemetals/migration/legacy_block_ids_1_12.json"))
                .getAsJsonObject().getAsJsonObject("ids");
        assertEquals(608, ids.size());
        assertEquals("basemetals:adamantine", ids.get("460").getAsString());
        Set<String> mapped = new LinkedHashSet<String>();
        for (Map.Entry<String, JsonElement> entry : ids.entrySet()) mapped.add(entry.getValue().getAsString());
        assertTrue(mapped.containsAll(strings(legacy.getAsJsonArray("blocks"))),
                "The pre-flattening map omits captured Base Metals block IDs");
    }

    @Test
    void localModelAndTextureReferencesResolve() throws Exception {
        JsonObject manifest = manifest();
        Deque<String> pending = new ArrayDeque<String>();
        for (String item : strings(manifest.getAsJsonArray("items"))) pending.add("item/" + path(item));
        for (String block : strings(manifest.getAsJsonArray("blocks"))) {
            visit(read(resource("assets/basemetals/blockstates/" + path(block) + ".json")), (key, value) -> {
                if (key.equals("model") && value.isJsonPrimitive()
                        && value.getAsString().startsWith("basemetals:")) {
                    pending.add(value.getAsString().substring("basemetals:".length()));
                }
            });
        }
        Set<String> visited = new LinkedHashSet<String>();
        while (!pending.isEmpty()) {
            String model = pending.removeFirst();
            if (!visited.add(model)) continue;
            Path modelPath = resource("assets/basemetals/models/" + model + ".json");
            assertTrue(Files.isRegularFile(modelPath), "Missing model basemetals:" + model);
            visit(read(modelPath), (key, value) -> {
                if (!(value.isJsonPrimitive() && value.getAsJsonPrimitive().isString())) return;
                String reference = value.getAsString();
                if (!reference.startsWith("basemetals:") || reference.contains("#")) return;
                String id = reference.substring("basemetals:".length());
                if (key.equals("parent") || key.equals("model")) pending.add(id);
                else if (key.startsWith("texture:")) assertResource("assets/basemetals/textures/" + id + ".png");
            });
        }
    }

    @Test
    void modelsUseFlattened113NamesAndWallStatesUseBooleanSides() throws Exception {
        Path modelRoot = MAIN.resolve("assets/basemetals/models");
        try (Stream<Path> paths = Files.walk(modelRoot)) {
            for (Path model : iterable(paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json")))) {
                String json = new String(Files.readAllBytes(model), StandardCharsets.UTF_8);
                assertFalse(json.contains("\"blocks/") || json.contains("\"items/"),
                        model + " uses a pre-flattening plural texture path");
            }
        }
        for (Path wall : files(GENERATED.resolve("assets/basemetals/blockstates"), "_wall.json")) {
            String json = new String(Files.readAllBytes(wall), StandardCharsets.UTF_8);
            assertTrue(json.contains("\"north\": \"true\""), wall.toString());
            assertFalse(json.contains("low") || json.contains("tall"), wall.toString());
        }
        assertFalse(Files.exists(MAIN.resolve("assets/minecraft/models/item/oak_door.json")));
    }

    @Test
    void fluidBucketModelsUseTheTargetNativeTintedLayers() throws Exception {
        Set<String> buckets = strings(manifest().getAsJsonArray("new_items"));
        assertEquals(36, buckets.size(), "Unexpected fluid bucket count");
        for (String bucket : buckets) {
            String id = path(bucket);
            JsonObject model = read(resource("assets/basemetals/models/item/" + id + ".json")).getAsJsonObject();
            assertEquals("item/generated", model.get("parent").getAsString(), bucket);
            JsonObject textures = model.getAsJsonObject("textures");
            assertEquals("minecraft:item/bucket", textures.get("layer0").getAsString(), bucket);
            assertEquals("basemetals:item/bucket_fluid", textures.get("layer1").getAsString(), bucket);
            assertEquals("basemetals:item/bucket_overlay", textures.get("layer2").getAsString(), bucket);
            assertFalse(model.has("loader"), bucket);
            assertFalse(model.has("fluid"), bucket);
        }
        assertTrue(Files.isRegularFile(resource("assets/basemetals/textures/item/bucket_fluid.png")));
    }

    @Test
    void transparentBlockTexturesRemainInKnownCutoutFamilies() throws Exception {
        Set<String> expected = new LinkedHashSet<String>();
        for (String block : strings(manifest().getAsJsonArray("blocks"))) {
            String id = path(block);
            if (id.endsWith("_bars")) expected.add(id + ".png");
            else if (id.endsWith("_door")) expected.add(id.substring(0, id.length() - 5) + "_door_upper.png");
            else if (id.endsWith("_trapdoor")) expected.add(id + ".png");
        }
        assertEquals(78, expected.size());
        for (String block : strings(manifest().getAsJsonArray("blocks"))) {
            String id = path(block);
            if (id.endsWith("_ore") && Files.isRegularFile(MAIN.resolve(
                    "assets/basemetals/textures/block/ore_overlays/" + id + ".png"))) {
                expected.add(id + ".png");
            }
        }
        Set<String> transparent = new LinkedHashSet<String>();
        for (Path texture : files(MAIN.resolve("assets/basemetals/textures/block"), ".png")) {
            if (hasTransparentPixel(texture)) transparent.add(texture.getFileName().toString());
        }
        Set<String> missedByJava8ImageIo = new LinkedHashSet<String>(expected);
        missedByJava8ImageIo.removeAll(transparent);
        assertEquals(new LinkedHashSet<String>(Arrays.asList("zinc_bars.png", "zinc_door_upper.png")),
                missedByJava8ImageIo,
                "Unexpected cutout textures lost transparency; the two indexed Zinc PNGs are rendered correctly "
                        + "but Java 8 ImageIO does not expose their tRNS alpha channel");
    }

    @Test
    void plateModelsUseTheLegacyDirectionToPlaneTransform() throws Exception {
        Map<String, int[]> rotations = new LinkedHashMap<String, int[]>();
        rotations.put("down", new int[] {90, 0});
        rotations.put("up", new int[] {270, 0});
        rotations.put("north", new int[] {0, 0});
        rotations.put("south", new int[] {0, 180});
        rotations.put("west", new int[] {0, 270});
        rotations.put("east", new int[] {0, 90});
        int plates = 0;
        for (String block : strings(manifest().getAsJsonArray("blocks"))) {
            String id = path(block);
            if (!id.endsWith("_plate") || id.endsWith("_pressure_plate")) continue;
            plates++;
            JsonObject variants = read(resource("assets/basemetals/blockstates/" + id + ".json"))
                    .getAsJsonObject().getAsJsonObject("variants");
            for (Map.Entry<String, int[]> expected : rotations.entrySet()) {
                JsonObject variant = variants.getAsJsonObject("facing=" + expected.getKey());
                assertNotNull(variant, id + " missing " + expected.getKey());
                assertEquals(expected.getValue()[0], variant.has("x") ? variant.get("x").getAsInt() : 0);
                assertEquals(expected.getValue()[1], variant.has("y") ? variant.get("y").getAsInt() : 0);
            }
        }
        assertEquals(23, plates);
    }

    @Test
    void tagsRecipesAndProviderReferenceExistingBaseMetalsContent() throws Exception {
        JsonObject manifest = manifest();
        Set<String> blocks = strings(manifest.getAsJsonArray("blocks"));
        Set<String> items = strings(manifest.getAsJsonArray("items"));
        Set<String> fluids = strings(manifest.getAsJsonArray("fluids"));
        validateTagTree(GENERATED.resolve("data"), "items", items);
        validateTagTree(GENERATED.resolve("data"), "blocks", blocks);
        validateTagTree(GENERATED.resolve("data"), "fluids", fluids);
        for (Path root : Arrays.asList(MAIN.resolve("data/basemetals"), GENERATED.resolve("data/basemetals"))) {
            for (Path file : files(root, ".json")) {
                visit(read(file), (key, value) -> {
                    if (!(value.isJsonPrimitive() && value.getAsJsonPrimitive().isString())) return;
                    String reference = value.getAsString();
                    if (!reference.startsWith("basemetals:")) return;
                    if (key.equals("item") || key.equals("name")) assertTrue(items.contains(reference), file + " -> " + reference);
                    else if (key.equals("fluid")) assertTrue(fluids.contains(reference), file + " -> " + reference);
                    else if (key.equals("block")) assertTrue(blocks.contains(reference), file + " -> " + reference);
                });
            }
        }
    }

    @Test
    void onlyTargetSupportedGenericCompatibilityDataIsPackaged() throws Exception {
        assertFalse(Files.exists(GENERATED.resolve("data/basemetals/recipes/compat")));
        assertFalse(Files.exists(GENERATED.resolve("data/mekanism")));
        assertFalse(Files.exists(GENERATED.resolve("data/thermal")));
        assertFalse(Files.exists(GENERATED.resolve("data/tconstruct")));
        assertFalse(Files.exists(GENERATED.resolve("data/enderio")));
        assertFalse(Files.exists(resource("data/basemetals/recipes/ancient_debris_crushing.json")));
        assertFalse(Files.exists(GENERATED.resolve("data/forge/loot_modifiers")));

        assertCrushingRecipe("iron_ore_crushing", "tag", "forge:ores/iron", "basemetals:iron_powder", 2);
        assertCrushingRecipe("coal_ore_crushing", "tag", "forge:ores/coal", "basemetals:coal_powder", 2);
        assertCrushingRecipe("tin_ingot_crushing", "tag", "forge:ingots/tin", "basemetals:tin_powder", 1);
        assertCrushingRecipe("tin_block_crushing", "tag", "forge:storage_blocks/tin", "basemetals:tin_powder", 9);

        JsonObject adamantAlias = read(resource("data/forge/tags/items/ingots/adamantium.json")).getAsJsonObject();
        assertTrue(strings(adamantAlias.getAsJsonArray("values")).contains("#forge:ingots/adamantine"));
        JsonObject sprocket = read(resource("data/forge/tags/items/sprockets/steel.json")).getAsJsonObject();
        assertTrue(strings(sprocket.getAsJsonArray("values")).contains("#forge:gears/steel"));
    }

    @Test
    void legacyFurnaceConversionsRemainComplete() throws Exception {
        assertCookingRecipe("iron_powder_smelting", "minecraft:smelting", "tag", "forge:dusts/iron", "minecraft:iron_ingot", 1);
        assertCookingRecipe("iron_smallpowder_smelting", "minecraft:smelting", "tag", "forge:tiny_dusts/iron", "minecraft:iron_nugget", 1);
        assertCookingRecipe("obsidian_powder_smelting", "minecraft:smelting", "tag", "forge:dusts/obsidian", "basemetals:obsidian_ingot", 1);
        assertCookingRecipe("tin_smallpowder_smelting", "minecraft:smelting", "tag", "forge:tiny_dusts/tin", "basemetals:tin_nugget", 1);
        assertCookingRecipe("bronze_smallblend_smelting", "minecraft:smelting", "tag", "forge:small_blends/bronze", "basemetals:bronze_nugget", 1);
        assertCookingRecipe("tin_crossbow_recycling", "basemetals:legacy_smelting", "item", "basemetals:tin_crossbow", "basemetals:tin_ingot", 4);
        assertCookingRecipe("tin_wall_recycling", "basemetals:legacy_smelting", "item", "basemetals:tin_wall", "basemetals:tin_block", 1);
        assertFalse(Files.exists(resource("data/basemetals/recipes/coal_powder_smelting.json")));
        assertTrue(strings(manifest().getAsJsonArray("recipe_serializers")).contains("basemetals:legacy_smelting"));
    }

    @Test
    void legacyCraftingYieldsAndAlternateRecipesRemainComplete() throws Exception {
        assertRecipeResult("tin_plate", "basemetals:tin_plate", 3);
        assertRecipeResult("tin_rod", "basemetals:tin_rod", 4);
        assertRecipeResult("tin_gear", "basemetals:tin_gear", 4);
        assertRecipeResult("tin_bars_2", "basemetals:tin_bars", 4);
        assertRecipePattern("tin_trapdoor", "XX", "XX");
        assertRecipePattern("tin_button", "X", "X");
        assertRecipeKeyTag("tin_button", "X", "forge:nuggets/tin");
        assertRecipeKeyTag("tin_pickaxe", "S", "forge:rods/wooden");
        assertRecipeKeyItem("copper_block", "X", "basemetals:copper_ingot");
        assertRecipeIngredientItem("copper_block_ingot", "basemetals:copper_block");
        assertRecipeResult("human_detector", "basemetals:human_detector", 1);
        assertRecipeResult("iron_bars_2", "minecraft:iron_bars", 4);
        assertFalse(Files.exists(resource("data/basemetals/recipes/stone_gear.json")));
    }

    @Test
    void everyHistoricalPlateRepairRecipeUsesTheTypedSerializer() throws Exception {
        long count = files(GENERATED.resolve("data/basemetals/recipes"), "_plate_repair.json").size();
        assertEquals(110, count);
        JsonObject recipe = read(resource("data/basemetals/recipes/adamantine_chestplate_plate_repair.json"))
                .getAsJsonObject();
        assertEquals("basemetals:plate_repair", recipe.get("type").getAsString());
        assertEquals("basemetals:adamantine_chestplate", recipe.get("target").getAsString());
        assertEquals("forge:plates/adamantine", recipe.getAsJsonObject("plate").get("tag").getAsString());
    }

    @Test
    void targetTagsContainNoPost113VanillaContent() throws Exception {
        Set<String> arrows = strings(read(resource("data/minecraft/tags/items/arrows.json"))
                .getAsJsonObject().getAsJsonArray("values"));
        assertTrue(arrows.contains("basemetals:tin_arrow"));
        assertFalse(arrows.contains("basemetals:tin_bolt"));

        Set<String> scythe = strings(read(resource("data/basemetals/tags/blocks/scythe_harvestable.json"))
                .getAsJsonObject().getAsJsonArray("values"));
        for (String post113 : Arrays.asList("minecraft:bamboo", "minecraft:sweet_berry_bush",
                "minecraft:warped_fungus", "minecraft:azalea", "minecraft:glow_lichen")) {
            assertFalse(scythe.contains(post113), post113);
        }
        Set<String> copperBlocks = strings(read(resource("data/forge/tags/blocks/ores/copper.json"))
                .getAsJsonObject().getAsJsonArray("values"));
        assertEquals(Collections.singleton("basemetals:copper_ore"), copperBlocks);
        assertFalse(Files.exists(GENERATED.resolve("data/minecraft/tags/blocks/mineable")));
        assertFalse(Files.exists(GENERATED.resolve("data/minecraft/tags/blocks/needs_stone_tool.json")));
    }

    @Test
    void advancementsRetainTheLegacyBehaviourTree() throws Exception {
        Map<String, String> parents = new LinkedHashMap<String, String>();
        parents.put("this_is_new", "minecraft:story/smelt_iron");
        parents.put("blocktastic", "basemetals:this_is_new");
        parents.put("geologist", "basemetals:this_is_new");
        parents.put("metallurgy", "basemetals:geologist");
        parents.put("angel_of_death", "basemetals:mithril_maker");
        parents.put("scuba_diver", "basemetals:aquarium_maker");
        parents.put("demon_slayer", "minecraft:story/enter_the_nether");
        parents.put("juggernaut", "minecraft:story/enter_the_nether");
        parents.put("moon_boots", "minecraft:end/root");
        Path advancements = GENERATED.resolve("data/basemetals/advancements");
        List<Path> gameplayAdvancements = files(advancements, ".json").stream()
                .filter(file -> file.getParent().equals(advancements)).collect(Collectors.toList());
        assertEquals(18, gameplayAdvancements.size());
        for (Path file : gameplayAdvancements) {
            JsonObject advancement = read(file).getAsJsonObject();
            assertEquals("minecraft:impossible", advancement.getAsJsonObject("criteria")
                    .getAsJsonObject("event").get("trigger").getAsString(), file.toString());
        }
        for (Map.Entry<String, String> entry : parents.entrySet()) {
            assertEquals(entry.getValue(), read(advancements.resolve(entry.getKey() + ".json"))
                    .getAsJsonObject().get("parent").getAsString(), entry.getKey());
        }
    }

    @Test
    void vanillaCrackhammerHeadsRetainTheirLegacyIngredients() throws Exception {
        JsonObject stone = read(resource("data/basemetals/recipes/stone_crackhammer.json")).getAsJsonObject();
        JsonObject wood = read(resource("data/basemetals/recipes/wood_crackhammer.json")).getAsJsonObject();
        assertEquals("minecraft:stone_bricks", stone.getAsJsonObject("key").getAsJsonObject("X").get("item").getAsString());
        assertEquals("minecraft:logs", wood.getAsJsonObject("key").getAsJsonObject("X").get("tag").getAsString());
    }

    @Test
    void chestInjectionTablesRetainEveryLegacyEntry() throws Exception {
        for (String name : Arrays.asList("abandoned_mineshaft", "desert_pyramid", "end_city_treasure",
                "jungle_temple", "nether_bridge", "simple_dungeon", "spawn_bonus_chest",
                "stronghold_corridor", "stronghold_crossing", "village_blacksmith")) {
            JsonObject legacy = read(Paths.get("reference", "1.12", "alt", "chests", name + ".json"))
                    .getAsJsonObject();
            JsonObject generated = read(resource("data/basemetals/loot_tables/chests/inject/" + name + ".json"))
                    .getAsJsonObject();
            assertEquals(legacy.getAsJsonArray("pools").size(), generated.getAsJsonArray("pools").size(), name);
            for (int pool = 0; pool < legacy.getAsJsonArray("pools").size(); pool++) {
                JsonArray expected = legacy.getAsJsonArray("pools").get(pool).getAsJsonObject().getAsJsonArray("entries");
                JsonArray actual = generated.getAsJsonArray("pools").get(pool).getAsJsonObject().getAsJsonArray("entries");
                assertEquals(itemNames(expected), itemNames(actual), name + " pool " + pool);
            }
        }
    }

    private static JsonObject manifest() throws IOException {
        return read(resource("data/basemetals/registry_manifest.json")).getAsJsonObject();
    }

    private static void assertBaselinePlusAdditions(JsonObject legacy, JsonObject current,
            String registry, String... additions) {
        Set<String> expected = new LinkedHashSet<String>(strings(legacy.getAsJsonArray(registry)));
        for (String addition : additions) expected.addAll(strings(current.getAsJsonArray(addition)));
        assertEquals(expected, strings(current.getAsJsonArray(registry)), registry);
    }

    private static Set<String> itemNames(JsonArray entries) {
        Set<String> result = new LinkedHashSet<String>();
        for (JsonElement value : entries) {
            JsonObject entry = value.getAsJsonObject();
            if (entry.has("name")) result.add(entry.get("name").getAsString());
        }
        return result;
    }

    private static void validateTagTree(Path dataRoot, String kind, Set<String> registry) throws Exception {
        for (Path file : files(dataRoot, ".json")) {
            if (!file.toString().replace('\\', '/').contains("/tags/" + kind + "/")) continue;
            JsonArray values = read(file).getAsJsonObject().getAsJsonArray("values");
            if (values == null) continue;
            for (JsonElement value : values) {
                String id = value.isJsonObject() ? value.getAsJsonObject().get("id").getAsString() : value.getAsString();
                if (id.startsWith("basemetals:")) assertTrue(registry.contains(id), file + " -> " + id);
            }
        }
    }

    private static void assertCrushingRecipe(String id, String kind, String ingredient, String result, int count)
            throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        assertEquals("basemetals:crushing", recipe.get("type").getAsString(), id);
        assertEquals(ingredient, recipe.getAsJsonObject("ingredient").get(kind).getAsString(), id);
        assertEquals(result, recipe.getAsJsonObject("result").get("item").getAsString(), id);
        JsonObject output = recipe.getAsJsonObject("result");
        assertEquals(count, output.has("count") ? output.get("count").getAsInt() : 1, id);
    }

    private static void assertCookingRecipe(String id, String type, String kind, String ingredient,
            String result, int count) throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        assertEquals(type, recipe.get("type").getAsString(), id);
        assertEquals(ingredient, recipe.getAsJsonObject("ingredient").get(kind).getAsString(), id);
        JsonElement output = recipe.get("result");
        assertEquals(result, output.isJsonObject() ? output.getAsJsonObject().get("item").getAsString()
                : output.getAsString(), id);
        assertEquals(count, output.isJsonObject() && output.getAsJsonObject().has("count")
                ? output.getAsJsonObject().get("count").getAsInt() : 1, id);
    }

    private static void assertRecipeResult(String id, String item, int count) throws Exception {
        JsonObject result = read(resource("data/basemetals/recipes/" + id + ".json"))
                .getAsJsonObject().getAsJsonObject("result");
        assertEquals(item, result.get("item").getAsString(), id);
        assertEquals(count, result.has("count") ? result.get("count").getAsInt() : 1, id);
    }

    private static void assertRecipeKeyTag(String id, String key, String tag) throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        assertEquals(tag, recipe.getAsJsonObject("key").getAsJsonObject(key).get("tag").getAsString(), id);
    }

    private static void assertRecipeKeyItem(String id, String key, String item) throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        assertEquals(item, recipe.getAsJsonObject("key").getAsJsonObject(key).get("item").getAsString(), id);
    }

    private static void assertRecipeIngredientItem(String id, String item) throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        assertEquals(item, recipe.getAsJsonArray("ingredients").get(0).getAsJsonObject().get("item").getAsString(), id);
    }

    private static void assertRecipePattern(String id, String... rows) throws Exception {
        JsonArray pattern = read(resource("data/basemetals/recipes/" + id + ".json"))
                .getAsJsonObject().getAsJsonArray("pattern");
        List<String> actual = new ArrayList<String>();
        for (JsonElement row : pattern) actual.add(row.getAsString());
        assertEquals(Arrays.asList(rows), actual, id);
    }

    private static void visit(JsonElement value, KeyValueConsumer consumer) {
        if (value.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                if (entry.getKey().equals("textures") && entry.getValue().isJsonObject()) {
                    for (Map.Entry<String, JsonElement> texture : entry.getValue().getAsJsonObject().entrySet()) {
                        consumer.accept("texture:" + texture.getKey(), texture.getValue());
                    }
                } else consumer.accept(entry.getKey(), entry.getValue());
                visit(entry.getValue(), consumer);
            }
        } else if (value.isJsonArray()) {
            for (JsonElement child : value.getAsJsonArray()) visit(child, consumer);
        }
    }

    private static List<Path> files(Path root, String suffix) throws IOException {
        if (!Files.isDirectory(root)) return Collections.emptyList();
        try (Stream<Path> paths = Files.walk(root)) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(suffix)).sorted().collect(Collectors.toList());
        }
    }

    private static Iterable<Path> iterable(Stream<Path> stream) {
        return stream::iterator;
    }

    private static Set<String> strings(JsonArray array) {
        Set<String> values = new LinkedHashSet<String>();
        for (JsonElement value : array) values.add(value.getAsString());
        return values;
    }

    private static String path(String id) {
        return id.substring(id.indexOf(':') + 1);
    }

    private static void assertResource(String relative) {
        assertTrue(Files.isRegularFile(resource(relative)), "Missing resource " + relative);
    }

    private static Path resource(String relative) {
        Path generated = GENERATED.resolve(relative);
        return Files.isRegularFile(generated) ? generated : MAIN.resolve(relative);
    }

    private static JsonElement read(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonElement value = new JsonParser().parse(reader);
            if (value.isJsonObject() && value.getAsJsonObject().has("recipe")) {
                return value.getAsJsonObject().get("recipe");
            }
            return value;
        }
    }

    private static boolean hasTransparentPixel(Path path) throws IOException {
        BufferedImage image = ImageIO.read(path.toFile());
        assertNotNull(image, "Unreadable PNG " + path);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if ((image.getRGB(x, y) >>> 24) < 255) return true;
            }
        }
        return false;
    }

    private static Set<String> difference(Set<String> expected, Set<String> actual) {
        Set<String> result = new LinkedHashSet<String>(expected);
        result.removeAll(actual);
        return result;
    }

    @FunctionalInterface
    private interface KeyValueConsumer {
        void accept(String key, JsonElement value);
    }
}
