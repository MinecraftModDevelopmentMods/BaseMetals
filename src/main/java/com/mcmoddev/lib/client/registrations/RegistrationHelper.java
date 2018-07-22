package com.mcmoddev.lib.client.registrations;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.client.renderer.FluidStateMapper;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Blocks;
import com.mcmoddev.lib.init.Items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RegistrationHelper {

	/**
	 *
	 * @param item The Item to register
	 */
	@SideOnly(Side.CLIENT)
	public static void registerItemRender(@Nonnull final Item item) {
		registerRender(item);
	}

	/**
	 *
	 * @param name The name of the Item to register
	 */
	@SideOnly(Side.CLIENT)
	public static void registerItemRender(@Nonnull final String name) {
		final Item item = Items.getItemByName(name);

		registerRender(item);
	}

	/**
	 *
	 * @param block The Block to register
	 */
	@SideOnly(Side.CLIENT)
	public static void registerBlockRender(@Nonnull final Block block) {

		if ((block instanceof BlockDoor) || (block instanceof BlockSlab)) {
			return; // do not add door blocks or slabs
		}

		registerRender(Item.getItemFromBlock(block));
	}

	/**
	 *
	 * @param name The name of the Block to register
	 */
	@SideOnly(Side.CLIENT)
	public static void registerBlockRender(@Nonnull final String name) {
		final Block block = Blocks.getBlockByName(name);

		if ((block instanceof BlockDoor) || (block instanceof BlockSlab)) {
			return; // do not add door blocks or slabs
		}

		registerRender(Item.getItemFromBlock(block));
	}

	@SideOnly(Side.CLIENT)
	public static void registerFluidRender(@Nonnull final String name) {
		final Fluid fluid = FluidRegistry.getFluid(name);
		registerFluidRender(fluid);
	}

	/**
	 *
	 * @param fluid The Fluid to register
	 */
	@SideOnly(Side.CLIENT)
	public static void registerFluidRender(@Nonnull final Fluid fluid) {
		// final Block block = fluid.getBlock(); // Unfortunately this breaks when
		// another mod registered it's fluid before us.
		final Block block = com.mcmoddev.lib.init.Fluids.getFluidBlockByName(fluid.getName());
		final Item item = Item.getItemFromBlock(block);
		final ResourceLocation resourceLocation = block.getRegistryName();
		final FluidStateMapper mapper = new FluidStateMapper(
				resourceLocation.getNamespace() + ":" + fluid.getName());

		if (!resourceLocation.getNamespace()
				.equals(Loader.instance().activeModContainer().getModId())) {
			return;
		}

		if (item != null) {
			ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, mapper);
		}
		ModelLoader.setCustomStateMapper(block, mapper);
	}

	/**
	 *
	 * @param item The Item to register
	 */
	@SideOnly(Side.CLIENT)
	public static void registerRender(@Nonnull final Item item) {
		final ResourceLocation resourceLocation = item.getRegistryName();

		if (!resourceLocation.getNamespace()
				.equals(Loader.instance().activeModContainer().getModId())) {
			return;
		}

		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(resourceLocation, "inventory"));
	}

	private RegistrationHelper() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}
}
