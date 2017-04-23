package com.mcmoddev.lib.integration.plugins.tinkers;

import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.material.MMDMaterial;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.traits.ITrait;

import java.util.*;

/**
 * Created by Daniel Hazelton on 2/21/2017.
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 */
public class TCMaterial {
    /*
     * General bookkeeping variables
     */
    private int headDurability = 1;
    private int bodyDurability = 1;
    private int extraDurability = 1;
    private int miningLevel = 1;
    private int shaftBonusAmmo = 1;
    private float miningSpeed = 1.0f;
    private float headAttackDamage = 1.0f;
    private float bodyModifier = 1.0f;
    private float bowDrawingSpeed = 1.0f;
    private float bowRange = 1.0f;
    private float bowDamage = 1.0f;
    private float bowstringModifier = 1.0f;
    private float shaftModifier = 1.0f;
    private float fletchingAccuracy = 1.0f;
    private float fletchingModifier = 1.0f;
    private int amountPer = 144;
    private boolean craftable = false;
    private boolean castable = true;
    private boolean toolforge = true;

    /*
     * Internal reference/future expansion stuff
     */
    private MMDMaterial metalmaterial;
    private String name;
    private HashMap<String, List<AbstractTrait>> traits = new HashMap<>();

    /*
     * TiC material stats
     */
    private HeadMaterialStats headStats;
    private HandleMaterialStats handleStats;
    private ExtraMaterialStats extraStats;
    private BowMaterialStats bowStats;
    private BowStringMaterialStats bowStringStats;
    private ArrowShaftMaterialStats arrowShaftStats;
    private FletchingMaterialStats fletchingStats;

    /**
     * Barebones, minimalist constructor.
     * @param name name of the material
     */
    public TCMaterial(String name) {
        this.name = name;
    }

    /**
     * This is the preferred constructor
     * @param name name of the material
     * @param material MetalMaterial that this TCMaterial represents
     */
    public TCMaterial(String name, MMDMaterial material) {
        this.headDurability = material.getToolDurability();
        this.miningSpeed = material.getStat(MaterialStats.MAGICAFFINITY) * 3 / 2;
        this.miningLevel = material.getToolHarvestLevel();
        this.headAttackDamage = material.getBaseAttackDamage() * 2;
        this.bodyDurability = material.getToolDurability() / 7;
        this.bodyModifier = (material.getStat(MaterialStats.HARDNESS) + material.getStat(MaterialStats.MAGICAFFINITY) * 2) / 9;
        this.extraDurability = material.getToolDurability() / 10;
        this.bowDrawingSpeed = calcDrawSpeed(material.getToolDurability());
        this.bowDamage = material.getBaseAttackDamage() + 3;
        this.bowRange = 15.0f;
        this.bowstringModifier = 1.0f;
        this.shaftModifier = 1.0f;
        this.fletchingAccuracy = 1.0f;
        this.fletchingModifier = 1.0f;
        this.shaftBonusAmmo = 1;
        this.metalmaterial = material;
        this.name = name;
    }

    /**
     * Internal function for calculating the draw-speed of a TiC bow
     * @param durability durability of the material
     * @return teh calculated draw speed
     */
    private float calcDrawSpeed(int durability) {
        float val;
        if (durability < 204) {
            val = 1.0f;
        } else {
            val = ((durability - 200) + 1) / 10.0f;
            val -= Math.floor(val);
        }

        return val;
    }

    /**
     * Generate the properties of the material from the MetalMaterial it represents
     * @return the material
     */
    public TCMaterial genFromMaterial() {
        if( this.metalmaterial == null ) {
            return this;
        }
        this.headDurability = this.metalmaterial.getToolDurability();
        this.miningSpeed = this.metalmaterial.getStat(MaterialStats.MAGICAFFINITY) * 3 / 2;
        this.miningLevel = this.metalmaterial.getToolHarvestLevel();
        this.headAttackDamage = this.metalmaterial.getBaseAttackDamage() * 2;
        this.bodyDurability = this.metalmaterial.getToolDurability() / 7;
        this.bodyModifier = (this.metalmaterial.getStat(MaterialStats.HARDNESS) + this.metalmaterial.getStat(MaterialStats.MAGICAFFINITY) * 2) / 9;
        this.extraDurability = this.metalmaterial.getToolDurability() / 10;
        this.bowDrawingSpeed = calcDrawSpeed(this.metalmaterial.getToolDurability());
        this.bowDamage = this.metalmaterial.getBaseAttackDamage() + 3;
        this.bowRange = 15.0f;
        this.bowstringModifier = 1.0f;
        this.shaftModifier = 1.0f;
        this.fletchingAccuracy = 1.0f;
        this.fletchingModifier = 1.0f;
        this.shaftBonusAmmo = 1;
        if( this.name == null ) {
            this.name = this.metalmaterial.getName();
        }
        return this;
    }

    /**
     * Generate the properties of the material and set it to be based on the passed-in material
     * @param mat The MetalMaterial to set this material to represent
     * @return the material
     */
    public TCMaterial genFromMaterial( MMDMaterial mat ) {
        this.metalmaterial = mat;
        return genFromMaterial();
    }

