package zone.moddev.mc.basemetals.testmod;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Items;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import io.netty.buffer.Unpooled;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentPolicy;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.loot.ContentModeLootCondition;
import zone.moddev.mc.basemetals.recipe.ContentCraftingRecipe;
import zone.moddev.mc.basemetals.trade.BaseMetalsTrades;
import zone.moddev.mc.basemetals.BaseMetalsEvents;
import zone.moddev.mc.basemetals.recipe.PlateRepairRecipe;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.registries.ForgeRegistries;

/** Exercises the policy against loaded Forge recipes, trades and loot tables. */
final class ContentModeChecks {
    static int run(MinecraftServer server) throws Exception {
        ServerLevel world = server.getLevel(Level.OVERWORLD);
        ContentPolicy policy = ContentPolicy.active();
        int checks = 0;
        int recipes = 0;
        int restricted = 0;

        for (Recipe recipe : server.getRecipeManager().getRecipes()) {
            if (recipe.getSerializer() != ContentCraftingRecipe.SERIALIZER) continue;
            recipes++;
            boolean allowed = policy.allows(recipe.getResultItem().getItem().getRegistryName().toString());
            if (!allowed) restricted++;
            require(recipe.isSpecial() == !allowed, "recipe-book visibility " + recipe.getId());
            CraftingContainer grid = new CraftingContainer(new AbstractContainerMenu(null, 0) {
                @Override public boolean stillValid(net.minecraft.world.entity.player.Player player) { return true; }
            }, 3, 3);
            boolean available = true;
            int slot = 0;
            int width = recipe instanceof ShapedRecipe ? ((ShapedRecipe) recipe).getWidth() : 3;
            for (Object element : recipe.getIngredients()) {
                Ingredient ingredient = (Ingredient) element;
                int gridSlot = (slot / width) * 3 + slot % width;
                slot++;
                if (ingredient == Ingredient.EMPTY) continue;
                ItemStack[] choices = ingredient.getItems();
                if (choices.length == 0) { available = false; continue; }
                grid.setItem(gridSlot, choices[0].copy());
            }
            require(recipe.matches(grid, world) == (allowed && available), "crafting policy " + recipe.getId());
            if (!allowed) require(recipe.assemble(grid).isEmpty(), "restricted crafting result " + recipe.getId());

            FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
            recipe.getSerializer().toNetwork(packet, recipe);
            Recipe restored = recipe.getSerializer().fromNetwork(recipe.getId(), packet);
            require(restored.getId().equals(recipe.getId()) && restored.isSpecial() == recipe.isSpecial(),
                    "recipe packet identity " + recipe.getId());
            require((restored instanceof ShapedRecipe) == (recipe instanceof ShapedRecipe),
                    "recipe-book shape survives network sync " + recipe.getId());
            if (recipe instanceof ShapedRecipe) {
                require(((ShapedRecipe) restored).getWidth() == width
                        && ((ShapedRecipe) restored).getHeight() == ((ShapedRecipe) recipe).getHeight(),
                        "recipe-book dimensions " + recipe.getId());
            }
            packet.release();
            checks += 6;
        }
        require(recipes > 900, "full crafting catalogue tested");
        require(restricted > 0 == (BaseMetalsConfig.activeMode() == zone.moddev.mc.basemetals.config.ContentMode.LOW_FANTASY),
                "expected restricted recipe count");

        Recipe mercury = server.getRecipeManager().byKey(new ResourceLocation("basemetals", "mercury_smallpowder_smelting")).orElse(null);
        SimpleContainer furnace = new SimpleContainer(3);
        furnace.setItem(0, new ItemStack(ModContent.item("mercury_smallpowder").get()));
        require(mercury.matches(furnace, world) == policy.allows("basemetals:mercury_nugget"), "mercury furnace policy");

        for (Recipe<?> recipe : server.getRecipeManager().getRecipes()) {
            if (!(recipe instanceof PlateRepairRecipe)) continue;
            Recipe<CraftingContainer> repair = (Recipe<CraftingContainer>) recipe;
            ItemStack[] plates = repair.getIngredients().get(1).getItems();
            if (plates.length == 0) continue;

            CraftingContainer grid = new CraftingContainer(new AbstractContainerMenu(null, 0) {
                @Override public boolean stillValid(net.minecraft.world.entity.player.Player player) { return true; }
            }, 3, 3);
            ItemStack damaged = repair.getResultItem().copy();
            damaged.setDamageValue(7);
            grid.setItem(0, damaged);
            grid.setItem(8, plates[0].copy());
            boolean allowed = policy.allows(damaged.getItem().getRegistryName().toString());
            require(repair.matches(grid, world) == allowed, "plate repair policy " + repair.getId());
            require(repair.assemble(grid).isEmpty() == !allowed, "plate repair output " + repair.getId());
            require(damaged.getDamageValue() == 7, "repair preserves input " + repair.getId());
            checks += 3;
        }

        for (String material : new String[] {"tin", "steel"}) {
            AnvilUpdateEvent upgrade = new AnvilUpdateEvent(new ItemStack(ModContent.item("antimony_shield").get()),
                    new ItemStack(ModContent.item(material + "_plate").get()), "", 0, null);
            new BaseMetalsEvents().onShieldUpgrade(upgrade);
            require(upgrade.getOutput().isEmpty() == !policy.allows("basemetals:" + material + "_shield"),
                    "shield upgrade policy " + material);
            checks++;
        }

        Method sales = BaseMetalsTrades.class.getDeclaredMethod("selling", Item.class, int.class, int.class, int.class);
        sales.setAccessible(true);
        for (zone.moddev.mc.basemetals.content.RegistryHandle<Item> handle : ModContent.itemsById().values()) {
            Item item = handle.get();
            String id = item.getRegistryName().toString();
            VillagerTrades.ItemListing factory = (VillagerTrades.ItemListing) sales.invoke(null, item, 1, 1, 1);
            MerchantOffer offer = factory.getOffer(null, new Random(0));
            require((offer != null) == (BaseMetalsConfig.VILLAGER_TRADES.get() && policy.allows(id)), "trade policy " + id);
            net.minecraft.world.level.storage.loot.predicates.LootItemCondition condition = new ContentModeLootCondition(id);
            require(condition.test(null) == policy.allows(id), "loot condition " + id);
            checks += 2;
        }

        for (String name : new String[] {"abandoned_mineshaft", "desert_pyramid", "end_city_treasure", "jungle_temple",
                "nether_bridge", "simple_dungeon", "spawn_bonus_chest", "stronghold_corridor", "stronghold_crossing", "village_blacksmith"}) {
            LootTable table = server.getLootTables().get(
                    new ResourceLocation("basemetals", "chests/inject/" + name));
            for (int seed = 0; seed < 100; seed++) {
                LootContext context = new LootContext.Builder(world).withOptionalRandomSeed(seed)
                        .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.EMPTY);
                for (ItemStack stack : table.getRandomItems(context)) {
                    require(policy.allows(stack.getItem().getRegistryName().toString()), "chest policy " + name + " " + stack);
                    checks++;
                }
            }
        }
        require(policy.allows(Items.BOW.getRegistryName().toString()), "vanilla bows remain unchanged");
        return checks + verifyMaterialDiscovery(server, world);
    }

    private static int verifyMaterialDiscovery(MinecraftServer server, ServerLevel world) {
        Map<String, Item> materials = new LinkedHashMap<>();
        for (String metal : new String[] {"adamantine", "antimony", "aquarium", "bismuth", "brass", "bronze",
                "coldiron", "copper", "cupronickel", "electrum", "invar", "lead", "mercury", "mithril",
                "nickel", "obsidian", "pewter", "platinum", "silver", "starsteel", "steel", "tin", "zinc"}) {
            materials.put(metal, ModContent.item(metal + "_ingot").get());
        }
        materials.put("iron", Items.IRON_INGOT);
        materials.put("gold", Items.GOLD_INGOT);
        materials.put("diamond", Items.DIAMOND);
        materials.put("emerald", Items.EMERALD);
        materials.put("quartz", Items.QUARTZ);
        materials.put("coal", Items.COAL);
        materials.put("charcoal", Items.CHARCOAL);
        materials.put("redstone", Items.REDSTONE);
        materials.put("stone", ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "stone")));
        materials.put("wood", ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "oak_log")));

        FakePlayer player = new FakePlayer(world, new GameProfile(UUID.randomUUID(), "MaterialDiscovery"));
        int recipes = 0;
        for (Recipe recipe : server.getRecipeManager().getRecipes()) {
            if (recipe.getSerializer() != ContentCraftingRecipe.SERIALIZER) continue;

            String name = recipe.getId().getPath();
            String material = name.matches("activator_rail|detector_rail|flint_and_steel|human_detector|minecart|piston|rail|tripwire_hook")
                    ? "steel" : name.substring(0, name.indexOf('_'));
            require(materials.containsKey(material), "discovery material for " + name);
            Advancement advancement = server.getAdvancements().getAdvancement(
                    new ResourceLocation("basemetals", "recipes/" + name));
            require(advancement != null && advancement.getCriteria().containsKey("has_material"),
                    "material discovery advancement " + name);
            InventoryChangeTrigger.TriggerInstance condition = (InventoryChangeTrigger.TriggerInstance)
                    advancement.getCriteria().get("has_material").getTrigger();

            player.getInventory().clearContent();
            player.getInventory().setItem(0, new ItemStack(materials.get(material)));
            require(condition.matches(player.getInventory(), player.getInventory().getItem(0), 1, 0, 35), "base material does not discover " + name);
            player.getInventory().setItem(0, new ItemStack(Items.STICK));
            require(!condition.matches(player.getInventory(), player.getInventory().getItem(0), 1, 0, 35), "unrelated item discovers " + name);
            recipes++;
        }
        require(recipes == 1024, "full material discovery catalogue tested");
        return recipes * 2;
    }

    private static void require(boolean result, String message) {
        if (!result) throw new IllegalStateException("Content-mode check failed: " + message);
    }
}
