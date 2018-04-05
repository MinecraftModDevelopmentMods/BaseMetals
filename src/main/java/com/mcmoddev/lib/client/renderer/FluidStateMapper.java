package com.mcmoddev.lib.client.renderer;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

	private final ModelResourceLocation location;

	public FluidStateMapper(final String resourceLocation) {
		this.location = new ModelResourceLocation(resourceLocation, "fluid");
	}

	public FluidStateMapper(final ResourceLocation resourceLocation) {
		this.location = new ModelResourceLocation(resourceLocation, "fluid");
	}

	public FluidStateMapper(final ModelResourceLocation modelResourceLocation) {
		this.location = modelResourceLocation;
	}

	@Nonnull
	@Override
	protected ModelResourceLocation getModelResourceLocation(@Nonnull final IBlockState state) {
		return this.location;
	}

	@Nonnull
	@Override
	public ModelResourceLocation getModelLocation(@Nonnull final ItemStack stack) {
		return this.location;
	}
}
