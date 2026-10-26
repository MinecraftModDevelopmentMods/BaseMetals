package zone.moddev.mc.basemetals.mixin;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zone.moddev.mc.basemetals.content.BaseMetalAnvilBlock;

/** Includes custom anvils in the normal wear check without changing their falling behaviour. */
@Mixin(AnvilMenu.class)
abstract class AnvilMenuMixin {
    @Redirect(
            method = "lambda$onTake$2(Lnet/minecraft/world/entity/player/Player;FLnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"),
            require = 1, allow = 1)
    private static boolean basemetals$includeCustomAnvil(BlockState state, TagKey<Block> tag) {
        return state.is(tag) || tag == BlockTags.ANVIL && state.getBlock() instanceof BaseMetalAnvilBlock;
    }
}
