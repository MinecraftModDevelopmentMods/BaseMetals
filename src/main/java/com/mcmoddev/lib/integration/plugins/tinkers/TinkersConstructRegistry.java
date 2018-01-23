package com.mcmoddev.lib.integration.plugins.tinkers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.AbstractTrait;

/**
 * Created by Daniel Hazelton on 2/21/2017.
 *
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 */

public class TinkersConstructRegistry {

	private final Map<String, TCMaterial> registry = new HashMap<>();
	private final List<MaterialIntegration> integrations = new ArrayList<>();

	private static TinkersConstructRegistry instance;

	/**
	 * Hacky-ass shit constructor. Be warned that this creates a semi-circular data
	 * structure.
	 * 
	 * @constructor
	 */
	private TinkersConstructRegistry() {
		if (instance == null)
			instance = this;
	}

	/**
	 * Return the existing instance or create a new one - if one doesn't exist - and
	 * return it
	 * 
	 * @return an instance of the registry
	 */
	public static TinkersConstructRegistry getInstance() {
		if (instance == null)
			instance = new TinkersConstructRegistry();

		return instance;
	}

	/**
	 * Has a material with the given name been registered ?
	 * 
	 * @param name
	 *            The name to test for
	 * @return Boolean truth value of whether or not the item has been registered
	 */
	public boolean isRegistered(@Nonnull final String name) {
		return registry.containsKey(name);
	}

	/**
	 * Used internally to add an entry to the registry
	 * 
	 * @param name
	 *            name of material being registered
	 * @param mat
	 *            Material to be registered
	 * @return the material
	 */
	private TCMaterial put(@Nonnull final String name, @Nonnull final TCMaterial mat) {
		if (isRegistered(name)) {
			return registry.get(name);
		}
		registry.put(name, mat);
		return mat;
	}

	/**
	 * Used internally to access the registry and grab an entry. Helps to minimize
	 * some potential issues.
	 * 
	 * @param name
	 *            name used for material during registration
	 * @return the request material
	 */
	private TCMaterial get(@Nonnull final String name) {
		return getInstance().registry.get(name);
	}

	/**
	 * Get a the named material, returning a new one of the correct name if an
	 * existing one is not available
	 * 
	 * @param name
	 *            Name of the material
	 * @return Reference to the material asked for or a new one of the correct name
	 */
	public TCMaterial getMaterial(@Nonnull final String name) {
		if (getInstance().isRegistered(name)) {
			return getInstance().get(name);
		}
		return getInstance().put(name, new TCMaterial(name));
	}

	/**
	 * Get a material of a given name based on a given MMDMaterial
	 * 
	 * @param name
	 *            name of the material
	 * @param material
	 *            MMDMaterial it is based on
	 * @return Any TCCode that represents an error or TCCode.SUCCESS
	 */
	public TCMaterial getMaterial(@Nonnull final String name, @Nonnull final MMDMaterial material) {
		if (getInstance().isRegistered(name)) {
			return getInstance().getMaterial(name);
		}

		return getInstance().put(name, new TCMaterial(name, material));
	}

	/**
	 * Private internal function for adding a MaterialIntegration to said list
	 * 
	 * @param m
	 *            The MaterialIntegration to add to the list
	 */
	private static void addIntegration(@Nonnull final MaterialIntegration m) {
		getInstance().integrations.add(m);
	}

