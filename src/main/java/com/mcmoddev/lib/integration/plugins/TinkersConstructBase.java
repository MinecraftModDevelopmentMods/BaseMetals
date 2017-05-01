package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.ModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkersConstructRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * TiC Plugin, redesigned
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 *
 */
public class TinkersConstructBase implements IIntegration {
	
	public static final String PLUGIN_MODID = "tconstruct";

	private static boolean initDone = false;

	protected static final TinkersConstructRegistry registry = TinkersConstructRegistry.getInstance();
	
	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled.get("tinkersconstruct")) {
			return;
		}

		initDone = true;
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
	protected static void registerExtraMelting(MMDMaterial base, Block block, int amountPer) {
		String materialName = base.getName();
		Fluid output = FluidRegistry.getFluid(materialName);

		if (block != null) {
			registry.registerMelting(Item.getItemFromBlock(block), output, amountPer);
		}
	}

	/**
	 * @param base
	 *            Material being melted
	 * @param amountPer
	 *            Amount of fluid per ingot
	 */
	protected static void registerFluid(MMDMaterial base, int amountPer) {
		registry.registerFluid(base, amountPer);
	}
	
	protected static void registerCasting(MMDMaterial base, int amountPer) {
		registry.registerBasin(base.getBlock(Names.BLOCK), base.getFluid(), amountPer * 9);
		registry.registerCasting(base.getItem(Names.INGOT), base.getFluid(), amountPer);
		registry.registerCasting(base.getItem(Names.NUGGET), base.getFluid(), amountPer/9 );
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
	protected static void registerAlloy(String outputName, int outputQty, String[] inputName, int[] inputQty) {
		registry.registerAlloy(outputName, outputQty, inputName, inputQty);
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
	protected static TCMaterial registerMaterial(MMDMaterial material, boolean craftable, boolean castable ) {
		return registry.getMaterial(material.getName(), material).setCraftable(craftable).setCastable(castable).setToolForge(true);
	}
	
	/**
	 * Creates a Tinkers Construct
	 * {@link slimeknights.tconstruct.library.materials.Material}
	 * 
	 * @param material
	 *            Information about the material and the material itself
	 */
	protected static void registerMaterial(TCMaterial material) {
		registry.getMaterial(material.getName()).settle();
	}

	public void registerModifierRecipe(String name, ItemStack... recipe) {
		ModifierRegistry.setModifierRecipe(name, recipe);
	}

	public void registerModifierItem(String name, ItemStack item) {
		ModifierRegistry.setModifierItem(name, item);
	}

	public void registerModifierItem(String name, Item item) {
		ModifierRegistry.setModifierItem(name, item);
	}
}
