package zone.moddev.mc.basemetals.network;

import java.util.function.Consumer;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.network.GatherLoginConfigurationTasksEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.network.config.ConfigurationTaskContext;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentMode;

/** Checks the content mode during configuration, before a player can enter the world. */
public final class ContentModeNetwork {
    private static final ConfigurationTask.Type TASK = new ConfigurationTask.Type("basemetals:content_mode");
    private static SimpleChannel channel;

    private ContentModeNetwork() {}

    public static void register() {
        channel = ChannelBuilder.named("basemetals:content_mode").networkProtocolVersion(1).simpleChannel();

        channel.messageBuilder(ModeMessage.class, 0, NetworkDirection.CONFIGURATION_TO_CLIENT)
                .encoder((message, buffer) -> buffer.writeUtf(message.mode.serializedName()))
                .decoder(buffer -> new ModeMessage(readMode(buffer.readUtf(32))))
                .consumerMainThread((message, context) -> {
                    ContentMode clientMode = BaseMetalsConfig.activeMode();
                    if (message.mode != clientMode) {
                        context.getConnection().disconnect(Component.translatable(
                                "config.basemetals.connection.mismatch",
                                Component.translatable(message.mode.translationKey()),
                                Component.translatable(clientMode.translationKey())));
                    } else {
                        channel.send(new Acknowledgement(), context.getConnection());
                    }
                    context.setPacketHandled(true);
                }).add();

        channel.messageBuilder(Acknowledgement.class, 1, NetworkDirection.CONFIGURATION_TO_SERVER)
                .encoder((message, buffer) -> {})
                .decoder(buffer -> new Acknowledgement())
                .consumerMainThread((message, context) -> {
                    if (!(context.getConnection().getPacketListener()
                            instanceof ServerConfigurationPacketListenerImpl listener)) {
                        throw new IllegalStateException("Base Metals acknowledgement outside configuration");
                    }
                    listener.finishCurrentTask(TASK);
                    context.setPacketHandled(true);
                }).add();

        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, ContentModeNetwork::gatherTasks);
    }

    public static boolean modesMatch(String server, String client) {
        return ContentMode.parse(server) == ContentMode.parse(client);
    }

    private static ContentMode readMode(String value) {
        if (!ContentMode.isValid(value)) throw new IllegalArgumentException("Unknown server content mode " + value);
        return ContentMode.parse(value);
    }

    private static void gatherTasks(GatherLoginConfigurationTasksEvent event) {
        event.addTask(new ModeTask());
    }

    private static final class ModeTask implements ConfigurationTask {
        @Override
        public void start(ConfigurationTaskContext context) {
            channel.send(new ModeMessage(BaseMetalsConfig.activeMode()), context.getConnection());
            // Only the client's acknowledgement completes this task.
        }

        @Override
        public void start(Consumer<Packet<?>> sender) {
            throw new IllegalStateException("Base Metals requires Forge's configuration task context");
        }

        @Override
        public Type type() {
            return TASK;
        }
    }

    private record ModeMessage(ContentMode mode) {}
    private record Acknowledgement() {}
}
