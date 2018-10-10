package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.data.TraitNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.ConstructsArmory;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerTraitLocation;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkersMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.events.MaterialRegistrationEvent;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.ADAMANTINE),
                MaterialNames.ADAMANTINE, ev, TraitNames.COLDBLOODED, TraitNames.INSATIABLE);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.ANTIMONY), MaterialNames.ANTIMONY, ev);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.AQUARIUM), MaterialNames.AQUARIUM, ev,
                TraitNames.AQUADYNAMIC, TinkerTraitLocation.HEAD, TraitNames.JAGGED,
                TinkerTraitLocation.HEAD, TraitNames.AQUADYNAMIC);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.BISMUTH), MaterialNames.BISMUTH, ev);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.BRASS), MaterialNames.BRASS, ev, TraitNames.DENSE);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.COLDIRON), MaterialNames.COLDIRON, ev, TraitNames.FREEZING);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.CUPRONICKEL),
                MaterialNames.CUPRONICKEL, ev);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.INVAR), MaterialNames.INVAR, ev);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.MITHRIL), MaterialNames.MITHRIL, ev, TraitNames.HOLY);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.NICKEL), MaterialNames.NICKEL, ev);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.PEWTER), MaterialNames.PEWTER, ev, TraitNames.SOFT);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.PLATINUM), MaterialNames.PLATINUM, ev);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.STARSTEEL),
                MaterialNames.STARSTEEL, ev, TraitNames.ENDERFERENCE,
                TinkerTraitLocation.HEAD, TraitNames.SPARKLY);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.TIN), MaterialNames.TIN, ev);
        registerMaterial(Config.Options.isMaterialEnabled(MaterialNames.ZINC), MaterialNames.ZINC, ev);
    }

    @Override
    protected void registerMaterial(final boolean active, final String name,
                                    MaterialRegistrationEvent ev, final Object... traits) {
        registerMaterial(active, name, true, false, ev, traits);
    }

    @Override
    protected void registerMaterial(final boolean active, final String name, final boolean castable, final boolean craftable,
                                    MaterialRegistrationEvent ev, final Object... traits) {
        if (active) {
            TinkersMaterial mat = new TinkersMaterial(Materials.getMaterialByName(name));
//                    .setCastable(castable).setCraftable(craftable).setToolForge(true);

//            int i = 0;
//
//            while(i < traits.length) {
//                TinkerTraitLocation loc;
//                String trait;
//                Object item = traits[i];
//                if(item instanceof TinkerTraitLocation) {
//                    loc = (TinkerTraitLocation)item;
//                    trait = ((String)traits[++i]).toLowerCase(Locale.US);
//
//                    mat.addTrait(trait, loc);
//                } else {
//                    trait = ((String)item).toLowerCase(Locale.US);
//                    mat.addTrait(trait);
//                }
//                i++;
//            }

            ev.getRegistry().register(mat.create());
        }
    }
}
