package com.mcmoddev.lib.util;

public class MaterialConfigOptions{
    private String identifier;
    private Boolean isVanilla;
    private Boolean isMaterialEnabled;
    private Boolean hasFluid;
    private Boolean isFluidEnabled;

    public MaterialConfigOptions(String identifier, Boolean isVanilla, Boolean isMaterialEnabled, Boolean hasFluid, Boolean isFluidEnabled) {
        this.identifier = identifier;
        this.isVanilla = isVanilla;
        this.isMaterialEnabled = isMaterialEnabled;
        this.hasFluid = hasFluid;
        this.isFluidEnabled = isFluidEnabled;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Boolean getVanilla() {
        return isVanilla;
    }

    public Boolean getMaterialEnabled() {
        return isMaterialEnabled;
    }

    public Boolean getHasFluid() {
        return hasFluid;
    }

    public Boolean getFluidEnabled() {
        return isFluidEnabled;
    }
}
