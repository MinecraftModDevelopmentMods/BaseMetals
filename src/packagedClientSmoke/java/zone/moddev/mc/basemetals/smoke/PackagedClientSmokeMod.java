package zone.moddev.mc.basemetals.smoke;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;

/** Loads the existing integrated-world assertions from an isolated packaged probe mod. */
@Mod(PackagedClientSmokeMod.MOD_ID)
public final class PackagedClientSmokeMod {
    public static final String MOD_ID = "basemetals_client_smoke";

    public PackagedClientSmokeMod() {
        IntegratedWorldSmoke.register();
        TickEvent.ClientTickEvent.Post.BUS.addListener(RemoteLoginSmoke::tick);
    }
}
