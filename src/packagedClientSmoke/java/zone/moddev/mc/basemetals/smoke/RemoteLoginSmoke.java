package zone.moddev.mc.basemetals.smoke;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentMode;

/** Checks login with a separate client process, including deliberate mode mismatches. */
public final class RemoteLoginSmoke {
    private static boolean connecting;
    private static boolean completed;
    private static int ticks;
    private static int joinedTicks;

    private RemoteLoginSmoke() {}

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || completed || !Boolean.getBoolean("basemetals.remoteLoginSmoke")) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (++ticks > 3600) throw new IllegalStateException("MODE_LOGIN_SMOKE FAIL: timed out at "
                + (minecraft.screen == null ? "gameplay" : minecraft.screen.getClass().getName()));
        boolean reject = Boolean.getBoolean("basemetals.expectModeReject");
        if (!connecting && minecraft.screen instanceof TitleScreen) {
            connecting = true;
            String address = System.getProperty("basemetals.remoteAddress");
            ConnectScreen.startConnecting(minecraft.screen, minecraft, ServerAddress.parseString(address),
                    new net.minecraft.client.multiplayer.ServerData("Base Metals smoke", address, false));
        }
        if (minecraft.player != null && minecraft.level != null) {
            if (reject) throw new IllegalStateException("MODE_LOGIN_SMOKE FAIL: mismatched modes entered gameplay");
            if (++joinedTicks < 40) return;
            var recipe = minecraft.level.getRecipeManager().byKey(new ResourceLocation("basemetals:adamantine_bow"))
                    .orElseThrow();
            if (recipe.isSpecial() != (BaseMetalsConfig.activeMode() == ContentMode.LOW_FANTASY)) {
                throw new IllegalStateException("MODE_LOGIN_SMOKE FAIL: recipe policy was not synchronized");
            }
            finish(minecraft, "connected");
        } else if (minecraft.screen instanceof DisconnectedScreen) {
            StringBuilder reason = new StringBuilder();
            try {
                for (var field : DisconnectedScreen.class.getDeclaredFields()) {
                    if (!Component.class.isAssignableFrom(field.getType())) continue;
                    field.setAccessible(true);
                    reason.append(((Component) field.get(minecraft.screen)).getString());
                }
            } catch (ReflectiveOperationException exception) {
                throw new IllegalStateException("Could not inspect the disconnect screen", exception);
            }
            if (!reject || !reason.toString().contains("Base Metals")
                    || !reason.toString().contains("High Fantasy") || !reason.toString().contains("Low Fantasy")) {
                throw new IllegalStateException("MODE_LOGIN_SMOKE FAIL: unexpected disconnect: " + reason);
            }
            finish(minecraft, "rejected");
        }
    }

    private static void finish(Minecraft minecraft, String result) {
        completed = true;
        BaseMetals.LOGGER.info("BASEMETALS_MODE_LOGIN_CLIENT PASS result={} mode={}",
                result, BaseMetalsConfig.activeMode().serializedName());
        minecraft.stop();
    }
}
