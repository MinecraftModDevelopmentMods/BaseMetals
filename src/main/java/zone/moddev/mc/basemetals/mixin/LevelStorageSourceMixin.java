package zone.moddev.mc.basemetals.mixin;

import java.nio.file.Path;

import com.mojang.serialization.Dynamic;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.basemetals.migration.LegacyWorldDataHook;

/** Captures original registry and item data before vanilla runs its data fixers. */
@Mixin(LevelStorageSource.LevelStorageAccess.class)
abstract class LevelStorageSourceMixin {
    @Shadow
    public abstract Path getLevelPath(LevelResource resource);

    @Inject(method = "getUnfixedDataTag", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/LevelStorageSource;readLevelDataTagRaw(Ljava/nio/file/Path;)Lnet/minecraft/nbt/CompoundTag;"))
    private void basemetals$prepareWorld(boolean useFallback,
            CallbackInfoReturnable<Dynamic<?>> callback) {
        // Only prepare the locked, selected world. Backups and list previews are read-only.
        if (!useFallback) {
            LegacyWorldDataHook.prepareSelectedWorld(getLevelPath(LevelResource.LEVEL_DATA_FILE));
        }
    }
}
