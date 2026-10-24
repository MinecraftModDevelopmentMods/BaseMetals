package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class Legacy112WorldMigratorTest {
    @TempDir Path world;

    private void writeLevel(String name, int version) throws Exception {
        CompoundTag root = new CompoundTag();
        CompoundTag data = new CompoundTag();
        data.putInt("DataVersion", version);
        root.put("Data", data);
        NbtIo.writeCompressed(root, world.resolve(name));
    }

    @Test
    void modernWorldDoesNotImportOrCreateMigrationArtifacts() throws Exception {
        writeLevel("level.dat", 3955);
        writeLevel("level.dat_old", 3955);
        byte[] before = Files.readAllBytes(world.resolve("level.dat"));
        assertTrue(Legacy112WorldMigrator.legacyBlockIdsForTest(world).isEmpty());
        assertFalse(Legacy112WorldMigrator.migrateIfNeeded(world));
        assertArrayEquals(before, Files.readAllBytes(world.resolve("level.dat")));
        try (var files = Files.list(world)) {
            assertEquals(2, files.count());
        }
    }

    @Test
    void modernWorldDoesNotScanShortEndRegionHeader() throws Exception {
        writeLevel("level.dat", 3955);
        writeLevel("level.dat_old", 3955);
        Path region = world.resolve("DIM1/region/r.-2.-2.mca");
        Files.createDirectories(region.getParent());
        byte[] original = new byte[] {1, 2, 3};
        Files.write(region, original);

        assertFalse(Legacy112WorldMigrator.migrateIfNeeded(world));

        assertArrayEquals(original, Files.readAllBytes(region));
        assertFalse(Files.exists(world.resolve("BASEMETALS_1_12_BLOCK_MIGRATION_COMPLETE.txt")));
        assertFalse(Files.exists(world.resolve("basemetals-1.12-region-backup")));
    }
    @Test
    void newWorldWithoutLevelDataDoesNotImport() throws Exception {
        assertFalse(Legacy112WorldMigrator.migrateIfNeeded(world));
        assertTrue(Legacy112WorldMigrator.legacyBlockIdsForTest(world).isEmpty());
    }

    @Test
    void oldMetadataStillEnablesPackagedRegistryFallback() throws Exception {
        writeLevel("level.dat", 1343);
        assertFalse(Legacy112WorldMigrator.legacyBlockIdsForTest(world).isEmpty());
    }

    @Test
    void rewrittenLevelStillDetectsOldMetadata() throws Exception {
        writeLevel("level.dat", 3955);
        writeLevel("level.dat_old", 1343);
        assertFalse(Legacy112WorldMigrator.legacyBlockIdsForTest(world).isEmpty());
    }
}