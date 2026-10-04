/*
 * Converts the catalogue-generated resources to Minecraft 1.13 formats.
 * Removes unsupported recipes and integrations, and converts block states,
 * item models and tags to the names and formats used by Forge 25.
 */
'use strict';

const fs = require('fs');
const path = require('path');

const root = path.resolve(__dirname, '..');
const generated = path.join(root, 'src', 'generated', 'resources');
const main = path.join(root, 'src', 'main', 'resources');

function inside(base, target) {
  const relative = path.relative(base, target);
  if (relative.startsWith('..') || path.isAbsolute(relative)) {
    throw new Error(`Refusing to modify path outside ${base}: ${target}`);
  }
  return target;
}

function remove(relative) {
  const target = inside(root, path.join(root, relative));
  fs.rmSync(target, { recursive: true, force: true });
}

function writeJson(file, value) {
  fs.mkdirSync(path.dirname(file), { recursive: true });
  fs.writeFileSync(file, JSON.stringify(value, null, 2) + '\n', 'utf8');
}

function writeBase64(file, value) {
  fs.mkdirSync(path.dirname(file), { recursive: true });
  fs.writeFileSync(file, Buffer.from(value, 'base64'));
}

function filesUnder(directory, suffix) {
  if (!fs.existsSync(directory)) return [];
  const result = [];
  for (const entry of fs.readdirSync(directory, { withFileTypes: true })) {
    const child = path.join(directory, entry.name);
    if (entry.isDirectory()) result.push(...filesUnder(child, suffix));
    else if (!suffix || entry.name.endsWith(suffix)) result.push(child);
  }
  return result.sort();
}

for (const relative of [
  'src/generated/resources/data/mekanism',
  'src/generated/resources/data/thermal',
  'src/generated/resources/data/tconstruct',
  'src/generated/resources/data/enderio',
  'src/generated/resources/data/basemetals/mekanism',
  'src/generated/resources/data/basemetals/thermal',
  'src/generated/resources/data/basemetals/tinkering',
  'src/generated/resources/data/basemetals/enderio',
  'src/generated/resources/data/basemetals/recipes/compat',
  'src/generated/resources/data/basemetals/loot_modifiers',
  'src/generated/resources/data/forge/loot_modifiers',
  'src/generated/resources/data/basemetals/loot_tables/blocks',
  'src/generated/resources/data/minecraft/tags/blocks/mineable',
  'src/generated/resources/data/minecraft/tags/blocks/needs_stone_tool.json',
  'src/generated/resources/data/minecraft/tags/blocks/needs_iron_tool.json',
  'src/generated/resources/data/minecraft/tags/blocks/needs_diamond_tool.json',
  'src/generated/resources/data/minecraft/tags/blocks/beacon_base_blocks.json'
]) remove(relative);

// Anvils need this tag to wear out during repairs. The coremod updates their
// damage state without replacing the block, as vanilla anvils do.
writeJson(path.join(generated, 'data', 'minecraft', 'tags', 'blocks', 'anvil.json'), {
  replace: false,
  values: [
    'basemetals:stone_anvil',
    'basemetals:steel_anvil',
    'basemetals:adamantine_anvil'
  ]
});

for (const file of filesUnder(path.join(generated, 'data', 'basemetals', 'recipes'), '.json')) {
  const recipe = JSON.parse(fs.readFileSync(file, 'utf8'));
  if (recipe.type === 'minecraft:blasting' || path.basename(file) === 'ancient_debris_crushing.json') {
    fs.rmSync(file);
  }
}

const manifestFile = path.join(generated, 'data', 'basemetals', 'registry_manifest.json');
const manifest = JSON.parse(fs.readFileSync(manifestFile, 'utf8'));
manifest.source = 'Base Metals 1.13.2 catalogue';
manifest.loot_modifier_serializers = [];
manifest.new_loot_modifier_serializers = [];
for (const key of ['recipe_serializers', 'new_recipe_serializers']) {
  if (!manifest[key].includes('basemetals:content_crafting')) manifest[key].push('basemetals:content_crafting');
  manifest[key].sort();
}
writeJson(manifestFile, manifest);

