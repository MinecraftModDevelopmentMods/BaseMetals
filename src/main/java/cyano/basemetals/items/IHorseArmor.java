package cyano.basemetals.items;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseType;
import net.minecraft.item.ItemStack;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public interface IHorseArmor {
	
	/**
	 * 
	 * @return
	 */
    HorseType getArmorType();

    /**
     *
     * @param entity
     * @param stack
     * @return
     */
    String getArmorTexture(EntityHorse entity, ItemStack stack);
}
