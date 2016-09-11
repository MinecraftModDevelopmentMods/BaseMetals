package cyano.basemetals.asm;

import cyano.basemetals.asm.transformer.EntityHorseTransformer;
import cyano.basemetals.asm.transformer.HorseTypeTransformer;
import cyano.basemetals.asm.transformer.ITransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.Name("BaseMetals")
@IFMLLoadingPlugin.MCVersion("1.9")
@IFMLLoadingPlugin.SortingIndex(1001)
public class BaseMetalPlugin implements IFMLLoadingPlugin {

	static List<ITransformer> transformerList = new ArrayList<>();
	static boolean inDevelopment;

	public BaseMetalPlugin() {
		transformerList.add(new EntityHorseTransformer());
		transformerList.add(new HorseTypeTransformer());
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"cyano.basemetals.asm.ModernMetalTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		inDevelopment = !(Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
