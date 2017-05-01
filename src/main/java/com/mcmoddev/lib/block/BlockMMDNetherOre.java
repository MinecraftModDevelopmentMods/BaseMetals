package com.mcmoddev.lib.block;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Ore Block
 */
public class BlockMMDNetherOre extends BlockOre implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material the ore is made from
	 */
	public BlockMMDNetherOre(MMDMaterial material) {
		super();
		this.setSoundType(SoundType.STONE);
		this.material = material;
		this.blockHardness = Math.max(5f, material.getOreBlockHardness());
		this.blockResistance = Math.max(1.5f, material.getBlastResistance() * 0.75f);
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
		// FMLLog.info(material.getName() + " ore harvest level set to " + material.getRequiredHarvestLevel());
	}

	@Override
	public int getExpDrop(final IBlockState bs, IBlockAccess w, final BlockPos coord, final int i) {
		return 0; // XP comes from smelting
	}

	@Override
	public boolean canEntityDestroy(IBlockState bs, IBlockAccess w, BlockPos coord, Entity entity) {
		if ((this == Materials.getMaterialByName(MaterialNames.STARSTEEL).getBlock(Names.ORE)) && (entity instanceof net.minecraft.entity.boss.EntityDragon))
			return false;
		return super.canEntityDestroy(bs, w, coord, entity);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
