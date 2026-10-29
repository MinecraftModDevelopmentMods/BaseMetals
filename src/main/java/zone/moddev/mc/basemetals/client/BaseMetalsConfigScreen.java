package zone.moddev.mc.basemetals.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

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
        super(Component.translatable(PREFIX + "title"));
        this.parent = parent;
        this.edits = new ConfigEdits(ContentMode.parse(BaseMetalsConfig.CONTENT_MODE.get()),
                VALUES[0].get(), VALUES[1].get(), VALUES[2].get(), VALUES[3].get());
    }

    @Override
    protected void init() {
        optionButtons.clear();
        int left = width / 2 - 150;
        int top = Math.max(38, height / 2 - 90);
        modeButton = addRenderableWidget(new OptionButton(left, top, 300, modeLabel(), () -> {
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
            addRenderableWidget(button);
        }

        addRenderableWidget(new OptionButton(left, top + 138, 145, I18n.get(PREFIX + "defaults"), () -> {
            edits.defaults();
            initLabels();
        }, null));
        addRenderableWidget(new OptionButton(left + 155, top + 138, 145, I18n.get(PREFIX + "undo"), () -> {
            edits.undo();
            initLabels();
        }, null));
        addRenderableWidget(new OptionButton(left, height - 28, 145, I18n.get("gui.done"), this::done, null));
        addRenderableWidget(new OptionButton(left + 155, height - 28, 145, I18n.get("gui.cancel"), this::onClose, null));
    }

    private String modeLabel() {
        return I18n.get(PREFIX + "contentMode") + ": " + I18n.get(edits.mode().translationKey());
    }

    private String optionLabel(int index) {
        return I18n.get(PREFIX + OPTIONS[index]) + ": "
                + I18n.get(edits.option(index) ? "options.on" : "options.off");
    }

    private void initLabels() {
        modeButton.setMessage(Component.literal(modeLabel()));

        for (int index = 0; index < optionButtons.size(); index++) {
            optionButtons.get(index).setMessage(Component.literal(optionLabel(index)));
        }
    }

    private void done() {
        if (edits.modeChanged()) {
            minecraft.gui.setScreen(new ConfirmScreen(this::confirmResult,
                    Component.translatable(PREFIX + "confirm.title"),
                    Component.translatable(PREFIX + "confirm.message")));
        } else {
            saveAndClose();
        }
    }

    private void confirmResult(boolean accepted) {
        if (accepted) {
            saveAndClose();
        } else {
            minecraft.gui.setScreen(this);
        }
    }

    private void saveAndClose() {
        if (edits.changed()) {
            BaseMetalsConfig.saveEdits(edits);
        }

        onClose();
    }

    @Override
    public void onClose() {
        minecraft.gui.setScreen(parent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        extractBackground(graphics, mouseX, mouseY, partialTicks);
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        graphics.centeredText(font, title, width / 2, 12, 0xFFFFFFFF);

        for (net.minecraft.client.gui.components.events.GuiEventListener child : children()) {
            if (child instanceof OptionButton && ((OptionButton) child).isHoveredOrFocused()) {
                String key = ((OptionButton) child).option;
                if (key != null) {
                    List<FormattedCharSequence> help = new ArrayList<>(font.split(
                            Component.translatable(PREFIX + key + ".tooltip"), 280));
                    if ("contentMode".equals(key)) {
                        help.addAll(font.split(Component.translatable(
                                edits.mode().translationKey() + ".description"), 280));
                        help.addAll(font.split(Component.translatable(PREFIX + "restart"), 280));
                    }
                    graphics.setTooltipForNextFrame(help, mouseX, mouseY);
                }
            }
        }
    }

    private static final class OptionButton extends Button.Plain {
        private final String option;

        private OptionButton(int x, int y, int width, String label, Runnable click, String option) {
            super(x, y, width, 20, Component.literal(label), button -> click.run(), DEFAULT_NARRATION);
            this.option = option;
        }

    }
}
