package zone.moddev.mc.basemetals.content;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
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
    @Override public Fluid getFlowing() { return content().flowing().get(); }
    @Override public Fluid getSource() { return content().source().get(); }
    @Override public Item getBucket() { return content().bucket().get(); }

    @Override protected boolean canConvertToSource() { return false; }
    @Override protected void beforeDestroyingBlock(IWorld world, BlockPos pos, BlockState state) {
        net.minecraft.block.Block.dropResources(state, world, pos, world.getBlockEntity(pos));
    }
    @Override public int getSlopeFindDistance(IWorldReader world) { return 2; }
    @Override public int getDropOff(IWorldReader world) { return 2; }
    @Override public int getTickDelay(IWorldReader world) { return 30; }
    @Override protected float getExplosionResistance() { return 100.0F; }
    @Override public boolean isSame(Fluid fluid) {
        return fluid == content().source().get() || fluid == content().flowing().get();
    }
    @Override protected boolean canBeReplacedWith(FluidState state, net.minecraft.world.IBlockReader world,
            BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isSame(this);
    }
    @Override public BlockState createLegacyBlock(FluidState state) {
        return content().block().get().defaultBlockState()
                .setValue(FlowingFluidBlock.LEVEL, Integer.valueOf(getLegacyLevel(state)));
    }

    public static final class Flowing extends MoltenFluid {
        public Flowing(String name) { super(name); }
        @Override protected void createFluidStateDefinition(StateContainer.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
        @Override public int getAmount(FluidState state) { return state.getValue(LEVEL); }
        @Override public boolean isSource(FluidState state) { return false; }
    }

    public static final class Source extends MoltenFluid {
        public Source(String name) { super(name); }
        @Override public int getAmount(FluidState state) { return 8; }
        @Override public boolean isSource(FluidState state) { return true; }
    }
}
