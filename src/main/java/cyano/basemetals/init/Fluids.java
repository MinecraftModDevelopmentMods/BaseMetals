package cyano.basemetals.init;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.blocks.InteractiveFluidBlock;
import cyano.basemetals.fluids.CustomFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

/**
 * This class initializes all fluids in Base Metals and provides some utility 
 * methods for looking up fluids. 
 * @author DrCyano
 *
 */
public abstract class Fluids {

	static {
		FluidRegistry.enableUniversalBucket();
	}

	public static Fluid fluidMercury = null;
	public static BlockFluidBase fluidBlockMercury = null;

	private static final Map<Fluid, BlockFluidBase> fluidBlocks = new HashMap<>();
	private static final Map<BlockFluidBase, String> fluidBlockNames = new HashMap<>();

	private static final ResourceLocation dizzyPotionKey = new ResourceLocation("nausea");

	private static boolean initDone = false;

	/**
	 * 
	 */
	public static void init() {
		if(initDone) return;

		// fluids
		fluidMercury = newFluid(BaseMetals.MODID, "mercury", 13594, 2000, 300, 0, 0xFFD8D8D8);

		// fluid blocks
		fluidBlockMercury = registerFluidBlock(fluidMercury, new InteractiveFluidBlock(
				fluidMercury, false, (World w, EntityLivingBase e)->{
					if(w.rand.nextInt(32) == 0) e.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(dizzyPotionKey), 30*20, 2));
				}), "liquid_mercury");

		initDone = true;
	}

	/**
	 * 
	 * @param modID
	 */
	@SideOnly(Side.CLIENT)
	public static void bakeModels(String modID) {
		for(Fluid fluid : fluidBlocks.keySet()) {
			BlockFluidBase block = fluidBlocks.get(fluid);
			Item item = Item.getItemFromBlock(block);
			final ModelResourceLocation fluidModelLocation = new ModelResourceLocation(
					modID.toLowerCase() + ":" + fluidBlockNames.get(block), "fluid");
            ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition()
			{
				public ModelResourceLocation getModelLocation(ItemStack stack)
				{
					return fluidModelLocation;
				}
			});
			ModelLoader.setCustomStateMapper(block, new StateMapperBase()
			{
				protected ModelResourceLocation getModelResourceLocation(IBlockState state)
				{
					return fluidModelLocation;
				}
			});
		}
	}

	private static Fluid newFluid(String modID, String name, int density, int viscosity, int temperature, int luminosity, int tintColor) {
		Fluid fluid = new CustomFluid(name, new ResourceLocation(modID+":blocks/"+name+"_still"), new ResourceLocation(modID+":blocks/"+name+"_flow"), tintColor);
		fluid.setDensity(density);
		fluid.setViscosity(viscosity);
		fluid.setTemperature(temperature);
		fluid.setLuminosity(luminosity);
		fluid.setUnlocalizedName(modID+"."+name);
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
		return fluid;
	}

	private static BlockFluidBase registerFluidBlock(Fluid fluid, BlockFluidBase block, String name) {
		ResourceLocation location = new ResourceLocation(BaseMetals.MODID, name);
		block.setRegistryName(location);
		block.setUnlocalizedName(location.toString());
		GameRegistry.register(block);
		block.setCreativeTab(CreativeTabs.MISC);

		ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(location);
		itemBlock.setUnlocalizedName(location.toString());
		GameRegistry.register(itemBlock);

		fluidBlocks.put(fluid, block);
		fluidBlockNames.put(block, name);
		return block;
	}
}
