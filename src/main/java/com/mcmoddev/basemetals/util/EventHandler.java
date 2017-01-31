package com.mcmoddev.basemetals.util;

import com.mcmoddev.basemetals.init.Achievements;
import com.mcmoddev.basemetals.material.IMetalObject;
import com.mcmoddev.basemetals.material.MetalMaterial;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.items.ItemMetalBlend;
import com.mcmoddev.lib.items.ItemMetalIngot;
import com.mcmoddev.lib.items.ItemMetalShield;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EventHandler {

	@SubscribeEvent
    public void attackEvent(LivingAttackEvent e) {
        float damage = e.getAmount();
        if (!(e.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) e.getEntityLiving();
        if (player.getActiveItemStack() == null) {
            return;
        }
        ItemStack activeItemStack = player.getActiveItemStack();
        if ((damage > 0.0F) && (activeItemStack != null) && (activeItemStack.getItem() instanceof ItemMetalShield)) {
            int i = 1 + MathHelper.floor(damage);
            activeItemStack.damageItem(i, player);
            if (activeItemStack.stackSize <= 0) {
                EnumHand enumhand = player.getActiveHand();
                ForgeEventFactory.onPlayerDestroyItem(player, activeItemStack, enumhand);
                if (enumhand == EnumHand.MAIN_HAND) {
                    player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, (ItemStack) null);
                }
                else {
                    player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, (ItemStack) null);
                }
                activeItemStack = null;
                if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                    player.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 0.8F, 0.8F + player.world.rand.nextFloat() * 0.4F);
                }
            }
        }
    }

	@SubscribeEvent
	void event(ItemCraftedEvent event) {
		final Item item = event.crafting.getItem();
		if (item instanceof IMetalObject) {
			final MetalMaterial material = ((IMetalObject) item).getMaterial();
			if (item instanceof ItemMetalBlend) {
				if (Options.ENABLE_ACHIEVEMENTS) {
//					event.player.addStat(Achievements.metallurgy, 1);
				}
			}
		}
	}

	@SubscribeEvent
	void event(ItemSmeltedEvent event) {
		final Item item = event.smelting.getItem();
		if (item instanceof IMetalObject) {
			final MetalMaterial material = ((IMetalObject) item).getMaterial();
			if (item instanceof ItemMetalIngot) {
				if (Options.ENABLE_ACHIEVEMENTS) {
//					event.player.addStat(Achievements.this_is_new, 1);
					if (material.getName().equals("aquarium")) {
						event.player.addStat(Achievements.aquarium_maker, 1);
					} else if (material.getName().equals("brass")) {
						event.player.addStat(Achievements.brass_maker, 1);
					} else if (material.getName().equals("bronze")) {
						event.player.addStat(Achievements.bronze_maker, 1);
					} else if (material.getName().equals("electrum")) {
						event.player.addStat(Achievements.electrum_maker, 1);
					} else if (material.getName().equals("steel")) {
						event.player.addStat(Achievements.steel_maker, 1);
					} else if (material.getName().equals("invar")) {
						event.player.addStat(Achievements.invar_maker, 1);
					} else if (material.getName().equals("mithril")) {
						event.player.addStat(Achievements.mithril_maker, 1);
					} else if (material.getName().equals("cupronickel")) {
						event.player.addStat(Achievements.cupronickel_maker, 1);
					}
				}
			}
		}
	}
}