// Forge 25's bucket loader leaves our fluids with missing textures. Use a
// normal item model instead; ClientSetup colours the fluid layer.
for (const bucket of manifest.new_items) {
  const id = bucket.substring(bucket.indexOf(':') + 1);
  writeJson(path.join(generated, 'assets', 'basemetals', 'models', 'item', `${id}.json`), {
    parent: 'item/generated',
    textures: {
      layer0: 'minecraft:item/bucket',
      layer1: 'basemetals:item/bucket_fluid',
      layer2: 'basemetals:item/bucket_overlay'
    }
  });
}
writeBase64(path.join(generated, 'assets', 'basemetals', 'textures', 'item', 'bucket_fluid.png'),
  'iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAA4SURBVDhPY2AYnuA/DoCuDgOga8AF0PWhAHTF6ABdPU5AtkZcgGSD0BWj80kGFBtAMRh4FwxeAAB9Pod56G8eMAAAAABJRU5ErkJggg==');

// Vanilla copper and the modern Nether/Caves & Cliffs plants do not exist in
// 1.13.2. Keep Base Metals copper in the common tags and retain only plants
// present in the target registry.
for (const relative of [
  'data/forge/tags/blocks/ores/copper.json',
  'data/forge/tags/blocks/storage_blocks/copper.json',
  'data/forge/tags/items/ingots/copper.json',
  'data/forge/tags/items/ores/copper.json',
  'data/forge/tags/items/storage_blocks/copper.json'
]) {
  const file = path.join(generated, relative);
  const tag = JSON.parse(fs.readFileSync(file, 'utf8'));
  tag.values = tag.values.filter(value => typeof value !== 'string' || !value.startsWith('minecraft:copper'))
    .filter(value => value !== 'minecraft:deepslate_copper_ore');
  writeJson(file, tag);
}

writeJson(path.join(generated, 'data', 'basemetals', 'tags', 'blocks', 'scythe_harvestable.json'), {
  replace: false,
  values: [
    '#minecraft:leaves', '#minecraft:saplings',
    'minecraft:oak_sapling', 'minecraft:spruce_sapling', 'minecraft:birch_sapling',
    'minecraft:jungle_sapling', 'minecraft:acacia_sapling', 'minecraft:dark_oak_sapling',
    'minecraft:oak_leaves', 'minecraft:spruce_leaves', 'minecraft:birch_leaves',
    'minecraft:jungle_leaves', 'minecraft:acacia_leaves', 'minecraft:dark_oak_leaves',
    'minecraft:cobweb', 'minecraft:grass', 'minecraft:fern', 'minecraft:dead_bush',
    'minecraft:seagrass', 'minecraft:tall_seagrass', 'minecraft:dandelion',
    'minecraft:poppy', 'minecraft:blue_orchid', 'minecraft:allium', 'minecraft:azure_bluet',
    'minecraft:red_tulip', 'minecraft:orange_tulip', 'minecraft:white_tulip',
    'minecraft:pink_tulip', 'minecraft:oxeye_daisy', 'minecraft:brown_mushroom',
    'minecraft:red_mushroom', 'minecraft:wheat', 'minecraft:cactus', 'minecraft:sugar_cane',
    'minecraft:pumpkin', 'minecraft:carved_pumpkin', 'minecraft:jack_o_lantern',
    'minecraft:melon', 'minecraft:attached_pumpkin_stem', 'minecraft:attached_melon_stem',
    'minecraft:pumpkin_stem', 'minecraft:melon_stem', 'minecraft:vine', 'minecraft:lily_pad',
    'minecraft:nether_wart', 'minecraft:cocoa', 'minecraft:carrots', 'minecraft:potatoes',
    'minecraft:sunflower', 'minecraft:lilac', 'minecraft:rose_bush', 'minecraft:peony',
    'minecraft:tall_grass', 'minecraft:large_fern', 'minecraft:chorus_plant',
    'minecraft:chorus_flower', 'minecraft:beetroots', 'minecraft:kelp', 'minecraft:kelp_plant',
    'minecraft:tube_coral', 'minecraft:brain_coral', 'minecraft:bubble_coral',
    'minecraft:fire_coral', 'minecraft:horn_coral', 'minecraft:tube_coral_fan',
    'minecraft:brain_coral_fan', 'minecraft:bubble_coral_fan', 'minecraft:fire_coral_fan',
    'minecraft:horn_coral_fan', 'minecraft:tube_coral_wall_fan',
    'minecraft:brain_coral_wall_fan', 'minecraft:bubble_coral_wall_fan',
    'minecraft:fire_coral_wall_fan', 'minecraft:horn_coral_wall_fan', 'minecraft:sea_pickle'
  ]
});

