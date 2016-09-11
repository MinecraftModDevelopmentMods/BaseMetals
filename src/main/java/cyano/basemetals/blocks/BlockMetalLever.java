package cyano.basemetals.blocks;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.BlockLever;
import net.minecraft.block.SoundType;

public class BlockMetalLever extends BlockLever implements IOreDictionaryEntry, IMetalObject {

	final MetalMaterial metal;

	public BlockMetalLever(MetalMaterial metal) {
		this.setSoundType(SoundType.METAL);
		this.metal = metal;
		this.blockHardness = metal.getMetalBlockHardness();
		this.blockResistance = metal.getBlastResistance();
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return this.metal;
	}

	@Override
	public String getOreDictionaryName() {
		return "lever" + this.metal.getCapitalizedName();
	}
}
