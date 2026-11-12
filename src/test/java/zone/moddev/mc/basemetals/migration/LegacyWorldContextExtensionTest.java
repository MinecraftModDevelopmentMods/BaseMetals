package zone.moddev.mc.basemetals.migration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zone.moddev.mc.basemetals.api.migration.Legacy112MigrationExtension;

import static org.junit.jupiter.api.Assertions.*;

class LegacyWorldContextExtensionTest {
    private static final List<Integer> VERSIONS = new ArrayList<>();
    private static final List<CompoundTag> SECOND = new ArrayList<>();

    @BeforeAll static void register() {
        Legacy112MigrationExtension.register(Identifier.parse("contexttest:a_mutator"),
                new Legacy112MigrationExtension() {
                    @Override public void prepareWorld(CompoundTag levelSnapshot) {
                        VERSIONS.add(levelSnapshot.getCompoundOrEmpty("Data").getIntOr("DataVersion", 0));
                        levelSnapshot.remove("Protected");
                        levelSnapshot.putBoolean("Injected", true);
                    }
                });
        Legacy112MigrationExtension.register(Identifier.parse("contexttest:b_observer"),
                new Legacy112MigrationExtension() {
                    @Override public void prepareWorld(CompoundTag levelSnapshot) {
                        SECOND.add(levelSnapshot.copy());
                    }
                });
    }

    @BeforeEach void clearObservations() {
        VERSIONS.clear();
        SECOND.clear();
    }

    @Test void companionsReceiveIndependentSnapshotsBeforeLegacyConversion() {
        CompoundTag root = snapshot(512), before = root.copy();
        Legacy112WorldMigrator.prepareWorld(Path.of("build", "context-test", "level.dat"), root);
        assertEquals(List.of(512), VERSIONS);
        assertEquals(List.of(before), SECOND, "One extension must not change another extension's snapshot");
        assertEquals(before, root, "Companions must not mutate the authoritative world root");
    }

    @Test void modernWorldSelectionAlsoNotifiesCompanionsToResetLegacyContext() {
        CompoundTag legacy = snapshot(1343), modern = snapshot(4790), before = modern.copy();
        Legacy112WorldMigrator.prepareWorld(Path.of("build", "context-old", "level.dat"), legacy);
        Legacy112WorldMigrator.prepareWorld(Path.of("build", "context-new", "level.dat"), modern);
        assertEquals(List.of(1343, 4790), VERSIONS);
        assertEquals(before, SECOND.get(1));
        assertEquals(before, modern);
    }

    private static CompoundTag snapshot(int version) {
        CompoundTag root = new CompoundTag(), data = new CompoundTag();
        data.putInt("DataVersion", version);
        root.put("Data", data);
        root.putString("Protected", "saved-registry-snapshot");
        return root;
    }
}
