package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.SharedConstants;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.chunk.storage.RegionFileVersion;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;

/** Checks the migration boundary without opening a player's save. */
class LegacyMigrationSafetyTest {
    @BeforeAll
    static void bootstrapRegistries() {
        SharedConstants.tryDetectVersion();
        net.minecraft.server.Bootstrap.bootStrap();
    }

    @TempDir(cleanup = CleanupMode.ON_SUCCESS)
    Path temporaryWorld;

    private static final String MARKER = "BASEMETALS_1_12_BLOCK_MIGRATION_COMPLETE.txt";
    private static final String VERSION = minecraftVersion();
    private static final int DATA_VERSION = dataVersion();

    @BeforeAll
    static void gameVersion() {
        SharedConstants.tryDetectVersion();
    }

    @Test
    void modernDeflateWorld() throws Exception {
        Path world = world("deflate", false);
        Path region = region(world, false, false);
        assertTrue(nativeAccepts(region, false));
        byte[] before = Files.readAllBytes(region);

        runEntry(world);

        assertArrayEquals(before, Files.readAllBytes(region));
        assertFalse(Files.exists(world.resolve(MARKER)));
    }

    @Test
    void modernEmptyRegion() throws Exception {
        Path world = world("empty", false);
        Path file = world.resolve("region/r.0.0.mca");
        Files.createDirectories(file.getParent());
        Files.createFile(file);
        assertTrue(nativeAccepts(file, true));
        long nativeLength = Files.size(file);

        runEntry(world);

        assertEquals(nativeLength, Files.size(file));
        assertFalse(Files.exists(world.resolve(MARKER)));
    }

    @Test
    void modernExternalChunk() throws Exception {
        Path world = world("external", false);
        Path file = region(world, true, false);
        boolean nativeAccepted = nativeAccepts(file, false);
        byte[] before = Files.readAllBytes(file);
        assertTrue(nativeAccepted);
        runEntry(world);
        assertFalse(Files.exists(world.resolve(MARKER)));
        assertArrayEquals(before, Files.readAllBytes(file));
    }

    @Test
    void modernLz4Chunk() throws Exception {
        assumeTrue(VERSION.equals("1.20.6") || VERSION.equals("1.21.11") || VERSION.equals("26.1.2"), "Native LZ4 is not available on older targets");
        Path world = world("lz4", false);
        Path file = region(world, false, true);
        assertTrue(nativeAccepts(file, false));
        byte[] before = Files.readAllBytes(file);
        runEntry(world);
        assertFalse(Files.exists(world.resolve(MARKER)));
        assertArrayEquals(before, Files.readAllBytes(file));

    }

    @Test
    void stalePreFlatteningBackupWithNoLegacyChunks() throws Exception {
        Path world = world("stale-backup", true);
        writeLevel(world.resolve("level.dat_old"), 1343, true, 3);
        byte[] before = Files.readAllBytes(world.resolve("level.dat"));
        runEntry(world);
        int experience = experience(world.resolve("level.dat"));
        assertEquals(37, experience);
        assertArrayEquals(before, Files.readAllBytes(world.resolve("level.dat")));
        assertFalse(Files.exists(world.resolve(MARKER)));
    }

    @Test
    void ordinaryModernBackupDoesNotReplacePlayer() throws Exception {
        Path world = world("modern-backup", true);
        writeLevel(world.resolve("level.dat_old"), DATA_VERSION, true, 3);
        runEntry(world);
        assertEquals(37, experience(world.resolve("level.dat")));
    }

    @Test
    void existingCompletionMarkerSkipsScanner() throws Exception {
        Path world = world("already-marked", true);
        writeLevel(world.resolve("level.dat_old"), 1343, true, 3);
        Path file = world.resolve("region/r.0.0.mca");
        Files.createDirectories(file.getParent());
        Files.createFile(file);
        Files.write(world.resolve(MARKER), Collections.singletonList("Existing marker"));
        runEntry(world);
        assertEquals(37, experience(world.resolve("level.dat")));
    }

