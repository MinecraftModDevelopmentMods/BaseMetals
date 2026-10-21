package zone.moddev.mc.basemetals.smoke;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

/** Validation-only mod that proves the packaged server loads the exact release jars. */
@Mod(PackagedServerSmoke.MOD_ID)
public final class PackagedServerSmoke {
    public static final String MOD_ID = "basemetals_server_smoke";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PackagedServerSmoke() {
        MinecraftForge.EVENT_BUS.register(this);
        if (Boolean.getBoolean("basemetals.upgradeWorldSmoke")) {
            try {
                MinecraftForge.EVENT_BUS.register(Class.forName(
                        "zone.moddev.mc.basemetals.smoke.UpgradeWorldSmoke"));
            } catch (ClassNotFoundException exception) {
                throw new IllegalStateException("The upgrade probe is missing from the test jar", exception);
            }
        }
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) throws IOException {
        String baseMetals = version("basemetals");
        String oreSpawn = version("orespawn");
        if (!"3.1.0.120011".equals(baseMetals) || !"4.1.0.120011".equals(oreSpawn)) {
            throw new IllegalStateException("PACKAGED_SERVER_SMOKE FAIL versions basemetals="
                    + baseMetals + " orespawn=" + oreSpawn);
        }
        verifyContent(event.getServer());
        if (Boolean.getBoolean("basemetals.oreSampleSmoke")) verifyOreSample(event.getServer());
        Properties result = new Properties();
        result.setProperty("basemetals", baseMetals);
        result.setProperty("orespawn", oreSpawn);
        result.setProperty("forge", version("forge"));
        Path marker = event.getServer().getServerDirectory().toPath()
                .resolve("packaged-server-smoke-pass.properties");
        try (var writer = Files.newBufferedWriter(marker, StandardCharsets.UTF_8)) {
            result.store(writer, "Base Metals packaged dedicated-server smoke");
        }
        LOGGER.info("BASEMETALS_PACKAGED_SERVER_SMOKE PASS basemetals={} orespawn={} forge={}",
                baseMetals, oreSpawn, result.getProperty("forge"));
        if (!Boolean.getBoolean("basemetals.awaitLoginSmoke") && !Boolean.getBoolean("basemetals.upgradeWorldSmoke")) {
            event.getServer().halt(false);
        }
    }

    private static String version(String modId) {
        return ModList.get().getModContainerById(modId)
                .orElseThrow(() -> new IllegalStateException("Missing required packaged mod " + modId))
                .getModInfo().getVersion().toString();
    }

    private static void verifyContent(net.minecraft.server.MinecraftServer server) throws IOException {
        var level = server.overworld();
        var policy = zone.moddev.mc.basemetals.config.ContentPolicy.active();
        var items = zone.moddev.mc.basemetals.content.ModContent.itemsById();
        if (items.size() != 1127) throw new IllegalStateException("Packaged item inventory changed: " + items.size());
        for (var entry : items.entrySet()) {
            String id = entry.getKey();
            if (id.endsWith("_powder") || id.endsWith("_smallpowder") || id.endsWith("_nugget")) {
                int burn = net.minecraftforge.common.ForgeHooks.getBurnTime(entry.getValue().get().getDefaultInstance(),
                        net.minecraft.world.item.crafting.RecipeType.SMELTING);
                int expected = id.startsWith("coal_") || id.startsWith("charcoal_")
                        ? (id.endsWith("_powder") ? 1600 : 200) : 0;
                if (burn != expected) throw new IllegalStateException(id + " has burn time " + burn + ", expected " + expected);
            }
        }
        var bow = server.getRecipeManager().byKey(new net.minecraft.resources.ResourceLocation("basemetals:adamantine_bow"))
                .orElseThrow();
        boolean enabled = !bow.isSpecial();
        if (enabled != policy.allows("basemetals:adamantine_bow")) {
            throw new IllegalStateException("The recipe book does not follow the active content mode");
        }
        if (ModList.get().isLoaded("tconstruct")) verifyTinkers();
        verifyIntegrationRecipes(server);
        LOGGER.info("BASEMETALS_PACKAGED_CONTENT PASS mode={} items={}",
                zone.moddev.mc.basemetals.config.BaseMetalsConfig.activeMode().serializedName(), items.size());
    }

    private static void verifyIntegrationRecipes(net.minecraft.server.MinecraftServer server) throws IOException {
        var policy = zone.moddev.mc.basemetals.config.ContentPolicy.active();
        int checked = 0;
        for (var resource : server.getResourceManager().listResources("recipes/compat", id -> id.getPath().endsWith(".json")).keySet()) {
            if (!resource.getNamespace().equals("basemetals")) continue;
            String path = resource.getPath();
            String mod = path.contains("/thermal/") ? "thermal_expansion"
                    : path.contains("/tconstruct/") ? "tconstruct"
                    : path.contains("/mekanism/") ? "mekanism" : "enderio";
            if (!ModList.get().isLoaded(mod)) continue;
            try (var input = server.getResourceManager().getResource(resource).orElseThrow().open()) {
                var json = com.google.gson.JsonParser.parseReader(new java.io.InputStreamReader(
                        input, StandardCharsets.UTF_8)).getAsJsonObject();
                boolean enabled = true;
                for (var condition : json.getAsJsonArray("conditions")) {
                    var object = condition.getAsJsonObject();
                    if (!"basemetals:content_mode".equals(object.get("type").getAsString())) continue;
                    enabled &= policy.allows(object.get("item").getAsString());
                }
                var id = new net.minecraft.resources.ResourceLocation("basemetals",
                        path.substring("recipes/".length(), path.length() - ".json".length()));
                if (server.getRecipeManager().byKey(id).isPresent() != enabled) {
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
            for (int chunkX = 0; chunkX < 3; chunkX++) {
                for (int chunkZ = 0; chunkZ < 3; chunkZ++) {
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
            LOGGER.info("BASEMETALS_ORE_SAMPLE PASS dimension={} chunks=9 ores={}", dimension.location(), counts);
        }
    }

    private static void verifyTinkers() {
        var mode = zone.moddev.mc.basemetals.config.BaseMetalsConfig.activeMode();
        var registry = slimeknights.tconstruct.library.materials.MaterialRegistry.getInstance();
        for (String material : zone.moddev.mc.basemetals.material.MaterialCatalogue.BY_NAME.keySet()) {
            var id = new slimeknights.tconstruct.library.materials.definition.MaterialId("basemetals:" + material);
            for (String stat : new String[] {"head", "handle", "limb", "grip"}) {
                boolean expected = zone.moddev.mc.basemetals.material.MaterialCatalogue.get(material).hasEquipment()
                        && zone.moddev.mc.basemetals.compat.TinkersContentPolicy.allows(mode, material,
                                "tconstruct:" + stat);
                if ("steel".equals(material) && Boolean.getBoolean("basemetals.tinkersOverrideSmoke")) expected = true;
                boolean present = registry.getMaterialStats(id,
                        new slimeknights.tconstruct.library.materials.stats.MaterialStatsId("tconstruct:" + stat)).isPresent();
                if (present != expected) throw new IllegalStateException("Tinkers " + id + "/" + stat
                        + " is present=" + present + " in " + mode);
            }
        }
        LOGGER.info("BASEMETALS_PACKAGED_TINKERS_POLICY PASS mode={}", mode.serializedName());
        if (Boolean.getBoolean("basemetals.tinkersOverrideSmoke")) {
            LOGGER.info("BASEMETALS_TINKERS_OVERRIDE PASS material=steel mode={}", mode.serializedName());
        }
    }
}
