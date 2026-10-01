package com.mcmoddev.alt;

import com.mcmoddev.alt.api.ALTPlugin;
import com.mcmoddev.alt.api.IALTPlugin;
import com.mcmoddev.basemetals.BaseMetals;

/** Marker used by Additional Loot Tables to discover Base Metals' bundled tables. */
@ALTPlugin(modid = BaseMetals.MODID)
class BaseMetalsALT implements IALTPlugin {
}
