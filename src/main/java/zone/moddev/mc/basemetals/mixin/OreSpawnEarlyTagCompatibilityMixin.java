package zone.moddev.mc.basemetals.mixin;

import java.util.Collections;
import java.util.Set;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Lets OreSpawn defer its early tag lookup until Minecraft has loaded the world's tags. */
@Mixin(targets = "zone.moddev.mc.orespawn.worldgen.OreSpawnOreGeneration", remap = false)
abstract class OreSpawnEarlyTagCompatibilityMixin {
    @Inject(method = "resolveTag", at = @At("HEAD"), cancellable = true, remap = false)
    private static void basemetals$deferUnboundTags(TagKey<Block> tag,
            CallbackInfoReturnable<Set<Block>> callback) {
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            try {
                // Modded holders can still be unbound after vanilla's holders are ready.
                block.defaultBlockState().is(tag);
            } catch (IllegalStateException failure) {
                if (!"Tags not bound".equals(failure.getMessage())) {
                    throw failure;
                }

                // OreSpawn rebakes the unchanged profile once the world's tags are available.
                callback.setReturnValue(Collections.emptySet());
                return;
            }
        }

        // Bound tags use OreSpawn's original resolver unchanged.
    }
}
