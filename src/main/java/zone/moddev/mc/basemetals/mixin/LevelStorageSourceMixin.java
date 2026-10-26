package zone.moddev.mc.basemetals.mixin;

import java.nio.file.Path;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.basemetals.migration.LegacyWorldDataHook;

/** Captures original registry and item data before vanilla runs its data fixers. */
@Mixin(LevelStorageSource.class)
abstract class LevelStorageSourceMixin {
    @Inject(method = "readLevelDataTagFixed", at = @At("HEAD"))
    private static void basemetals$prepareWorld(Path file, DataFixer fixer,
            CallbackInfoReturnable<Dynamic<?>> callback) {
        // A fallback backup must never make a modern world look like a legacy save.
        if (file.getFileName().toString().equals("level.dat")) {
            LegacyWorldDataHook.prepareLegacyWorld(file.toFile());
        }
    }
}
