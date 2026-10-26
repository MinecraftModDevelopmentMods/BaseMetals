package zone.moddev.mc.basemetals.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.basemetals.migration.LegacyWorldDataHook;

/** Restores legacy mod identities on either side of vanilla's chunk conversion. */
@Mixin(SimpleRegionStorage.class)
abstract class SimpleRegionStorageMixin {
    @Inject(method = "upgradeChunkTag(Lnet/minecraft/nbt/CompoundTag;ILnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;",
            at = @At("HEAD"))
    private void basemetals$prepareChunk(CompoundTag chunk, int fallbackVersion,
            CompoundTag context, CallbackInfoReturnable<CompoundTag> callback) {
        LegacyWorldDataHook.prepareLegacyChunk(chunk);
    }

    @Inject(method = "upgradeChunkTag(Lnet/minecraft/nbt/CompoundTag;ILnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;",
            at = @At("RETURN"), cancellable = true)
    private void basemetals$finishChunk(CompoundTag chunk, int fallbackVersion,
            CompoundTag context, CallbackInfoReturnable<CompoundTag> callback) {
        callback.setReturnValue(LegacyWorldDataHook.finalizeLegacyChunk(callback.getReturnValue()));
    }
}
