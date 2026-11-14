package zone.moddev.mc.basemetals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.trade.BaseMetalsTrades;
import zone.moddev.mc.basemetals.migration.Legacy112WorldMigrator;
import zone.moddev.mc.lib.common.runtime.StandardItems;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.storage.LevelResource;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

/** NeoForge event handlers for Base Metals. */
public final class BaseMetalsEvents {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        var recipes = new ArrayList<RecipeHolder<?>>();

        for (var recipe : player.server.getRecipeManager().getRecipes()) {
            if (!recipe.id().getNamespace().equals(BaseMetals.MOD_ID) || recipe.value().isSpecial()) continue;

            var unlock = player.server.getAdvancements().get(ResourceLocation.fromNamespaceAndPath(
                    BaseMetals.MOD_ID, "recipes/" + recipe.id().getPath()));
            if (unlock != null && player.getAdvancements().getOrStartProgress(unlock).isDone()) recipes.add(recipe);
        }

        player.awardRecipes(recipes);
    }

    @SubscribeEvent
    public void onServerAboutToStart(ServerAboutToStartEvent event) {
        try {
            java.nio.file.Path worldRoot = event.getServer().getWorldPath(LevelResource.ROOT);
            if (Legacy112WorldMigrator.migrateIfNeeded(worldRoot)) {
                Legacy112WorldMigrator.refreshLoadedSingleplayerPlayer(event.getServer(), worldRoot);
            }
        } catch (java.io.IOException exception) {
            throw new IllegalStateException(
                    "Base Metals could not safely pre-flatten the legacy Forge 1.10/1.12 world", exception);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        // Material effects are MMDLib player-tick handlers; see effects.MaterialEffects.
        if (event.getEntity() instanceof ServerPlayer player && player.tickCount % 20 == 0) {
            BaseMetalsAdvancements.onEquipment(player);
        }
    }

    @SubscribeEvent
    public void onShieldUpgrade(AnvilUpdateEvent event) {
        if (!(event.getLeft().getItem() instanceof StandardItems.Shield shield)
                || event.getLeft().getCount() != 1 || event.getRight().getCount() != 1) return;
        MaterialDefinition current = MaterialCatalogue.of(shield.material()).orElse(null);
        if (current == null) return;
        MaterialDefinition upgrade = shieldMaterials().values().stream()
                .filter(candidate -> candidate.hardness() > current.hardness())
                .filter(candidate -> event.getRight().is(ItemTags.create(
                        ResourceLocation.fromNamespaceAndPath("c", "plates/" + candidate.name()))))
                .sorted(Comparator.comparingDouble(MaterialDefinition::hardness)
                        .thenComparing(MaterialDefinition::name))
                .findFirst().orElse(null);
        if (upgrade == null) return;
        if (!zone.moddev.mc.basemetals.config.ContentPolicy.active().allows("basemetals:" + upgrade.name() + "_shield")) return;
        ItemStack output = ModContent.item(upgrade.name() + "_shield").get().getDefaultInstance();
        output.set(DataComponents.ENCHANTMENTS, event.getLeft().getTagEnchantments());
        int enchantments = event.getLeft().getTagEnchantments().size();
        int cost = shieldUpgradeCost(current, upgrade, enchantments);
        event.setOutput(output);
        event.setCost(cost);
        event.setMaterialCost(1);
    }

    private static Map<String, MaterialDefinition> shieldMaterials() {
        Map<String, MaterialDefinition> materials = new LinkedHashMap<>();
        ModContent.materials().forEach((name, content) -> {
            if (content.items().containsKey("shield")) materials.put(name, content.definition());
        });
        return materials;
    }

    static int shieldUpgradeCost(MaterialDefinition current, MaterialDefinition upgrade, int enchantments) {
        return Math.max(5, (int) (5.0D * (upgrade.hardness() - current.hardness())
                + upgrade.magic() * enchantments));
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            BaseMetalsAdvancements.onCrafted(player, event.getCrafting());
        }
    }

    @SubscribeEvent
    public void onItemSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            BaseMetalsAdvancements.onSmelted(player, event.getSmelting());
        }
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            BaseMetalsAdvancements.onPlaced(player, event.getPlacedBlock());
        }
    }

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        BaseMetalsTrades.add(event);
    }
}
