package cyano.basemetals.init;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Creates a creative tab better than before.
 *
 * @author TheCodedOne
 */
public class EnhancedCreativeTab extends CreativeTabs {

    protected Item item;
    protected int meta;
    protected java.util.Comparator<ItemStack> itemSortingAlgorithm = null;

    private EnhancedCreativeTab(String label, Item item, int meta) {
        super(label);
        this.item = item;
        this.meta = meta;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(List<ItemStack> itemList) {
        if (this.itemSortingAlgorithm != null)
            itemList.sort(this.itemSortingAlgorithm);
        super.displayAllRelevantItems(itemList);
    }

    public static EnhancedCreativeTab create(String label) {
        return new EnhancedCreativeTab(label, Items.APPLE, 0);
    }

    public EnhancedCreativeTab setIcon(ItemStack stack) {
        Preconditions.checkArgument(stack != null, "null 'ItemStack' is not supported");
        this.item = stack.getItem();
        this.meta = stack.getMetadata();
        return this;
    }

    public EnhancedCreativeTab setIcon(Block block) {
        Preconditions.checkArgument(block != null, "null 'Block' is not supported");
        return setIcon(new ItemStack(block, 1, meta));
    }

    public EnhancedCreativeTab setIcon(Item item) {
        Preconditions.checkArgument(item != null, "null 'Item' is not supported");
        return setIcon(new ItemStack(item, 1, meta));
    }


    public EnhancedCreativeTab setItemSortingAlgorithm(BiFunction<ItemStack, ItemStack, Integer> itemSortingAlgorithm) {
        Preconditions.checkArgument(itemSortingAlgorithm != null, "null 'BiFunction<ItemStack, ItemStack, Integer>' is not supported");
        this.itemSortingAlgorithm = itemSortingAlgorithm::apply;
        return this;
    }

    public EnhancedCreativeTab setMeta(int meta) {
        this.meta = meta;
        return this;
    }

    @Override
    public Item getTabIconItem() {
        return item;
    }

    @Override
    public int getIconItemDamage() {
        return meta;
    }
}