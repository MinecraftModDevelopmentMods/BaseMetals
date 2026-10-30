package zone.moddev.mc.basemetals.smoke;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

/** Validation-only mod that proves the packaged server loads the exact release jars. */
@Mod(PackagedServerSmoke.MOD_ID)
public final class PackagedServerSmoke {
    public static final String MOD_ID = "basemetals_server_smoke";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PackagedServerSmoke() {
        ServerStartedEvent.BUS.addListener(this::onServerStarted);

        if (Boolean.getBoolean("basemetals.capabilityDiskSmoke")) {
            SavedWorldCapabilitySmoke.register();
        }

        if (Boolean.getBoolean("basemetals.upgradeWorldSmoke")) {
            try {
                Class.forName("zone.moddev.mc.basemetals.smoke.UpgradeWorldSmoke")
                        .getMethod("register").invoke(null);
            } catch (ReflectiveOperationException exception) {
                throw new IllegalStateException("Could not register the isolated upgrade probe", exception);
            }
        }
    }

    public void onServerStarted(ServerStartedEvent event) {
        String baseMetals = version("basemetals");
        String oreSpawn = version("orespawn");
        if (!"3.1.1.2603001".equals(baseMetals) || !"4.1.0.2603001".equals(oreSpawn)) {
            throw new IllegalStateException("PACKAGED_SERVER_SMOKE FAIL versions basemetals="
                    + baseMetals + " orespawn=" + oreSpawn);
        }
        verifyContent(event.getServer());
        if (Boolean.getBoolean("basemetals.oreSampleSmoke")) verifyOreSample(event.getServer());
        if (Boolean.getBoolean("basemetals.modernFlatWorldSmoke")) verifyModernFlatTerrain(event.getServer());
        if (Boolean.getBoolean("basemetals.capabilityDiskSmoke")) SavedWorldCapabilitySmoke.verify(event.getServer());
        Properties result = new Properties();
        result.setProperty("basemetals", baseMetals);
        result.setProperty("orespawn", oreSpawn);
        result.setProperty("forge", version("forge"));
        Path marker = event.getServer().getServerDirectory()
                .resolve("packaged-server-smoke-pass.properties");
        try (var writer = Files.newBufferedWriter(marker, StandardCharsets.UTF_8)) {
            result.store(writer, "Base Metals packaged dedicated-server smoke");
        } catch (IOException exception) {
            throw new java.io.UncheckedIOException("Could not save the server smoke result", exception);
        }
        LOGGER.info("BASEMETALS_PACKAGED_SERVER_SMOKE PASS basemetals={} orespawn={} forge={}",
                baseMetals, oreSpawn, result.getProperty("forge"));
        if (!Boolean.getBoolean("basemetals.awaitLoginSmoke") && !Boolean.getBoolean("basemetals.upgradeWorldSmoke")) {
            event.getServer().halt(false);
        }
    }

    private static String version(String modId) {
        return ModList.getModContainerById(modId)
                .orElseThrow(() -> new IllegalStateException("Missing required packaged mod " + modId))
                .getModInfo().getVersion().toString();
    }

