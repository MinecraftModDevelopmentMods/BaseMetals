package com.mcmoddev.basemetals.network;

import com.mcmoddev.basemetals.content.ContentMode;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/** Client declaration of its effective, startup-latched content mode. */
public final class ContentModeMessage implements IMessage {
	private String serializedMode;

	public ContentModeMessage() {
	}

	ContentModeMessage(final ContentMode mode) {
		serializedMode = mode.serializedName();
	}

	ContentMode mode() {
		return ContentMode.fromSerializedName(serializedMode);
	}

	boolean hasValidMode() {
		return ContentMode.isValidSerializedName(serializedMode);
	}

	@Override
	public void fromBytes(final ByteBuf buffer) {
		serializedMode = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void toBytes(final ByteBuf buffer) {
		ByteBufUtils.writeUTF8String(buffer, serializedMode);
	}
}
