package com.mcmoddev.basemetals.network;

import com.mcmoddev.basemetals.content.ContentMode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentModeNetworkTest {
	@Test
	void everyStableModeRoundTripsOverTheHandshake() {
		for (final ContentMode mode : ContentMode.values()) {
			final ByteBuf buffer = Unpooled.buffer();
			new ContentModeMessage(mode).toBytes(buffer);
			final ContentModeMessage decoded = new ContentModeMessage();
			decoded.fromBytes(buffer);
			assertTrue(decoded.hasValidMode());
			assertEquals(mode, decoded.mode());
			assertTrue(ContentModeNetwork.modesMatch(mode, decoded.mode()));
		}
	}

	@Test
	void genuineModeDifferencesAreRejectedByTheComparisonContract() {
		assertFalse(ContentModeNetwork.modesMatch(
				ContentMode.HIGH_FANTASY, ContentMode.LOW_FANTASY));
		assertFalse(ContentModeNetwork.modesMatch(
				ContentMode.LOW_FANTASY, ContentMode.REALISM));
	}
}
