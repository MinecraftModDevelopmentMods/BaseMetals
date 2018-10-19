package com.mcmoddev.lib.util;

public class MaterialConfigOptions{
    private String identifier;
    private Boolean isVanilla;
    private Boolean isMaterialEnabled;
    private Boolean hasFluid;
    private Boolean isFluidEnabled;
    private Boolean hasTraits;
    private Boolean forceTraits;

    public MaterialConfigOptions(String identifier, Boolean isVanilla, Boolean isMaterialEnabled, Boolean hasFluid, Boolean isFluidEnabled) {
        this(identifier, isVanilla, isMaterialEnabled, hasFluid, isFluidEnabled, false);
    }

    public MaterialConfigOptions(String identifier, Boolean isVanilla, Boolean isMaterialEnabled, Boolean hasFluid, Boolean isFluidEnabled, Boolean hasTraits) {
        this.identifier = identifier;
        this.isVanilla = isVanilla;
        this.isMaterialEnabled = isMaterialEnabled;
        this.hasFluid = hasFluid;
        this.isFluidEnabled = isFluidEnabled;
        this.hasTraits = hasTraits;
        this.forceTraits = false;
    }

    public MaterialConfigOptions(String identifier, Boolean isVanilla, Boolean isMaterialEnabled, Boolean hasFluid, Boolean isFluidEnabled, Boolean hasTraits, Boolean forceTraits) {
        this.identifier = identifier;
        this.isVanilla = isVanilla;
        this.isMaterialEnabled = isMaterialEnabled;
        this.hasFluid = hasFluid;
        this.isFluidEnabled = isFluidEnabled;
        this.hasTraits = hasTraits;
        this.forceTraits = forceTraits;
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

    public Boolean getHasTraits() {
        return hasTraits;
    }

    public Boolean getForceTraits() {
        return forceTraits;
    }
}
