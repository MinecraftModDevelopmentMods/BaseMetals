package zone.moddev.mc.basemetals.testmod;

import java.util.*;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.state.IProperty;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ForgeRegistries;

/** Compare all connected bars against the target game's own iron bars. */
final class BarModelChecks {
    static int run(Minecraft mc,String namespace){
        int checks=0,materials=0;
        for(Block block:ForgeRegistries.BLOCKS.getValues()){
            if(!block.getRegistryName().getNamespace().equals(namespace)||!block.getRegistryName().getPath().endsWith("_bars"))continue;
            materials++;
            for(BlockState state:block.getStateContainer().getValidStates()){
                BlockState vanilla=Blocks.IRON_BARS.getDefaultState();
                for(IProperty property:vanilla.getProperties()){
                    IProperty actual=state.getBlock().getStateContainer().getProperty(property.getName());
                    if(actual!=null)vanilla=vanilla.with(property,state.get(actual));
                }
                Map<String,Integer> expected=geometry(mc,vanilla),actual=geometry(mc,state);
                if(!actual.equals(expected))throw new IllegalStateException("Bars geometry differs from vanilla: "+state+" expected "+expected.size()+" faces, actual "+actual.size());
                checks++;
            }
        }
        if(materials==0)throw new IllegalStateException("No bars checked for "+namespace);
        System.out.println("BARS_MODEL_CHECK PASS mod="+namespace+" materials="+materials+" states="+checks);
        return checks;
    }
    private static Map<String,Integer> geometry(Minecraft mc,BlockState state){
        IBakedModel model=mc.getBlockRendererDispatcher().getModelForState(state);
        if(model==mc.getModelManager().getMissingModel())throw new IllegalStateException("Missing bars model "+state);
        List<BakedQuad> quads=new ArrayList<>(model.getQuads(state,null,new Random(42)));
        for(Direction side:Direction.values())quads.addAll(model.getQuads(state,side,new Random(42)));
        Map<String,Integer> result=new TreeMap<>();
        for(BakedQuad quad:quads){
            if(quad.getSprite().getName().getPath().equals("missingno"))throw new IllegalStateException("Missing bars sprite "+state);
            int[] data=quad.getVertexData();int stride=data.length/4;List<String> vertices=new ArrayList<>();
            for(int v=0;v<4;v++)vertices.add(Math.round(Float.intBitsToFloat(data[v*stride])*1000000)+","+Math.round(Float.intBitsToFloat(data[v*stride+1])*1000000)+","+Math.round(Float.intBitsToFloat(data[v*stride+2])*1000000));
            Collections.sort(vertices);String key=quad.getFace()+vertices.toString();result.merge(key,1,Integer::sum);
        }
        return result;
    }
}
