package zone.moddev.mc.basemetals.testmod;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import zone.moddev.mc.basemetals.content.ModContent;

/** Samples fixed chunks in each vanilla dimension and checks the provider's bounds. */
final class OrePlacementChecks {
    private OrePlacementChecks() {}

    static int run(MinecraftServer server) throws Exception {
        JsonObject ores;
        try (InputStreamReader reader = new InputStreamReader(OrePlacementChecks.class.getResourceAsStream(
                "/data/basemetals/orespawn/provider.json"), StandardCharsets.UTF_8)) {
            ores = new JsonParser().parse(reader).getAsJsonObject().getAsJsonObject("ores");
        }

        Map<Block, JsonObject> rules = new LinkedHashMap<>();
        ores.entrySet().forEach(entry -> {
            JsonObject rule = entry.getValue().getAsJsonObject();
            rules.put(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(rule.get("block").getAsString())), rule);
        });
        int checks = 0;
        for (DimensionType dimension : new DimensionType[] {
                DimensionType.OVERWORLD, DimensionType.THE_NETHER, DimensionType.THE_END}) {
            ServerWorld world = server.getWorld(dimension);
            String dimensionId = DimensionType.getKey(dimension).toString();
            Map<String, Integer> counts = new LinkedHashMap<>();
            int rockCount = 0;
            int centerX = dimension == DimensionType.OVERWORLD ? world.getSpawnPoint().getX() >> 4 : 0;
            int centerZ = dimension == DimensionType.OVERWORLD ? world.getSpawnPoint().getZ() >> 4 : 0;

            for (int cx = centerX - 2; cx <= centerX + 2; cx++) {
                for (int cz = centerZ - 2; cz <= centerZ + 2; cz++) {
                    Chunk chunk = world.getChunk(cx, cz);
                    for (ChunkSection section : chunk.getSections()) {
                        if (section == null || section.isEmpty()) continue;
                        for (int x = 0; x < 16; x++) for (int y = 0; y < 16; y++) for (int z = 0; z < 16; z++) {
                            Block block = section.getBlockState(x, y, z).getBlock();
                            if ("mineralogy".equals(block.getRegistryName().getNamespace())
                                    && !block.getRegistryName().getPath().endsWith("_ore")) rockCount++;
                            if (!rules.containsKey(block)) {
                                if (block == ModContent.blocksById().get("antimony_ore").get()
                                        || block == ModContent.blocksById().get("bismuth_ore").get()) {
                                    throw new IllegalStateException("Non-generating ore appeared: " + block);
                                }
                                continue;
                            }

                            JsonObject rule = rules.get(block);
                            JsonObject placement = rule.has("dimensions")
                                    ? rule.getAsJsonObject("dimensions").getAsJsonObject(dimensionId)
                                    : dimension == DimensionType.OVERWORLD ? rule.getAsJsonObject("dimension_selectors")
                                            .getAsJsonObject("orespawn:all_except_nether_end") : null;
                            int height = section.getYLocation() + y;
                            if (placement == null || height < placement.get("min_y").getAsInt()
                                    || height > placement.get("max_y").getAsInt()) {
                                throw new IllegalStateException("Ore outside its dimension/height range: " + block
                                        + " in " + dimensionId + " at " + height);
                            }
                            counts.merge(block.getRegistryName().toString(), 1, Integer::sum);
                            checks++;
                        }
                    }
                }
            }

            if (counts.isEmpty()) throw new IllegalStateException("No Base Metals ore in " + dimensionId + " sample");
            if (dimension == DimensionType.OVERWORLD && net.minecraftforge.fml.ModList.get().isLoaded("mineralogy")) {
                if (rockCount == 0) throw new IllegalStateException("Mineralogy strata are absent from the ore sample");
                LogManager.getLogger("basemetalsprobe").info("BASEMETALS_MINERALOGY_COEXISTENCE PASS rocks={}", rockCount);
            }
            LogManager.getLogger("basemetalsprobe").info("BASEMETALS_ORE_SAMPLE PASS dimension={} chunks=25 counts={}",
                    dimensionId, counts);
        }

        return checks;
    }
}
