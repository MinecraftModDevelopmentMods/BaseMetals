package com.mcmoddev.lib.util;

public class IntegrationConfigOptions {
    private String identifier;
    private String modid;
    private Boolean isEnabled;

    public IntegrationConfigOptions(String identifier, String modid, Boolean isMaterialEnabled) {
        this.identifier = identifier;
        this.modid = modid;
        this.isEnabled = isMaterialEnabled;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getModid() {
        return modid;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }
}
