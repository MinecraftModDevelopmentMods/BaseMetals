package zone.moddev.mc.basemetals.smoke;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModList;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.client.BaseMetalsConfigScreen;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentMode;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry;

/** Exercises the real widgets using only the disposable smoke-test configuration. */
final class ConfigScreenSmoke {
    private ConfigScreenSmoke() {}

    static void verify(Minecraft minecraft, Screen parent) {
        var mod = ModList.get().getModContainerById(BaseMetals.MOD_ID).orElseThrow().getModInfo();
        Screen forgeScreen = ConfigGuiHandler.getGuiFactoryFor(mod).orElseThrow().apply(minecraft, parent);
        require(forgeScreen instanceof BaseMetalsConfigScreen, "Forge Config button has the wrong screen");

        var extensions = WorldSettingsExtensionRegistry.extensions().stream()
                .filter(extension -> extension.id().toString().equals("basemetals:configuration")).toList();
        require(extensions.size() == 1, "OreSpawn must have exactly one Base Metals cog");
        Screen screen = extensions.get(0).createScreen(parent);
        require(screen instanceof BaseMetalsConfigScreen, "OreSpawn cog has the wrong screen");

        String originalMode = BaseMetalsConfig.CONTENT_MODE.get();
        boolean originalEffects = BaseMetalsConfig.SPECIAL_EFFECTS.get();
        ContentMode activeMode = BaseMetalsConfig.activeMode();
        minecraft.setScreen(screen);
        require(buttons(screen).size() == 9, "Expected the mode, four switches and four action buttons");
        String initialLabel = buttons(screen).get(0).getMessage().getString();
        buttons(screen).get(0).onPress();
        require(!initialLabel.equals(buttons(screen).get(0).getMessage().getString()), "Mode did not change");
        button(screen, "config.basemetals.undo").onPress();
        require(initialLabel.equals(buttons(screen).get(0).getMessage().getString()), "Undo lost the initial mode");
        button(screen, "config.basemetals.defaults").onPress();
        require(buttons(screen).get(0).getMessage().getString().contains(I18n.get(ContentMode.HIGH_FANTASY.translationKey())),
                "Defaults must select High Fantasy");
        screen.onClose();
        require(minecraft.screen == parent, "Cancel did not return to the parent screen");
        require(originalMode.equals(BaseMetalsConfig.CONTENT_MODE.get()), "Cancel saved pending edits");

        screen = extensions.get(0).createScreen(parent);
        minecraft.setScreen(screen);
        buttons(screen).get(1).onPress();
        button(screen, "gui.done").onPress();
        require(minecraft.screen == parent, "Done did not return to the parent screen");
        require(BaseMetalsConfig.SPECIAL_EFFECTS.get() != originalEffects, "Done did not save a boolean edit");

        screen = extensions.get(0).createScreen(parent);
        minecraft.setScreen(screen);
        buttons(screen).get(0).onPress();
        button(screen, "gui.done").onPress();
        require(minecraft.screen instanceof ConfirmScreen, "A mode change needs restart confirmation");
        button(minecraft.screen, "gui.yes").onPress();
        require(minecraft.screen == parent, "Confirmation did not return to the parent screen");
        require(!originalMode.equals(BaseMetalsConfig.CONTENT_MODE.get()), "Done did not save the mode edit");
        require(BaseMetalsConfig.activeMode() == activeMode, "A GUI edit changed the running content mode");

        BaseMetalsConfig.set(BaseMetalsConfig.CONTENT_MODE, originalMode);
        BaseMetalsConfig.set(BaseMetalsConfig.SPECIAL_EFFECTS, originalEffects);
        BaseMetalsConfig.save();
        BaseMetals.LOGGER.info("BASEMETALS_CONFIG_SCREEN_SMOKE PASS Forge OreSpawn Done Cancel Undo Defaults restart");
    }

    private static List<Button> buttons(Screen screen) {
        return screen.children().stream().filter(Button.class::isInstance).map(Button.class::cast).toList();
    }

    private static Button button(Screen screen, String translation) {
        return buttons(screen).stream().filter(button -> button.getMessage().getString().equals(I18n.get(translation)))
                .findFirst().orElseThrow(() -> new IllegalStateException("Missing button " + translation));
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException("CONFIG_SCREEN_SMOKE FAIL: " + message);
    }
}
