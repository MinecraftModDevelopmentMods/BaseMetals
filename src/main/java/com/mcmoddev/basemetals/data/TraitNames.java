package com.mcmoddev.basemetals.data;

import com.mcmoddev.lib.data.SharedStrings;

/**
 * <p><h1>Trait Names:</h1> <br>
 * These strings refer to TiC tool modifiers.
 * All traits found in this class are used by Base Metals in some way.
 * </p>
 *
 * <h2><u>Modifiers added by Base Metals:</u></h2>
 * <ul>
 *     <li>Sparkly:<br>
 *         {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitSparkly}</li>
 *     <li>Soft:<br>
 *         {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitSoft}</li>
 * </ul>
 *
 *
 * <h2><u>Modifiers added by TiC:</u></h2>
 * <ul>
 *     <li>Enderference:<br>
 *         {@link slimeknights.tconstruct.tools.traits.TraitEnderference}</li>
 *     <li>Jagged:<br>
 *         {@link slimeknights.tconstruct.tools.traits.TraitJagged}</li>
 *     <li>holy:<br>
 *         {@link slimeknights.tconstruct.tools.traits.TraitHoly}</li>
 *     <li>coldblooded:<br>
 *         {@link slimeknights.tconstruct.tools.traits.TraitColdblooded}</li>
 *     <li>insatiable:<br>
 *         {@link slimeknights.tconstruct.tools.traits.TraitInsatiable}</li>
 *     <li>aquadynamic:<br>
 *         {@link slimeknights.tconstruct.tools.traits.TraitAquadynamic}</li>
 *     <li>dense:<br>
 *         {@link slimeknights.tconstruct.tools.traits.TraitDense}</li>
 *     <li>freezing:<br>
 *         {@link slimeknights.tconstruct.tools.traits.TraitFreezing}</li>
 * </ul>
 *
 * <h2><u>Other Modifiers:</u></h2>
 * Other modifiers that can be found in the library of Base Metals but are not utilized by Base Metals:<br>
 * If you are looking for the modifiers applicable to Modern Metals, please use the references below.
 *
 * <h3><u>Modifiers added by Modern Metals:</u></h3>
 * <ul>
 *     <li>Heavy:<br>
 *         {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitHeavy}</li>
 *     <li>Brittle:<br>
 *         {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitBrittle}</li>
 *     <li>Toxic:<br>
 *         {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitToxic}</li>
 *     <li>Radioactive:<br>
 *         {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitRadioactive}</li>
 *     <li>Reactive:<br>
 *         {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitReactive}</li>
 * </ul>
 */
public final class TraitNames {

	//:: Base Metal Modifiers
	/**
	 * <h2><u>Sparkly Tool Modifier:</u></h2>
	 * <b>Name:</b> sparkly
	 * <br>
	 * <b>Desc:</b> This modifier, when applied to tools, has an automatic durability repair effect. Heals the tool for
	 * 1 durability every 200 ticks. (10 seconds)
	 *
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitSparkly}
	 */
	public static final String SPARKLY = "sparkly";

	/**
	 * <h2><u>Soft Tool Modifier:</u></h2>
	 * <b>Name:</b> soft
	 * <br>
	 * <b>Desc:</b> This modifier, when applied to tools, increases the damage used by the tool when the tool is damaged by
	 * a factor of 1.25 <br><em>(i.e. 1.25 more damage will be dealt to the tool's durability)</em>
	 *
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitSoft}
	 */
	public static final String SOFT = "soft";

	//:: Tinkers Modifiers
	/**
	 * <h2><u>Enderference Tool Modifier:</u></h2>
	 * <b>Name:</b> enderference
	 * <br>
	 * <b>Desc:</b> <a href=http://tinkers-construct.wikia.com/wiki/Material_Stats> doc source and accreditation</a><br>
	 * Prevent an enderman from teleporting for 5 seconds after hitting it.
	 *
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link slimeknights.tconstruct.tools.traits.TraitEnderference}
	 */
	public static final String ENDERFERENCE = "enderference";

