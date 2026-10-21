package review;

import com.google.gson.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod(modid="legacy110review", name="Legacy 1.10 review", version="1", serverSideOnly=true,
     acceptableRemoteVersions="*", dependencies="required-after:basemetals;after:poweradvantage;after:steamadvantage;after:electricadvantage")
public final class Legacy110Probe {
    @Mod.EventHandler
    public void started(FMLServerStartedEvent event) throws Exception {
        if (System.getProperty("golem.phase") != null) return;
        Object server = FMLCommonHandler.instance().getMinecraftServerInstance();
        Object world = call(server, new String[]{"func_71218_a", "worldServerForDimension"}, new Class[]{int.class}, 0);

        if (System.getProperty("review.chunks") != null) {
            visitChunks(server);
            call(server, new String[]{"func_71267_a","saveAllWorlds"},new Class[]{boolean.class},false);
            System.out.println("LEGACY110_WORLD_VISITS_COMPLETE phase=" + System.getProperty("review.phase"));
            call(server,new String[]{"func_71263_m","initiateShutdown"},new Class[0]);
            return;
        }

        JsonObject manifest;
        try (Reader reader = Files.newBufferedReader(Paths.get(System.getProperty("review.manifest")), StandardCharsets.UTF_8)) {
            manifest = new JsonParser().parse(reader).getAsJsonObject();
        }
        String phase = System.getProperty("review.phase");
        JsonObject result = new JsonObject();
        JsonArray checked = new JsonArray();
        result.addProperty("phase", phase);
        result.add("items", checked);
        int missing = 0, changedData = 0, changedIdentity = 0;
        for (JsonElement element : manifest.getAsJsonArray("items")) {
            JsonObject expected = element.getAsJsonObject();
            int x=expected.get("chest_x").getAsInt(), z=expected.get("chest_z").getAsInt();
            if ((phase.equals("first") || phase.equals("reload")) && (x >> 4) == -64 && (z >> 4) == 64) continue;
            BlockPos pos = new BlockPos(x, expected.get("chest_y").getAsInt(), z);
            Object chest = call(world, new String[]{"func_175625_s", "getTileEntity"}, new Class[]{BlockPos.class}, pos);
            if (chest == null) throw new AssertionError("Missing fixture chest " + pos);
            Object stack = call(chest, new String[]{"func_70301_a", "getStackInSlot"}, new Class[]{int.class}, expected.get("slot").getAsInt());
            JsonObject actual = new JsonObject();
            actual.addProperty("old", expected.get("id").getAsString());
            if (stack == null) {
                actual.addProperty("missing", true);
                missing++;
            } else {
                Object item = call(stack, new String[]{"func_77973_b", "getItem"}, new Class[0]);
                String id = String.valueOf(item.getClass().getMethod("getRegistryName").invoke(item));
                actual.addProperty("id", id);

                String expectedId = expected.get("id").getAsString();
                if (expectedId.endsWith("_door_item")) {
                    expectedId = expectedId.substring(0, expectedId.length() - 5);
                } else if (expectedId.equals("basemetals:liquid_mercury")) {
                    expectedId = "basemetals:mercury";
                } else if (expectedId.equals("basemetals:carbon_powder")) {
                    expectedId = "basemetals:coal_powder";
                }

                if (!id.equals(expectedId)) changedIdentity++;
                int count = integerField(stack, "field_77994_a", "stackSize");
                int damage = (Integer) call(stack, new String[]{"func_77952_i", "getItemDamage"}, new Class[0]);
                Object tag = call(stack, new String[]{"func_77978_p", "getTagCompound"}, new Class[0]);
                actual.addProperty("count", count);
                actual.addProperty("damage", damage);
                actual.addProperty("tag_snbt", String.valueOf(tag));
                boolean preserved = count == expected.get("count").getAsInt()
                        && damage == expected.get("damage").getAsInt()
                        && String.valueOf(tag).equals(expected.get("tag_snbt").getAsString());
                actual.addProperty("dataPreserved", preserved);
                if (!preserved) changedData++;
            }
            checked.add(actual);
        }
        JsonObject mods = new JsonObject();
        for (String id : new String[]{"basemetals","poweradvantage","steamadvantage","electricadvantage"}) {
            ModContainer mod = Loader.instance().getIndexedModList().get(id);
            mods.addProperty(id, mod == null ? "MISSING" : mod.getVersion());
        }
        result.add("mods", mods);
        result.add("humanDetector", verifyHumanDetector(world));
        if (phase.equals("delayed") || phase.equals("stable")) {
            JsonArray blockChanges = new JsonArray();
            int states = 0;
            for (JsonElement blockElement : manifest.getAsJsonArray("blocks")) {
                JsonObject entry = blockElement.getAsJsonObject();
                String oldId = entry.get("id").getAsString();
                String expectedId = oldId.equals("basemetals:liquid_mercury") ? "basemetals:mercury" : oldId;
                for (JsonElement stateElement : entry.getAsJsonArray("states")) {
                    JsonObject saved = stateElement.getAsJsonObject();
                    Object dimension = call(server,new String[]{"func_71218_a","worldServerForDimension"},
                            new Class[]{int.class},saved.get("dimension").getAsInt());
                    BlockPos pos = new BlockPos(saved.get("x").getAsInt(),saved.get("y").getAsInt(),saved.get("z").getAsInt());
                    Object state = call(dimension,new String[]{"func_180495_p","getBlockState"},new Class[]{BlockPos.class},pos);
                    Object block = call(state,new String[]{"func_177230_c","getBlock"},new Class[0]);
                    String id = String.valueOf(block.getClass().getMethod("getRegistryName").invoke(block));
                    int meta = (Integer)call(block,new String[]{"func_176201_c","getMetaFromState"},
                            new Class[]{Class.forName("net.minecraft.block.state.IBlockState")},state);
                    int expectedMeta = saved.get("saved_metadata").getAsInt();
                    if (!id.equals(expectedId) || meta != expectedMeta) {
                        JsonObject change = new JsonObject();
                        change.addProperty("old",oldId); change.addProperty("id",id);
                        change.addProperty("expectedMeta",expectedMeta); change.addProperty("meta",meta);
                        change.addProperty("position",pos.toString()); change.addProperty("dimension",saved.get("dimension").getAsInt());
                        blockChanges.add(change);
                    }
                    states++;
                }
            }
            result.addProperty("blockStatesChecked",states);
            result.add("blockChanges",blockChanges);
        }
        result.addProperty("missing", missing);
        result.addProperty("changedData", changedData);
        result.addProperty("changedIdentity", changedIdentity);
        boolean passed = missing == 0 && changedData == 0 && changedIdentity == 0
                && (!result.has("blockChanges") || result.getAsJsonArray("blockChanges").size() == 0);
        result.addProperty("passed", passed);
        try (Writer writer=Files.newBufferedWriter(Paths.get(phase+"-review.json"),StandardCharsets.UTF_8)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(result,writer);
        }
        call(server, new String[]{"func_71267_a","saveAllWorlds"},new Class[]{boolean.class},false);
        System.out.println("LEGACY110_" + (passed ? "PASS" : "FAIL") + " phase="+phase+" checked="+checked.size()+" missing="+missing+" changedData="+changedData);
        call(server,new String[]{"func_71263_m","initiateShutdown"},new Class[0]);
    }

