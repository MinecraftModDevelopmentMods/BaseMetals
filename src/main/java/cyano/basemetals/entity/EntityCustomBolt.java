package cyano.basemetals.entity;

import cyano.basemetals.init.Materials;
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
	 * @param worldIn The World
	 */
	public EntityCustomBolt(World worldIn) {
		super(worldIn);
	}

	/**
	 *
	 * @param worldIn The World
	 * @param stack The ItemStack
	 * @param shooter The Shooter
	 */
	public EntityCustomBolt(World worldIn, ItemStack stack, EntityPlayer shooter) {
		super(worldIn, shooter);
		this.itemStack = stack;
	}

	@Override
	protected ItemStack getArrowStack() {
		if (this.itemStack == null) {
			// TODO: FIXME
			this.itemStack = new ItemStack(Materials.vanilla_iron.bolt);
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
