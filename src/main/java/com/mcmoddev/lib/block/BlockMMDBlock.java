package com.mcmoddev.lib.block;

import com.mcmoddev.basemetals.data.AchievementNames;
import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Block.
 */
public class BlockMMDBlock extends net.minecraft.block.Block implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material the block is made from
	 */
	public BlockMMDBlock(final MMDMaterial material) {
		super(material.getVanillaMaterial());
		this.material = material;

		this.setSoundType(this.material.getSoundType());
		this.fullBlock = true;
		this.setLightOpacity(255);
//		this.lightOpacity = 255;
		this.translucent = false;
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel(this.material.getHarvestTool(),
				this.material.getRequiredHarvestLevel());
		// Additional
		this.fullBlock = true;
	}

	@Override
	public boolean isBeaconBase(final IBlockAccess worldObj, final BlockPos pos,
			final BlockPos beacon) {
		return this.material.isBeaconBase();
	}

	@Override
	public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state,
			final EntityLivingBase placer, final ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		// achievement
		if ((Options.enableAchievements()) && (placer instanceof EntityPlayer)) {
			((EntityPlayer) placer)
					.addStat(Achievements.getAchievementByName(AchievementNames.BLOCKTASTIC), 1);
		}
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
