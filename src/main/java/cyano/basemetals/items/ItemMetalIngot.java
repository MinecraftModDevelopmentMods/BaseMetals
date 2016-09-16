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
 * Ingots
 *
 * @author DrCyano
 *
 */
public class ItemMetalIngot extends net.minecraft.item.Item implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial metal;
	private final String oreDict;

	/**
	 *
	 * @param metal
	 */
	public ItemMetalIngot(MetalMaterial metal) {
		this.metal = metal;
		this.setCreativeTab(CreativeTabs.MATERIALS);
		this.oreDict = "ingot" + metal.getCapitalizedName();
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		crafter.addStat(Achievements.this_is_new, 1);

		if(this.getMetalMaterial() == Materials.aquarium)
			crafter.addStat(Achievements.aquarium_maker, 1);
		else if(this.getMetalMaterial() == Materials.brass)
			crafter.addStat(Achievements.brass_maker, 1);
		else if(this.getMetalMaterial() == Materials.bronze)
			crafter.addStat(Achievements.bronze_maker, 1);
		else if(this.getMetalMaterial() == Materials.electrum)
			crafter.addStat(Achievements.electrum_maker, 1);
		else if(this.getMetalMaterial() == Materials.steel)
			crafter.addStat(Achievements.steel_maker, 1);
		else if(this.getMetalMaterial() == Materials.invar)
			crafter.addStat(Achievements.invar_maker, 1);
		else if(this.getMetalMaterial() == Materials.mithril)
			crafter.addStat(Achievements.mithril_maker, 1);
		else if(this.getMetalMaterial() == Materials.cupronickel)
			crafter.addStat(Achievements.cupronickel_maker, 1);
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
