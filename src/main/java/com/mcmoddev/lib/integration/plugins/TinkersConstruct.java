package com.mcmoddev.lib.integration.plugins;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;

import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMetalMaterial;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.BowStringMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.tools.TinkerTools;

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
	 * @param base Material being melted
	 * @param amountPer Amount of fluid per ingot
	 */
	protected static void registerFluid(MetalMaterial base, int amountPer) {
		String materialName = base.getName();
		Fluid output = FluidRegistry.getFluid(materialName);
		String oreDictName = base.getCapitalizedName();

		// hacky fix for Coal being itemCoal and not ingotCoal
		if (base.getName() == "coal")
			TinkerRegistry.registerMelting("itemCoal", output, 144);

		if (base.hasOre)
			TinkerRegistry.registerMelting(Oredicts.ORE + oreDictName, output, amountPer * 2);
		if (base.block != null)
			TinkerRegistry.registerMelting(Oredicts.BLOCK + oreDictName, output, amountPer * 9);
		if (base.ingot != null)
			TinkerRegistry.registerMelting(Oredicts.INGOT + oreDictName, output, amountPer);
		if (base.nugget != null)
			TinkerRegistry.registerMelting(Oredicts.NUGGET + oreDictName, output, amountPer / 9);
		if (base.powder != null)
			TinkerRegistry.registerMelting(Oredicts.DUST + oreDictName, output, amountPer);
		if (base.smallpowder != null)
			TinkerRegistry.registerMelting(Oredicts.DUSTSMALL + oreDictName, output, amountPer / 9);
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
		TCMetalMaterial m = new TCMetalMaterial(material);
		m.craftable = craftable;
		m.castable = castable;
		if( trait != null ) m.addTrait(trait);
		
		registerMaterial(m);
	}
	
	/**
	 * Creates a Tinkers Construct {@link slimeknights.tconstruct.library.materials.Material}
	 * @param material Information about the material and the material itself
	 */
	protected static void registerMaterial(TCMetalMaterial material) {
		if (material == null) {
			return;
		}

		if (material.metalmaterial.fluid == null) {
			return;
		}

		Material tcmat = TinkerRegistry.getMaterial(material.metalmaterial.getName());
		if( tcmat == Material.UNKNOWN ) {
			tcmat = new Material(material.metalmaterial.getName(), material.metalmaterial.getTintColor());
			TinkerRegistry.addMaterial(tcmat);
		}

		// Set fluid used, Set whether craftable, set whether castable, adds the
		// item with the value 144.
		tcmat.setFluid(material.metalmaterial.fluid).setCraftable(material.craftable).setCastable(material.castable);

		// register the fluid for the material, 1 ingot is 144mB
		registerFluid( material.metalmaterial, 144 );

		// register the material as being a possible Tool Forge material
		// somewhat hacky, but we need to keep the API changes minimal
		if( material.toolforge ) {
			TinkerTools.registerToolForgeBlock(Oredicts.BLOCK+material.metalmaterial.getCapitalizedName());
		}
		
		// in here we should always have a nugget and an ingot
		tcmat.addItem(material.metalmaterial.nugget, 1, Material.VALUE_Nugget);
		tcmat.addItem(material.metalmaterial.ingot, 1, Material.VALUE_Ingot);
		tcmat.setRepresentativeItem(material.metalmaterial.ingot);

		// setup the stats for the item - first the tool part stats
		final HeadMaterialStats headStats = new HeadMaterialStats( material.headDurability, material.miningSpeed, material.headAttackDamage, material.miningLevel);
		final HandleMaterialStats handleStats = new HandleMaterialStats(material.bodyModifier, material.bodyDurability);
		final ExtraMaterialStats extraStats = new ExtraMaterialStats(material.extraDurability);

		/*
		 * Things are oddly broken in this chunk 
         */
		// then the bow-part stats
		final BowMaterialStats bowStats = new BowMaterialStats( material.bowDrawingSpeed, material.bowRange, material.bowDamage);
		final BowStringMaterialStats bowStringStats = new BowStringMaterialStats(material.bowstringModifier);
		final ArrowShaftMaterialStats arrowShaftStats = new ArrowShaftMaterialStats( material.shaftModifier, material.shaftBonusAmmo );
		final FletchingMaterialStats fletchingStats = new FletchingMaterialStats(material.fletchingAccuracy, material.fletchingModifier);
		
		// add the base tool stats
		TinkerRegistry.addMaterialStats(tcmat, headStats); // Sets stats for head
		TinkerRegistry.addMaterialStats(tcmat, handleStats); // Sets Stats for handle
		TinkerRegistry.addMaterialStats(tcmat, extraStats); // Sets stats for everything else

		// now add the extended stats for bow/projectile
		TinkerRegistry.addMaterialStats(tcmat, bowStats); // Sets stats for Bow
		TinkerRegistry.addMaterialStats(tcmat, bowStringStats); // Sets stats for Bow String
		TinkerRegistry.addMaterialStats(tcmat, arrowShaftStats); // Sets stats for Arrow Shaft
		TinkerRegistry.addMaterialStats(tcmat, fletchingStats); // Sets stats for Fletching 

		if (material.hasTraits) {
			ITrait t;
			
			String[] traitLocs = new String[] { null, MaterialTypes.HEAD, MaterialTypes.HANDLE, MaterialTypes.EXTRA, MaterialTypes.BOW, MaterialTypes.BOWSTRING, MaterialTypes.PROJECTILE, MaterialTypes.SHAFT, MaterialTypes.FLETCHING };

			for( String tLoc : traitLocs ) {
				t = material.getTrait(tLoc);
				if( t != null ) TinkerRegistry.addMaterialTrait(tcmat, t, null);
			}
		}

	}

}