	/**
	 * Private internal that handles actual material registration and creates the
	 * MaterialIntegration objects
	 * used to make sure that our materials are properly registered with TiC
	 * 
	 * @param mat
	 *            The material being registered.
	 * @return Hopefully TCCode.SUCCESS, but can be any number of the various error
	 *         code returns
	 */
	private TCCode register(@Nonnull final TCMaterial mat) {
		if (TinkerRegistry.getMaterial(mat.getName().toLowerCase()) != Material.UNKNOWN) {
			return TCCode.MATERIAL_ALREADY_REGISTERED;
		}

		final Boolean hasTraits = !mat.getTraitLocations().isEmpty();

		if (mat.getMetalMaterial().getFluid() == null) {
			return TCCode.BAD_MATERIAL;
		}

		// make sure the name used here is all lower case
		final Material tcmat = new Material(mat.getName().toLowerCase(), mat.getMetalMaterial().getTintColor());

		if (hasTraits) {
			for (final String s : mat.getTraitLocations()) {
				for (final AbstractTrait t : mat.getTraits(s)) {
					tcmat.addTrait(t, "general".equals(s) ? null : s);
				}
			}
		}

		Item matRepItem;
		final MaterialType matType = mat.getMetalMaterial().getType();
		switch (matType) {
			case METAL:
				matRepItem = mat.getMetalMaterial().getItem(Names.INGOT);
				break;
			case GEM:
				matRepItem = mat.getMetalMaterial().getItem(Names.GEM);
				break;
			case CRYSTAL:
				matRepItem = mat.getMetalMaterial().getItem(Names.CRYSTAL);
				break;
			case MINERAL:
				matRepItem = mat.getMetalMaterial().getItem(Names.INGOT);
				break;
			case ROCK:
			case WOOD:
			default:
				return TCCode.BAD_MATERIAL;
		}

		registerFluid(mat.getMetalMaterial(), mat.getAmountPer());

		TinkerRegistry.addMaterialStats(tcmat, mat.getHeadStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getHandleStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getExtraStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getBowStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getBowStringStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getArrowShaftStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getFletchingStats());

		tcmat.setFluid(mat.getMetalMaterial().getFluid()).setCraftable(mat.getCraftable())
				.setCastable(mat.getCastable()).addItem(matRepItem, 1, Material.VALUE_Ingot);
		tcmat.setRepresentativeItem(matRepItem);

		final String base = mat.getMetalMaterial().getName();
		final String suffix = base.substring(0, 1).toUpperCase() + base.substring(1);
		final MaterialIntegration m = new MaterialIntegration(tcmat, mat.getMetalMaterial().getFluid(), suffix);

		if (mat.getToolForge()) {
			m.toolforge();
		}

		addIntegration(m);
		m.integrate();
		return TCCode.SUCCESS;
	}

	/**
	 * Register all materials in the registry
	 * 
	 * @return Any TCCode that represents an error or TCCode.SUCCESS
	 */
	public TCCode registerAll() {
		for (final Entry<String, TCMaterial> ent : registry.entrySet()) {
			// log ent.getKey() - the material name - here ?
			final TCCode rv = register(ent.getValue());
			// Either we've had a success or the material is already registered
			// in those two cases, we carry on. Otherwise we return the error and
			// halt
			if (rv != TCCode.SUCCESS && rv != TCCode.MATERIAL_ALREADY_REGISTERED) {
				return rv;
			}
		}
		return TCCode.SUCCESS;
	}

	/**
	 * Register an item as melting to a given amount of a specific output fluid
	 * As we have no real method of detecting errors here, we could have this return
	 * void. Using TCCode for orthogonality and possible future utility
	 * 
	 * @param input
	 *            The Item to be melted
	 * @param output
	 *            The fluid it melts to
	 * @param outputQuantity
	 *            How much of the fluid, in mB
	 * @return Any TCCode that represents an error or TCCode.SUCCESS
	 */
	public TCCode registerMelting(@Nonnull final Item input, @Nonnull final Fluid output,
			@Nonnull final int outputQuantity) {
		if (outputQuantity == 0) {
			return TCCode.FAILURE_PARAMETER_ERROR;
		}
		TinkerRegistry.registerMelting(input, output, outputQuantity);
		return TCCode.SUCCESS;
	}

	/**
	 * Same as the other function, but using an ore-dict
	 * 
	 * @param oredictName
	 *            OreDictionary entry to use as the input item
	 * @param output
	 *            Output fluid
	 * @param outputQuantity
	 *            Output Amount
	 * @return Any TCCode that represents an error or TCCode.SUCCESS
	 */
	public TCCode registerMelting(@Nonnull final String oredictName, @Nonnull final Fluid output,
			@Nonnull final int outputQuantity) {
		if (outputQuantity == 0) {
			return TCCode.FAILURE_PARAMETER_ERROR;
		}
		TinkerRegistry.registerMelting(oredictName, output, outputQuantity);
		return TCCode.SUCCESS;
	}

