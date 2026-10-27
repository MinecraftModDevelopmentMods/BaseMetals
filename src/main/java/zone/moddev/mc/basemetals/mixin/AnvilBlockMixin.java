package zone.moddev.mc.basemetals.mixin;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zone.moddev.mc.basemetals.content.BaseMetalAnvilBlock;

/** Advances the saved damage state without replacing a custom anvil with a vanilla one. */
@Mixin(AnvilBlock.class)
abstract class AnvilBlockMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private static void basemetals$damageAnvil(BlockState state, CallbackInfoReturnable<BlockState> callback) {
        if (state.getBlock() instanceof BaseMetalAnvilBlock) {
            int damage = state.getValue(BaseMetalAnvilBlock.DAMAGE);
            callback.setReturnValue(damage < 2
                    ? state.setValue(BaseMetalAnvilBlock.DAMAGE, damage + 1) : null);
        }
    }
}
