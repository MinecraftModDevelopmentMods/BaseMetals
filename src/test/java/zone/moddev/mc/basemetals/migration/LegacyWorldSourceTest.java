package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

class LegacyWorldSourceTest {
    @TempDir
    Path temporaryWorld;

    @Test
    void protectsSavedChunksWithoutMixingDimensions() throws IOException {
        String[] directories = {"region", "DIM-1/region", "DIM1/region", "dimensions/example/nested/cavern/region"};
        String[] dimensions = {"minecraft:overworld", "minecraft:the_nether", "minecraft:the_end", "example:nested/cavern"};
        for (int index = 0; index < directories.length; index++) {
            Path directory = Files.createDirectories(temporaryWorld.resolve(directories[index]));
            byte[] locations = new byte[4096];
            locations[index * 4 + 3] = 1;
            Files.write(directory.resolve("r.0.0.mca"), locations);
        }

        assertEquals(4, LegacyWorldDataHook.indexLegacyChunks(temporaryWorld.toFile()));
        for (int dimension = 0; dimension < dimensions.length; dimension++) {
            for (int chunk = 0; chunk < directories.length; chunk++) {
                assertEquals(dimension == chunk, LegacyWorldDataHook.isProtectedChunk(
                        new net.minecraft.resources.ResourceLocation(dimensions[dimension]),
                        new net.minecraft.core.BlockPos(chunk * 16, 64, 0)));
            }
        }
        assertTrue(!LegacyWorldDataHook.isProtectedChunk(new net.minecraft.resources.ResourceLocation("example:new"),
                new net.minecraft.core.BlockPos(0, 64, 0)));
    }

    @Test
    void removesOnlyPhantomBedEntitiesOnBaseMetalsBlocks() {
        ListTag palette = new ListTag();
        for (String name : new String[] {"basemetals:tin_ore", "minecraft:red_bed", "anothermod:ore"}) {
            CompoundTag state = new CompoundTag();
            state.putString("Name", name);
            palette.add(state);
        }
        net.minecraft.util.BitStorage states = new net.minecraft.util.BitStorage(4, 4096);
        states.set(0x112, 1);
        states.set(0x113, 2);
        CompoundTag section = new CompoundTag();
        section.putByte("Y", (byte) 0);
        section.put("Palette", palette);
        section.putLongArray("BlockStates", states.getRaw());
        ListTag sections = new ListTag();
        sections.add(section);
        ListTag entities = new ListTag();
        for (int x = 1; x <= 3; x++) {
            CompoundTag bed = new CompoundTag();
            bed.putString("id", "minecraft:bed");
            bed.putInt("x", x);
            bed.putInt("y", 1);
            bed.putInt("z", 1);
            entities.add(bed);
        }
        CompoundTag chest = entities.getCompound(0).copy();
        chest.putString("id", "minecraft:chest");
        entities.add(chest);
        CompoundTag level = new CompoundTag();
        level.put("Sections", sections);
        level.put("TileEntities", entities);

        assertEquals(1, LegacyWorldDataHook.removePhantomBedEntities(level));
        assertEquals(3, entities.size());
        assertEquals(2, entities.getCompound(0).getInt("x"));
        assertEquals("minecraft:chest", entities.getCompound(2).getString("id"));
        assertEquals(0, LegacyWorldDataHook.removePhantomBedEntities(level));
    }

    @Test
    void removesOnlyTheRetiredVanillaProfessionIndex() {
        CompoundTag root = professionSnapshot("minecraft:smith");
        CompoundTag entity = new CompoundTag();
        entity.putInt("Profession", 3);
        root.put("VillagerProof", entity);

        assertTrue(LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(root));
        assertTrue(!root.getCompound("fml").getCompound("Registries")
                .contains("minecraft:villagerprofessions"));
        assertEquals(3, root.getCompound("VillagerProof").getInt("Profession"));
        assertTrue(!LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(root));

        CompoundTag custom = professionSnapshot("anothermod:smith");
        assertTrue(!LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(custom));
        assertTrue(custom.getCompound("fml").getCompound("Registries")
                .contains("minecraft:villagerprofessions"));

        CompoundTag blocked = professionSnapshot("minecraft:smith");
        blocked.getCompound("fml").getCompound("Registries")
                .getCompound("minecraft:villagerprofessions").putIntArray("blocked", new int[] {7});
        assertTrue(!LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(blocked));

        CompoundTag aliased = professionSnapshot("minecraft:smith");
        ListTag aliases = new ListTag();
        aliases.add(StringTag.valueOf("anothermod:smith"));
        aliased.getCompound("fml").getCompound("Registries")
                .getCompound("minecraft:villagerprofessions").put("aliases", aliases);
        assertTrue(!LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(aliased));

        CompoundTag malformed = professionSnapshot("minecraft:smith");
        malformed.getCompound("fml").getCompound("Registries")
                .getCompound("minecraft:villagerprofessions").put("ids", aliases);
        assertTrue(!LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(malformed));
    }

    private static CompoundTag professionSnapshot(String name) {
        CompoundTag entry = new CompoundTag();
        entry.putString("K", name);
        entry.putInt("V", 3);
        ListTag ids = new ListTag();
        ids.add(entry);
        CompoundTag snapshot = new CompoundTag();
        snapshot.put("ids", ids);
        CompoundTag registries = new CompoundTag();
        registries.put("minecraft:villagerprofessions", snapshot);
        CompoundTag fml = new CompoundTag();
        fml.put("Registries", registries);
        CompoundTag root = new CompoundTag();
        root.put("fml", fml);
        return root;
    }

