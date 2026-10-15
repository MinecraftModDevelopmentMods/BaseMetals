package zone.moddev.mc.basemetals.content;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public final class HumanDetectorBlock extends PressurePlateBlock {
    public HumanDetectorBlock(Properties properties) {
        super(Sensitivity.EVERYTHING, properties, net.minecraft.world.level.block.state.properties.BlockSetType.STONE);
    }

    @Override
    protected int getSignalStrength(Level level, BlockPos pos) {
        AABB box = TOUCH_AABB.move(pos);
        List<Player> players = level.getEntitiesOfClass(Player.class, box,
                player -> !player.isSpectator() && !player.isIgnoringBlockTriggers());
        return players.isEmpty() ? 0 : 15;
    }
}