    private static void verifyContent(net.minecraft.server.MinecraftServer server) {
        var level = server.overworld();
        var policy = zone.moddev.mc.basemetals.config.ContentPolicy.active();
        var items = zone.moddev.mc.basemetals.content.ModContent.itemsById();
        if (items.size() != 1127) throw new IllegalStateException("Packaged item inventory changed: " + items.size());
        for (var entry : items.entrySet()) {
            String id = entry.getKey();
            if (id.endsWith("_powder") || id.endsWith("_smallpowder") || id.endsWith("_nugget")) {
                int burn = new TestFurnace().burnDuration(level, entry.getValue().get().getDefaultInstance());
                int expected = id.startsWith("coal_") || id.startsWith("charcoal_")
                        ? (id.endsWith("_powder") ? 1600 : 200) : 0;
                if (burn != expected) throw new IllegalStateException(id + " has burn time " + burn + ", expected " + expected);
            }
        }
        var bow = server.getRecipeManager().byKey(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, net.minecraft.resources.Identifier.parse("basemetals:adamantine_bow")))
                .orElseThrow();
        boolean enabled = !bow.value().isSpecial();
        if (enabled != policy.allows("basemetals:adamantine_bow")) {
            throw new IllegalStateException("The recipe book does not follow the active content mode");
        }
        if (ModList.isLoaded("tconstruct")) verifyTinkers();
        try {
            verifyIntegrationRecipes(server);
        } catch (IOException exception) {
            throw new java.io.UncheckedIOException("Could not read the integration recipes", exception);
        }
        LOGGER.info("BASEMETALS_PACKAGED_CONTENT PASS mode={} items={}",
                zone.moddev.mc.basemetals.config.BaseMetalsConfig.activeMode().serializedName(), items.size());
    }

    private static final class TestFurnace extends net.minecraft.world.level.block.entity.FurnaceBlockEntity {
        private TestFurnace() {
            super(net.minecraft.core.BlockPos.ZERO, net.minecraft.world.level.block.Blocks.FURNACE.defaultBlockState());
        }

        private int burnDuration(net.minecraft.server.level.ServerLevel level, net.minecraft.world.item.ItemStack stack) {
            return getBurnDuration(level, stack);
        }
    }

    private static void verifyIntegrationRecipes(net.minecraft.server.MinecraftServer server) throws IOException {
        var policy = zone.moddev.mc.basemetals.config.ContentPolicy.active();
        int checked = 0;
        for (var resource : server.getResourceManager().listResources("recipe/compat", id -> id.getPath().endsWith(".json")).keySet()) {
            if (!resource.getNamespace().equals("basemetals")) continue;
            String path = resource.getPath();
            String mod = path.contains("/thermal/") ? "thermal_expansion"
                    : path.contains("/tconstruct/") ? "tconstruct"
                    : path.contains("/mekanism/") ? "mekanism" : "enderio";
            if (!ModList.isLoaded(mod)) continue;
            try (var input = server.getResourceManager().getResource(resource).orElseThrow().open()) {
                var json = com.google.gson.JsonParser.parseReader(new java.io.InputStreamReader(
                        input, StandardCharsets.UTF_8)).getAsJsonObject();
                boolean enabled = true;
                for (var condition : json.getAsJsonArray("conditions")) {
                    var object = condition.getAsJsonObject();
                    if (!"basemetals:content_mode".equals(object.get("type").getAsString())) continue;
                    enabled &= policy.allows(object.get("item").getAsString());
                }
                var id = net.minecraft.resources.Identifier.fromNamespaceAndPath("basemetals",
                        path.substring("recipe/".length(), path.length() - ".json".length()));
                if (server.getRecipeManager().byKey(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, id)).isPresent() != enabled) {
                    throw new IllegalStateException("Integration recipe does not follow content mode: " + id);
                }
                checked++;
            }
        }
        LOGGER.info("BASEMETALS_PACKAGED_INTEGRATIONS PASS mode={} recipes={}",
                zone.moddev.mc.basemetals.config.BaseMetalsConfig.activeMode().serializedName(), checked);
    }

    private static void verifyOreSample(net.minecraft.server.MinecraftServer server) {
        if (zone.moddev.mc.orespawn.api.OreSpawnApi.getProviderStatus("basemetals")
                != zone.moddev.mc.orespawn.api.ProviderStatus.ACTIVE) {
            throw new IllegalStateException("Base Metals' OreSpawn provider is not active");
        }
        for (var level : server.getAllLevels()) {
            var dimension = level.dimension();
            var counts = new java.util.TreeMap<String, Integer>();
            int start = dimension == net.minecraft.world.level.Level.OVERWORLD
                    ? Integer.getInteger("basemetals.oreSampleOverworldChunkStart", 0) : 0;

            for (int chunkX = start; chunkX < start + 3; chunkX++) {
                for (int chunkZ = start; chunkZ < start + 3; chunkZ++) {
                    var chunk = level.getChunk(chunkX, chunkZ);
                    for (var section : chunk.getSections()) {
                        for (int x = 0; x < 16; x++) for (int y = 0; y < 16; y++) for (int z = 0; z < 16; z++) {
                            var id = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(
                                    section.getBlockState(x, y, z).getBlock());
                            if (id == null || !id.getNamespace().equals("basemetals") || !id.getPath().endsWith("_ore")) continue;
                            String ore = id.getPath();
                            boolean permitted = dimension == net.minecraft.world.level.Level.NETHER
                                    ? ore.equals("adamantine_ore") || ore.equals("coldiron_ore")
                                    : dimension == net.minecraft.world.level.Level.END ? ore.equals("starsteel_ore")
                                    : java.util.Set.of("tin_ore", "lead_ore", "zinc_ore", "silver_ore",
                                            "mercury_ore", "nickel_ore", "platinum_ore").contains(ore);
                            if (!permitted) throw new IllegalStateException("Unexpected ore in " + dimension + ": " + id);
                            counts.merge(ore, 1, Integer::sum);
                        }
                    }
                }
            }
            if (counts.isEmpty()) throw new IllegalStateException("No Base Metals ores in " + dimension);
            if (Boolean.getBoolean("basemetals.tagOnlyTinSmoke")
                    && dimension == net.minecraft.world.level.Level.OVERWORLD
                    && counts.getOrDefault("tin_ore", 0) == 0) {
                throw new IllegalStateException("The tag-only tin rule produced no ore after tags were loaded");
            }
            LOGGER.info("BASEMETALS_ORE_SAMPLE PASS dimension={} chunks=9 ores={}", dimension.identifier(), counts);
        }
    }

    private static void verifyTinkers() {
        throw new IllegalStateException("Tinkers integration is not enabled in this Forge 26.3 build");
    }

    private static void verifyModernFlatTerrain(net.minecraft.server.MinecraftServer server) {
        var level = server.overworld();
        if (!(level.getChunkSource().getGenerator() instanceof net.minecraft.world.level.levelgen.FlatLevelSource)) {
            throw new IllegalStateException("The modern-world control must use its captured flat generator");
        }

        int start = Boolean.getBoolean("basemetals.modernLaterChunkSmoke") ? 10 : 0;
        int checked = 0;

        for (int chunkX = start; chunkX < start + 3; chunkX++) {
            for (int chunkZ = start; chunkZ < start + 3; chunkZ++) {
                var chunk = level.getChunk(chunkX, chunkZ);

                for (int sectionIndex = 0; sectionIndex < chunk.getSectionsCount(); sectionIndex++) {
                    var section = chunk.getSection(sectionIndex);
                    int bottom = chunk.getSectionYFromSectionIndex(sectionIndex) * 16;

                    for (int y = 0; y < 16; y++) {
                        int height = bottom + y;
                        var expected = height == -64 ? net.minecraft.world.level.block.Blocks.BEDROCK
                                : height == -63 || height == -62 ? net.minecraft.world.level.block.Blocks.DIRT
                                : height == -61 ? net.minecraft.world.level.block.Blocks.GRASS_BLOCK
                                : net.minecraft.world.level.block.Blocks.AIR;

                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {
                                if (!section.getBlockState(x, y, z).is(expected)) {
                                    throw new IllegalStateException("Modern terrain changed at chunk "
                                            + chunk.getPos() + ", local " + x + "," + height + "," + z);
                                }
                                checked++;
                            }
                        }
                    }
                }
            }
        }

        LOGGER.info("BASEMETALS_MODERN_TERRAIN PASS blocks={} later_chunks={}",
                checked, start != 0);
    }
}
