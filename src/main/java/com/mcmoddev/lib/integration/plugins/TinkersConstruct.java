package com.mcmoddev.lib.integration.plugins;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import org.apache.commons.lang3.StringUtils;

import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.init.Materials;

import slimeknights.tconstruct.TinkerIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.BowStringMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.ProjectileMaterialStats;
import slimeknights.tconstruct.library.traits.ITrait;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class TinkersConstruct implements IIntegration {

	public static final String PLUGIN_MODID = "tconstruct";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableTinkersConstruct) {
			return;
		}

		initDone = true;
	}

	/**
	 *
	 * @param fluid the fluid to register
	 * @param toolforge Should this have a toolforge
	 */
	protected static void registerFluid(Fluid fluid, boolean toolforge) {
		final NBTTagCompound message = new NBTTagCompound();
		message.setString("fluid", fluid.getName());
		message.setString("ore", StringUtils.capitalize(fluid.getName()));
		message.setBoolean("toolforge", toolforge);
		// message.setTag("alloy", alloysTagList); // you can also send an alloy with the registration (see below)

		// send the NBT to TCon
		FMLInterModComms.sendMessage(PLUGIN_MODID, "integrateSmeltery", message);
	}

	/**
	 *
	 * @param outputName The alloy to output
	 * @param outputQty The amount to output
	 * @param inputName Array of input
	 * @param inputQty Array of quantities for input
	 */
	protected static void registerAlloy(String outputName, int outputQty, String[] inputName, int[] inputQty) {
		if (inputName.length != inputQty.length) {
			throw new RuntimeException("Alloy must have the same amount of inputName and intQty");
		}

		FluidStack output = FluidRegistry.getFluidStack(outputName, outputQty);
		FluidStack[] input = new FluidStack[inputName.length];
		for( int i = 0; i < inputName.length; i++ ) {
			input[i] = FluidRegistry.getFluidStack(inputName[i], inputQty[i]);
		}
		
		TinkerRegistry.registerAlloy(output, input);
	}

	protected static void registerMaterial(String name, boolean craftable, boolean castable) {
		registerMaterial(Materials.getMaterialByName(name), craftable, castable, null);
	}

	/**
	 * Creates a Tinkers Construct {@link slimeknights.tconstruct.library.materials.Material}
	 * @param material Material identifier
	 * @param craftable If this be crafted
	 * @param castable If this can be casted
	 * @param trait to apply
	 */
	protected static void registerMaterial(MetalMaterial material, boolean craftable, boolean castable, ITrait trait) {

//		MetalMaterial material = Materials.getMaterialByName(name);
		if (material == null) {
			return;
		}

		if (material.fluid == null) {
			return;
		}

//		Fluid fluid;
//		int tintColor;

//		if (material != null) {
//		tintColor = material.getTintColor();
//		fluid = material.fluid;
//		} else {
//			tintColor = 0xFFD8D8D8; // Hack for Mercury as it doesn't have a metalMaterial
//			fluid = Fluids.getFluidByName(name);
//		}

		final Material tcmaterial = new Material(material.getName(), material.getTintColor());

		final HeadMaterialStats headStats = new HeadMaterialStats(material.getToolDurability(), material.magicAffinity * 3 / 2, material.getBaseAttackDamage() * 2, material.getToolHarvestLevel());

		final HandleMaterialStats handleStats = new HandleMaterialStats((int) (material.hardness + material.magicAffinity * 2) / 9, (int) (material.getToolDurability() / 6.8));

		final ExtraMaterialStats extraStats = new ExtraMaterialStats(material.getToolDurability() / 10);
		/*
	    final BowMaterialStats bowStats = new BowMaterialStats(1f, 1f, 0f);
	    final BowStringMaterialStats bowStringStats = new BowStringMaterialStats(1f);
	    final ArrowShaftMaterialStats arrowShaftStats = new ArrowShaftMaterialStats(1f, 0);
	    final FletchingMaterialStats fletchingStats = new FletchingMaterialStats(1f, 1f);
	    final ProjectileMaterialStats projectileStats = new ProjectileMaterialStats();
	    */

		registerFluid(material.fluid, true); // Register the fluid with tinkers

		TinkerRegistry.addMaterialStats(tcmaterial, headStats); // Sets stats for head
		TinkerRegistry.addMaterialStats(tcmaterial, handleStats); // Sets Stats for handle
		TinkerRegistry.addMaterialStats(tcmaterial, extraStats); // Sets stats for everything else
		/*
		TinkerRegistry.addMaterialStats(tcmaterial, bowStats); // Sets stats for Bow
		TinkerRegistry.addMaterialStats(tcmaterial, bowStringStats); // Sets stats for Bow String
		TinkerRegistry.addMaterialStats(tcmaterial, arrowShaftStats); // Sets stats for Arrow Shaft
		TinkerRegistry.addMaterialStats(tcmaterial, fletchingStats); // Sets stats for Fletching 
		TinkerRegistry.addMaterialStats(tcmaterial, projectileStats); // Sets stats for Projectile
		*/

		if (trait != null) {
			String stats = "temporary placeholder"; // TODO: find out what goes here
			TinkerRegistry.addMaterialTrait(tcmaterial, trait, stats);
		}

		// Set fluid used, Set whether craftable, set whether castable, adds the
		// item with the value 144.
		tcmaterial.setFluid(material.fluid).setCraftable(craftable).setCastable(castable);
		tcmaterial.addItem(material.nugget, 1, Material.VALUE_Nugget);
		tcmaterial.addItem(material.ingot, 1, Material.VALUE_Ingot);
		tcmaterial.setRepresentativeItem(material.ingot); // Use this as the picture

		// TinkerIntegration.integrate(tcmaterial);
		TinkerIntegration.integrate(tcmaterial, material.fluid, StringUtils.capitalize(material.fluid.getName()));
	}
}
