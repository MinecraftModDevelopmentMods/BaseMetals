package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.integration.plugins.tinkers.TCCode;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMaterial;

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

public class NewTinkersConstruct {
    private final Map<String,TCMaterial> REGISTRY = new HashMap<>();
    private final List<MaterialIntegration> integrations = new ArrayList<>();
    
    private static NewTinkersConstruct instance;

    private NewTinkersConstruct() {
    	if( instance == null )
    		instance = this;
    }

    public static NewTinkersConstruct getInstance() {
    	if( instance == null )
    		instance = new NewTinkersConstruct();
    	
    	return instance;
    }
    
    public boolean isRegistered(String name) {
        return REGISTRY.containsKey(name);
    }

    public TCMaterial put(String name, TCMaterial mat) {
        if( isRegistered(name) ) {
            return REGISTRY.get(name);
        }
        REGISTRY.put(name,mat);
        return mat;
    }

    private TCMaterial get(String name) {
    	return getInstance().REGISTRY.get(name);
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

    private static void addIntegration(MaterialIntegration m) {
    	getInstance().integrations.add(m);
    }
    
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
        for( Entry<String,TCMaterial> ent : REGISTRY.entrySet() ) {
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
    public static TCCode registerFluid(MetalMaterial base, int amountPer) {
		String materialName = base.getName();
		Fluid output = FluidRegistry.getFluid(materialName);
		String oreDictName = base.getCapitalizedName();

		// hacky fix for Coal being itemCoal and not ingotCoal
		if (base.getName() == "coal")
			TinkerRegistry.registerMelting("itemCoal", output, amountPer);

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

		if (base.halfSlab != null)
			TinkerRegistry.registerMelting(base.halfSlab, output, (amountPer * 4) + (amountPer / 2));

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

		if (base.pressurePlate != null)
			TinkerRegistry.registerMelting(base.pressurePlate, output, amountPer * 2);

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
		
		return TCCode.SUCCESS;
    }
}
