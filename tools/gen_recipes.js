const fs = require('fs');

const base_mats =  [ "Adamantine", "Antimony", "Bismuth", "ColdIron", "Copper", "Lead", "Mercury", "Nickel", "Platinum", "Silver", "StarSteel", "Steel", "Tin", "Zinc" ];
const van_mats = [ "Diamond", "Emerald", "Gold", "Iron", "Stone", "Wood", "Quartz", "Obsidian", "Charcoal", "Coal" ];

const armor_mats = [ "Adamantine", "Antimony", "Bismuth", "Brass", "Bronze", "ColdIron", "Copper", "Cupronickel", "Diamond", "Electrum", "Emerald", "Gold", "Iron", "Lead",
		     "Nickel", "Obsidian", "Pewter", "Platinum", "Quartz", "Silver", "StarSteel", "Steel", "Tin", "Zinc" ];
const armor_pieces = [ 'boots', 'chestplate', 'helmet', 'leggings', 'shield' ];

const _factories = {
    "conditions": {
	"enabled":"com.mcmoddev.lib.recipe.conditions.ConditionEnabled",
	"hammer":"com.mcmoddev.lib.recipe.conditions.hammerEnabled",
	"plate":"com.mcmoddev.lib.recipe.conditions.PlateRepairEnabled"
    },
    "recipes": {
	"variableOutputOre":"com.mcmoddev.lib.recipe.factories.ConfigVariedOutput",
	"armor_repair":"com.mcmoddev.lib.recipe.factories.ArmorRepair"
    }
};

const alloy_mix = { 'Aquarium': { 'recipe': { 'type': 'forge:ore_shapeless',
					      'mix': [ { "type": "oredict", "name": "dustCopper" },
						       { "type": "oredict", "name": "dustCopper" },
						       { "type": "oredict", "name": "dustZinc" },
						       { "type": "oredict", "name": "dustPrismarine" } ] },
				  'output': 3 },
		    'Brass': { 'recipe': { 'type': 'forge:ore_shapeless',
					   'mix': [ { "type": "oredict", "name": "dustCopper" },
						    { "type": "oredict", "name": "dustCopper" },
						    { "type": "oredict", "name": "dustZinc" } ] },
				  'output': 3 },
		    'Bronze': { 'recipe': { 'type': 'forge:ore_shapeless',
					    'mix': [ { "type": "oredict", "name": "dustCopper" },
						     { "type": "oredict", "name": "dustCopper" },
						     { "type": "oredict", "name": "dustCopper" },
						     { "type": "oredict", "name": "dustTin" } ] },
				  'output': 4 },
		    'Cupronickel': { 'recipe': { 'type': 'forge:ore_shapeless',
						 'mix': [ { "type": "oredict", "name": "dustCopper" },
							  { "type": "oredict", "name": "dustCopper" },
							  { "type": "oredict", "name": "dustCopper" },
							  { "type": "oredict", "name": "dustNickel" } ] },
				  'output': 4 },
		    'Electrum': { 'recipe': { 'type': 'forge:ore_shapeless',
					      'mix': [ { "type": "oredict", "name": "dustGold" },
						       { "type": "oredict", "name": "dustSilver" } ] },
				  'output': 2 },
		    'Pewter': { 'recipe': { 'type': 'forge:ore_shapeless',
					    'mix': [ { "type": "oredict", "name": "dustTin" },
						     { "type": "oredict", "name": "dustCopper" },
						     { "type": "oredict", "name": "dustLead" } ] },
				'output': 3 } };
const alloy_conditions = { 'Aquarium': { 'enabled': [ 'Basics' ], 'materials': [ 'Copper', 'Zinc' ] },
			   'Brass': { 'enabled': [ 'Basics' ], 'materials': [ 'Copper', 'Zinc' ] },
			   'Bronze': { 'enabled': [ 'Basics' ], 'materials': [ 'Copper', 'Tin' ] },
			   'Cupronickel': { 'enabled': [ 'Basics'] , 'materials': [ 'Copper', 'Nickel' ] },
			   'Electrum': { 'enabled': [ 'Basics' ], 'materials': [ 'Silver' ] },
			   'Pewter': { 'enabled': [ 'Basics' ], 'materials': [ 'Copper', 'Lead', 'Tin' ] } };

