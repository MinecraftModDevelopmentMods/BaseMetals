package com.mcmoddev.lib.integration.tinkers;

public enum TCCode {
  SUCCESS(1), // base success
  FAILURE(2), // generic failure
  FAILURE_PARAMETER_ERROR(4), // bad/missing/null parameter
  MATERIAL_ALREADY_REGISTERED(8), // material already known
  UNKNOWN_MATERIAL_ERROR(16), // asked for a fluid that doesn't exist
  BAD_MATERIAL(32) // Material exists but does not have a registered fluid - it cannot be used with the TiC plugin
  ;
	
  private final int tcCode;
  
  private TCCode(int tcCode) {
	  this.tcCode = tcCode;
  }
  
  public int getCode() {
	  return this.tcCode;
  }
}
