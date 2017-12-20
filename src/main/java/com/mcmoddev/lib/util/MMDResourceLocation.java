package com.mcmoddev.lib.util;

import java.util.UUID;
import net.minecraft.util.ResourceLocation;

public class MMDResourceLocation extends ResourceLocation {
    private final String entryNamespace;
    private final String entryType;
    private final String entryName;
    
    public MMDResourceLocation(String namespace, String entryType, String entryName ) {
        super( "mmdlib", UUID.nameUUIDFromBytes(String.format("%s:%s/%s", namespace, entryType, entryName).getBytes()).toString() );
        this.entryNamespace = namespace;
        this.entryType = entryType;
        this.entryName = entryName;
    }

    public String getEntryNamespace() {
        return this.entryNamespace;
    }
    
    public String getEntryType() {
        return this.entryType;
    }
    
    public String getEntryName() {
        return this.entryName;
    }
}