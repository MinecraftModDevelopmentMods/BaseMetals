package zone.moddev.mc.basemetals.content;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraft.util.ResourceLocation;

/** Common flow behaviour for the molten metals. */
public abstract class MoltenFluid extends FlowingFluid {
    private final String name;

    protected MoltenFluid(String name) {
        this.name = name;
    }

    private FluidContent content() { return ModContent.fluid(name); }

    @Override
    protected FluidAttributes createAttributes() {
        return FluidAttributes.builder(new ResourceLocation("basemetals", "block/molten_metal_still"),
                new ResourceLocation("basemetals", "block/molten_metal_flow"))
                .color(0xFF000000 | ModContent.fluidColour(this)).build(this);
    }
    @Override public Fluid getFlowingFluid() { return content().flowing().get(); }
    @Override public Fluid getStillFluid() { return content().source().get(); }
    @Override public Item getFilledBucket() { return content().bucket().get(); }

    @Override protected boolean canSourcesMultiply() { return false; }
    @Override protected void beforeReplacingBlock(IWorld world, BlockPos pos, BlockState state) {
        net.minecraft.block.Block.spawnDrops(state, world.getWorld(), pos);
    }
    @Override public int getSlopeFindDistance(IWorldReader world) { return 2; }
    @Override public int getLevelDecreasePerBlock(IWorldReader world) { return 2; }
    @Override public int getTickRate(IWorldReader world) { return 30; }
    @Override protected float getExplosionResistance() { return 100.0F; }
    @Override public boolean isEquivalentTo(Fluid fluid) {
        return fluid == content().source().get() || fluid == content().flowing().get();
    }
    @Override protected boolean canDisplace(IFluidState state, net.minecraft.world.IBlockReader world,
            BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isEquivalentTo(this);
    }
    @Override public BlockState getBlockState(IFluidState state) {
        return content().block().get().getDefaultState()
                .with(FlowingFluidBlock.LEVEL, Integer.valueOf(getLevelFromState(state)));
    }

    public static final class Flowing extends MoltenFluid {
        public Flowing(String name) { super(name); }
        @Override protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }
        @Override public int getLevel(IFluidState state) { return state.get(LEVEL_1_8); }
        @Override public boolean isSource(IFluidState state) { return false; }
    }

    public static final class Source extends MoltenFluid {
        public Source(String name) { super(name); }
        @Override public int getLevel(IFluidState state) { return 8; }
        @Override public boolean isSource(IFluidState state) { return true; }
    }
}
