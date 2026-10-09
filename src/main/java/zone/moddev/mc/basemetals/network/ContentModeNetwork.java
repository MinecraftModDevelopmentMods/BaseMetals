package zone.moddev.mc.basemetals.network;

import java.util.Collections;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fmllegacy.network.FMLHandshakeHandler;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentMode;

/** Checks the content mode before Forge lets a player join the world. */
public final class ContentModeNetwork {
    private static SimpleChannel channel;

    private ContentModeNetwork() {}

    public static void register() {
        channel = NetworkRegistry.newSimpleChannel(new ResourceLocation("basemetals", "content_mode"),
                () -> "1", "1"::equals, "1"::equals);

        channel.messageBuilder(ModeMessage.class, 0)
                .encoder((message, buffer) -> buffer.writeUtf(message.mode.serializedName()))
                .decoder(ModeMessage::read)
                .loginIndex(message -> message.loginIndex, (message, index) -> message.loginIndex = index)
                .buildLoginPacketList(local -> Collections.singletonList(
                        Pair.of("Base Metals content mode", new ModeMessage(BaseMetalsConfig.activeMode()))))
                .consumer(ContentModeNetwork::receive)
                .add();

        channel.messageBuilder(Acknowledgement.class, 1)
                .encoder((message, buffer) -> {})
                .decoder(buffer -> new Acknowledgement())
                .loginIndex(message -> message.loginIndex, (message, index) -> message.loginIndex = index)
                .consumer(FMLHandshakeHandler.indexFirst((handshake, message, context) ->
                        context.get().setPacketHandled(true)))
                .add();
    }

    public static boolean modesMatch(String server, String client) {
        return ContentMode.parse(server) == ContentMode.parse(client);
    }

    private static void receive(ModeMessage message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.setPacketHandled(true);
        if (context.getDirection() != NetworkDirection.LOGIN_TO_CLIENT) {
            throw new IllegalStateException("Content mode received outside the client login handshake");
        }

        ContentMode clientMode = BaseMetalsConfig.activeMode();
        if (message.mode != clientMode) {
            context.getNetworkManager().disconnect(new TranslatableComponent(
                    "config.basemetals.connection.mismatch",
                    new TranslatableComponent(message.mode.translationKey()),
                    new TranslatableComponent(clientMode.translationKey())));
            return;
        }

        channel.reply(new Acknowledgement(), context);
    }

    private static final class ModeMessage implements IntSupplier {
        private final ContentMode mode;
        private int loginIndex;

        private ModeMessage(ContentMode mode) { this.mode = mode; }

        private static ModeMessage read(FriendlyByteBuf buffer) {
            String value = buffer.readUtf(32);
            if (!ContentMode.isValid(value)) throw new IllegalArgumentException("Unknown server content mode " + value);
            return new ModeMessage(ContentMode.parse(value));
        }

        @Override public int getAsInt() { return loginIndex; }
    }

    private static final class Acknowledgement implements IntSupplier {
        private int loginIndex;
        @Override public int getAsInt() { return loginIndex; }
    }
}
