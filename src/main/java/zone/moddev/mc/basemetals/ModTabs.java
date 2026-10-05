package zone.moddev.mc.basemetals;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModTabs {
    public static final ItemGroup BLOCKS = tab("blocks", "starsteel_block", Item.getItemFromBlock(Blocks.IRON_BLOCK));
    public static final ItemGroup ITEMS = tab("items", "starsteel_gear", Items.IRON_INGOT);
    public static final ItemGroup TOOLS = tab("tools", "starsteel_pickaxe", Items.IRON_PICKAXE);
    public static final ItemGroup COMBAT = tab("combat", "starsteel_sword", Items.IRON_SWORD);

    private ModTabs() {}

    private static ItemGroup tab(String suffix, final String iconId, final Item fallback) {
        return new ItemGroup(BaseMetals.MOD_ID + "." + suffix) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack createIcon() {
                Item registered = ForgeRegistries.ITEMS.getValue(new ResourceLocation(BaseMetals.MOD_ID, iconId));
                return new ItemStack(registered == null ? fallback : registered);
            }
        };
    }
}