    static void visitChunks(Object server) throws Exception {
        int visited = 0;
        for (String line : Files.readAllLines(Paths.get(System.getProperty("review.chunks")), StandardCharsets.UTF_8)) {
            if (line.trim().isEmpty()) continue;
            String[] point = line.split(",");
            Object dimension = call(server, new String[]{"func_71218_a", "worldServerForDimension"},
                    new Class[]{int.class}, Integer.parseInt(point[0]));
            Object chunk = call(dimension, new String[]{"func_72964_e", "getChunkFromChunkCoords"},
                    new Class[]{int.class, int.class}, Integer.parseInt(point[1]), Integer.parseInt(point[2]));
            call(chunk, new String[]{"func_76630_e", "setChunkModified"}, new Class[0]);
            visited++;
        }
        if (visited == 0) throw new AssertionError("No migration chunks were visited");
        System.out.println("LEGACY110_CHUNKS visited=" + visited);
    }

    static JsonObject verifyHumanDetector(Object world) throws Exception {
        ResourceLocation name = new ResourceLocation("basemetals", "human_detector");
        Object detector = ForgeRegistries.BLOCKS.getValue(name);
        Object item = ForgeRegistries.ITEMS.getValue(name);
        if (!ForgeRegistries.BLOCKS.containsKey(name) || !ForgeRegistries.ITEMS.containsKey(name)) {
            throw new AssertionError("The Human Detector block and item must both be registered");
        }
        Object itemBlock = call(item, new String[]{"func_179223_d", "getBlock"}, new Class[0]);
        if (itemBlock != detector) throw new AssertionError("Human Detector item places another block");

        Class<?> worldType = Class.forName("net.minecraft.world.World");
        Class<?> entityType = Class.forName("net.minecraft.entity.Entity");
        Method strength = null;
        for (Method method : detector.getClass().getDeclaredMethods()) {
            if (method.getReturnType() == int.class
                    && Arrays.equals(method.getParameterTypes(), new Class[]{worldType, BlockPos.class})) {
                strength = method;
                strength.setAccessible(true);
            }
        }
        if (strength == null) throw new AssertionError("Missing player detection method");

        BlockPos pos = new BlockPos(400, 230, 400);
        call(world, new String[]{"func_72964_e", "getChunkFromChunkCoords"}, new Class[]{int.class, int.class}, 25, 25);
        Object zombie = Class.forName("net.minecraft.entity.monster.EntityZombie")
                .getConstructor(worldType).newInstance(world);
        Object profile = Class.forName("com.mojang.authlib.GameProfile")
                .getConstructor(UUID.class, String.class).newInstance(UUID.fromString("ff5b37c5-5351-4617-aa26-8e67005dc684"), "DetectorTest");
        Object player = Class.forName("net.minecraftforge.common.util.FakePlayer")
                .getConstructor(Class.forName("net.minecraft.world.WorldServer"), profile.getClass()).newInstance(world, profile);
        JsonObject result = new JsonObject();
        Class<?> stateType = Class.forName("net.minecraft.block.state.IBlockState");
        BlockPos below = new BlockPos(400, 229, 400);
        Object oldPlate = call(world, new String[]{"func_180495_p", "getBlockState"}, new Class[]{BlockPos.class}, pos);
        Object oldFloor = call(world, new String[]{"func_180495_p", "getBlockState"}, new Class[]{BlockPos.class}, below);

        try {
            Object stone = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft", "stone"));
            Object floor = call(stone, new String[]{"func_176223_P", "getDefaultState"}, new Class[0]);
            Object plate = call(detector, new String[]{"func_176223_P", "getDefaultState"}, new Class[0]);
            call(world, new String[]{"func_180501_a", "setBlockState"}, new Class[]{BlockPos.class, stateType, int.class}, below, floor, 2);
            call(world, new String[]{"func_180501_a", "setBlockState"}, new Class[]{BlockPos.class, stateType, int.class}, pos, plate, 2);
            int empty = (Integer) strength.invoke(detector, world, pos);
            call(zombie, new String[]{"func_70107_b", "setPosition"}, new Class[]{double.class, double.class, double.class}, 400.5, 230.0, 400.5);
            call(world, new String[]{"func_72838_d", "spawnEntityInWorld"}, new Class[]{entityType}, zombie);
            int mob = (Integer) strength.invoke(detector, world, pos);
            call(detector, new String[]{"func_180634_a", "onEntityCollidedWithBlock"}, new Class[]{worldType, BlockPos.class, stateType, entityType}, world, pos, plate, zombie);
            int mobPower = power(detector, world, pos);
            call(player, new String[]{"func_70107_b", "setPosition"}, new Class[]{double.class, double.class, double.class}, 400.5, 230.0, 400.5);
            call(world, new String[]{"func_72838_d", "spawnEntityInWorld"}, new Class[]{entityType}, player);
            int human = (Integer) strength.invoke(detector, world, pos);
            call(detector, new String[]{"func_180634_a", "onEntityCollidedWithBlock"}, new Class[]{worldType, BlockPos.class, stateType, entityType}, world, pos, plate, player);
            int humanPower = power(detector, world, pos);
            call(player, new String[]{"func_70107_b", "setPosition"}, new Class[]{double.class, double.class, double.class}, 403.5, 230.0, 400.5);
            int released = (Integer) strength.invoke(detector, world, pos);
            Object pressed = call(world, new String[]{"func_180495_p", "getBlockState"}, new Class[]{BlockPos.class}, pos);
            call(detector, new String[]{"func_180650_b", "updateTick"}, new Class[]{worldType, BlockPos.class, stateType, Random.class}, world, pos, pressed, new Random(0));
            int releasedPower = power(detector, world, pos);

            result.addProperty("empty", empty);
            result.addProperty("mob", mob);
            result.addProperty("player", human);
            result.addProperty("released", released);
            result.addProperty("mobPower", mobPower);
            result.addProperty("playerPower", humanPower);
            result.addProperty("releasedPower", releasedPower);
            if (empty != 0 || mob != 0 || human != 15 || released != 0
                    || mobPower != 0 || humanPower != 15 || releasedPower != 0) {
                throw new AssertionError("Incorrect Human Detector strengths: " + result);
            }
        } finally {
            call(world, new String[]{"func_72900_e", "removeEntity"}, new Class[]{entityType}, player);
            call(world, new String[]{"func_72900_e", "removeEntity"}, new Class[]{entityType}, zombie);
            call(world, new String[]{"func_180501_a", "setBlockState"}, new Class[]{BlockPos.class, stateType, int.class}, pos, oldPlate, 2);
            call(world, new String[]{"func_180501_a", "setBlockState"}, new Class[]{BlockPos.class, stateType, int.class}, below, oldFloor, 2);
        }
        return result;
    }

    static int power(Object detector, Object world, BlockPos pos) throws Exception {
        Object state = call(world, new String[]{"func_180495_p", "getBlockState"}, new Class[]{BlockPos.class}, pos);
        Class<?> facing = Class.forName("net.minecraft.util.EnumFacing");
        Object up = Enum.valueOf((Class) facing, "UP");
        return (Integer) call(detector, new String[]{"func_180656_a", "getWeakPower"},
                new Class[]{Class.forName("net.minecraft.block.state.IBlockState"), Class.forName("net.minecraft.world.IBlockAccess"), BlockPos.class, facing}, state, world, pos, up);
    }
    static Object call(Object object, String[] names, Class[] types, Object... args) throws Exception {
        for(String name:names) {
            try {Method method=object.getClass().getMethod(name,types); method.setAccessible(true); return method.invoke(object,args);}
            catch(NoSuchMethodException ignored) {}
        }
        throw new NoSuchMethodException(object.getClass()+" "+Arrays.toString(names));
    }
    static int integerField(Object object,String...names) throws Exception {
        for(String name:names) {
            try {return object.getClass().getField(name).getInt(object);}
            catch(NoSuchFieldException ignored) {}
        }
        throw new NoSuchFieldException(Arrays.toString(names));
    }
}
