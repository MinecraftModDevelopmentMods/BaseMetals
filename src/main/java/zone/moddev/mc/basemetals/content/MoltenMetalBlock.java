package zone.moddev.mc.basemetals.content;

import zone.moddev.mc.basemetals.config.BaseMetalsConfig;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class MoltenMetalBlock extends LiquidBlock {
    private final boolean mercury;

    public MoltenMetalBlock(FlowingFluid fluid, Properties properties, boolean mercury) {
        super(fluid, properties);
        this.mercury = mercury;
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        super.entityInside(state, world, pos, entity);
        if (mercury && !world.isClientSide && BaseMetalsConfig.MERCURY_EFFECTS.get()
                && entity instanceof LivingEntity && world.random.nextInt(32) == 0) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.CONFUSION, 30 * 20, 2));
        }
    }
}
