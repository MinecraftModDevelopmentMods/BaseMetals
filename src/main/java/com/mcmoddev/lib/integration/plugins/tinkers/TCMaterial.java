package com.mcmoddev.lib.integration.plugins.tinkers;

import com.mcmoddev.lib.material.MetalMaterial;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.traits.ITrait;

import java.util.*;

/**
 * Created by Daniel Hazelton on 2/21/2017.
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 */
public class TCMaterial {
    private static int headDurability = 1;
    private static int bodyDurability = 1;
    private static int extraDurability = 1;
    private static int miningLevel = 1;
    private static int shaftBonusAmmo = 1;
    private static float miningSpeed = 1.0f;
    private static float headAttackDamage = 1.0f;
    private static float bodyModifier = 1.0f;
    private static float bowDrawingSpeed = 1.0f;
    private static float bowRange = 1.0f;
    private static float bowDamage = 1.0f;
    private static float bowstringModifier = 1.0f;
    private static float shaftModifier = 1.0f;
    private static float fletchingAccuracy = 1.0f;
    private static float fletchingModifier = 1.0f;
    private static int amountPer = 144;
    private static MetalMaterial metalmaterial;
    private static boolean craftable = false;
    private static boolean castable = true;
    private static boolean toolforge = true;
    private static String name = "FixMe";
    private static HashMap<String, List<AbstractTrait>> traits = new HashMap<>();

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
    public TCMaterial(String name, MetalMaterial material) {
        this.headDurability = material.getToolDurability();
        this.miningSpeed = material.magicAffinity * 3 / 2;
        this.miningLevel = material.getToolHarvestLevel();
        this.headAttackDamage = material.getBaseAttackDamage() * 2;
        this.bodyDurability = material.getToolDurability() / 7;
        this.bodyModifier = (material.hardness + material.magicAffinity * 2) / 9;
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
        this.miningSpeed = this.metalmaterial.magicAffinity * 3 / 2;
        this.miningLevel = this.metalmaterial.getToolHarvestLevel();
        this.headAttackDamage = this.metalmaterial.getBaseAttackDamage() * 2;
        this.bodyDurability = this.metalmaterial.getToolDurability() / 7;
        this.bodyModifier = (this.metalmaterial.hardness + this.metalmaterial.magicAffinity * 2) / 9;
        this.extraDurability = this.metalmaterial.getToolDurability() / 10;
        this.bowDrawingSpeed = calcDrawSpeed(this.metalmaterial.getToolDurability());
        this.bowDamage = this.metalmaterial.getBaseAttackDamage() + 3;
        this.bowRange = 15.0f;
        this.bowstringModifier = 1.0f;
        this.shaftModifier = 1.0f;
        this.fletchingAccuracy = 1.0f;
        this.fletchingModifier = 1.0f;
        this.shaftBonusAmmo = 1;
        if( this.name.equals("FixMe") ) {
            this.name = this.metalmaterial.getName();
        }
        return this;
    }

    /**
     * Generate the properties of the material and set it to be based on the passed-in material
     * @param mat The MetalMaterial to set this material to represent
     * @return the material
     */
    public TCMaterial genFromMaterial( MetalMaterial mat ) {
        this.metalmaterial = mat;
        return this.genFromMaterial();
    }

    // TODO: Add JavaDoc for the getters and setters
    public TCMaterial setHeadDurability(int headDurability) {
        this.headDurability = headDurability;
        return this;
    }

    public TCMaterial setBodyDurability(int bodyDurability) {
        this.bodyDurability = bodyDurability;
        return this;
    }

    public TCMaterial setExtraDurability(int extraDurability) {
        this.extraDurability = extraDurability;
        return this;
    }

    public TCMaterial setMiningLevel(int miningLevel) {
        this.miningLevel = miningLevel;
        return this;
    }

    public TCMaterial setShaftBonusAmmo(int shaftBonusAmmo) {
        this.shaftBonusAmmo = shaftBonusAmmo;
        return this;
    }

    public TCMaterial setMiningSpeed(float miningSpeed) {
        this.miningSpeed = miningSpeed;
        return this;
    }

    public TCMaterial setHeadAttackDamage(float headAttackDamage) {
        this.headAttackDamage = headAttackDamage;
        return this;
    }

    public TCMaterial setBodyModifier(float bodyModifier) {
        this.bodyModifier = bodyModifier;
        return this;
    }

    public TCMaterial setBowDrawingSpeed(float bowDrawingSpeed) {
        this.bowDrawingSpeed = bowDrawingSpeed;
        return this;
    }

    public TCMaterial setBowRange(float bowRange) {
        this.bowRange = bowRange;
        return this;
    }

    public TCMaterial setBowDamage(float bowDamage) {
        this.bowDamage = bowDamage;
        return this;
    }

    public TCMaterial setBowstringModifier(float bowstringModifier) {
        this.bowstringModifier = bowstringModifier;
        return this;
    }

    public TCMaterial setShaftModifier(float shaftModifier) {
        this.shaftModifier = shaftModifier;
        return this;
    }

    public TCMaterial setFletchingAccuracy(float fletchingAccuracy) {
        this.fletchingAccuracy = fletchingAccuracy;
        return this;
    }

    public TCMaterial setFletchingModifier(float fletchingModifier) {
        this.fletchingModifier = fletchingModifier;
        return this;
    }

    public TCMaterial setAmountPer(int amountPer) {
        this.amountPer = amountPer;
        return this;
    }

    public TCMaterial setMetalMaterial(MetalMaterial metalmaterial) {
        this.metalmaterial = metalmaterial;
        return this;
    }

    public TCMaterial setCraftable(boolean craftable) {
        // castable and craftable are exclusive
        // you can either cast the material or craft it into parts
        // at a part crafter, not both
        if( this.castable && craftable ) {
            this.castable = false;
        }

        this.craftable = craftable;
        return this;
    }

    public TCMaterial setCastable(boolean castable) {
        // castable and craftable are exclusive
        // you can either cast the material or craft it into parts
        // at a part crafter, not both
        if( this.craftable && castable ) {
            this.craftable = false;
        }

        this.castable = castable;
        return this;
    }

    public TCMaterial setToolForge(boolean toolforge) {
        this.toolforge = toolforge;
        return this;
    }

    public TCMaterial setName(String name) {
        this.name = name;
        return this;
    }

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

    public MetalMaterial getMetalMaterial() {
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

    public TCMaterial addTrait(String name, String loc) {
        ITrait trait = TraitRegistry.get(name);
        if (trait == null) {
            return this;
        }
        if (traits.keySet().contains(loc)) {
            if (!traits.get(loc).add((AbstractTrait) trait)) {
                return this;
            }
        } else {
            List<AbstractTrait> t = new ArrayList<>();
            t.add((AbstractTrait) trait);
            traits.put(loc, t);
        }

        return this;
    }

    public TCMaterial addTrait(String name) {
        return this.addTrait(name, "general");
    }

    public List<AbstractTrait> getTraits( String loc ) {
        String location = loc==null?"general":loc;
        if(this.traits.isEmpty() || this.traits.get(location).isEmpty() ) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(this.traits.get(location));
    }

    public Set<String> getTraitLocations() {
        if( this.traits.isEmpty() ) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(this.traits.keySet());
    }
}
