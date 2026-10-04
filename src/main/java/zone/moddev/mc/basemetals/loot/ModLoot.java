package zone.moddev.mc.basemetals.loot;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import zone.moddev.mc.basemetals.BaseMetals;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Adds Base Metals loot to vanilla chest tables. */
@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ModLoot {
    private static final Map<ResourceLocation, ResourceLocation> INJECTIONS;

    static {
        Map<ResourceLocation, ResourceLocation> injections = new LinkedHashMap<ResourceLocation, ResourceLocation>();
        inject(injections, "abandoned_mineshaft", "abandoned_mineshaft");
        inject(injections, "desert_pyramid", "desert_pyramid");
        inject(injections, "end_city_treasure", "end_city_treasure");
        inject(injections, "jungle_temple", "jungle_temple");
        inject(injections, "nether_bridge", "nether_bridge");
        inject(injections, "simple_dungeon", "simple_dungeon");
        inject(injections, "spawn_bonus_chest", "spawn_bonus_chest");
        inject(injections, "stronghold_corridor", "stronghold_corridor");
        inject(injections, "stronghold_crossing", "stronghold_crossing");
        // Minecraft 1.13 still has the single village blacksmith chest.
        inject(injections, "village_blacksmith", "village_blacksmith");
        INJECTIONS = Collections.unmodifiableMap(injections);
    }

    private ModLoot() {}

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation auxiliary = INJECTIONS.get(event.getName());
        if (auxiliary == null || event.getTable().getPool("basemetals_injection") != null) return;
        LootEntry entry = new LootEntryTable(auxiliary, 1, 0, new LootCondition[0], "basemetals_table");
        event.getTable().addPool(new LootPool(new LootEntry[] { entry }, new LootCondition[0],
                new RandomValueRange(1.0F), new RandomValueRange(0.0F), "basemetals_injection"));
    }

    private static void inject(Map<ResourceLocation, ResourceLocation> injections, String vanilla, String auxiliary) {
        injections.put(new ResourceLocation("minecraft", "chests/" + vanilla),
                new ResourceLocation(BaseMetals.MOD_ID, "chests/inject/" + auxiliary));
    }
}
