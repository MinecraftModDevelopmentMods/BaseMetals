package zone.moddev.mc.basemetals.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ConfigEdits;
import zone.moddev.mc.basemetals.config.ContentMode;

/** The same screen is used by Forge's Mods list and OreSpawn's configuration cog. */
public final class BaseMetalsConfigScreen extends Screen {
    private static final String PREFIX = "config.basemetals.";
    private static final String[] OPTIONS = {
            "specialEffects", "starsteelRegeneration", "mercuryImmersionEffects", "villagerTrades"
    };
    private static final ForgeConfigSpec.BooleanValue[] VALUES = {
            BaseMetalsConfig.SPECIAL_EFFECTS, BaseMetalsConfig.STARSTEEL_REGENERATION,
            BaseMetalsConfig.MERCURY_EFFECTS, BaseMetalsConfig.VILLAGER_TRADES
    };

    private final Screen parent;
    private final ConfigEdits edits;
    private final List<OptionButton> optionButtons = new ArrayList<>();
    private OptionButton modeButton;

    public BaseMetalsConfigScreen(Screen parent) {
        super(new TranslationTextComponent(PREFIX + "title"));
        this.parent = parent;
        this.edits = new ConfigEdits(ContentMode.parse(BaseMetalsConfig.CONTENT_MODE.get()),
                VALUES[0].get(), VALUES[1].get(), VALUES[2].get(), VALUES[3].get());
    }

    @Override
    protected void init() {
        optionButtons.clear();
        int left = width / 2 - 150;
        int top = Math.max(38, height / 2 - 90);
        modeButton = addButton(new OptionButton(left, top, 300, modeLabel(), () -> {
            edits.cycleMode();
            initLabels();
        }, "contentMode"));

        for (int index = 0; index < OPTIONS.length; index++) {
            final int option = index;
            OptionButton button = new OptionButton(left, top + 26 * (index + 1), 300,
                    optionLabel(index), () -> {
                        edits.toggle(option);
                        initLabels();
                    }, OPTIONS[index]);
            optionButtons.add(button);
            addButton(button);
        }

        addButton(new OptionButton(left, top + 138, 145, I18n.format(PREFIX + "defaults"), () -> {
            edits.defaults();
            initLabels();
        }, null));
        addButton(new OptionButton(left + 155, top + 138, 145, I18n.format(PREFIX + "undo"), () -> {
            edits.undo();
            initLabels();
        }, null));
        addButton(new OptionButton(left, height - 28, 145, I18n.format("gui.done"), this::done, null));
        addButton(new OptionButton(left + 155, height - 28, 145, I18n.format("gui.cancel"), this::onClose, null));
    }

    private String modeLabel() {
        return I18n.format(PREFIX + "contentMode") + ": " + I18n.format(edits.mode().translationKey());
    }

    private String optionLabel(int index) {
        return I18n.format(PREFIX + OPTIONS[index]) + ": "
                + I18n.format(edits.option(index) ? "options.on" : "options.off");
    }

    private void initLabels() {
        modeButton.setMessage(modeLabel());

        for (int index = 0; index < optionButtons.size(); index++) {
            optionButtons.get(index).setMessage(optionLabel(index));
        }
    }

    private void done() {
        if (edits.modeChanged()) {
            minecraft.displayGuiScreen(new ConfirmScreen(this::confirmResult,
                    new TranslationTextComponent(PREFIX + "confirm.title"),
                    new TranslationTextComponent(PREFIX + "confirm.message")));
        } else {
            saveAndClose();
        }
    }

    private void confirmResult(boolean accepted) {
        if (accepted) saveAndClose();
        else minecraft.displayGuiScreen(this);
    }

    private void saveAndClose() {
        if (edits.changed()) {
            BaseMetalsConfig.set(BaseMetalsConfig.CONTENT_MODE, edits.mode().serializedName());
            for (int index = 0; index < VALUES.length; index++) BaseMetalsConfig.set(VALUES[index], edits.option(index));
            BaseMetalsConfig.save();
        }

        onClose();
    }

    @Override
    public void onClose() {
        minecraft.displayGuiScreen(parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        drawCenteredString(font, I18n.format(PREFIX + "title"), width / 2, 12, 0xFFFFFF);
        super.render(mouseX, mouseY, partialTicks);

        for (Widget button : buttons) {
            if (button instanceof OptionButton && button.isHovered()) {
                String key = ((OptionButton) button).option;
                if (key != null) {
                    List<String> help = new ArrayList<>(font.listFormattedStringToWidth(
                            I18n.format(PREFIX + key + ".tooltip"), 280));
                    if ("contentMode".equals(key)) {
                        help.addAll(font.listFormattedStringToWidth(
                                I18n.format(edits.mode().translationKey() + ".description"), 280));
                        help.addAll(font.listFormattedStringToWidth(
                                I18n.format(PREFIX + "restart"), 280));
                    }
                    renderTooltip(help, mouseX, mouseY);
                }
            }
        }
    }

    private static final class OptionButton extends Button {
        private final String option;

        private OptionButton(int x, int y, int width, String label, Runnable click, String option) {
            super(x, y, width, 20, label, button -> click.run());
            this.option = option;
        }

    }
}
