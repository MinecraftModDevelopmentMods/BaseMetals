package zone.moddev.mc.basemetals.content;

import java.util.List;

import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/** A pressure plate which responds to players only. */
public final class HumanDetectorBlock extends PressurePlateBlock {
    public HumanDetectorBlock(Properties properties) {
        super(Sensitivity.EVERYTHING, properties);
    }

    @Override
    public int getSignalStrength(Level world, BlockPos pos) {
        AABB box = TOUCH_AABB.move(pos);
        List<Player> players = world.getEntitiesOfClass(Player.class, box);
        for (Player player : players) {
            if (!player.isSpectator() && !player.isIgnoringBlockTriggers()) return 15;
        }
        return 0;
    }
}
