package com.mcmoddev.lib.integration.plugins;

//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fml.common.event.FMLInterModComms;

//import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;

import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.util.Oredicts;

//import slimeknights.tconstruct.TinkerIntegration;
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
		if (material == null) {
			return;
		}

		if (material.fluid == null) {
			return;
		}

		Material tcmat = TinkerRegistry.getMaterial(material.getName());
		if( tcmat == Material.UNKNOWN ) {
			tcmat = new Material(material.getName(), material.getTintColor());
			TinkerRegistry.addMaterial(tcmat);
		}

		// Set fluid used, Set whether craftable, set whether castable, adds the
		// item with the value 144.
		tcmat.setFluid(material.fluid).setCraftable(craftable).setCastable(castable);

		// register the fluid for the material, 1 ingot is 144mB
		registerFluid( material, 144 );

		// register the material as being a possible Tool Forge material
		// somewhat hacky, but we need to keep the API changes minimal
		TinkerTools.registerToolForgeBlock(Oredicts.BLOCK+material.getCapitalizedName());

		// in here we should always have a nugget and an ingot
		tcmat.addItem(material.nugget, 1, Material.VALUE_Nugget);
		tcmat.addItem(material.ingot, 1, Material.VALUE_Ingot);
		tcmat.setRepresentativeItem(material.ingot);

		HashMap<String, Float> stats = new HashMap<String, Float>();
		completeMaterialStats(material, stats);

		// setup the stats for the item - first the tool part stats
		final HeadMaterialStats headStats = new HeadMaterialStats( stats.get("head_durability").intValue(), stats.get("mining_speed").floatValue(), stats.get("head_attack").floatValue(), stats.get("harvest_level").intValue());
		final HandleMaterialStats handleStats = new HandleMaterialStats(stats.get("body_modifier").floatValue(), stats.get("body_durability").intValue());
		final ExtraMaterialStats extraStats = new ExtraMaterialStats(stats.get("extra_durability").intValue());

		/*
		 * Things are oddly broken in this chunk 
 
		// then the bow-part stats
		final BowMaterialStats bowStats = new BowMaterialStats( stats.get("draw_speed").floatValue(), stats.get("range").floatValue(), stats.get("bow_damage").floatValue());
		final BowStringMaterialStats bowStringStats = new BowStringMaterialStats(stats.get("bowstring_modifier").floatValue());
		// The ArrowShaftMaterialStats line here isn't working...
		final ArrowShaftMaterialStats arrowShaftStats = new ArrowShaftMaterialStats(stats.get("shaft_modifer").floatValue(), stats.get("shaft_ammo").intValue() );
		final FletchingMaterialStats fletchingStats = new FletchingMaterialStats(stats.get("fletching_accuracy").floatValue(), stats.get("fletching_modifier").floatValue());
		final ProjectileMaterialStats projectileStats = new ProjectileMaterialStats();
		*/
		// add the base tool stats
		TinkerRegistry.addMaterialStats(tcmat, headStats); // Sets stats for head
		TinkerRegistry.addMaterialStats(tcmat, handleStats); // Sets Stats for handle
		TinkerRegistry.addMaterialStats(tcmat, extraStats); // Sets stats for everything else

		/*
		// now add the extended stats for bow/projectile
		TinkerRegistry.addMaterialStats(tcmat, bowStats); // Sets stats for Bow
		TinkerRegistry.addMaterialStats(tcmat, bowStringStats); // Sets stats for Bow String
		TinkerRegistry.addMaterialStats(tcmat, arrowShaftStats); // Sets stats for Arrow Shaft
		TinkerRegistry.addMaterialStats(tcmat, fletchingStats); // Sets stats for Fletching 
		TinkerRegistry.addMaterialStats(tcmat, projectileStats); // Sets stats for Projectile
		 */		

/*		if (trait != null) {
			String stats = "temporary placeholder"; // TODO: find out what goes here
			TinkerRegistry.addMaterialTrait(tcmat, trait, stats);
		}
*/
	}

	protected static void completeMaterialStats(MetalMaterial material, HashMap<String,Float> baseStats) {
		if( baseStats == null ) {
			throw new Error("baseStats cannot be null");
		}

		String[] possibleStats = new String[] { "head_durability", "mining_speed", "head_attack", "harvest_level",
												"body_modifier", "body_durability", 
												"extra_durability",
												"draw_speed", "range", "bow_damage",
												"bowstring_modifier",
												"fletching_accuracy", "fletching_modifier",
												"shaft_modifier", "shaft_ammo" };
		for( String s : possibleStats ) {
			if( !baseStats.containsKey(s) ) {
				float val;
				switch(s) {
				case "head_durability":
					val = material.getToolDurability();
					break;
				case "mining_speed":
					val = material.magicAffinity * 3 / 2;
					break;
				case "harvest_level":
					val = material.getToolHarvestLevel();
					break;
				case "head_attack":
					val = material.getBaseAttackDamage() * 2;
					break;
				case "body_durability":
					val = material.getToolDurability() / 6.8f;
					break;
				case "body_modifier":
					val = (material.hardness + material.magicAffinity * 2) / 9;
					break;
				case "extra_durability":
					val = material.getToolDurability() / 10;
					break;
				case "draw_speed":
					if (material.getToolDurability() < 204) {
						val = 1;
					} else {
						val = ((material.getToolDurability() - 200) + 1) / 10.0f;
						val -= Math.floor(val);
					}
					break;
				case "range":
					val = 15;
					break;
				case "bow_damage":
					val = material.getBaseAttackDamage() + 3;
					break;
				case "bowstring_modifier":
					val = 1;
					break;
				case "fletching_accuracy":
					val = 1;
					break;
				case "fletching_modifier":
					val = 1;
					break;
				case "shaft_modifier":
					val = 1;
					break;
				case "shaft_ammo":
					val = 1;
					break;
				default:
					val = 0;
				}
				val = Math.round(val * 100.0f) / 100.0f;
				baseStats.put(s, val);
			}
		}
	}
}
