package cyano.basemetals.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * A pressure plate that only activates when a player steps on it
 */
public class BlockHumanDetector extends net.minecraft.block.BlockPressurePlate {

	public BlockHumanDetector() {
		super(Material.iron, Sensitivity.MOBS);
	}

	@Override
	protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
		AxisAlignedBB axisalignedbb = this.getSensitiveAABB(pos);
		List<? extends Entity > list = worldIn.<Entity>getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);

		if (!list.isEmpty()) {
			return 15;
		}

		return 0;
	}
}
