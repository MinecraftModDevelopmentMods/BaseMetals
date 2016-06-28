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

public class ItemMetalIngot extends net.minecraft.item.Item implements IOreDictionaryEntry, IMetalObject {

	protected final MetalMaterial metal;
	private final String oreDict;
	public ItemMetalIngot(MetalMaterial metal) {
		this.metal = metal;
		this.setCreativeTab(CreativeTabs.MATERIALS);
		this.oreDict = "ingot"+metal.getCapitalizedName();
	}

	public String getOreDictionaryName() {
		return oreDict;
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		crafter.addStat(Achievements.this_is_new, 1);

		if(metal == Materials.aquarium) crafter.addStat(Achievements.aquarium_maker, 1);
		if(metal == Materials.brass) crafter.addStat(Achievements.brass_maker, 1);
		if(metal == Materials.bronze) crafter.addStat(Achievements.bronze_maker, 1);
		if(metal == Materials.electrum) crafter.addStat(Achievements.electrum_maker, 1);
		if(metal == Materials.steel) crafter.addStat(Achievements.steel_maker, 1);
		if(metal == Materials.invar) crafter.addStat(Achievements.invar_maker, 1);
		if(metal == Materials.mithril) crafter.addStat(Achievements.mithril_maker, 1);
		if(metal == Materials.cupronickel) crafter.addStat(Achievements.cupronickel_maker, 1);
	}
}
