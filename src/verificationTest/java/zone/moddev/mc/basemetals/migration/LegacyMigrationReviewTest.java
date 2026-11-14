package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import zone.moddev.mc.basemetals.api.migration.Legacy112MigrationExtension;

class LegacyMigrationReviewTest {
    @TempDir Path world;

    @BeforeAll static void version() { SharedConstants.tryDetectVersion(); }
    @AfterEach void clearPreparation() { Legacy112WorldMigrator.beginWorldLoad(); }

    @Test void oversizedLegacyChunkStaysExternalAndKeepsItsPayload() throws Exception {
        writeLevel(1343, false, 0);
        CompoundTag chunk = legacyChunk();
        byte[] payload = new byte[2 * 1024 * 1024];
        new Random(545).nextBytes(payload);
        chunk.getCompound("Level").putByteArray("ReviewPayload", payload);
        Path region = writeChunk(chunk);
        Path external = region.resolveSibling("c.0.0.mcc");
        assertTrue(Files.size(external) > 1024 * 1024, "Fixture must genuinely exceed inline storage");
        byte[] originalRegion = Files.readAllBytes(region);
        byte[] originalExternal = Files.readAllBytes(external);

        assertTrue(Legacy112WorldMigrator.migrateIfNeeded(world));

        CompoundTag converted = readChunk(region);
        assertEquals(1456, converted.getInt("DataVersion"));
        assertArrayEquals(payload, converted.getCompound("Level").getByteArray("ReviewPayload"));
        assertTrue(Files.size(external) > 1024 * 1024);
        assertArrayEquals(originalRegion, Files.readAllBytes(backup(region)));
        assertArrayEquals(originalExternal, Files.readAllBytes(backup(external)));
        byte[] regionAfter = Files.readAllBytes(region);
        byte[] externalAfter = Files.readAllBytes(external);
        assertFalse(Legacy112WorldMigrator.migrateIfNeeded(world));
        assertArrayEquals(regionAfter, Files.readAllBytes(region));
        assertArrayEquals(externalAfter, Files.readAllBytes(external));
    }

    @Test void nonAlignedConvertedRegionHasAnExactOriginalBackup() throws Exception {
        writeLevel(1343, false, 0);
        Path region = writeChunk(legacyChunk());
        Files.write(region, new byte[] {42}, StandardOpenOption.APPEND);
        byte[] original = Files.readAllBytes(region);

        assertTrue(Legacy112WorldMigrator.migrateIfNeeded(world));

        assertArrayEquals(original, Files.readAllBytes(backup(region)));
        assertEquals(1456, readChunk(region).getInt("DataVersion"));
    }

    @Test void nonAlignedUnconvertedRegionIsNeverModifiedByTheScan() throws Exception {
        writeLevel(currentVersion(), false, 0);
        CompoundTag source = level(1343, false, 0);
        NbtIo.writeCompressed(source, world.resolve("basemetals-legacy-registry.dat"));
        CompoundTag chunk = new CompoundTag();
        chunk.putInt("DataVersion", currentVersion());
        chunk.putInt("xPos", 0);
        chunk.putInt("zPos", 0);
        Path region = writeChunk(chunk);
        Files.write(region, new byte[] {42}, StandardOpenOption.APPEND);
        byte[] original = Files.readAllBytes(region);

        assertFalse(Legacy112WorldMigrator.migrateIfNeeded(world));

        assertArrayEquals(original, Files.readAllBytes(region));
        assertFalse(Files.exists(backup(region)));
    }

