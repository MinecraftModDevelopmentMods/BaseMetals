package cyano.basemetals.integration.plugins;

import cyano.basemetals.init.Fluids;
import cyano.basemetals.init.Materials;
import cyano.basemetals.integration.BaseMetalsPlugin;
import cyano.basemetals.integration.IIntegration;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.util.Config.Options;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.apache.commons.lang3.StringUtils;
import slimeknights.tconstruct.TinkerIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.ITrait;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@BaseMetalsPlugin(TinkersConstruct.PLUGIN_MODID)
public class TinkersConstruct implements IIntegration {

	public static final String PLUGIN_MODID = "tconstruct";

	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !cyano.basemetals.util.Config.Options.ENABLE_TINKERS_CONSTRUCT) {
			return;
		}

		if (Options.ENABLE_ADAMANTINE) {
			registerMaterial("adamantine", false, true);
		}
		if (Options.ENABLE_ANTIMONY) {
			registerMaterial("antimony", false, true);
		}
		if (Options.ENABLE_AQUARIUM) {
			registerMaterial("aquarium", false, true);
		}
		if (Options.ENABLE_BISMUTH) {
			registerMaterial("bismuth", false, true);
		}
		if (Options.ENABLE_BRASS) {
			registerMaterial("brass", false, true);
		}
		//if (Options.ENABLE_BRONZE) {
		//	 registerMaterial("bronze", false, true);
		//}
		if (Options.ENABLE_COLDIRON) {
			registerMaterial("coldiron", false, true);
		}
		//if (Options.ENABLE_COPPER) {
		//	 registerMaterial("copper", false, true);
		//}
		if (Options.ENABLE_CUPRONICKEL) {
			registerMaterial("cupronickel", false, true);
		}
		//if (Options.ENABLE_ELECTRUM) {
		//	 registerMaterial("electrum", false, true);
		//}
		if (Options.ENABLE_INVAR) {
			registerMaterial("invar", false, true);
		}
		//if (Options.ENABLE_LEAD) {
		//	 registerTinkerMaterial("lead", false, true);
		//}
		//if (Options.ENABLE_MERCURY) {
		//	 registerMaterial("mercury", false, true); // Crashes
		//}
		if (Options.ENABLE_MITHRIL) {
			registerMaterial("mithril", false, true);
		}
		//if (Options.ENABLE_NICKEL) {
		//	 registerMaterial("nickel", false, true);
		//}
		if (Options.ENABLE_PEWTER) {
			registerMaterial("pewter", false, true);
		}
		if (Options.ENABLE_PLATINUM) {
			registerMaterial("platinum", false, true);
		}
		//if (Options.ENABLE_SILVER) {
		// registerMaterial("silver", false, true);
		//}
		if (Options.ENABLE_STARSTEEL) {
			registerMaterial("starsteel", false, true);
		}
		//if (Options.ENABLE_STEEL) {
		// registerMaterial("steel",  Fluids.fluidSteel, false, true);
		//}
		if (Options.ENABLE_TIN) {
			registerMaterial("tin", false, true);
		}
		if (Options.ENABLE_ZINC) {
			registerMaterial("zinc", false, true);
		}

		// registerAlloy("aluminumbrass", 2, new String[] { "aluminum", "brass" }, new int[] { 1, 1 }); // TCon already has Aluminum Brass alloy
		// registerAlloy("galvanizedsteel", 2, new String[] { "steel", "zinc" }, new int[] { 1, 1 });
		// registerAlloy("nichrome", 2, new String[] { "nickel", "chrome" }, new int[] { 1, 1 });
		// registerAlloy("stainlesssteel", 2, new String[] { "steel", "chrome" }, new int[] { 1, 1 });
		// registerAlloy("titanium", 2, new String[] { "rutile", "magnesium" }, new int[] { 1, 1 });

		initDone = true;
	}

	/**
	 *
	 * @param fluid
	 * @param toolforge
	 */
	protected static void registerFluid(Fluid fluid, boolean toolforge) {
		final NBTTagCompound message = new NBTTagCompound();
		message.setString("fluid", fluid.getName());
		message.setString("ore", StringUtils.capitalize(fluid.getName()));
		message.setBoolean("toolforge", toolforge);
		// message.setTag("alloy", alloysTagList); // you can also send an alloy with the registration (see below)

		// send the NBT to TCon
		FMLInterModComms.sendMessage(PLUGIN_MODID, "integrateSmeltery", message);
	}

	/**
	 *
	 * @param outputName
	 * @param outputQty
	 * @param inputName
	 * @param inputQty
	 */
	protected static void registerAlloy(String outputName, int outputQty, String[] inputName, int[] inputQty) {
		if (inputName.length != inputQty.length) {
			throw new RuntimeException("Alloy must have the same amount of inputName and intQty");
		}

		final NBTTagList tagList = new NBTTagList();

		// if you have access to the fluid-instance you can also do FluidStack.writeToNBT
		NBTTagCompound fluid = new NBTTagCompound();
		fluid.setString("FluidName", outputName);
		fluid.setInteger("Amount", outputQty); // 144 = 1 ingot, 16 = 1 nugget
		tagList.appendTag(fluid);

		// alloy fluid
		for (int i = 0; i < inputName.length; i++) {
			fluid = new NBTTagCompound();
			fluid.setString("FluidName", inputName[i]);
			fluid.setInteger("Amount", inputQty[i]);
			tagList.appendTag(fluid);
		}

		final NBTTagCompound message = new NBTTagCompound();
		message.setTag("alloy", tagList);
		FMLInterModComms.sendMessage(PLUGIN_MODID, "alloy", message);
	}

	protected static void registerMaterial(String name, boolean craftable, boolean castable) {
		registerMaterial(name, craftable, castable, null);
	}

	/**
	 * Creates a Tinkers Construct {@link slimeknights.tconstruct.library.materials.Material}
	 * @param name Material identifier
	 * @param craftable If this be crafted
	 * @param castable If this can be casted
	 * @param trait to apply
	 * @return
	 */
	protected static void registerMaterial(String name, boolean craftable, boolean castable, ITrait trait) {

		MetalMaterial material = Materials.getMaterialByName(name);
		Fluid fluid = Fluids.getFluidByName(name);

		if (fluid == null) {
			return;
		}

		int tintColor;
		if (material != null) {
			tintColor = material.getTintColor();
		} else {
			tintColor = 0xFFD8D8D8; // Hack for Mercury as it doesn't have a metalMaterial
		}

		final Material tcmaterial = new Material(name, tintColor);

		HeadMaterialStats headStats = new HeadMaterialStats(material.getToolDurability(), material.magicAffinity * 3 / 2, material.getBaseAttackDamage() * 2, material.getToolHarvestLevel());

		HandleMaterialStats handleStats = new HandleMaterialStats((int) (material.hardness + material.magicAffinity * 2) / 9, (int) (material.getToolDurability() / 6.8));

		ExtraMaterialStats extraStats = new ExtraMaterialStats((material.getToolDurability() / 10));

		registerFluid(fluid, true); // Register the fluid with tinkers

		TinkerRegistry.addMaterialStats(tcmaterial, headStats); // Sets stats for head
		TinkerRegistry.addMaterialStats(tcmaterial, handleStats); // Sets Stats for handle
		TinkerRegistry.addMaterialStats(tcmaterial, extraStats); // Sets stats for everything else

		if (trait != null) {
			String stats = "temporary placeholder"; // TODO: find out what goes here
			TinkerRegistry.addMaterialTrait(tcmaterial, trait, stats);
		}

		// Set fluid used, Set whether craftable, set whether castable, adds the
		// item with the value 144.
		tcmaterial.setFluid(fluid).setCraftable(craftable).setCastable(castable);
		tcmaterial.addItem(material.nugget, 1, Material.VALUE_Nugget);
		tcmaterial.addItem(material.ingot, 1, Material.VALUE_Ingot);
		tcmaterial.setRepresentativeItem(material.ingot); // Use this as the picture

		// TinkerIntegration.integrate(tcmaterial);
		TinkerIntegration.integrate(tcmaterial, fluid, StringUtils.capitalize(fluid.getName()));
	}
}
