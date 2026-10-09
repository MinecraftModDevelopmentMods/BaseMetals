/*
 * Builds the recipes, drops and tags used by the Minecraft 1.17 port.
 * Ore models are generated separately by Forge's native model provider.
 */
'use strict';

const fs = require('fs');
const path = require('path');

const root = path.resolve(__dirname, '..');
const generated = path.join(root, 'src', 'generated', 'resources');
const main = path.join(root, 'src', 'main', 'resources');
const verify = process.argv.includes('--verify');

function inside(base, target) {
  const relative = path.relative(base, target);
  if (relative.startsWith('..') || path.isAbsolute(relative)) {
    throw new Error(`Refusing to modify path outside ${base}: ${target}`);
  }
  return target;
}

function remove(relative) {
  const target = inside(root, path.join(root, relative));
  if (verify) {
    if (fs.existsSync(target)) throw new Error(`Obsolete generated resource: ${relative}`);
    return;
  }
  fs.rmSync(target, { recursive: true, force: true });
}

function writeJson(file, value) {
  if (verify) {
    if (!fs.existsSync(file) || JSON.stringify(JSON.parse(fs.readFileSync(file, 'utf8'))) !== JSON.stringify(value)) {
      throw new Error(`Generated resource is stale: ${path.relative(root, file)}. Run generateResources.`);
    }
    return;
  }
  fs.mkdirSync(path.dirname(file), { recursive: true });
  fs.writeFileSync(file, JSON.stringify(value, null, 2) + '\n', 'utf8');
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

const manifestFile = path.join(generated, 'data', 'basemetals', 'registry_manifest.json');
const manifest = JSON.parse(fs.readFileSync(manifestFile, 'utf8'));
manifest.source = 'Base Metals 1.17.1 catalogue';
const oreMaterials = manifest.blocks.filter(id => id.endsWith('_ore')).map(id => id.split(':')[1].replace(/_ore$/, ''));
const rawMaterials = oreMaterials.filter(name => name !== 'copper');
for (const material of rawMaterials) {
  const id = `basemetals:${material}_raw`;
  for (const key of ['items', 'new_items']) {
    if (!manifest[key].includes(id)) manifest[key].push(id);
    manifest[key].sort();
  }
}
manifest.loot_modifier_serializers = [];
manifest.new_loot_modifier_serializers = [];
for (const key of ['recipe_serializers', 'new_recipe_serializers']) {
  if (!manifest[key].includes('basemetals:content_crafting')) manifest[key].push('basemetals:content_crafting');
  manifest[key].sort();
}
writeJson(manifestFile, manifest);

// Minecraft 1.14 reads block drops from loot tables, including old double slabs.
const fluidIds = new Set(manifest.fluids);
for (const block of manifest.blocks) {
  if (fluidIds.has(block)) continue;
  const id = block.split(':')[1];
  if (id.endsWith('_ore')) {
    const material = id.replace(/_ore$/, '');
    const raw = material === 'copper' ? 'minecraft:raw_copper' : `basemetals:${material}_raw`;
    const functions = material === 'copper' ? [{ function: 'minecraft:set_count', count: {
      type: 'minecraft:uniform', min: 2, max: 3
    }}] : [];
    functions.push({ function: 'minecraft:apply_bonus', enchantment: 'minecraft:fortune', formula: 'minecraft:ore_drops' },
      { function: 'minecraft:explosion_decay' });
    writeJson(path.join(generated, 'data', 'basemetals', 'loot_tables', 'blocks', `${id}.json`), {
      type: 'minecraft:block', pools: [{ name: `basemetals_${id}`, rolls: 1, entries: [{
        type: 'minecraft:alternatives', children: [{ type: 'minecraft:item', name: block, conditions: [{
          condition: 'minecraft:match_tool', predicate: { enchantments: [{ enchantment: 'minecraft:silk_touch', levels: { min: 1 } }] }
        }] }, { type: 'minecraft:item', name: raw, functions }]
      }] }]
    });
    continue;
  }
  const compatibilitySlab = id.startsWith('double_') && id.endsWith('_slab');
  const drop = compatibilitySlab ? id.substring('double_'.length) : id;
  const functions = [];
  const stateCondition = properties => ({
    condition: 'minecraft:block_state_property', block, properties
  });

  if (compatibilitySlab) {
    functions.push({ function: 'minecraft:set_count', count: 2 });
  } else if (id.endsWith('_slab')) {
    functions.push({ function: 'minecraft:set_count', count: 2,
      conditions: [stateCondition({ type: 'double' })] });
  } else if (id.endsWith('_anvil')) {
    for (const damage of [1, 2]) {
      functions.push({ function: 'minecraft:set_nbt',
        tag: `{BlockStateTag:{damage:"${damage}"}}`,
        conditions: [stateCondition({ damage: String(damage) })] });
    }
  }

  const conditions = [{ condition: 'minecraft:survives_explosion' }];
  if (id.endsWith('_door')) conditions.push(stateCondition({ half: 'lower' }));
  writeJson(path.join(generated, 'data', 'basemetals', 'loot_tables', 'blocks', `${id}.json`), {
    type: 'minecraft:block',
    pools: [{ name: `basemetals_${id}`, rolls: 1, conditions,
      entries: [{ type: 'minecraft:item', name: `basemetals:${drop}`, functions }] }]
  });
}

// Forge draws the actual fluid texture inside the bucket, as it does in 1.18.
for (const bucket of manifest.new_items.filter(id => id.endsWith('_bucket'))) {
  const id = bucket.substring(bucket.indexOf(':') + 1);
  writeJson(path.join(generated, 'assets', 'basemetals', 'models', 'item', `${id}.json`), {
    parent: 'forge:item/bucket',
    loader: 'forge:bucket',
    fluid: `basemetals:${id.replace(/_bucket$/, '')}`
  });
}
remove('src/generated/resources/assets/basemetals/textures/item/bucket_fluid.png');

// Keep copper ingredients interchangeable without replacing saved Base Metals items.
for (const kind of ['blocks', 'items']) {
  for (const [tagName, additions] of Object.entries({
    'ores/copper': ['minecraft:copper_ore', 'minecraft:deepslate_copper_ore'],
    'storage_blocks/copper': ['minecraft:copper_block'],
    ...(kind === 'items' ? { 'ingots/copper': ['minecraft:copper_ingot'] } : {})
  })) {
    const file = path.join(generated, 'data/forge/tags', kind, `${tagName}.json`);
    const tag = JSON.parse(fs.readFileSync(file, 'utf8'));
    tag.values = [...new Set([...tag.values, ...additions])];
    writeJson(file, tag);
  }
  for (const material of ['iron', 'gold', 'coal', 'diamond', 'emerald', 'lapis', 'redstone']) {
    const file = path.join(generated, 'data/forge/tags', kind, `ores/${material}.json`);
    const tag = fs.existsSync(file) ? JSON.parse(fs.readFileSync(file, 'utf8'))
      : { replace: false, values: [`minecraft:${material}_ore`] };
    tag.values = [...new Set([...tag.values, `minecraft:deepslate_${material}_ore`])];
    writeJson(file, tag);
  }
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
crushable.values = crushable.values.filter(value => value !== '#forge:gravel');
if (!crushable.values.includes('#forge:ores/netherite_scrap')) {
  crushable.values.push('#forge:ores/netherite_scrap');
}
if (!crushable.values.includes('minecraft:gravel')) {
  crushable.values.push('minecraft:gravel');
}
writeJson(crushableFile, crushable);

// Forge 36 did not provide a forge:gravel item tag. Keep the historical
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
if (!verify) fs.mkdirSync(targetChests, { recursive: true });
for (const file of filesUnder(legacyChests, '.json')) {
  const target = path.join(targetChests, path.basename(file));
  const table = JSON.parse(fs.readFileSync(file, 'utf8'));
  const tableName = path.basename(file, '.json');
  // Forge 36 requires every loot pool to have a stable, unique name.
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

// Each wall connection can now be absent, low or tall.
for (const file of filesUnder(path.join(generated, 'assets', 'basemetals', 'blockstates'), '_wall.json')) {
  const name = path.basename(file, '.json');
  writeJson(file, {
    multipart: [
      { when: { up: 'true' }, apply: { model: `basemetals:block/${name}_post` } },
      ...['north', 'east', 'south', 'west'].flatMap((side, index) => [
        { when: { [side]: 'low' }, apply: { model: `basemetals:block/${name}_side`, y: index * 90, uvlock: true } },
        { when: { [side]: 'tall' }, apply: { model: `basemetals:block/${name}_side_tall`, y: index * 90, uvlock: true } }
      ])
    ]
  });
  const sideModel = JSON.parse(fs.readFileSync(path.join(main, 'assets', 'basemetals', 'models', 'block', `${name}_side.json`), 'utf8'));
  writeJson(path.join(generated, 'assets', 'basemetals', 'models', 'block', `${name}_side_tall.json`), {
    ...sideModel, parent: 'minecraft:block/template_wall_side_tall'
  });
}

writeJson(path.join(generated, 'data', 'forge', 'tags', 'items', 'ores', 'gold.json'), {
  replace: false, values: ['minecraft:gold_ore', 'minecraft:deepslate_gold_ore', 'minecraft:nether_gold_ore']
});
writeJson(path.join(generated, 'data', 'forge', 'tags', 'items', 'ores', 'netherite_scrap.json'), {
  replace: false, values: ['minecraft:ancient_debris']
});
writeJson(path.join(generated, 'data', 'forge', 'tags', 'blocks', 'ores', 'gold.json'), {
  replace: false, values: ['minecraft:gold_ore', 'minecraft:deepslate_gold_ore', 'minecraft:nether_gold_ore']
});
writeJson(path.join(generated, 'data', 'forge', 'tags', 'blocks', 'ores', 'netherite_scrap.json'), {
  replace: false, values: ['minecraft:ancient_debris']
});
writeJson(path.join(generated, 'data', 'basemetals', 'recipes', 'ancient_debris_crushing.json'), {
  type: 'basemetals:crushing', ingredient: { tag: 'forge:ores/netherite_scrap' },
  result: { item: 'minecraft:netherite_scrap', count: 2 }
});

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
  enabled: name !== 'copper',
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

// Raw ore is a processing ingredient, not a substitute for powder in alloys.
for (const material of [...rawMaterials, 'iron', 'gold', 'copper']) {
  const raw = rawMaterials.includes(material) ? `basemetals:${material}_raw` : `minecraft:raw_${material}`;
  writeJson(path.join(generated, 'data/forge/tags/items/raw_materials', `${material}.json`), {
    replace: false, values: [raw]
  });
  writeJson(path.join(recipesDirectory, `${material}_raw_crushing.json`), {
    type: 'basemetals:crushing', ingredient: { tag: `forge:raw_materials/${material}` },
    result: { item: `basemetals:${material}_powder`, count: 1 }
  });
  if (!rawMaterials.includes(material)) continue;

  writeJson(path.join(generated, 'assets/basemetals/models/item', `${material}_raw.json`), {
    parent: 'minecraft:item/generated', textures: { layer0: `basemetals:item/${material}_raw` }
  });
  const oldSmelting = JSON.parse(fs.readFileSync(path.join(recipesDirectory, `${material}_powder_smelting.json`), 'utf8'));
  for (const [method, ticks] of [['smelting', 200], ['blasting', 100]]) {
    writeJson(path.join(recipesDirectory, `${material}_raw_${method}.json`), {
      type: `minecraft:${method}`, ingredient: { tag: `forge:raw_materials/${material}` },
      result: `basemetals:${material}_ingot`, experience: oldSmelting.experience, cookingtime: ticks
    });
  }
}
writeJson(path.join(generated, 'data/forge/tags/items/raw_materials.json'), {
  replace: false, values: [...rawMaterials, 'iron', 'gold', 'copper'].map(name => `#forge:raw_materials/${name}`)
});

const itemPredicate = ingredient => ingredient.tag ? { tag: ingredient.tag } : { items: [ingredient.item] };

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
    if (fs.existsSync(oldUnlock) && !name.includes('_raw_')) remove(path.relative(root, oldUnlock));
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
      conditions: { items: [itemPredicate(discovery)] }
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
    const predicate = itemPredicate(ingredient);
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

for (const material of rawMaterials) {
  for (const method of ['smelting', 'blasting']) {
    const name = `${material}_raw_${method}`;
    writeJson(path.join(unlockDirectory, `${name}.json`), {
      parent: 'minecraft:recipes/root', criteria: {
        has_raw: { trigger: 'minecraft:inventory_changed', conditions: { items: [{ tag: `forge:raw_materials/${material}` }] } },
        has_recipe: { trigger: 'minecraft:recipe_unlocked', conditions: { recipe: `basemetals:${name}` } }
      }, requirements: [['has_raw', 'has_recipe']], rewards: { recipes: [`basemetals:${name}`] }
    });
  }
}

// Item predicates changed from "item" to "items" in 1.17, including old story advancements.
function updatePredicates(value) {
  if (Array.isArray(value)) return value.forEach(updatePredicates);
  if (!value || typeof value !== 'object') return;
  if (value.item && !value.type && !value.condition) {
    value.items = [value.item];
    delete value.item;
  }
  Object.values(value).forEach(updatePredicates);
}
for (const file of filesUnder(path.join(generated, 'data/basemetals/advancements'), '.json')) {
  const advancement = JSON.parse(fs.readFileSync(file, 'utf8'));
  // Icons still use "item"; only criterion conditions are predicates.
  Object.values(advancement.criteria || {}).forEach(criterion => updatePredicates(criterion.conditions));
  writeJson(file, advancement);
}

const configTranslations = require('./config_translations.json');
const playerTranslations = require('./player_translations.json');
const bucketTranslations = require('./bucket_translations.json');
const rawNameTemplates = {
  en_us: 'Raw %s', de_de: '%s-Erzbrocken', es_es: '%s en bruto', fr_fr: '%s brut',
  it_it: '%s grezzo', ja_jp: '%sの原石', nl_nl: 'Ruw %s', pt_br: '%s bruto',
  ru_ru: 'Рудное сырьё: %s', zh_cn: '粗%s', zh_tw: '粗%s'
};
for (const file of filesUnder(path.join(generated, 'assets', 'basemetals', 'lang'), '.json')) {
  const locale = path.basename(file, '.json');
  const source = configTranslations.aliases[locale] || locale;
  const text = configTranslations.locales[source];
  if (!text) throw new Error(`Missing configuration translation: ${locale}`);
  const playerText = playerTranslations.locales[source];
  if (!playerText) throw new Error(`Missing player translation: ${locale}`);
  const language = JSON.parse(fs.readFileSync(file, 'utf8'));
  Object.assign(language, text, playerText);
  const rawTemplate = rawNameTemplates[source];
  if (!rawTemplate) throw new Error(`Missing raw-material translation: ${locale}`);
  for (const material of rawMaterials) {
    language[`item.basemetals.${material}_raw`] = rawTemplate.replace('%s', language[`material.${material}.name`]);
  }

  const buckets = bucketTranslations.locales[source];
  if (!buckets) throw new Error(`Missing bucket translations: ${locale}`);
  for (const bucket of manifest.new_items.filter(id => id.endsWith('_bucket'))) {
    const id = bucket.substring(bucket.indexOf(':') + 1);
    const material = id.replace(/_bucket$/, '');
    const name = buckets[material];
    if (!name) throw new Error(`Missing bucket translation: ${locale}/${material}`);
    language[`item.bucket.${material}`] = name;
    language[`item.basemetals.${id}`] = name;
  }

  writeJson(file, language);
}

console.log(verify ? 'Minecraft 1.17.1 generated resources are current.' : 'Generated Minecraft 1.17.1 Base Metals resources.');
