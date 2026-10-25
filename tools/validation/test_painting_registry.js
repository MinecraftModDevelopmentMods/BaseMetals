const assert = require('node:assert/strict');
const test = require('node:test');
const { isVanillaPaintingSnapshot } = require('./painting_registry');

test('accepts vanilla paintings moved out of the old Forge snapshot', () => {
    assert.equal(isVanillaPaintingSnapshot('minecraft:painting_variant',
        ['minecraft:kebab', 'minecraft:aztec', 'minecraft:wither']), true);
});

test('does not hide an unknown or modded painting', () => {
    for (const painting of ['minecraft:unknown', 'another_mod:painting']) {
        assert.equal(isVanillaPaintingSnapshot('minecraft:painting_variant',
            ['minecraft:kebab', painting]), false);
    }
});

test('does not accept the same name from a different registry', () => {
    assert.equal(isVanillaPaintingSnapshot('minecraft:block', ['minecraft:fire']), false);
});

test('does not accept an empty missing-mapping report', () => {
    assert.equal(isVanillaPaintingSnapshot('minecraft:painting_variant', []), false);
});
