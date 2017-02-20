package com.mcmoddev.lib.integration.plugins;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;

import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MetalMaterial;

import java.util.Random;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.integration.plugins.tinkers.ModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMetalMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.TraitRegistry;

import slimeknights.tconstruct.TinkerIntegration;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.BowStringMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.AbstractTrait;
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
	 * Variant of fluid registration to allow for late registration of specific
	 * materials that have a different amount of fluid per block than normal.
	 * Setup specifically for NetherMetals and EndMetals.
	 *
	 * @param base
	 *            MetalMaterial that is the base material for this
	 * @param block
	 *            Block to register
	 * @param amountPer
	 *            the amount of fluid per this block
	 */
	protected static void registerExtraMelting(MetalMaterial base, Block block, int amountPer) {
		String materialName = base.getName();
		Fluid output = FluidRegistry.getFluid(materialName);

		try {
			if (block != null)
				TinkerRegistry.registerMelting(Item.getItemFromBlock(block), output, amountPer);
		} catch (final Exception ex) {
			FMLLog.severe("Tried registering %s for melting, caught exception: %s", block.getUnlocalizedName(), ex.getMessage());
		}
	}

	/**
	 * @param base
	 *            Material being melted
	 * @param amountPer
	 *            Amount of fluid per ingot
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
		if (base.oreNether != null)
			TinkerRegistry.registerMelting(base.oreNether, output, amountPer * 4);
		if (base.oreEnd != null)
			TinkerRegistry.registerMelting(base.oreEnd, output, amountPer * 4);

		if (base.half_slab != null)
			TinkerRegistry.registerMelting(base.half_slab, output, (amountPer * 4) + (amountPer / 2));

		if (base.wall != null)
			TinkerRegistry.registerMelting(base.wall, output, amountPer * 9);

		if (base.boots != null)
			TinkerRegistry.registerMelting(base.boots, output, amountPer * 4);

		if (base.helmet != null)
			TinkerRegistry.registerMelting(base.helmet, output, amountPer * 5);

		if (base.chestplate != null)
			TinkerRegistry.registerMelting(base.chestplate, output, amountPer * 8);

		if (base.leggings != null)
			TinkerRegistry.registerMelting(base.leggings, output, amountPer * 7);

		if (base.shears != null)
			TinkerRegistry.registerMelting(base.shears, output, amountPer * 2);

		if (base.pressure_plate != null)
			TinkerRegistry.registerMelting(base.pressure_plate, output, amountPer * 2);

		if (base.bars != null)
			TinkerRegistry.registerMelting(base.bars, output, ((amountPer / 9) * 3) + 6); // Fun math

		if (base.rod != null)
			TinkerRegistry.registerMelting(base.rod, output, amountPer / 2);

		if (base.door != null)
			TinkerRegistry.registerMelting(base.door, output, amountPer * 2);

		if (base.trapdoor != null)
			TinkerRegistry.registerMelting(base.trapdoor, output, amountPer * 4);

		if (base.button != null)
			TinkerRegistry.registerMelting(base.button, output, (amountPer / 9) * 2);
	}

	protected static void registerCasting(MetalMaterial base, int amountPer) {
		if (base.block != null)
			TinkerRegistry.registerBasinCasting(new ItemStack(base.block), null, base.fluid, amountPer * 9);
		if (base.ingot != null)
			TinkerRegistry.registerTableCasting(new ItemStack(base.ingot), null, base.fluid, amountPer);
		if (base.nugget != null)
			TinkerRegistry.registerTableCasting(new ItemStack(base.nugget), null, base.fluid, amountPer / 9);
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
		if (inputName.length != inputQty.length) {
			throw new RuntimeException("Alloy must have the same amount of inputName and intQty");
		}

		FluidStack output = FluidRegistry.getFluidStack(outputName, outputQty);
		FluidStack[] input = new FluidStack[inputName.length];
		for (int i = 0; i < inputName.length; i++) {
			FluidStack temp = FluidRegistry.getFluidStack(inputName[i], inputQty[i]);
			if (temp != null) {
				input[i] = FluidRegistry.getFluidStack(inputName[i], inputQty[i]);
			} else {
				String mess = String.format("BaseMetals-TCon: material %s is not a registered fluid, alloy recipe %s ignored.", inputName[i], outputName);
				BaseMetals.logger.error(mess);
				return;
			}
		}

		TinkerRegistry.registerAlloy(output, input);
	}

	protected static void registerMaterial(String name, boolean craftable, boolean castable) {
		registerMaterial(Materials.getMaterialByName(name), craftable, castable, null);
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
	 * @param trait
	 *            to apply
	 */
	protected static void registerMaterial(MetalMaterial material, boolean craftable, boolean castable, ITrait trait) {
		TCMetalMaterial m = new TCMetalMaterial(material);
		m.craftable = craftable;
		m.castable = castable;
		if (trait != null) {
			String randName = ((Integer) (new Random()).nextInt()).toString();
			TraitRegistry.addTrait(randName, trait);
			m.addTrait(randName);
		}

		registerMaterial(m);
	}

	/**
	 * Creates a Tinkers Construct
	 * {@link slimeknights.tconstruct.library.materials.Material}
	 * 
	 * @param material
	 *            Information about the material and the material itself
	 */
	protected static void registerMaterial(TCMetalMaterial material) {
		if (material == null) {
			return;
		}

		if (material.metalmaterial.fluid == null) {
			return;
		}

		// make sure the name used here is all lower case
		Material tcmat = new Material(material.metalmaterial.getName().toLowerCase(), TextFormatting.WHITE);

		if (material.hasTraits) {
			for (String s : material.getTraitLocs()) {
				for (AbstractTrait t : material.getTraits(s)) {
					tcmat.addTrait(t, s == "general" ? null : s);
				}
			}
		}

		if (TinkerRegistry.getMaterial(tcmat.identifier) != Material.UNKNOWN) {
			return;
		}

		// register the fluid for the material, 1 ingot is 144mB
		registerFluid(material.metalmaterial, material.amountPerOre);

		tcmat.addItem(material.metalmaterial.ingot, 1, Material.VALUE_Ingot);
		tcmat.addItemIngot(Oredicts.INGOT + material.metalmaterial.getName());
		tcmat.setRepresentativeItem(material.metalmaterial.ingot);

		// setup the stats for the item - first the tool part stats
		final HeadMaterialStats headStats = new HeadMaterialStats(material.headDurability, material.miningSpeed, material.headAttackDamage, material.miningLevel);
		final HandleMaterialStats handleStats = new HandleMaterialStats(material.bodyModifier, material.bodyDurability);
		final ExtraMaterialStats extraStats = new ExtraMaterialStats(material.extraDurability);

		// then the bow-part stats
		final BowMaterialStats bowStats = new BowMaterialStats(material.bowDrawingSpeed, material.bowRange, material.bowDamage);
		final BowStringMaterialStats bowStringStats = new BowStringMaterialStats(material.bowstringModifier);
		final ArrowShaftMaterialStats arrowShaftStats = new ArrowShaftMaterialStats(material.shaftModifier, material.shaftBonusAmmo);
		final FletchingMaterialStats fletchingStats = new FletchingMaterialStats(material.fletchingAccuracy, material.fletchingModifier);

		TinkerRegistry.addMaterialStats(tcmat, headStats);
		TinkerRegistry.addMaterialStats(tcmat, handleStats);
		TinkerRegistry.addMaterialStats(tcmat, extraStats);
		TinkerRegistry.addMaterialStats(tcmat, bowStats);
		TinkerRegistry.addMaterialStats(tcmat, bowStringStats);
		TinkerRegistry.addMaterialStats(tcmat, arrowShaftStats);
		TinkerRegistry.addMaterialStats(tcmat, fletchingStats);

		TinkerRegistry.addMaterial(tcmat);

		// everything we send through here has a fluid - fluid and craftable are opposites
		tcmat.setFluid(material.metalmaterial.fluid).setCastable(material.castable).setCraftable(material.craftable);

		String base = material.metalmaterial.getName();
		String suffix = base.substring(0, 1).toUpperCase() + base.substring(1);
		MaterialIntegration m = new MaterialIntegration(null, material.metalmaterial.fluid, suffix);
		if (material.toolforge) {
			m.toolforge();
		}

		TinkerIntegration.integrationList.add(m);
		m.integrate();
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
