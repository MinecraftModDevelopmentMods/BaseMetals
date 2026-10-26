package zone.moddev.mc.basemetals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.gametest.GameTest;
import net.minecraftforge.registries.RegisterEvent;
import zone.moddev.mc.basemetals.compat.CompatibilityRecipeGameTests;
import zone.moddev.mc.basemetals.content.BaseMetalsGameTests;
import zone.moddev.mc.basemetals.content.ForwardPortGameTests;
import zone.moddev.mc.basemetals.content.NativeComponentGameTests;
import zone.moddev.mc.basemetals.migration.MigrationGameTests;

/** Registers the isolated tests in Minecraft's native test-function registry. */
@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GameTestRegistration {
    private GameTestRegistration() {}

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(Registries.TEST_FUNCTION, registry -> {
            NativeComponentGameTests.registerCapabilityProbe();
            int count = 0;

            for (Class<?> testClass : List.of(BaseMetalsGameTests.class, ForwardPortGameTests.class,
                    NativeComponentGameTests.class, MigrationGameTests.class, CompatibilityRecipeGameTests.class)) {
                for (Method method : testClass.getDeclaredMethods()) {
                    if (!method.isAnnotationPresent(GameTest.class)) continue;

                    String name = method.getName().replaceAll("([A-Z])", "_$1").toLowerCase(Locale.ROOT);
                    registry.register(Identifier.fromNamespaceAndPath(BaseMetals.MOD_ID, name),
                            helper -> run(method, helper));
                    count++;
                }
            }

            if (count != 46) {
                throw new IllegalStateException("Expected 46 Base Metals GameTests, found " + count);
            }
        });
    }

    private static void run(Method method, GameTestHelper helper) {
        try {
            method.invoke(null, helper);
        } catch (InvocationTargetException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof RuntimeException failure) throw failure;
            if (cause instanceof Error failure) throw failure;

            throw new IllegalStateException("GameTest failed: " + method.getName(), cause);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not invoke GameTest " + method.getName(), exception);
        }
    }
}
