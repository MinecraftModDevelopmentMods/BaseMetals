package cyano.basemetals.entity;

import cyano.basemetals.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class EntityCustomBolt extends EntityTippedArrow {

	private ItemStack itemStack;

	/**
	 *
	 * @param worldIn
	 */
	public EntityCustomBolt(World worldIn) {
		super(worldIn);
	}

	/**
	 *
	 * @param worldIn
	 * @param stack
	 * @param shooter
	 */
	public EntityCustomBolt(World worldIn, ItemStack stack, EntityPlayer shooter) {
		super(worldIn, shooter);
		this.itemStack = stack;
	}

	@Override
	protected ItemStack getArrowStack() {
		if (this.itemStack == null) {
			// TODO: FIXME
			this.itemStack = new ItemStack(Items.getItemByName("iron_bolt"));
		}

		return new ItemStack(this.itemStack.getItem(), 1, this.itemStack.getItemDamage());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		final NBTTagCompound itemStackCompound = new NBTTagCompound();
		this.itemStack.writeToNBT(itemStackCompound);

		compound.setTag("itemstack", itemStackCompound);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.itemStack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("itemstack"));
	}
}
