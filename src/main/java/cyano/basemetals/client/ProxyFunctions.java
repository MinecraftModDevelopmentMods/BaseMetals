package cyano.basemetals.client;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * 
 * @author Jasmine Iwanek
 *
 */
public class ProxyFunctions {

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static net.minecraftforge.fml.client.registry.IRenderFactory<?> entityVillagerRenderer() {
		return (IRenderFactory) RenderVillager::new;
	}
}
