package cyano.basemetals.init;

import cyano.basemetals.BaseMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * This class initializes all item groups in Base Metals.
 *
 * @author DrCyano
 *
 */
public class ItemGroups {

	public static final java.util.function.BiFunction<ItemStack, ItemStack, Integer> sortingAlgorithm = (ItemStack a, ItemStack b) -> {
		final int delta = Items.getSortingValue(a) - Items.getSortingValue(b);
		if (delta == 0) {
			return a.getItem().getUnlocalizedName().compareToIgnoreCase(b.getItem().getUnlocalizedName());
		}
		return delta;
	};

	public static CreativeTabs tab_blocks;
	public static CreativeTabs tab_items;
	public static CreativeTabs tab_tools;

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		// placeholders
		// Item blockItem = Items.copper_crackhammer;
		// Item itemItem = Items.copper_crackhammer;
		// Item toolItem = Items.copper_crackhammer;

		tab_blocks = new GeneralizedCreativeTab(BaseMetals.MODID.concat(".blocks"), true, () -> Items.copper_crackhammer);

		tab_items = tab_blocks;
		tab_tools = tab_items;

		initDone = true;
	}
}
