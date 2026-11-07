package zone.moddev.mc.basemetals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

class SerializedSetupTest {
    private static final String MOD = "zone/moddev/mc/basemetals/";
    private static final String SETUP_EVENT = "net/minecraftforge/fml/event/lifecycle/FMLCommonSetupEvent";
    private static final Set<String> REGISTRATIONS = Set.of(
            MOD + "migration/LegacyWorldDataHook", MOD + "network/ContentModeNetwork");

    @Test
    void registersSharedForgeStateOnlyThroughCommonSetupQueue() throws Exception {
        ClassNode entry = new ClassNode();
        try (InputStream input = getClass().getResourceAsStream("/" + MOD + "BaseMetals.class")) {
            assertNotNull(input);
            new ClassReader(input).accept(entry, 0);
        }
        Set<String> queued = new HashSet<>();
        int subscribed = 0;
        for (MethodNode method : entry.methods) {
            for (AbstractInsnNode instruction : method.instructions.toArray()) {
                if (instruction instanceof MethodInsnNode call) {
                    assertFalse(REGISTRATIONS.contains(call.owner) && call.name.equals("register"),
                            "Direct registration can race Forge during " + method.name);
                }
                if (!(instruction instanceof InvokeDynamicInsnNode callback)) continue;
                AbstractInsnNode next = instruction.getNext();
                while (next != null && next.getOpcode() < 0) next = next.getNext();
                for (Object argument : callback.bsmArgs) {
                    if (!(argument instanceof Handle target)) continue;
                    if (target.getOwner().equals(entry.name) && target.getName().equals("commonSetup")) {
                        assertEquals("<init>", method.name);
                        assertTrue(next instanceof MethodInsnNode listener
                                && listener.owner.equals("net/minecraftforge/eventbus/api/IEventBus")
                                && listener.name.equals("addListener"));
                        subscribed++;
                    }
                    if (REGISTRATIONS.contains(target.getOwner()) && target.getName().equals("register")) {
                        assertEquals("commonSetup", method.name);
                        assertEquals("(L" + SETUP_EVENT + ";)V", method.desc);
                        assertTrue(next instanceof MethodInsnNode enqueue
                                && enqueue.owner.equals(SETUP_EVENT)
                                && enqueue.name.equals("enqueueWork")
                                && enqueue.desc.startsWith("(Ljava/lang/Runnable;)"));
                        assertTrue(queued.add(target.getOwner()), "Queue each registration once");
                    }
                }
            }
        }
        assertEquals(1, subscribed);
        assertEquals(REGISTRATIONS, queued);
    }
}
