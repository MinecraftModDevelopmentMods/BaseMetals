// These old Forge snapshot entries are now supplied by vanilla's painting data.
const paintings = new Set([
    'alban', 'aztec', 'aztec2', 'bomb', 'burning_skull', 'bust', 'courbet', 'creebet',
    'donkey_kong', 'earth', 'fighters', 'fire', 'graham', 'kebab', 'match', 'pigscene',
    'plant', 'pointer', 'pool', 'sea', 'skeleton', 'skull_and_roses', 'stage', 'sunset',
    'void', 'wanderer', 'wasteland', 'water', 'wind', 'wither'
].map(name => 'minecraft:' + name));

function isVanillaPaintingSnapshot(registry, entries) {
    return registry === 'minecraft:painting_variant'
        && entries.length > 0
        && entries.every(entry => paintings.has(entry));
}

module.exports = { isVanillaPaintingSnapshot };