const crushableFile = path.join(generated, 'data', 'basemetals', 'tags', 'blocks',
  'crackhammer_crushable.json');
const crushable = JSON.parse(fs.readFileSync(crushableFile, 'utf8'));
crushable.values = crushable.values.filter(value =>
  value !== '#forge:ores/netherite_scrap' && value !== '#forge:gravel');
if (!crushable.values.includes('minecraft:gravel')) {
  crushable.values.push('minecraft:gravel');
}
writeJson(crushableFile, crushable);

// Forge 25 did not provide a forge:gravel item tag. Keep the historical
// gravel-to-sand crushing recipe, but name the vanilla item directly.
const gravelCrushingFile = path.join(generated, 'data', 'basemetals', 'recipes',
  'gravel_crushing.json');
const gravelCrushing = JSON.parse(fs.readFileSync(gravelCrushingFile, 'utf8'));
gravelCrushing.ingredient = { item: 'minecraft:gravel' };
writeJson(gravelCrushingFile, gravelCrushing);

// These are the original 1.12 auxiliary tables and already use the 1.13 loot
// grammar. They preserve the exact historical weights and enchanted-item rolls.
const legacyChests = path.join(root, 'reference', '1.12', 'alt', 'chests');
const targetChests = path.join(generated, 'data', 'basemetals', 'loot_tables', 'chests', 'inject');
remove('src/generated/resources/data/basemetals/loot_tables/chests/inject');
fs.mkdirSync(targetChests, { recursive: true });
for (const file of filesUnder(legacyChests, '.json')) {
  const target = path.join(targetChests, path.basename(file));
  const table = JSON.parse(fs.readFileSync(file, 'utf8'));
  const tableName = path.basename(file, '.json');
  // Forge 25 requires every loot pool to have a stable, unique name.
  table.pools.forEach((pool, index) => {
    pool.name = `basemetals_${tableName}_${index}`;
    for (const entry of pool.entries) {
      if (entry.type === 'item' && entry.name.startsWith('basemetals:')) {
        entry.conditions = [...(entry.conditions || []), {
          condition: 'basemetals:content_mode', item: entry.name
        }];
      }
    }
  });
  writeJson(target, table);
}

// Walls used booleans in 1.13; low/tall wall-height enums arrived later.
for (const file of filesUnder(path.join(generated, 'assets', 'basemetals', 'blockstates'), '_wall.json')) {
  const name = path.basename(file, '.json');
  writeJson(file, {
    multipart: [
      { when: { up: 'true' }, apply: { model: `basemetals:block/${name}_post` } },
      { when: { north: 'true' }, apply: { model: `basemetals:block/${name}_side`, uvlock: true } },
      { when: { east: 'true' }, apply: { model: `basemetals:block/${name}_side`, y: 90, uvlock: true } },
      { when: { south: 'true' }, apply: { model: `basemetals:block/${name}_side`, y: 180, uvlock: true } },
      { when: { west: 'true' }, apply: { model: `basemetals:block/${name}_side`, y: 270, uvlock: true } }
    ]
  });
}

