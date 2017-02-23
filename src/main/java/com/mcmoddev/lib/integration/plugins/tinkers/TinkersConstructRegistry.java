package com.mcmoddev.lib.integration.plugins.tinkers;

import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.TinkerIntegration;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.AbstractTrait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Daniel Hazelton on 2/21/2017.
 *
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 */

public class TinkersConstructRegistry {
    private final Map<String,TCMaterial> registry = new HashMap<>();
    private final List<MaterialIntegration> integrations = new ArrayList<>();
    
    private static TinkersConstructRegistry instance;

    /**
     * Hacky-ass shit constructor. Be warned that this creates a semi-circular data structure.
     * @constructor
     */
    private TinkersConstructRegistry() {
    	if( instance == null )
    		instance = this;
    }

    /**
     * Return the existing instance or create a new one - if one doesn't exist - and return it
     * @return an instance of the registry
     */
    public static TinkersConstructRegistry getInstance() {
    	if( instance == null )
    		instance = new TinkersConstructRegistry();
    	
    	return instance;
    }
    
    /**
     * Has a material with the given name been registered ?
     * @param name The name to test for
     * @return Boolean truth value of whether or not the item has been registered
     */
    public boolean isRegistered(String name) {
        return registry.containsKey(name);
    }

    /**
     * Used internally to add an entry to the registry
     * @param name name of material being registered
     * @param mat Material to be registered
     * @return the material
     */
    private TCMaterial put(String name, TCMaterial mat) {
        if( isRegistered(name) ) {
            return registry.get(name);
        }
        registry.put(name,mat);
        return mat;
    }

    /**
     * Used internally to access the registry and grab an entry. Helps to minimize some potential issues.
     * @param name name used for material during registration
     * @return the request material
     */
    private TCMaterial get(String name) {
    	return getInstance().registry.get(name);
    }
    /**
     * Get a the named material, returning a new one of the correct name if an existing one is not available
     * @param name Name of the material
     * @return Reference to the material asked for or a new one of the correct name
     */
    public TCMaterial getMaterial(String name) {
        String n = name==null?"FixYourCode":name;
        if( getInstance().isRegistered(n) ) {
            return getInstance().get(n);
        }
        return getInstance().put( n, new TCMaterial(n) );
    }

    /**
     * Get a material of a given name based on a given MetalMaterial
     * @param name name of the material
     * @param material MetalMaterial it is based on
     * @return Any TCCode that represents an error or TCCode.SUCCESS
     */
    public TCMaterial getMaterial(String name, MetalMaterial material) {
        String n = name==null?(material==null?"FixYourCode":material.getName()):name;
        if( material == null ) {
            return null;
        }

        if( getInstance().isRegistered(n) ) {
            return getInstance().getMaterial(n);
        }
        return getMaterial(name).setMaterial(material);
    }

    /**
     * Private internal function for adding a MaterialIntegration to said list
     * @param m The MaterialIntegration to add to the list
     */
    private static void addIntegration(MaterialIntegration m) {
    	getInstance().integrations.add(m);
    }
    
    /**
     * Private internal that handles actual material registration and creates the MaterialIntegration objects
     * used to make sure that our materials are properly registered with TiC
     * @param mat The material being registered.
     * @return Hopefully TCCode.SUCCESS, but can be any number of the various error code returns
     */
    private TCCode register(TCMaterial mat) {
    	Boolean hasTraits = !mat.getTraitLocations().isEmpty();
    	
    	if( mat.getMetalMaterial().fluid == null ) {
    		return TCCode.BAD_MATERIAL;
    	}
    	
		// make sure the name used here is all lower case
		Material tcmat = new Material(mat.getName().toLowerCase(), mat.getMetalMaterial().getTintColor());

		if (hasTraits) {
			for (String s : mat.getTraitLocations()) {
				for (AbstractTrait t : mat.getTraits(s)) {
					tcmat.addTrait(t, s == "general" ? null : s);
				}
			}
		}

		if (TinkerRegistry.getMaterial(tcmat.identifier) != Material.UNKNOWN) {
			return TCCode.MATERIAL_ALREADY_REGISTERED;
		}
    	
		registerFluid(mat.getMetalMaterial(), mat.getAmountPer());
		tcmat.addItem(mat.getMetalMaterial().ingot, 1, Material.VALUE_Ingot);
		tcmat.addItemIngot(Oredicts.INGOT + mat.getMetalMaterial().getName());
		tcmat.setRepresentativeItem(mat.getMetalMaterial().ingot);

		TinkerRegistry.addMaterialStats(tcmat, mat.getHeadStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getHandleStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getExtraStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getBowStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getBowStringStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getArrowShaftStats());
		TinkerRegistry.addMaterialStats(tcmat, mat.getFletchingStats());
		TinkerRegistry.addMaterial(tcmat);

		tcmat.setFluid(mat.getMetalMaterial().fluid).setCastable(mat.getCastable()).setCraftable(mat.getCraftable());

		String base = mat.getMetalMaterial().getName();
		String suffix = base.substring(0, 1).toUpperCase() + base.substring(1);
		MaterialIntegration m = new MaterialIntegration(null, mat.getMetalMaterial().fluid, suffix);
		if (mat.getToolForge()) {
			m.toolforge();
		}

		TinkerIntegration.integrationList.add(m);
		addIntegration(m);
		m.integrate();
		return TCCode.SUCCESS;
    }
    
