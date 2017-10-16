package com.mcmoddev.lib.item;

import net.minecraft.entity.projectile.EntityFishHook;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemFishingRod extends net.minecraft.item.ItemFishingRod {

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (playerIn.fishEntity != null) {
			final int i = playerIn.fishEntity.handleHookRetraction();
			playerIn.getHeldItemMainhand().damageItem(i, playerIn);
			playerIn.swingArm(hand);
		} else {
			worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / ((itemRand.nextFloat() * 0.4F) + 0.8F));

			if (!worldIn.isRemote) {
				// Changed from MC EntityFishHook to BMe EntityFishHook
				EntityFishHook entityfishhook = new EntityFishHook(worldIn, playerIn);

				worldIn.spawnEntity(entityfishhook);
			}

			playerIn.swingArm(hand);
			playerIn.addStat(StatList.getObjectUseStats(this));
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItemMainhand());
	}
}
