package zone.moddev.mc.basemetals.recipe;

import java.util.Collections;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import zone.moddev.mc.basemetals.BaseMetals;

/** Restores discovery for inventories and advancement progress saved before a recipe update. */
@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID)
public final class RecipeDiscovery {
    private static MinecraftServer pendingReload;
    private RecipeDiscovery() {}

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer() instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
        player.getServer().addScheduledTask(() -> discover(player));
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();
        server.getResourceManager().addReloadListener(resources -> pendingReload = server);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || pendingReload == null) return;
        MinecraftServer server = pendingReload;
        pendingReload = null;
        // Player advancements are reloaded after the resource listeners finish.
        server.getPlayerList().getPlayers().forEach(RecipeDiscovery::discover);
    }

    public static void discover(EntityPlayerMP player) {
        CriteriaTriggers.INVENTORY_CHANGED.trigger(player, player.inventory);
        for (IRecipe recipe : player.getServer().getRecipeManager().getRecipes()) {
            if (!BaseMetals.MOD_ID.equals(recipe.getId().getNamespace()) || recipe.isDynamic()
                    || player.getRecipeBook().isUnlocked(recipe)) continue;
            Advancement advancement = player.getServer().getAdvancementManager().getAdvancement(
                    new ResourceLocation(BaseMetals.MOD_ID, "recipes/" + recipe.getId().getPath()));
            if (advancement != null && player.getAdvancements().getProgress(advancement).isDone()) {
                player.unlockRecipes(Collections.singletonList(recipe));
            }
        }
    }
}
