package zone.moddev.mc.basemetals.content;

import java.util.function.Consumer;

import net.minecraft.resources.Identifier;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

/** Shared molten-fluid rendering; Forge only requests these extensions on the client. */
public final class MetalFluidType extends FluidType {
    private final Identifier still;
    private final Identifier flowing;
    private final int colour;

    public MetalFluidType(Properties properties, Identifier still, Identifier flowing, int colour) {
        super(properties);
        this.still = still;
        this.flowing = flowing;
        this.colour = 0xFF000000 | colour;
    }

    public int colour() {
        return colour;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override public Identifier getStillTexture() { return still; }
            @Override public Identifier getFlowingTexture() { return flowing; }
            @Override public int getTintColor() { return colour; }
        });
    }
}
