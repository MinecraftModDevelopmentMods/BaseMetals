package cyano.basemetals.blocks;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Ore Block
 */
public class BlockMetalOre extends BlockOre implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial metal;
	private final String oreDict;

	/**
	 * 
	 * @param metal
	 */
	public BlockMetalOre(MetalMaterial metal) {
		super();
		this.setSoundType(SoundType.STONE);
		this.metal = metal;
		this.blockHardness = Math.max(5f, metal.getOreBlockHardness());
		this.blockResistance = Math.max(1.5f, metal.getBlastResistance() * 0.75f);
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());
		this.oreDict = "ore" + metal.getCapitalizedName();
	//	FMLLog.info(metal.getName() + " ore harvest level set to "+metal.getRequiredHarvestLevel());
	}

	@Override
	public int getExpDrop(final IBlockState bs, IBlockAccess w, final BlockPos coord, final int i) {
		return 0; // XP comes from smelting
	}
	@Override
	public boolean canEntityDestroy(IBlockState bs, IBlockAccess w, BlockPos coord, Entity entity) {
		if(this == cyano.basemetals.init.Blocks.starsteel_ore && entity instanceof net.minecraft.entity.boss.EntityDragon) return false;
		return super.canEntityDestroy(bs, w, coord, entity);
	}

	public MetalMaterial getMetal() {
		return metal;
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
