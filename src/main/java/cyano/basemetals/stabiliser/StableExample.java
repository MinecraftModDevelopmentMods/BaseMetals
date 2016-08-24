package cyano.basemetals.stabiliser;

import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.awt.*;

public class StableExample implements IStableInfo {

	public void preInit() {
		FMLInterModComms.sendMessage("basemetals", "stabiliser-register", "cyano.basemetals.stabiliser.StableExample");
	}

	@Override
	public String getModID() {
		return "stable-example";
	}

	@Override
	public String getModVersion() {
		return "1.0.0";
	}

	@Override
	public String getModName() {
		return "Stable Example";
	}

	@Override
	public String getModClass() {
		return "cyano.basemetals.stabiliser.StableExample";
	}

	@Override
	public String getUpdateJSONURL() {
		return "Not Currently Finished";
	}

	@Override
	public ColorScheme getColorScheme() {
		return ColorScheme.create(
				new Color(0xC5C3FF).getRGB(),
				new Color(0xB8FFD6).getRGB()
		);
	}
}
