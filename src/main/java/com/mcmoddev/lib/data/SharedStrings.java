package com.mcmoddev.lib.data;

public class SharedStrings {

	public static final String NOT_INSTANTIABLE = "Not a instantiable class";
	public static final String INVALID_FINGERPRINT = "Invalid fingerprint detected!";
	public static final String UPDATE_JSON_URL = "https://raw.githubusercontent.com/MinecraftModDevelopmentMods/";
	public static final String MMD_PROXY_GROUP = "com.mcmoddev.";
	public static final String DOT_PROXY_DOT = ".proxy.";
	public static final String CLIENTPROXY = "ClientProxy";
	public static final String SERVERPROXY = "ServerProxy";
	public static final String ORESPAWN_MODID = "orespawn";
	public static final String ORESPAWN_VERSION = "3.2.2";
	public static final String ORESPAWN_MISSING_TEXT = "MMD Ore Spawn Mod (fallback generator disabled, MMD OreSpawn enabled)";
	public static final String TAB_BLOCKS = "blocks";
	public static final String TAB_ITEMS = "items";
	public static final String TAB_TOOLS = "tools";
	public static final String TAB_COMBAT = "combat";

	private SharedStrings() {
		throw new IllegalAccessError(NOT_INSTANTIABLE);
	}
}
