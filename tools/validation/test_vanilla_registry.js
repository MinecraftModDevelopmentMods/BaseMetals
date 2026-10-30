const assert = require('node:assert/strict');
const test = require('node:test');
const { isRetiredVanillaMemorySnapshot } = require('./vanilla_registry');

test('accepts the vanilla AI flag removed in 26.3', () => {
    assert.equal(isRetiredVanillaMemorySnapshot('minecraft:memory_module_type', ['minecraft:is_tempted']), true);
});

test('rejects unknown or modded memories', () => {
    for (const name of ['minecraft:unknown', 'basemetals:is_tempted']) {
        assert.equal(isRetiredVanillaMemorySnapshot('minecraft:memory_module_type',
            ['minecraft:is_tempted', name]), false);
    }
});

test('does not excuse another registry or an empty report', () => {
    assert.equal(isRetiredVanillaMemorySnapshot('minecraft:block', ['minecraft:is_tempted']), false);
    assert.equal(isRetiredVanillaMemorySnapshot('minecraft:memory_module_type', []), false);
});
