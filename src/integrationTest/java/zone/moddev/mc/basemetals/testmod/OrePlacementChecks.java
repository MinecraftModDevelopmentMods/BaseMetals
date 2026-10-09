package zone.moddev.mc.basemetals.testmod;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.world.level.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import zone.moddev.mc.basemetals.content.ModContent;

/** Samples fixed chunks in vanilla dimensions and an ordinary custom Overworld. */
final class OrePlacementChecks {
    private static final long SAMPLE_WORLD_SEED = 8675309L;

    private OrePlacementChecks() {}

    static int run(MinecraftServer server) throws Exception {
        long worldSeed = server.overworld().getSeed();
        if (worldSeed != SAMPLE_WORLD_SEED) {
            throw new IllegalStateException("Ore sample world has seed " + worldSeed
                    + "; expected " + SAMPLE_WORLD_SEED);
        }

        JsonObject activeOres = zone.moddev.mc.orespawn.api.OreSpawnApi.getActiveProfile(server)
                .orElseThrow().toJson().getAsJsonObject("ores");
        boolean expectedCopper = Boolean.getBoolean("basemetalsprobe.expectCopperEnabled");
        if (activeOres.getAsJsonObject("basemetals:ore/copper").get("enabled").getAsBoolean() != expectedCopper) {
            throw new IllegalStateException("The effective copper rule did not retain its expected setting");
        }

        JsonObject ores;
        try (InputStreamReader reader = new InputStreamReader(zone.moddev.mc.basemetals.BaseMetals.class.getResourceAsStream(
                "/data/basemetals/orespawn/provider.json"), StandardCharsets.UTF_8)) {
            ores = new JsonParser().parse(reader).getAsJsonObject().getAsJsonObject("ores");
        }

        Map<Block, JsonObject> rules = new LinkedHashMap<>();
        ores.entrySet().forEach(entry -> {
            JsonObject rule = activeOres.getAsJsonObject(entry.getKey());
            rules.put(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(rule.get("block").getAsString())), rule);
        });
        int checks = 0;
        for (ServerLevel world : server.getAllLevels()) {
            ResourceKey<Level> dimension = world.dimension();
            String dimensionId = dimension.location().toString();
            Map<String, Integer> counts = new LinkedHashMap<>();
            int rockCount = 0;
            int vanillaCopperCount = 0;
            int centerX = dimension == Level.OVERWORLD ? world.getSharedSpawnPos().getX() >> 4 : 0;
            int centerZ = dimension == Level.OVERWORLD ? world.getSharedSpawnPos().getZ() >> 4 : 0;

            for (int cx = centerX - 2; cx <= centerX + 2; cx++) {
                for (int cz = centerZ - 2; cz <= centerZ + 2; cz++) {
                    LevelChunk chunk = world.getChunk(cx, cz);
                    for (LevelChunkSection section : chunk.getSections()) {
                        if (section == null || section.isEmpty()) continue;
                        for (int x = 0; x < 16; x++) for (int y = 0; y < 16; y++) for (int z = 0; z < 16; z++) {
                            Block block = section.getBlockState(x, y, z).getBlock();
                            if (block == net.minecraft.world.level.block.Blocks.COPPER_ORE
                                    || block == net.minecraft.world.level.block.Blocks.DEEPSLATE_COPPER_ORE) vanillaCopperCount++;
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
                            if (!rule.get("enabled").getAsBoolean()) {
                                throw new IllegalStateException("Default-disabled ore appeared: " + block);
                            }
                            JsonObject placement = rule.has("dimensions")
                                    ? rule.getAsJsonObject("dimensions").getAsJsonObject(dimensionId) : null;
                            if (placement == null && dimension != Level.NETHER && dimension != Level.END
                                    && rule.has("dimension_selectors")) {
                                placement = rule.getAsJsonObject("dimension_selectors")
                                        .getAsJsonObject("orespawn:all_except_nether_end");
                            }
                            int height = section.bottomBlockY() + y;
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
            if (dimension == Level.OVERWORLD && !net.minecraftforge.fml.ModList.get().isLoaded("mineralogy")) {
                if (vanillaCopperCount == 0) throw new IllegalStateException("Vanilla copper generation was disabled");
                LogManager.getLogger("basemetalsprobe").info("BASEMETALS_COPPER_DEFAULTS PASS vanilla={} basemetals_enabled={}",
                        vanillaCopperCount, expectedCopper);
            }
            if (dimension == Level.OVERWORLD && net.minecraftforge.fml.ModList.get().isLoaded("mineralogy")) {
                if (rockCount == 0) throw new IllegalStateException("Mineralogy strata are absent from the ore sample");
                LogManager.getLogger("basemetalsprobe").info("BASEMETALS_MINERALOGY_COEXISTENCE PASS rocks={}", rockCount);
            }
            LogManager.getLogger("basemetalsprobe").info("BASEMETALS_ORE_SAMPLE PASS dimension={} chunks=25 counts={}",
                    dimensionId, counts);
        }

        if (server.getLevel(ResourceKey.create(net.minecraft.core.Registry.DIMENSION_REGISTRY,
                new ResourceLocation("basemetalsprobe", "test_overworld"))) == null) {
            throw new IllegalStateException("Custom-dimension ore sample was not loaded");
        }

        return checks;
    }
}
