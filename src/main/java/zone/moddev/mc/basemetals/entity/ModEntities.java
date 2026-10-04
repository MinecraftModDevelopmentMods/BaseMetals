package zone.moddev.mc.basemetals.entity;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.content.RegistryHandle;

import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntities {
    public static final RegistryHandle<EntityType<MaterialProjectile>> CUSTOM_ARROW =
            new RegistryHandle<EntityType<MaterialProjectile>>("custom_arrow");
    public static final RegistryHandle<EntityType<MaterialProjectile>> CUSTOM_BOLT =
            new RegistryHandle<EntityType<MaterialProjectile>>("custom_bolt");

    private ModEntities() {}

    public static void initialize() {
        // Register the event subscriber before item factories ask for these entities.
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        CUSTOM_ARROW.bind(register(event, "custom_arrow", CUSTOM_ARROW));
        CUSTOM_BOLT.bind(register(event, "custom_bolt", CUSTOM_BOLT));
    }

    private static EntityType<MaterialProjectile> register(RegistryEvent.Register<EntityType<?>> event,
            String name, final RegistryHandle<EntityType<MaterialProjectile>> self) {
        EntityType<MaterialProjectile> type = EntityType.Builder
                .create(MaterialProjectile.class, world -> new MaterialProjectile(self.get(), world))
                .tracker(64, 20, true)
                .build(BaseMetals.MOD_ID + ":" + name);
        type.setRegistryName(BaseMetals.MOD_ID, name);
        event.getRegistry().register(type);
        return type;
    }
}
