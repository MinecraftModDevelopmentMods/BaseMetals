package zone.moddev.mc.basemetals.content;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;

/** Shared molten-fluid rendering; Forge only requests these extensions on the client. */
public final class MetalFluidType extends FluidType {
    private final ResourceLocation still;
    private final ResourceLocation flowing;
    private final int colour;

    public MetalFluidType(Properties properties, ResourceLocation still, ResourceLocation flowing, int colour) {
        super(properties);
        this.still = still;
        this.flowing = flowing;
        this.colour = 0xFF000000 | colour;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override public ResourceLocation getStillTexture() { return still; }
            @Override public ResourceLocation getFlowingTexture() { return flowing; }
            @Override public int getTintColor() { return colour; }
        });
    }
}