const alloy_names = [ 'Aquarium', 'Brass', 'Bronze', 'Cupronickel', 'Electrum', 'Pewter' ];

const patterns_basic = {
    // Basics
    'nugget': { 'type': 'forge:ore_shapeless', 'input': 'INGOT', 'result': { 'mat': 'NUGGET', 'count': 9 }, 'config': { 'enabled': [ 'Basics'  ] }, "group": "basics" },
    'ingot': { 'type': 'forge:ore_shaped', 'input': [ 'xxx', 'xxx', 'xxx' ], 'key': { 'x': 'NUGGET' }, 'result': { 'mat': 'INGOT', 'count': 1 }, 'config': { 'enabled': [ 'Basics' ] }, "group": "basics" },
    'block': { 'type': 'forge:ore_shaped', 'input': [ 'xxx', 'xxx', 'xxx' ], 'key' : { 'x': 'INGOT' }, 'result': { 'mat': 'BLOCK', 'count': 1 }, 'config': { 'enabled': [ 'Basics' ] }, "group": "basics" },
    'powder': { 'type': 'forge:ore_shaped', 'input': [ 'xxx', 'xxx', 'xxx' ], 'key': { 'x': 'SMALLPOWDER' }, 'result': { 'mat': 'POWDER', 'count': 1 }, 'config': { 'enabled':  [ 'Basics' ] }, "group": "basics" },
    'smallpowder': { 'type': 'forge:ore_shapeless', 'input': 'POWDER', 'result': { 'mat': 'SMALLPOWDER', 'count': 9 }, 'config': { 'enabled': [ 'Basics' ] }, "group": "basics" },
    'blend': { 'type': 'forge:ore_shaped', 'input': [ 'xxx', 'xxx', 'xxx' ], 'key': { 'x': 'SMALLBLEND' }, 'result': { 'mat': 'BLEND', 'count': 1 }, 'config': { 'enabled': [ 'Basics' ] }, "group": "basics" },
    'smallblend': { 'type': 'forge:ore_shapeless', 'input': 'BLEND', 'result': { 'mat': 'SMALLBLEND', 'count': 9 }, 'config': { 'enabled': [ 'Basics' ] }, "group": "basics" },
    // Basic Tools
    'axe': { 'type': 'forge:ore_shaped', 'input': [ 'xx', 'xs', ' s' ], 'key': { 'x': 'INGOT', 's': "STICK" }, 'result': { 'mat': 'AXE', 'count': 1 }, 'config': { 'enabled': [ 'Basics', 'BasicTools' ] }, "group": "tools" },
    'pickaxe': { 'type': 'forge:ore_shaped', 'input': [ 'xxx', ' s', ' s' ], 'key': { 'x': 'INGOT', 's': "STICK" }, 'result': { 'mat': 'PICKAXE', 'count': 1 }, 'config': { 'enabled': [ 'Basics', 'BasicTools' ] }, "group": "tools" },
    'shovel': { 'type': 'forge:ore_shaped', 'input': [ ' x', ' s', ' s' ], 'key': { 'x': 'INGOT', 's': "STICK" }, 'result': { 'mat': 'SHOVEL', 'count': 1 }, 'config': { 'enabled': [ 'Basics', 'BasicTools' ] }, "group": "tools" },
    'hoe': { 'type': 'forge:ore_shaped', 'input': [ 'xx', ' s', ' s' ], 'key': { 'x': 'INGOT', 's': "STICK" }, 'result': { 'mat': 'HOE', 'count': 1 }, 'config': { 'enabled': [ 'Basics', 'BasicTools' ] }, "group": "tools" },
    'sword': { 'type': 'forge:ore_shaped', 'input': [ ' x', ' x', ' s' ], 'key': { 'x': 'INGOT', 's': "STICK" }, 'result': { 'mat': 'SWORD', 'count': 1 }, 'config': { 'enabled': [ 'Basics', 'BasicTools' ] }, "group": "weapon" },
    // Armor
    'boots': { 'type': 'forge:ore_shaped', 'input': [ 'x x', 'x x' ], 'key': { 'x': 'INGOT' }, 'result': { 'mat': 'BOOTS', 'count': 1 }, 'config': { 'enabled': [ 'Basics', 'Armor' ] }, "group": "armor" },
    'helmet': { 'type': 'forge:ore_shaped', 'input': [ 'xxx', 'x x' ], 'key': { 'x': 'INGOT' }, 'result': { 'mat': 'HELMET', 'count': 1 }, 'config': { 'enabled': [ 'Basics', 'Armor' ] }, "group": "armor" },
    'chestplate': { 'type': 'forge:ore_shaped', 'input': [ 'x x', 'xxx', 'xxx' ], 'key': { 'x': 'INGOT' }, 'result': { 'mat': 'CHESTPLATE', 'count': 1 }, 'config': { 'enabled': [ 'Basics', 'Armor' ] }, "group": "armor" },
    'leggings': { 'type': 'forge:ore_shaped', 'input': [ 'xxx', 'x x', 'x x' ], 'key': { 'x': 'INGOT' }, 'result': { 'mat': 'LEGGINGS', 'count': 1 }, 'config': { 'enabled': [ 'Basics', 'Armor' ] }, "group": "armor" },
    // Misc.
    'arrow': { 'type': 'forge:ore_shaped', 'input': [ 'x', 'y', 'z' ], 'key': { 'x': 'NUGGET', 'y': 'ROD', 'z': 'FEATHER' }, 'result': { 'mat': 'ARROW', 'count': 4 }, 'config': { 'enabled': [ 'BowAndArrow', 'Basics' ] }, "group": "weapon" },
    'bars_1': { 'type': 'forge:ore_shaped', 'input': [ 'xxx', 'xxx' ], 'key': { 'x': 'INGOT' }, 'result': { 'mat': 'BARS', 'count': 16 }, 'config': { 'enabled': [ 'Bars', 'Basics' ] }, "group": "misc" },
    'bars_2': { 'type': 'forge:ore_shaped', 'input': [ 'xxx' ], 'key': { 'x': 'ROD' }, 'result': { 'mat': 'BARS', 'count': 4 }, 'config': { 'enabled': [ 'Bars', 'Rod' ] }, "group": "misc" },
    'bow': { 'type': 'forge:ore_shaped', 'input': [ ' xs', 'x s', ' xs' ], 'key': { 'x': 'ROD', 's': 'STRING' }, 'result': { 'mat': 'BOW', 'count': 1 }, 'config': { 'enabled': [ 'BowAndArrow', 'Rod' ] }, "group": "weapon"  },    
    'button': { 'type': 'forge:ore_shaped', 'input': [ 'x', 'x' ], 'key': { 'x': 'NUGGET' }, 'result': { 'mat': 'BUTTON', 'count': 1 }, 'config': { 'enabled': [ 'Button', 'Basics' ] }, "group": "misc" },
    'bolt': { 'type': 'forge:ore_shaped', 'input': [ 'x', 'x', 'y' ], 'key': { 'x': 'ROD', 'y': 'FEATHER' }, 'result': { 'mat': 'BOLT', 'count': 4 }, 'config': { 'enabled': [ 'CrossbowAndBolts', 'Rod' ] }, "group": "weapon"  },
    'crackhammer': { 'type': 'forge:ore_shaped', 'input': [ 'x', 'y', 'y' ], 'key': { 'x': 'BLOCK', 'y': 'STICK' }, 'result': { 'mat': 'CRACKHAMMER', 'count': 1 }, 'config': { 'enabled': [ 'Basics', 'hammerEnabled' ] }, "group": "tools" },
    'crossbow': { 'type': 'forge:ore_shaped', 'input': [ 'zxx', ' yx', 'x z' ], 'key': { 'x': 'ROD', 'y': 'STRING', 'z': 'GEAR' }, 'result': { 'mat': 'CROSSBOW', 'count': 1 }, 'config': { 'enabled': [ 'CrossbowAndBolts', 'Rod', 'Gear' ] }, "group": "weapon"  },
    'door': { 'type': 'forge:ore_shaped', 'input': [ 'xx', 'xx', 'xx' ], 'key': { 'x': 'INGOT' }, 'result': { 'mat': 'DOOR', 'count': 3 }, 'config': { 'enabled': [ 'Door', 'Basics' ] }, "group": "misc" },
    'fishingrod': { 'type': 'forge:ore_shaped', 'input': [ '  x', ' xy', 'x y' ], 'key': { 'x': 'ROD', 'y': 'STRING' }, 'result': { 'mat': 'FISHING_ROD', 'count': 1 }, 'config': { 'enabled': [ 'FishingRod', 'Rod' ] }, "group": "misc" },
    'gear': { 'type': 'basemetals:variableOutputOre', 'input': [' x ', 'xrx', ' x ' ], 'key': { 'x': 'INGOT', 'r': 'ROD' }, 'result': { 'mat': 'GEAR' }, 'config': { 'enabled': [ 'Gear', 'Basics' ] }, "group": "misc" },
    'horsearmor': { 'type': 'forge:ore_shaped', 'input': [ '  x', 'xyx', 'xxx' ], 'key': { 'x': 'INGOT', 'y': 'WOOL' }, 'result': { 'mat': 'HORSE_ARMOR', 'count': 1 }, 'config': { 'enabled': [ 'HorseArmor', 'Basics' ] }, "group": "armor" },
    'lever': { 'type': 'forge:ore_shaped', 'input': [ 'x', 'y' ], 'key': { 'x': 'ROD', 'y': 'INGOT' }, 'result': { 'mat': 'LEVER', 'count': 1 }, 'config': { 'enabled': [ 'Lever', 'Rod', 'Basics' ] }, "group": "misc" },
    'plate': { 'type': 'basemetals:variableOutputOre', 'input': [ 'xx', 'xx' ], 'key': { 'x': 'INGOT' }, 'result': { 'mat': 'PLATE' }, 'config': { 'enabled': [ 'Plate', 'Basics' ] }, "group": "misc" },
    'pressureplate': { 'type': 'forge:ore_shaped', 'input': [ 'xx' ], 'key': { 'x': 'INGOT' }, 'result': { 'mat': 'PRESSURE_PLATE', 'count': 1 }, 'config': { 'enabled': [ 'PressurePlate', 'Basics' ] }, "group": "misc" },
    'rod': { 'type': 'forge:ore_shaped', 'input': [ 'x', 'x' ], 'key' : { 'x': 'INGOT' }, 'result': { 'mat': 'ROD', 'count': 4 }, 'config': { 'enabled': [ 'Rod', 'Basics' ] }, "group": "misc" },
    'shears': { 'type': 'forge:ore_shaped', 'input': [ ' x', 'x' ], 'key': { 'x': 'INGOT' }, 'result': { 'mat': 'INGOT', 'count': 1 }, 'config': { 'enabled': [ 'Shears', 'Basics' ] }, "group": "tools" },
    'shield': { 'type': 'forge:ore_shaped', 'input': [ 'xyx', 'xxx', ' x ' ], 'key': { 'x': 'INGOT', 'y': 'WOOD_PLANK' }, 'result': { 'mat': 'SHIELD', 'count': 1 }, 'config': { 'enabled': [ 'Shields', 'Basics' ] }, "group": "armor" },
    'slab': { 'type': 'forge:ore_shaped', 'input': [ 'xxx' ], 'key' : { 'x': 'BLOCK' }, 'result': { 'mat': 'SLAB', 'count': 6 }, 'config': { 'enabled': [ 'Slab', 'Basics' ] }, "group": "misc" },
    'stairs': { 'type': 'forge:ore_shaped', 'input': [ 'x', 'xx', 'xxx' ], 'key' : { 'x': 'BLOCK' }, 'result': { 'mat': 'STAIRS', 'count': 4 }, 'config': { 'enabled': [ 'Stairs', 'Basics' ] }, "group": "misc" },
    'trapdoor': { 'type': 'forge:ore_shaped', 'input': [ 'xx', 'xx' ], 'key': { 'x': 'INGOT' }, 'result': { 'mat': 'TRAPDOOR', 'count': 1 }, 'config': { 'enabled': [ 'Trapdoors', 'Basics' ] }, "group": "misc" },
    'wall': { 'type': 'forge:ore_shaped', 'input': [ 'xxx', 'xxx' ], 'key' : { 'x': 'BLOCK' }, 'result': { 'mat': 'WALL', 'count': 6 }, 'config': { 'enabled': [ 'Wall', 'Basics' ] }, "group": "misc" }
};

