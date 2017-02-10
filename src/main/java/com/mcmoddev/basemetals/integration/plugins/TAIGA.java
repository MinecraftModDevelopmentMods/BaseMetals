package com.mcmoddev.basemetals.integration.plugins;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.tinkers.TraitRegistry;
import com.mcmoddev.lib.material.MetalMaterial;

import com.mcmoddev.lib.integration.plugins.taiga.TAIGAItems;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import com.sosnitzka.taiga.Blocks;
import com.sosnitzka.taiga.Items;

@BaseMetalsPlugin(TAIGA.PLUGIN_MODID)
public class TAIGA extends com.mcmoddev.lib.integration.plugins.TAIGA implements IIntegration {
	private static boolean initDone = false;

	private static List<MetalMaterial> myMats = new ArrayList<>();
	
	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableTinkersConstruct) {
			return;
		}
		
		TraitRegistry.initTAIGATraits();
		genMetalMaterialsForBlock();
		TAIGAItems.init(myMats);
		
		initDone = true;
	}
	
	private static void genMetalMaterialsForBlock() {
		try {
			Field[] allBlocks = Blocks.class.getDeclaredFields();
			Field[] allItems = Items.class.getDeclaredFields();
			Map<String, Item[]> actualItems = new HashMap<>();
			
			for( Field i : allItems ) {
				String t = i.getName();
				if( !t.startsWith("iron") ) {
					String name = t.replace("(?:Ingot|Dust|Nugget)", "");
					if( !actualItems.containsKey(name) ) {
						Item[] theseItems = new Item[3];
						theseItems[0] = (Item)Items.class.getField(name+"Ingot").get(Items.class);
						theseItems[1] = (Item)Items.class.getField(name+"Dust").get(Items.class);
						theseItems[2] = (Item)Items.class.getField(name+"Nugget").get(Items.class);
						actualItems.put(name, theseItems);
					}
				}
			}
			
			for( Field f : allBlocks ) {
				String t = f.getName();
				if( t.endsWith("Block") && !t.startsWith("basalt") ) {
					String name = new String(t.substring(0,  t.length() - 5));
					name = name.substring(0,1).toUpperCase() + name.substring(1);

					Block k = (Block) f.get(f.getClass());
					float harvestlevel = k.getHarvestLevel(null);
					float resist = k.getExplosionResistance(null);
					
					@SuppressWarnings("deprecation")
					MetalMaterial repThis = new MetalMaterial(name,harvestlevel*3.0f,resist/2.5f,1.0f,false);
					
					repThis.block = k;
					repThis.ingot = actualItems.get(name.toLowerCase())[0];
					repThis.powder = actualItems.get(name.toLowerCase())[1];
					repThis.nugget = actualItems.get(name.toLowerCase())[2];
					
					myMats.add(repThis);
				}
			}
		} catch( final Exception e ) {
			// do nought
		}
	}
}
