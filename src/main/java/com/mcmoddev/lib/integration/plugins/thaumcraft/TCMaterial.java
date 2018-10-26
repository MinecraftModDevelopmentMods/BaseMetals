package com.mcmoddev.lib.integration.plugins.thaumcraft;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.data.NameToken;
import com.mcmoddev.lib.data.Names;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class TCMaterial extends IForgeRegistryEntry.Impl<TCMaterial> implements IMMDObject {
    private MMDMaterial baseMaterial;
    private Map<String, AspectList> aspectMap;
    private final Map<NameToken, Map<Aspect, IAspectCalculation>> aspectCalcs;
    private final List<Pair<Aspect, IAspectCalculation>> materialAspects;
    
    public TCMaterial(MMDMaterial baseMaterial) {
        this.baseMaterial = baseMaterial;
        this.aspectMap = new TreeMap<>();
        this.aspectCalcs = new HashMap<>();
        this.materialAspects = new LinkedList<>();
        
        super.setRegistryName(this.baseMaterial.getRegistryName());
    }

    public Set<String> getAspectMapKeys(){
        return aspectMap.keySet();
    }

    public void addAspectCalculation(Names part, Aspect aspect, IAspectCalculation calculationFunc) {
    	this.addAspectCalculation(part.toString(), aspect, calculationFunc);
    }
    
    public void addAspectCalculation(String part, Aspect aspect, IAspectCalculation calculationFunc) {
    	this.addAspectCalculation(new NameToken(part), aspect, calculationFunc);
    }
    
    public void addAspectCalculation(NameToken part, Aspect aspect, IAspectCalculation calculationFunc) {
    	Map<Aspect, IAspectCalculation> vals = this.aspectCalcs.getOrDefault(part, new TreeMap<Aspect, IAspectCalculation>());
    	vals.put(aspect, calculationFunc);
    	this.aspectCalcs.put(part, vals);
    }

    public void addMaterialAspect(Aspect aspect, IAspectCalculation calculateValue) {
    	this.materialAspects.add(Pair.of(aspect, calculateValue));
    }
    
    public List<Pair<Aspect, IAspectCalculation>> getMaterialAspects() {
    	return Collections.unmodifiableList(this.materialAspects);
    }
    
    public boolean hasCalcFor(Names part) {
    	return this.hasCalcFor(part.toString());
    }
    
    public boolean hasCalcFor(String part) {
    	return this.hasCalcFor(new NameToken(part));
    }
    
    public boolean hasCalcFor(NameToken part) {
    	return this.aspectCalcs.containsKey(part);
    }
    
    public Map<Aspect, IAspectCalculation> getCalcsFor(Names part) {
    	return this.getCalcsFor(part.toString());
    }
    
    public Map<Aspect, IAspectCalculation> getCalcsFor(String part) {
    	return this.getCalcsFor(new NameToken(part));    	
    }
    
    public Map<Aspect, IAspectCalculation> getCalcsFor(NameToken part) {
    	return this.aspectCalcs.getOrDefault(part, Collections.emptyMap());
    }
    
    public TCMaterial addAspect(Names part, Aspect aspect, int amount){
    	if (this.aspectMap.containsKey(part.toString())) {
    		AspectList al = this.aspectMap.get(part.toString());
    		al.add(aspect, amount);
    		this.aspectMap.put(part.toString(), al);
    	}  else {
    		this.aspectMap.put(part.toString(), new AspectList().add(aspect, amount));
    	}
    	return this;
    }

    public TCMaterial addAspect(Names part, AspectList aspectList){
    	if (this.aspectMap.containsKey(part.toString())) {
    		AspectList al = this.aspectMap.get(part.toString());
    		al.add(aspectList);
    		this.aspectMap.put(part.toString(), al);
    	}  else {
    		this.aspectMap.put(part.toString(), aspectList);
    	}
    	return this;
    }

    public TCMaterial addAspect(String part, Aspect aspect, int amount){
    	if (this.aspectMap.containsKey(part)) {
    		AspectList al = this.aspectMap.get(part);
    		al.add(aspect, amount);
    		this.aspectMap.put(part, al);
    	}  else {
    		this.aspectMap.put(part, new AspectList().add(aspect, amount));
    	}
    	return this;
    }

    public TCMaterial addAspect(String part, AspectList aspectList){
    	if (this.aspectMap.containsKey(part)) {
    		AspectList al = this.aspectMap.get(part);
    		al.add(aspectList);
    		this.aspectMap.put(part, al);
    	}  else {
    		this.aspectMap.put(part, aspectList);
    	}
    	return this;
    }

    public AspectList getAspectFor(String part) {
        return aspectMap.get(part);
    }

    public AspectList getAspectFor(Names part){
        return getAspectFor(part);
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
