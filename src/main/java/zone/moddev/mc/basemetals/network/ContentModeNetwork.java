package zone.moddev.mc.basemetals.network;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.function.Supplier;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.network.FMLHandshakeMessages;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentMode;

/** Makes sure client and server use the same content mode before the player joins. */
public final class ContentModeNetwork {
    private ContentModeNetwork() {}

    public static void register() {
        SimpleChannel channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("basemetals", "content_mode"), () -> "1", "1"::equals, "1"::equals);
        channel.messageBuilder(ModeMessage.class, 0)
                .encoder((message, buffer) -> buffer.writeString(message.mode.serializedName()))
                .decoder(ModeMessage::read)
                .loginIndex(message -> message.loginIndex, (message, index) -> message.loginIndex = index)
                .buildLoginPacketList(local -> Collections.singletonList(
                        Pair.of("Base Metals content mode", new ModeMessage(BaseMetalsConfig.activeMode()))))
                .consumer(ContentModeNetwork::receive)
                .add();

        Forge25Login.initialize();
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
            Forge25Login.manager(context).closeChannel(new TextComponentTranslation(
                    "config.basemetals.connection.mismatch",
                    new TextComponentTranslation(message.mode.translationKey()),
                    new TextComponentTranslation(clientMode.translationKey())));
            return;
        }

        Forge25Login.acknowledge(context);
    }

    private static final class ModeMessage {
        private final ContentMode mode;
        private int loginIndex;

        private ModeMessage(ContentMode mode) { this.mode = mode; }

        private static ModeMessage read(PacketBuffer buffer) {
            String value = buffer.readString(32);
            if (!ContentMode.isValid(value)) throw new IllegalArgumentException("Unknown server content mode " + value);
            return new ModeMessage(ContentMode.parse(value));
        }
    }

    private static final class Forge25Login {
        private static final Method NETWORK_MANAGER;
        private static final SimpleChannel HANDSHAKE;

        static {
            // Forge 25 doesn't expose these login helpers. Cache them here so we can
            // send its normal acknowledgement after checking the content mode.
            try {
                // Forge's handshake listener has to be ready before anyone connects.
                Class.forName("net.minecraftforge.fml.network.FMLHandshakeHandler");
                NETWORK_MANAGER = NetworkEvent.Context.class.getDeclaredMethod("getNetworkManager");
                NETWORK_MANAGER.setAccessible(true);
                Field field = FMLNetworkConstants.class.getDeclaredField("handshakeChannel");
                field.setAccessible(true);
                HANDSHAKE = (SimpleChannel) field.get(null);
            } catch (ReflectiveOperationException failure) {
                throw new ExceptionInInitializerError(failure);
            }
        }

        private static void initialize() {
            // The static initializer prepares Forge's handshake before the server starts.
        }

        private static NetworkManager manager(NetworkEvent.Context context) {
            try {
                return (NetworkManager) NETWORK_MANAGER.invoke(context);
            } catch (ReflectiveOperationException failure) {
                throw new IllegalStateException("Cannot access Forge's login connection", failure);
            }
        }

        private static void acknowledge(NetworkEvent.Context context) {
            HANDSHAKE.reply(new FMLHandshakeMessages.C2SAcknowledge(), context);
        }
    }
}
