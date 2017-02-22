package com.mcmoddev.lib.block;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Ore Block
 */
public class BlockMetalEndOre extends BlockOre implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material The material the ore is made from
	 */
	public BlockMetalEndOre(MetalMaterial material) {
		super();
		this.setSoundType(SoundType.STONE);
		this.material = material;
		this.blockHardness = Math.max(5f, material.getOreBlockHardness());
		this.blockResistance = Math.max(1.5f, material.getBlastResistance() * 0.75f);
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
		this.oreDict = Oredicts.OREEND + material.getCapitalizedName();
		// FMLLog.info(material.getName() + " ore harvest level set to " + material.getRequiredHarvestLevel());
	}

	@Override
	public int getExpDrop(final IBlockState bs, IBlockAccess w, final BlockPos coord, final int i) {
		return 0; // XP comes from smelting
	}

	@Override
	public boolean canEntityDestroy(IBlockState bs, IBlockAccess w, BlockPos coord, Entity entity) {
		if ((this == Materials.starsteel.ore) && (entity instanceof net.minecraft.entity.boss.EntityDragon))
			return false;
		return super.canEntityDestroy(bs, w, coord, entity);
	}

	public MetalMaterial getMetal() {
		return this.material;
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.material;
	}
}
