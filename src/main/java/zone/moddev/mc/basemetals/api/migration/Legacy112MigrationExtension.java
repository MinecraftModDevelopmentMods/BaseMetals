package zone.moddev.mc.basemetals.api.migration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.basemetals.migration.Legacy112WorldMigrator;

/**
 * Companion callbacks run before vanilla consumes legacy mod data. Register once
 * during mod setup. Callbacks run serially during startup and must tolerate a
 * retry after an interrupted upgrade. Do not access live worlds from a callback.
 */
public interface Legacy112MigrationExtension {
    enum Payload { CHUNK, PLAYER }

    /** Mutate legacy data before snapshotting; report whether anything changed. */
    default boolean prepareData(CompoundTag tag, int dataVersion, Payload payload) { return false; }

    default void beforeChunkFlattening(CompoundTag chunk) { }

    /**
     * Called for the embedded single-player host and legacy playerdata files.
     * Result uses the 1.20.1 item schema; component conversion runs afterwards.
     */
    default void afterPlayerFlattening(CompoundTag preparedSource, CompoundTag result) { }

    /** Return null for an unowned ID; never return air for an owned legacy block. */
    default BlockState decodeBlockState(ResourceLocation legacyId, int metadata) { return null; }

    static void register(ResourceLocation id, Legacy112MigrationExtension extension) {
        Legacy112WorldMigrator.registerExtension(id, extension);
    }
}