	/**
	 * Internal helper to get a FluidStack from a pair of parameters to the variadic
	 * 'registerAlloy' function
	 * 
	 * @param fluid
	 *            String, Fluid or MetalMaterial representing the fluid
	 * @param amt
	 *            passed in as an Object, but actually an Integer representing the
	 *            amount of the fluid, in mB, that is in the stack
	 * @return a FluidStack representing the Fluid+Amount pair of parameters passed
	 *         in
	 */
	private static FluidStack getFluidStack(@Nonnull final Object fluid, @Nonnull final Object amt) {
		final Integer amount = (Integer) amt;
		if (fluid instanceof String) {
			return new FluidStack(FluidRegistry.getFluid((String) fluid), amount.intValue());
		} else if (fluid instanceof MMDMaterial) {
			return new FluidStack(FluidRegistry.getFluid(((MMDMaterial) fluid).getName()), amount.intValue());
		} else if (fluid instanceof Fluid) {
			return new FluidStack(FluidRegistry.getFluid(((Fluid) fluid).getName()), amount.intValue());
		}
		return null;
	}

	/**
	 * Register an alloy with the given recipe, name and output fluid in the
	 * specified amount
	 * 
	 * @param name
	 *            Name of the alloy
	 * @param output
	 *            Output fluid
	 * @param outputAmount
	 *            Amount of fluid produced
	 * @param recipe
	 *            A chunk of String/Fluid/MetalMaterial/TCMaterial followed by an
	 *            amount - must be at least two entries (4 items)
	 * @return Any TCCode that represents an error or TCCode.SUCCESS
	 */
	public TCCode registerAlloy(@Nonnull final String name, @Nonnull final Fluid output,
			@Nonnull final int outputAmount, @Nonnull final Object... recipe) {
		if (outputAmount == 0) {
			return TCCode.FAILURE_PARAMETER_ERROR;
		}

		final FluidStack outputStack = new FluidStack(output, outputAmount);
		final FluidStack[] inputs = new FluidStack[recipe.length / 2];

		for (int i = 0, j = 0; i < recipe.length; i += 2, j++) {
			inputs[j] = getFluidStack(recipe[i], recipe[i + 1]);
		}

		TinkerRegistry.registerAlloy(outputStack, inputs);
		return TCCode.SUCCESS;
	}

	/**
	 * Variant for backwards compat
	 * 
	 * @param output
	 *            Oredict name of output fluid
	 * @param outputQty
	 *            Amount of output produced
	 * @param inputNames
	 *            Array of oredict names of inputs
	 * @param inputQty
	 *            Array of quantities matching those inputs
	 * @return TCCode.SUCCESS or TCCode.FAILURE_PARAMETER_ERROR
	 */
	public TCCode registerAlloy(@Nonnull final String output, @Nonnull final int outputQty,
			@Nonnull final String[] inputNames, @Nonnull final int[] inputQty) {
		if (inputNames.length != inputQty.length) {
			return TCCode.FAILURE_PARAMETER_ERROR;
		}

		final FluidStack outputs = getFluidStack(output, outputQty);
		final FluidStack[] inputs = new FluidStack[inputNames.length];

		for (int i = 0; i < inputNames.length; i++) {
			inputs[i] = getFluidStack(inputNames[i], inputQty[i]);
		}
		TinkerRegistry.registerAlloy(outputs, inputs);
		return TCCode.SUCCESS;
	}

	/**
	 * Register the ability to cast a block of material from a specified quantity of
	 * a fluid
	 * 
	 * @param block
	 *            Block that is created
	 * @param source
	 *            Fluid being poured
	 * @param sourceQty
	 *            Amount of fluid being poured
	 * @return Any TCCode that represents an error or TCCode.SUCCESS
	 */
	public TCCode registerBasin(@Nonnull final Block block, @Nonnull final Fluid source, @Nonnull final int sourceQty) {
		if (sourceQty == 0) {
			return TCCode.FAILURE_PARAMETER_ERROR;
		}
		TinkerRegistry.registerBasinCasting(new ItemStack(block), null, source, sourceQty);
		return TCCode.SUCCESS;
	}

