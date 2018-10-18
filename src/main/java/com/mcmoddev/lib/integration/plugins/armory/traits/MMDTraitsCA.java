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

    /**
     * <h2><u>Toxic Armor Modifier:</u></h2>
     * <b>Name:</b> toxic
     * <br>
     * <b>Desc:</b>
     * Applies poison 2 for 5 seconds as well as blindness for 2.5 seconds when a living entity hits the player.
     *
     * <br>
     * <b>String Reference:<br></b>
     * "toxic"
     * "mmd-toxic"
     */
    public static final AbstractTrait toxic = new TraitToxic();

    /**
     * <h2><u>Reactive Armor Modifier:</u></h2>
     * <b>Name:</b> reactive
     * <br>
     * <b>Desc:</b>
     * Damages the armor by 5 durability if the armor is "wet" (the player is in water) and the armor is being used
     * by the player.
     * This modifier also also deals extra damage to entities that can breathe under water when they hurt the player.
     * The damage type is fire damage.
     * Amount dealt to target entity is 4.
     *
     * <br>
     * <b>String Reference:<br></b>
     * "reactive"
     * "mmd-reactive"
     */
    public static final AbstractTrait reactive = new TraitReactive();


    public static final AbstractTrait brittle = new TraitBrittle();

    /**
     * <h2><u>Poisonous Armor Modifier:</u></h2>
     * <b>Name:</b> poisonous
     * <br>
     * <b>Desc:</b>
     * Applies poison for 5 seconds when a living entity hits the player.
     *
     * <br>
     * <b>String Reference:<br></b>
     * "poisonous"
     * "mmd-poisonous"
     */
    public static final AbstractTrait poisonous = new TraitPoisonous();

    private MMDTraitsCA() {

    }
}
