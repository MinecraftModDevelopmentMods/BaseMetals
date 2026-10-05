package net.minecraftforge.fml.client.gui;

/** Test access to Forge's package-private Mods-list entries. */
public final class ModSelection {
    private ModSelection() {}

    public static void select(GuiModList screen, String modId) {
        GuiSlotModList list = (GuiSlotModList) screen.children().stream()
                .filter(child -> child instanceof GuiSlotModList).findFirst().get();

        for (GuiSlotModList.ModEntry entry : list.children()) {
            if (entry.getInfo().getModId().equals(modId)) {
                screen.setSelected(entry);
                return;
            }
        }

        throw new IllegalStateException("Missing mod " + modId);
    }
}
