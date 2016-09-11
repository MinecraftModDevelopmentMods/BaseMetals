package cyano.basemetals.init;

import com.google.common.collect.Lists;
import cyano.basemetals.utils.StringUtilities;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

import java.util.List;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class TinkersConstructPlugin {

	private static final String TCONSTRUCT_MODID = "tconstruct";

	private static boolean initDone = false;

	/**
	 *
	 */
	@SuppressWarnings("unused")
	public static void init() {
		if(initDone)
			return;

		if(Loader.isModLoaded(TCONSTRUCT_MODID)) {
			final double d = 0; // durabilityFactorGeneral;
//			System.out.println("DURABILITY FACTOR" + d);
			final float s = 0;// (float) speedFactorGeneral;
//			System.out.println("SPEED FACTOR" + s);
			final float a = 0; // (float) attackFactorGeneral;
//			System.out.println("ATTACK FACTOR" + a);

//			final Material aluminum = new Material("aluminum", 0xFFC5C8C1);

			// registerFluid(Fluids.fluidAluminum); // TCon already has this
			// registerTinkerMaterial(aluminum, Fluids.fluidAluminum, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1, false, true);
			// registerFluid(Fluids.fluidAluminumBrass); // TCon already has this
			// registerTinkerMaterial(aluminumbrass, Fluids.fluidAluminumBrass, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1, false, true);

			// registerAlloy("aluminumbrass", 2, new String[] { "aluminum", 1, "brass" }, new int[] { 1, 1 }); // TCon already has Aluminum Brass alloy
			registerAlloy("galvanizedsteel", 2, new String[] { "steel", "zinc" }, new int[] { 1, 1 });
			registerAlloy("nichrome", 2, new String[] { "nickel", "chrome" }, new int[] { 1, 1 });
			registerAlloy("stainlesssteel", 2, new String[] { "steel", "chrome" }, new int[] { 1, 1 });
			registerAlloy("titanium", 2, new String[] { "rutile", "magnesium" }, new int[] { 1, 1 });
		}

		initDone = true;
	}

	/**
	 *
	 * @param fluid
	 * @param toolforge
	 */
	public static void registerFluid(Fluid fluid, boolean toolforge) {
		if(Loader.isModLoaded(TCONSTRUCT_MODID)) {
			final NBTTagCompound message = new NBTTagCompound();
			message.setString("fluid", fluid.getName());
			message.setString("ore", StringUtilities.upperCaseFirst(fluid.getName()));
			message.setBoolean("toolforge", toolforge);
			//message.setTag("alloy", alloysTagList); // you can also send an alloy with the registration (see below)

			// send the NBT to TCon
			FMLInterModComms.sendMessage(TCONSTRUCT_MODID, "integrateSmeltery", message);
		}
	}

	/**
	 *
	 * @param outputName
	 * @param outputQty
	 * @param inputName
	 * @param inputQty
	 */
	public static void registerAlloy(String outputName, int outputQty, String[] inputName, int[] inputQty)
	{
		if(Loader.isModLoaded(TCONSTRUCT_MODID)) {
			if(inputName.length != inputQty.length)
				throw new RuntimeException("Alloy must have the same amount of inputName and intQty");

			final NBTTagList tagList = new NBTTagList();

			// if you have access to the fluid-instance you can also do FluidStack.writeToNBT
			NBTTagCompound fluid = new NBTTagCompound();
			fluid.setString("FluidName", outputName);
			fluid.setInteger("Amount", outputQty); // 144 = 1 ingot, 16 = 1 nugget
			tagList.appendTag(fluid);

			// alloy fluid
			for(int i = 0; i < inputName.length; i++) {
				fluid = new NBTTagCompound();
				fluid.setString("FluidName", inputName[i]);
				fluid.setInteger("Amount", inputQty[i]);
				tagList.appendTag(fluid);
			}

			NBTTagCompound message = new NBTTagCompound();
			message.setTag("alloy", tagList);
			FMLInterModComms.sendMessage(TCONSTRUCT_MODID, "alloy", message);
		}
	}

	private static void registerTinkerMaterial(Material material, Fluid fluid, int headDura, float headSpeed, float headAttack, float handleMod, int handleDura, int extra, int headLevel, boolean craft, boolean cast) {

		final List<MaterialIntegration> integrateList = Lists.newArrayList(); // List of materials needed to be integrated

		TinkerRegistry.addMaterialStats(material, new HeadMaterialStats(headDura, headSpeed, headAttack, headLevel)); // Sets stats for head
		TinkerRegistry.addMaterialStats(material, new HandleMaterialStats(handleMod, handleDura)); // Sets Stats for handle
		TinkerRegistry.addMaterialStats(material, new ExtraMaterialStats(extra)); // Sets stats for everything else

		final Item item = Items.getItemByName(material.identifier + "_ingot"); // Why do we need to get an item here?
		
		// Set fluid used, Set whether craftable, set whether castable, adds the
		// item with the value 144.
		//material.setFluid(fluid).setCraftable(craft).setCastable(cast).addItem(item, 1, Material.VALUE_Ingot); TODO
		material.setRepresentativeItem(item); // Uses item as the picture?

		// Probably don't need this
		//proxy.setRenderInfo(material);
		final MaterialIntegration integration = new MaterialIntegration(material, fluid, StringUtilities.upperCaseFirst(fluid.getName()));
		integration.integrate();
		integrateList.add(integration);
	}
}