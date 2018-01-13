const fs = require("fs"),
      util = require("util"),
      console = require("console");


const original_data = JSON.parse( fs.readFileSync( "basemetals.json" ) );

const new_data = { "version": "2.0", "spawns": {} };

for( var i = 0; i < original_data.dimensions.length; i++ ) {
    var this_dim = original_data.dimensions[i];
    var curDim = this_dim.hasOwnProperty('dimension')?this_dim.dimension:Math.PI;
    var spawns = this_dim.ores;
    for( var j = 0; j < spawns.length; j++ ) {
	let dimBlock = { "includes": [ curDim ] };
	if( curDim == Math.PI ) dimBlock = { "excludes": [ -1, 1 ] };
	var ns = { "retrogen": false, "enabled": true, "feature": spawns[j].feature, "replaces": spawns[j].replace_block, "dimensions": dimBlock , "biomes": { "excludes": [] }, "parameters": spawns[j].parameters };
	ns.block = { "name": spawns[j].block, "chance": 100 };
	if( spawns[j].hasOwnProperty('metaData') ) {
	    ns.block.metaData = spawns[j].metaData;
	} else if( spawns[j].hasOwnProperty('state') ) {
	    ns.block.state = spawns[j].state;
	}
	var name = spawns[j].block.split(":")[1];
	new_data.spawns[name] = ns;
    }
}

var out =  JSON.stringify( new_data, null, "\t" );

fs.writeFileSync( "basemetals-new.json", out );
