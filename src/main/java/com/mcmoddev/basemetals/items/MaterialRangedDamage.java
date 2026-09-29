package com.mcmoddev.basemetals.items;

import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.projectile.EntityArrow;

/** Shared material contribution used by Base Metals ranged weapons. */
final class MaterialRangedDamage {

	private static final double NEUTRAL_COMPONENT_DAMAGE = 1.0D;
	private static final double MINIMUM_PROJECTILE_DAMAGE = 0.1D;

	private MaterialRangedDamage() {
		throw new IllegalAccessError("Not instantiable");
	}

	/**
	 * Replaces one vanilla-neutral launcher or ammunition contribution with the
	 * supplied material's established base attack damage.
	 *
	 * @param projectile projectile whose base damage is being composed
	 * @param material launcher or ammunition material
	 */
	static void applyComponent(final EntityArrow projectile, final MMDMaterial material) {
		final double adjustedDamage = projectile.getDamage()
				- NEUTRAL_COMPONENT_DAMAGE + material.getBaseAttackDamage();
		projectile.setDamage(Math.max(MINIMUM_PROJECTILE_DAMAGE, adjustedDamage));
	}
}
