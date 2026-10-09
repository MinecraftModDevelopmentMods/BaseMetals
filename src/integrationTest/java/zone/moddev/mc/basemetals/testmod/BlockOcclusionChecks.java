package zone.moddev.mc.basemetals.testmod;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import org.apache.logging.log4j.LogManager;

import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.RegistryHandle;

/** Checks the face-culling decisions used by Minecraft's chunk renderer. */
final class BlockOcclusionChecks {
    private BlockOcclusionChecks() {}

    static int run() {
        int checks = 0;
        int blocks = 0;
        int states = 0;

        for (Class<?> family : new Class<?>[] {TrapDoorBlock.class, DoorBlock.class, IronBarsBlock.class}) {
            for (Map.Entry<String, RegistryHandle<Block>> entry : ModContent.blocksById().entrySet()) {
                Block block = entry.getValue().get();
                if (!family.isInstance(block)) continue;

                Block vanilla = block instanceof TrapDoorBlock ? Blocks.IRON_TRAPDOOR
                        : block instanceof DoorBlock ? Blocks.IRON_DOOR : Blocks.IRON_BARS;
                blocks++;

                if (block instanceof TrapDoorBlock) {
                    require(Block.shouldRenderFace(Blocks.STONE.defaultBlockState(),
                            new NeighbourView(block.defaultBlockState()), BlockPos.ZERO.below(),
                            Direction.UP, BlockPos.ZERO), "floor hidden by closed trapdoor: " + entry.getKey());
                    checks++;
                }

                for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                    NeighbourView view = new NeighbourView(state);
                    states++;

                    for (Direction face : Direction.values()) {
                        BlockPos stone = BlockPos.ZERO.relative(face.getOpposite());
                        require(Block.shouldRenderFace(Blocks.STONE.defaultBlockState(), view,
                                stone, face, BlockPos.ZERO), "stone face hidden by " + state + " on " + face);
                        checks++;
                    }

                    require(!state.canOcclude(), "transparent block still hides neighbours: " + state);
                    require(!Shapes.joinIsNotEmpty(state.getCollisionShape(view, BlockPos.ZERO),
                            vanilla.withPropertiesOf(state).getCollisionShape(view, BlockPos.ZERO), BooleanOp.NOT_SAME),
                            "collision shape differs from vanilla: " + state);
                    checks += 2;
                }
            }
        }

        for (Map.Entry<String, RegistryHandle<Block>> entry : ModContent.blocksById().entrySet()) {
            String name = entry.getKey();
            if (!name.endsWith("_block") && !name.endsWith("_ore") && !name.endsWith("_plate")
                    && !name.endsWith("_slab") && !name.endsWith("_stairs") && !name.endsWith("_wall")) continue;

            require(entry.getValue().get().defaultBlockState().canOcclude(),
                    "opaque block lost face culling: " + name);
            checks++;
        }

        require(blocks > 0 && states > blocks, "transparent block families were not checked");
        LogManager.getLogger("basemetalsprobe").info(
                "BASEMETALS_OCCLUSION_PROBE PASS blocks={} states={} checks={}", blocks, states, checks);
        return checks;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException("Block occlusion check failed: " + message);
    }

    private static final class NeighbourView implements BlockGetter {
        private final BlockState neighbour;

        private NeighbourView(BlockState neighbour) {
            this.neighbour = neighbour;
        }

        @Override public BlockState getBlockState(BlockPos pos) {
            return pos.equals(BlockPos.ZERO) ? neighbour : Blocks.STONE.defaultBlockState();
        }

        @Override public FluidState getFluidState(BlockPos pos) { return getBlockState(pos).getFluidState(); }
        @Override public BlockEntity getBlockEntity(BlockPos pos) { return null; }
        @Override public int getHeight() { return 256; }
        @Override public int getMinBuildHeight() { return 0; }
    }
}
