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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class ResourceIntegrityTest {
    private static final Path MAIN = Path.of("src/main/resources");
    private static final Path GENERATED = Path.of("src/generated/resources");

    @Test
    void everyJsonResourceParses() throws Exception {
        int count = 0;
        for (Path root : List.of(MAIN, GENERATED)) {
            try (Stream<Path> paths = Files.walk(root)) {
                for (Path path : (Iterable<Path>) paths.filter(Files::isRegularFile)
                        .filter(file -> file.toString().endsWith(".json"))::iterator) {
                    assertNotNull(read(path), path.toString());
                    count++;
                }
            }
        }
        assertTrue(count > 2_000, "Expected the complete generated data set, found " + count);
    }

    @Test
    void manifestHasModelsLootAndTranslationsForEveryRegisteredId() throws Exception {
        JsonObject manifest = read(resource("data/basemetals/registry_manifest.json")).getAsJsonObject();
        Set<String> blocks = strings(manifest.getAsJsonArray("blocks"));
        Set<String> items = strings(manifest.getAsJsonArray("items"));
        Set<String> fluids = strings(manifest.getAsJsonArray("fluids"));
        assertEquals(360, blocks.size());
        assertEquals(1127, items.size());
        assertEquals(72, fluids.size());
        assertEquals(2, manifest.getAsJsonArray("entities").size());

        JsonObject language = read(resource("assets/basemetals/lang/en_us.json")).getAsJsonObject();
        for (String block : blocks) {
            String id = path(block);
            assertResource("assets/basemetals/blockstates/" + id + ".json");
            assertTrue(language.has("block.basemetals." + id), "Missing block translation for " + block);
            if (!fluids.contains(block)) {
                assertResource("data/basemetals/loot_tables/blocks/" + id + ".json");
            }
        }
        for (String item : items) {
            String id = path(item);
            assertResource("assets/basemetals/models/item/" + id + ".json");
            assertTrue(language.has("item.basemetals." + id), "Missing item translation for " + item);
        }
    }

    @Test
    void registryManifestMatchesTheAudited112BaselinePlusExplicitNewIds() throws Exception {
        JsonObject legacy = read(resource("data/basemetals/registry_manifest_1_12.json")).getAsJsonObject();
        JsonObject current = read(resource("data/basemetals/registry_manifest.json")).getAsJsonObject();

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
        assertEquals(strings(current.getAsJsonArray("loot_modifier_serializers")),
                strings(current.getAsJsonArray("new_loot_modifier_serializers")));
    }

    @Test
    void packaged112NumericMapCoversEveryCapturedLegacyBlock() throws Exception {
        JsonObject legacy = read(resource("data/basemetals/registry_manifest_1_12.json")).getAsJsonObject();
        JsonObject numericMap = read(MAIN.resolve("data/basemetals/migration/legacy_block_ids_1_12.json"))
                .getAsJsonObject();
        JsonObject ids = numericMap.getAsJsonObject("ids");
        assertEquals(608, ids.size());
        assertEquals("basemetals:adamantine", ids.get("460").getAsString());
        Set<String> mapped = new LinkedHashSet<>();
        ids.entrySet().forEach(entry -> mapped.add(entry.getValue().getAsString()));
        assertTrue(mapped.containsAll(strings(legacy.getAsJsonArray("blocks"))),
                "The pre-flattening map omits captured Base Metals block IDs");
    }

    @Test
    void localModelAndTextureReferencesResolve() throws Exception {
        JsonObject manifest = read(resource("data/basemetals/registry_manifest.json")).getAsJsonObject();
        Deque<String> pending = new ArrayDeque<>();
        strings(manifest.getAsJsonArray("items")).forEach(item -> pending.add("item/" + path(item)));
        for (String block : strings(manifest.getAsJsonArray("blocks"))) {
            JsonElement state = read(resource("assets/basemetals/blockstates/" + path(block) + ".json"));
            visit(state, (key, value) -> {
                if (key.equals("model") && value.isJsonPrimitive()
                        && value.getAsString().startsWith("basemetals:")) {
                    pending.add(value.getAsString().substring("basemetals:".length()));
                }
            });
        }
        Set<String> visited = new LinkedHashSet<>();
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
                if (key.equals("parent") || key.equals("model")) {
                    pending.add(id);
                } else if (key.startsWith("texture:")) {
                    assertResource("assets/basemetals/textures/" + id + ".png");
                }
            });
        }
    }

    @Test
    void modelResourcesUseFlattened118Names() throws Exception {
        Path modelRoot = MAIN.resolve("assets/basemetals/models");
        try (Stream<Path> paths = Files.walk(modelRoot)) {
            for (Path model : (Iterable<Path>) paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))::iterator) {
                String json = Files.readString(model, StandardCharsets.UTF_8);
                assertTrue(!json.contains("\"blocks/") && !json.contains("\"items/"),
                        model + " uses a pre-flattening plural texture path");
                for (String obsolete : List.of("block/half_slab", "block/upper_slab", "block/wall_post",
                        "block/wall_side", "block/trapdoor_bottom", "block/trapdoor_top", "block/trapdoor_open")) {
                    assertTrue(!json.contains("\"parent\": \"" + obsolete + "\""),
                            model + " uses obsolete parent " + obsolete);
                }
            }
        }
        assertTrue(!Files.exists(MAIN.resolve("assets/minecraft/models/item/oak_door.json")),
                "Base Metals must not replace vanilla door models");
    }

    @Test
    void fluidBucketModelsUseTheForgeBucketTemplate() throws Exception {
        JsonObject manifest = read(resource("data/basemetals/registry_manifest.json")).getAsJsonObject();
        Set<String> buckets = new LinkedHashSet<>(strings(manifest.getAsJsonArray("new_items")).stream()
                .filter(id -> id.endsWith("_bucket")).toList());
        assertEquals(36, buckets.size(), "Unexpected fluid bucket count");
        for (String bucket : buckets) {
            String id = path(bucket);
            JsonObject model = read(resource("assets/basemetals/models/item/" + id + ".json")).getAsJsonObject();
            assertEquals("forge:item/bucket", model.get("parent").getAsString(),
                    bucket + " has no visible bucket base or fluid mask");
            assertEquals("forge:fluid_container", model.get("loader").getAsString(),
                    bucket + " does not use Forge's dynamic bucket loader");
            assertEquals("basemetals:" + id.substring(0, id.length() - "_bucket".length()),
                    model.get("fluid").getAsString(), bucket + " references the wrong fluid");
        }
    }

    @Test
    void everyTransparentBlockTextureHasAnExplicitRenderLayerFamily() throws Exception {
        JsonObject manifest = read(resource("data/basemetals/registry_manifest.json")).getAsJsonObject();
        Set<String> expectedCutouts = new LinkedHashSet<>();
        for (String block : strings(manifest.getAsJsonArray("blocks"))) {
            String id = path(block);
            if (id.endsWith("_bars")) {
                expectedCutouts.add(id + ".png");
            } else if (id.endsWith("_door")) {
                expectedCutouts.add(id.substring(0, id.length() - "_door".length())
                        + "_door_upper.png");
            } else if (id.endsWith("_trapdoor")) {
                expectedCutouts.add(id + ".png");
            }
        }
        assertEquals(78, expectedCutouts.size());

        Path textures = MAIN.resolve("assets/basemetals/textures/block");
        Set<String> transparent = new LinkedHashSet<>();
        try (Stream<Path> paths = Files.walk(textures)) {
            for (Path texture : (Iterable<Path>) paths.filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".png"))::iterator) {
                if (hasTransparentPixel(texture)) transparent.add(texture.getFileName().toString());
            }
        }
        assertTrue(transparent.containsAll(expectedCutouts),
                "Cutout block textures lost their transparent pixels: "
                        + difference(expectedCutouts, transparent));

        Set<String> exemptTextureSheetSpace = Set.of(
                "adamantine_anvil_top_damaged_0.png",
                "adamantine_anvil_top_damaged_1.png",
                "adamantine_anvil_top_damaged_2.png",
                "steel_anvil_top_damaged_0.png",
                "steel_anvil_top_damaged_1.png",
                "steel_anvil_top_damaged_2.png",
                "stone_anvil_top_damaged_0.png",
                "stone_anvil_top_damaged_1.png",
                "stone_anvil_top_damaged_2.png",
                "door_iron_upper.png");
        Set<String> unexplained = new LinkedHashSet<>(transparent);
        unexplained.removeAll(expectedCutouts);
        unexplained.removeAll(exemptTextureSheetSpace);
        unexplained.removeIf(name -> name.endsWith("_ore.png"));
        assertTrue(unexplained.isEmpty(),
                "Transparent block textures are outside a registered cutout family: " + unexplained);
        assertEquals(103, transparent.size());
    }

    @Test
    void plateModelsUseTheLegacyDirectionToPlaneTransform() throws Exception {
        JsonObject manifest = read(resource("data/basemetals/registry_manifest.json")).getAsJsonObject();
        Map<String, int[]> rotations = Map.of(
                "down", new int[] {90, 0},
                "up", new int[] {270, 0},
                "north", new int[] {0, 0},
                "south", new int[] {0, 180},
                "west", new int[] {0, 270},
                "east", new int[] {0, 90});
        int plates = 0;
        for (String block : strings(manifest.getAsJsonArray("blocks"))) {
            String id = path(block);
            if (!id.endsWith("_plate") || id.endsWith("_pressure_plate")) continue;
            plates++;
            JsonObject variants = read(resource("assets/basemetals/blockstates/" + id + ".json"))
                    .getAsJsonObject().getAsJsonObject("variants");
            for (Map.Entry<String, int[]> expected : rotations.entrySet()) {
                JsonObject variant = variants.getAsJsonObject("facing=" + expected.getKey());
                assertNotNull(variant, id + " is missing facing=" + expected.getKey());
                int x = variant.has("x") ? variant.get("x").getAsInt() : 0;
                int y = variant.has("y") ? variant.get("y").getAsInt() : 0;
                assertEquals(expected.getValue()[0], x,
                        id + " facing=" + expected.getKey() + " has the wrong X rotation");
                assertEquals(expected.getValue()[1], y,
                        id + " facing=" + expected.getKey() + " has the wrong Y rotation");
            }
        }
        assertEquals(23, plates, "Plate transform coverage no longer matches the registered family");
    }

    @Test
    void tagsRecipesLootAndProviderReferenceExistingBaseMetalsContent() throws Exception {
        JsonObject manifest = read(resource("data/basemetals/registry_manifest.json")).getAsJsonObject();
        Set<String> blocks = strings(manifest.getAsJsonArray("blocks"));
        Set<String> items = strings(manifest.getAsJsonArray("items"));
        Set<String> fluids = strings(manifest.getAsJsonArray("fluids"));

        validateTagTree(GENERATED.resolve("data"), "items", items);
        validateTagTree(GENERATED.resolve("data"), "blocks", blocks);
        validateTagTree(GENERATED.resolve("data"), "fluids", fluids);

        for (Path root : List.of(MAIN.resolve("data/basemetals"), GENERATED.resolve("data/basemetals"))) {
            if (!Files.isDirectory(root)) continue;
            try (Stream<Path> paths = Files.walk(root)) {
                for (Path file : (Iterable<Path>) paths.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".json"))::iterator) {
                    visit(read(file), (key, value) -> {
                        if (!(value.isJsonPrimitive() && value.getAsJsonPrimitive().isString())) return;
                        String reference = value.getAsString();
                        if (!reference.startsWith("basemetals:")) return;
                        if (key.equals("item") || key.equals("name")) {
                            assertTrue(items.contains(reference), file + " references missing item " + reference);
                        } else if (key.equals("fluid")) {
                            assertTrue(fluids.contains(reference), file + " references missing fluid " + reference);
                        } else if (key.equals("block")) {
                            assertTrue(blocks.contains(reference), file + " references missing block " + reference);
                        }
                    });
                }
            }
        }
    }

    @Test
    void crushingAliasesAndOptionalProcessingContractsAreGenerated() throws Exception {
        JsonObject crushing = read(resource("data/basemetals/recipes/copper_ore_crushing.json")).getAsJsonObject();
        assertEquals("basemetals:crushing", crushing.get("type").getAsString());
        assertEquals("forge:ores/copper", crushing.getAsJsonObject("ingredient").get("tag").getAsString());
        assertEquals(2, crushing.getAsJsonObject("result").get("count").getAsInt());

        assertCrushingRecipe("iron_ore_crushing", "tag", "forge:ores/iron",
                "basemetals:iron_powder", 2);
        assertCrushingRecipe("coal_ore_crushing", "tag", "forge:ores/coal",
                "basemetals:coal_powder", 2);
        assertCrushingRecipe("ancient_debris_crushing", "tag", "forge:ores/netherite_scrap",
                "minecraft:netherite_scrap", 2);
        assertCrushingRecipe("tin_ingot_crushing", "tag", "forge:ingots/tin",
                "basemetals:tin_powder", 1);
        assertCrushingRecipe("tin_block_crushing", "tag", "forge:storage_blocks/tin",
                "basemetals:tin_powder", 9);
        assertCrushingRecipe("tin_nugget_crushing", "tag", "forge:nuggets/tin",
                "basemetals:tin_smallpowder", 1);

        JsonObject adamantAlias = read(resource("data/forge/tags/items/ingots/adamantium.json")).getAsJsonObject();
        assertTrue(strings(adamantAlias.getAsJsonArray("values")).contains("#forge:ingots/adamantine"));
        JsonObject sprocket = read(resource("data/forge/tags/items/sprockets/steel.json")).getAsJsonObject();
        assertTrue(strings(sprocket.getAsJsonArray("values")).contains("#forge:gears/steel"));

        assertConditionalRecipe("compat/mekanism/adamantine/ore_to_dirty_slurry", "mekanism");
        assertConditionalRecipe("compat/thermal/alloys/bronze", "thermal_expansion");
        assertConditionalRecipe("compat/tconstruct/alloys/bronze", "tconstruct");
        assertConditionalRecipe("compat/enderio/alloys/bronze", "enderio_machines");
    }

    @Test
    void legacyFurnaceConversionsAreGeneratedForEveryMaterialFamily() throws Exception {
        assertCookingRecipe("iron_powder_smelting", "minecraft:smelting",
                "tag", "forge:dusts/iron", "minecraft:iron_ingot", 1);
        assertCookingRecipe("iron_smallpowder_smelting", "minecraft:smelting",
                "tag", "forge:tiny_dusts/iron", "minecraft:iron_nugget", 1);
        assertCookingRecipe("obsidian_powder_smelting", "minecraft:smelting",
                "tag", "forge:dusts/obsidian", "basemetals:obsidian_ingot", 1);
        assertCookingRecipe("tin_smallpowder_smelting", "minecraft:smelting",
                "tag", "forge:tiny_dusts/tin", "basemetals:tin_nugget", 1);
        assertCookingRecipe("bronze_smallblend_smelting", "minecraft:smelting",
                "tag", "forge:small_blends/bronze", "basemetals:bronze_nugget", 1);

        assertCookingRecipe("tin_crossbow_recycling", "basemetals:legacy_smelting",
                "item", "basemetals:tin_crossbow", "basemetals:tin_ingot", 4);
        assertCookingRecipe("tin_wall_recycling", "basemetals:legacy_smelting",
                "item", "basemetals:tin_wall", "basemetals:tin_block", 1);
        assertCookingRecipe("iron_crackhammer_recycling", "basemetals:legacy_smelting",
                "item", "basemetals:iron_crackhammer", "minecraft:iron_block", 1);

        assertFalse(Files.exists(resource("data/basemetals/recipes/coal_powder_smelting.json")),
                "Coal powder must remain a fuel rather than smelting back into coal");
        assertFalse(Files.exists(resource("data/basemetals/recipes/charcoal_powder_smelting.json")),
                "Charcoal powder must remain a fuel rather than smelting back into charcoal");

        JsonObject manifest = read(resource("data/basemetals/registry_manifest.json")).getAsJsonObject();
        assertTrue(strings(manifest.getAsJsonArray("recipe_serializers"))
                .contains("basemetals:legacy_smelting"));
    }

    @Test
    void legacyCraftingYieldsIngredientsAndAlternateRecipesAreRestored() throws Exception {
        assertRecipeResult("tin_plate", "basemetals:tin_plate", 3);
        assertRecipeResult("tin_rod", "basemetals:tin_rod", 4);
        assertRecipeResult("tin_gear", "basemetals:tin_gear", 4);
        assertRecipeResult("tin_bars_2", "basemetals:tin_bars", 4);
        assertRecipeResult("tin_trapdoor", "basemetals:tin_trapdoor", 1);
        assertRecipeResult("diamond_trapdoor", "basemetals:diamond_trapdoor", 1);

        assertRecipePattern("tin_trapdoor", "XX", "XX");
        assertRecipePattern("tin_button", "X", "X");
        assertRecipeKeyTag("tin_button", "X", "forge:nuggets/tin");
        assertRecipePattern("diamond_door", "XX", "XX", "XX");
        assertRecipePattern("diamond_bolt", "R", "R", "F");
        assertRecipePattern("emerald_sword", "X", "X", "S");
        assertRecipeKeyTag("tin_bars_2", "X", "forge:rods/tin");
        assertRecipeKeyTag("tin_slab", "X", "forge:storage_blocks/tin");
        assertRecipeKeyTag("tin_stairs", "X", "forge:storage_blocks/tin");
        assertRecipeKeyTag("tin_wall", "X", "forge:storage_blocks/tin");
        assertRecipeKeyTag("tin_pickaxe", "S", "forge:rods/wooden");
        assertRecipeKeyTag("diamond_scythe", "X", "forge:gems/diamond");
        assertRecipeKeyTag("obsidian_scythe", "X", "forge:storage_blocks/obsidian");
        assertRecipeKeyTag("emerald_gear", "R", "forge:rods/iron");
        assertRecipeKeyTag("stone_scythe", "X", "forge:stone");
        assertRecipeKeyTag("copper_pickaxe", "X", "forge:ingots/copper");
        assertRecipeKeyItem("copper_block", "X", "basemetals:copper_ingot");
        assertRecipeIngredientItem("copper_block_ingot", "basemetals:copper_block");
        assertRecipeIngredientTag("tin_block_ingot", "forge:storage_blocks/tin");
        assertRecipeIngredientItem("copper_nuggets", "basemetals:copper_ingot");
        assertRecipeKeyItem("copper_ingot_from_nuggets", "X", "basemetals:copper_nugget");
        assertRecipeResult("iron_plate", "basemetals:iron_plate", 3);
        assertRecipeResult("gold_plate", "basemetals:gold_plate", 3);

        assertRecipeResult("activator_rail", "minecraft:activator_rail", 1);
        assertRecipeResult("human_detector", "basemetals:human_detector", 1);
        assertRecipeResult("charcoal_block_dust", "basemetals:charcoal_block", 1);
        assertRecipeResult("coal_nugget", "basemetals:coal_nugget", 9);
        assertRecipeResult("iron_bars_2", "minecraft:iron_bars", 4);
        assertRecipeKeyTag("iron_bars_2", "X", "forge:rods/iron");
        assertRecipeResult("obsidian_block", "minecraft:obsidian", 1);
        assertRecipeResult("quartz_nugget", "basemetals:quartz_nugget", 9);
        assertRecipeIngredientTag("quartz_nugget", "forge:gems/quartz");
        assertRecipeResult("redstone_smallpowder", "basemetals:redstone_smallpowder", 9);
        assertFalse(Files.exists(resource("data/basemetals/recipes/stone_gear.json")));
        assertFalse(Files.exists(resource("data/basemetals/recipes/wood_gear.json")));
        assertFalse(Files.exists(resource("data/basemetals/recipes/lapis_from_smallpowder.json")));
    }

    @Test
    void everyHistoricalPlateRepairRecipeUsesTheTypedSerializer() throws Exception {
        Path recipes = GENERATED.resolve("data/basemetals/recipes");
        long count;
        try (Stream<Path> paths = Files.list(recipes)) {
            count = paths.filter(path -> path.getFileName().toString().endsWith("_plate_repair.json")).count();
        }
        assertEquals(110, count);
        JsonObject recipe = read(recipes.resolve("adamantine_chestplate_plate_repair.json")).getAsJsonObject();
        assertEquals("basemetals:plate_repair", recipe.get("type").getAsString());
        assertEquals("basemetals:adamantine_chestplate", recipe.get("target").getAsString());
        assertEquals("forge:plates/adamantine",
                recipe.getAsJsonObject("plate").get("tag").getAsString());
        JsonObject vanilla = read(recipes.resolve("iron_chestplate_plate_repair.json")).getAsJsonObject();
        assertEquals("minecraft:iron_chestplate", vanilla.get("target").getAsString());

        JsonObject manifest = read(resource("data/basemetals/registry_manifest.json")).getAsJsonObject();
        assertTrue(strings(manifest.getAsJsonArray("recipe_serializers"))
                .contains("basemetals:plate_repair"));
    }

    @Test
    void harvestAmmoAndWorldgenTagsCoverTheRestoredRuntimeContracts() throws Exception {
        Set<String> stone = strings(read(resource("data/minecraft/tags/blocks/needs_stone_tool.json"))
                .getAsJsonObject().getAsJsonArray("values"));
        Set<String> iron = strings(read(resource("data/minecraft/tags/blocks/needs_iron_tool.json"))
                .getAsJsonObject().getAsJsonArray("values"));
        Set<String> diamond = strings(read(resource("data/minecraft/tags/blocks/needs_diamond_tool.json"))
                .getAsJsonObject().getAsJsonArray("values"));
        assertTrue(stone.contains("basemetals:copper_ore"));
        assertTrue(iron.contains("basemetals:bronze_block"));
        assertTrue(diamond.contains("basemetals:adamantine_ore"));

        Set<String> beaconBases = strings(read(resource("data/minecraft/tags/blocks/beacon_base_blocks.json"))
                .getAsJsonObject().getAsJsonArray("values"));
        assertEquals(22, beaconBases.size());
        assertTrue(beaconBases.contains("basemetals:adamantine_block"));
        assertTrue(beaconBases.contains("basemetals:charcoal_block"));
        assertFalse(beaconBases.contains("basemetals:mercury"));

        Set<String> arrows = strings(read(resource("data/minecraft/tags/items/arrows.json"))
                .getAsJsonObject().getAsJsonArray("values"));
        assertTrue(arrows.contains("basemetals:tin_arrow"));
        assertFalse(arrows.contains("basemetals:tin_bolt"));

        JsonObject provider = read(MAIN.resolve("data/basemetals/orespawn/provider.json")).getAsJsonObject();
        JsonObject ores = provider.getAsJsonObject("ores");
        assertTrue(strings(ores.getAsJsonObject("basemetals:ore/coldiron")
                .getAsJsonObject("dimensions").getAsJsonObject("minecraft:the_nether")
                .getAsJsonArray("host_blocks")).contains("minecraft:netherrack"));
        assertTrue(strings(ores.getAsJsonObject("basemetals:ore/starsteel")
                .getAsJsonObject("dimensions").getAsJsonObject("minecraft:the_end")
                .getAsJsonArray("host_blocks")).contains("minecraft:end_stone"));
    }

    @Test
    void generatedHarvestTierTagsHaveDeterministicOrdering() throws Exception {
        for (String tier : List.of("stone", "iron", "diamond")) {
            JsonArray values = read(resource("data/minecraft/tags/blocks/needs_" + tier + "_tool.json"))
                    .getAsJsonObject().getAsJsonArray("values");
            List<String> actual = new ArrayList<>();
            values.forEach(value -> actual.add(value.getAsString()));
            List<String> sorted = actual.stream().sorted().toList();
            assertEquals(sorted, actual, tier + " harvest tier tag must be generated deterministically");
        }
    }

    @Test
    void advancementsUseTheLegacyBehaviourTreeAndServerAwardCriterion() throws Exception {
        Map<String, String> parents = Map.ofEntries(
                Map.entry("this_is_new", "minecraft:story/smelt_iron"),
                Map.entry("blocktastic", "basemetals:this_is_new"),
                Map.entry("geologist", "basemetals:this_is_new"),
                Map.entry("metallurgy", "basemetals:geologist"),
                Map.entry("angel_of_death", "basemetals:mithril_maker"),
                Map.entry("scuba_diver", "basemetals:aquarium_maker"),
                Map.entry("demon_slayer", "minecraft:story/enter_the_nether"),
                Map.entry("juggernaut", "minecraft:story/enter_the_nether"),
                Map.entry("moon_boots", "minecraft:end/root"));
        Path advancements = GENERATED.resolve("data/basemetals/advancements");
        long count;
        try (Stream<Path> paths = Files.list(advancements)) {
            count = paths.filter(path -> path.toString().endsWith(".json")).count();
        }
        assertEquals(18, count);
        try (Stream<Path> paths = Files.list(advancements)) {
            for (Path path : (Iterable<Path>) paths.filter(file -> file.toString().endsWith(".json"))::iterator) {
                JsonObject advancement = read(path).getAsJsonObject();
                JsonObject event = advancement.getAsJsonObject("criteria").getAsJsonObject("event");
                assertEquals("minecraft:impossible", event.get("trigger").getAsString(), path.toString());
                assertFalse(advancement.toString().contains("minecraft:inventory_changed"), path.toString());
            }
        }
        parents.forEach((id, parent) -> {
            try {
                assertEquals(parent, read(advancements.resolve(id + ".json")).getAsJsonObject()
                        .get("parent").getAsString(), id);
            } catch (IOException exception) {
                throw new AssertionError(exception);
            }
        });
        for (String challenge : List.of("angel_of_death", "scuba_diver", "demon_slayer",
                "juggernaut", "moon_boots")) {
            assertEquals("challenge", read(advancements.resolve(challenge + ".json")).getAsJsonObject()
                    .getAsJsonObject("display").get("frame").getAsString(), challenge);
        }
    }

    @Test
    void customAnvilLootRetainsDamageWithoutUsingTheUnsafeVanillaTag() throws Exception {
        assertFalse(Files.exists(GENERATED.resolve("data/minecraft/tags/blocks/anvil.json")),
                "Custom anvils trigger vanilla's hard-coded fall deletion when placed in minecraft:anvil");
        JsonObject loot = read(resource("data/basemetals/loot_tables/blocks/stone_anvil.json"))
                .getAsJsonObject();
        JsonArray functions = loot.getAsJsonArray("pools").get(0).getAsJsonObject()
                .getAsJsonArray("entries").get(0).getAsJsonObject().getAsJsonArray("functions");
        assertTrue(functions.toString().contains("minecraft:copy_state"));
        assertTrue(functions.toString().contains("\"damage\""));
    }

    @Test
    void vanillaCrackhammerHeadsRetainTheirLegacyIngredients() throws Exception {
        JsonObject stone = read(resource("data/basemetals/recipes/stone_crackhammer.json")).getAsJsonObject();
        JsonObject wood = read(resource("data/basemetals/recipes/wood_crackhammer.json")).getAsJsonObject();

        assertEquals("minecraft:stone_bricks",
                stone.getAsJsonObject("key").getAsJsonObject("X").get("item").getAsString());
        assertEquals("minecraft:logs",
                wood.getAsJsonObject("key").getAsJsonObject("X").get("tag").getAsString());
    }

    @Test
    void chestInjectionTablesRetainEveryLegacyEntry() throws Exception {
        Map<String, String> templates = Map.ofEntries(
                Map.entry("abandoned_mineshaft", "abandoned_mineshaft"),
                Map.entry("desert_pyramid", "desert_pyramid"),
                Map.entry("end_city_treasure", "end_city_treasure"),
                Map.entry("jungle_temple", "jungle_temple"),
                Map.entry("nether_bridge", "nether_bridge"),
                Map.entry("simple_dungeon", "simple_dungeon"),
                Map.entry("spawn_bonus_chest", "spawn_bonus_chest"),
                Map.entry("stronghold_corridor", "stronghold_corridor"),
                Map.entry("stronghold_crossing", "stronghold_crossing"),
                Map.entry("village_armorer", "village_blacksmith"),
                Map.entry("village_toolsmith", "village_blacksmith"),
                Map.entry("village_weaponsmith", "village_blacksmith"));
        for (Map.Entry<String, String> target : templates.entrySet()) {
            JsonObject legacy = read(Path.of("reference/1.12/alt/chests/" + target.getValue() + ".json"))
                    .getAsJsonObject();
            JsonObject generated = read(resource("data/basemetals/loot_tables/chests/inject/"
                    + target.getKey() + ".json")).getAsJsonObject();
            assertEquals(legacy.getAsJsonArray("pools").size(), generated.getAsJsonArray("pools").size(), target.getKey());
            for (int pool = 0; pool < legacy.getAsJsonArray("pools").size(); pool++) {
                JsonArray legacyEntries = legacy.getAsJsonArray("pools").get(pool).getAsJsonObject()
                        .getAsJsonArray("entries");
                JsonArray generatedEntries = generated.getAsJsonArray("pools").get(pool).getAsJsonObject()
                        .getAsJsonArray("entries");
                assertEquals(legacyEntries.size(), generatedEntries.size(), target.getKey() + " pool " + pool);
                assertEquals(itemNames(legacyEntries), itemNames(generatedEntries),
                        target.getKey() + " changed legacy loot entries");
            }
        }
    }

    private static void assertBaselinePlusAdditions(JsonObject legacy, JsonObject current,
            String registry, String... additions) {
        Set<String> expected = new LinkedHashSet<>(strings(legacy.getAsJsonArray(registry)));
        for (String addition : additions) {
            expected.addAll(strings(current.getAsJsonArray(addition)));
        }
        assertEquals(expected, strings(current.getAsJsonArray(registry)), registry);
    }

    private static Set<String> itemNames(JsonArray entries) {
        Set<String> result = new LinkedHashSet<>();
        entries.forEach(value -> {
            JsonObject entry = value.getAsJsonObject();
            if (entry.has("name")) result.add(entry.get("name").getAsString());
        });
        return result;
    }

    private static void validateTagTree(Path dataRoot, String kind, Set<String> registry) throws Exception {
        if (!Files.isDirectory(dataRoot)) return;
        try (Stream<Path> paths = Files.walk(dataRoot)) {
            for (Path file : (Iterable<Path>) paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().replace('\\', '/').contains("/tags/" + kind + "/"))
                    .filter(path -> path.toString().endsWith(".json"))::iterator) {
                JsonArray values = read(file).getAsJsonObject().getAsJsonArray("values");
                for (JsonElement value : values) {
                    String id = value.isJsonObject() ? value.getAsJsonObject().get("id").getAsString() : value.getAsString();
                    if (id.startsWith("basemetals:")) {
                        assertTrue(registry.contains(id), file + " references missing " + kind + " ID " + id);
                    }
                }
            }
        }
    }

    private static void assertConditionalRecipe(String id, String modId) throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        JsonObject condition = recipe.getAsJsonArray("conditions").get(0).getAsJsonObject();
        assertEquals("forge:mod_loaded", condition.get("type").getAsString(), id);
        assertEquals(modId, condition.get("modid").getAsString(), id);
    }

    private static void assertCrushingRecipe(String id, String ingredientKind, String ingredient,
            String result, int count) throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        assertEquals("basemetals:crushing", recipe.get("type").getAsString(), id);
        assertEquals(ingredient, recipe.getAsJsonObject("ingredient").get(ingredientKind).getAsString(), id);
        assertEquals(result, recipe.getAsJsonObject("result").get("item").getAsString(), id);
        int actualCount = recipe.getAsJsonObject("result").has("count")
                ? recipe.getAsJsonObject("result").get("count").getAsInt()
                : 1;
        assertEquals(count, actualCount, id);
    }

    private static void assertCookingRecipe(String id, String type, String ingredientKind,
            String ingredient, String result, int count) throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        assertEquals(type, recipe.get("type").getAsString(), id);
        assertEquals(ingredient, recipe.getAsJsonObject("ingredient").get(ingredientKind).getAsString(), id);
        JsonElement resultElement = recipe.get("result");
        String actualItem;
        int actualCount;
        if (resultElement.isJsonObject()) {
            JsonObject resultObject = resultElement.getAsJsonObject();
            actualItem = resultObject.get("item").getAsString();
            actualCount = resultObject.has("count") ? resultObject.get("count").getAsInt() : 1;
        } else {
            actualItem = resultElement.getAsString();
            actualCount = 1;
        }
        assertEquals(result, actualItem, id);
        assertEquals(count, actualCount, id);
    }

    private static void assertRecipeResult(String id, String item, int count) throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        JsonObject result = recipe.getAsJsonObject("result");
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

    private static void assertRecipeIngredientTag(String id, String tag) throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        assertEquals(tag, recipe.getAsJsonArray("ingredients").get(0).getAsJsonObject()
                .get("tag").getAsString(), id);
    }

    private static void assertRecipeIngredientItem(String id, String item) throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        assertEquals(item, recipe.getAsJsonArray("ingredients").get(0).getAsJsonObject()
                .get("item").getAsString(), id);
    }

    private static void assertRecipePattern(String id, String... rows) throws Exception {
        JsonObject recipe = read(resource("data/basemetals/recipes/" + id + ".json")).getAsJsonObject();
        List<String> actual = new ArrayList<>();
        recipe.getAsJsonArray("pattern").forEach(row -> actual.add(row.getAsString()));
        assertEquals(List.of(rows), actual, id);
    }

    private static void visit(JsonElement value, KeyValueConsumer consumer) {
        if (value.isJsonObject()) {
            JsonObject object = value.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                if (entry.getKey().equals("textures") && entry.getValue().isJsonObject()) {
                    entry.getValue().getAsJsonObject().entrySet().forEach(texture ->
                            consumer.accept("texture:" + texture.getKey(), texture.getValue()));
                } else {
                    consumer.accept(entry.getKey(), entry.getValue());
                }
                visit(entry.getValue(), consumer);
            }
        } else if (value.isJsonArray()) {
            value.getAsJsonArray().forEach(child -> visit(child, consumer));
        }
    }

    private static Set<String> strings(JsonArray array) {
        Set<String> values = new LinkedHashSet<>();
        array.forEach(value -> values.add(value.getAsString()));
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
        if (Files.isRegularFile(generated)) return generated;
        return MAIN.resolve(relative);
    }

    private static JsonElement read(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonElement value = JsonParser.parseReader(reader);
            if (value.isJsonObject() && value.getAsJsonObject().has("recipe")
                    && "basemetals:content_crafting".equals(value.getAsJsonObject().get("type").getAsString())) {
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
        Set<String> result = new LinkedHashSet<>(expected);
        result.removeAll(actual);
        return result;
    }

    @FunctionalInterface
    private interface KeyValueConsumer {
        void accept(String key, JsonElement value);
    }
}
