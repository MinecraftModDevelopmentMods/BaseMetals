package zone.moddev.mc.basemetals;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModTabs {
    public static final CreativeModeTab BLOCKS = tab("blocks", "starsteel_block", Item.byBlock(Blocks.IRON_BLOCK));
    public static final CreativeModeTab ITEMS = tab("items", "starsteel_gear", Items.IRON_INGOT);
    public static final CreativeModeTab TOOLS = tab("tools", "starsteel_pickaxe", Items.IRON_PICKAXE);
    public static final CreativeModeTab COMBAT = tab("combat", "starsteel_sword", Items.IRON_SWORD);

    private ModTabs() {}

    private static CreativeModeTab tab(String suffix, final String iconId, final Item fallback) {
        return new CreativeModeTab(BaseMetals.MOD_ID + "." + suffix) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {
                Item registered = ForgeRegistries.ITEMS.getValue(new ResourceLocation(BaseMetals.MOD_ID, iconId));
                return new ItemStack(registered == null ? fallback : registered);
            }
        };
    }
}