    @Test void playerRecoverySurvivesAProcessRestartAfterLevelRewrite() throws Exception {
        String phase = System.getProperty("basemetals.reviewCrash.phase", "single");
        if (!phase.equals("single")) {
            assertTrue(phase.equals("stage") || phase.equals("recover"));
            Path explicit = Path.of(System.getProperty("basemetals.reviewCrash.world")).toAbsolutePath().normalize();
            if (phase.equals("stage")) {
                assertFalse(Files.exists(explicit), "Process fixture must be a fresh directory");
                Files.createDirectory(explicit);
                Files.writeString(explicit.resolve("review-fixture.marker"), "PR545 disposable player recovery");
            } else {
                assertEquals("PR545 disposable player recovery", Files.readString(explicit.resolve("review-fixture.marker")));
                assertNotEquals(ProcessHandle.current().pid(), Long.parseLong(Files.readString(explicit.resolve("stage.pid"))));
            }
            world = explicit;
        }
        if (!phase.equals("recover")) {
            writeLevel(1343, true, 7);
            LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());
            writeLevel(currentVersion(), true, 37);
            if (phase.equals("stage")) {
                Files.writeString(world.resolve("stage.pid"), Long.toString(ProcessHandle.current().pid()));
                return;
            }
            // The ordinary unit case also discards all preparation explicitly.
            Legacy112WorldMigrator.beginWorldLoad();
        }
        LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());
        AtomicInteger callbacks = new AtomicInteger();

        assertTrue(Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension(callbacks))));

        CompoundTag player = read(world.resolve("level.dat")).getCompound("Data").getCompound("Player");
        assertEquals(7, player.getInt("XpLevel"));
        assertEquals(1, callbacks.get());
        assertRestoredPlayer(player);
        // A completed upgrade must never restore the old snapshot over later progress.
        writeLevel(currentVersion(), true, 83);
        LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());
        assertFalse(Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension(callbacks))));
        assertEquals(83, read(world.resolve("level.dat")).getCompound("Data").getCompound("Player").getInt("XpLevel"));
        if (phase.equals("recover")) Files.writeString(world.resolve("recovered.pid"), Long.toString(ProcessHandle.current().pid()));
    }

    @Test void dedicatedPlayerRetryDoesNotRepeatAnAlreadySavedCallback() throws Exception {
        writeLevel(1343, false, 0);
        Files.createDirectories(world.resolve("playerdata"));
        for (int i = 1; i <= 2; i++) {
            CompoundTag player = player(i);
            player.putInt("DataVersion", 1343);
            NbtIo.writeCompressed(player, world.resolve("playerdata/" + new UUID(0, i) + ".dat"));
        }
        AtomicInteger calls = new AtomicInteger();
        Legacy112MigrationExtension interrupted = new Legacy112MigrationExtension() {
            @Override public void afterPlayerFlattening(CompoundTag source, CompoundTag result) {
                if (source.getInt("XpLevel") == 2) throw new IllegalStateException("Simulated interrupted upgrade");
                extension(calls).afterPlayerFlattening(source, result);
            }
        };
        assertThrows(IllegalStateException.class,
                () -> Legacy112WorldMigrator.migrateIfNeeded(world, List.of(interrupted)));
        assertEquals(1, calls.get());
        assertFalse(Files.exists(world.resolve("BASEMETALS_1_12_BLOCK_MIGRATION_COMPLETE.txt")));
        Path first = world.resolve("playerdata/" + new UUID(0, 1) + ".dat");
        byte[] saved = Files.readAllBytes(first);

        Legacy112WorldMigrator.beginWorldLoad();
        LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());
        assertTrue(Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension(calls))));
        assertEquals(2, calls.get());
        assertArrayEquals(saved, Files.readAllBytes(first));
    }

    @Test void completedRegionsStillRecoverTheHostBeforeItsFirstModernWorldSave() throws Exception {
        writeLevel(1343, true, 7);
        LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());
        AtomicInteger calls = new AtomicInteger();
        assertTrue(Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension(calls))));
        CompoundTag loaded = new CompoundTag();
        Legacy112WorldMigrator.refreshLoadedSingleplayerPlayer(loaded, world);
        assertRestoredPlayer(loaded);
        assertEquals(1343, read(world.resolve("level.dat")).getCompound("Data").getInt("DataVersion"));
        assertTrue(Files.exists(world.resolve("BASEMETALS_1_12_BLOCK_MIGRATION_COMPLETE.txt")));

        // The region marker was saved, but the process ended before saving level.dat.
        Legacy112WorldMigrator.beginWorldLoad();
        LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());
        assertTrue(Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension(calls))));
        assertFalse(Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension(calls))));
        CompoundTag restarted = new CompoundTag();
        Legacy112WorldMigrator.refreshLoadedSingleplayerPlayer(restarted, world);
        assertRestoredPlayer(restarted);
        assertEquals(7, restarted.getInt("XpLevel"));
        assertEquals(2, calls.get());
    }

    @Test void malformedPendingSnapshotFailsClosedButACompletedUpgradeIgnoresIt() throws Exception {
        writeLevel(currentVersion(), true, 37);
        Path journal = world.resolve("basemetals-1.12-region-backup/player-migration-source.dat");
        Files.createDirectories(journal.getParent());
        NbtIo.writeCompressed(new CompoundTag(), journal);
        byte[] before = Files.readAllBytes(world.resolve("level.dat"));
        assertThrows(IllegalStateException.class,
                () -> LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile()));
        assertArrayEquals(before, Files.readAllBytes(world.resolve("level.dat")));
        Files.writeString(world.resolve("BASEMETALS_1_12_BLOCK_MIGRATION_COMPLETE.txt"), "complete");
        assertDoesNotThrow(() -> LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile()));
        assertArrayEquals(before, Files.readAllBytes(world.resolve("level.dat")));
    }

    @Test void modernWorldGenerationMetadataWithoutDataVersionDoesNotUseNumericFallback() throws Exception {
        CompoundTag level = level(currentVersion(), false, 0);
        level.getCompound("Data").remove("DataVersion");
        level.getCompound("Data").put("WorldGenSettings", new CompoundTag());
        NbtIo.writeCompressed(level, world.resolve("level.dat"));
        assertTrue(Legacy112WorldMigrator.legacyBlockIdsForTest(world).isEmpty());
    }

    @Test void dedicatedPlayersReceivePostFlatteningCallbacksExactlyOnce() throws Exception {
        writeLevel(1343, false, 0);
        Files.createDirectories(world.resolve("playerdata"));
        for (int i = 1; i <= 2; i++) {
            CompoundTag player = player(i);
            player.putInt("DataVersion", 1343);
            NbtIo.writeCompressed(player, world.resolve("playerdata/" + new UUID(0, i) + ".dat"));
        }
        AtomicInteger callbacks = new AtomicInteger();
        assertTrue(Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension(callbacks))));
        assertEquals(2, callbacks.get());
        for (int i = 1; i <= 2; i++) {
            Path file = world.resolve("playerdata/" + new UUID(0, i) + ".dat");
            CompoundTag converted = read(file);
            assertEquals(currentVersion(), converted.getInt("DataVersion"));
            assertEquals(i, converted.getInt("XpLevel"));
            assertRestoredPlayer(converted);
            assertEquals(1343, read(backup(file)).getInt("DataVersion"));
        }
        assertFalse(Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension(callbacks))));
        assertEquals(2, callbacks.get());
    }

    @Test void missingDataVersionStillAllowsTheLegacyPackagedMap() throws Exception {
        CompoundTag original = level(1343, false, 0);
        original.getCompound("Data").remove("DataVersion");
        original.remove("FML");
        NbtIo.writeCompressed(original, world.resolve("level.dat"));
        assertFalse(Legacy112WorldMigrator.legacyBlockIdsForTest(world).isEmpty());
    }

    @Test void missingDataVersionDoesNotMakeModernVersionMetadataLegacy() throws Exception {
        CompoundTag original = level(currentVersion(), false, 0);
        CompoundTag data = original.getCompound("Data");
        data.remove("DataVersion");
        CompoundTag version = new CompoundTag();
        version.putInt("Id", currentVersion());
        data.put("Version", version);
        NbtIo.writeCompressed(original, world.resolve("level.dat"));
        assertTrue(Legacy112WorldMigrator.legacyBlockIdsForTest(world).isEmpty());
    }

    @Test void missingLevelVersionStillConvertsSavedNumericBlocks() throws Exception {
        CompoundTag original = level(1343, false, 0);
        original.getCompound("Data").remove("DataVersion");
        NbtIo.writeCompressed(original, world.resolve("level.dat"));
        Path region = writeChunk(legacyChunk());
        LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());

        assertTrue(Legacy112WorldMigrator.migrateIfNeeded(world));

        assertEquals("example:legacy_block",
                Legacy112WorldMigrator.stateAtForTest(readChunk(region), 0, 0).getString("Name"));
    }

    @Test void capturedPlayerCrossesStartupThreadsWithoutLeakingIntoAnotherWorld() throws Exception {
        writeLevel(1343, true, 7);
        try (var client = java.util.concurrent.Executors.newSingleThreadExecutor()) {
            long clientThread = client.submit(() -> {
                LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());
                return Thread.currentThread().threadId();
            }).get(10, java.util.concurrent.TimeUnit.SECONDS);
            assertNotEquals(Thread.currentThread().threadId(), clientThread);
            writeLevel(currentVersion(), true, 37);
            assertTrue(Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension(new AtomicInteger()))));
            CompoundTag loaded = new CompoundTag();
            Legacy112WorldMigrator.refreshLoadedSingleplayerPlayer(loaded, world);
            assertEquals(7, loaded.getInt("XpLevel"));
            assertRestoredPlayer(loaded);

            Path other = Files.createDirectory(world.resolve("other-world"));
            NbtIo.writeCompressed(level(currentVersion(), true, 91), other.resolve("level.dat"));
            client.submit(() -> LegacyWorldDataHook.prepareLegacyWorld(other.resolve("level.dat").toFile()))
                    .get(10, java.util.concurrent.TimeUnit.SECONDS);
            assertFalse(Legacy112WorldMigrator.migrateIfNeeded(other));
            assertEquals(91, read(other.resolve("level.dat")).getCompound("Data").getCompound("Player").getInt("XpLevel"));
        }
    }

    @Test void retainedRegistryDoesNotKeepTerrainProtectionActiveForever() throws Exception {
        writeLevel(2730, false, 0);
        writeChunk(legacyChunk());
        LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());
        assertTrue(terrainActive());
        writeLevel(currentVersion(), false, 0);

        LegacyWorldDataHook.prepareLegacyWorld(world.resolve("level.dat").toFile());

        assertFalse(terrainActive());
        assertTrue(Files.exists(world.resolve("basemetals-legacy-registry.dat")));
    }

    @Test void preparedStateUsesTheSameMonitorOnBothStartupThreads() throws Exception {
        for (String name : List.of("beginWorldLoad", "prepareWorld", "migrateIfNeeded", "refreshLoadedSingleplayerPlayer")) {
            var methods = java.util.Arrays.stream(Legacy112WorldMigrator.class.getDeclaredMethods())
                    .filter(method -> method.getName().equals(name)).toList();
            assertFalse(methods.isEmpty());
            assertTrue(methods.stream().allMatch(method -> Modifier.isSynchronized(method.getModifiers())), name);
        }
    }

    private static Legacy112MigrationExtension extension(AtomicInteger calls) {
        return new Legacy112MigrationExtension() {
            @Override public void afterPlayerFlattening(CompoundTag source, CompoundTag result) {
                calls.incrementAndGet();
                CompoundTag old = source.getList("Inventory", Tag.TAG_COMPOUND).getCompound(0);
                CompoundTag item = result.getList("Inventory", Tag.TAG_COMPOUND).getCompound(0);
                item.putString("id", "minecraft:iron_pickaxe");
                CompoundTag tag = item.getCompound("tag");
                tag.putInt("Damage", old.getInt("Damage"));
                item.put("tag", tag);
                result.putString("ReviewWitness", "post-flattening");
            }
        };
    }

    private static void assertRestoredPlayer(CompoundTag player) {
        assertEquals("post-flattening", player.getString("ReviewWitness"));
        CompoundTag item = player.getList("Inventory", Tag.TAG_COMPOUND).getCompound(0);
        assertEquals("minecraft:iron_pickaxe", item.getString("id"));
        assertEquals(1, item.getInt("count"));
        assertEquals(27, item.getCompound("components").getInt("minecraft:damage"));
    }

    private void writeLevel(int version, boolean host, int xp) throws Exception {
        NbtIo.writeCompressed(level(version, host, xp), world.resolve("level.dat"));
    }

    private static CompoundTag level(int version, boolean host, int xp) {
        CompoundTag root = new CompoundTag();
        CompoundTag data = new CompoundTag();
        data.putInt("DataVersion", version);
        if (host) data.put("Player", player(xp));
        root.put("Data", data);
        CompoundTag registry = new CompoundTag();
        ListTag ids = new ListTag();
        for (var entry : java.util.Map.of(0, "minecraft:air", 200, "example:legacy_block", 300, "basemetals:copper_ore").entrySet()) {
            CompoundTag id = new CompoundTag();
            id.putInt("V", entry.getKey());
            id.putString("K", entry.getValue());
            ids.add(id);
        }
        registry.put("ids", ids);
        CompoundTag registries = new CompoundTag();
        registries.put("minecraft:blocks", registry);
        CompoundTag fml = new CompoundTag();
        fml.put("Registries", registries);
        root.put("FML", fml);
        return root;
    }

    private static CompoundTag player(int xp) {
        CompoundTag player = new CompoundTag();
        player.putInt("XpLevel", xp);
        ListTag inventory = new ListTag();
        CompoundTag tool = new CompoundTag();
        tool.putString("id", "example:legacy_pickaxe");
        tool.putByte("Count", (byte) 1);
        tool.putByte("Slot", (byte) 0);
        tool.putShort("Damage", (short) 27);
        inventory.add(tool);
        player.put("Inventory", inventory);
        player.put("EnderItems", new ListTag());
        return player;
    }

    private static CompoundTag legacyChunk() {
        CompoundTag root = new CompoundTag();
        root.putInt("DataVersion", 1343);
        CompoundTag level = new CompoundTag();
        level.putInt("xPos", 0);
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
        root.put("Level", level);
        return root;
    }

    private Path writeChunk(CompoundTag chunk) throws Exception {
        Path path = world.resolve("region/r.0.0.mca");
        Files.createDirectories(path.getParent());
        try (RegionFile region = region(path);
                DataOutputStream output = region.getChunkDataOutputStream(new ChunkPos(0, 0))) {
            NbtIo.write(chunk, output);
        }
        return path;
    }

    private static CompoundTag readChunk(Path path) throws Exception {
        try (RegionFile region = region(path);
                DataInputStream input = region.getChunkDataInputStream(new ChunkPos(0, 0))) {
            assertNotNull(input);
            return NbtIo.read(input);
        }
    }

    private static RegionFile region(Path path) throws Exception {
        return new RegionFile(new RegionStorageInfo("Review regression", Level.OVERWORLD, "chunk"), path, path.getParent(), false);
    }

    private Path backup(Path path) { return world.resolve("basemetals-1.12-region-backup").resolve(world.relativize(path)); }
    private static CompoundTag read(Path path) throws Exception { return NbtIo.readCompressed(path, NbtAccounter.unlimitedHeap()); }
    private static int currentVersion() { return SharedConstants.getCurrentVersion().getDataVersion().getVersion(); }
    private static boolean terrainActive() throws Exception {
        var field = LegacyWorldDataHook.class.getDeclaredField("legacyWorldActive");
        field.setAccessible(true);
        return field.getBoolean(null);
    }
}
