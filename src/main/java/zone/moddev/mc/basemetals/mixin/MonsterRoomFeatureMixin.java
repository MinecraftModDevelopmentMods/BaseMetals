package zone.moddev.mc.basemetals.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.basemetals.migration.LegacyWorldDataHook;

/** Keeps dungeon generation out of imported terrain before it tries to place a spawner. */
@Mixin(MonsterRoomFeature.class)
abstract class MonsterRoomFeatureMixin {
    @Inject(method = "place(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
            at = @At("HEAD"), cancellable = true)
    private void basemetals$leaveImportedRoomsAlone(WorldGenLevel level, ChunkGenerator generator,
            RandomSource random, BlockPos position, CallbackInfoReturnable<Boolean> callback) {
        if (level instanceof WorldGenRegion region
                && LegacyWorldDataHook.shouldBlockWorldgenWrite(region, position)) {
            callback.setReturnValue(false);
        }
    }
}
