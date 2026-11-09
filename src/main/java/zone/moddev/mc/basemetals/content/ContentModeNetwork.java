package zone.moddev.mc.basemetals.content;

import java.util.Set;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.CustomPacketRegistrationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/** Advertise the active recipe mode in Forge's login channel registration. */
public final class ContentModeNetwork {
	private static final String HIGH = "BaseMetals|High";
	private static final String LOW = "BaseMetals|Low";

	private ContentModeNetwork() {}

	public static void register() {
		NetworkRegistry.INSTANCE.newEventDrivenChannel(ContentPolicy.active().mode() == ContentMode.LOW_FANTASY ? LOW : HIGH);
		MinecraftForge.EVENT_BUS.register(new ContentModeNetwork());
	}

	@SubscribeEvent
	public void checkMode(final CustomPacketRegistrationEvent<?> event) {
		final Set<?> channels = event.getRegistrations();
		// Forge includes FML|HS in its login registration, not ordinary channel updates.
		if (!"REGISTER".equals(event.getOperation()) || !channels.contains("FML|HS")) return;

		final boolean localLow = ContentPolicy.active().mode() == ContentMode.LOW_FANTASY;
		final boolean remoteLow = channels.contains(LOW);
		final boolean remoteHigh = channels.contains(HIGH);
		final boolean server = event.getSide().isServer();
		final ITextComponent reason;
		if (remoteLow == remoteHigh) {
			// A client with no mode channel may also lack the translation key.
			reason = server
					? new TextComponentString("Base Metals could not check the Fantasy Level. "
							+ "Use the same Base Metals build on the server and client.")
					: new TextComponentTranslation("config.basemetals.connection.protocol");
		} else if (remoteLow == localLow) {
			return;
		} else {
			final boolean serverLow = server ? localLow : remoteLow;
			reason = new TextComponentTranslation("config.basemetals.connection.mismatch",
					modeLabel(serverLow), modeLabel(!serverLow));
		}

		final NetworkManager manager = event.getManager();
		manager.channel().config().setAutoRead(false);
		if (server) {
			manager.sendPacket(new SPacketDisconnect(reason), future -> manager.closeChannel(reason));
		} else {
			manager.closeChannel(reason);
		}
	}

	private static ITextComponent modeLabel(final boolean low) {
		return new TextComponentTranslation(low ? ContentMode.LOW_FANTASY.translationKey() : ContentMode.HIGH_FANTASY.translationKey());
	}
}
