package com.mcmoddev.lib.util;

import java.util.concurrent.ConcurrentHashMap;

import com.mcmoddev.basemetals.init.ItemGroups;
import com.mcmoddev.lib.interfaces.ITabProvider;

public class BMeIoC {

	protected ConcurrentHashMap<Class<?>, Object> container = new ConcurrentHashMap<Class<?>, Object>();
	protected static BMeIoC _instance;
	
	protected BMeIoC() {
	
	}
	
	public <K, V extends K> boolean register(Class<K> key, V value) {
	    return _instance.container.put(key, value) == null;
	}

	public <K, V extends K> V resolve(Class<K> keyObject) {
	    return (V) container.get(keyObject);
	}
	
	public void wireup() {
		ItemGroups.init();
		
		this.register(ITabProvider.class, ItemGroups.myTabs);
	}
	
	public static BMeIoC getInstance() {
		return getInstance(true);
	}
	
	public static BMeIoC getInstance(Boolean autoWirup) {
	      if(_instance == null) {
	         _instance = new BMeIoC();
	         
	         if (autoWirup)
	        	 _instance.wireup();
	      }
	      
	      return _instance;
	   }
}