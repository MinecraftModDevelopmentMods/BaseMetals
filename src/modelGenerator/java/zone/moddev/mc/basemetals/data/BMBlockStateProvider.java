package zone.moddev.mc.basemetals.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

/**
 * Kiri's ore-overlay model, written as JSON for Forge 1.13's data pipeline.
 * The newer Forge BlockStateProvider and BlockModelBuilder are not available here.
 *
 * @author KiriCattus (Kiri), original two-layer model
 */
public final class BMBlockStateProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String[] FACES = {"down", "up", "north", "south", "west", "east"};

    private BMBlockStateProvider() {}

    public static void main(String[] args) throws IOException {
        if (args.length != 1 && (args.length != 3
                || !("--output".equals(args[1]) || "--client-fixtures".equals(args[1])))) {
            throw new IllegalArgumentException("Expected a resource directory, optionally followed by "
                    + "--output <directory> or --client-fixtures <directory>");
        }

        Path resources = Paths.get(args[0]);
        boolean clientFixture = args.length == 3 && "--client-fixtures".equals(args[1]);
        Path output = args.length == 3 ? Paths.get(args[2]) : resources;

        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            if (!material.hasOre()) continue;

            String blockName = material.name() + "_ore";
            String baseTexture = baseTexture(material.name());
            String overlayTexture = "basemetals:block/ore_overlays/" + blockName;
            Path overlay = resources.resolve("assets/basemetals/textures/block/ore_overlays/"
                    + blockName + ".png");

            boolean hasOverlay = Files.isRegularFile(overlay);
            JsonObject model = clientFixture && !hasOverlay
                    ? createOverlayOre(baseTexture, "minecraft:block/glass")
                    : modelFor(blockName, baseTexture, overlayTexture, hasOverlay);

            write(output.resolve("assets/basemetals/models/block/" + blockName + ".json"), model);
        }

        if (clientFixture) {
            // The client probe enables these host replacements in a test-only resource pack.
            copyHostFixture(resources, output, "tin", "stone");
            copyHostFixture(resources, output, "coldiron", "netherrack");
            copyHostFixture(resources, output, "starsteel", "end_stone");
        }
    }

    public static String baseTexture(String material) {
        if ("adamantine".equals(material) || "coldiron".equals(material)) {
            return "minecraft:block/netherrack";
        }
        if ("starsteel".equals(material)) return "minecraft:block/end_stone";

        return "minecraft:block/stone";
    }

    public static JsonObject modelFor(String blockName, String baseTexture,
            String overlayTexture, boolean hasOverlay) {
        if (hasOverlay) return createOverlayOre(baseTexture, overlayTexture);

        // Use the original texture when an overlay is absent.
        JsonObject model = new JsonObject();
        model.addProperty("parent", "block/cube_all");
        JsonObject textures = new JsonObject();
        textures.addProperty("all", "basemetals:block/" + blockName);
        model.add("textures", textures);
        return model;
    }

    public static JsonObject createOverlayOre(String baseTexture, String overlayTexture) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:block/block");

        JsonObject textures = new JsonObject();
        textures.addProperty("particle", baseTexture);
        textures.addProperty("base", baseTexture);
        textures.addProperty("overlay", overlayTexture);
        model.add("textures", textures);

        JsonArray elements = new JsonArray();

        // Element 1: base block (Stone, Netherrack or End Stone).
        elements.add(element(0, 16, "#base"));

        // Element 2: Kiri's 0.001 offset keeps the overlay from z-fighting with the base.
        elements.add(element(-0.001F, 16.001F, "#overlay"));

        model.add("elements", elements);
        return model;
    }

    private static JsonObject element(float from, float to, String texture) {
        JsonObject element = new JsonObject();
        element.add("from", point(from));
        element.add("to", point(to));

        JsonObject faces = new JsonObject();
        for (String direction : FACES) {
            JsonObject face = new JsonObject();
            face.addProperty("texture", texture);
            face.addProperty("cullface", direction);
            faces.add(direction, face);
        }

        element.add("faces", faces);
        return element;
    }

    private static JsonArray point(float coordinate) {
        JsonArray point = new JsonArray();
        for (int axis = 0; axis < 3; axis++) point.add(coordinate);
        return point;
    }

    private static void write(Path file, JsonObject model) throws IOException {
        byte[] contents = (GSON.toJson(model) + "\n").getBytes(StandardCharsets.UTF_8);
        if (Files.isRegularFile(file) && Arrays.equals(contents, Files.readAllBytes(file))) return;

        Files.createDirectories(file.getParent());
        Files.write(file, contents);
    }

    private static void copyHostFixture(Path resources, Path output, String ore, String host)
            throws IOException {
        Path target = output.resolve("assets/minecraft/textures/block/" + host + ".png");
        Files.createDirectories(target.getParent());
        Files.copy(resources.resolve("assets/basemetals/textures/block/" + ore + "_ore.png"),
                target, StandardCopyOption.REPLACE_EXISTING);
    }
}
