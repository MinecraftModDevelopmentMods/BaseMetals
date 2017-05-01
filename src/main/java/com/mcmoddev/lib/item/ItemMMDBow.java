package com.mcmoddev.lib.item;

import java.util.List;

import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDBow extends ItemBow implements IMMDObject {

	protected final MMDMaterial material;
	protected final String repairOreDictName;
	protected static final long REGEN_INTERVAL = 200;

	/**
	 *
	 * @param material
	 *            The material to make the bow from
	 */
	public ItemMMDBow(MMDMaterial material) {
		this.material = material;
		this.setMaxDamage(this.material.getToolDurability());
		this.repairOreDictName = Oredicts.INGOT + this.material.getCapitalizedName();
	}

	/*
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			final EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			final boolean flag = entityplayer.capabilities.isCreativeMode || (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0);
			ItemStack itemstack = this.findAmmo(entityplayer);

			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, (EntityPlayer) entityLiving, i, (itemstack != null) || flag);
			if (i < 0)
				return;

			if ((itemstack != null) || flag) {
				if (itemstack == null)
					// TODO: FIXME
					itemstack = new ItemStack(Materials.vanilla_iron.arrow);

				final float f = getArrowVelocity(i);

				if ((double) f >= 0.1D) {
					final boolean flag1 = flag && (itemstack.getItem() instanceof ItemMetalArrow); // Forge: Fix consuming custom arrows.

					if (!worldIn.isRemote) {
						// TODO: FIXME
						final ItemMetalArrow itemMetalArrow = (ItemMetalArrow) (itemstack.getItem() instanceof ItemMetalArrow ? itemstack.getItem() : Materials.vanilla_iron.arrow);
						final EntityCustomArrow entityarrow = itemMetalArrow.createArrow(worldIn, itemstack, entityplayer);
						entityarrow.setAim(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

						if (f == 1.0F)
							entityarrow.setIsCritical(true);

						final int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

						if (j > 0)
							entityarrow.setDamage(entityarrow.getDamage() + ((double) j * 0.5D) + 0.5D);

						final int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

						if (k > 0)
							entityarrow.setKnockbackStrength(k);

						if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
							entityarrow.setFire(100);

						stack.damageItem(1, entityplayer);

						if (flag1)
							entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

						worldIn.spawnEntity(entityarrow);
					}

					worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, (1.0F / ((itemRand.nextFloat() * 0.4F) + 1.2F)) + (f * 0.5F));

					if (!flag1) {
						--itemstack.stackSize;

						if (itemstack.stackSize == 0)
							entityplayer.inventory.deleteStack(itemstack);
					}

					entityplayer.addStat(StatList.getObjectUseStats(this));
				}
			}
		}
	}
*/
/*
	private ItemStack findAmmo(EntityPlayer player) {
		if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
			return player.getHeldItem(EnumHand.OFF_HAND);
		else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
			return player.getHeldItem(EnumHand.MAIN_HAND);
		else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				final ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (this.isArrow(itemstack))
					return itemstack;
			}

			return null;
		}
	}
*/
/*
	@Override
	protected boolean isArrow(ItemStack stack) {
		return (stack != null) && (stack.getItem() instanceof ItemMetalArrow);
	}
*/
/*
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		final boolean flag = this.findAmmo(playerIn) != null;

		if (!playerIn.capabilities.isCreativeMode && !flag)
			return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
		else {
			playerIn.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}
	}
*/
	@Override
	public boolean getIsRepairable(final ItemStack intputItem, final ItemStack repairMaterial) {
		final List<ItemStack> acceptableItems = OreDictionary.getOres(this.repairOreDictName);
		for (final ItemStack i : acceptableItems)
			if (ItemStack.areItemsEqual(i, repairMaterial))
				return true;

		return false;
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if (this.material.regenerates() && !world.isRemote && isHeld && (item.getItemDamage() > 0) && ((world.getTotalWorldTime() % REGEN_INTERVAL) == 0))
			item.setItemDamage(item.getItemDamage() - 1);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean b) {
		super.addInformation(stack, player, list, b);
		MMDToolEffects.addToolSpecialPropertiesToolTip(this.material, list);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
