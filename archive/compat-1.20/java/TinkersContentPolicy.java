/* package zone.moddev.mc.basemetals.compat;

import zone.moddev.mc.basemetals.config.ContentMode;
import zone.moddev.mc.basemetals.config.ContentPolicy;
import zone.moddev.mc.basemetals.config.MaterialForm;

/** Tinkers head stats enable a whole tool family, not individual tools. * /
public final class TinkersContentPolicy {
    private TinkersContentPolicy() {}

    public static boolean allows(ContentMode mode, String material, String stat) {
        if (mode == ContentMode.HIGH_FANTASY) return true;

        ContentPolicy policy = ContentPolicy.forMode(mode);
        return switch (stat) {
            case "tconstruct:head", "tconstruct:handle", "tconstruct:extra" ->
                    policy.allows(material, MaterialForm.PICKAXE);
            case "tconstruct:limb", "tconstruct:grip", "tconstruct:bow", "tconstruct:bowstring",
                    "tconstruct:shaft", "tconstruct:fletching" -> false;
            case "tconstruct:plating", "tconstruct:mail", "tconstruct:armor" ->
                    policy.allows(material, MaterialForm.CHESTPLATE);
            default -> true;
        };
    }
}
 */