    /**
     * Register all materials in the registry
     * @return Any TCCode that represents an error or TCCode.SUCCESS
     */
    public TCCode registerAll() {
        for( Entry<String,TCMaterial> ent : registry.entrySet() ) {
        	// log ent.getKey() - the material name - here ?
        	TCCode rv = register(ent.getValue());
        	if( rv != TCCode.SUCCESS ) {
        		return rv;
        	}
        }
        return TCCode.SUCCESS;
    }

    /**
     * Register an item as melting to a given amount of a specific output fluid
     * As we have no real method of detecting errors here, we could have this return void. Using TCCode for orthogonality and possible future utility
     * @param input The Item to be melted
     * @param output The fluid it melts to
     * @param outputQuantity How much of the fluid, in mB
     * @return Any TCCode that represents an error or TCCode.SUCCESS
     */
    public TCCode registerMelting(Item input, Fluid output, int outputQuantity) {
        if( input == null || output == null || outputQuantity == 0 ) {
            return TCCode.FAILURE_PARAMETER_ERROR;
        }
        TinkerRegistry.registerMelting(input, output, outputQuantity);
        return TCCode.SUCCESS;
    }

    /**
     * Same as the other function, but using an ore-dict
     * @param oredictName  OreDictionary entry to use as the input item
     * @param output Output fluid
     * @param outputQuantity Output Amount
     * @return Any TCCode that represents an error or TCCode.SUCCESS
     */
    public TCCode registerMelting(String oredictName, Fluid output, int outputQuantity ){
        if( oredictName == null || output == null || outputQuantity == 0 ) {
            return TCCode.FAILURE_PARAMETER_ERROR;
        }
        TinkerRegistry.registerMelting(oredictName, output, outputQuantity);
        return TCCode.SUCCESS;
    }

    /**
     * Internal helper to get a FluidStack from a pair of parameters to the variadic 'registerAlloy' function
     * @param fluid String, Fluid or MetalMaterial representing the fluid
     * @param amt passed in as an Object, but actually an Integer representing the amount of the fluid, in mB, that is in the stack
     * @return a FluidStack representing the Fluid+Amount pair of parameters passed in
     */
    private static FluidStack getFluidStack( Object fluid, Object amt ) {
    	Integer amount = (Integer)amt;
    	if( fluid instanceof String ) {
    		return new FluidStack(FluidRegistry.getFluid((String)fluid), amount.intValue());
    	} else if( fluid instanceof MetalMaterial ) {
    		return new FluidStack(FluidRegistry.getFluid(((MetalMaterial)fluid).getName()), amount.intValue());
    	} else if( fluid instanceof Fluid ) {
    		return new FluidStack(FluidRegistry.getFluid(((Fluid)fluid).getName()), amount.intValue());    		
    	}
    	return null;
    }
    
    /**
     * Register an alloy with the given recipe, name and output fluid in the specified amount
     * @param name Name of the alloy
     * @param output Output fluid
     * @param outputAmount Amount of fluid produced
     * @param recipe A chunk of String/Fluid/MetalMaterial/TCMaterial followed by an amount - must be at least two entries (4 items)
     * @return Any TCCode that represents an error or TCCode.SUCCESS
     */
    public TCCode registerAlloy(String name, Fluid output, int outputAmount, Object... recipe) {
        if( name == null || output == null || outputAmount == 0 ) {
            return TCCode.FAILURE_PARAMETER_ERROR;
        }

        FluidStack outputStack = new FluidStack(output, outputAmount);
        FluidStack[] inputs = new FluidStack[recipe.length/2];

        for( int i = 0, j = 0; i < recipe.length; i += 2, j++ ) {
        	inputs[j] = getFluidStack(recipe[i], recipe[i+1]);
        }
        
        TinkerRegistry.registerAlloy(outputStack, inputs);
        return TCCode.SUCCESS;
    }

