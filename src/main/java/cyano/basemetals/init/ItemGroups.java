package cyano.basemetals.init;

import cyano.basemetals.BaseMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;

/**
 * This class initializes all item groups in Base Metals.
 * @author DrCyano
 *
 */
public class ItemGroups {

	public static final java.util.function.BiFunction<ItemStack,ItemStack,Integer> sortingAlgorithm = (ItemStack a,ItemStack b)->{
		int delta = Items.getSortingValue(a) - Items.getSortingValue(b);
		if(delta == 0)
			return a.getItem().getUnlocalizedName().compareToIgnoreCase(b.getItem().getUnlocalizedName());
		return delta;
	};

	public static CreativeTabs tab;

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

		tab = FunctionalCreativeTab.create(BaseMetals.MODID.concat(".blocks"))
				.setIconMetadata(3)
				.setIcon(blockItem)
				.setItemSortingAlgorithm(sortingAlgorithm);

		initDone = true;
	}
}

