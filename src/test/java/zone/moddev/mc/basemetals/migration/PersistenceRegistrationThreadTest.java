package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

class PersistenceRegistrationThreadTest {
    private static final String ENTRY_POINT = "zone/moddev/mc/basemetals/BaseMetals";
    private static final String HOOK = "zone/moddev/mc/basemetals/migration/LegacyWorldDataHook";
    private static final String SETUP_EVENT = "net/minecraftforge/fml/event/lifecycle/FMLCommonSetupEvent";
    private static final String SETUP_DESCRIPTOR = "(L" + SETUP_EVENT + ";)V";

    @Test
    void neverRegistersTheSharedWorldHookDirectly() throws Exception {
        for (MethodNode method : entryPoint().methods) {
            for (AbstractInsnNode instruction : method.instructions.toArray()) {
                if (instruction instanceof MethodInsnNode) {
                    MethodInsnNode call = (MethodInsnNode) instruction;
                    assertFalse(call.owner.equals(HOOK) && call.name.equals("register"),
                            "Do not mutate Forge's shared hook ArrayList during parallel mod loading: "
                                    + method.name);
                }
            }
        }
    }

    @Test
    void subscribesQueuedCommonSetupOnceOnTheModEventBus() throws Exception {
        int listeners = 0;
        for (MethodNode method : entryPoint().methods) {
            for (AbstractInsnNode instruction : method.instructions.toArray()) {
                if (!(instruction instanceof MethodInsnNode)) continue;
                MethodInsnNode call = (MethodInsnNode) instruction;
                if (call.owner.equals("net/minecraftforge/eventbus/api/IEventBus")
                        && call.name.equals("addListener")
                        && references(previousCode(call), ENTRY_POINT, "commonSetup", SETUP_DESCRIPTOR)) {
                    assertEquals("<init>", method.name);
                    listeners++;
                }
            }
        }
        assertEquals(1, listeners, "The mod event bus must receive common setup exactly once");
    }

    @Test
    void registersTheHookOnceThroughNativeEnqueueWork() throws Exception {
        int registrations = 0;
        for (MethodNode method : entryPoint().methods) {
            for (AbstractInsnNode instruction : method.instructions.toArray()) {
                if (!(instruction instanceof InvokeDynamicInsnNode)
                        || !references(instruction, HOOK, "register", "()V")) continue;
                assertEquals("commonSetup", method.name);
                assertEquals(SETUP_DESCRIPTOR, method.desc);
                assertEquals("()Ljava/lang/Runnable;", ((InvokeDynamicInsnNode) instruction).desc);
                AbstractInsnNode next = nextCode(instruction);
                assertNotNull(next);
                assertEquals(Opcodes.INVOKEVIRTUAL, next.getOpcode());
                MethodInsnNode enqueue = (MethodInsnNode) next;
                assertEquals(SETUP_EVENT, enqueue.owner);
                assertEquals("enqueueWork", enqueue.name);
                assertEquals("(Ljava/lang/Runnable;)Ljava/util/concurrent/CompletableFuture;", enqueue.desc);
                registrations++;
            }
        }
        assertEquals(1, registrations, "Queue one registration after parallel setup, before world loading");
    }

    private static ClassNode entryPoint() throws Exception {
        ClassNode node = new ClassNode();
        try (InputStream input = PersistenceRegistrationThreadTest.class.getResourceAsStream(
                "/" + ENTRY_POINT + ".class")) {
            assertNotNull(input);
            new ClassReader(input).accept(node, 0);
        }
        return node;
    }

    private static boolean references(AbstractInsnNode instruction, String owner, String name, String descriptor) {
        if (!(instruction instanceof InvokeDynamicInsnNode)) return false;
        for (Object argument : ((InvokeDynamicInsnNode) instruction).bsmArgs) {
            if (!(argument instanceof Handle)) continue;
            Handle handle = (Handle) argument;
            if (handle.getOwner().equals(owner) && handle.getName().equals(name)
                    && handle.getDesc().equals(descriptor)) return true;
        }
        return false;
    }

    private static AbstractInsnNode previousCode(AbstractInsnNode instruction) {
        do { instruction = instruction.getPrevious(); }
        while (instruction != null && instruction.getOpcode() < 0);
        return instruction;
    }

    private static AbstractInsnNode nextCode(AbstractInsnNode instruction) {
        do { instruction = instruction.getNext(); }
        while (instruction != null && instruction.getOpcode() < 0);
        return instruction;
    }
}
