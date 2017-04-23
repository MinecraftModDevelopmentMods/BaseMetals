package com.mcmoddev.lib.data;

import java.util.TreeMap;

import com.google.common.collect.Maps;

import net.minecraft.util.IStringSerializable;

public enum Names implements IStringSerializable {
		// Items
	ANVIL,ARROW,AXE,BLEND,BOOTS,BOLT,BOW,CHESTPLATE,CRACKHAMMER,CROSSBOW,DOOR,FISHINGROD,GEAR,HELMET,HOE,HORSEARMOR,INGOT,LEGGINGS,NUGGET,PICKAXE,POWDER,ROD,SHEARS,SHIELD,SHOVEL,SLAB,SMALLBLEND,SMALLPOWDER,SWORD,GEM,

        // Blocks
	ANVILBLOCK,BARS,BLOCK,BOOKSHELF,BUTTON,DOORBLOCK,DOUBLESLAB,HALFSLAB,FLOWERPOT,LADDER,LEVER,PLATE,PRESSUREPLATE,STAIRS,TRAPDOOR,TRIPWIREHOOK,WALL,FENCE,FENCEGATE,

        // Mekanism
	CRYSTAL,SHARD,CLUMP,POWDERDIRTY,

        // IC2
	CASING,DENSEPLATE,CRUSHED,CRUSHEDPURIFIED,

        // Ores
	ORE,ENDORE,NETHERORE;

	
    public static final TreeMap<Integer, Names> MAP = Maps.newTreeMap();

    static {
        for( Names name : values() )
            MAP.put(name.ordinal(),name);
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }

    public static Names getType(int ordinal) {
        if ( ordinal > values().length || ordinal < 0 )
            ordinal = 0;
        return values()[ordinal];
    }

    @Override
    public String toString() {
    	return getName();
    }
}

