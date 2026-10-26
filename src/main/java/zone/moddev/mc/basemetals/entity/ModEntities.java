package zone.moddev.mc.basemetals.entity;

import zone.moddev.mc.basemetals.BaseMetals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            ForgeRegistries.ENTITY_TYPES, BaseMetals.MOD_ID);
    public static final RegistryObject<EntityType<MaterialProjectile>> CUSTOM_ARROW = ENTITIES.register(
            "custom_arrow", () -> EntityType.Builder.<MaterialProjectile>of(MaterialProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20)
                    .build(ENTITIES.key("custom_arrow")));
    public static final RegistryObject<EntityType<MaterialProjectile>> CUSTOM_BOLT = ENTITIES.register(
            "custom_bolt", () -> EntityType.Builder.<MaterialProjectile>of(MaterialProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20)
                    .build(ENTITIES.key("custom_bolt")));

    private ModEntities() {}
    public static void register(BusGroup bus) { ENTITIES.register(bus); }
}