    @Test
    void usesTheUntouchedLegacyRegistryAfterForgeRewritesLevelDat() throws IOException {
        writeLevel("level.dat", 2_975, 1, "minecraft:stone");
        writeLevel("level.dat_old", 512, 1_976, "basemetals:copper_ore");

        Map<Integer, String> ids = LegacyWorldDataHook.legacyBlockIdsForTest(temporaryWorld);

        assertEquals("basemetals:copper_ore", ids.get(1_976));
    }

    @Test
    void normalizesRegistryPathsThatWereLegalInMinecraft110() {
        assertEquals("mca:rosegoldore",
                LegacyWorldDataHook.normalizeLegacyRegistryName("mca:RoseGoldOre"));
        assertEquals("basemetals:copper_ore",
                LegacyWorldDataHook.normalizeLegacyRegistryName("basemetals:copper_ore"));
    }

    @Test
    void targetsNative117ChunkLoadingWithoutTheOldLeavesPatch() throws IOException {
        String coremod = new String(Files.readAllBytes(Paths.get(
                "src/main/resources/coremods/basemetals_117_compatibility.js")), "UTF-8");
        assertTrue(coremod.contains("net.minecraft.world.level.chunk.storage.ChunkStorage"));
        assertTrue(coremod.contains("Ljava/util/function/Supplier;"));
        assertTrue(coremod.contains("Lcom/mojang/serialization/DynamicOps;"));
        assertTrue(coremod.contains("Lnet/minecraft/resources/ResourceKey;"));
        assertTrue(coremod.contains("'(Lnet/minecraft/core/BlockPos;)Z'"));
        assertTrue(!coremod.contains("leaves_fixer"));
        assertTrue(!coremod.contains("fluid_renderer"));
    }

    @Test
    void convertsOnlyOldBaseMetalsWallConnections() {
        CompoundTag state = new CompoundTag();
        state.putString("Name", "basemetals:steel_wall");
        CompoundTag properties = new CompoundTag();
        properties.putString("north", "true");
        properties.putString("east", "false");
        properties.putString("south", "tall");
        properties.putString("west", "low");
        properties.putString("up", "true");
        state.put("Properties", properties);

        assertEquals(2, LegacyWorldDataHook.migrateWallStates(state));
        assertEquals("low", properties.getString("north"));
        assertEquals("none", properties.getString("east"));
        assertEquals("tall", properties.getString("south"));
        assertEquals("low", properties.getString("west"));
        assertEquals("true", properties.getString("up"));
        assertEquals(0, LegacyWorldDataHook.migrateWallStates(state));

        state.putString("Name", "anothermod:steel_wall");
        properties.putString("north", "true");
        assertEquals(0, LegacyWorldDataHook.migrateWallStates(state));
        assertEquals("true", properties.getString("north"));
    }

    @Test
    void preservesAChunkWhoseOnlyLegacyBaseMetalsContentIsInAContainer() throws Exception {
        Field active = LegacyWorldDataHook.class.getDeclaredField("legacyWorldActive");
        active.setAccessible(true);
        boolean previous = active.getBoolean(null);
        try {
            active.setBoolean(null, true);
            CompoundTag stack = new CompoundTag();
            stack.putString("id", "basemetals:carbon_powder");
            stack.putByte("Count", (byte) 1);
            ListTag items = new ListTag();
            items.add(stack);
            CompoundTag chest = new CompoundTag();
            chest.putString("id", "Chest");
            chest.put("Items", items);
            ListTag tileEntities = new ListTag();
            tileEntities.add(chest);
            CompoundTag level = new CompoundTag();
            level.putInt("xPos", 12);
            level.putInt("zPos", -7);
            level.put("TileEntities", tileEntities);
            CompoundTag root = new CompoundTag();
            root.put("Level", level);

            LegacyWorldDataHook.prepareLegacyChunk(root);
            CompoundTag finalized = LegacyWorldDataHook.finalizeLegacyChunk(root);

            assertEquals("full", finalized.getCompound("Level").getString("Status"));
            CompoundTag migratedChest = finalized.getCompound("Level")
                    .getList("TileEntities", 10).getCompound(0);
            assertEquals("minecraft:chest", migratedChest.getString("id"));
            assertEquals("basemetals:coal_powder",
                    migratedChest.getList("Items", 10).getCompound(0).getString("id"));
        } finally {
            active.setBoolean(null, previous);
        }
    }

    private void writeLevel(String name, int dataVersion, int blockId, String blockName) throws IOException {
        CompoundTag entry = new CompoundTag();
        entry.putInt("V", blockId);
        entry.putString("K", blockName);
        ListTag ids = new ListTag();
        ids.add(entry);

        CompoundTag blocks = new CompoundTag();
        blocks.put("ids", ids);
        CompoundTag registries = new CompoundTag();
        registries.put("minecraft:blocks", blocks);
        CompoundTag fml = new CompoundTag();
        fml.put("Registries", registries);
        CompoundTag data = new CompoundTag();
        data.putInt("DataVersion", dataVersion);
        CompoundTag level = new CompoundTag();
        level.put("FML", fml);
        level.put("Data", data);

        try (FileOutputStream output = new FileOutputStream(temporaryWorld.resolve(name).toFile())) {
            NbtIo.writeCompressed(level, output);
        }
    }
}
