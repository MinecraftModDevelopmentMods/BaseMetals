package zone.moddev.mc.basemetals.content;

import java.util.function.Supplier;

import zone.moddev.mc.basemetals.config.BaseMetalsConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public final class MoltenMetalBlock extends LiquidBlock {
    private final boolean mercury;

    public MoltenMetalBlock(Supplier<? extends FlowingFluid> fluid, Properties properties, boolean mercury) {
        super(fluid.get(), properties);
        this.mercury = mercury;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        if (level.isClientSide) return;
        if (!mercury) {
            // Temperature is descriptive; apply the actual contact heat here.
            // Vanilla lava damage respects fire immunity and Fire Resistance.
            entity.lavaHurt();
            return;
        }
        if (BaseMetalsConfig.MERCURY_EFFECTS.get()
                && entity instanceof LivingEntity living && level.random.nextInt(32) == 0) {
            living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 30 * 20, 2));
        }
    }
}
