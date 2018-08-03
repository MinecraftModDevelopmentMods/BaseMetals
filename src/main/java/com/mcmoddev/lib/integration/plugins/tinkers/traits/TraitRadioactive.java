package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;

/**
 * <h2><u>Radioactive Tool Modifier:</u></h2>
 * <b>Name:</b> radioactive
 * <br>
 * <b>Desc:</b>
 * Does nothing if the tool is broken or if the tool is plated with lead.
 * Otherwise, there is a 50% chance of having the tool either apply nausea for 1 second or poison for half a second.
 * Effects are applied when ever the tool is within the player's inventory.
 *
 * <br>
 * <b>String Reference:<br></b>
 * "radioactive"
 * "mmd-radioactive"
 *
 * @author Java doc author: Vase of Petunias
 */
public class TraitRadioactive extends AbstractTrait {

	public TraitRadioactive() {
		super("mmd-radioactive", 0x28DF28);
	}

	@Override
	public void onUpdate(@Nonnull final ItemStack tool, @Nonnull final World world,
			@Nonnull final Entity entity, @Nonnull final int itemSlot,
			@Nonnull final boolean isSelected) {
		if (!ToolHelper.isBroken(tool) && (entity instanceof EntityPlayer)) {
			final EntityLivingBase player = (EntityPlayer) entity;
			final NBTTagCompound root = TagUtil.getTagSafe(tool);
			final NBTTagCompound extra = TagUtil.getExtraTag(root);
			if (!extra.getBoolean("plated")) {
				if ((new Random()).nextInt(100) >= 50) {
					player.addPotionEffect(new PotionEffect(MobEffects.POISON, 10, 1));
				} else {
					player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 20));
				}
			}
		}
	}
}