const recipes = [ 'nugget','ingot','block','powder','smallpowder','blend','smallblend','axe','pickaxe','shovel','hoe','sword',
		  'boots','helmet','chestplate','leggings','arrow','bars_1','bars_2','bow','button','bolt','crackhammer',
		  'crossbow','door','fishingrod','gear','horsearmor','lever','plate','pressureplate','rod','shears','shield','slab','stairs','trapdoor','wall' ];

const vanilla_map = { "Diamond": [ 'powder', 'smallpowder', 'arrow', 'bars_1', 'bars_2', 'bow', 'button', 'bolt', 'crackhammer', 'crossbow', 'door', 'fishingrod', 'gear', 'lever', 'plate', 'pressureplate', 'rod', 'stairs', 'slab', 'shield',
				   'shears', 'trapdoor', 'wall' ],
		      "Emerald": [ 'powder', 'smallpowder', 'sword', 'shovel', 'hoe', 'axe', 'pickaxe', 'boots', 'helmet', 'chestplate', 'leggings', 'arrow', 'bars_1', 'bars_2', 'bow', 'button', 'bolt', 'crackhammer', 'crossbow', 'door',
				   'fishingrod', 'gear', 'horsearmor', 'lever', 'plate', 'pressureplate', 'rod', 'stairs', 'slab', 'shield', 'shears', 'trapdoor', 'wall' ],
		      "Gold": [ 'powder', 'smallpowder', 'arrow', 'bars_1', 'bars_2', 'bow', 'button', 'bolt', 'crackhammer', 'crossbow', 'door', 'fishingrod', 'gear', 'lever', 'plate', 'pressureplate', 'rod', 'stairs', 'slab', 'shield', 'shears',
				'trapdoor', 'wall' ],
		      "Iron": [ 'powder', 'smallpowder', 'arrow', 'bars_2', 'bow', 'button', 'bolt', 'crackhammer', 'crossbow', 'fishingrod', 'gear', 'lever', 'plate', 'pressureplate', 'rod', 'stairs', 'slab', 'shield', 'trapdoor', 'wall' ],
		      "Stone": [ 'crackhammer' ],
		      "Wood": [ 'crackhammer' ],
		      "Quartz": [ 'powder', 'smallpowder', 'nugget', 'ingot', 'sword', 'shovel', 'hoe', 'axe', 'pickaxe', 'boots', 'helmet', 'chestplate', 'leggings', 'arrow', 'bars_1', 'bars_2', 'bow', 'button', 'bolt', 'crackhammer', 'crossbow', 'door',
				  'fishingrod', 'gear', 'horsearmor', 'lever', 'plate', 'pressureplate', 'rod', 'stairs', 'slab', 'shield', 'shears', 'trapdoor', 'wall' ],
		      "Obsidian": [ 'powder', 'smallpowder', 'nugget', 'ingot', 'block', 'sword', 'shovel', 'hoe', 'axe', 'pickaxe', 'boots', 'helmet', 'chestplate', 'leggings', 'arrow', 'bars_1', 'bars_2', 'bow', 'button', 'bolt', 'crackhammer', 'crossbow', 'door',
				    'fishingrod', 'gear', 'horsearmor', 'lever', 'plate', 'pressureplate', 'rod', 'stairs', 'slab', 'shield', 'shears', 'trapdoor', 'wall' ],
		      "Coal": [ 'powder', 'smallpowder', 'nugget', 'ingot' ],
		      "Charcoal": [ 'powder', 'smallpowder', 'block', 'nugget', 'ingot' ] };

