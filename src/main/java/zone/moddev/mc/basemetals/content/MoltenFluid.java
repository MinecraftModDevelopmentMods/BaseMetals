package zone.moddev.mc.basemetals.content;

import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/** Common flow behaviour for the molten metals. */
public abstract class MoltenFluid extends FlowingFluid {
    private final String name;

    protected MoltenFluid(String name) {
        this.name = name;
    }

    private FluidContent content() { return ModContent.fluid(name); }
    @Override public Fluid getFlowingFluid() { return content().flowing().get(); }
    @Override public Fluid getStillFluid() { return content().source().get(); }
    @Override public Item getFilledBucket() { return content().bucket().get(); }

    @Override @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.TRANSLUCENT; }
    @Override protected boolean canSourcesMultiply() { return false; }
    @Override protected void beforeReplacingBlock(IWorld world, BlockPos pos, IBlockState state) {
        state.dropBlockAsItem(world.getWorld(), pos, 0);
    }
    @Override public int getSlopeFindDistance(IWorldReaderBase world) { return 2; }
    @Override public int getLevelDecreasePerBlock(IWorldReaderBase world) { return 2; }
    @Override public int getTickRate(IWorldReaderBase world) { return 30; }
    @Override protected float getExplosionResistance() { return 100.0F; }
    @Override public boolean isEquivalentTo(Fluid fluid) {
        return fluid == content().source().get() || fluid == content().flowing().get();
    }
    @Override protected boolean canOtherFlowInto(IFluidState state, Fluid fluid, EnumFacing direction) {
        return direction == EnumFacing.DOWN && !fluid.isEquivalentTo(this);
    }
    @Override public IBlockState getBlockState(IFluidState state) {
        return content().block().get().getDefaultState()
                .with(BlockFlowingFluid.LEVEL, Integer.valueOf(getLevelFromState(state)));
    }

    public static final class Flowing extends MoltenFluid {
        public Flowing(String name) { super(name); }
        @Override protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_TO_8);
        }
        @Override public int getLevel(IFluidState state) { return state.get(LEVEL_1_TO_8); }
        @Override public boolean isSource(IFluidState state) { return false; }
    }

    public static final class Source extends MoltenFluid {
        public Source(String name) { super(name); }
        @Override public int getLevel(IFluidState state) { return 8; }
        @Override public boolean isSource(IFluidState state) { return true; }
    }
}