	/**
	 * Register a casting table piece. This is either an ingot, a nugget or a gem
	 * 
	 * @param output
	 *            Item that is output
	 * @param source
	 *            source material/fluid
	 * @param sourceQty
	 *            Amount
	 * @return Any TCCode that represents an error or TCCode.SUCCESS
	 */
	public TCCode registerCasting(@Nonnull final Item output, @Nonnull final Fluid source,
			@Nonnull final int sourceQty) {
		if (sourceQty == 0) {
			return TCCode.FAILURE_PARAMETER_ERROR;
		}
		TinkerRegistry.registerTableCasting(new ItemStack(output), null, source, sourceQty);
		return TCCode.SUCCESS;
	}

	/**
	 * Register all the fluids a MetalMaterial might melt into, at the amountPer
	 * amount
	 * 
	 * @param base
	 *            MetalMaterial that is the base for this fluid
	 * @param amountPer
	 *            How much per ingot/single item of the material
	 * @return TCCode.SUCCESS - at this point there are no failure points in this
	 *         routine
	 */
	public TCCode registerFluid(@Nonnull final MMDMaterial base, @Nonnull final int amountPer) {
		final String materialName = base.getName();
		final Fluid output = FluidRegistry.getFluid(materialName);
		final String oreDictName = base.getCapitalizedName();

		// hacky fix for Coal being itemCoal and not ingotCoal
		if (MaterialNames.COAL.equals(base.getName()))
			TinkerRegistry.registerMelting("itemCoal", output, amountPer);

		meltingHelper(Oredicts.ORE + oreDictName, output, amountPer * 2);
		meltingHelper(Oredicts.BLOCK + oreDictName, output, amountPer * 9);
		meltingHelper(Oredicts.INGOT + oreDictName, output, amountPer);
		meltingHelper(Oredicts.NUGGET + oreDictName, output, amountPer / 9);
		meltingHelper(Oredicts.DUST + oreDictName, output, amountPer);
		meltingHelper(Oredicts.DUST_SMALL + oreDictName, output, amountPer / 9);
		meltingHelper(base.getBlock(Names.NETHERORE), output, amountPer * 4);
		meltingHelper(base.getBlock(Names.ENDORE), output, amountPer * 4);

		meltingHelper(base.getBlock(Names.SLAB), output, (amountPer * 4) + (amountPer / 2));
		meltingHelper(base.getBlock(Names.WALL), output, amountPer * 9);
		meltingHelper(base.getItem(Names.BOOTS), output, amountPer * 4);
		meltingHelper(base.getItem(Names.HELMET), output, amountPer * 5);
		meltingHelper(base.getItem(Names.CHESTPLATE), output, amountPer * 8);
		meltingHelper(base.getItem(Names.LEGGINGS), output, amountPer * 7);
		meltingHelper(base.getItem(Names.SHEARS), output, amountPer * 2);
		meltingHelper(base.getBlock(Names.PRESSURE_PLATE), output, amountPer * 2);
		meltingHelper(base.getBlock(Names.BARS), output, ((amountPer / 9) * 3) + 6); // Fun math
		meltingHelper(base.getItem(Names.ROD), output, amountPer / 2);
		meltingHelper(base.getItem(Names.DOOR), output, amountPer * 2);
		meltingHelper(base.getBlock(Names.TRAPDOOR), output, amountPer * 4);
		meltingHelper(base.getBlock(Names.BUTTON), output, (amountPer / 9) * 2);

		return TCCode.SUCCESS;
	}

	/*
	 * The following functions are helpers to help make the registerMelting function
	 * a touch less complex
	 */
	private void meltingHelper(@Nonnull final Item item, @Nonnull final Fluid output, @Nonnull final int amount) {
		TinkerRegistry.registerMelting(item, output, amount);
	}

	private void meltingHelper(@Nonnull final String itemName, @Nonnull final Fluid output, @Nonnull final int amount) {
		TinkerRegistry.registerMelting(itemName, output, amount);
	}

	private void meltingHelper(@Nonnull final Block block, @Nonnull final Fluid output, @Nonnull final int amount) {
		TinkerRegistry.registerMelting(block, output, amount);
	}

	public void integrateRecipes() {
		for (final MaterialIntegration m : integrations) {
			m.integrateRecipes();
		}
	}
}
