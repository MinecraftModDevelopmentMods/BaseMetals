package com.mcmoddev.lib.init;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.*;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.block.InteractiveFluidBlock;
import com.mcmoddev.lib.fluids.CustomFluid;
import com.mcmoddev.lib.material.MMDMaterial;

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

	// static {
	// FluidRegistry.enableUniversalBucket();
	// }

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

	protected static Fluid addFluid(MMDMaterial material, int density, int viscosity, int temperature, int luminosity) {
		int tintColor;
		if (material != null) {
			if (material.getFluid() != null) {
				return material.getFluid();
			}
			tintColor = material.getTintColor();
		} else {
			return null;
		}

		final Fluid fluid = new CustomFluid(material.getName(),
				new ResourceLocation(BaseMetals.MODID + ":blocks/molten_metal_still"),
				new ResourceLocation(BaseMetals.MODID + ":blocks/molten_metal_flow"), tintColor);
		fluid.setDensity(density);
		fluid.setViscosity(viscosity);
		fluid.setTemperature(temperature);
		fluid.setLuminosity(luminosity);
		fluid.setUnlocalizedName(Loader.instance().activeModContainer().getModId() + "." + material.getName());
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);

		material.setFluid(fluid);

		fluidRegistry.put(material.getName(), fluid);
		return fluid;
	}

	protected static BlockFluidClassic addFluidBlock(@Nonnull MMDMaterial material) {

		if (material == null) {
			return null;
		}

		if (material.getFluidBlock() != null) {
			return material.getFluidBlock();
		}

		BlockFluidClassic block;
		String name = material.getName();

		if (name != "mercury") {
			block = new BlockFluidClassic(material.getFluid(), Material.LAVA);
		} else {
			block = new InteractiveFluidBlock(getFluidByName(name), false,
					(World w, EntityLivingBase e) -> {
						if (w.rand.nextInt(32) == 0) {
							e.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(dizzyPotionKey), 30 * 20, 2));
						}
					});
		}

		block.setRegistryName(name); // fullName
		block.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + name);
		GameRegistry.register(block);
		block.setCreativeTab(CreativeTabs.MISC);

		final ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(name); // fullName
		itemBlock.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + name);
		GameRegistry.register(itemBlock);

		material.setFluidBlock(block);
		fluidBlockRegistry.put(name, block);
		return block;
	}

	/**
	 * Gets a fluid by its name. The name is the name as it is registered in the
	 * GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "basemetals.")
	 *
	 * @param name
	 *            The name of the fluid in question
	 * @return The fluid matching that name, or null if there isn't one
	 */
	public static Fluid getFluidByName(String name) {
		return fluidRegistry.get(name);
	}

	/**
	 * This is the reverse of the getFluidByName(...) method, returning the
	 * registered name of an fluid instance (Base Metals fluids only).
	 *
	 * @param b
	 *            The item in question
	 * @return The name of the item, or null if the item is not a Base Metals
	 *         fluid block.
	 */
	public static String getNameOfFluid(Fluid b) {
		return fluidRegistry.inverse().get(b);
	}

	public static Map<String, Fluid> getFluidRegistry() {
		return fluidRegistry;
	}

	/**
	 * Gets a fluid block by its name. The name is the name as it is registered
	 * in the GameRegistry, not its unlocalized name (the unlocalized name is
	 * the registered name plus the prefix "basemetals.")
	 *
	 * @param name
	 *            The name of the fluid block in question
	 * @return The fluid block matching that name, or null if there isn't one
	 */
	public static BlockFluidBase getFluidBlockByName(String name) {
		return fluidBlockRegistry.get(name);
	}

	/**
	 * This is the reverse of the getFluidBlockByName(...) method, returning the
	 * registered name of an fluid block instance (Base Metals fluid blocks
	 * only).
	 *
	 * @param b
	 *            The item in question
	 * @return The name of the item, or null if the item is not a Base Metals
	 *         fluid block.
	 */
	public static String getNameOfFluidBlock(BlockFluidBase b) {
		return fluidBlockRegistry.inverse().get(b);
	}

	public static Map<String, BlockFluidBase> getFluidBlockRegistry() {
		return fluidBlockRegistry;
	}
}
