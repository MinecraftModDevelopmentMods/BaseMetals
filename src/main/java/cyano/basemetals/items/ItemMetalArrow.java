package cyano.basemetals.items;

import cyano.basemetals.entity.EntityCustomArrow;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMetalArrow extends ItemArrow implements IOreDictionaryEntry, IMetalObject {

	protected final MetalMaterial metal;
	private final String oreDict;

	/**
	 *
	 * @param metal The material to make the arrow from
	 */
	public ItemMetalArrow(MetalMaterial metal) {
		this.metal = metal;
		this.setCreativeTab(CreativeTabs.MATERIALS);
		this.oreDict = "arrow" + metal.getCapitalizedName();
	}

	/**
	 *
	 * @param worldIn The world
	 * @param stack The itemstack
	 * @param shooter The shooter
	 * @return The Custom Arrow
	 */
	public EntityCustomArrow createArrow(World worldIn, ItemStack stack, EntityPlayer shooter) {
		return new EntityCustomArrow(worldIn, stack, shooter);
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.metal;
	}

	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.metal;
	}
}