	/**
	 * <h2><u>Jagged Tool Modifier:</u></h2>
	 * <b>Name:</b> jagged
	 * <br>
	 * <b>Desc:</b> <a href=http://tinkers-construct.wikia.com/wiki/Material_Stats> doc source and accreditation</a><br>
	 * Gains bonus damage when tool is damaged. The more damaged it is (difference, not percentage), the more damage it deals.
	 * Tool damage output is increased by: <br> log((maxDurability - durability) / 72d + 1d) * 2;
	 *
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link slimeknights.tconstruct.tools.traits.TraitJagged}
	 */
	public static final String JAGGED = "jagged";

	/**
	 * <h2><u>Holy Tool Modifier:</u></h2>
	 * <b>Name:</b> holy
	 * <br>
	 * <b>Desc:</b>
	 * Damage of the tool is increased by 5 if target is an undead mob.
	 *
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link slimeknights.tconstruct.tools.traits.TraitHoly}
	 */
	public static final String HOLY = "holy";

	/**
	 * <h2><u>Coldblooded Tool Modifier:</u></h2>
	 * <b>Name:</b> coldblooded
	 * <br>
	 * <b>Desc:</b>
	 * If the target of the tool is at full health, the damage dealt to the target is equivalent to the original
	 * damage dealt plus half of the original damage dealt.
	 *
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link slimeknights.tconstruct.tools.traits.TraitColdblooded}
	 */
	public static final String COLDBLOODED = "coldblooded";

	/**
	 * <h2><u>Insatiable Tool Modifier:</u></h2>
	 * <b>Name:</b> insatiable
	 * <br>
	 * <b>Desc:</b> <a href=http://tinkers-construct.wikia.com/wiki/Material_Stats> doc source and accreditation</a><br>
	 * Using the tool adds a level of insatiable for 15 seconds, limited to 10 levels. Increases speed and damage by (level / 3).
	 *
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link slimeknights.tconstruct.tools.traits.TraitInsatiable}
	 */
	public static final String INSATIABLE = "insatiable";

	/**
	 * <h2><u>Aquadynamic Tool Modifier:</u></h2>
	 * <b>Name:</b> aquadynamic
	 * <br>
	 * <b>Desc:</b>
	 * Tool mining rate underwater is negated, and has the speeds of mining above water.
	 * Tool mining rate while raining is 1.6 that of the rain fall of the current biome that is being mined.
	 * I.E. mining speed is increased by the rainfall rate / 1.6
	 *
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link slimeknights.tconstruct.tools.traits.TraitAquadynamic}
	 */
	public static final String AQUADYNAMIC = "aquadynamic";

	/**
	 * <h2><u>Dense Tool Modifier:</u></h2>
	 * <b>Name:</b> dense
	 * <br>
	 * <b>Desc:</b>
	 * The chance for a tool to be damaged on use is changed to:
	 * (0.75f * (1f - durability / maxDurability))^3
	 *
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link slimeknights.tconstruct.tools.traits.TraitDense}
	 */
	public static final String DENSE = "dense";

	/**
	 * <h2><u>Freezing Tool Modifier:</u></h2>
	 * <b>Name:</b> freezing
	 * <br>
	 * <b>Desc:</b>
	 * Applies the slowness potion effect to the hit target of the tool.
	 * Each hit adds a level of slowness to the target up to a maximum of level 4 slowness.
	 *
	 * <br>
	 * <b>Modifier class:</b>
	 * {@link slimeknights.tconstruct.tools.traits.TraitFreezing}
	 */
	public static final String FREEZING = "freezing";

	public static final String ICY = "icy";

	public static final String MALLEABLE = "malleable";

	public static final String MAGNETIC = "magnetic";

	public static final String MAGNETIC2 = "magnetic2";

	public static final String REACTIVE = "reactive";

	public static final String SHOCKING = "shocking";

	public static final String BRITTLE = "brittle";

	private TraitNames() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}
}
