package cyano.basemetals.blocks;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Metal Bars
 */
public class BlockMetalBars extends net.minecraft.block.BlockPane implements IOreDictionaryEntry, IMetalObject {

	final MetalMaterial metal;

	public BlockMetalBars(MetalMaterial metal) {
		super(Material.IRON, true);
		this.setSoundType(SoundType.METAL);
		this.metal = metal;
		this.blockHardness = metal.getMetalBlockHardness();
		this.blockResistance = metal.getBlastResistance();
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());
	}

	@Override
	public String getOreDictionaryName() {
		return "bars"+metal.getCapitalizedName();
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