function toTitleCase(str)
{
    return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
}

const OreDictMaps = {
    'NUGGET': (name) => { return `ore:nugget${name}`; },
    'INGOT': (name) => { return `ore:ingot${name}`; },
    'POWDER': (name) => { return `ore:dust${name}`; },
    'BLEND': (name) => { return `ore:blend${name}`; },
    'SMALLPOWDER': (name) => { return `ore:dustSmall${name}`; },
    'SMALLBLEND': (name) => { return `ore:blendSmall${name}`; },
    'STICK': (name) => { return 'ore:stickWood'; },
    'WOOL': (name) => { return 'minecraft:wool'; },
    'WOOD_PLANK': (name) => { return 'ore:plankWood'; },
    'STRING': (name) => { return 'minecraft:string'; },
    'BLOCK': (name) => { return `ore:block${name}`; }
};

function mapName(rawName, matName) {
    if( OreDictMaps.hasOwnProperty(rawName) ) {
	return OreDictMaps[rawName](matName);
    }

    let name = rawName.toLowerCase();
    let mname = matName.toLowerCase();
    
    return `basemetals:${mname}_${name}`;
}

function processKey(recipe_key, mat) {
    let res = {};
    var ks = Object.keys(recipe_key);
    for( let k = 0; k < ks.length; k++ ) {
	res[ks[k]] = processIngredient( recipe_key[ks[k]], mat );
    }
    return res;
}

