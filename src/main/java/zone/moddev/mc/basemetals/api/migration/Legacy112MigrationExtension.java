package zone.moddev.mc.basemetals.api.migration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.basemetals.migration.Legacy112WorldMigrator;

/**
 * Lets a companion mod preserve its data when Base Metals pre-flattens a
 * Minecraft 1.10 or 1.12 world. Register during common setup, before worlds load.
 */
public interface Legacy112MigrationExtension {
    enum Payload { CHUNK, PLAYER }

    /**
     * Updates a legacy chunk or player payload in place, before vanilla's data
     * fixer or the migrator's block entity snapshot. Leave stacks that vanilla
     * recognizes in the legacy item format so its fixer can componentize them.
     * Return true on mutation.
     */
    default boolean prepareData(CompoundTag tag, int dataVersion, Payload payload) { return false; }

    /** Runs immediately before the intermediate chunk data fix, only for chunks being converted. */
    default void beforeChunkFlattening(CompoundTag chunk) { }

    /**
     * Checks or restores extension data after vanilla converts the embedded
     * single-player record. The prepared source is a read-only legacy snapshot;
     * the result uses the current item component format. Vanilla does not
     * componentize arbitrary stacks inside opaque mod payloads.
     */
    default void afterPlayerFlattening(CompoundTag preparedSource, CompoundTag result) { }

    /**
     * Returns the native state for an owned legacy block, or null for an ID
     * this extension does not own. Metadata is the original four-bit value.
     */
    default BlockState decodeBlockState(ResourceLocation legacyId, int metadata) { return null; }

    /** The ID identifies the registering extension and must be unique. */
    static void register(ResourceLocation id, Legacy112MigrationExtension extension) {
        Legacy112WorldMigrator.registerExtension(id, extension);
    }
}
