package com.mcmoddev.basemetals.network;

import java.util.Set;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.content.ContentMode;
import com.mcmoddev.basemetals.util.BMeConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.CustomPacketRegistrationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/** Rejects connections when the client and server use different content modes. */
public final class ContentModeNetwork {
	private static final String HIGH_CHANNEL = "bm_mode_high";
	private static final String LOW_CHANNEL = "bm_mode_low";
	private static final String FORGE_HANDSHAKE = "FML|HS";
	private static final int FORGE_MOD_LIST = 2;
	private static final String REJECTION_HANDLER = "basemetals:content_mode";
	private static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("bm_content_mode");
	private static boolean initialized;

	private ContentModeNetwork() {
	}

	public static synchronized void init() {
		if (!initialized) {
			NetworkRegistry.INSTANCE.newEventDrivenChannel(
					BMeConfig.getActiveContentMode() == ContentMode.LOW_FANTASY ? LOW_CHANNEL : HIGH_CHANNEL);
			MinecraftForge.EVENT_BUS.register(new ContentModeNetwork());
			CHANNEL.registerMessage(ServerHandler.class, ContentModeMessage.class, 0, Side.SERVER);
			initialized = true;
		}
	}

	public static void sendClientMode() {
		CHANNEL.sendToServer(new ContentModeMessage(BMeConfig.getActiveContentMode()));
	}

	public static boolean modesMatch(final ContentMode client, final ContentMode server) {
		return client == server;
	}

	@SubscribeEvent
	public void checkLoginChannels(final CustomPacketRegistrationEvent<?> event) {
		final Set<String> channels = event.getRegistrations();
		if (!event.getSide().isServer() || !"REGISTER".equals(event.getOperation())
				|| !channels.contains(FORGE_HANDSHAKE)) {
			return;
		}

		final ContentMode server = BMeConfig.getActiveContentMode();
		final boolean high = channels.contains(HIGH_CHANNEL);
		final boolean low = channels.contains(LOW_CHANNEL);
		final ContentMode client = low ? ContentMode.LOW_FANTASY : ContentMode.HIGH_FANTASY;
		final String reason;

		if (high == low) {
			reason = "Base Metals could not check the client's Fantasy Level. Use the same Base Metals build "
					+ "on the server and client.";
		} else if (!modesMatch(client, server)) {
			reason = mismatchMessage(client.serializedName(), server);
		} else {
			return;
		}

		final NetworkManager manager = event.getManager();
		if (manager.channel().pipeline().get(REJECTION_HANDLER) == null) {
			manager.channel().pipeline().addBefore("fml:packet_handler", REJECTION_HANDLER,
					new RejectModList(manager, reason));
		}
	}

	private static String mismatchMessage(final String client, final ContentMode server) {
		return "Base Metals content mode mismatch: server uses '" + server.serializedName()
				+ "' but client uses '" + client
				+ "'. Set General/contentMode to the same value and restart Minecraft.";
	}

	private static final class RejectModList extends ChannelInboundHandlerAdapter {
		private final NetworkManager manager;
		private final TextComponentString reason;

		RejectModList(final NetworkManager manager, final String reason) {
			this.manager = manager;
			this.reason = new TextComponentString(reason);
		}

		@Override
		public void channelRead(final ChannelHandlerContext context, final Object message) {
			if (message instanceof CPacketCustomPayload) {
				final CPacketCustomPayload packet = (CPacketCustomPayload) message;
				final PacketBuffer data = packet.getBufferData();

				// Closing at registration interrupts Forge's login writes. Wait for the client's mod list.
				if (FORGE_HANDSHAKE.equals(packet.getChannelName()) && data.isReadable()
						&& data.getUnsignedByte(data.readerIndex()) == FORGE_MOD_LIST) {
					data.release();
					context.channel().config().setAutoRead(false);
					BaseMetals.logger.warn(reason.getUnformattedText());
					manager.sendPacket(new SPacketDisconnect(reason), future -> manager.closeChannel(reason));
					return;
				}
			}

			context.fireChannelRead(message);
		}
	}

	public static final class ServerHandler implements IMessageHandler<ContentModeMessage, IMessage> {
		@Override
		public IMessage onMessage(final ContentModeMessage message, final MessageContext context) {
			final EntityPlayerMP player = context.getServerHandler().player;

			player.getServerWorld().addScheduledTask(() -> {
				final ContentMode serverMode = BMeConfig.getActiveContentMode();

				if (!message.hasValidMode() || !modesMatch(message.mode(), serverMode)) {
					final String clientName = message.hasValidMode()
							? message.mode().serializedName()
							: "invalid";
					final String reason = mismatchMessage(clientName, serverMode);

					BaseMetals.logger.warn("Rejecting %s: %s", player.getName(), reason);
					player.connection.disconnect(new TextComponentString(reason));
				}
			});

			return null;
		}
	}
}
