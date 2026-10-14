package zone.moddev.mc.basemetals.config;

import java.util.Arrays;

/** Keeps edits separate from the loaded config until the player presses Done. */
public final class ConfigEdits {
    private final ContentMode originalMode;
    private final boolean[] originalOptions;
    private ContentMode mode;
    private boolean[] options;

    public ConfigEdits(ContentMode mode, boolean... options) {
        this.originalMode = mode;
        this.originalOptions = options.clone();
        undo();
    }

    public ContentMode mode() { return mode; }
    public boolean option(int index) { return options[index]; }
    public boolean modeChanged() { return mode != originalMode; }

    public boolean changed() {
        return modeChanged() || !Arrays.equals(options, originalOptions);
    }

    public void cycleMode() {
        mode = mode == ContentMode.HIGH_FANTASY ? ContentMode.LOW_FANTASY : ContentMode.HIGH_FANTASY;
    }

    public void toggle(int index) {
        options[index] = !options[index];
    }

    public void undo() {
        mode = originalMode;
        options = originalOptions.clone();
    }

    public void defaults() {
        mode = ContentMode.HIGH_FANTASY;
        Arrays.fill(options, true);
    }
}
