package cyano.basemetals.items;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.items.IHorseArmor;
import cyano.basemetals.utils.HorseUtilities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMetalHorseArmor extends Item implements IMetalObject, IHorseArmor {

	private final MetalMaterial metal;

	/**
	 *
	 * @param m
	 */
	public ItemMetalHorseArmor(MetalMaterial m) {
		this.metal = m;
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.setMaxStackSize(1);
	}

	@Override
	public HorseType getArmorType() {
		return HorseUtilities.getType("test", 1024);
	}

	@Override
	public String getArmorTexture(EntityHorse entity, ItemStack stack) {
		return "basemetals:textures/entity/horse/armor/horse_armor_" + this.metal.getName() + ".png";
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return this.metal;
	}
}
