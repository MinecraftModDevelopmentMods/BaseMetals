package com.mcmoddev.basemetals.integration.plugins;

import c4.conarm.common.armor.traits.ArmorTraits;
import c4.conarm.lib.traits.IArmorTrait;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.ConstructsArmory;
import com.mcmoddev.lib.integration.plugins.tinkers.ArmoryTraitLocation;
import com.mcmoddev.lib.integration.plugins.tinkers.events.MaterialRegistrationEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.MMDTraits;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.tools.TinkerTraits;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = BMeConstructsArmory.PLUGIN_MODID, versions = BMeConstructsArmory.PLUGIN_MODID
        + "@[1.12.2-2.10.1.87,)")
public final class BMeConstructsArmory extends BMeTinkersConstruct {

    public static final String PLUGIN_MODID = ConstructsArmory.PLUGIN_MODID;

    public BMeConstructsArmory() {
        // do nothing
    }

    @Override
    public void init() {
        ConstructsArmory.INSTANCE.init();
        if (!Config.Options.isModEnabled(PLUGIN_MODID)) {
            return;
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void materialRegistration(MaterialRegistrationEvent ev) {
        ev.getRegistry().getEntries().stream()
                .map(ent -> ent.getValue())
                .forEach(mat -> TinkerRegistry.addMaterialStats(mat.getTinkerMaterial(), mat.getCoreStats(), mat.getPlatesStats(), mat.getTrimStats()));

        addArmorTrait(MaterialNames.ADAMANTINE, ArmorTraits.vengeful, ArmorTraits.prideful);
        addArmorTrait(MaterialNames.AQUARIUM, ArmorTraits.rough, ArmorTraits.aquaspeed);
        addArmorTrait(MaterialNames.BRASS, ArmorTraits.dense);
        //addArmorTrait(MaterialNames.COLDIRON, ); // add Freezing trait armor counterpart
        addArmorTrait(MaterialNames.MITHRIL, ArmorTraits.blessed);
        //addArmorTrait(MaterialNames.PEWTER, ); // add Soft trait armor counterpart
        addArmorTrait(MaterialNames.STARSTEEL, MMDTraits.sparkly, ArmorTraits.enderport); // test if sparkly is working properly
    }

    private void addArmorTrait(String mat, AbstractTrait trait1, AbstractTrait trait2){
        TinkerRegistry.getMaterial(mat).addTrait(trait1, ArmoryTraitLocation.CORE.toString());
        TinkerRegistry.getMaterial(mat).addTrait(trait2, ArmoryTraitLocation.PLATES.toString());
        TinkerRegistry.getMaterial(mat).addTrait(trait2, ArmoryTraitLocation.TRIM.toString());
    }

    private void addArmorTrait(String mat, AbstractTrait trait1){
        TinkerRegistry.getMaterial(mat).addTrait(trait1, ArmoryTraitLocation.CORE.toString());
        TinkerRegistry.getMaterial(mat).addTrait(trait1, ArmoryTraitLocation.PLATES.toString());
        TinkerRegistry.getMaterial(mat).addTrait(trait1, ArmoryTraitLocation.TRIM.toString());
    }
}
