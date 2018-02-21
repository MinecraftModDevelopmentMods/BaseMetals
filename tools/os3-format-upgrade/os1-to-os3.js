const fs = require("fs"),
      util = require("util"),
      console = require("console");

const input_file = process.argv[2];
const output_file = process.argv[3];

const original_data = JSON.parse( fs.readFileSync( input_file ) );

const new_data = { "version": "2.0", "spawns": {} };

for( var i = 0; i < original_data.dimensions.length; i++ ) {
    var this_dim = original_data.dimensions[i];
    let dimBlock = this_dim.dimension=="+"?[]:[this_dim.dimension];
    for( var j = 0; j < this_dim.ores.length; j++ ) {
	var this_spawn = this_dim.ores[j];
	var parameters = {};
	parameters.size = this_spawn.size;
	parameters.variation = this_spawn.variation;
	parameters.frequency = this_spawn.frequency;
	parameters.minHeight = this_spawn.minHeight;
	parameters.maxHeight = this_spawn.maxHeight;
	let biomeBlock = { "excludes": [] };
	if( this_spawn.hasOwnProperty("biomes") ) {
	    biomeBlock = this_spawn.biomes.length>0?{ "includes": this_dim.biomes }:{ "excludes": [] };
	}
	var ns = {
		"retrogen": false,
		"enabled": true,
		"feature": "default",
		"replaces": "default",
		"dimensions": dimBlock ,
		"biomes": biomeBlock,
		"parameters": parameters };
	var block = { "name": this_spawn.blockID, "chance": 100 };
	if( this_spawn.hasOwnProperty('metaData') ) {
	    block.metaData = this_spawn.metaData;
	} else if( this_spawn.hasOwnProperty('state') ) {
	    block.state = this_spawn.state;
	}
	var name = block.name.split(":")[1];
	ns.blocks = [ block ];
	new_data.spawns[name] = ns;
    }
}

var out =  JSON.stringify( new_data, null, "\t" );

fs.writeFileSync( output_file, out );
