package com.mcmoddev.basemetals.network;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.content.ContentMode;
import com.mcmoddev.basemetals.util.BMeConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/** Stable network contract that prevents client/server recipe-policy disagreement. */
public final class ContentModeNetwork {
	private static final SimpleNetworkWrapper CHANNEL =
			NetworkRegistry.INSTANCE.newSimpleChannel("bm_content_mode");
	private static boolean initialized;

	private ContentModeNetwork() {
	}

	public static synchronized void init() {
		if (!initialized) {
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

	public static final class ServerHandler
			implements IMessageHandler<ContentModeMessage, IMessage> {
		@Override
		public IMessage onMessage(final ContentModeMessage message, final MessageContext context) {
			final EntityPlayerMP player = context.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {
				final ContentMode serverMode = BMeConfig.getActiveContentMode();
				if (!message.hasValidMode() || !modesMatch(message.mode(), serverMode)) {
					final String clientName = message.hasValidMode()
							? message.mode().serializedName() : "invalid";
					final String reason = "Base Metals content mode mismatch: server uses '"
							+ serverMode.serializedName() + "' but client uses '" + clientName
							+ "'. Set General/contentMode to the same value and restart Minecraft.";
					BaseMetals.logger.warn("Rejecting {}: {}", player.getName(), reason);
					player.connection.disconnect(new TextComponentString(reason));
				}
			});
			return null;
		}
	}
}
