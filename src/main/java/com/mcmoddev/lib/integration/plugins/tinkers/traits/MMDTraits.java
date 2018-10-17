package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import slimeknights.tconstruct.library.traits.AbstractTrait;

/**
 * Contains static final references of the MMD Base Metals and Modern Metals tool modifiers.
 */
public class MMDTraits {

	/**
	 * <h2><u>Soft Tool Modifier:</u></h2>
	 * <b>Name:</b> soft
	 * <br>
	 * <b>Desc:</b> This modifier, when applied to tools, increases the damage used by the tool when the tool is damaged by
	 * a factor of 1.25. <br><em>(i.e. 1.25 more damage will be dealt to the tool's durability)</em>
	 *
	 * <br>
	 * <b>String Reference:<br></b>
	 * TraitNames.SOFT<br>
	 * "soft"<br>
	 * "mmd-soft"<br>
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitSoft}
	 *
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 * @author Java doc author: Vase of Petunias
	 */
	public static final AbstractTrait soft = new TraitSoft();

	/**
	 * <h2><u>Sparkly Tool Modifier:</u></h2>
	 * <b>Name:</b> sparkly
	 * <br>
	 * <b>Desc:</b> This modifier, when applied to tools, has an automatic durability repair effect. Heals the tool for
	 * 1 durability every 200 ticks. (10 seconds)
	 *
	 * <br>
	 * <b>String Reference:<br></b>
	 * TraitNames.SPARKLY<br>
	 * "sparkly"<br>
	 * "mmd-sparkly"<br>
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitSparkly}
	 *
	 * @author Java doc author: Vase of Petunias
	 */
	public static final AbstractTrait sparkly = new TraitSparkly();

	/**
	 * <h2><u>Heavy Tool Modifier:</u></h2>
	 * <b>Name:</b> heavy
	 * <br>
	 * <b>Desc:</b>
	 * Increases the knockback of the tool by the factor of the tool's attack stat.
	 *
	 * <br>
	 * <b>String Reference:<br></b>
	 * "heavy"<br>
	 * "mmd-heavy"<br>
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitHeavy}
	 *
	 * @author Java doc author: Vase of Petunias
	 */
	public static final AbstractTrait heavy = new TraitHeavy();

	/**
	 * <h2><u>Brittle Tool Modifier:</u></h2>
	 * <b>Name:</b> brittle
	 * <br>
	 * <b>Desc:</b>
	 * Tools will lose 0 or 1 durability if the durability of the tool minus one is less than 5.
	 * Otherwise, the tool will lose a minimum of 0 to a maximum of 5 durability at random.
	 * Durability loss is applied and calculated if the block that is being broken is a stone block.
	 *
	 * <br>
	 * <b>String Reference:<br></b>
	 * "brittle"<br>
	 * "mmd-brittle"<br>
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitBrittle}
	 *
	 * @author Java doc author: Vase of Petunias
	 */
	public static final AbstractTrait brittle = new TraitBrittle();

	/**
	 * <h2><u>Toxic Tool Modifier:</u></h2>
	 * <b>Name:</b> toxic
	 * <br>
	 * <b>Desc:</b>
	 * Applies poison 2 for 5 seconds as well as blindness for 2.5 seconds when a living entity is hit.
	 *
	 * <br>
	 * <b>String Reference:<br></b>
	 * "toxic"
	 * "mmd-toxic"
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitToxic}
	 *
	 * @author Java doc author: Vase of Petunias
	 */
	public static final AbstractTrait toxic = new TraitToxic();

	/**
	 * <h2><u>Radioactive Tool Modifier:</u></h2>
	 * <b>Name:</b> radioactive
	 * <br>
	 * <b>Desc:</b>
	 * Does nothing if the tool is broken or if the tool is plated with lead.
	 * Otherwise, there is a 50% chance of having the tool either apply nausea for 1 second or poison for half a second.
	 * Effects are applied when ever the tool is within the player's inventory.
	 *
	 * <br>
	 * <b>String Reference:<br></b>
	 * "radioactive"
	 * "mmd-radioactive"
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitRadioactive}
	 *
	 * @author Java doc author: Vase of Petunias
	 */
	public static final AbstractTrait radioactive = new TraitRadioactive();

	/**
	 * <h2><u>Reactive Tool Modifier:</u></h2>
	 * <b>Name:</b> reactive
	 * <br>
	 * <b>Desc:</b>
	 * Damages the tool by 5 durability if the tool is "wet" (the player is in water) and the tool is being used
	 * by the player.
	 * This modifier also also deals extra damage to entities that can breathe under water.
	 * The damage type is fire damage.
	 * Amount dealt to target entity is 4, and 8 on critical strikes.
	 *
	 * <br>
	 * <b>String Reference:<br></b>
	 * "reactive"
	 * "mmd-reactive"
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitReactive}
	 *
	 * @author Java doc author: Vase of Petunias
	 */
	public static final AbstractTrait reactive = new TraitReactive();
}