const common = {
  enabled: true,
  min_quantity: 4,
  max_quantity: 11,
  pattern: 'vein',
  height_distribution: 'uniform',
  discard_chance_on_air_exposure: 0,
  spread: 8,
  vertical_spread: 4,
  node_size: 4,
  length: 16
};
const rule = (minY, maxY, frequency, hosts, tags, families) => Object.assign({}, common, {
  min_y: minY,
  max_y: maxY,
  frequency,
  host_blocks: hosts || [],
  host_tags: tags || [],
  host_families: families || []
});
const ore = (name, dimensions, selectors) => ({
  block: `basemetals:${name}_ore`,
  enabled: true,
  source_mod: 'basemetals',
  retrogen: false,
  ...(dimensions ? { dimensions } : { dimension_selectors: selectors })
});
const ordinaryHosts = ['minecraft:stone'];
const ordinaryTags = ['forge:stone'];
const ordinaryFamilies = ['sedimentary', 'metamorphic', 'igneous_intrusive', 'igneous_volcanic'];
const ordinary = (minY, maxY, frequency) => ({
  'orespawn:all_except_nether_end': rule(minY, maxY, frequency,
    ordinaryHosts, ordinaryTags, ordinaryFamilies)
});

writeJson(path.join(main, 'data', 'basemetals', 'orespawn', 'provider.json'), {
  schema_version: 3,
  provider_modid: 'basemetals',
  provider_revision: 1,
  ores: {
    'basemetals:ore/coldiron': ore('coldiron', {
      'minecraft:the_nether': rule(0, 127, 5, ['minecraft:netherrack'], ['forge:netherrack'])
    }),
    'basemetals:ore/adamantine': ore('adamantine', {
      'minecraft:the_nether': rule(0, 127, 2, ['minecraft:netherrack'], ['forge:netherrack'])
    }),
    'basemetals:ore/starsteel': ore('starsteel', {
      'minecraft:the_end': rule(0, 254, 5, ['minecraft:end_stone'], ['forge:end_stones'])
    }),
    'basemetals:ore/copper': ore('copper', null, ordinary(0, 95, 10)),
    'basemetals:ore/silver': ore('silver', null, ordinary(0, 31, 4)),
    'basemetals:ore/tin': ore('tin', null, ordinary(0, 127, 10)),
    'basemetals:ore/lead': ore('lead', null, ordinary(0, 63, 5)),
    'basemetals:ore/zinc': ore('zinc', null, ordinary(0, 95, 5)),
    'basemetals:ore/mercury': ore('mercury', null, ordinary(0, 31, 3)),
    'basemetals:ore/nickel': ore('nickel', null, ordinary(32, 95, 1)),
    'basemetals:ore/platinum': ore('platinum', null, ordinary(1, 31, 0.125))
  },
  rocks: {},
  geomes: {},
  biome_rules: {},
  terrain_dimensions: {},
  fluid_deposits: {}
});

const recipesDirectory = path.join(generated, 'data', 'basemetals', 'recipes');
const unlockDirectory = path.join(generated, 'data', 'basemetals', 'advancements', 'recipes');

// As in 1.12, finding the base material reveals its crafting recipes.
const discoveryIngredients = {};
for (const metal of [
  'adamantine', 'antimony', 'aquarium', 'bismuth', 'brass', 'bronze', 'coldiron',
  'copper', 'cupronickel', 'electrum', 'gold', 'invar', 'iron', 'lead', 'mercury',
  'mithril', 'nickel', 'obsidian', 'pewter', 'platinum', 'silver', 'starsteel',
  'steel', 'tin', 'zinc'
]) {
  discoveryIngredients[metal] = { tag: `forge:ingots/${metal}` };
}
for (const gem of ['diamond', 'emerald', 'quartz']) {
  discoveryIngredients[gem] = { tag: `forge:gems/${gem}` };
}
Object.assign(discoveryIngredients, {
  coal: { item: 'minecraft:coal' },
  charcoal: { item: 'minecraft:charcoal' },
  redstone: { tag: 'forge:dusts/redstone' },
  stone: { tag: 'forge:stone' },
  wood: { tag: 'minecraft:logs' }
});
const genericSteelRecipes = new Set([
  'activator_rail', 'detector_rail', 'flint_and_steel', 'human_detector',
  'minecart', 'piston', 'rail', 'tripwire_hook'
]);

