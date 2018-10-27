package com.mcmoddev.lib.data;

import java.util.UUID;

import com.mcmoddev.lib.util.Version5UUID;

public class NameToken {
	private final UUID uuid;
	private final String origData;
	
	public NameToken(Names name) {
		this(name.toString());
	}
	
	public NameToken(String name) {
		this.uuid = Version5UUID.mmdlibUUID(name);
		this.origData = name;
	}
	
	@Override
	public String toString() {
		return String.format("V5UUID@%s", this.uuid);
	}
	
	@Override
	public int hashCode() {
		return this.uuid.hashCode();
	}
	
	public String asString() {
		return this.origData;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof NameToken)) return false;
		
		NameToken ot = (NameToken) other;
		
		return (ot.asString().equals(this.origData) && ot.hashCode() == this.hashCode() && ot.toString().equals(this.toString()));
	}
}
