package review.worldgen;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.LinkedHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.DimensionManager;
import zone.moddev.mc.orespawn.api.OreSpawnApi;
import zone.moddev.mc.orespawn.api.ProviderStatus;

/** Checks the effective OreSpawn profile, not just successful mod loading. */
@Mod(modid = "basemetalsworldgenprobe", name = "Base Metals worldgen checks", version = "1",
        serverSideOnly = true, acceptableRemoteVersions = "*", dependencies = "required-after:basemetals")
public final class OreSpawn110Probe {
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        if (System.getProperty("worldgen.vanillaManaged") != null) VanillaTakeoverChecks.register();
    }

    @Mod.EventHandler
    public void started(final FMLServerStartedEvent event) throws Exception {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        final JsonObject report = new JsonObject();
        final String phase = System.getProperty("worldgen.phase", "first");
        report.addProperty("phase", phase);

        try {
            require("4.1.2.110021".equals(Loader.instance().getIndexedModList().get("orespawn").getVersion()),
                    "Wrong OreSpawn release");
            for (final String retired : new String[] {
                    "com.mcmoddev.orespawn.BaseMetalsOreSpawn",
                    "com.mcmoddev.lib.oregen.FallbackGenerator",
                    "com.mcmoddev.lib.oregen.FallbackGeneratorData" }) {
                try {
                    Class.forName(retired);
                    throw new AssertionError("Retired generation code is still packaged: " + retired);
                } catch (ClassNotFoundException expected) {
                    // These classes must no longer be shipped by Base Metals.
                }
            }

            require(OreSpawnApi.getProviderStatus("basemetals") == ProviderStatus.ACTIVE,
                    "Base Metals provider is not active");
            require(OreSpawnApi.isOreTakeoverActive("basemetals"), "OreSpawn is not managing Base Metals ores");
            final JsonObject profile = OreSpawnApi.getActiveProfile(server).get().toJson();
            report.add("profile", profile);

            final JsonObject expected = new JsonParser().parse(new String(Files.readAllBytes(
                    Paths.get(System.getProperty("worldgen.expected"))), StandardCharsets.UTF_8)).getAsJsonObject();
            assertSubset(expected, profile, "profile");
            final JsonObject ores = profile.getAsJsonObject("ores");
            int owned = 0;
            for (final Map.Entry<String, JsonElement> ore : ores.entrySet()) {
                if (ore.getKey().startsWith("basemetals:")) owned++;
                else require(!ore.getValue().toString().contains("basemetals:"),
                        "Base Metals ore was registered twice: " + ore.getKey());
            }
            require(owned == expected.getAsJsonObject("ores").entrySet().size(), "Unexpected Base Metals rule count");
            report.addProperty("baseMetalsRules", owned);

            final Field generators = GameRegistry.class.getDeclaredField("worldGenerators");
            generators.setAccessible(true);
            int oreSpawnGenerators = 0;
            for (final Object generator : (Iterable<?>) generators.get(null)) {
                final String name = generator.getClass().getName();
                require(!name.contains("FallbackGenerator"), "A fallback generator is registered");
                if (name.equals("zone.moddev.mc.orespawn.worldgen.OreSpawnWorldGenerator")) oreSpawnGenerators++;
            }
            require(oreSpawnGenerators == 1, "Expected exactly one OreSpawn generator");
            if (System.getProperty("worldgen.vanillaManaged") != null) {
                VanillaTakeoverChecks.check(server.worldServerForDimension(0), profile, report);
            }
            if (Boolean.getBoolean("worldgen.sample")) sampleOres(server, ores, report);
            if (Boolean.getBoolean("worldgen.savedMarker")) {
                checkSavedMarker(server.worldServerForDimension(0), new BlockPos(8, 80, 8));
                if (Boolean.getBoolean("worldgen.openRemote")) {
                    checkSavedMarker(server.worldServerForDimension(0), new BlockPos(1288, 80, -1272));
                }
            }
            report.addProperty("passed", true);
            System.out.println("BASE_METALS_OS41_PASS " + phase + " rules=" + owned);
            server.saveAllWorlds(false);
        } catch (final Throwable failure) {
            report.addProperty("passed", false);
            report.addProperty("failure", failure.toString());
            throw failure;
        } finally {
            Files.write(Paths.get(phase + "-worldgen.json"),
                    (new GsonBuilder().setPrettyPrinting().create().toJson(report) + "\n").getBytes(StandardCharsets.UTF_8));
            server.initiateShutdown();
        }
    }

    private static void sampleOres(final MinecraftServer server, final JsonObject ores, final JsonObject report) {
        final JsonObject samples = new JsonObject();
        for (final int dimension : new int[] { 0, -1, 1 }) {
            if (DimensionManager.getWorld(dimension) == null) DimensionManager.initDimension(dimension);
            final WorldServer world = server.worldServerForDimension(dimension);
            final String name = dimension == 0 ? "minecraft:overworld"
                    : dimension == -1 ? "minecraft:the_nether" : "minecraft:the_end";
            final Map<String, Integer> counts = new LinkedHashMap<>();
            for (int x = -3; x <= 3; x++) {
                for (int z = -3; z <= 3; z++) {
                    world.getChunkFromChunkCoords(x, z);
                }
            }

            final BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos();
            for (int cx = -2; cx <= 2; cx++) {
                for (int cz = -2; cz <= 2; cz++) {
                    final net.minecraft.world.chunk.Chunk chunk = world.getChunkFromChunkCoords(cx, cz);
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = 0; y < 256; y++) {
                                position.setPos(cx * 16 + x, y, cz * 16 + z);
                                final IBlockState state = chunk.getBlockState(position);
                                final Block block = state.getBlock();
                                if (!"basemetals".equals(block.getRegistryName().getResourceDomain())
                                        || !block.getRegistryName().getResourcePath().endsWith("_ore")) continue;

                                final String path = block.getRegistryName().getResourcePath();
                                final JsonObject ore = ores.getAsJsonObject("basemetals:legacy/" + path);
                                require(ore != null, "An undeclared ore generated: " + path);
                                final JsonObject dimensions = ore.getAsJsonObject("dimensions");
                                final JsonObject selectors = ore.getAsJsonObject("dimension_selectors");
                                final JsonObject placement = dimensions != null && dimensions.has(name)
                                        ? dimensions.getAsJsonObject(name)
                                        : dimension == 0 && selectors != null
                                        ? selectors.getAsJsonObject("orespawn:all_except_nether_end") : null;
                                require(placement != null && y >= placement.get("min_y").getAsInt()
                                        && y <= placement.get("max_y").getAsInt(),
                                        "Ore generated outside its dimension or height: " + path);
                                counts.put(path, counts.getOrDefault(path, 0) + 1);
                            }
                        }
                    }
                }
            }
            require(!counts.isEmpty(), "No Base Metals ores generated in " + name);
            final JsonObject sample = new JsonObject();
            counts.forEach(sample::addProperty);
            samples.add(name, sample);
        }
        report.add("oreSamples", samples);
    }

    private static void checkSavedMarker(final WorldServer world, final BlockPos position) {
        require("basemetals:copper_block".equals(world.getBlockState(position).getBlock().getRegistryName().toString()),
                "An existing copper block changed");
        final TileEntityChest chest = (TileEntityChest) world.getTileEntity(position.up());
        final ItemStack stack = chest.getStackInSlot(0);
        require(stack != null && stack.stackSize == 3
                && "basemetals:copper_ingot".equals(stack.getItem().getRegistryName().toString())
                && "FG7 saved copper".equals(stack.getDisplayName())
                && "kept across reload".equals(stack.getTagCompound()
                        .getCompoundTag("BaseMetalsBuildCheck").getString("Check")), "Saved item data changed");
        world.getChunkFromBlockCoords(position).setChunkModified();
    }

    private static void assertSubset(final JsonElement expected, final JsonElement actual, final String path) {
        require(actual != null, "Missing " + path);
        if (expected.isJsonObject()) {
            require(actual.isJsonObject(), "Not an object: " + path);
            for (final Map.Entry<String, JsonElement> entry : expected.getAsJsonObject().entrySet()) {
                assertSubset(entry.getValue(), actual.getAsJsonObject().get(entry.getKey()), path + "/" + entry.getKey());
            }
        } else {
            require(expected.equals(actual), "Changed " + path + ": expected " + expected + ", got " + actual);
        }
    }

    private static void require(final boolean condition, final String message) {
        if (!condition) throw new AssertionError(message);
    }
}
