package cyano.basemetals.items;

import cyano.basemetals.entity.EntityCustomBolt;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMetalBolt extends Item implements IOreDictionaryEntry, IMetalObject {

	protected final MetalMaterial metal;
	private final String oreDict;

	/**
	 *
	 * @param metal
	 */
	public ItemMetalBolt(MetalMaterial metal) {
		this.metal = metal;
		this.setCreativeTab(CreativeTabs.MATERIALS);
		this.oreDict = "ammoBolt";
	}

	/**
	 * 
	 * @param worldIn
	 * @param stack
	 * @param shooter
	 * @return
	 */
	public EntityCustomBolt createBolt(World worldIn, ItemStack stack, EntityPlayer shooter) {
		return new EntityCustomBolt(worldIn, stack, shooter);
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return this.metal;
	}
}
