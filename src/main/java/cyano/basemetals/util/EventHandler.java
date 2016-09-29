package cyano.basemetals.util;

import cyano.basemetals.init.Achievements;
import cyano.basemetals.init.Materials;
import cyano.basemetals.items.ItemMetalIngot;
import cyano.basemetals.items.ItemMetalShield;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
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
        if ((damage > 0.0F) && (activeItemStack != null) && ((activeItemStack.getItem() instanceof ItemMetalShield))) {
            int i = 1 + MathHelper.floor_float(damage);
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
                    player.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 0.8F, 0.8F + player.worldObj.rand.nextFloat() * 0.4F);
                }
            }
        }
    }

	
	/*
	@SubscribeEvent
	void event(ItemCraftedEvent event) {

		FMLLog.severe("BASEMETALS: An Item Was Crafted!");

		final Item item = event.crafting.getItem();

		if (item instanceof IMetalObject) {
			FMLLog.severe("BASEMETALS: It's one of ours!");

			final MetalMaterial material = ((IMetalObject) item).getMetalMaterial();

			if (item instanceof ItemMetalBlend)
				FMLLog.severe("BASEMETALS: Yes, It was a blend!");
		}
	}
*/

	@SubscribeEvent
	void event(ItemSmeltedEvent event) {
//		FMLLog.severe("BASEMETALS: An Item Was Smelted!");

		final Item item = event.smelting.getItem();

		if (item instanceof IMetalObject) {
//			FMLLog.severe("BASEMETALS: It's one of ours!");

			final MetalMaterial material = ((IMetalObject) item).getMetalMaterial();
			if (item instanceof ItemMetalIngot) {
//				FMLLog.severe("BASEMETALS: Yes, It was an ingot!");

				event.player.addStat(Achievements.this_is_new, 1);

				if (material == Materials.aquarium)
					event.player.addStat(Achievements.aquarium_maker, 1);
				else if (material == Materials.brass)
					event.player.addStat(Achievements.brass_maker, 1);
				else if (material == Materials.bronze)
					event.player.addStat(Achievements.bronze_maker, 1);
				else if (material == Materials.electrum)
					event.player.addStat(Achievements.electrum_maker, 1);
				else if (material == Materials.steel)
					event.player.addStat(Achievements.steel_maker, 1);
				else if (material == Materials.invar)
					event.player.addStat(Achievements.invar_maker, 1);
				else if (material == Materials.mithril)
					event.player.addStat(Achievements.mithril_maker, 1);
				else if (material == Materials.cupronickel)
					event.player.addStat(Achievements.cupronickel_maker, 1);
			}
		}
	}
}
