package zone.moddev.mc.basemetals.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.util.Config;

@SideOnly(Side.CLIENT)
public final class BaseMetalsConfigScreen extends GuiConfig {
	public BaseMetalsConfigScreen(final GuiScreen parent) {
		super(parent, categories(), BaseMetals.MODID, false, true,
				I18n.format("config.basemetals.title"), I18n.format("config.basemetals.restart_guidance"));
	}

	private static List<IConfigElement> categories() {
		final Configuration config = Config.getConfiguration();
		final List<IConfigElement> elements = new ArrayList<>();

		for (final String category : new String[] { "General", "Metals", "Vanilla",
				"Tools and Items", "Fluids", "Mod Integration", "Crack Hammer Recipes" }) {
			elements.add(new ConfigElement(config.getCategory(category)));
		}

		return elements;
	}
}
