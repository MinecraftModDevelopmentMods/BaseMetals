package zone.moddev.mc.basemetals.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ConfigEdits;
import zone.moddev.mc.basemetals.config.ContentMode;

/** The same screen is used by Forge's Mods list and OreSpawn's configuration cog. */
public final class BaseMetalsConfigScreen extends GuiScreen {
    private static final String PREFIX = "config.basemetals.";
    private static final String[] OPTIONS = {
            "specialEffects", "starsteelRegeneration", "mercuryImmersionEffects", "villagerTrades"
    };
    private static final ForgeConfigSpec.BooleanValue[] VALUES = {
            BaseMetalsConfig.SPECIAL_EFFECTS, BaseMetalsConfig.STARSTEEL_REGENERATION,
            BaseMetalsConfig.MERCURY_EFFECTS, BaseMetalsConfig.VILLAGER_TRADES
    };

    private final GuiScreen parent;
    private final ConfigEdits edits;
    private final List<Button> optionButtons = new ArrayList<>();

    public BaseMetalsConfigScreen(GuiScreen parent) {
        this.parent = parent;
        this.edits = new ConfigEdits(ContentMode.parse(BaseMetalsConfig.CONTENT_MODE.get()),
                VALUES[0].get(), VALUES[1].get(), VALUES[2].get(), VALUES[3].get());
    }

    @Override
    protected void initGui() {
        optionButtons.clear();
        int left = width / 2 - 150;
        int top = Math.max(38, height / 2 - 90);
        addButton(new Button(20, left, top, 300, modeLabel(), () -> {
            edits.cycleMode();
            initLabels();
        }, "contentMode"));

        for (int index = 0; index < OPTIONS.length; index++) {
            final int option = index;
            Button button = new Button(10 + index, left, top + 26 * (index + 1), 300,
                    optionLabel(index), () -> {
                        edits.toggle(option);
                        initLabels();
                    }, OPTIONS[index]);
            optionButtons.add(button);
            addButton(button);
        }

        addButton(new Button(2, left, top + 138, 145, I18n.format(PREFIX + "defaults"), () -> {
            edits.defaults();
            initLabels();
        }, null));
        addButton(new Button(3, left + 155, top + 138, 145, I18n.format(PREFIX + "undo"), () -> {
            edits.undo();
            initLabels();
        }, null));
        addButton(new Button(0, left, height - 28, 145, I18n.format("gui.done"), this::done, null));
        addButton(new Button(1, left + 155, height - 28, 145, I18n.format("gui.cancel"), this::close, null));
    }

    private String modeLabel() {
        return I18n.format(PREFIX + "contentMode") + ": " + I18n.format(edits.mode().translationKey());
    }

    private String optionLabel(int index) {
        return I18n.format(PREFIX + OPTIONS[index]) + ": "
                + I18n.format(edits.option(index) ? "options.on" : "options.off");
    }

    private void initLabels() {
        for (GuiButton button : buttons) {
            if (button.id == 20) button.displayString = modeLabel();
        }

        for (int index = 0; index < optionButtons.size(); index++) {
            optionButtons.get(index).displayString = optionLabel(index);
        }
    }

    private void done() {
        if (edits.modeChanged()) {
            mc.displayGuiScreen(new GuiYesNo(this, I18n.format(PREFIX + "confirm.title"),
                    I18n.format(PREFIX + "confirm.message"), 0));
        } else {
            saveAndClose();
        }
    }

    @Override
    public void confirmResult(boolean accepted, int id) {
        if (accepted) saveAndClose();
        else mc.displayGuiScreen(this);
    }

    private void saveAndClose() {
        if (edits.changed()) {
            BaseMetalsConfig.set(BaseMetalsConfig.CONTENT_MODE, edits.mode().serializedName());
            for (int index = 0; index < VALUES.length; index++) BaseMetalsConfig.set(VALUES[index], edits.option(index));
            BaseMetalsConfig.save();
        }

        close();
    }

    @Override
    public void close() {
        mc.displayGuiScreen(parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, I18n.format(PREFIX + "title"), width / 2, 12, 0xFFFFFF);
        super.render(mouseX, mouseY, partialTicks);

        for (GuiButton button : buttons) {
            if (button instanceof Button && button.isMouseOver()) {
                String key = ((Button) button).option;
                if (key != null) {
                    List<String> help = new ArrayList<>(fontRenderer.listFormattedStringToWidth(
                            I18n.format(PREFIX + key + ".tooltip"), 280));
                    if ("contentMode".equals(key)) {
                        help.addAll(fontRenderer.listFormattedStringToWidth(
                                I18n.format(edits.mode().translationKey() + ".description"), 280));
                        help.addAll(fontRenderer.listFormattedStringToWidth(
                                I18n.format(PREFIX + "restart"), 280));
                    }
                    drawHoveringText(help, mouseX, mouseY);
                }
            }
        }
    }

    private static final class Button extends GuiButton {
        private final Runnable click;
        private final String option;

        private Button(int id, int x, int y, int width, String label, Runnable click, String option) {
            super(id, x, y, width, 20, label);
            this.click = click;
            this.option = option;
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            click.run();
        }
    }
}
