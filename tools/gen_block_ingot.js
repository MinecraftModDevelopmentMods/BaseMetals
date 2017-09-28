const util = require('util'),
      fs = require('fs');

const mats =  [ "Adamantine", "Antimony", "Bismuth", "ColdIron", "Copper", "Lead", "Nickel", "Platinum", "Silver", "StarSteel", "Tin", "Zinc",
		'Aquarium', 'Brass', 'Bronze', 'Cupronickel', 'Electrum', 'Invar', 'Mithril', 'Pewter', 'Steel' ];

function toTitleCase(str)
{
    return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
}


function getBlockOreDict(str) {
    return { type: "forge:ore_dict", ore: `block${toTitleCase(str.toLowerCase())}` };
}

function getIngotName(str) {
    return { item: `basemetals:${str.toLowerCase()}_ingot`, count: 9 };
}

function getConditions(str) {
    return { type: "forge:and",
	     values: [
		 { type: "basemetals:enabled",
		   optionName: "material",
		   optionValue: str },
		 { type: "basemetals:enabled",
		   optionName: "thing",
		   optionValue: "basics" } ] };
}

function getRecipeFor(str) {
    return { type: "forge:ore_shapeless",
	     ingredients: [ getBlockOreDict(str) ],
	     result: getIngotName(str),
	     group: "basics",
	     conditions: [ getConditions(str) ] };
}

for( let i = 0; i < mats.length; i++ ) {
    let f = `output/${mats[i].toLowerCase()}_block_ingot.json`;
    fs.writeFileSync( f, JSON.stringify( getRecipeFor(mats[i]), null, '\t' ) );
}
