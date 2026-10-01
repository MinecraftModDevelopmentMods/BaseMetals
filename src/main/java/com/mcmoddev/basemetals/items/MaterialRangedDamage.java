package com.mcmoddev.basemetals.items;

import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.projectile.EntityArrow;

/** Combines the launcher and ammunition materials to calculate shot damage. */
final class MaterialRangedDamage {

	private static final double NEUTRAL_COMPONENT_DAMAGE = 1.0D;
	private static final double MINIMUM_PROJECTILE_DAMAGE = 0.1D;

	private MaterialRangedDamage() {
		throw new IllegalAccessError("Not instantiable");
	}

	/**
	 * Replaces the vanilla contribution of 1.0 with the material's attack damage.
	 * Called once for ammunition and once for the launcher, before Power bonuses.
	 */
	static void applyComponent(final EntityArrow projectile, final MMDMaterial material) {
		final double adjustedDamage = projectile.getDamage()
				- NEUTRAL_COMPONENT_DAMAGE + material.getBaseAttackDamage();

		projectile.setDamage(Math.max(MINIMUM_PROJECTILE_DAMAGE, adjustedDamage));
	}
}
