package com.mcmoddev.basemetals.network;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

/** Sends the effective mode after Forge establishes the modded connection. */
public final class ClientContentModeConnectionHandler {
	@SubscribeEvent
	public void connected(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
		ContentModeNetwork.sendClientMode();
	}
}
