package zone.moddev.mc.basemetals.mixin;

import java.util.Arrays;

import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.BlockStateData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Makes room for the numeric block IDs used by old Forge saves. */
@Mixin(BlockStateData.class)
abstract class BlockStateDataMixin {
    @Shadow @Final @Mutable
    private static Dynamic<?>[] MAP;

    @Shadow @Final @Mutable
    private static Dynamic<?>[] BLOCK_DEFAULTS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void basemetals$expandLegacyTables(CallbackInfo callback) {
        MAP = Arrays.copyOf(MAP, Math.max(MAP.length, 65_536));
        BLOCK_DEFAULTS = Arrays.copyOf(BLOCK_DEFAULTS, Math.max(BLOCK_DEFAULTS.length, 4_096));
    }
}
