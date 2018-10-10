package com.mcmoddev.lib.integration.plugins.tinkers;

import java.util.Locale;

public enum ArmoryTraitLocation {
	CORE, PLATES, TRIM, GENERAL;

	@Override
	public String toString() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}
