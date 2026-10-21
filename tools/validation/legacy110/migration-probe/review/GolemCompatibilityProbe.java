package review;

import static review.Legacy110Probe.call;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.eventhandler.Event;

/** Checks the unmodified Golems add-on against saved entities and current materials. */
@Mod(modid = "basemetalsgolemprobe", name = "Base Metals golem checks", version = "1",
        serverSideOnly = true, acceptableRemoteVersions = "*",
        dependencies = "required-after:basemetals;after:golems_metal;after:legacy110review")
public final class GolemCompatibilityProbe {
    private static final String[] MATERIALS = { "adamantine", "aquarium", "brass", "bronze",
            "coldiron", "copper", "cupronickel", "electrum", "invar", "lead", "mithril",
            "nickel", "platinum", "silver", "starsteel", "steel", "tin", "zinc" };
    private static final String[] CLASSES = { "Adamantine", "Aquarium", "Brass", "Bronze",
            "ColdIron", "Copper", "Cupronickel", "Electrum", "Invar", "Lead", "Mithril",
            "Nickel", "Platinum", "Silver", "StarSteel", "Steel", "Tin", "Zinc" };

    @Mod.EventHandler
    public void started(final FMLServerStartedEvent event) throws Exception {
        final String phase = System.getProperty("golem.phase");
        if (phase == null) return;

        final Object server = FMLCommonHandler.instance().getMinecraftServerInstance();
        final Object world = call(server, new String[]{"func_71218_a", "worldServerForDimension"}, new Class[]{int.class}, 0);
        final JsonObject report = new JsonObject();
        final JsonArray errors = new JsonArray();
        report.addProperty("phase", phase);
        report.addProperty("addonPresent", Loader.isModLoaded("golems_metal"));

        try {
            if ("absent".equals(phase)) {
                require(!Loader.isModLoaded("golems_metal"), "Golems add-on was not excluded");
                report.add("humanDetector", Legacy110Probe.verifyHumanDetector(world));
                verifyBridge();
            } else if ("seed".equals(phase)) {
                seed(world, 14, 13, "near");
                seed(world, 154, -504, "later");
                report.addProperty("seeded", 36);
            } else {
                require(Loader.isModLoaded("golems_metal"), "Missing unmodified Golems add-on");
                verifyBridge();
                report.addProperty("constructedAndBuilt", verifyConstruction(world));
                report.addProperty("nearEntities", verifySaved(world, 14, 13, "near"));
                if ("delayed".equals(phase) || "stable".equals(phase)) {
                    report.addProperty("laterEntities", verifySaved(world, 154, -504, "later"));
                }
            }

            if (System.getProperty("review.chunks") != null) {
                Legacy110Probe.visitChunks(server);
            }
            if (Boolean.getBoolean("golem.checkMmdApi")) {
                verifyMmdApi();
                report.addProperty("oldMmdApi", true);
            }
        } catch (final Throwable failure) {
            final Throwable cause = failure instanceof InvocationTargetException ? failure.getCause() : failure;
            errors.add(new JsonPrimitive(cause.toString()));
            cause.printStackTrace();
        }

        report.add("errors", errors);
        report.addProperty("passed", errors.size() == 0);
        try (Writer writer = Files.newBufferedWriter(Paths.get(phase + "-golems.json"), StandardCharsets.UTF_8)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(report, writer);
        }

        call(server, new String[]{"func_71267_a", "saveAllWorlds"}, new Class[]{boolean.class}, false);
        System.out.println("BASE_METALS_GOLEMS_" + (errors.size() == 0 ? "PASS" : "FAIL") + " phase=" + phase);
        call(server, new String[]{"func_71263_m", "initiateShutdown"}, new Class[0]);
    }

    private static void verifyBridge() throws Exception {
        final Class<?> blocks = Class.forName("cyano.basemetals.init.Blocks");
        final Class<?> items = Class.forName("cyano.basemetals.init.Items");

        for (final String material : MATERIALS) {
            require(blocks.getField(material + "_block").get(null) == ForgeRegistries.BLOCKS.getValue(id(material, "block")),
                    material + " has a duplicate or missing block");
            require(items.getField(material + "_ingot").get(null) == ForgeRegistries.ITEMS.getValue(id(material, "ingot")),
                    material + " has a duplicate or missing ingot");
        }
    }

