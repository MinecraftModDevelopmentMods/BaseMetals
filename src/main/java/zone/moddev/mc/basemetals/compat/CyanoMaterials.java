package zone.moddev.mc.basemetals.compat;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/** Registry lookups for the small Cyano API used by Base Metal Golems. */
public final class CyanoMaterials {

	private CyanoMaterials() {
	}

	public static Block block(final String material) {
		final ResourceLocation name = new ResourceLocation("basemetals", material + "_block");
		return ForgeRegistries.BLOCKS.containsKey(name) ? ForgeRegistries.BLOCKS.getValue(name) : null;
	}

	public static Item ingot(final String material) {
		final ResourceLocation name = new ResourceLocation("basemetals", material + "_ingot");
		return ForgeRegistries.ITEMS.containsKey(name) ? ForgeRegistries.ITEMS.getValue(name) : null;
	}
}
