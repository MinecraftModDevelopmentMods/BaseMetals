package zone.moddev.mc.basemetals.compat;

import com.mcmoddev.lib.asm.ASMPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/** Keeps the existing horse-armour hooks and installs the old API name adapter. */
@IFMLLoadingPlugin.Name("BaseMetals")
@IFMLLoadingPlugin.MCVersion("1.10.2")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("zone.moddev.mc.basemetals.compat.")
public final class BaseMetalsLoadingPlugin extends ASMPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
				"zone.moddev.mc.basemetals.compat.LegacyNamespaceTransformer",
				"com.mcmoddev.lib.asm.ASMTransformer"
		};
	}
}
