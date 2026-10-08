package zone.moddev.mc.basemetals.content;

import zone.moddev.mc.basemetals.config.BaseMetalsConfig;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class MoltenMetalBlock extends FlowingFluidBlock {
    private final boolean mercury;

    public MoltenMetalBlock(FlowingFluid fluid, Properties properties, boolean mercury) {
        super(fluid, properties);
        this.mercury = mercury;
    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        super.entityInside(state, world, pos, entity);
        if (mercury && !world.isClientSide && BaseMetalsConfig.MERCURY_EFFECTS.get()
                && entity instanceof LivingEntity && world.random.nextInt(32) == 0) {
            ((LivingEntity) entity).addEffect(new EffectInstance(Effects.CONFUSION, 30 * 20, 2));
        }
    }
}
