package zone.moddev.mc.basemetals.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.basemetals.migration.LegacyWorldDataHook;

/** Protects imported terrain while allowing generation in genuinely new chunks. */
@Mixin(WorldGenRegion.class)
abstract class WorldGenRegionMixin {
    @Inject(method = "ensureCanWrite", at = @At("HEAD"), cancellable = true)
    private void basemetals$protectTerrain(BlockPos position, CallbackInfoReturnable<Boolean> callback) {
        if (LegacyWorldDataHook.shouldBlockWorldgenWrite((WorldGenRegion) (Object) this, position)) {
            callback.setReturnValue(false);
        }
    }
}
