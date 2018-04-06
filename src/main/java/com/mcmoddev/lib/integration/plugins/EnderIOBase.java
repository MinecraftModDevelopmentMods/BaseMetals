package com.mcmoddev.lib.integration.plugins;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.crash.CrashReport;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.tuple.Triple;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class EnderIOBase implements IIntegration {

	public static final String PLUGIN_MODID = "enderio";

	/**
	 *
	 */
	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 */
	protected static void addAlloySmelterRecipe(@Nonnull final String materialName) {
		addAlloySmelterRecipe(Materials.getMaterialByName(materialName), null, 2000);
	}

	/**
	 *
	 * @param material
	 *            The Material
	 */
	protected static void addAlloySmelterRecipe(@Nonnull final MMDMaterial material) {
		addAlloySmelterRecipe(material, null, 2000);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(@Nonnull final String materialName,
			@Nonnull final int energy) {
		addAlloySmelterRecipe(Materials.getMaterialByName(materialName), null, energy);
	}

	/**
	 *
	 * @param material
	 *            The Material
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(@Nonnull final MMDMaterial material,
			@Nonnull final int energy) {
		addAlloySmelterRecipe(material, null, energy);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(@Nonnull final String materialName,
			final String outputSecondary, @Nonnull final int energy) {
		addAlloySmelterRecipe(Materials.getMaterialByName(materialName), outputSecondary, energy);
	}

	private static final void addRecipeIMC(@Nonnull final String recipeType, @Nonnull final String recipeName, @Nonnull int energy, final List<Triple<String,Integer,Float>> recipe) {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = fac.newDocumentBuilder();
			Document rec = builder.newDocument();
			
			Element root = rec.createElement("recipe");
			Element base = rec.createElement(recipeType);
			
			root.setAttribute("name", recipeName);
			root.setAttribute("required", "false");
			
			base.setAttribute("energy", String.format("%d", energy));
			
			List<Element> elems = new ArrayList<>();
			
			recipe.stream()
			.filter(ing -> ing.getRight() == 0f)
			.forEach(ing -> {
				Element out = rec.createElement("output");
				out.setAttribute("name", ing.getLeft());
				out.setAttribute("amount", ing.getMiddle().toString());
				elems.add(out);
			});

			recipe.stream()
			.filter(ing -> ing.getRight() > 0f)
			.forEach(ing -> {
				Element out = rec.createElement("output");
				if(ing.getRight() != 1f) {
					out.setAttribute("chance", ing.getRight().toString());
				}
				out.setAttribute("name", ing.getLeft());
				out.setAttribute("amount", ing.getMiddle().toString());
				elems.add(out);
			});

			elems.stream().forEach(base::appendChild);
			
			rec.appendChild(base);
			root.appendChild(rec);
			
	        Transformer tf = TransformerFactory.newInstance().newTransformer();
	        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        tf.setOutputProperty(OutputKeys.INDENT, "yes");
	        Writer out = new StringWriter();
	        tf.transform(new DOMSource(root), new StreamResult(out));
			FMLInterModComms.sendMessage(PLUGIN_MODID, "recipe:xml", out.toString());
		} catch (ParserConfigurationException | TransformerFactoryConfigurationError | TransformerException e) {
			CrashReport report = CrashReport.makeCrashReport(e, "Error building XML for EnderIO IMC");
			report.getCategory().addCrashSection("BaseMetals Version", BaseMetals.getVersion());
			BaseMetals.logger.error(report.getCompleteReport());
		}		
	}
	
	/**
	 * 
	 * @param material   Only used for naming the recipe in the IMC
	 * @param energy     How much RF/How many "micro-infinities" does this take ?
	 * @param outputItem What does this produce? (should be an ore-dictionary entry but can be a string-form of a ResourceLocation
	 * @param outputQty  How many does it produce
	 * @param recipe     Array containing the inputs, in the form of:
	 *                   new Object[] { "name", quantity }
	 *                   - "chance" for input ingredients is "0f" so its left off here
	 *                   - alloy smelter doesn't do secondary outputs
	 */
	protected static void addAlloySmelterAlloy(@Nonnull final MMDMaterial material, final int energy,
			@Nonnull final String outputItem, @Nonnull final int outputQty, @Nonnull Object[] recipe) {
		List<Triple<String,Integer,Float>> rec = Arrays.asList(Triple.of(outputItem, outputQty, 1.0f));
		
		int i = 0;
		while(i < (recipe.length - 2)) {
			rec.add(Triple.of((String)recipe[i++], Integer.valueOf((int)recipe[i++]), 0f));
		}
		
		final String ownerModID = Loader.instance().activeModContainer().getModId();

		final String capitalizedName = material.getCapitalizedName();
		
		addRecipeIMC("alloying", String.format("%s: alloy recipe for %s", ownerModID, capitalizedName), energy, rec);
	}
	
	/**
	 *
	 * @param material
	 *            The Material
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(@Nonnull final MMDMaterial material,
			final String outputSecondary, @Nonnull final int energy) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();

		final String capitalizedName = material.getCapitalizedName();

		final String input = Oredicts.ORE + capitalizedName;
		final String output = Oredicts.INGOT + capitalizedName;

		if (!(material.hasOre())) {
			return; // Only run for Ore types
		}
		
		List<Triple<String,Integer,Float>> rec = Arrays.asList( Triple.of(input, 1, 0f), Triple.of(output, 2, 1.0f) );
		if(outputSecondary != null) {
			rec.add( Triple.of(outputSecondary, 1, 0.5f));
		}
		addRecipeIMC("alloying", String.format("%s: %s to %s", ownerModID, input, output), energy, rec);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 */
	protected static void addSagMillRecipe(@Nonnull final String materialName) {
		addSagMillRecipe(Materials.getMaterialByName(materialName), null, 2400);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(@Nonnull final String materialName,
			@Nonnull final int energy) {
		addSagMillRecipe(Materials.getMaterialByName(materialName), null, energy);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(@Nonnull final String materialName,
			final String outputSecondary, @Nonnull final int energy) {
		addSagMillRecipe(Materials.getMaterialByName(materialName), outputSecondary, energy);
	}

	/**
	 *
	 * @param material
	 *            The Material
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(@Nonnull final MMDMaterial material,
			final String outputSecondary, @Nonnull final int energy) {
		int primaryQty = 2;
		final int secondaryQty = 1;

		if (material.getType() == MaterialType.MINERAL) {
			primaryQty = 4;
		}

		addSagMillRecipe(material, primaryQty, outputSecondary, secondaryQty, energy);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param primaryQty
	 *            How much to make
	 * @param outputSecondary
	 *            The secondary output
	 * @param secondaryQty
	 *            How much to make
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(@Nonnull final String materialName,
			@Nonnull final int primaryQty, @Nonnull final String outputSecondary,
			@Nonnull final int secondaryQty, @Nonnull final int energy) {
		addSagMillRecipe(Materials.getMaterialByName(materialName), primaryQty, outputSecondary,
				secondaryQty, energy);
	}

	/**
	 *
	 * @param material
	 *            The Material
	 * @param primaryQty
	 *            How much to make
	 * @param outputSecondary
	 *            The secondary output
	 * @param secondaryQty
	 *            How much to make
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(@Nonnull final MMDMaterial material,
			@Nonnull final int primaryQty, final String outputSecondary,
			@Nonnull final int secondaryQty, @Nonnull final int energy) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();

		final String capitalizedName = material.getCapitalizedName();

		final String input = Oredicts.ORE + capitalizedName;
		
		String primaryOutput = Oredicts.DUST + capitalizedName;
		String secondaryOutput = Oredicts.DUST;
		
		if (material.getType() == MaterialType.GEM) {
			primaryOutput = Oredicts.GEM + capitalizedName;
			secondaryOutput = Oredicts.GEM;
		}
		
		List<Triple<String,Integer,Float>> rec = Arrays.asList( Triple.of(input, 1, 0f), Triple.of(primaryOutput, primaryQty, 1.0f) );
		if( outputSecondary != null) {
			secondaryOutput += outputSecondary;
			rec.add(Triple.of(secondaryOutput, secondaryQty, 0.1f));
		}
		
		rec.add(Triple.of("minecraft:cobblestone", 1, 0.15f));
		
		addRecipeIMC("sagmilling", String.format("%s: %s to %s", ownerModID, input, primaryOutput), energy, rec);
	}
}