function processOutputs(recipe_outputs, mat) {
    let res = {};
    res.item = processIngredient( recipe_outputs.mat, mat );
    if( recipe_outputs.count > 1 ) {
	res.count = recipe_outputs.count;
    }

    return res;
}

function processConditions( conditions, mat ) {
    var _conds = [ { "type": "basemetals:enabled",
			 "optionName": "material",
			 "optionValue": mat
		       } ];

    for( let c = 0; c < conditions.length; c++ ) {
	var cur = conditions[c];
	if( cur == 'hammerEnabled' ) {
	    _conds.push( { "type": "basemetals:hammer" } );
	} else {
	    _conds.push( { "type": "basemetals:enabled", "optionName": "thing", "optionValue": cur } );
	}
    }

    var res = [ {
	"type": "forge:and",
	"values": _conds
    } ];
    return res;
}

function processIngredients( inputs, mat ) {
    let res = [];
    for( let i = 0; i < inputs.length; i++ ) {
	res.push( processIngredient(inputs[i], mat) );
    }

    return res;
}

function processIngredient( input, mat ) {
    let res = {};

    let name = mapName(input,mat);
    if( name.startsWith("ore:") ) {
	res = { "type": "forge:ore_dict", "ore": name.split(':')[1] };
    } else {
	res = { "item": name };
    }

    return res;
}

