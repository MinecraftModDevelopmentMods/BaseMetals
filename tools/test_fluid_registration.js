const assert = require('node:assert/strict');
const test = require('node:test');
const fs = require('node:fs');
const path = require('node:path');

const source = path.join(__dirname, '../src/main/java/zone/moddev/mc/basemetals');
const mod = fs.readFileSync(path.join(source, 'BaseMetals.java'), 'utf8');
const content = fs.readFileSync(path.join(source, 'content/ModContent.java'), 'utf8');

test('network channel creation waits for the sequential setup queue', () => {
  assert.ok(!mod.includes('        ContentModeNetwork.register();'));
  assert.ok(mod.includes('DeferredWorkQueue.runLater(ContentModeNetwork::register);'));
});

test('parallel mod construction does not register fluids', () => {
  assert.doesNotMatch(mod, /initializeFluids\s*\(/);
});

test('the sequential block registration event binds fluids before block factories run', () => {
  assert.match(content, /@SubscribeEvent\s+public static void registerBlocks\(RegistryEvent\.Register<Block> event\)\s*\{\s*initializeFluids\(\);/);
  assert.equal((content.match(/initializeFluids\(\);/g) || []).length, 1);
});

test('fluid registration cannot be called from another startup stage', () => {
  assert.match(content, /private static void initializeFluids\(\)/);
  assert.match(content, /if \(isBound\(content\.source\(\)\)\) continue;/);
});
