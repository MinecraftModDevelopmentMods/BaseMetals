'use strict';

const fs = require('fs');
const path = require('path');

// Forge 37 supplies the module-path launch arguments in its installed metadata.
function serverArguments(runtime, directory, version, forge) {
    const platform = process.platform === 'win32' ? 'win' : 'unix';
    const source = path.join(runtime, 'libraries/net/minecraftforge/forge',
        `${version}-${forge}`, `${platform}_args.txt`);
    const libraries = path.join(runtime, 'libraries').replace(/\\/g, '/');
    let contents = fs.readFileSync(source, 'utf8')
        .replaceAll('libraries/', libraries + '/')
        .replace('-DlibraryDirectory=libraries', '-DlibraryDirectory=' + libraries);
    contents = contents.split(/\r?\n/).map(line => {
        if (line.startsWith('-p ')) return '-p "' + line.substring(3) + '"';
        if (line.startsWith('-D') && line.includes(libraries)) return '"' + line + '"';
        return line;
    }).join('\n');
    const staged = path.join(directory, 'forge-arguments.txt');
    fs.writeFileSync(staged, contents);
    return ['--add-opens', 'java.base/java.lang.invoke=ALL-UNNAMED', '@' + staged, 'nogui'];
}

function clientLaunch(runtime, version, forge) {
    const candidates = [`${version}-forge-${forge}`, `forge-${forge}`];
    const id = candidates.find(value => fs.existsSync(path.join(runtime, 'versions', value, value + '.json')));
    if (!id) throw new Error('Missing installed Forge client metadata.');
    const metadata = JSON.parse(fs.readFileSync(path.join(runtime, 'versions', id, id + '.json')));
    const vanilla = JSON.parse(fs.readFileSync(path.join(runtime, 'versions', version, version + '.json')));
    const libraries = new Map();
    const osName = process.platform === 'win32' ? 'windows' : process.platform === 'darwin' ? 'osx' : 'linux';
    for (const item of [vanilla, metadata]) {
        for (const library of item.libraries) {
            let allowed = !library.rules || library.rules.length === 0;
            for (const rule of library.rules || []) {
                if (!rule.os || (rule.os.name === osName && (!rule.os.arch || rule.os.arch === 'x86_64'))) {
                    allowed = rule.action === 'allow';
                }
            }
            if (allowed) {
                const artifact = path.join(runtime, 'libraries', library.downloads.artifact.path);
                if (!fs.existsSync(artifact)) throw new Error('Missing client library: ' + artifact);
                libraries.set(library.name.split(':').slice(0, 2).join(':'), artifact);
            }
        }
    }
    const classpath = [...libraries.values(), path.join(runtime, 'versions', version, `${version}.jar`)].join(path.delimiter);
    const replacements = { library_directory: path.join(runtime, 'libraries'),
        classpath_separator: path.delimiter, version_name: metadata.inheritsFrom };
    const jvm = metadata.arguments.jvm.map(value => {
        for (const [key, replacement] of Object.entries(replacements)) {
            value = value.replaceAll('${' + key + '}', replacement);
        }
        if (value.includes('${')) throw new Error('Unresolved client argument: ' + value);
        return value;
    });
    const natives = candidates.map(value => path.join(runtime, 'natives', value))
        .find(directory => fs.existsSync(directory)) || path.join(runtime, 'natives');
    return { metadata, vanilla, natives,
        args: [...jvm, '--add-opens', 'java.base/java.lang.invoke=ALL-UNNAMED',
            `-Djava.library.path=${natives}`, '-cp', classpath, metadata.mainClass] };
}

module.exports = { serverArguments, clientLaunch };
