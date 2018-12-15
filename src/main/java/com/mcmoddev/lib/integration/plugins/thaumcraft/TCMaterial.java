package com.mcmoddev.lib.integration.plugins.thaumcraft;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.data.NameToken;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.plugins.Thaumcraft;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class TCMaterial extends IForgeRegistryEntry.Impl<TCMaterial> implements IMMDObject {
    private MMDMaterial baseMaterial;
    private Map<NameToken, AspectList> aspectMap;
    private final Map<NameToken, Map<Aspect, IAspectCalculation>> aspectCalcs;
    private final List<Pair<Aspect, IAspectCalculation>> materialAspects;
    private final IAspectGetter aspectGetter;
    
    public TCMaterial(MMDMaterial baseMaterial, IAspectGetter aspectGetter) {
        this.baseMaterial = baseMaterial;
        this.aspectMap = new HashMap<>();
        this.aspectCalcs = new HashMap<>();
        this.aspectGetter = aspectGetter;
        this.materialAspects = aspectGetter.getAspectForPart(BaseAspectGetter.MATERIAL_WIDE);

        Map<Aspect, IAspectCalculation> tempMA = new HashMap<>();
        this.materialAspects.stream()
		.forEach( p -> tempMA.put(p.getKey(), p.getValue()));
  
        this.aspectCalcs.put(BaseAspectGetter.MATERIAL_WIDE, tempMA); 
        this.aspectCalcs.get(BaseAspectGetter.MATERIAL_WIDE).entrySet().stream()
        .forEach(ent -> this.addMaterialAspect(ent.getKey(), ent.getValue()));
        super.setRegistryName(this.baseMaterial.getRegistryName());
    	
    }
    
    public List<NameToken> getAspectMapKeys() {
    	return this.aspectMap.keySet().stream().collect(Collectors.toList());
    }
    
    public AspectList getAspectsFor(Names part) {
    	return getAspectsFor(part.toString());
    }
    
    public AspectList getAspectsFor(String part) {
    	return getAspectsFor(new NameToken(part));
    }
    
    private boolean aspectExists(AspectList aspectList, Aspect aspect) {
    	return aspectList.aspects.containsKey(aspect);
    }
    
    private boolean aspectDoesNotExist(AspectList aspectList, Aspect aspect) {
    	return !aspectExists(aspectList, aspect);
    }
    
    private int applyCalculation(NameToken part, IAspectCalculation calc) {
    	return (int) calc.apply(Thaumcraft.getPartMultiplier(part));
    }
    
    private AspectList getAsAspectList(Aspect aspect, IAspectCalculation calc, NameToken part) {
		AspectList rv = new AspectList();
		int val = applyCalculation(part, calc);
		rv.add(aspect, val);
		this.addAspect(part, aspect, val);
		
		return rv;
	}
    
    private AspectList getAspectList(NameToken part) {
		AspectList rv = new AspectList();
		
		this.aspectCalcs.get(part).entrySet().stream()
		.forEach(kvp -> rv.add(getAsAspectList(kvp.getKey(), kvp.getValue(), part)));
		
		return rv;
    }
    
    private AspectList getAspectListFiltered(NameToken part, Predicate<? super Entry<Aspect, IAspectCalculation>> filter) {
		AspectList rv = new AspectList();

		this.aspectCalcs.get(part).entrySet().stream()
		.filter(filter)
		.forEach(ent -> rv.add(getAsAspectList(ent.getKey(), ent.getValue(), part)));

		return rv;
    }
    
    public AspectList getAspectsFor(NameToken part) {
		AspectList rv = new AspectList();
		
    	if(this.aspectCalcs.containsKey(part)) {
    		rv.add(getAspectList(part));
    		rv.add(getAspectListFiltered(part, ent -> aspectDoesNotExist(this.aspectMap.get(part), ent.getKey())));
    	} else {
    		List<Pair<Aspect, IAspectCalculation>> maybe = this.aspectGetter.getAspectForPart(part);
    		Map<Aspect, IAspectCalculation> b = new HashMap<>();
    		
    		if (!maybe.isEmpty()) {
    			maybe.stream()
    			.forEach(kvp -> {
    				Aspect as = kvp.getKey();
    				rv.add(getAsAspectList(as, kvp.getValue(), part));
    				b.put(as, kvp.getValue());
    			});
    			
        		this.aspectCalcs.get(BaseAspectGetter.MATERIAL_WIDE).entrySet().stream()
        		.filter(ent -> aspectDoesNotExist(this.aspectMap.get(part), ent.getKey()))
        		.forEach(ent ->  {
    				Aspect as = ent.getKey();
    				rv.add(getAsAspectList(as, ent.getValue(), part));
    				b.put(as, ent.getValue());
    			});
    			
    			this.aspectCalcs.put(part, b);
    		}
    	}
    	
    	return rv;
    }

    public void update() {
    	this.materialAspects.stream()
    	.forEach(p -> {
    		this.aspectMap.entrySet().stream()
    		.filter(ent -> !ent.getKey().equals(BaseAspectGetter.MATERIAL_WIDE))
    		.forEach( ent -> ent.getValue().add(p.getKey(), p.getValue().apply(Thaumcraft.getPartMultiplier(ent.getKey()))));
    	});
    	
    	Arrays.asList(Names.values()).stream()
    	.forEach(name -> this.materialAspects.stream()
    		.forEach(p -> this.addAspect(name, p.getKey(), p.getValue().apply(Thaumcraft.getPartMultiplier(name)))));
    }
    
    public TCMaterial addMaterialAspect(Aspect aspect, int amount) {
    	return this.addMaterialAspect(aspect, (m) -> amount);
    }
    
    public TCMaterial addMaterialAspect(Aspect aspect, IAspectCalculation calc) {
    	Map<Aspect, IAspectCalculation> matAsp = this.aspectCalcs.get(BaseAspectGetter.MATERIAL_WIDE);
    	matAsp.put(aspect, calc);
    	
    	this.addAspect(BaseAspectGetter.MATERIAL_WIDE, aspect, calc.apply(1.0f));
    	
    	return this;
    }
    
    public TCMaterial addAspect(NameToken part, Aspect aspect, int amount) {
    	if (this.aspectMap.containsKey(part)) {
    		AspectList al = this.aspectMap.get(part);
    		al.add(aspect, amount);
    		this.aspectMap.put(part, al);
    	}  else {
    		this.aspectMap.put(part, new AspectList().add(aspect, amount));
    	}
    	
    	return this;
    }
    
    public TCMaterial addAspect(Names part, Aspect aspect, int amount){
    	return this.addAspect(part.toString(), aspect, amount);
    }

    public TCMaterial addAspect(String part, Aspect aspect, int amount){
    	return this.addAspect(new NameToken(part), aspect, amount);
    }

    public TCMaterial addAspect(NameToken part, AspectList aspectList) {
    	if (this.aspectMap.containsKey(part)) {
    		AspectList al = this.aspectMap.get(part);
    		al.add(aspectList);
    		this.aspectMap.put(part, al);
    	}  else {
    		this.aspectMap.put(part, aspectList);
    	}
    	
    	return this;
    }
    
    public TCMaterial addAspect(Names part, AspectList aspectList){
    	return this.addAspect(part.toString(), aspectList);
    }

    public TCMaterial addAspect(String part, AspectList aspectList){
    	return this.addAspect(new NameToken(part), aspectList);
    }

    public AspectList getAspectFor(NameToken part) {
        return aspectMap.get(part);    	
    }
    
    public AspectList getAspectFor(String part) {
    	return getAspectFor(new NameToken(part));
    }

    public AspectList getAspectFor(Names part){
        return getAspectFor(part.toString());
    }

    public Item getItem(String item){
        return baseMaterial.getItem(item);
    }

    public ItemStack getItemStack(String item){
        return baseMaterial.getItemStack(item);
    }

    public Block getBlock(String block){
        return  baseMaterial.getBlock(block);
    }

    public ItemStack getBlockItemStack(String block){
        return baseMaterial.getBlockItemStack(block);
    }

    public Item getItem(NameToken item){
        return baseMaterial.getItem(item);
    }

    public ItemStack getItemStack(NameToken item){
        return baseMaterial.getItemStack(item);
    }

    public Block getBlock(NameToken block){
        return  baseMaterial.getBlock(block);
    }

    public ItemStack getBlockItemStack(NameToken block){
        return baseMaterial.getBlockItemStack(block);
    }
    
    public String getName(){
        return this.baseMaterial.getName();
    }

    @Override
    public MMDMaterial getMMDMaterial() {
        return baseMaterial;
    }
}
