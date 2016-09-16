package cyano.basemetals.items;

import cyano.basemetals.init.Achievements;
import cyano.basemetals.init.Materials;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Blends
 * @author DrCyano
 *
 */
public class ItemMetalBlend extends net.minecraft.item.Item implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial metal;
	private final String oreDict;

	/**
	 *
	 * @param metal
	 */
	public ItemMetalBlend(MetalMaterial metal) {
		this.metal = metal;
		this.setCreativeTab(CreativeTabs.MATERIALS);
		this.oreDict = "dust" + metal.getCapitalizedName(); // same oreDict entry as powder
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		// achievement
		if((this.getMetalMaterial() == Materials.aquarium)
				|| (this.getMetalMaterial() == Materials.brass)
				|| (this.getMetalMaterial() == Materials.bronze)
				|| (this.getMetalMaterial() == Materials.electrum)
				|| (this.getMetalMaterial() == Materials.invar)
				|| (this.getMetalMaterial() == Materials.steel))
			crafter.addStat(Achievements.metallurgy, 1);
	}

	@Override
	public String getOreDictionaryName() {
		return oreDict;
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