    /**
     * @param headDurability durability of material when used as a tool head
     * @return the material
     */
    public TCMaterial setHeadDurability(int headDurability) {
        this.headDurability = headDurability;
        return this;
    }

    /**
     * @param bodyDurability durability of material when used as a tool rod/body
     * @return the material
     */
    public TCMaterial setBodyDurability(int bodyDurability) {
        this.bodyDurability = bodyDurability;
        return this;
    }

    /**
     * @param extraDurability durability of material when used as a tool extra part (binding, large plate, etc...)
     * @return the material
     */
    public TCMaterial setExtraDurability(int extraDurability) {
        this.extraDurability = extraDurability;
        return this;
    }

    /**
     * @param miningLevel Mining Level of the material
     * @return the material
     */
    public TCMaterial setMiningLevel(int miningLevel) {
        this.miningLevel = miningLevel;
        return this;
    }

    /**
     * @param shaftBonusAmmo Amount of ammo you get when crafting arrows/bolts with this material as the shaft
     * @return the material
     */
    public TCMaterial setShaftBonusAmmo(int shaftBonusAmmo) {
        this.shaftBonusAmmo = shaftBonusAmmo;
        return this;
    }

    /**
     * @param miningSpeed Speed of mining with this material
     * @return the material
     */
    public TCMaterial setMiningSpeed(float miningSpeed) {
        this.miningSpeed = miningSpeed;
        return this;
    }

    /**
     * @param headAttackDamage Attack damage when used as a tool/weapon head
     * @return the material
     */
    public TCMaterial setHeadAttackDamage(float headAttackDamage) {
        this.headAttackDamage = headAttackDamage;
        return this;
    }

    /**
     * @param bodyModifier Modifier amount applied when used for a tool/weapon body
     * @return the material
     */
    public TCMaterial setBodyModifier(float bodyModifier) {
        this.bodyModifier = bodyModifier;
        return this;
    }

    /**
     * @param bowDrawingSpeed draw speed of bows made from this material
     * @return the material
     */
    public TCMaterial setBowDrawingSpeed(float bowDrawingSpeed) {
        this.bowDrawingSpeed = bowDrawingSpeed;
        return this;
    }

    /**
     * @param bowRange range of bows made from this material
     * @return the material
     */
    public TCMaterial setBowRange(float bowRange) {
        this.bowRange = bowRange;
        return this;
    }

    /**
     * @param bowDamage base damage of bows made from this material
     * @return the material
     */
    public TCMaterial setBowDamage(float bowDamage) {
        this.bowDamage = bowDamage;
        return this;
    }

    /**
     * @param bowstringModifier Modifier when this material is used to make a bow string
     * @return the material
     */
    public TCMaterial setBowstringModifier(float bowstringModifier) {
        this.bowstringModifier = bowstringModifier;
        return this;
    }

    /**
     * @param shaftModifier Modifier when used as bolt/arrow shaft
     * @return the material
     */
    public TCMaterial setShaftModifier(float shaftModifier) {
        this.shaftModifier = shaftModifier;
        return this;
    }

    /**
     * @param fletchingAccuracy accuracy modifier when material is used to make fletching
     * @return the material
     */
    public TCMaterial setFletchingAccuracy(float fletchingAccuracy) {
        this.fletchingAccuracy = fletchingAccuracy;
        return this;
    }

    /**
     * @param fletchingMod base modifier when used as fletching
     * @return the material
     */
    public TCMaterial setFletchingModifier(float fletchingMod) {
        this.fletchingModifier = fletchingMod;
        return this;
    }

    /**
     * @param amount amount of fluid per ingot
     * @return the material
     */
    public TCMaterial setAmountPer(int amount) {
        this.amountPer = amount;
        return this;
    }

    /**
     * @param mm MetalMaterial this wrapper represents
     * @return the material
     */
    public TCMaterial setMetalMaterial(MMDMaterial mm) {
        this.metalmaterial = mm;
        return genFromMaterial();
    }

    /**
     * Wrapper around {setMetalMaterial}
     * @param mm MetalMaterial this represents
     * @return the material
     */
    public TCMaterial setMaterial(MMDMaterial mm) {
    	return this.setMetalMaterial(mm);
    }
    
    /**
     * Craftable and Castable are mutually exclusive, this will unset castable if it is set and the parameter is 'true'
     * @param iscraftable boolean true/false - is this material capable of being crafted into parts at a part maker ?
     * @return the material
     */
    public TCMaterial setCraftable(boolean iscraftable) {
        // castable and craftable are exclusive
        // you can either cast the material or craft it into parts
        // at a part crafter, not both
        if( this.castable && iscraftable ) {
            this.castable = false;
        }

        this.craftable = iscraftable;
        return this;
    }

    /**
     * Craftable and Castable are mutually exclusive, this will unset craftable if it is set and the parameter is 'true'
     * @param iscastable boolean true/false - is this material capable of being cast into parts at the smeltery ?
     * @return the material
     */
    public TCMaterial setCastable(boolean iscastable) {
        // castable and craftable are exclusive
        // you can either cast the material or craft it into parts
        // at a part crafter, not both
        if( this.craftable && iscastable ) {
            this.craftable = false;
        }

        this.castable = iscastable;
        return this;
    }

