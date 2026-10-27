package zone.moddev.mc.basemetals.smoke;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.resources.Identifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentMode;

/** Checks login with a separate client process, including deliberate mode mismatches. */
public final class RemoteLoginSmoke {
    private static boolean connecting;
    private static boolean completed;
    private static int ticks;
    private static int titleTicks;
    private static int joinedTicks;
    private static int connections;

    private RemoteLoginSmoke() {}

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent.Post event) {
        if (completed || !Boolean.getBoolean("basemetals.remoteLoginSmoke")) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (++ticks > 3600) throw new IllegalStateException("MODE_LOGIN_SMOKE FAIL: timed out at "
                + (minecraft.screen == null ? "gameplay" : minecraft.screen.getClass().getName()));
        boolean reject = Boolean.getBoolean("basemetals.expectModeReject");
        if (!connecting && minecraft.screen instanceof TitleScreen) {
            // Wait until the loading overlay is gone, as a player would have to.
            if (minecraft.getOverlay() != null || ++titleTicks < 20) return;

            connecting = true;
            String address = System.getProperty("basemetals.remoteAddress");
            ConnectScreen.startConnecting(minecraft.screen, minecraft, ServerAddress.parseString(address),
                    new net.minecraft.client.multiplayer.ServerData("Base Metals smoke", address,
                            net.minecraft.client.multiplayer.ServerData.Type.OTHER), false, null);
        }
        if (minecraft.player != null && minecraft.level != null) {
            if (reject) throw new IllegalStateException("MODE_LOGIN_SMOKE FAIL: mismatched modes entered gameplay");
            if (++joinedTicks < 40) return;
            if (minecraft.player.connection == null) {
                throw new IllegalStateException("MODE_LOGIN_SMOKE FAIL: gameplay has no live connection");
            }
            connections++;
            if (connections < Integer.getInteger("basemetals.loginSmokeConnections", 1)) {
                BaseMetals.LOGGER.info("BASEMETALS_MODE_RECONNECT connection={}", connections);
                minecraft.level.disconnect(net.minecraft.network.chat.Component.literal("Reconnect smoke"));
                minecraft.disconnect(new TitleScreen(), false);
                connecting = false;
                titleTicks = 0;
                joinedTicks = 0;
            } else {
                finish(minecraft, "connected");
            }
        } else if (minecraft.screen instanceof DisconnectedScreen disconnected) {
            String reason = disconnected.getNarrationMessage().getString();

            if (!reject || !reason.contains("Base Metals")
                    || !reason.contains("High Fantasy") || !reason.contains("Low Fantasy")) {
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