function processRecipe( mat, rec ) {
    var this_recipe = rec;
    res = {}; // zero out the result on each loop
    res.type = this_recipe.type;
    switch( this_recipe.type ) {
    case 'forge:ore_shaped':
	res.result = processOutputs( this_recipe.result, mat );
	res.pattern = this_recipe.input;
	res.key = processKey(this_recipe.key, mat);
	res.conditions = processConditions( this_recipe.config.enabled, mat );	    
	break;
    case 'forge:ore_shapeless':
	res.result = processOutputs( this_recipe.result, mat );
	res.ingredients = processIngredients( this_recipe.input, mat );
	res.conditions = processConditions( this_recipe.config.enabled, mat );	    
	break;
    case 'basemetals:variableOutputOre':
	res.result = processOutputs( this_recipe.result, mat );
	res.pattern = this_recipe.input;
	res.key = processKey(this_recipe.key, mat);
	res.conditions = processConditions( this_recipe.config.enabled, mat );	    
	break;
    }
    res.group = this_recipe.group;
    return res;
}

function processMat( mat ) {
    for( let x = 0; x < recipes.length; x++ ) {
	let res = processRecipe( mat, patterns_basic[recipes[x]] );
	let matFileName = mat.toLowerCase();
	let recFileName = recipes[x].toLowerCase();
	fs.writeFileSync( `output/${matFileName}_${recFileName}.json`, JSON.stringify(res, null, '\t' ) );
    }
}

