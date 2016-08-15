package cyano.basemetals.init;

public abstract class DungeonLoot {

    // See the following link for a nice tutorial on how loot works in MC1.9:
    // http://www.minecraftforum.net/forums/minecraft-discussion/redstone-discussion-and/command-blocks/2546347-1-9-custom-loot-tables#Filelocation
	
	private static boolean initDone = false;
	public static void init() {
		if(initDone)
			return;
		// loot now handled by AdditionalLootTables mod
		initDone = true;
	}
}
