package review.fantasy;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

@Mod(modid = "basemetalsfantasyprobe", name = "Base Metals Fantasy Level checks", version = "1",
		serverSideOnly = true, dependencies = "required-after:basemetals")
public final class FantasyProbe {
	@Mod.EventHandler
	public void started(final FMLServerStartedEvent event) throws Exception {
		if (!Boolean.getBoolean("fantasy.serverProbe")) {
			return;
		}

		final Set<String> registered = new TreeSet<>();
		ForgeRegistries.ITEMS.forEach(item -> registered.add(item.getRegistryName().toString()));
		ForgeRegistries.BLOCKS.forEach(block -> registered.add(block.getRegistryName().toString()));
		final Map<String, Integer> crafting = new TreeMap<>();
		CraftingManager.getInstance().getRecipeList().forEach(recipe -> count(crafting, recipe.getRecipeOutput()));
		final Map<String, Integer> smelting = new TreeMap<>();
		FurnaceRecipes.instance().getSmeltingList().values().forEach(stack -> count(smelting, stack));
		final Map<String, Integer> crushing = new TreeMap<>();
		CrusherRecipeRegistry.getInstance().getAllRecipes().forEach(recipe -> count(crushing, recipe.getOutput()));

		final GsonBuilder gson = new GsonBuilder().setPrettyPrinting();
		final JsonObject result = new JsonObject();
		result.addProperty("passed", true);
		result.add("registry", gson.create().toJsonTree(registered));
		result.add("crafting", gson.create().toJsonTree(crafting));
		result.add("smelting", gson.create().toJsonTree(smelting));
		result.add("crushing", gson.create().toJsonTree(crushing));
		result.add("trades", gson.create().toJsonTree(smithTrades()));
		if (Loader.isModLoaded("tconstruct")) {
			final Class<?> registry = Class.forName("slimeknights.tconstruct.library.TinkerRegistry");
			final Object material = registry.getMethod("getMaterial", String.class).invoke(null, "adamantine");
			result.addProperty("tinkersBow", (Boolean) material.getClass()
					.getMethod("hasStats", String.class).invoke(material, "bow"));
			result.addProperty("tinkersHead", (Boolean) material.getClass()
					.getMethod("hasStats", String.class).invoke(material, "head"));
		}
		final String phase = System.getProperty("fantasy.phase", "first");
		Files.write(Paths.get(phase + "-fantasy.json"), gson.create().toJson(result).getBytes(StandardCharsets.UTF_8));
		FMLCommonHandler.instance().getMinecraftServerInstance().initiateShutdown();
	}

	private static Map<String, Integer> smithTrades() {
		final VillagerProfession smith = VillagerRegistry.instance().getRegistry()
				.getValue(new ResourceLocation("minecraft:smith"));
		final Map<String, Integer> results = new TreeMap<>();
		for (int career = 0; career < 3; career++) {
			for (int level = 1; level <= 10; level++) {
				final List<ITradeList> trades = smith.getCareer(career).getTrades(level);
				if (trades == null) {
					continue;
				}
				for (final ITradeList trade : trades) {
					final MerchantRecipeList recipes = new MerchantRecipeList();
					trade.addMerchantRecipe(null, recipes, new Random(8675309));
					recipes.forEach(recipe -> count(results, recipe.getItemToSell()));
				}
			}
		}
		return results;
	}

	private static void count(final Map<String, Integer> counts, final ItemStack stack) {
		if (stack != null && !stack.isEmpty() && stack.getItem().getRegistryName() != null) {
			counts.merge(stack.getItem().getRegistryName().toString(), 1, Integer::sum);
		}
	}
}
