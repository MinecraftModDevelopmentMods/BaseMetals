package zone.moddev.mc.basemetals.client;

import java.util.ArrayList;
import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.TextComponent;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
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
        super(new TranslatableComponent(PREFIX + "title"));
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
        modeButton.setMessage(new TextComponent(modeLabel()));

        for (int index = 0; index < optionButtons.size(); index++) {
            optionButtons.get(index).setMessage(new TextComponent(optionLabel(index)));
        }
    }

    private void done() {
        if (edits.modeChanged()) {
            minecraft.setScreen(new ConfirmScreen(this::confirmResult,
                    new TranslatableComponent(PREFIX + "confirm.title"),
                    new TranslatableComponent(PREFIX + "confirm.message")));
        } else {
            saveAndClose();
        }
    }

    private void confirmResult(boolean accepted) {
        if (accepted) saveAndClose();
        else minecraft.setScreen(this);
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
        minecraft.setScreen(parent);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrices);
        drawCenteredString(matrices, font, title, width / 2, 12, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, partialTicks);

        for (net.minecraft.client.gui.components.events.GuiEventListener child : children()) {
            if (child instanceof OptionButton && ((OptionButton) child).isHovered()) {
                String key = ((OptionButton) child).option;
                if (key != null) {
                    List<FormattedCharSequence> help = new ArrayList<>(font.split(
                            new TranslatableComponent(PREFIX + key + ".tooltip"), 280));
                    if ("contentMode".equals(key)) {
                        help.addAll(font.split(new TranslatableComponent(edits.mode().translationKey() + ".description"), 280));
                        help.addAll(font.split(new TranslatableComponent(PREFIX + "restart"), 280));
                    }
                    renderTooltip(matrices, help, mouseX, mouseY);
                }
            }
        }
    }

    private static final class OptionButton extends Button {
        private final String option;

        private OptionButton(int x, int y, int width, String label, Runnable click, String option) {
            super(x, y, width, 20, new TextComponent(label), button -> click.run());
            this.option = option;
        }

    }
}