    private static void verifyMmdApi() throws Exception {
        final Class<?> consumer = Class.forName("review.LegacyMmdConsumer");
        final Class<?> current = Class.forName("zone.moddev.mc.basemetals.BaseMetals");
        require(consumer.getMethod("literal").invoke(null) == current, "Old MMD class literal has another identity");
        require(consumer.getMethod("reflected").invoke(null) == current, "Old MMD reflection name has another identity");
        require(consumer.getMethod("instance").invoke(null) == current.getField("instance").get(null), "Duplicate mod instance");
        require(consumer.getSuperclass() == Class.forName("zone.moddev.mc.basemetals.proxy.CommonProxy"), "Old MMD inheritance was not relinked");
        final Class<?> materials = Class.forName("zone.moddev.mc.basemetals.init.Materials");
        require(consumer.getMethod("material").invoke(null) == materials.getMethod("getMaterialByName", String.class).invoke(null, "copper"), "Duplicate material registry");
    }

    private static int verifyConstruction(final Object world) throws Exception {
        final Class<?> worldType = Class.forName("net.minecraft.world.World");
        final Class<?> stateType = Class.forName("net.minecraft.block.state.IBlockState");
        final BlockPos head = new BlockPos(400, 233, 400);
        final BlockPos[] body = {new BlockPos(400, 232, 400), new BlockPos(400, 231, 400),
                new BlockPos(399, 232, 400), new BlockPos(401, 232, 400)};
        final Object[] previous = new Object[body.length];
        call(world, new String[]{"func_72964_e", "getChunkFromChunkCoords"}, new Class[]{int.class, int.class}, 25, 25);

        for (int i = 0; i < body.length; i++) {
            previous[i] = call(world, new String[]{"func_180495_p", "getBlockState"}, new Class[]{BlockPos.class}, body[i]);
        }

        try {
            for (int i = 0; i < MATERIALS.length; i++) {
                final String material = MATERIALS[i];
                final Object block = ForgeRegistries.BLOCKS.getValue(id(material, "block"));
                final Object state = call(block, new String[]{"func_176223_P", "getDefaultState"}, new Class[0]);
                for (final BlockPos pos : body) {
                    call(world, new String[]{"func_180501_a", "setBlockState"}, new Class[]{BlockPos.class, stateType, int.class}, pos, state, 2);
                }

                final Object event = Class.forName("com.golems.events.GolemBuildEvent")
                        .getConstructor(worldType, BlockPos.class, boolean.class).newInstance(world, head, true);
                MinecraftForge.EVENT_BUS.post((Event) event);
                final Object built = event.getClass().getMethod("getGolem").invoke(event);
                require(built != null && built.getClass() == golemClass(i), "Wrong constructed golem for " + material);
                require(!(Boolean) event.getClass().getMethod("isGolemBanned").invoke(event), "Default golem is banned: " + material);
                verifyMaterial(built, material);
            }
        } finally {
            for (int i = 0; i < body.length; i++) {
                call(world, new String[]{"func_180501_a", "setBlockState"}, new Class[]{BlockPos.class, stateType, int.class}, body[i], previous[i], 2);
            }
        }
        return MATERIALS.length;
    }

    private static void verifyMaterial(final Object golem, final String material) throws Exception {
        final Object ingot = ForgeRegistries.ITEMS.getValue(id(material, "ingot"));
        final Object returned = golem.getClass().getMethod("getCreativeReturn").invoke(golem);
        final Object returnedItem = call(returned, new String[]{"func_77973_b", "getItem"}, new Class[0]);
        require(returnedItem == ForgeRegistries.ITEMS.getValue(id(material, "block")), "Wrong golem block: " + material);

        final List<Object> drops = new ArrayList<>();
        golem.getClass().getMethod("addGolemDrops", List.class, boolean.class, int.class).invoke(golem, drops, false, 0);
        require(drops.size() == 1, "Wrong golem drops: " + material);
        require(drops.get(0).getClass().getField("item").get(drops.get(0)) == ingot, "Wrong dropped ingot: " + material);
    }

