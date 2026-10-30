package zone.moddev.mc.basemetals.migration;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import static org.junit.jupiter.api.Assertions.*;

class LeavesTransformerTest {
    private static final String OWNER = "com/mojang/datafixers/Typed";
    private static final String METHOD = "(L" + OWNER + ";)L" + OWNER + ";";
    private static final String PREFIX = "(Lcom/mojang/datafixers/OpticFinder;";

    private ClassNode transform(ClassNode node) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        assertNotNull(engine, "Run the native Java 8 coremod engine");
        try (Reader reader = Files.newBufferedReader(Paths.get(
                "src/main/resources/coremods/basemetals_113_compatibility.js"), StandardCharsets.UTF_8)) {
            engine.eval(reader);
        }
        ScriptObjectMirror modules = (ScriptObjectMirror)engine.eval("initializeCoreMod()");
        ScriptObjectMirror entry = (ScriptObjectMirror)modules.get("basemetals_forge25_leaves_fixer");
        return (ClassNode)entry.callMember("transformer", node);
    }

    private ClassNode fixture(boolean broken) {
        ClassNode node = new ClassNode();
        node.name = "net/minecraft/util/datafix/fixes/LeavesFix$Section";
        MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, "func_208465_a", METHOD, null, null);
        node.methods.add(method);
        if (broken) method.instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, OWNER));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, OWNER, "set",
                PREFIX + (broken ? "L" + OWNER + ";" : "Ljava/lang/Object;") + ")L" + OWNER + ";", false));
        return node;
    }

    @Test void repairsBrokenCallAndRemainsIdempotent() throws Exception {
        ClassNode node = fixture(true);
        assertSame(node, transform(node));
        assertEquals(1, node.methods.get(0).instructions.size());
        String descriptor = ((MethodInsnNode)node.methods.get(0).instructions.getFirst()).desc;
        assertTrue(descriptor.contains("Ljava/lang/Object;"));
        assertSame(node, transform(node));
        assertEquals(descriptor, ((MethodInsnNode)node.methods.get(0).instructions.getFirst()).desc);
    }

    @Test void preservesAlreadyCorrectNativeCall() throws Exception {
        ClassNode node = fixture(false);
        Object instruction = node.methods.get(0).instructions.getFirst();
        assertSame(node, transform(node));
        assertSame(instruction, node.methods.get(0).instructions.getFirst());
        assertEquals(1, node.methods.get(0).instructions.size());
    }

    @Test void rejectsUnknownStructure() {
        ClassNode node = fixture(false);
        ((MethodInsnNode)node.methods.get(0).instructions.getFirst()).name = "notSet";
        assertThrows(Exception.class, () -> transform(node));
    }

    @Test void unrelatedObjectSetDoesNotMaskMissingFixer() {
        ClassNode node = fixture(false);
        node.methods.get(0).desc = "()V";
        assertThrows(Exception.class, () -> transform(node));
    }
}
