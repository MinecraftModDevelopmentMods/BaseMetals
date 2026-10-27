package zone.moddev.mc.basemetals.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraftforge.common.util.LevelCapabilityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.basemetals.migration.LegacyWorldCapabilityCodec;

/** Forge 61 adds a wrapper that older world-capability files do not have. */
@Mixin(value = LevelCapabilityData.class, remap = false)
abstract class LevelCapabilityDataMixin {
    @Inject(method = "type", at = @At("RETURN"), cancellable = true, remap = false)
    private static void basemetals$readOlderCapabilities(ServerLevel level,
            CallbackInfoReturnable<SavedDataType<LevelCapabilityData>> callback) {
        var current = callback.getReturnValue();
        callback.setReturnValue(new SavedDataType<>(current.id(), current.constructor(),
                LegacyWorldCapabilityCodec.acceptLegacyPayload(current.codec()), current.dataFixType()));
    }
}
