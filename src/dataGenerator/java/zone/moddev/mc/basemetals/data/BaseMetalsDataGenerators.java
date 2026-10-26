package zone.moddev.mc.basemetals.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.content.*;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;

/** Material-independent shapes are shared templates; item definitions use native model properties. */
@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class BaseMetalsDataGenerators implements DataProvider {
    private final Path output;
    private final List<CompletableFuture<?>> writes = new ArrayList<>();

    private BaseMetalsDataGenerators(PackOutput output) {
        this.output = output.getOutputFolder();
    }

    @SubscribeEvent
    public static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeClient()) {
            generator.addProvider(true, new BaseMetalsDataGenerators(generator.getPackOutput()));
        }
        generator.addProvider(true, new GeneratedData(generator.getPackOutput()));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        writes.clear();

        try {
            writeBlockAssets(cache);
            writeItemAssets(cache);
            writeEquipment(cache);
        } catch (IOException exception) {
            return CompletableFuture.failedFuture(exception);
        }

        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    private void writeBlockAssets(CachedOutput cache) throws IOException {
        for (var entry : ModContent.blocksById().entrySet()) {
            String id = entry.getKey();
            Block block = entry.getValue().get();
            if (block instanceof LiquidBlock || id.endsWith("_ore")) continue;

            String shape = block instanceof PlateBlock ? "plate"
                    : block instanceof IronBarsBlock ? "bars"
                    : block instanceof DoorBlock ? "door"
                    : block instanceof TrapDoorBlock ? "trapdoor"
                    : block instanceof ButtonBlock ? "button"
                    : block instanceof SlabBlock ? "slab"
                    : block instanceof HumanDetectorBlock ? "human_detector"
                    : block instanceof PressurePlateBlock ? "pressure_plate"
                    : block instanceof StairBlock ? "stairs"
                    : block instanceof WallBlock ? "wall"
                    : block instanceof LeverBlock ? "lever"
                    : block instanceof BaseMetalAnvilBlock ? "anvil" : "block";
            String material = id.startsWith("double_") ? id.substring(7, id.length() - 5)
                    : shape.equals("human_detector") ? "" : id.substring(0, id.length() - shape.length() - 1);

            save(cache, template("blockstates/" + shape, material, id),
                    "blockstates/" + id + ".json");

            if (shape.equals("door")) {
                for (String half : List.of("bottom", "top")) {
                    for (String side : List.of("left", "right")) {
                        for (String open : List.of("", "_open")) {
                            String part = "door_" + half + "_" + side + open;
                            save(cache, template("block/" + part, material, id),
                                    "models/block/" + material + "_" + part + ".json");
                        }
                    }
                }
            } else if (shape.equals("wall") || shape.equals("bars")) {
                String part = shape.equals("wall") ? "wall_side_tall" : "bars_post_ends";
                save(cache, template("block/" + part, material, id),
                        "models/block/" + material + "_" + part + ".json");
            }
        }
    }

    private static JsonObject template(String name, String material, String id) throws IOException {
        try (InputStream stream = BaseMetalsDataGenerators.class.getResourceAsStream(
                "/datagen/templates/" + name + ".json")) {
            if (stream == null) throw new IOException("Missing model template " + name);

            String fullBlock = name.equals("blockstates/block") || MaterialCatalogue.BY_NAME.containsKey(material)
                    ? "basemetals:block/" + material + "_block"
                    : "minecraft:block/" + (material.equals("obsidian") ? "obsidian" : material + "_block");
            if (name.equals("block/wall_side_tall") && material.equals("quartz")) {
                fullBlock = "minecraft:block/quartz_block_side";
            }
            String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("basemetals:block/copper_block", fullBlock)
                    .replace("copper", material).replace("steel_anvil", id);

            return JsonParser.parseString(json).getAsJsonObject();
        }
    }

    private void writeItemAssets(CachedOutput cache) throws IOException {
        for (var entry : ModContent.itemsById().entrySet()) {
            String id = entry.getKey();
            var item = entry.getValue().get();

            if (id.endsWith("_raw") || id.endsWith("_blend") || id.endsWith("_smallblend")) {
                String texture = id.endsWith("_smallblend") ? id.replace("_smallblend", "_smallpowder")
                        : id.endsWith("_blend") ? id.replace("_blend", "_powder") : id;
                if (id.endsWith("_raw") || id.startsWith("diamond_") || id.startsWith("gold_") || id.startsWith("iron_")) {
                    save(cache, object("parent", "minecraft:item/generated",
                            "textures", object("layer0", "basemetals:item/" + texture)),
                            "models/item/" + id + ".json");
                }
            }

            JsonObject model = model(id);
            if (item instanceof MaterialItems.Bow || item instanceof MaterialItems.Crossbow) {
                JsonArray entries = new JsonArray();
                entries.add(object("threshold", 0.65, "model", model(id + "_pulling_1")));
                entries.add(object("threshold", 0.9, "model", model(id + "_pulling_2")));
                JsonObject draw = object("type", "minecraft:range_dispatch", "property", "minecraft:use_duration",
                        "scale", 0.05, "fallback", model(id + "_pulling_0"), "entries", entries);
                model = condition("minecraft:using_item", draw, model);
            } else if (item instanceof MaterialItems.FishingRod) {
                model = condition("minecraft:fishing_rod/cast", model(id + "_cast"), model);
            } else if (item instanceof MaterialItems.Shield) {
                model = condition("minecraft:using_item", model(id + "_blocking"), model);
            }

            if (ModContent.fluids().containsKey(id)) {
                String texture = id.equals("mercury") ? "mercury_still" : "molten_metal_still";
                save(cache, object("parent", "minecraft:item/generated",
                        "textures", object("layer0", "basemetals:block/" + texture)),
                        "models/item/" + id + ".json");
            }

            if (id.endsWith("_bucket")) {
                String fluidName = id.substring(0, id.length() - 7);

                // Keep the bucket and its fluid on one atlas. The aliases still use resource-pack textures.
                save(cache, object("parent", "forge:item/bucket", "fluid", "basemetals:" + fluidName,
                        "loader", "forge:fluid_container", "textures", object(
                                "base", "basemetals:block/bucket_base",
                                "fluid", "basemetals:block/bucket_fluid",
                                "cover", "basemetals:block/bucket_cover",
                                "particle", "#base")), "models/item/" + id + ".json");
                int colour = ((MetalFluidType) ModContent.fluids().get(fluidName)
                        .source().get().getFluidType()).colour();
                JsonArray tints = new JsonArray();
                tints.add(object("type", "minecraft:constant", "value", -1));
                tints.add(object("type", "minecraft:constant", "value", 0xFF000000 | colour));
                tints.add(object("type", "minecraft:constant", "value", -1));
                model.add("tints", tints);
            }

            save(cache, object("model", model), "items/" + id + ".json");
        }
    }

    private void writeEquipment(CachedOutput cache) throws IOException {
        var materials = new java.util.TreeSet<String>();
        for (var entry : ModContent.itemsById().values()) {
            if (entry.get() instanceof MaterialItems.Armor armor) {
                materials.add(armor.baseMetalsMaterial().name());
            }
        }

        for (String material : materials) {
            JsonArray humanoid = new JsonArray();
            humanoid.add(object("texture", "basemetals:" + material));
            JsonArray leggings = new JsonArray();
            leggings.add(object("texture", "basemetals:" + material));
            JsonArray horse = new JsonArray();
            horse.add(object("texture", "basemetals:" + material));
            save(cache, object("layers", object("humanoid", humanoid,
                    "humanoid_leggings", leggings, "horse_body", horse)),
                    "equipment/" + material + ".json");

            copyTexture(cache, "models/armor/" + material + "_layer_1.png",
                    "entity/equipment/humanoid/" + material + ".png");
            copyTexture(cache, "models/armor/" + material + "_layer_2.png",
                    "entity/equipment/humanoid_leggings/" + material + ".png");
            copyTexture(cache, "entity/horse/armor/horse_armor_" + material + ".png",
                    "entity/equipment/horse_body/" + material + ".png");
        }
    }

    private void copyTexture(CachedOutput cache, String source, String destination) throws IOException {
        Path root = Path.of(System.getProperty("basemetals.projectDir"));
        byte[] bytes = Files.readAllBytes(root.resolve("src/main/resources/assets/basemetals/textures/" + source));
        cache.writeIfNeeded(output.resolve("assets/basemetals/textures/" + destination),
                bytes, com.google.common.hash.Hashing.sha1().hashBytes(bytes));
    }

    private static JsonObject model(String id) {
        return object("type", "minecraft:model", "model", "basemetals:item/" + id);
    }

    private static JsonObject condition(String property, JsonObject yes, JsonObject no) {
        return object("type", "minecraft:condition", "property", property, "on_true", yes, "on_false", no);
    }

    private void save(CachedOutput cache, JsonObject json, String path) {
        writes.add(DataProvider.saveStable(cache, json, output.resolve("assets/basemetals/" + path)));
    }

    static JsonObject object(Object... entries) {
        JsonObject result = new JsonObject();
        for (int index = 0; index < entries.length; index += 2) {
            String name = (String) entries[index];
            Object value = entries[index + 1];
            if (value instanceof com.google.gson.JsonElement json) result.add(name, json);
            else if (value instanceof Number number) result.addProperty(name, number);
            else if (value instanceof Boolean bool) result.addProperty(name, bool);
            else result.addProperty(name, value.toString());
        }

        return result;
    }

    @Override
    public String getName() {
        return "Base Metals block and equipment assets";
    }
}
