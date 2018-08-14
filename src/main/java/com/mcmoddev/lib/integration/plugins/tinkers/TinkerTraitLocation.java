package com.mcmoddev.lib.integration.plugins.tinkers;

import java.util.Locale;

public enum TinkerTraitLocation {
	HEAD, HANDLE, EXTRA, BOW, BOWSTRING, PROJECTILE, SHAFT, FLETCHING, GENERAL;

	@Override
	public String toString() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}
