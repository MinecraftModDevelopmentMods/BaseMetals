package zone.moddev.mc.basemetals.network;

import java.util.function.Consumer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ConfigurationTask;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentMode;

/** Checks content modes during configuration, before a player joins the world. */
@EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ContentModeNetwork {
    private static final ConfigurationTask.Type TASK_TYPE =
            new ConfigurationTask.Type(BaseMetals.MOD_ID + ":content_mode");

    private ContentModeNetwork() {}

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.configurationToClient(ModeMessage.TYPE, ModeMessage.CODEC, ContentModeNetwork::receive);
        registrar.configurationToServer(Acknowledgement.TYPE, Acknowledgement.CODEC,
                (message, context) -> context.finishCurrentTask(TASK_TYPE));
    }

    @SubscribeEvent
    public static void registerTasks(RegisterConfigurationTasksEvent event) {
        event.register(new ModeCheckTask());
    }

    public static boolean modesMatch(String server, String client) {
        return ContentMode.parse(server) == ContentMode.parse(client);
    }

    private static void receive(ModeMessage message, IPayloadContext context) {
        ContentMode clientMode = BaseMetalsConfig.activeMode();
        if (message.mode() != clientMode) {
            context.disconnect(Component.translatable("config.basemetals.connection.mismatch",
                    Component.translatable(message.mode().translationKey()),
                    Component.translatable(clientMode.translationKey())));
            return;
        }
        context.reply(new Acknowledgement());
    }

    private static final class ModeCheckTask implements ICustomConfigurationTask {
        @Override
        public void run(Consumer<CustomPacketPayload> sender) {
            sender.accept(new ModeMessage(BaseMetalsConfig.activeMode()));
        }

        @Override
        public ConfigurationTask.Type type() {
            return TASK_TYPE;
        }
    }

    private record ModeMessage(ContentMode mode) implements CustomPacketPayload {
        private static final Type<ModeMessage> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, "content_mode"));
        private static final StreamCodec<FriendlyByteBuf, ModeMessage> CODEC = StreamCodec.of(
                (buffer, message) -> buffer.writeUtf(message.mode().serializedName(), 32),
                buffer -> {
                    String value = buffer.readUtf(32);
                    if (!ContentMode.isValid(value)) {
                        throw new IllegalArgumentException("Unknown server content mode " + value);
                    }
                    return new ModeMessage(ContentMode.parse(value));
                });

        @Override
        public Type<ModeMessage> type() {
            return TYPE;
        }
    }

    private record Acknowledgement() implements CustomPacketPayload {
        private static final Type<Acknowledgement> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, "content_mode_ack"));
        private static final StreamCodec<FriendlyByteBuf, Acknowledgement> CODEC =
                StreamCodec.unit(new Acknowledgement());

        @Override
        public Type<Acknowledgement> type() {
            return TYPE;
        }
    }
}