    /**
     * @param toolForge is this material capable of being a ToolForge base material ?
     * @return the material
     */
    public TCMaterial setToolForge(boolean toolForge) {
        this.toolforge = toolForge;
        return this;
    }

    /**
     * the 'Name' parameter is not used, at this time
     * @param newName name for the material
     * @return the material
     */
    public TCMaterial setName(String newName) {
        this.name = newName;
        return this;
    }

    // what follows are getters - they shouldn't need documentation
    public int getHeadDurability() {
        return this.headDurability;
    }

    public int getBodyDurability() {
        return this.bodyDurability;
    }

    public int getExtraDurability() {
        return this.extraDurability;
    }

    public int getMiningLevel() {
        return this.miningLevel;
    }

    public int getShaftBonusAmmo() {
        return this.shaftBonusAmmo;
    }

    public float getMiningSpeed() {
        return this.miningSpeed;
    }

    public float getHeadAttackDamage() {
        return this.headAttackDamage;
    }

    public float getBodyModifier() {
        return this.bodyModifier;
    }

    public float getBowDrawingSpeed() {
        return this.bowDrawingSpeed;
    }

    public float getBowRange() {
        return this.bowRange;
    }

    public float getBowDamage() {
        return this.bowDamage;
    }

    public float getBowstringModifier() {
        return this.bowstringModifier;
    }

    public float getShaftModifier() {
        return this.shaftModifier;
    }

    public float getFletchingAccuracy() {
        return this.fletchingAccuracy;
    }

    public float getFletchingModifier() {
        return this.fletchingModifier;
    }

    public int getAmountPer() {
        return this.amountPer;
    }

    public MMDMaterial getMetalMaterial() {
        return this.metalmaterial;
    }

    public boolean getCraftable() {
        return this.craftable;
    }

    public boolean getCastable() {
        return this.castable;
    }

    public boolean getToolForge() {
        return this.toolforge;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Add a trait for a given tool part/use - "general" for overall/any undefined
     * @param name Trait name - can be any that are available in TiC or one of the customs ones added by this plugin
     * @param loc Location/part to attach trait to
     * @return The material
     */
    public TCMaterial addTrait(String name, String loc) {
        ITrait trait = TraitRegistry.get(name);
        if (trait == null) {
            return this;
        }
        if (this.traits.keySet().contains(loc)) {
            if (!this.traits.get(loc).add((AbstractTrait) trait)) {
                return this;
            }
        } else {
            List<AbstractTrait> t = new ArrayList<>();
            t.add((AbstractTrait) trait);
            this.traits.put(loc, t);
        }

        return this;
    }

    /**
     * Add the named trait to general/overall use, covers only those positions that do not have traits specifically attached
     * @param name Name of the trait to add
     * @return The material
     */
    public TCMaterial addTrait(String name) {
        return this.addTrait(name, "general");
    }

    /**
     * Get a list of all traits on a given location
     * @param loc Location to get traits for, null/unsupplied means the same as "general"
     * @return Either an unmodifiable list of traits or the empty list
     */
    public List<AbstractTrait> getTraits( String loc ) {
        String location = loc==null?"general":loc;
        if(this.traits.isEmpty() || this.traits.get(location).isEmpty() ) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(this.traits.get(location));
    }

    /**
     * Get the complete set of locations that have defined traits in this material
     * @return Either the full set of locations with defined traits or the empty set
     */
    public Set<String> getTraitLocations() {
        if( this.traits.isEmpty() ) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(this.traits.keySet());
    }

    /**
     * Sets applies the changes and sets up stuff internally to assist with the material registration
     */
    public void settle() {
        this.headStats =  new HeadMaterialStats(headDurability, miningSpeed, headAttackDamage, miningLevel);
        this.handleStats =  new HandleMaterialStats(bodyModifier, bodyDurability);
        this.extraStats =  new ExtraMaterialStats(extraDurability);
        this.bowStats =  new BowMaterialStats(bowDrawingSpeed, bowRange, bowDamage);
        this.bowStringStats =  new BowStringMaterialStats(bowstringModifier);
        this.arrowShaftStats =  new ArrowShaftMaterialStats(shaftModifier, shaftBonusAmmo);
        this.fletchingStats =  new FletchingMaterialStats(fletchingAccuracy, fletchingModifier);
    }

    public HeadMaterialStats getHeadStats() {
        return this.headStats;
    }

    public HandleMaterialStats getHandleStats() {
        return this.handleStats;
    }

    public ExtraMaterialStats getExtraStats() {
        return this.extraStats;
    }
    public BowMaterialStats getBowStats() {
        return this.bowStats;
    }

    public BowStringMaterialStats getBowStringStats() {
        return this.bowStringStats;
    }

    public ArrowShaftMaterialStats getArrowShaftStats() {
        return this.arrowShaftStats;
    }

    public FletchingMaterialStats getFletchingStats() {
        return this.fletchingStats;
    }
}
