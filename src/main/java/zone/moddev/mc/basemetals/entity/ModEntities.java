package zone.moddev.mc.basemetals.entity;

import zone.moddev.mc.basemetals.BaseMetals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            Registries.ENTITY_TYPE, BaseMetals.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<MaterialProjectile>> CUSTOM_ARROW = ENTITIES.register(
            "custom_arrow", () -> EntityType.Builder.<MaterialProjectile>of(MaterialProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20)
                    .build(BaseMetals.MOD_ID + ":custom_arrow"));
    public static final DeferredHolder<EntityType<?>, EntityType<MaterialProjectile>> CUSTOM_BOLT = ENTITIES.register(
            "custom_bolt", () -> EntityType.Builder.<MaterialProjectile>of(MaterialProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20)
                    .build(BaseMetals.MOD_ID + ":custom_bolt"));

    private ModEntities() {}
    public static void register(IEventBus bus) { ENTITIES.register(bus); }
}
