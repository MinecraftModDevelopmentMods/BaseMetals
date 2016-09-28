package cyano.basemetals.blocks;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Metal Bars
 * 
 * @author DrCyano
 *
 */
public class BlockMetalBars extends net.minecraft.block.BlockPane implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial metal;
	private final String oreDict;

	/**
	 *
	 * @param metal
	 */
	public BlockMetalBars(MetalMaterial metal) {
		super(Material.IRON, true);
		this.setSoundType(SoundType.METAL);
		this.metal = metal;
		this.blockHardness = metal.getMetalBlockHardness();
		this.blockResistance = metal.getBlastResistance();
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());
		this.oreDict = "bars" + metal.getCapitalizedName();
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