    private static void seed(final Object world, final int chunkX, final int chunkZ, final String group) throws Exception {
        final Object chunk = call(world, new String[]{"func_72964_e", "getChunkFromChunkCoords"}, new Class[]{int.class, int.class}, chunkX, chunkZ);
        final Class<?> entityType = Class.forName("net.minecraft.entity.Entity");

        for (int i = 0; i < MATERIALS.length; i++) {
            final Object golem = golemClass(i).getConstructor(Class.forName("net.minecraft.world.World")).newInstance(world);
            final String marker = "BM compatibility " + group + " " + MATERIALS[i];
            call(golem, new String[]{"func_96094_a", "setCustomNameTag"}, new Class[]{String.class}, marker);
            call(golem, new String[]{"func_94061_f", "setNoAI"}, new Class[]{boolean.class}, true);
            call(golem, new String[]{"func_189654_d", "setNoGravity"}, new Class[]{boolean.class}, true);
            call(golem, new String[]{"func_70107_b", "setPosition"}, new Class[]{double.class, double.class, double.class},
                    chunkX * 16.0 + 1.5 + (i % 4) * 3, 230.0, chunkZ * 16.0 + 1.5 + (i / 4) * 3);
            final float maximum = (Float) call(golem, new String[]{"func_110138_aP", "getMaxHealth"}, new Class[0]);
            call(golem, new String[]{"func_70606_j", "setHealth"}, new Class[]{float.class}, maximum - 1.5F);
            require((Boolean) call(world, new String[]{"func_72838_d", "spawnEntityInWorld"}, new Class[]{entityType}, golem), "Could not seed " + marker);
        }
        call(chunk, new String[]{"func_76630_e", "setChunkModified"}, new Class[0]);
    }

    private static int verifySaved(final Object world, final int chunkX, final int chunkZ, final String group) throws Exception {
        call(world, new String[]{"func_72964_e", "getChunkFromChunkCoords"}, new Class[]{int.class, int.class}, chunkX, chunkZ);
        final Class<?> boxType = Class.forName("net.minecraft.util.math.AxisAlignedBB");
        final Object box = boxType.getConstructor(double.class, double.class, double.class, double.class, double.class, double.class)
                .newInstance(chunkX * 16.0, 220.0, chunkZ * 16.0, chunkX * 16.0 + 16, 245.0, chunkZ * 16.0 + 16);
        final List<?> entities = (List<?>) call(world, new String[]{"func_72872_a", "getEntitiesWithinAABB"},
                new Class[]{Class.class, boxType}, Class.forName("net.minecraft.entity.Entity"), box);

        for (int i = 0; i < MATERIALS.length; i++) {
            final String marker = "BM compatibility " + group + " " + MATERIALS[i];
            Object match = null;
            for (final Object entity : entities) {
                final String name = (String) call(entity, new String[]{"func_95999_t", "getCustomNameTag"}, new Class[0]);
                if (marker.equals(name)) {
                    require(match == null, "Duplicate saved golem: " + marker);
                    match = entity;
                }
            }

            require(match != null && match.getClass() == golemClass(i), "Missing or changed saved golem: " + marker);
            verifyMaterial(match, MATERIALS[i]);
            final float maximum = (Float) call(match, new String[]{"func_110138_aP", "getMaxHealth"}, new Class[0]);
            final float health = (Float) call(match, new String[]{"func_110143_aJ", "getHealth"}, new Class[0]);
            require(health == maximum - 1.5F, "Health changed: " + marker);
        }
        return MATERIALS.length;
    }

    private static Class<?> golemClass(final int index) throws ClassNotFoundException {
        return Class.forName("com.golems_metal.entity.Entity" + CLASSES[index] + "Golem");
    }

    private static ResourceLocation id(final String material, final String form) {
        return new ResourceLocation("basemetals", material + "_" + form);
    }

    private static void require(final boolean condition, final String message) {
        if (!condition) throw new AssertionError(message);
    }
}
