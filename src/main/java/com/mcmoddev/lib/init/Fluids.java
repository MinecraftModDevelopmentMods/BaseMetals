package com.mcmoddev.lib.init;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.*;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.blocks.InteractiveFluidBlock;
import com.mcmoddev.lib.fluids.CustomFluid;
import com.mcmoddev.lib.material.MetalMaterial;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This class initializes all fluids in Base Metals and provides some utility
 * methods for looking up fluids.
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Fluids {

//	static {
//		FluidRegistry.enableUniversalBucket();
//	}

	private static boolean initDone = false;

	private static final BiMap<String, Fluid> fluidRegistry = HashBiMap.create();
	private static final BiMap<String, BlockFluidBase> fluidBlockRegistry = HashBiMap.create();

	private static final ResourceLocation dizzyPotionKey = new ResourceLocation("nausea");

	protected Fluids() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		initDone = true;
	}

	protected static Fluid addFluid(MetalMaterial material, int density, int viscosity, int temperature, int luminosity) {
		int tintColor;
		if (material != null) {
			if (material.fluid != null) {
				return material.fluid;
			}
			tintColor = material.getTintColor();
		} else {
			return null;
		}

		final Fluid fluid = new CustomFluid(material.getName(), new ResourceLocation(BaseMetals.MODID + ":blocks/molten_metal_still"), new ResourceLocation(BaseMetals.MODID + ":blocks/molten_metal_flow"), tintColor);
		fluid.setDensity(density);
		fluid.setViscosity(viscosity);
		fluid.setTemperature(temperature);
		fluid.setLuminosity(luminosity);
		fluid.setUnlocalizedName(Loader.instance().activeModContainer().getModId() + "." + material.getName());
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);

		material.fluid = fluid;

		fluidRegistry.put(material.getName(), fluid);
		return fluid;
	}
/*
	protected static Fluid addFluid(String name, int density, int viscosity, int temperature, int luminosity) {
		MetalMaterial material = Materials.getMaterialByName(name);
		int tintColor;
		if (material != null) {
			if (material.fluid != null) {
				return material.fluid;
			}
			tintColor = material.getTintColor();
		} else {
			tintColor = 0xFFD8D8D8; // Hack for Mercury as it doesn't have a metalMaterial
		}

		final Fluid fluid = new CustomFluid(name, new ResourceLocation(BaseMetals.MODID + ":blocks/molten_metal_still"), new ResourceLocation(BaseMetals.MODID + ":blocks/molten_metal_flow"), tintColor);
		fluid.setDensity(density);
		fluid.setViscosity(viscosity);
		fluid.setTemperature(temperature);
		fluid.setLuminosity(luminosity);
		fluid.setUnlocalizedName(Loader.instance().activeModContainer().getModId() + "." + name);
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);

		if (material != null) {
			material.fluid = fluid;
		}
		fluidRegistry.put(name, fluid);
		return fluid;
	}
*/
	protected static BlockFluidClassic addFluidBlock(@Nonnull MetalMaterial material) {
		if (material == null) {
			return null;
		}

		if (material.fluidBlock != null) {
			return material.fluidBlock;
		}

		final ResourceLocation location = new ResourceLocation(Loader.instance().activeModContainer().getModId(), material.getName());
		BlockFluidClassic block;

		if (material.getName() != "mercury") {
			block = new BlockFluidClassic(material.fluid, Material.LAVA);
		} else {
			block = new InteractiveFluidBlock(getFluidByName(material.getName()), false, (World w, EntityLivingBase e) -> {
				if (w.rand.nextInt(32) == 0) {
					e.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(dizzyPotionKey), 30 * 20, 2));
				}
			});
		}

		String fluidUnlocal = material.fluid.getUnlocalizedName();
		block.setRegistryName(location);
		block.setUnlocalizedName(fluidUnlocal);
		GameRegistry.register(block);
		block.setCreativeTab(CreativeTabs.MISC);

		final ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(location);
		itemBlock.setUnlocalizedName(location.toString());
		GameRegistry.register(itemBlock); 
		material.fluidBlock = block;
		fluidBlockRegistry.put(material.getName(), block);
		return block;
	}
/*
	protected static BlockFluidBase addFluidBlock(String name) {
		MetalMaterial material = Materials.getMaterialByName(name);
		if (material != null) {
			if (material.fluidBlock != null) {
				return material.fluidBlock;
			}
		}

		final ResourceLocation location = new ResourceLocation(Loader.instance().activeModContainer().getModId(), name);
		BlockFluidBase block;

		if ((name != "mercury") && (material != null)) {
			block = new BlockMoltenFluid(material.fluid);
		} else {
			block = new InteractiveFluidBlock(getFluidByName(name), false, (World w, EntityLivingBase e) -> {
				if (w.rand.nextInt(32) == 0) {
					e.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(dizzyPotionKey), 30 * 20, 2));
				}
			});
		}

		block.setRegistryName(location);
		block.setUnlocalizedName(location.toString());
		GameRegistry.register(block);
		block.setCreativeTab(CreativeTabs.MISC);

		final ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(location);
		itemBlock.setUnlocalizedName(location.toString());
		GameRegistry.register(itemBlock);
		if (material != null) {
			material.fluidBlock = block;
		}
		fluidBlockRegistry.put(name, block);
		return block;
	}
*/
	/**
	 * Gets a fluid by its name. The name is the name as it is registered in
	 * the GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "basemetals.")
	 *
	 * @param name The name of the fluid in question
	 * @return The fluid matching that name, or null if there isn't one
	 */
	public static Fluid getFluidByName(String name) {
		return fluidRegistry.get(name);
	}

	/**
	 * This is the reverse of the getFluidByName(...) method, returning the
	 * registered name of an fluid instance (Base Metals fluids only).
	 *
	 * @param b The item in question
	 * @return The name of the item, or null if the item is not a Base Metals
	 * fluid block.
	 */
	public static String getNameOfFluid(Fluid b) {
		return fluidRegistry.inverse().get(b);
	}

	public static Map<String, Fluid> getFluidRegistry() {
		return fluidRegistry;
	}

	/**
	 * Gets a fluid block by its name. The name is the name as it is registered in
	 * the GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "basemetals.")
	 *
	 * @param name The name of the fluid block in question
	 * @return The fluid block matching that name, or null if there isn't one
	 */
	public static BlockFluidBase getFluidBlockByName(String name) {
		return fluidBlockRegistry.get(name);
	}

	/**
	 * This is the reverse of the getFluidBlockByName(...) method, returning the
	 * registered name of an fluid block instance (Base Metals fluid blocks only).
	 *
	 * @param b The item in question
	 * @return The name of the item, or null if the item is not a Base Metals
	 * fluid block.
	 */
	public static String getNameOfFluidBlock(BlockFluidBase b) {
		return fluidBlockRegistry.inverse().get(b);
	}

	public static Map<String, BlockFluidBase> getFluidBlockRegistry() {
		return fluidBlockRegistry;
	}
}
