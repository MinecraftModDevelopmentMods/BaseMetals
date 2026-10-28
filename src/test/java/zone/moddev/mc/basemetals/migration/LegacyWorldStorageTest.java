package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;

class LegacyWorldStorageTest {
    @TempDir(cleanup = CleanupMode.ON_SUCCESS)
    Path world;

    @Test
    void previousCapabilityFileGetsANamespacedCopyAndKeepsItsOriginal() throws Exception {
        level(4671);
        Path old = capability("data/capabilities.dat", 42);
        byte[] before = Files.readAllBytes(old);

        prepare();

        Path current = world.resolve("dimensions/minecraft/overworld/data/forge/capabilities.dat");
        assertArrayEquals(before, Files.readAllBytes(current));
        assertArrayEquals(before, Files.readAllBytes(old));
        assertEquals(42, LegacyNbt.readCompressed(current.toFile()).getCompoundOrEmpty("data").getIntOr("marker", 0));
        prepare();
        assertArrayEquals(before, Files.readAllBytes(current));
    }

    @Test
    void currentCapabilitiesAreAuthoritativeDuringAnUpgrade() throws Exception {
        level(4671);
        Path old = capability("data/capabilities.dat", 1);
        Path current = capability("dimensions/minecraft/overworld/data/forge/capabilities.dat", 2);
        byte[] before = Files.readAllBytes(current);

        prepare();

        assertArrayEquals(before, Files.readAllBytes(current));
        assertEquals(1, LegacyNbt.readCompressed(old.toFile()).getCompoundOrEmpty("data").getIntOr("marker", 0));
    }

    @Test
    void modernWorldNeverImportsALeftoverLegacyCapabilityFile() throws Exception {
        level(SharedVersion.current());
        capability("data/capabilities.dat", 1);

        prepare();

        assertFalse(Files.exists(world.resolve("dimensions/minecraft/overworld/data/forge/capabilities.dat")));
        assertFalse(Files.exists(world.resolve("basemetals-legacy-registry.dat")));
    }

    @Test
    void metadataWithoutAPositiveVersionDoesNotActivateStorageMigration() throws Exception {
        try (var output = Files.newOutputStream(world.resolve("level.dat"))) {
            LegacyNbt.writeCompressed(new CompoundTag(), output);
        }
        capability("data/capabilities.dat", 1);

        prepare();

        assertFalse(Files.exists(world.resolve("dimensions/minecraft/overworld/data/forge/capabilities.dat")));
    }

    @Test
    void zeroVersionDoesNotCountAsUpgradeProvenance() throws Exception {
        level(0);
        capability("data/capabilities.dat", 1);

        prepare();

        assertFalse(Files.exists(world.resolve("dimensions/minecraft/overworld/data/forge/capabilities.dat")));
    }

    private void prepare() throws Exception {
        LegacyWorldDataHook.prepareSelectedWorld(world.resolve("level.dat"));
    }

    @Test
    void embeddedPlayerIsNotLostWhenItsOldDiskFileIsMissing() throws Exception {
        embeddedPlayer(42);

        prepare();

        Path file = world.resolve("playerdata/00000000-0000-0000-0000-000000000118.dat");
        assertTrue(Files.isRegularFile(file));
        CompoundTag player = LegacyNbt.readCompressed(file.toFile());
        assertEquals(42, player.getIntOr("XpTotal", 0));
        assertEquals(4671, player.getIntOr("DataVersion", 0));
        assertEquals(42, LegacyNbt.readCompressed(world.resolve("level.dat").toFile())
                .getCompoundOrEmpty("Data").getCompoundOrEmpty("Player").getIntOr("XpTotal", 0));
    }

    @Test
    void embeddedPlayerNeverOverwritesAnExistingPlayerFile() throws Exception {
        embeddedPlayer(42);
        Path file = world.resolve("players/data/00000000-0000-0000-0000-000000000118.dat");
        Files.createDirectories(file.getParent());
        CompoundTag player = new CompoundTag();
        player.putInt("XpTotal", 99);
        try (var output = Files.newOutputStream(file)) {
            LegacyNbt.writeCompressed(player, output);
        }
        byte[] before = Files.readAllBytes(file);

        prepare();

        assertArrayEquals(before, Files.readAllBytes(file));
        assertFalse(Files.exists(world.resolve("playerdata/00000000-0000-0000-0000-000000000118.dat")));
    }

    @Test
    void modernWorldNeverRestoresALeftoverEmbeddedPlayer() throws Exception {
        embeddedPlayer(42);
        Files.copy(world.resolve("level.dat"), world.resolve("level.dat_old"));
        CompoundTag current = LegacyNbt.readCompressed(world.resolve("level.dat").toFile());
        current.getCompoundOrEmpty("Data").putInt("DataVersion", SharedVersion.current());
        try (var output = Files.newOutputStream(world.resolve("level.dat"))) {
            LegacyNbt.writeCompressed(current, output);
        }

        prepare();

        assertFalse(Files.exists(world.resolve("playerdata")));
        assertFalse(Files.exists(world.resolve("players")));
    }

    @Test
    void staleBackupCannotSupplyAnEmbeddedPlayer() throws Exception {
        embeddedPlayer(42);
        Files.move(world.resolve("level.dat"), world.resolve("level.dat_old"));
        level(SharedVersion.current());

        prepare();

        assertFalse(Files.exists(world.resolve("playerdata")));
        assertFalse(Files.exists(world.resolve("players")));
    }

    private void embeddedPlayer(int experience) throws Exception {
        level(4671);
        CompoundTag root = LegacyNbt.readCompressed(world.resolve("level.dat").toFile());
        CompoundTag player = new CompoundTag();
        player.putIntArray("UUID", new int[] {0, 0, 0, 280});
        player.putInt("XpTotal", experience);
        root.getCompoundOrEmpty("Data").put("Player", player);
        try (var output = Files.newOutputStream(world.resolve("level.dat"))) {
            LegacyNbt.writeCompressed(root, output);
        }
    }

    private void level(int version) throws Exception {
        CompoundTag data = new CompoundTag();
        data.putInt("DataVersion", version);
        CompoundTag root = new CompoundTag();
        root.put("Data", data);
        try (var output = Files.newOutputStream(world.resolve("level.dat"))) {
            LegacyNbt.writeCompressed(root, output);
        }
    }

    private Path capability(String relative, int marker) throws Exception {
        Path path = world.resolve(relative);
        Files.createDirectories(path.getParent());
        CompoundTag payload = new CompoundTag();
        payload.putInt("marker", marker);
        CompoundTag root = new CompoundTag();
        root.put("data", payload);
        try (var output = Files.newOutputStream(path)) {
            LegacyNbt.writeCompressed(root, output);
        }
        return path;
    }

    private static final class SharedVersion {
        static int current() {
            net.minecraft.SharedConstants.tryDetectVersion();
            return net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version();
        }
    }
}
