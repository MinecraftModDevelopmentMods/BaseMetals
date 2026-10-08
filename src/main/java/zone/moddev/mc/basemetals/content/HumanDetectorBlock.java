package zone.moddev.mc.basemetals.content;

import java.util.List;

import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** A pressure plate which responds to players only. */
public final class HumanDetectorBlock extends PressurePlateBlock {
    public HumanDetectorBlock(Properties properties) {
        super(Sensitivity.EVERYTHING, properties);
    }

    @Override
    public int getSignalStrength(World world, BlockPos pos) {
        AxisAlignedBB box = TOUCH_AABB.move(pos);
        List<PlayerEntity> players = world.getEntitiesOfClass(PlayerEntity.class, box);
        for (PlayerEntity player : players) {
            if (!player.isSpectator() && !player.isIgnoringBlockTriggers()) return 15;
        }
        return 0;
    }
}
