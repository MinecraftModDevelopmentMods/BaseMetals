package com.mcmoddev.lib.client.renderer;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

	public final ModelResourceLocation location;

	public FluidStateMapper(String resourceLocation) {
		this.location = new ModelResourceLocation(resourceLocation, "fluid");
	}

	public FluidStateMapper(ResourceLocation resourceLocation) {
		this.location = new ModelResourceLocation(resourceLocation, "fluid");
	}

	public FluidStateMapper(ModelResourceLocation modelResourceLocation) {
		this.location = modelResourceLocation;
	}

	@Nonnull
	@Override
	protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
		return location;
	}

	@Nonnull
	@Override
	public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
		return location;
	}
}
