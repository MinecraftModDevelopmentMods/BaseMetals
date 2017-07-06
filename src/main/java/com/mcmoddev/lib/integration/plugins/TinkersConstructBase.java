package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.tinkers.ModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkersConstructRegistry;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * TiC Plugin, redesigned
 * 
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 *
 */
public class TinkersConstructBase implements IIntegration {

	public static final String PLUGIN_MODID = "tconstruct";

	private static boolean initDone = false;

	protected static final TinkersConstructRegistry registry = TinkersConstructRegistry.getInstance();

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled(PLUGIN_MODID)) {
			return;
		}

		initDone = true;
	}

	protected static void registerExtraMelting(@Nonnull final String materialName, @Nonnull final Block block, @Nonnull final int amountPer) {
		registerExtraMelting(Materials.getMaterialByName(materialName), block, amountPer);
	}

	/**
	 * Variant of fluid registration to allow for late registration of specific
	 * materials that have a different amount of fluid per block than normal.
	 * Setup specifically for NetherMetals and EndMetals.
	 *
	 * @param base
	 *            MMDMaterial that is the base material for this
	 * @param block
	 *            Block to register
	 * @param amountPer
	 *            the amount of fluid per this block
	 */
	protected static void registerExtraMelting(@Nonnull final MMDMaterial base, @Nonnull final Block block, @Nonnull final int amountPer) {
		final String materialName = base.getName();
		final Fluid output = FluidRegistry.getFluid(materialName);

		if (block != null) {
			registry.registerMelting(Item.getItemFromBlock(block), output, amountPer);
		}
	}

	protected static void registerFluid(@Nonnull final String materialName, @Nonnull final int amountPer) {
		registerFluid(Materials.getMaterialByName(materialName), amountPer);
	}

	/**
	 * @param base
	 *            Material being melted
	 * @param amountPer
	 *            Amount of fluid per ingot
	 */
	protected static void registerFluid(@Nonnull final MMDMaterial material, @Nonnull final int amountPer) {
		registry.registerFluid(material, amountPer);
	}

	protected static void registerCasting(@Nonnull final String materialName, @Nonnull final int amountPer) {
		registerCasting(Materials.getMaterialByName(materialName), amountPer);
	}

	protected static void registerCasting(@Nonnull final MMDMaterial material, @Nonnull final int amountPer) {
		final Fluid fluid = material.getFluid();

		registry.registerBasin(material.getBlock(Names.BLOCK), fluid, amountPer * 9);
		registry.registerCasting(material.getItem(Names.INGOT), fluid, amountPer);
		registry.registerCasting(material.getItem(Names.NUGGET), fluid, amountPer / 9);
	}

	/**
	 *
	 * @param outputName
	 *            The alloy to output
	 * @param outputQty
	 *            The amount to output
	 * @param inputName
	 *            Array of input
	 * @param inputQty
	 *            Array of quantities for input
	 */
	protected static void registerAlloy(@Nonnull final String outputName, @Nonnull final int outputQty, @Nonnull final String[] inputName, @Nonnull final int[] inputQty) {
		registry.registerAlloy(outputName, outputQty, inputName, inputQty);
	}

	protected static TCMaterial registerMaterial(@Nonnull final String materialName, @Nonnull final boolean craftable, @Nonnull final boolean castable) {
		return registerMaterial(Materials.getMaterialByName(materialName), craftable, castable);
	}
	/**
	 * Creates a Tinkers Construct
	 * {@link slimeknights.tconstruct.library.materials.Material}
	 * 
	 * @param material
	 *            Material identifier
	 * @param craftable
	 *            If this be crafted
	 * @param castable
	 *            If this can be casted
	 * @return a handle for potential, further manipulation of the material
	 */
	protected static TCMaterial registerMaterial(@Nonnull final MMDMaterial material, @Nonnull final boolean craftable, @Nonnull final boolean castable) {
		return registry.getMaterial(material.getName(), material).setCraftable(craftable).setCastable(castable).setToolForge(true);
	}

	/**
	 * Creates a Tinkers Construct
	 * {@link slimeknights.tconstruct.library.materials.Material}
	 * 
	 * @param material
	 *            Information about the material and the material itself
	 */
	protected static void registerMaterial(@Nonnull final TCMaterial material) {
		registry.getMaterial(material.getName()).settle();
	}

	public void registerModifierRecipe(@Nonnull final String name, @Nonnull final ItemStack... recipe) {
		ModifierRegistry.setModifierRecipe(name, recipe);
	}

	public void registerModifierItem(@Nonnull final String name, @Nonnull final ItemStack item) {
		ModifierRegistry.setModifierItem(name, item);
	}

	public void registerModifierItem(@Nonnull final String name, @Nonnull final Item item) {
		ModifierRegistry.setModifierItem(name, item);
	}
}
