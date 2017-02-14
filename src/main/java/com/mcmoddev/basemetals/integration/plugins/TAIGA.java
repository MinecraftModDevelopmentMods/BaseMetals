package com.mcmoddev.basemetals.integration.plugins;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.tinkers.TraitRegistry;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.material.MetalMaterial.MaterialType;
import com.mcmoddev.lib.integration.plugins.taiga.TAIGAItems;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.sosnitzka.taiga.Blocks;
import com.sosnitzka.taiga.Items;

@BaseMetalsPlugin(TAIGA.PLUGIN_MODID)
public class TAIGA extends com.mcmoddev.lib.integration.plugins.TAIGA implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableTinkersConstruct) {
			return;
		}

		TraitRegistry.initTAIGATraits();		
		TAIGAMaterials.init();
		TAIGAItems.init(TAIGAMaterials.materials);

//		TraitRegistry.dumpRegistry();
		
		initDone = true;
	}
	
	private static class TAIGAMaterials extends com.mcmoddev.lib.init.Materials {
		private static Field[] allBlocks = Blocks.class.getDeclaredFields();
		
		public static List<MetalMaterial> materials = new ArrayList<>();
		
		public static void init() {
			try {
				for (Field f : allBlocks) {
					String t = f.getName();

					if (t.endsWith("Block") && t != "basaltBlock") {
						String name = new String(t.substring(0,  t.length() - 5));

						Block k = (Block) f.get(f.getClass());
						float harvestlevel = k.getHarvestLevel(null);
						float resist = k.getExplosionResistance(null);

						MetalMaterial repThis = createOrelessMaterial(name, MaterialType.METAL, harvestlevel * 3.0f, resist / 2.5f, 1.0f, 0x00000000);

						repThis.block = k;
						repThis.ingot = new ItemStack((Item) Items.class.getField(name.toLowerCase() + "Ingot").get(Items.class), 1 ).getItem();
						repThis.powder = new ItemStack((Item) Items.class.getField(name.toLowerCase() + "Dust").get(Items.class), 1).getItem();
						repThis.nugget = new ItemStack((Item) Items.class.getField(name.toLowerCase() + "Nugget").get(Items.class), 1).getItem();

						materials.add(repThis);
					}
				}
			} catch( final Exception ex ) {
				throw new Error(ex.getMessage());
				// do nought
			}
		}
	}
}