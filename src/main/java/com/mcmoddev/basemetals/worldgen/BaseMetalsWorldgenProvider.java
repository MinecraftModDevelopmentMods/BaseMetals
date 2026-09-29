package com.mcmoddev.basemetals.worldgen;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.content.ContentPolicy;
import com.mcmoddev.basemetals.data.MaterialNames;
import net.minecraft.util.ResourceLocation;
import zone.moddev.mc.orespawn.api.OreDimensionSelector;
import zone.moddev.mc.orespawn.api.OreHeightDistribution;
import zone.moddev.mc.orespawn.api.OrePattern;
import zone.moddev.mc.orespawn.api.OreSpawnApi;
import zone.moddev.mc.orespawn.api.WorldgenProvider;

/** Builds Base Metals' OreSpawn defaults from the startup-latched content mode. */
public final class BaseMetalsWorldgenProvider {
	public static final int PROVIDER_REVISION = 2;
	private static final ResourceLocation NETHER = id("minecraft", "the_nether");
	private static final ResourceLocation END = id("minecraft", "the_end");
	private static final ResourceLocation STONE = id("minecraft", "stone");
	private static final ResourceLocation NETHERRACK = id("minecraft", "netherrack");
	private static final ResourceLocation END_STONE = id("minecraft", "end_stone");

	private BaseMetalsWorldgenProvider() {
	}

	public static boolean enqueue() {
		return OreSpawnApi.enqueue(build(ContentPolicy.active()));
	}

	public static WorldgenProvider build(final ContentPolicy policy) {
		final WorldgenProvider.Builder provider = WorldgenProvider
				.builder(BaseMetals.MODID, PROVIDER_REVISION)
				.mergeNewEntriesIntoExistingWorlds(true);

		addDimensionOre(provider, policy, MaterialNames.COLDIRON, NETHER, 0, 127, 5.0D);
		addDimensionOre(provider, policy, MaterialNames.ADAMANTINE, NETHER, 0, 127, 2.0D);
		addDimensionOre(provider, policy, MaterialNames.STARSTEEL, END, 0, 254, 5.0D);
		addOrdinaryOre(provider, policy, MaterialNames.COPPER, 0, 95, 10.0D);
		addOrdinaryOre(provider, policy, MaterialNames.SILVER, 0, 31, 4.0D);
		addOrdinaryOre(provider, policy, MaterialNames.TIN, 0, 127, 10.0D);
		addOrdinaryOre(provider, policy, MaterialNames.LEAD, 0, 63, 5.0D);
		addOrdinaryOre(provider, policy, MaterialNames.ZINC, 0, 95, 5.0D);
		addOrdinaryOre(provider, policy, MaterialNames.MERCURY, 0, 31, 3.0D);
		addOrdinaryOre(provider, policy, MaterialNames.NICKEL, 32, 95, 1.0D);
		addOrdinaryOre(provider, policy, MaterialNames.PLATINUM, 1, 31, 0.125D);
		return provider.build();
	}

	private static void addDimensionOre(final WorldgenProvider.Builder provider,
			final ContentPolicy policy, final String material, final ResourceLocation dimension,
			final int minY, final int maxY, final double attempts) {
		final ResourceLocation block = id(BaseMetals.MODID, material + "_ore");
		provider.ore(id(BaseMetals.MODID, "legacy/" + material + "_ore"), block, ore -> ore
				.enabled(policy.defaultsOreEnabled(material))
				.nativeGeneration(false)
				.retrogen(false)
				.output(block, 100.0D)
				.dimension(dimension, placement -> configure(placement, minY, maxY, attempts)));
	}

	private static void addOrdinaryOre(final WorldgenProvider.Builder provider,
			final ContentPolicy policy, final String material, final int minY,
			final int maxY, final double attempts) {
		final ResourceLocation block = id(BaseMetals.MODID, material + "_ore");
		provider.ore(id(BaseMetals.MODID, "legacy/" + material + "_ore"), block, ore -> ore
				.enabled(policy.defaultsOreEnabled(material))
				.nativeGeneration(false)
				.retrogen(false)
				.output(block, 100.0D)
				.dimensionSelector(OreDimensionSelector.ALL_EXCEPT_NETHER_AND_END,
						placement -> configure(placement, minY, maxY, attempts)));
	}

	private static void configure(final WorldgenProvider.OreDimensionDefinition.Builder placement,
			final int minY, final int maxY, final double attempts) {
		placement.enabled(true)
				.yRange(minY, maxY)
				.attempts(attempts)
				.quantityRange(4, 11)
				.pattern(OrePattern.DEFAULT)
				.heightDistribution(OreHeightDistribution.UNIFORM)
				.discardChanceOnAirExposure(0.0D)
				.spread(8, 4)
				.nodeSize(8)
				.hostBlock(STONE)
				.hostBlock(NETHERRACK)
				.hostBlock(END_STONE);
	}

	private static ResourceLocation id(final String namespace, final String path) {
		return new ResourceLocation(namespace, path);
	}
}
