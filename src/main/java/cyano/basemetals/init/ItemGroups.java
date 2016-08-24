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
		return (delta==0)? a.getItem().getUnlocalizedName().compareToIgnoreCase(b.getItem().getUnlocalizedName()) : delta;
	};

	public static CreativeTabs tab;

	private static final Item tabItem = Items.copper_crackhammer;

	private static boolean initDone = false;

	public static void init() {
		if(initDone)
			return;

		tab = FunctionalCreativeTab.create(BaseMetals.MODID.concat(".blocks"))
				.setIconMetadata(3)
				.setIcon(tabItem)
				.setItemSortingAlgorithm(sortingAlgorithm);

		initDone = true;
	}
}

