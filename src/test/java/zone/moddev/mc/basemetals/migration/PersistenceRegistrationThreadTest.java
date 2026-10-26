package zone.moddev.mc.basemetals.migration;

import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import static org.junit.jupiter.api.Assertions.*;

class PersistenceRegistrationThreadTest {
    @Test
    void registersTheSharedWorldHookOnceThroughQueuedCommonSetup() throws Exception {
        ClassNode node = new ClassNode();
        try (InputStream input = getClass().getResourceAsStream(
                "/zone/moddev/mc/basemetals/BaseMetals.class")) {
            assertNotNull(input);
            new ClassReader(input).accept(node, 0);
        }
        String hook = "zone/moddev/mc/basemetals/migration/LegacyWorldDataHook";
        int queuedRegistrations = 0;
        for (MethodNode method : node.methods) {
            boolean hookRunnable = false;
            for (AbstractInsnNode instruction : method.instructions.toArray()) {
                if (instruction instanceof MethodInsnNode) {
                    MethodInsnNode call = (MethodInsnNode) instruction;
                    assertFalse(call.owner.equals(hook) && call.name.equals("register"),
                            "Do not mutate Forge's hook ArrayList during parallel mod construction");
                    if (call.owner.equals("net/minecraftforge/fml/DeferredWorkQueue")
                            && call.name.equals("runLater") && hookRunnable) {
                        assertEquals("commonSetup", method.name);
                        queuedRegistrations++;
                        hookRunnable = false;
                    }
                }
                if (instruction instanceof InvokeDynamicInsnNode) {
                    for (Object argument : ((InvokeDynamicInsnNode) instruction).bsmArgs) {
                        if (argument instanceof Handle && ((Handle) argument).getOwner().equals(hook)
                                && ((Handle) argument).getName().equals("register")) hookRunnable = true;
                    }
                }
            }
        }
        assertEquals(1, queuedRegistrations,
                "Register once after parallel setup, before any world can load");
    }
}
