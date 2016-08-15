package cyano.basemetals.init;

import cyano.basemetals.BaseMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * This class initializes all item groups in Base Metals.
 * @author DrCyano
 *
 */
public class ItemGroups {

	public static CreativeTabs tab_blocks;
	public static CreativeTabs tab_items;
	public static CreativeTabs tab_tools;

	@SuppressWarnings("unused")
	private static Item blockItem;
//	private static Item itemItem;
//	private static Item toolItem;

	private static boolean initDone = false;

	/**
	 * 
	 */
	public static void init() {
		if(initDone)
			return;

		// placeholders
		blockItem = Items.copper_crackhammer;
//		itemItem = Items.copper_crackhammer;
//		toolItem = Items.copper_crackhammer;

		tab_blocks = new FunctionalCreativeTab(BaseMetals.MODID.concat(".blocks"), false,
				()->Items.copper_crackhammer, 
				(ItemStack a,ItemStack b)->{
					int delta = Items.getSortingValue(a) - Items.getSortingValue(b);
					if(delta == 0)
						return a.getItem().getUnlocalizedName().compareToIgnoreCase(b.getItem().getUnlocalizedName());
					return delta;
				});
//		tab_items = new FunctionalCreativeTab(BaseMetals.MODID.concat(".items"), ()->itemItem);
//		tab_tools = new FunctionalCreativeTab(BaseMetals.MODID.concat(".tools"), ()->toolItem);
		tab_items = tab_blocks;
		tab_tools = tab_items;

		initDone = true;
	}
}

