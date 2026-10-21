package zone.moddev.mc.basemetals.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.orespawn.api.GeologyFamily;
import zone.moddev.mc.orespawn.api.OreDimensionSelector;
import zone.moddev.mc.orespawn.api.OrePattern;
import zone.moddev.mc.orespawn.api.OreSpawnApi;
import zone.moddev.mc.orespawn.api.WorldgenProvider;

/** Declares the historical ore rules; OreSpawn owns placement and config import. */
public final class BaseMetalsWorldgenProvider {

	private BaseMetalsWorldgenProvider() {
	}

	public static void enqueue() {
		build(ForgeRegistries.BLOCKS::containsKey).ifPresent(provider -> {
			if (!OreSpawnApi.enqueue(provider)) {
				throw new IllegalStateException("Base Metals' OreSpawn provider was registered twice");
			}
		});
	}

	public static Optional<WorldgenProvider> build(final Predicate<ResourceLocation> available) {
		final List<WorldgenProvider.OreDefinition> ores = new ArrayList<>();
		final ResourceLocation nether = id("minecraft", "the_nether");
		final ResourceLocation end = id("minecraft", "the_end");

		addOre(ores, available, "coldiron", nether, 0, 127, 5.0D);
		addOre(ores, available, "adamantine", nether, 0, 127, 2.0D);
		addOre(ores, available, "starsteel", end, 0, 254, 5.0D);
		addOre(ores, available, "copper", null, 0, 95, 10.0D);
		addOre(ores, available, "silver", null, 0, 31, 4.0D);
		addOre(ores, available, "tin", null, 0, 127, 10.0D);
		addOre(ores, available, "lead", null, 0, 63, 5.0D);
		addOre(ores, available, "zinc", null, 0, 95, 5.0D);
		addOre(ores, available, "mercury", null, 0, 31, 3.0D);
		addOre(ores, available, "nickel", null, 32, 95, 1.0D);
		addOre(ores, available, "platinum", null, 1, 31, 0.125D);

		// A pack may disable every ore-bearing material in BaseMetals.cfg.
		if (ores.isEmpty()) return Optional.empty();

		final WorldgenProvider.Builder provider = WorldgenProvider.builder(BaseMetals.MODID, 1);
		ores.forEach(provider::ore);
		return Optional.of(provider.build());
	}

	private static void addOre(final List<WorldgenProvider.OreDefinition> ores,
			final Predicate<ResourceLocation> available, final String material,
			final ResourceLocation dimension, final int minY, final int maxY, final double attempts) {
		final ResourceLocation block = id(BaseMetals.MODID, material + "_ore");
		if (!available.test(block)) return;

		final WorldgenProvider.OreDefinition.Builder ore = WorldgenProvider.OreDefinition
				.builder(id(BaseMetals.MODID, "legacy/" + material + "_ore"), block)
				.material(id("orespawn", material))
				.nativeGeneration(false)
				.retrogen(false)
				.output(block, 100.0D);

		if (dimension == null) {
			ore.dimensionSelector(OreDimensionSelector.ALL_EXCEPT_NETHER_AND_END,
					placement -> configure(placement, minY, maxY, attempts));
		} else {
			ore.dimension(dimension, placement -> configure(placement, minY, maxY, attempts));
		}

		ores.add(ore.build());
	}

	private static void configure(final WorldgenProvider.OreDimensionDefinition.Builder placement,
			final int minY, final int maxY, final double attempts) {
		placement.yRange(minY, maxY)
				.attempts(attempts)
				.quantityRange(4, 11)
				.pattern(OrePattern.DEFAULT)
				.placementChannel(id("orespawn", "standard"))
				.hostBlock(id("minecraft", "stone"))
				.hostBlock(id("minecraft", "netherrack"))
				.hostBlock(id("minecraft", "end_stone"));

		// This also permits the ore-replaceable rocks supplied by Mineralogy.
		for (final GeologyFamily family : GeologyFamily.values()) {
			placement.hostFamily(family);
		}
	}

	private static ResourceLocation id(final String namespace, final String path) {
		return new ResourceLocation(namespace, path);
	}
}
