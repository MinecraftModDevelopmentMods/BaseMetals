package cyano.basemetals.client;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;

public class ProxyFunctions {

	@SuppressWarnings("rawtypes")
	public static net.minecraftforge.fml.client.registry.IRenderFactory<?> entityVillagerRenderer() {
		return new net.minecraftforge.fml.client.registry.IRenderFactory() {
			@Override
			public Render<?> createRenderFor(RenderManager manager) {
				return new net.minecraft.client.renderer.entity.RenderVillager(manager);
			}
		};
	}
}
