package zone.moddev.mc.basemetals.loot;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import zone.moddev.mc.basemetals.BaseMetals;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
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
        inject(injections, "village/village_armorer", "village_blacksmith");
        inject(injections, "village/village_toolsmith", "village_blacksmith");
        inject(injections, "village/village_weaponsmith", "village_blacksmith");
        INJECTIONS = Collections.unmodifiableMap(injections);
    }

    private ModLoot() {}

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation auxiliary = INJECTIONS.get(event.getName());
        if (auxiliary == null || event.getTable().getPool("basemetals_injection") != null) return;
        event.getTable().addPool(LootPool.lootPool().name("basemetals_injection")
                .setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(auxiliary)).build());
    }

    private static void inject(Map<ResourceLocation, ResourceLocation> injections, String vanilla, String auxiliary) {
        injections.put(new ResourceLocation("minecraft", "chests/" + vanilla),
                new ResourceLocation(BaseMetals.MOD_ID, "chests/inject/" + auxiliary));
    }
}