function processAlloyConditions( conds, mat ) {
    let m = conds.materials;
    let res = [];
    for( let i = 0; i < m.length; i++ ) {
	res.push( { "type":"basemetals:enabled", "optionName":"material","optionValue":m[i] } );
    }

    let e = conds.enabled;
    for( let x = 0; x < e.length; e++ ) {
	res.push( { "type":"basemetals:enabled", "optionName":"thing", "optionValue":e[x] } );
    }
    return res;
}

function processAlloy( mat ) {
    var name = mat;
    var this_blend = mapName( 'BLEND', name );
    var this_mix = alloy_mix[name];
    var conds = alloy_conditions[name];
    var res = {};
    res.result = { "item": this_blend, "count": this_mix.output };
    res.type = this_mix.recipe.type;
    res.ingredients = [];
    for( let x = 0; x < this_mix.recipe.mix.length; x++ ) {
	let name = this_mix.recipe.mix[x].name;
	res.ingredients.push( `ore:${name}` );
    }
    res.group = "alloys";
    let tnc = processAlloyConditions(conds,name);
    tnc.push( { "type":"basemetals:enabled", "optionName":"material","optionValue":name } );
    
    res.conditions = [ { "type":"forge:and", "values": tnc } ];
    return res;
}

for( let i = 0; i < base_mats.length; i++ ) {
    let mat = base_mats[i];
    processMat( mat );
}

for( let x = 0; x < alloy_names.length; x++ ) {
    let mat = alloy_names[x];
    let res_blend_1 = processAlloy( mat );
    let res_small_blend = processRecipe( mat, patterns_basic['smallblend'] );
    let res_blend_2 = processRecipe( mat, patterns_basic['blend'] );

    let matFileName = mat.toLowerCase();
    fs.writeFileSync( `output/${matFileName}_blend_base.json`, JSON.stringify(res_blend_1, null, '\t' ) );
    fs.writeFileSync( `output/${matFileName}_blend_smallblend.json`, JSON.stringify(res_blend_2, null, '\t' ) );
    fs.writeFileSync( `output/${matFileName}_smallblend_blend.json`, JSON.stringify(res_small_blend, null, '\t' ) );
}

for( let v = 0; v < van_mats.length; v++ ) {
    let recipes = vanilla_map[van_mats[v]];
    let matFileName = van_mats[v].toLowerCase();
    for( let r = 0; r < recipes.length; r++ ) {
	let recName = recipes[r];
	let recipe = patterns_basic[recName];
	let res = processRecipe( van_mats[v], recipe );
	res.group = "vanilla";
	let recFileName = recName.toLowerCase();

	fs.writeFileSync( `output/${matFileName}_${recFileName}.json`, JSON.stringify( res, null, '\t' ) );
    }
}

for( let am = 0; am < armor_mats.length; am++ ) {
    let this_mat = armor_mats[am];
    for( let at = 0; at < armor_pieces.length; at++ ) {
	let this_type = armor_pieces[at];
	let res = { "type": "basemetals:armor_repair", "armorType": "basemetals:armor_repair", "material":this_mat, "type":this_type, "conditions": [
	    { "type": "forge:and",
	      "values": [
		  {
		      "type": "basemetals:enabled",
		      "optionName": "material",
		      "optionValue": this_mat
		  },
		  {
		      "type": "basemetals:enabled",
		      "optionName": "thing",
		      "optionValue": "Armor"
		  },
		  {
		      "type": "basemetals:enabled",
		      "optionName": "thing",
		      "optionValue": "Plate"
		  },
		  {
		      "type": "basemetals:plate"
		  }
	      ]
	    }
	] };
	let fName = `output/${this_mat}_${this_type}_plate_repair.json`.toLowerCase();
	fs.writeFileSync( fName, JSON.stringify( res, null, '\t' ) );
    }
}
    
fs.writeFileSync( 'output/_factories.json', JSON.stringify( _factories, null, '\t' ) );

console.log( 'done!' );