    @Test
    void savedRegistryDoesNotMakeAnOldBackupTheCurrentPlayer() throws Exception {
        Path world = world("saved-registry", true);
        writeLevel(world.resolve("level.dat_old"), 1343, true, 3);
        writeRegistry(world.resolve("basemetals-legacy-registry.dat"));
        Path region = region(world, true, false);
        byte[] regionBefore = Files.readAllBytes(region);
        byte[] playerBefore = Files.readAllBytes(world.resolve("level.dat"));
        byte[] externalBefore = Files.readAllBytes(region.getParent().resolve("c.0.0.mcc"));

        runEntry(world);

        assertTrue(Files.exists(world.resolve(MARKER)));
        assertArrayEquals(regionBefore, Files.readAllBytes(region));
        assertArrayEquals(externalBefore, Files.readAllBytes(region.getParent().resolve("c.0.0.mcc")));
        assertArrayEquals(playerBefore, Files.readAllBytes(world.resolve("level.dat")));
    }

    @Test
    void savedRegistryCanBeScannedWithAnEmptyRegion() throws Exception {
        Path world = world("saved-registry-empty", false);
        writeRegistry(world.resolve("basemetals-legacy-registry.dat"));
        Path file = world.resolve("region/r.0.0.mca");
        Files.createDirectories(file.getParent());
        Files.createFile(file);

        runEntry(world);

        assertEquals(0, Files.size(file));
        assertTrue(Files.exists(world.resolve(MARKER)));
    }

    @Test
    void savedRegistryCanBeScannedWithLz4Chunks() throws Exception {
        assumeTrue(VERSION.equals("1.20.6") || VERSION.equals("1.21.11") || VERSION.equals("26.1.2"));
        Path world = world("saved-registry-lz4", false);
        writeRegistry(world.resolve("basemetals-legacy-registry.dat"));
        Path file = region(world, false, true);
        byte[] before = Files.readAllBytes(file);

        runEntry(world);

        assertArrayEquals(before, Files.readAllBytes(file));
        assertTrue(Files.exists(world.resolve(MARKER)));
    }

