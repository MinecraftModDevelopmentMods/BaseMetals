package com.mcmoddev.lib.recipe.factories;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ConfigVariedOutput implements IRecipeFactory {

	@Override
	public IRecipe parse(JsonContext context, JsonObject json) {
		String confKey = JsonUtils.getString(json, "config_key");
		int resAmount = 0;
		String group = JsonUtils.getString(json, "group", "");
		
		switch( confKey ) {
		case "gear":
			resAmount = Options.gearQuantity();
			break;
		case "plate":
			resAmount = Options.plateQuantity();
			break;
		default:
			BaseMetals.logger.error("Unknown quantity config value {}, setting to 1", confKey );
			resAmount = 1;
		}
		
		// load the data here, map the ingredients, setup the primer and return the ShapedOreRecipe :)
		Map<Character, Ingredient> ingMap = Maps.newHashMap();
		
		JsonUtils.getJsonObject(json, "key").entrySet().stream()
		.filter( ent -> ent.getKey().length() == 1 && !ent.getKey().isEmpty() )
		.forEach( ent -> ingMap.put( ent.getKey().toCharArray()[0], CraftingHelper.getIngredient(ent.getValue(), context)));

        ingMap.put(' ', Ingredient.EMPTY);
        
        JsonArray patternJ = JsonUtils.getJsonArray(json, "pattern");
        if( patternJ.size() == 0 ) {
        	throw new JsonSyntaxException("Invalid pattern: empty pattern not allows");
        }
        
        String[] pattern = new String[patternJ.size()];
        
        for (int x = 0; x < pattern.length; ++x) {
            String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
            if (x > 0 && pattern[0].length() != line.length())
                throw new JsonSyntaxException("Invalid pattern: each row must  be the same width");
            pattern[x] = line;
        }
        
		ShapedPrimer primer = new ShapedPrimer();
        primer.width = pattern[0].length();
        primer.height = pattern.length;
        primer.mirrored = true;
        primer.input = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);
        Set<Character> keys = Sets.newHashSet(ingMap.keySet());
        keys.remove(' ');
        
        int x = 0;
        for( String line : pattern ) {
        	for( char chr : line.toCharArray() ) {
        		Ingredient ing = ingMap.get(chr);
        		if( ing == null ) {
        			throw new JsonSyntaxException("Pattern references symbol '"+chr+"' but it's not defined in the key");
        		}
        		primer.input.set(x++, ing);
        		keys.remove(chr);
        	}
        }
        
        if( !keys.isEmpty() ) {
        	throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: "+keys);
        }
        
        ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
        result.setCount(resAmount);
        return new ShapedOreRecipe(group.isEmpty()?null:new ResourceLocation(group), result, primer);
	}

}