// Vanilla Bits used several shortened patterns that collided or returned too much when recycled.
for (const file of filesUnder(recipesDirectory, '.json')) {
  const name = path.basename(file, '.json');
  const saved = JSON.parse(fs.readFileSync(file, 'utf8'));
  const recipe = saved.type === 'basemetals:content_crafting' ? saved.recipe : saved;

  if (name.endsWith('_door') && recipe.type === 'minecraft:crafting_shaped') {
    recipe.pattern = ['XX', 'XX', 'XX'];
  }
  if (name.endsWith('_bolt') && recipe.type === 'minecraft:crafting_shaped') {
    recipe.pattern = ['R', 'R', 'F'];
  }
  if (name.endsWith('_sword') && recipe.type === 'minecraft:crafting_shaped'
      && recipe.pattern.length === 2) {
    recipe.pattern = ['X', 'X', 'S'];
  }
  if (name === 'rail') recipe.result.count = 16;
  if (name === 'obsidian_ingot') recipe.result.item = 'basemetals:obsidian_ingot';
  if (name === 'obsidian_block') recipe.pattern = ['XXX', 'XXX', 'XXX'];

  // Low Fantasy keeps mercury powder and the ingot needed for Mithril,
  // but doesn't allow mercury nuggets or building blocks.
  if (recipe.type === 'minecraft:smelting' && recipe.result === 'basemetals:mercury_nugget') {
    recipe.type = 'basemetals:legacy_smelting';
    recipe.result = { item: recipe.result };
    writeJson(file, recipe);
  }

  if (!recipe.type.startsWith('minecraft:crafting_')) {
    const oldUnlock = path.join(unlockDirectory, `${name}.json`);
    if (fs.existsSync(oldUnlock)) fs.rmSync(oldUnlock);
    continue;
  }

  // Variants of one result share a cell; different tools and weapons do not.
  recipe.group = recipe.result.item;
  writeJson(file, { type: 'basemetals:content_crafting', recipe });

  const ingredients = recipe.key ? Object.values(recipe.key) : recipe.ingredients;
  const choices = ingredients.flat().filter(value => value.item || value.tag);
  const materialChoices = choices.filter(value =>
    value.tag !== 'forge:rods/wooden' && value.item !== 'minecraft:stick'
      && value.item !== 'minecraft:string' && value.item !== 'minecraft:feather');
  const material = genericSteelRecipes.has(name) ? 'steel' : name.split('_')[0];
  const discovery = discoveryIngredients[material];
  if (!discovery) throw new Error(`Missing recipe discovery material: ${name}`);

  const criteria = {
    has_material: {
      trigger: 'minecraft:inventory_changed',
      conditions: { items: [discovery] }
    },
    has_recipe: {
      trigger: 'minecraft:recipe_unlocked',
      conditions: { recipe: `basemetals:${name}` }
    }
  };
  // Keep the old ingredient criteria so earned progress survives this update.
  const seen = new Set();
  for (const ingredient of materialChoices.length ? materialChoices : choices) {
    const key = JSON.stringify(ingredient);
    if (seen.has(key)) continue;
    seen.add(key);
    const predicate = ingredient.tag ? { tag: ingredient.tag } : { item: ingredient.item };
    criteria[`has_ingredient_${seen.size}`] = {
      trigger: 'minecraft:inventory_changed',
      conditions: { items: [predicate] }
    };
  }
  writeJson(path.join(unlockDirectory, `${name}.json`), {
    parent: 'minecraft:recipes/root',
    criteria,
    requirements: [Object.keys(criteria)],
    rewards: { recipes: [`basemetals:${name}`] }
  });
}

const configTranslations = require('./config_translations.json');
const playerTranslations = require('./player_translations.json');
for (const file of filesUnder(path.join(generated, 'assets', 'basemetals', 'lang'), '.json')) {
  const locale = path.basename(file, '.json');
  const source = configTranslations.aliases[locale] || locale;
  const text = configTranslations.locales[source];
  if (!text) throw new Error(`Missing configuration translation: ${locale}`);
  const playerText = playerTranslations.locales[source];
  if (!playerText) throw new Error(`Missing player translation: ${locale}`);
  const language = JSON.parse(fs.readFileSync(file, 'utf8'));
  Object.assign(language, text, playerText);
  writeJson(file, language);
}

console.log('Generated Minecraft 1.13.2-compatible Base Metals resources.');
