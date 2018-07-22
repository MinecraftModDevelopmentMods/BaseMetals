package com.mcmoddev.lib.data;

public class ActiveModData {
	private String activeMod = null;

	public static final ActiveModData instance = new ActiveModData();

	public void setActive(final String name) {
		activeMod = name;
	}

	public String activeMod() {
		return activeMod == null ? "basemetals" : activeMod;
	}
}
