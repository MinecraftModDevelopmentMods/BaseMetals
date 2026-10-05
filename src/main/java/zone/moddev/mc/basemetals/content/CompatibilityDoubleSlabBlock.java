package zone.moddev.mc.basemetals.content;

import net.minecraft.block.SlabBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.SlabType;
import net.minecraftforge.common.ToolType;

/** Keeps old double slabs intact when a 1.10 or 1.12 world is upgraded. */
public final class CompatibilityDoubleSlabBlock extends SlabBlock {
    private final int harvestLevel;

    public CompatibilityDoubleSlabBlock(Properties properties, int harvestLevel) {
        super(properties);
        this.harvestLevel = harvestLevel;
        setDefaultState(getDefaultState().with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, Boolean.FALSE));
    }

    @Override public ToolType getHarvestTool(BlockState state) { return ToolType.PICKAXE; }
    @Override public int getHarvestLevel(BlockState state) { return harvestLevel; }
}
