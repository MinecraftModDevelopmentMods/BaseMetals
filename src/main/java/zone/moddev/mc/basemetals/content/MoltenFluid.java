package zone.moddev.mc.basemetals.content;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraft.resources.ResourceLocation;

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
    @Override protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        net.minecraft.world.level.block.Block.dropResources(state, world, pos, world.getBlockEntity(pos));
    }
    @Override public int getSlopeFindDistance(LevelReader world) { return 2; }
    @Override public int getDropOff(LevelReader world) { return 2; }
    @Override public int getTickDelay(LevelReader world) { return 30; }
    @Override protected float getExplosionResistance() { return 100.0F; }
    @Override public boolean isSame(Fluid fluid) {
        return fluid == content().source().get() || fluid == content().flowing().get();
    }
    @Override protected boolean canBeReplacedWith(FluidState state, net.minecraft.world.level.BlockGetter world,
            BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isSame(this);
    }
    @Override public BlockState createLegacyBlock(FluidState state) {
        return content().block().get().defaultBlockState()
                .setValue(LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(state)));
    }

    public static final class Flowing extends MoltenFluid {
        public Flowing(String name) { super(name); }
        @Override protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
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
