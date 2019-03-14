package com.mcmoddev.basemetals.properties;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;

public class AquariumProperty extends BMEPropertyBase {
	@Override
	public void apply(ItemStack stack) {
		if(hasEffect(stack)) {
			// only apply if there is an effect
			// we should always double check here :)
		}
	}

	@Override
	public void apply(ItemStack stack, EntityPlayer player) {
		if(hasEffect(stack, player)) {
			if ((hasFullSuit(player, MaterialNames.AQUARIUM)) && (player.posY > 0)
					&& (player.posY < 255)) {
				
				final Block b1 = Minecraft.getMinecraft().world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ))
						.getBlock();
				final Block b2 = Minecraft.getMinecraft().world
						.getBlockState(new BlockPos(player.posX, player.posY + 1, player.posZ))
						.getBlock();
				if ((b1 == Blocks.WATER) && (b2 == Blocks.WATER)) {
					final PotionEffect waterBreathing = new PotionEffect(MobEffects.WATER_BREATHING,
							EFFECT_DURATION, 0, false, false);
					player.addPotionEffect(waterBreathing);
					final PotionEffect protection = new PotionEffect(MobEffects.RESISTANCE,
							EFFECT_DURATION, 0, false, false);
					player.addPotionEffect(protection);
					player.removePotionEffect(MobEffects.MINING_FATIGUE);
				}
			}
		}
	}

	@Override
	public void apply(ItemStack stack, EntityLivingBase ent) {
		if(hasEffect(stack, ent) && ent instanceof EntityPlayer) {
			apply(stack, (EntityPlayer)ent);
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return false; // no effect for just the stack
	}

	@Override
	public boolean hasEffect(ItemStack stack, EntityPlayer player) {
		MMDMaterial mat = Materials.getMaterialByName(MaterialNames.AQUARIUM);
		boolean rv = (stackIsArmorMaterial(stack, mat) 
				&& ((stack.getItem() instanceof IMMDObject) && 
						(((IMMDObject)stack.getItem()).getMMDMaterial() == mat))) && 
				(countArmorPieces(Materials.getMaterialByName(MaterialNames.AQUARIUM),player) > 0);
 
		return rv;
	}

	@Override
	public boolean hasEffect(ItemStack stack, EntityLivingBase ent) {
		return ent instanceof EntityPlayer?hasEffect(stack, (EntityPlayer)ent):false;
	}
}
