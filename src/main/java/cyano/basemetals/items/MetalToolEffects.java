package cyano.basemetals.items;

import cyano.basemetals.init.Materials;
import cyano.basemetals.material.MetalMaterial;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public abstract class MetalToolEffects {

	public static void extraEffectsOnAttack(final MetalMaterial metal, final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker){
    	if(metal.equals(Materials.coldiron)){
    		if(target.isImmuneToFire()){
    			DamageSource extraDamage = DamageSource.generic; 
    			target.attackEntityFrom(extraDamage, 3f);
    		}
    	} else if(metal.equals(Materials.adamantine)){
    		if(target.getMaxHealth() > 20f){
    			DamageSource extraDamage = DamageSource.generic; 
    			target.attackEntityFrom(extraDamage, 4f);
    		}
    	} else if(metal.equals(Materials.mithril)){
    		if(target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD){
                final ResourceLocation witherKey = new ResourceLocation("wither");
                final ResourceLocation blindKey = new ResourceLocation("wither");
    			final PotionEffect wither = new PotionEffect(Potion.REGISTRY.getObject(witherKey),60,3);
    			final PotionEffect blind = new PotionEffect(Potion.REGISTRY.getObject(blindKey),60,1);
    			target.addPotionEffect(wither);
    			target.addPotionEffect(blind);
    		}
    	} else if(metal.equals(Materials.aquarium)){
    		if(target.canBreatheUnderwater()){
    			DamageSource extraDamage = DamageSource.generic; 
    			target.attackEntityFrom(extraDamage, 4f);
    		}
    	}
    }
	
	public static void extraEffectsOnCrafting(final MetalMaterial metal, final ItemStack item, final World world, final EntityPlayer crafter){
		// do nothing for now. This would be where achievements or automatic enchantments could appear
	}
	
	public static void addToolSpecialPropertiesToolTip(MetalMaterial metal, java.util.List<String> tooltipList){
		if(metal == Materials.adamantine){
			tooltipList.add(I18n.translateToLocal("tooltip.adamantine.tool").replace("%x", String.valueOf(4)));
		} else if(metal == Materials.aquarium){
			tooltipList.add(I18n.translateToLocal("tooltip.aquarium.tool").replace("%x", String.valueOf(4)));
		} else if(metal == Materials.coldiron){
			tooltipList.add(I18n.translateToLocal("tooltip.coldiron.tool").replace("%x", String.valueOf(3)));
		} else if(metal == Materials.mithril){
			tooltipList.add(I18n.translateToLocal("tooltip.mithril.tool"));
		} else if(metal == Materials.starsteel){
			tooltipList.add(I18n.translateToLocal("tooltip.starsteel.tool").replace("%x", String.valueOf(10)));
		}
	}

	public static void addArmorSpecialPropertiesToolTip(MetalMaterial metal, java.util.List<String> tooltipList){
		if(metal == Materials.adamantine){
			tooltipList.add(I18n.translateToLocal("tooltip.adamantine.armor").replace("%x", String.valueOf(4)));
		} else if(metal == Materials.aquarium){
			tooltipList.add(I18n.translateToLocal("tooltip.aquarium.armor").replace("%x", String.valueOf(4)));
		} else if(metal == Materials.coldiron){
			tooltipList.add(I18n.translateToLocal("tooltip.coldiron.armor").replace("%x", String.valueOf(3)));
		} else if(metal == Materials.mithril){
			tooltipList.add(I18n.translateToLocal("tooltip.mithril.armor"));
		} else if(metal == Materials.starsteel){
			tooltipList.add(I18n.translateToLocal("tooltip.starsteel.armor").replace("%x", String.valueOf(10)));
		}
	}
}
