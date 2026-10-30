'use strict';

// Minecraft 26.3 no longer registers this AI flag.
function isRetiredVanillaMemorySnapshot(registry, entries) {
    return registry === 'minecraft:memory_module_type'
        && entries.length > 0
        && entries.every(entry => entry === 'minecraft:is_tempted');
}

module.exports = { isRetiredVanillaMemorySnapshot };
