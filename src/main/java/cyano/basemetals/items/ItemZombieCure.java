package cyano.basemetals.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemZombieCure extends net.minecraft.item.Item {

	public ItemZombieCure() {
		super();
		this.setCreativeTab(CreativeTabs.tabBrewing);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target) {
		if (target instanceof EntityZombie)
		{
			EntityZombie zombo = (EntityZombie)target;

			if (zombo.isVillager()) {
				PotionEffect weakness = new PotionEffect(Potion.weakness.id,200);
				zombo.addPotionEffect(weakness);
				player.setCurrentItemOrArmor(0, new ItemStack(net.minecraft.init.Items.golden_apple, 1, 0));
				zombo.interact(player);
				player.setCurrentItemOrArmor(0, stack);
				if (!player.capabilities.isCreativeMode) {
					--stack.stackSize;
				}
			}

			return true;
		} else {
			return false;
		}
	}
}
