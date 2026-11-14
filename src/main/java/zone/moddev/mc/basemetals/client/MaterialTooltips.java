package zone.moddev.mc.basemetals.client;

import java.util.Map;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.lib.common.runtime.MaterialBacked;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.TieredItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/** Describes each special material's effect on its equipment. */
@EventBusSubscriber(modid = BaseMetals.MOD_ID, value = Dist.CLIENT)
public final class MaterialTooltips {
    // Material -> bonus shown in its tool tooltip; 0 when that tooltip takes no value.
    private static final Map<String, Integer> TOOL_BONUS = Map.of(
            "adamantine", 4, "aquarium", 4, "coldiron", 3, "mithril", 0, "starsteel", 10);

    private MaterialTooltips() {}

    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (!(item instanceof MaterialBacked backed)) return;
        String material = backed.material().name()
                .filter(id -> id.getNamespace().equals(BaseMetals.MOD_ID))
                .map(id -> id.getPath()).orElse(null);
        if (material == null || !TOOL_BONUS.containsKey(material)) return;
        if (item instanceof ArmorItem) {
            event.getToolTip().add(Component.translatable("tooltip." + material + ".armor"));
        } else if (item instanceof TieredItem || item instanceof ShearsItem || item instanceof ShieldItem
                || item instanceof ProjectileWeaponItem || item instanceof FishingRodItem) {
            int bonus = TOOL_BONUS.get(material);
            event.getToolTip().add(bonus == 0
                    ? Component.translatable("tooltip." + material + ".tool")
                    : Component.translatable("tooltip." + material + ".tool", bonus));
        }
    }
}
