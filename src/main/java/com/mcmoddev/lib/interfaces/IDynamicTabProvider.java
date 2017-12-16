package com.mcmoddev.lib.interfaces;

public interface IDynamicTabProvider extends ITabProvider {
	/**
	 * Add a new tab to the set
	 * @param tabName Name of tab to add
	 * @param searchable Is the tab searchable
	 * @param modID Id of the mod related to the tab
	 */
	void addTab(String tabName, boolean searchable, String modID);
}
