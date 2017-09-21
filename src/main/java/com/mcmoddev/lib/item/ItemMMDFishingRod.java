package com.mcmoddev.lib.item;

import java.util.List;

import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDFishingRod extends ItemFishingRod implements IMMDObject {

	private final MMDMaterial material;
	protected final String repairOreDictName;
	protected static final long REGEN_INTERVAL = 200;

	/**
	 *
	 * @param material
	 *            The material to make the fishing rod from
	 */
	public ItemMMDFishingRod(MMDMaterial material) {
		super();
		this.material = material;
		// this.setMaxDamage(64);
		// this.setMaxStackSize(1);
		// this.setCreativeTab(CreativeTabs.TOOLS);
		this.repairOreDictName = Oredicts.INGOT + this.material.getCapitalizedName();
//		this.addPropertyOverride(new ResourceLocation("cast"), new IItemPropertyGetter() {
//			@SideOnly(Side.CLIENT)
//			@Override
//			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
//				return entityIn == null ? 0.0F : ((entityIn.getHeldItemMainhand() == stack) && (entityIn instanceof EntityPlayer) && (((EntityPlayer) entityIn).fishEntity != null) ? 1.0F : 0.0F);
//			}
//		});
	}

	/*
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (playerIn.fishEntity != null) {
			final int i = playerIn.fishEntity.handleHookRetraction();
			itemStackIn.damageItem(i, playerIn);
			playerIn.swingArm(hand);
		} else {
			worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / ((itemRand.nextFloat() * 0.4F) + 0.8F));

			if (!worldIn.isRemote)
				worldIn.spawnEntity(new EntityMetalFishHook(worldIn, playerIn));

			playerIn.swingArm(hand);
			playerIn.addStat(StatList.getObjectUseStats(this));
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
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
		super.onUpdate(item, world, player, inventoryIndex, isHeld);

		if (world.isRemote)
			return;

		if (this.material.regenerates() && isHeld && (item.getItemDamage() > 0) && ((world.getTotalWorldTime() % REGEN_INTERVAL) == 0)) {
			item.setItemDamage(item.getItemDamage() - 1);
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		MMDToolEffects.addToolSpecialPropertiesToolTip(this.material, tooltip);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
