package cyano.basemetals.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import cyano.basemetals.BaseMetals;
import cyano.basemetals.material.MetalMaterial;

public abstract class DungeonLoot {

	private static boolean initDone = false;
	public static void init(){
		if(initDone) return;
		cyano.basemetals.init.Materials.init();
		cyano.basemetals.init.Items.init();

		for(MetalMaterial metal : Materials.getAllMetals()) {
			WeightedRandomChestContent loot = makeChestLootEntry(metal);
			if(loot == null) continue;
			if(metal.getLootSpawnWeight() > 20) ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(loot);
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(loot);
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(loot);
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(loot);
		}
		initDone = true;
	}

	private static WeightedRandomChestContent makeChestLootEntry(MetalMaterial metal){
		Item item = cyano.basemetals.init.Items.getItemByName(metal.getName()+"_ingot");
		if(item == null) return null;
		int spawnWeight = (int)(BaseMetals.chestLootFactor * metal.getLootSpawnWeight());
		if(spawnWeight <= 0) return null;
		ItemStack itemStack = new ItemStack(item, 1);
		int minSpawnNumber=1;
		int maxSpawnNumber=Math.max(Math.min(metal.getLootSpawnWeight() / 5, 6), 2);
		return new WeightedRandomChestContent(itemStack, minSpawnNumber, maxSpawnNumber, spawnWeight);
	}
}
