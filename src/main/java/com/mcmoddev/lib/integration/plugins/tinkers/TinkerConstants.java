package com.mcmoddev.lib.integration.plugins.tinkers;


public final class TinkerConstants {
	private TinkerConstants() {
		throw new IllegalAccessError("Cannot instantiate this class");
	}
	
	public static final class TinkerStatNames {
		private TinkerStatNames() {
			throw new IllegalAccessError("Cannot instantiate this class");
		}
		
		/*
		 * Binding/Extra stats
		 */
		public static final String EXTRA_DURABILTIY   = "extraDurability";
	
		/*
		 * Tool Head Stats
		 */
		public static final String HEAD_DURABILITY    = "headDurability";
		public static final String MINING_SPEED       = "miningSpeed";
		public static final String MINING_LEVEL       = "miningLevel";
		public static final String HEAD_ATTACK_DAMAGE = "headAttackDamage";
		
		/*
		 * Body/Tool Rod Stats
		 */
		public static final String BODY_DURABILITY    = "bodyDurability";
		public static final String BODY_MODIFIER      = "bodyModifier";
		
		/*
		 * Bow Stats
		 */
		public static final String BOW_DRAW_SPEED     = "bowDrawSpeed";
		public static final String BOW_DAMAGE         = "bowDamage";
		public static final String BOW_RANGE          = "bowRange";
		
		/*
		 * Arrowshaft Stats
		 */
		public static final String ARROWSHAFT_BONUS_AMMO = "shaftBonusAmmo";
		public static final String ARROWSHAFT_MODIFIER   = "shaftModifier";

		/*
		 * Fletching Stats
		 */
		public static final String FLETCHING_ACCURACY = "fletchingAccuracy";
		public static final String FLETCHING_MODIFIER = "fletchingModifier";
		
		/*
		 * BowString Stats
		 */
		public static final String BOWSTRING_MODIFIER = "bowstringModifier";
	}
}
