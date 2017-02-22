package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.material.MetalMaterial;
import slimeknights.mantle.util.RecipeMatch;
// wrap/replace TCMetalMaterial
// import com.mcmoddev.lib.integration.tinkers.TCMaterial;
// return values and other important numbers
// import com.mcmoddev.lib.integration.tinkers.TCCode;

import net.minecraft.item.Item;
import net.minecraft.fluids.Fluid;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel Hazelton on 2/21/2017.
 *
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 */

public class NewTinkersConstruct {
    private static Map<String,TCMaterial> REGISTRY = new HashMap<>();

    private static NewTinkersConstruct instance;

    /**
     * @constructor
     * Either returns the existing instance or a new one if one doesn't exist
     */
    public NewTinkersConstruct() {
        if( instance != null ) {
            return instance;
        }
        instance = this;
    }

    /**
     * Get a the named material, returning a new one of the correct name if an existing one is not available
     * @param name Name of the material
     * @return Reference to the material asked for or a new one of the correct name
     */
    public TCMaterial getMaterial(String name) {
        if( REGISTRY.containsKey(name) ) {
            return REGISTRY.get(name);
        } else {
            REGISTRY.put( name, new TCMaterial(name) );
            return REGISTRY.get(name);
        }
    }

    /**
     * Register all materials in the registry
     * @return Any TCCode that represents an error or TCCode.SUCCESS
     */
    public TCCode registerAll() {
        for( Entry<String,TCMaterial> ent : REGISTRY.entrySet() ) {
            // make actual TiC register calls here
        }
    }

    /**
     * Register an item as melting to a given amount of a specific output fluid
     * @param input The Item to be melted
     * @param output The fluid it melts to
     * @param outputQuantity How much of the fluid, in mB
     * @return Any TCCode that represents an error or TCCode.SUCCESS
     */
    public TCCode registerMelting(Item input, Fluid output, int outputQuantity) {
        // register a material->liquid melt with amount
    }

    /**
     * Same as the other function, but using an ore-dict
     * @param oredictName  OreDictionary entry to use as the input item
     * @param output Output fluid
     * @param outputQuantity Output Amount
     * @return Any TCCode that represents an error or TCCode.SUCCESS
     */
    public TCCode registerMelting(String oredictName, Fluid output, int outputQuantity ){
        // same as above, but ore-dicted
    }

    public TCCode registerAlloy(/* same as before */) {
        // register an alloy recipe
    }

    public TCCode registerBasin(/* ... */) {
        // blargh
    }

    public TCCode registerCasting(/* ... */) {
        // ...
    }

    /**
     * Get a material of a given name based on a given MetalMaterial
     * @param name name of the material
     * @param material MetalMaterial it is based on
     * @return Any TCCode that represents an error or TCCode.SUCCESS
     */
    public TCCode getMaterial(String name, MetalMaterial material) {
        // gen a new TCMaterial
        // add it to the registry
    }
}
