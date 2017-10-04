package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.tinkers.ModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkersConstructRegistry;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * TiC Plugin, redesigned
 * 
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 *
 */
public class TinkersConstructBase implements IIntegration {

	public static final String PLUGIN_MODID = "tconstruct";

	private static boolean initDone = false;

	protected static final TinkersConstructRegistry registry = TinkersConstructRegistry.instance;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		initDone = true;
	}

	protected static void registerExtraMelting(@Nonnull final String materialName, @Nonnull final String type, @Nonnull final String name, @Nonnull final int amountPer) {
		String actName = String.format("%s:%s", type.toLowerCase(), name);
		registry.getMaterial(materialName).addExtraMelting(actName, amountPer);
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
		FluidStack output = FluidRegistry.getFluidStack(outputName, outputQty);
		FluidStack[] inputs = new FluidStack[inputName.length];
		
		for( int i = 0; i < inputName.length; i++ ) {
			inputs[i] = FluidRegistry.getFluidStack(inputName[i], inputQty[i]);
		}
		
		registry.registerAlloy(outputName, output, inputs);
	}

	protected static TCMaterial registerMaterial(@Nonnull final String materialName, @Nonnull final boolean craftable, @Nonnull final boolean castable) {
		MMDMaterial mat = Materials.getMaterialByName(materialName);
		
		return registerMaterial(mat, craftable, castable);
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
		TCMaterial tcm = registry.newMaterial(material.getName(), material.getTintColor());
		if( craftable )
			tcm.setCraftable();
		if( castable )
			tcm.setCastable();
		
		return tcm;
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

	protected void registerModifierRecipe(@Nonnull final String name, @Nonnull final ItemStack... recipe) {
		ModifierRegistry.setModifierRecipe(name, recipe);
	}

	protected void registerModifierItem(@Nonnull final String name, @Nonnull final ItemStack item) {
		ModifierRegistry.setModifierItem(name, item);
	}

	protected void registerModifierItem(@Nonnull final String name, @Nonnull final Item item) {
		ModifierRegistry.setModifierItem(name, item);
	}

	public void preInitSetup() {
		registry.setupIntegrations();
		registry.addMaterialStats();
	}

	public void setMaterialsVisible() {
		registry.setMaterialsVisible();
	}

	public void initSetup() {
		registry.resolveTraits();
		registry.integrationsInit();
		setMaterialsVisible();
		registry.registerMeltings();
	}

	public void postInitSetup() {
		setMaterialsVisible();
		registry.registerAlloys();
	}

	public void modifierSetup() {
		ModifierRegistry.initModifiers();
	}

	public void modifierRegister() {
		ModifierRegistry.registerModifiers();
	}
}
