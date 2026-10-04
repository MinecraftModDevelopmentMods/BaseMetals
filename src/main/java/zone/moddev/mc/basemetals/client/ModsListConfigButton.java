package zone.moddev.mc.basemetals.client;

import java.lang.reflect.Field;

import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.gui.GuiModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import zone.moddev.mc.basemetals.BaseMetals;

/** Enables our Config button on Forge 25's Mods screen. */
final class ModsListConfigButton {
    private static final int CONFIG_BUTTON_ID = 20;
    private static final Field SELECTED_MOD = selectedModField();

    private ModsListConfigButton() {}

    static void onDraw(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (!(event.getGui() instanceof GuiModList)) return;

        GuiModList screen = (GuiModList) event.getGui();
        ModInfo selected = selectedMod(screen);
        if (selected != null && !BaseMetals.MOD_ID.equals(selected.getModId())) return;

        // Forge 25's hasConfigUI() always returns false, even with a registered factory.
        screen.getChildren().stream()
                .filter(GuiButton.class::isInstance)
                .map(GuiButton.class::cast)
                .filter(button -> button.id == CONFIG_BUTTON_ID)
                .forEach(button -> button.enabled = selected != null);
    }

    private static Field selectedModField() {
        // Forge keeps the selection private; the field isn't Minecraft-mapped.
        try {
            Field field = GuiModList.class.getDeclaredField("selectedMod");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException failure) {
            throw new IllegalStateException("Cannot read the selected mod on Forge's Mods screen", failure);
        }
    }

    private static ModInfo selectedMod(GuiModList screen) {
        try {
            return (ModInfo) SELECTED_MOD.get(screen);
        } catch (IllegalAccessException failure) {
            throw new IllegalStateException("Cannot read the selected mod on Forge's Mods screen", failure);
        }
    }
}
