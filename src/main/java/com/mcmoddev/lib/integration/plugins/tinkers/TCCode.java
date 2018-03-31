package com.mcmoddev.lib.integration.plugins.tinkers;

public enum TCCode {

	SUCCESS, // base success
	FAILURE, // generic failure
	FAILURE_PARAMETER_ERROR, // bad/missing/null parameter
	MATERIAL_ALREADY_REGISTERED, // material already known
	UNKNOWN_MATERIAL_ERROR, // asked for a fluid that doesn't exist
	BAD_MATERIAL // Material exists but does not have a registered fluid - it cannot be used with
					// the TiC plugin
	;
}
