package cyano.basemetals.init;

import net.minecraft.block.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Creates creative tabs easier and lazier.
 * @author TheCodedOne
 */
public class FunctionalCreativeTab extends CreativeTabs {

	protected ItemStack icon;
	protected boolean searchable;
	protected java.util.Comparator<ItemStack> itemSortingAlgorithm;
	protected int tabPage;

	public FunctionalCreativeTab setTabPage(int tabPage) {
		this.tabPage = tabPage;
		return this;
	}

	private FunctionalCreativeTab(String label) {
		super(label);
	}

	public static FunctionalCreativeTab create(String label) {
		return new FunctionalCreativeTab(label);
	}

	@Override
	public Item getTabIconItem() {
		if(icon!=null) return icon.getItem();
		else return net.minecraft.init.Items.APPLE;
	}

	/**
	 * Set tab icon
	 *
	 * @param icon ItemStack of icon
	 */
	public FunctionalCreativeTab setIcon(ItemStack icon) {
		this.icon = icon;
		return this;
	}
	/**
	 * Set tab icon
	 *
	 * @param icon Item of icon
	 */
	public FunctionalCreativeTab setIcon(Item icon) {
		this.icon = new ItemStack(icon);
		return this;
	}

	/**
	 * Set tab icon
	 *
	 * @param icon Block of icon
	 */
	public FunctionalCreativeTab setIcon(Block icon) {
		this.icon = new ItemStack(icon);
		return this;
	}

	@Override
	public int getIconItemDamage() {
		if(icon!=null) return icon.getMetadata();
		return 0;
	}

	@Override
	public int getTabPage() {
		return tabPage;
	}

	/**
	 * Sets to show searchbar
	 */
	public FunctionalCreativeTab setSearchable(boolean searchable) {
		this.searchable = searchable;
		if(searchable)setBackgroundImageName("item_search.png");
		return this;
	}

	public FunctionalCreativeTab setItemSortingAlgorithm(BiFunction<ItemStack,ItemStack,Integer> itemSortingAlgorithm) {
		this.itemSortingAlgorithm = itemSortingAlgorithm::apply;
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(List<ItemStack> itemList) {
		super.displayAllRelevantItems(itemList);
		if (itemSortingAlgorithm != null) itemList.sort(itemSortingAlgorithm);
	}

	public FunctionalCreativeTab setIconMetadata(int meta) {
		if(this.icon!=null) this.icon.setItemDamage(meta);
		else FMLLog.bigWarning("No tab icon has been set, the metadata will not work");
		return this;
	}

	@Override
	public boolean hasSearchBar() {
		return searchable;
	}

	private static void tutorial() {
		CreativeTabs tab = FunctionalCreativeTab.create("label")
				.setIcon(net.minecraft.init.Blocks.DIRT) //Can be Block, Item or ItemStack
				.setIcon(net.minecraft.init.Items.APPLE)
				.setIcon(new ItemStack(net.minecraft.init.Items.GOLDEN_APPLE,1,1)) //Example of Metadata used in itemstack
				.setTabPage(1) //Set the page the tab will be seen on
				// If you used an ItemStack it will use the metadata from that stack unless overridden by this function
				.setIconMetadata(1) //Metadata of the icon,
				.setItemSortingAlgorithm(ItemGroups.sortingAlgorithm); //Sets the sorting algorithm for the creative tab
	}
}
