package cyano.basemetals.blocks;

import cyano.basemetals.init.Blocks;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;

/**
 * 
 * @author Jasmine Iwanek
 *
 */
public class BlockMetalStairs extends BlockStairs implements IOreDictionaryEntry, IMetalObject {

	final MetalMaterial metal;

	/**
	 * 
	 * @param metal
	 */
	public BlockMetalStairs(MetalMaterial metal) {
		super(Blocks.getBlockByName(metal.getName() + "_block").getDefaultState());
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
		return "stairs" + this.metal.getCapitalizedName();
	}
}
