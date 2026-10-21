package zone.moddev.mc.basemetals.network;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.DefaultSocketChannelConfig;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

class ConnectionReadInterestTest {
    private static final Path PATCH = Path.of("src/main/resources/coremods/connection_read_interest.js");

    @Test
    void protocolReadChangesStayOnTheEventLoop() throws Exception {
        ClassNode connection = connectionClass();
        assertEquals(1, autoReadCalls(method(connection, "sendPacket")));
        assertEquals(0, autoReadCalls(method(connection, "doSendPacket")));

        transform(connection);

        assertEquals(0, autoReadCalls(method(connection, "sendPacket")));
        assertEquals(1, autoReadCalls(method(connection, "doSendPacket")));
        assertTrue(Files.readString(Path.of("src/main/resources/META-INF/coremods.json"))
                .contains("coremods/connection_read_interest.js"));
    }

    @Test
    void applyingThePatchTwiceDoesNotDuplicateTheReadChange() throws Exception {
        ClassNode connection = connectionClass();
        transform(connection);
        transform(connection);

        assertEquals(0, autoReadCalls(method(connection, "sendPacket")));
        assertEquals(1, autoReadCalls(method(connection, "doSendPacket")));
    }

    @Test
    void anUnexpectedMinecraftMethodFailsClearly() throws Exception {
        ClassNode connection = connectionClass();
        method(connection, "doSendPacket").desc = "()V";
        assertThrows(ScriptException.class, () -> transform(connection));
    }

    @Test
    void serializingAutoReadChangesPreventsTheLostReadInterest() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        DelayedReadChannel channel = new DelayedReadChannel();

        try (ServerSocket listener = new ServerSocket(0)) {
            new Bootstrap().group(group).channelFactory(() -> channel)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socket) {}
                    }).connect(new InetSocketAddress("127.0.0.1", listener.getLocalPort())).sync();

            try (var peer = listener.accept()) {
                channel.eventLoop().submit(() -> {}).sync();
                assertTrue((channel.readInterest() & SelectionKey.OP_READ) != 0);

                // Pause the off-thread disable after the flag changes, but before Netty clears OP_READ.
                channel.delayClear = true;
                Thread sender = new Thread(() -> channel.config().setAutoRead(false));
                sender.start();
                assertTrue(channel.clearing.await(5, TimeUnit.SECONDS));
                channel.eventLoop().submit(() -> channel.config().setAutoRead(true)).sync();
                channel.releaseClear.countDown();
                sender.join(5000);
                channel.eventLoop().submit(() -> {}).sync();

                assertTrue(channel.config().isAutoRead());
                assertEquals(0, channel.readInterest() & SelectionKey.OP_READ,
                        "The original off-thread disable can erase the newer read request");

                channel.delayClear = false;
                channel.eventLoop().submit(() -> {
                    channel.config().setAutoRead(false);
                    channel.config().setAutoRead(true);
                }).sync();

                assertTrue((channel.readInterest() & SelectionKey.OP_READ) != 0,
                        "Changing both flags on the event loop must leave reads enabled");
            }
        } finally {
            channel.releaseClear.countDown();
            channel.close().sync();
            group.shutdownGracefully(0, 5, TimeUnit.SECONDS).sync();
        }
    }

    private static ClassNode connectionClass() throws Exception {
        try (var bytes = ConnectionReadInterestTest.class.getClassLoader()
                .getResourceAsStream("net/minecraft/network/Connection.class")) {
            assertNotNull(bytes);
            ClassNode node = new ClassNode();
            new ClassReader(bytes).accept(node, 0);
            return node;
        }
    }

    private static MethodNode method(ClassNode node, String name) {
        return node.methods.stream().filter(method -> method.name.equals(name)).findFirst().orElseThrow();
    }

    private static long autoReadCalls(MethodNode method) {
        long count = 0;
        for (var instruction : method.instructions) {
            if (instruction instanceof MethodInsnNode call && call.name.equals("setAutoRead")) {
                ++count;
            }
        }
        return count;
    }

    private static void transform(ClassNode node) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        assertNotNull(engine);
        String script = Files.readString(PATCH).replace(
                "Java.type('net.minecraftforge.coremod.api.ASMAPI')",
                "({mapMethod: function(name) { return ({m_129520_: 'sendPacket', "
                        + "m_243087_: 'doSendPacket', m_129498_: 'setProtocol'})[name] || name; }})");
        engine.eval(script);
        engine.put("connection", node);
        engine.eval("initializeCoreMod().basemetals_connection_read_interest.transformer(connection)");
    }

    private static final class DelayedReadChannel extends NioSocketChannel {
        private final CountDownLatch clearing = new CountDownLatch(1);
        private final CountDownLatch releaseClear = new CountDownLatch(1);
        private volatile boolean delayClear;
        private final DefaultSocketChannelConfig config = new DefaultSocketChannelConfig(this, javaChannel().socket()) {
            @Override
            protected void autoReadCleared() {
                if (delayClear) {
                    clearing.countDown();
                    try {
                        if (!releaseClear.await(5, TimeUnit.SECONDS)) {
                            throw new IllegalStateException("Timed out arranging the read-interest race");
                        }
                    } catch (InterruptedException interrupted) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException(interrupted);
                    }
                }
                clearReadPending();
            }
        };

        @Override
        public DefaultSocketChannelConfig config() {
            return config;
        }

        int readInterest() {
            return selectionKey().interestOps();
        }
    }
}
