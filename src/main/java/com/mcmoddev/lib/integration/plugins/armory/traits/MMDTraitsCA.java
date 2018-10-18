package com.mcmoddev.lib.integration.plugins.armory.traits;

import slimeknights.tconstruct.library.traits.AbstractTrait;

public class MMDTraitsCA {

    /**
     * <h2><u>Icy Armor Modifier</u></h2>
     * <b>Name:</b> icy
     * <br>
     * <b>Desc:</b>
     * Applies Fire Protection
     *
     * <br>
     * <b>String Reference:<br></b>
     * "icy"
     * "mmd-icy"
     * <br>
     * <b>Modifier class:</b>
     * {@link TraitIcy}
     */
    public static final AbstractTrait icy = new TraitIcy();

    /**
     * <h2><u>Maleable Armor Modifier:</u></h2>
     * <b>Name:</b> soft
     * <br>
     * <b>Desc:</b>
     * This modifier, when applied to armors, decreases the damage used by the armor when the armor is damaged by
     * a factor of 0.25. <br><em>(i.e. 0.25 less damage will be dealt to the armor's durability)</em>
     *
     * <br>
     * <b>String Reference:<br></b>
     * "maleable"
     * "mmd-maleable"
     * <br>
     * <b>Modifier class:</b>
     * {@link TraitMalleable}
     */
    public static final AbstractTrait malleable = new TraitMalleable();


    public static final AbstractTrait toxic = new TraitToxic();


    public static final AbstractTrait reactive = new TraitReactive();


    public static final AbstractTrait brittle = new TraitBrittle();

    private MMDTraitsCA() {

    }
}
