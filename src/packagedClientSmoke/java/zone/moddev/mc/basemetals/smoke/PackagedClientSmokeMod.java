package zone.moddev.mc.basemetals.smoke;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

/** Loads the existing integrated-world assertions from an isolated packaged probe mod. */
@Mod(PackagedClientSmokeMod.MOD_ID)
public final class PackagedClientSmokeMod {
    public static final String MOD_ID = "basemetals_client_smoke";

    public PackagedClientSmokeMod() {
        MinecraftForge.EVENT_BUS.register(IntegratedWorldSmoke.class);
        MinecraftForge.EVENT_BUS.register(RemoteLoginSmoke.class);
    }
}