    @Test
    void firstUpgradePreparesThePlayerBeforeNativeFileFixingAndNeverRestoresAnOlderCopy() throws Exception {
        Path world = world("first-legacy-upgrade", true);
        writeLevel(world.resolve("level.dat"), 1343, true, 7);
        LegacyWorldDataHook.prepareSelectedWorld(world.resolve("level.dat"));
        assertEquals(7, experience(world.resolve("level.dat")));
        assertTrue(Files.exists(world.resolve(MARKER)));

        // A later native save is authoritative, even if an older backup still exists.
        writeLevel(world.resolve("level.dat"), DATA_VERSION, true, 37);
        writeLevel(world.resolve("level.dat_old"), 1343, true, 3);
        runEntry(world);

        assertEquals(37, experience(world.resolve("level.dat")));
        assertTrue(Files.exists(world.resolve(MARKER)));

        writeLevel(world.resolve("level.dat"), DATA_VERSION, true, 38);
        LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());
        runEntry(world);
        assertEquals(38, experience(world.resolve("level.dat")));
    }

    @Test
    void openingAnotherWorldDoesNotCarryOverLegacyProvenance() throws Exception {
        Path oldWorld = world("old-world", true);
        writeLevel(oldWorld.resolve("level.dat"), 1343, true, 7);
        LegacyWorldDataHook.prepareLegacyWorld(oldWorld.resolve("level.dat").toFile());

        Path modernWorld = world("next-modern-world", true);
        LegacyWorldDataHook.prepareLegacyWorld(modernWorld.resolve("level.dat").toFile());
        writeLevel(modernWorld.resolve("level.dat_old"), 1343, true, 3);
        runEntry(modernWorld);

        assertEquals(37, experience(modernWorld.resolve("level.dat")));
        assertFalse(Files.exists(modernWorld.resolve(MARKER)));
    }

    @Test
    void anOldBackupDoesNotActivateTerrainProtectionInAModernWorld() throws Exception {
        for (int backupVersion : new int[] {1343, 2730}) {
            Path world = world("old-terrain-backup", true);
            writeLevel(world.resolve("level.dat_old"), backupVersion, true, 3);
            region(world, false, false);

            LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());

            Field active = LegacyWorldDataHook.class.getDeclaredField("legacyWorldActive");
            active.setAccessible(true);
            assertFalse(active.getBoolean(null), "Backup version " + backupVersion);
            assertEquals(37, experience(world.resolve("level.dat")));
        }
    }

    @Test
    void terrainProtectionSurvivesReloadWithoutTrustingAnOldBackup() throws Exception {
        Path world = world("flattened-upgrade", false);
        writeLevel(world.resolve("level.dat"), 2730, false, 0);
        region(world, false, false);
        LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());

        assertTrue(Files.exists(world.resolve("basemetals-legacy-registry.dat")));
        writeLevel(world.resolve("level.dat"), DATA_VERSION, false, 0);
        LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());

        Field active = LegacyWorldDataHook.class.getDeclaredField("legacyWorldActive");
        active.setAccessible(true);
        assertTrue(active.getBoolean(null));
        runEntry(world);
        assertFalse(Files.exists(world.resolve(MARKER)));
    }

    @Test
    void mixedRegionsAndLaterOpenedCustomDimensionChunksKeepTheirContents() throws Exception {
        Path world = world("mixed-legacy-chunks", true);
        writeRegistry(world.resolve("basemetals-legacy-registry.dat"));
        writeLevel(world.resolve("level.dat_old"), 1343, true, 3);
        Path mixed = region(world, true, false);
        Path external = mixed.getParent().resolve("c.0.0.mcc");
        byte[] externalBefore = Files.readAllBytes(external);
        appendLegacyChunk(mixed, 1, 1);
        byte[] mixedBefore = Files.readAllBytes(mixed);

        Path later = world.resolve("dimensions/example/nested/cavern/region/r.2.0.mca");
        Files.createDirectories(later.getParent());
        Files.write(later, new byte[8192]);
        appendLegacyChunk(later, 0, 64);

        runEntry(world);

        for (Path file : Arrays.asList(mixed, later)) {
            int index = file.equals(mixed) ? 1 : 0;
            CompoundTag chunk = readRegionChunk(file, index);
            assertEquals(1456, chunk.getIntOr("DataVersion", 0));
            assertEquals("example:legacy_block",
                    Legacy112WorldMigrator.stateAtForTest(chunk, 0, 0).getStringOr("Name", ""));
            assertTrue(Files.exists(world.resolve("basemetals-1.12-region-backup").resolve(world.relativize(file))));
        }
        assertEquals(37, experience(world.resolve("level.dat")));
        assertArrayEquals(externalBefore, Files.readAllBytes(external));
        assertArrayEquals(externalBefore, Files.readAllBytes(
                world.resolve("basemetals-1.12-region-backup/region/c.0.0.mcc")));
        assertArrayEquals(mixedBefore, Files.readAllBytes(
                world.resolve("basemetals-1.12-region-backup/region/r.0.0.mca")));
        assertTrue(nativeAccepts(mixed, false));

        byte[] mixedAfter = Files.readAllBytes(mixed);
        byte[] laterAfter = Files.readAllBytes(later);
        runEntry(world);
        assertArrayEquals(mixedAfter, Files.readAllBytes(mixed));
        assertArrayEquals(laterAfter, Files.readAllBytes(later));
    }

    private static void appendLegacyChunk(Path file, int index, int chunkX) throws Exception {
        CompoundTag chunk = new CompoundTag();
        chunk.putInt("DataVersion", 1343);
        CompoundTag level = new CompoundTag();
        level.putInt("xPos", chunkX);
        level.putInt("zPos", 0);
        level.putBoolean("TerrainPopulated", true);
        level.putBoolean("LightPopulated", true);
        level.put("Entities", new ListTag());
        level.put("TileEntities", new ListTag());
        level.putByteArray("Biomes", new byte[256]);
        level.putIntArray("HeightMap", new int[256]);
        CompoundTag section = new CompoundTag();
        section.putByte("Y", (byte) 0);
        byte[] blocks = new byte[4096];
        blocks[0] = (byte) 200;
        section.putByteArray("Blocks", blocks);
        section.putByteArray("Data", new byte[2048]);
        ListTag sections = new ListTag();
        sections.add(section);
        level.put("Sections", sections);
        chunk.put("Level", level);

        ByteArrayOutputStream compressed = new ByteArrayOutputStream();
        try (DataOutputStream output = new DataOutputStream(new DeflaterOutputStream(compressed))) {
            NbtIo.write(chunk, output);
        }
        byte[] payload = compressed.toByteArray();
        byte[] original = Files.readAllBytes(file);
        int offset = (original.length + 4095) / 4096;
        int sectors = (payload.length + 5 + 4095) / 4096;
        ByteBuffer result = ByteBuffer.wrap(Arrays.copyOf(original, (offset + sectors) * 4096));
        result.putInt(index * 4, (offset << 8) | sectors);
        result.position(offset * 4096);
        result.putInt(payload.length + 1);
        result.put((byte) 2);
        result.put(payload);
        Files.write(file, result.array());
    }

    private static CompoundTag readRegionChunk(Path file, int index) throws Exception {
        byte[] contents = Files.readAllBytes(file);
        ByteBuffer header = ByteBuffer.wrap(contents);
        int offset = (header.getInt(index * 4) >>> 8) * 4096;
        int length = header.getInt(offset);
        assertEquals(2, contents[offset + 4]);
        try (DataInputStream input = new DataInputStream(new InflaterInputStream(
                new ByteArrayInputStream(contents, offset + 5, length - 1)))) {
            return (CompoundTag) readNbt(input);
        }
    }

    private static void writeRegistry(Path file) throws IOException {
        try (DataOutputStream output = new DataOutputStream(new GZIPOutputStream(Files.newOutputStream(file)))) {
            output.writeByte(10);
            output.writeUTF("");
            output.writeByte(10);
            output.writeUTF("Data");
            integer(output, "DataVersion", 2730);
            output.writeByte(0);
            for (String name : Arrays.asList("FML", "Registries", "minecraft:blocks")) {
                output.writeByte(10);
                output.writeUTF(name);
            }
            output.writeByte(9);
            output.writeUTF("ids");
            output.writeByte(10);
            output.writeInt(3);
            integer(output, "V", 0);
            output.writeByte(8);
            output.writeUTF("K");
            output.writeUTF("minecraft:air");
            output.writeByte(0);
            integer(output, "V", 300);
            output.writeByte(8);
            output.writeUTF("K");
            output.writeUTF("basemetals:copper_ore");
            output.writeByte(0);
            integer(output, "V", 200);
            output.writeByte(8);
            output.writeUTF("K");
            output.writeUTF("example:legacy_block");
            output.writeByte(0);
            for (int end = 0; end < 4; end++) output.writeByte(0);
        }
    }

    private static void runEntry(Path world) throws IOException {
        Legacy112WorldMigrator.migrateIfNeeded(world);
    }

    private static com.google.gson.JsonObject versionInformation() {
        try (InputStream input = LegacyMigrationSafetyTest.class.getResourceAsStream("/version.json");
                Reader reader = new InputStreamReader(input, java.nio.charset.StandardCharsets.UTF_8)) {
            return com.google.gson.JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException failure) {
            throw new IllegalStateException(failure);
        }
    }

    private static String minecraftVersion() {
        return versionInformation().get("id").getAsString();
    }

    private static int dataVersion() {
        return versionInformation().get("world_version").getAsInt();
    }

    private Path world(String name, boolean player) throws Exception {
        Path world = Files.createTempDirectory(temporaryWorld, name + "-");
        writeLevel(world.resolve("level.dat"), DATA_VERSION, player, 37);
        return world;
    }

    private static void writeLevel(Path file, int version, boolean player, int xp) throws IOException {
        try (DataOutputStream output = new DataOutputStream(new GZIPOutputStream(Files.newOutputStream(file)))) {
            output.writeByte(10);
            output.writeUTF("");
            output.writeByte(10);
            output.writeUTF("Data");
            integer(output, "DataVersion", version);
            if (player) {
                output.writeByte(10);
                output.writeUTF("Player");
                integer(output, "XpLevel", xp);
                for (String name : Arrays.asList("Inventory", "EnderItems")) {
                    output.writeByte(9);
                    output.writeUTF(name);
                    output.writeByte(10);
                    output.writeInt(0);
                }
                output.writeByte(0);
            }
            output.writeByte(0);
            output.writeByte(0);
        }
    }

    private static void integer(DataOutputStream output, String key, int value) throws IOException {
        output.writeByte(3);
        output.writeUTF(key);
        output.writeInt(value);
    }

    private static int experience(Path file) throws IOException {
        try (DataInputStream input = new DataInputStream(new GZIPInputStream(Files.newInputStream(file)))) {
            return NbtIo.read(input).getCompoundOrEmpty("Data").getCompoundOrEmpty("Player").getIntOr("XpLevel", 0);
        }
    }

    private static CompoundTag readNbt(DataInputStream input) throws IOException {
        return NbtIo.read(input);
    }

    private static Path region(Path world, boolean external, boolean lz4) throws Exception {
        ByteArrayOutputStream compressed = new ByteArrayOutputStream();
        OutputStream compressor = new DeflaterOutputStream(compressed);
        if (lz4) {
            RegionFileVersion version = (RegionFileVersion) RegionFileVersion.class.getField("VERSION_LZ4").get(null);
            compressor = version.wrap(compressed);
        }
        try (DataOutputStream output = new DataOutputStream(compressor)) {
            output.writeByte(10);
            output.writeUTF("");
            integer(output, "DataVersion", DATA_VERSION);
            integer(output, "xPos", 0);
            integer(output, "zPos", 0);
            output.writeByte(0);
        }
        byte[] payload = external ? new byte[0] : compressed.toByteArray();
        int sectors = (payload.length + 5 + 4095) / 4096;
        ByteBuffer buffer = ByteBuffer.allocate(8192 + sectors * 4096);
        buffer.putInt((2 << 8) | sectors);
        buffer.position(8192);
        buffer.putInt(payload.length + 1);
        buffer.put((byte) ((lz4 ? 4 : 2) | (external ? 128 : 0)));
        buffer.put(payload);
        Path file = world.resolve("region/r.0.0.mca");
        Files.createDirectories(file.getParent());
        Files.write(file, buffer.array());
        if (external) Files.write(file.getParent().resolve("c.0.0.mcc"), compressed.toByteArray());
        return file;
    }

    private static boolean nativeAccepts(Path file, boolean empty) throws IOException {
        try (RegionFile region = new RegionFile(new RegionStorageInfo("Base Metals test", Level.OVERWORLD, "chunk"),
                file, file.getParent(), false);
                DataInputStream input = region.getChunkDataInputStream(new ChunkPos(0, 0))) {
            if (empty) {
                assertNull(input);
                return true;
            }

            assertNotNull(input);
            assertEquals(DATA_VERSION, NbtIo.read(input).getIntOr("DataVersion", 0));
            return true;
        }
    }
}
