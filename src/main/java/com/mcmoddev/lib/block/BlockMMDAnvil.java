package com.mcmoddev.lib.block;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.item.MMDContainerRepair;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

/**
 * Anvil.
 */
public class BlockMMDAnvil extends net.minecraft.block.BlockAnvil implements IMMDObject {

	private final MMDMaterial mmdMaterial;

	/**
	 *
	 * @param material The material the Anvil is made from
	 */
	public BlockMMDAnvil(final MMDMaterial material) {
		super();
		this.mmdMaterial = material;
		this.setSoundType(this.mmdMaterial.getSoundType());
		this.blockHardness = this.mmdMaterial.getBlockHardness();
		this.blockResistance = this.mmdMaterial.getBlastResistance();
		this.setHarvestLevel(this.mmdMaterial.getHarvestTool(),
				this.mmdMaterial.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos,
			final IBlockState state, final EntityPlayer playerIn, final EnumHand hand,
			final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.displayGui(new BlockMMDAnvil.MMDAnvil(worldIn, pos, this.mmdMaterial));
		}

		return true;
	}

	public static class MMDAnvil implements IInteractionObject {

		private final World world;
		private final BlockPos position;
		private final MMDMaterial material;

		/**
		 *
		 * @param worldIn
		 * @param pos
		 * @param material
		 */
		public MMDAnvil(final World worldIn, final BlockPos pos, final MMDMaterial material) {
			this.world = worldIn;
			this.position = pos;
			this.material = material;
		}

		/**
		 * Get the name of this object. For players this returns their username
		 */
		@Override
		public String getName() {
			return Names.ANVIL + this.material.getCapitalizedName();
		}

		/**
		 * Returns true if this thing is named.
		 */
		@Override
		public boolean hasCustomName() {
			return false;
		}

		/**
		 * Get the formatted ChatComponent that will be used for the sender's username in chat.
		 */
		@Override
		public ITextComponent getDisplayName() {
			return new TextComponentTranslation(this.material.getBlock(Names.ANVIL) + ".name");
		}

		@Override
		public Container createContainer(final InventoryPlayer playerInventory,
				final EntityPlayer playerIn) {
			return new MMDContainerRepair(playerInventory, this.world, this.position, playerIn);
		}

		@Override
		public String getGuiID() {
			return "minecraft:anvil";
		}
	}
}
