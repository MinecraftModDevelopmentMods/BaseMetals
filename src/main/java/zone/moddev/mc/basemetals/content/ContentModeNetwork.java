package zone.moddev.mc.basemetals.content;

import java.util.Set;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
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
	private static final String HANDSHAKE = "FML|HS";
	private static final String REJECTION_HANDLER = "basemetals:fantasy_level";
	private static final int FORGE_MOD_LIST = 2;

	private ContentModeNetwork() {}

	public static void register() {
		NetworkRegistry.INSTANCE.newEventDrivenChannel(ContentPolicy.active().mode() == ContentMode.LOW_FANTASY ? LOW : HIGH);
		MinecraftForge.EVENT_BUS.register(new ContentModeNetwork());
	}

	@SubscribeEvent
	public void checkMode(final CustomPacketRegistrationEvent<?> event) {
		final Set<?> channels = event.getRegistrations();
		// Forge includes FML|HS in its login registration, not ordinary channel updates.
		if (!"REGISTER".equals(event.getOperation()) || !channels.contains(HANDSHAKE)) {
			return;
		}

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
		} else if (!server || remoteLow == localLow) {
			return;
		} else {
			reason = new TextComponentTranslation("config.basemetals.connection.mismatch",
					modeLabel(localLow), modeLabel(remoteLow));
		}

		final NetworkManager manager = event.getManager();
		if (server) {
			if (manager.channel().pipeline().get(REJECTION_HANDLER) == null) {
				manager.channel().pipeline().addBefore("fml:packet_handler", REJECTION_HANDLER,
						new RejectModList(manager, reason));
			}
		} else {
			// An older server cannot reject a missing Fantasy Level itself.
			manager.channel().eventLoop().execute(() -> {
				if (manager.isChannelOpen()) {
					manager.closeChannel(reason);
				}
			});
		}
	}

	private static ITextComponent modeLabel(final boolean low) {
		return new TextComponentTranslation(low ? ContentMode.LOW_FANTASY.translationKey() : ContentMode.HIGH_FANTASY.translationKey());
	}

	private static final class RejectModList extends ChannelInboundHandlerAdapter {
		private final NetworkManager manager;
		private final ITextComponent reason;

		RejectModList(final NetworkManager manager, final ITextComponent reason) {
			this.manager = manager;
			this.reason = reason;
		}

		@Override
		public void channelRead(final ChannelHandlerContext context, final Object message) {
			if (message instanceof CPacketCustomPayload) {
				final CPacketCustomPayload packet = (CPacketCustomPayload) message;
				final PacketBuffer data = packet.getBufferData();

				// Forge 1.11 sends its mod list after registration and ClientHello.
				// Reject it here so the client finishes writing before we close the connection.
				if (HANDSHAKE.equals(packet.getChannelName()) && data.isReadable()
						&& data.getUnsignedByte(data.readerIndex()) == FORGE_MOD_LIST) {
					data.release();
					context.channel().config().setAutoRead(false);
					manager.sendPacket(new SPacketDisconnect(reason), future -> manager.closeChannel(reason));
					return;
				}
			}

			context.fireChannelRead(message);
		}
	}
}
