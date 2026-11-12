package zone.moddev.mc.basemetals.api.migration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.basemetals.migration.Legacy112WorldMigrator;

/** Preserves companion data before Base Metals converts a pre-flattening world. */
public interface Legacy112MigrationExtension {
    enum Payload { CHUNK, PLAYER }

    /**
     * Supplies this selected world's saved registry context before any conversion.
     * Each extension receives its own copy. Modern worlds are included so a
     * companion can clear registry IDs retained from a previous world.
     */
    default void prepareWorld(CompoundTag levelSnapshot) { }

    /** Mutates owned legacy data before vanilla fixes and the block-entity snapshot. */
    default boolean prepareData(CompoundTag tag, int dataVersion, Payload payload) { return false; }

    /** Runs immediately before the intermediate chunk data fix. */
    default void beforeChunkFlattening(CompoundTag chunk) { }

    /**
     * Restores owned data at the 1.20.1 item schema, before componentization.
     * The prepared source is read-only; result still uses historical Count/tag.
     * On 26.1 and later this seeds a missing host file before FileFixerUpper moves it.
     */
    default void afterPlayerFlattening(CompoundTag preparedSource, CompoundTag result) { }

    /** Returns a native owned state, or null for an unowned registry ID. */
    default BlockState decodeBlockState(Identifier legacyId, int metadata) { return null; }

    /** Register during common setup, before any world is opened. */
    static void register(Identifier id, Legacy112MigrationExtension extension) {
        Legacy112WorldMigrator.registerExtension(id, extension);
    }
}
