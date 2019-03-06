package com.mcmoddev.basemetals.init;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.properties.BMEPropertiesHelper;
import com.mcmoddev.lib.block.InteractiveFluidBlock;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterialType.MaterialType;
import com.mcmoddev.lib.material.IFluidBlockGetter;
import com.mcmoddev.lib.util.Config.Options;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * This class initializes all of the materials in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public final class Materials extends com.mcmoddev.lib.init.Materials {

	private Materials() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		// Vanilla Materials
		// always created and populated with their base item-sets
		// even if they are not enabled
		// Oreless because our Recipe code can tend to be silly otherwise
		createVanillaMats();

		final List<String> rareMaterials = Arrays.asList(MaterialNames.ADAMANTINE, MaterialNames.COLDIRON,
				MaterialNames.PLATINUM, MaterialNames.STARSTEEL);

		final List<String> materials = Arrays.asList(MaterialNames.ANTIMONY, MaterialNames.BISMUTH,
				MaterialNames.COPPER, MaterialNames.LEAD, MaterialNames.NICKEL,
				MaterialNames.SILVER, MaterialNames.TIN, MaterialNames.ZINC);

		final List<String> rareAlloyMaterials = Arrays.asList(MaterialNames.AQUARIUM, MaterialNames.MITHRIL);

		final List<String> alloyMaterials = Arrays.asList(MaterialNames.BRASS, MaterialNames.BRONZE,
				MaterialNames.CUPRONICKEL, MaterialNames.ELECTRUM, MaterialNames.INVAR, MaterialNames.PEWTER,
				MaterialNames.STEEL);

		materials.stream().filter(Options::isMaterialEnabled).forEach(name -> createMaterial(name, MaterialType.METAL,
				getHardness(name), getStrength(name), getMagic(name), getColor(name)));

		alloyMaterials.stream().filter(Options::isMaterialEnabled).forEach(name -> createAlloyMaterial(name,
				MaterialType.METAL, getHardness(name), getStrength(name), getMagic(name), getColor(name)));

		rareMaterials.stream().filter(Options::isMaterialEnabled).forEach(name -> createRareMaterial(name,
				MaterialType.METAL, getHardness(name), getStrength(name), getMagic(name), getColor(name)));

		rareAlloyMaterials.stream().filter(Options::isMaterialEnabled).forEach(name -> createRareAlloyMaterial(name,
				MaterialType.METAL, getHardness(name), getStrength(name), getMagic(name), getColor(name)));
		
		if(Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			createMaterial(MaterialNames.MERCURY, MaterialType.METAL, 1.0d, 1.0d, 1.0d, 0xFFE2E2E2, true);
			Materials.getMaterialByName(MaterialNames.MERCURY).setFluidBlockGetter(new IFluidBlockGetter() {
				public BlockFluidClassic apply(String fluidName) {
					return new InteractiveFluidBlock(FluidRegistry.getFluid(MaterialNames.MERCURY), false,
		                    (final World w, final EntityLivingBase e) -> {
		                        if (w.rand.nextInt(32) == 0) {
		                            e.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 30 * 20, 2));
		                        }
		                    });					
				}
			});
		}
		
		// Mod Materials
		if (hasMaterial(MaterialNames.ADAMANTINE)) {
			getMaterialByName(MaterialNames.ADAMANTINE).setBlastResistance(2000f).setSpawnSize(4)
					.setDefaultDimension(-1);
		}

		if (hasMaterial(MaterialNames.STARSTEEL)) {
			getMaterialByName(MaterialNames.STARSTEEL).setBlastResistance(2000f).setSpawnSize(6)
					.setDefaultDimension(1).setRegenerates(true);
		}
	}

	public static void initTooltips() {
		Arrays.asList(MaterialNames.ADAMANTINE, MaterialNames.COLDIRON, MaterialNames.AQUARIUM, MaterialNames.MITHRIL)
		.stream().filter(Options::isMaterialEnabled)
		.forEach(mn -> {
			Arrays.asList(Names.HELMET, Names.CHESTPLATE, Names.LEGGINGS, Names.BOOTS).stream()
			.forEach(n -> Materials.getMaterialByName(mn)
					.addTooltipFor(n, BMEPropertiesHelper.addArmorSpecialPropertiesToolTip(mn)));
			Arrays.asList(Names.values()).stream()
			.filter(n -> (n != Names.HELMET && n != Names.CHESTPLATE && n != Names.LEGGINGS && n != Names.BOOTS))
			.forEach(n -> Materials.getMaterialByName(mn)
					.addTooltipFor(n, BMEPropertiesHelper.addToolSpecialPropertiesToolTip(mn)));
		});
	}
	
	private static int getColor(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.ADAMANTINE:
				return 0xFF53393F;
			case MaterialNames.ANTIMONY:
				return 0xFFD8E3DE;
			case MaterialNames.AQUARIUM:
				return 0xFF0000FF;
			case MaterialNames.BISMUTH:
				return 0xFFDDD7CB;
			case MaterialNames.BRASS:
				return 0xFFFFE374;
			case MaterialNames.BRONZE:
				return 0xFFF7A54F;
			case MaterialNames.COLDIRON:
				return 0xFFC7CEF0;
			case MaterialNames.COPPER:
				return 0xFFFF9F78;
			case MaterialNames.CUPRONICKEL:
				return 0xFFC8AB6F;
			case MaterialNames.ELECTRUM:
				return 0xFFFFF2B3;
			case MaterialNames.INVAR:
				return 0xFFD2CDB8;
			case MaterialNames.LEAD:
				return 0xFF7B7B7B;
			case MaterialNames.MITHRIL:
				return 0xFFF4FFFF;
			case MaterialNames.NICKEL:
				return 0xFFEEFFEB;
			case MaterialNames.PEWTER:
				return 0xFF92969F;
			case MaterialNames.PLATINUM:
				return 0xFFF2FFFF;
			case MaterialNames.SILVER:
				return 0xFFFFFFFF;
			case MaterialNames.STARSTEEL:
				return 0xFF53393F;
			case MaterialNames.STEEL:
				return 0xFFD5E3E5;
			case MaterialNames.TIN:
				return 0xFFFFF7EE;
			case MaterialNames.ZINC:
				return 0xFFBCBCBC;
			default:
				return 0x00000000;
		}
	}

	private static double getMagic(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.PLATINUM:
			case MaterialNames.AQUARIUM:
				return 15.0d;
			case MaterialNames.STARSTEEL:
				return 12.0d;
			case MaterialNames.ELECTRUM:
				return 10.0d;
			case MaterialNames.MITHRIL:
			case MaterialNames.BRASS:
				return 9.0d;
			case MaterialNames.NICKEL:
			case MaterialNames.COLDIRON:
				return 7.0d;
			case MaterialNames.SILVER:
			case MaterialNames.CUPRONICKEL:
				return 6.0d;
			case MaterialNames.COPPER:
				return 5.0d;
			case MaterialNames.BRONZE:
				return 4.5d;
			case MaterialNames.INVAR:
				return 3.0d;
			case MaterialNames.TIN:
			case MaterialNames.STEEL:
				return 2.0d;
			case MaterialNames.ANTIMONY:
			case MaterialNames.BISMUTH:
			case MaterialNames.LEAD:
			case MaterialNames.PEWTER:
			case MaterialNames.ZINC:
				return 1.0d;
			case MaterialNames.ADAMANTINE:
				return 0.0d;
			default:
				return 1.0d;
		}
	}

	private static double getStrength(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.ADAMANTINE:
				return 100.0d;
			case MaterialNames.STARSTEEL:
				return 25.0d;
			case MaterialNames.STEEL:
				return 15.0d;
			case MaterialNames.INVAR:
			case MaterialNames.AQUARIUM:
				return 10.0d;
			case MaterialNames.MITHRIL:
				return 9.0d;
			case MaterialNames.COLDIRON:
				return 7.0d;
			case MaterialNames.CUPRONICKEL:
				return 6.0d;
			case MaterialNames.PLATINUM:
				return 5.0d;
			case MaterialNames.SILVER:
			case MaterialNames.NICKEL:
			case MaterialNames.ELECTRUM:
			case MaterialNames.COPPER:
			case MaterialNames.BRONZE:
				return 4.0d;
			case MaterialNames.BRASS:
				return 3.0d;
			case MaterialNames.TIN:
			case MaterialNames.ANTIMONY:
			case MaterialNames.BISMUTH:
			case MaterialNames.LEAD:
			case MaterialNames.PEWTER:
			case MaterialNames.ZINC:
			default:
				return 1.0d;
		}
	}

	private static double getHardness(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.ADAMANTINE:
				return 12.0d;
			case MaterialNames.STARSTEEL:
				return 10.0d;
			case MaterialNames.INVAR:
			case MaterialNames.MITHRIL:
				return 9.0d;
			case MaterialNames.STEEL:
			case MaterialNames.BRONZE:
				return 8.0d;
			case MaterialNames.COLDIRON:
				return 7.0d;
			case MaterialNames.CUPRONICKEL:
				return 6.0d;
			case MaterialNames.SILVER:
			case MaterialNames.ELECTRUM:
				return 5.0d;
			case MaterialNames.COPPER:
			case MaterialNames.AQUARIUM:
			case MaterialNames.NICKEL:
				return 4.0d;
			case MaterialNames.BRASS:
				return 3.5d;
			case MaterialNames.TIN:
			case MaterialNames.PLATINUM:
				return 3.0d;
			case MaterialNames.ANTIMONY:
			case MaterialNames.BISMUTH:
			case MaterialNames.LEAD:
			case MaterialNames.PEWTER:
			case MaterialNames.ZINC:
			default:
				return 1.0d;
		}
	}

	private static void createVanillaMats() {
		createVanillaMaterial(MaterialNames.WOOD, MaterialType.WOOD, 2, 2, 6, 0xFF695433);
		createVanillaMaterial(MaterialNames.STONE, MaterialType.ROCK, 5, 4, 2, 0xFF8F8F8F);
		createVanillaMaterial(MaterialNames.IRON, MaterialType.METAL, 8, 8, 4.5, 0xFFD8D8D8);
		createVanillaMaterial(MaterialNames.GOLD, MaterialType.METAL, 1, 1, 10, 0xFFFFFF8B);
		createVanillaMaterial(MaterialNames.DIAMOND, MaterialType.GEM, 10, 15, 4, 0xFF8CF4E1);
		createVanillaMaterial(MaterialNames.COAL, MaterialType.MINERAL, 4, 4, 2, 0xFF151515);
		createVanillaMaterial(MaterialNames.CHARCOAL, MaterialType.MINERAL, 4, 4, 2, 0xFF231F18);
		createVanillaMaterial(MaterialNames.EMERALD, MaterialType.GEM, 10, 15, 4, 0xFF82F6AC);
		createVanillaMaterial(MaterialNames.ENDER, MaterialType.GEM, 2, 2, 6, 0xFF063931);
		createVanillaMaterial(MaterialNames.QUARTZ, MaterialType.GEM, 5, 4, 2, 0xFFEAE3DB);
		createVanillaMaterial(MaterialNames.OBSIDIAN, MaterialType.ROCK, 10, 15, 4, 0xFF101019);
		createVanillaMaterial(MaterialNames.LAPIS, MaterialType.MINERAL, 1, 1, 1, 0xFF26619c);
		createVanillaMaterial(MaterialNames.PRISMARINE, MaterialType.MINERAL, 1, 1, 1, 0xFF7fb8a4);
		createVanillaMaterial(MaterialNames.REDSTONE, MaterialType.MINERAL, 1, 1, 1, 0xFF720000);
	}
}