    /**
     * Variant for backwards compat
     * @param output Oredict name of output fluid
     * @param outputQty Amount of output produced
     * @param inputNames Array of oredict names of inputs
     * @param inputQty Array of quantities matching those inputs
     * @return TCCode.SUCCESS or TCCode.FAILURE_PARAMETER_ERROR
     */
    public TCCode registerAlloy(String output, int outputQty, String[] inputNames, int[] inputQty) {
    	if( inputNames.length != inputQty.length ) {
    		return TCCode.FAILURE_PARAMETER_ERROR;
    	}
    	
    	FluidStack outputs = getFluidStack(output, outputQty);
    	FluidStack[] inputs = new FluidStack[inputNames.length];
    	
    	for( int i = 0; i < inputNames.length; i++ ) {
    		inputs[i] = getFluidStack(inputNames[i], inputQty[i]);
    	}
    	TinkerRegistry.registerAlloy(outputs,inputs);
        return TCCode.SUCCESS;
    }

    /**
     * Register the ability to cast a block of material from a specified quantity of a fluid
     * @param block Block that is created
     * @param source Fluid being poured
     * @param sourceQty Amount of fluid being poured
     * @return Any TCCode that represents an error or TCCode.SUCCESS
     */
    public TCCode registerBasin(Block block, Fluid source, int sourceQty) {
        if( block == null || source == null || sourceQty == 0 ) {
            return TCCode.FAILURE_PARAMETER_ERROR;
        }
        TinkerRegistry.registerBasinCasting(new ItemStack(block), null, source, sourceQty);
        return TCCode.SUCCESS;
    }

    /**
     * Register a casting table piece. This is either an ingot, a nugget or a gem
     * @param output Item that is output
     * @param source source material/fluid
     * @param sourceQty Amount
     * @return Any TCCode that represents an error or TCCode.SUCCESS
     */
    public TCCode registerCasting(Item output, Fluid source, int sourceQty) {
        if( output == null || source == null || sourceQty == 0 ) {
            return TCCode.FAILURE_PARAMETER_ERROR;
        }
        TinkerRegistry.registerTableCasting( new ItemStack(output), null, source, sourceQty);
        return TCCode.SUCCESS;
    }

    /**
     * Register all the fluids a MetalMaterial might melt into, at the amountPer amount
     * @param base MetalMaterial that is the base for this fluid
     * @param amountPer How much per ingot/single item of the material
     * @return TCCode.SUCCESS - at this point there are no failure points in this routine
     */
    public TCCode registerFluid(MetalMaterial base, int amountPer) {
		String materialName = base.getName();
		Fluid output = FluidRegistry.getFluid(materialName);
		String oreDictName = base.getCapitalizedName();

		// hacky fix for Coal being itemCoal and not ingotCoal
		if (base.getName() == "coal")
			TinkerRegistry.registerMelting("itemCoal", output, amountPer);

		
		meltingHelper(Oredicts.ORE + oreDictName, output, amountPer * 2);
		meltingHelper(Oredicts.BLOCK + oreDictName, output, amountPer * 9);
		meltingHelper(Oredicts.INGOT + oreDictName, output, amountPer);
		meltingHelper(Oredicts.NUGGET + oreDictName, output, amountPer / 9);
		meltingHelper(Oredicts.DUST + oreDictName, output, amountPer);
		meltingHelper(Oredicts.DUSTSMALL + oreDictName, output, amountPer / 9);
		meltingHelper(base.oreNether, output, amountPer * 4);
		meltingHelper(base.oreEnd, output, amountPer * 4);

		meltingHelper(base.halfSlab, output, (amountPer * 4) + (amountPer / 2));
		meltingHelper(base.wall, output, amountPer * 9);
		meltingHelper(base.boots, output, amountPer * 4);
		meltingHelper(base.helmet, output, amountPer * 5);
		meltingHelper(base.chestplate, output, amountPer * 8);
		meltingHelper(base.leggings, output, amountPer * 7);
		meltingHelper(base.shears, output, amountPer * 2);
		meltingHelper(base.pressurePlate, output, amountPer * 2);
		meltingHelper(base.bars, output, ((amountPer / 9) * 3) + 6); // Fun math
		meltingHelper(base.rod, output, amountPer / 2);
		meltingHelper(base.door, output, amountPer * 2);
		meltingHelper(base.trapdoor, output, amountPer * 4);
		meltingHelper(base.button, output, (amountPer / 9) * 2);    	
		
		return TCCode.SUCCESS;
    }
    
    /*
     * The following functions are helpers to help make the registerMelting function a touch less complex
     */
    private void meltingHelper( Item item, Fluid output, int amount ) {
    	if( item == null || output == null ) {
    		return;
    	}
    	
    	TinkerRegistry.registerMelting(item, output, amount);
    }
    
    private void meltingHelper( String itemName, Fluid output, int amount ) {
    	if( itemName == null || output == null ) {
    		return;
    	}
    	
    	TinkerRegistry.registerMelting(itemName, output, amount);
    }

    private void meltingHelper( Block block, Fluid output, int amount ) {
    	if( block == null || output == null ) {
    		return;
    	}
    	
    	TinkerRegistry.registerMelting(block, output, amount);
    }

}
