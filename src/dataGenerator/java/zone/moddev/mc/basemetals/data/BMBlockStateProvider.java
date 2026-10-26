package zone.moddev.mc.basemetals.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;

import static zone.moddev.mc.basemetals.data.BaseMetalsDataGenerators.object;

/**
 * Builds ores from a vanilla host and a transparent overlay.
 *
 * @author KiriCattus (Kiri), original two-layer model
 */
@Mod.EventBusSubscriber(modid = "basemetals", bus = Mod.EventBusSubscriber.Bus.MOD)
public final class BMBlockStateProvider implements DataProvider {
    private static final float OVERLAY_OFFSET = 0.05F;
    private final java.nio.file.Path output;
    private final List<CompletableFuture<?>> writes = new ArrayList<>();

    public BMBlockStateProvider(PackOutput output) {
        this.output = output.getOutputFolder();
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if (event.includeClient()) {
            event.getGenerator().addProvider(true, new BMBlockStateProvider(event.getGenerator().getPackOutput()));
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        writes.clear();
        for (var material : MaterialCatalogue.ALL) {
            if (!material.hasOre()) continue;

            String host = switch (material.name()) {
                case "adamantine", "coldiron" -> "netherrack";
                case "starsteel" -> "end_stone";
                default -> "stone";
            };
            createOverlayOre(cache, ModContent.blocksById().get(material.name() + "_ore").get(),
                    Identifier.withDefaultNamespace("block/" + host),
                    Identifier.fromNamespaceAndPath("basemetals", "block/ore_overlays/" + material.name() + "_ore"));
        }

        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    private void createOverlayOre(CachedOutput cache, Block block, Identifier baseTexture, Identifier overlayTexture) {
        String blockName = ForgeRegistries.BLOCKS.getKey(block).getPath();
        JsonObject textures = object("particle", baseTexture, "base", baseTexture, "overlay", overlayTexture);
        JsonArray elements = new JsonArray();
        elements.add(cube(0, 16, "#base"));
        // Keep the overlay clear of the host, including distant mip levels.
        elements.add(cube(-OVERLAY_OFFSET, 16 + OVERLAY_OFFSET, "#overlay"));

        JsonObject model = object("parent", "minecraft:block/block", "textures", textures, "elements", elements);
        writes.add(DataProvider.saveStable(cache, model,
                output.resolve("assets/basemetals/models/block/" + blockName + ".json")));
        writes.add(DataProvider.saveStable(cache,
                object("variants", object("", object("model", "basemetals:block/" + blockName))),
                output.resolve("assets/basemetals/blockstates/" + blockName + ".json")));
    }

    private static JsonObject cube(float from, float to, String texture) {
        JsonArray start = new JsonArray();
        JsonArray end = new JsonArray();
        for (int axis = 0; axis < 3; axis++) {
            start.add(from);
            end.add(to);
        }

        JsonObject faces = new JsonObject();
        for (String direction : List.of("down", "up", "north", "south", "west", "east")) {
            faces.add(direction, object("texture", texture, "cullface", direction));
        }

        return object("from", start, "to", end, "faces", faces);
    }

    @Override
    public String getName() {
        return "Base Metals ore overlays";
    }
}
