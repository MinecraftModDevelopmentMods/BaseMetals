package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.BlockButton;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMMDButton extends BlockButton implements IOreDictionaryEntry, IMMDObject {

	final MMDMaterial material;
	private final String oreDict;

	public BlockMMDButton(MMDMaterial material) {
		super(false);
		this.setSoundType(SoundType.METAL);
		this.material = material;
		this.oreDict = Oredicts.BUTTON + this.material.getCapitalizedName();
		this.blockHardness = material.getBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
	}

	@Override
	protected void playClickSound(EntityPlayer player, World worldIn, BlockPos pos) {
		worldIn.playSound(player, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
	}

	@Override
	protected void playReleaseSound(World worldIn, BlockPos pos) {
		worldIn.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
	}

	@Override
	public MMDMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MMDMaterial getMetalMaterial() {
		return this.material;
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}
}
