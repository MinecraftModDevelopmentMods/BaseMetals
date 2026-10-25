// Each profile uses its own worlds. Never launch Minecraft against a source fixture.
const fs = require('fs');
const path = require('path');
const crypto = require('crypto');
const { spawn } = require('child_process');
const { serverArguments } = require('./forge_launcher');
const { isVanillaPaintingSnapshot } = require('./painting_registry');

const spec = JSON.parse(fs.readFileSync(process.argv[2], 'utf8'));
const phase = process.argv[3] || 'upgrade';
if (!['capture', 'upgrade'].includes(phase)) {
    throw new Error('Expected capture or upgrade as the test phase.');
}
const root = path.resolve(spec.output);
const captureVersion = spec.captureVersion || '1.10.2';
if (fs.existsSync(root) && fs.readdirSync(root).length && phase === 'capture') {
    throw new Error('Choose a new, empty capture output directory.');
}
fs.mkdirSync(root, { recursive: true });

function files(directory) {
    return fs.readdirSync(directory, { withFileTypes: true }).sort((a, b) => a.name.localeCompare(b.name))
        .flatMap(entry => entry.isDirectory() ? files(path.join(directory, entry.name))
            : [path.join(directory, entry.name)]);
}

function digest(file) {
    return crypto.createHash('sha256').update(fs.readFileSync(file)).digest('hex');
}

function treeDigest(directory) {
    const hash = crypto.createHash('sha256');
    for (const file of files(directory)) {
        hash.update(path.relative(directory, file).replace(/\\/g, '/') + '\n' + digest(file) + '\n');
    }
    return hash.digest('hex');
}

function prepare(directory, runtime, launcher, mods) {
    if (fs.existsSync(directory)) throw new Error('Refusing to overwrite ' + directory);
    fs.mkdirSync(path.join(directory, 'mods'), { recursive: true });
    if (phase === 'capture') {
        for (const file of [launcher, 'minecraft_server.' + captureVersion + '.jar']) {
            fs.copyFileSync(path.join(runtime, file), path.join(directory, file));
        }
        fs.cpSync(path.join(runtime, 'libraries'), path.join(directory, 'libraries'), { recursive: true });
    }
    for (const mod of mods) {
        const file = typeof mod === 'string' ? mod : mod.path;
        if (mod.sha256 && digest(file).toLowerCase() !== mod.sha256.toLowerCase()) {
            throw new Error('Unexpected release bytes: ' + file);
        }
        fs.copyFileSync(file, path.join(directory, 'mods', path.basename(file)));
    }
    fs.writeFileSync(path.join(directory, 'eula.txt'), 'eula=true\n');
    for (const file of ['banned-players.json', 'banned-ips.json', 'ops.json', 'whitelist.json']) {
        fs.writeFileSync(path.join(directory, file), '[]\n');
    }
    fs.writeFileSync(path.join(directory, 'server.properties'),
        'level-name=world\nlevel-seed=8675309\n'
        + (phase === 'capture'
            ? 'level-type=FLAT\ngenerator-settings=3;minecraft:bedrock,2*minecraft:dirt,minecraft:grass;1;\n'
            : 'level-type=default\ngenerator-settings=\n')
        + 'online-mode=false\nserver-port=0\nview-distance=4\nspawn-protection=0\nmax-tick-time=-1\n'
        + 'generate-structures=false\n');
}

function checkUpgradeLog(log) {
    // Forge reports these removed vanilla names when it reads an older registry snapshot.
    const retiredVanillaEntries = new Set([
        'minecraft:grass_path',
        // These names were changed by vanilla's 1.20.5 data fixes.
        'minecraft:grass', 'minecraft:scute', 'minecraft:sweeping', 'minecraft:empty',
        'minecraft:entity.parrot.imitate.enderman', 'minecraft:entity.parrot.imitate.panda',
        'minecraft:entity.parrot.imitate.polar_bear', 'minecraft:entity.parrot.imitate.wolf',
        'minecraft:entity.parrot.imitate.zombie_pigman',
        'minecraft:zombie_pigman', 'minecraft:zombie_pigman_spawn_egg',
        'minecraft:golem_last_seen_time', 'minecraft:opened_doors',
        'minecraft:nitwit', 'minecraft:unemployed',
        'minecraft:pottery_shard_archer', 'minecraft:pottery_shard_arms_up',
        'minecraft:pottery_shard_prize', 'minecraft:pottery_shard_skull',
        'minecraft:item.brush.brush_sand_completed', 'minecraft:item.brush.brushing',
        'minecraft:music.overworld.jungle_and_forest',
        'minecraft:entity.zombie_pigman.ambient', 'minecraft:entity.zombie_pigman.angry',
        'minecraft:entity.zombie_pigman.death', 'minecraft:entity.zombie_pigman.hurt',
        'minecraft:music.nether', 'minecraft:item.sweet_berries.pick_from_bush'
    ]);
    // Minecraft's Caves and Cliffs data fix folds these biomes into their new names.
    for (const name of ['badlands_plateau', 'bamboo_jungle_hills', 'birch_forest_hills', 'dark_forest_hills',
        'deep_warm_ocean', 'desert_hills', 'desert_lakes', 'giant_spruce_taiga', 'giant_spruce_taiga_hills',
        'giant_tree_taiga', 'giant_tree_taiga_hills', 'gravelly_mountains', 'jungle_edge', 'jungle_hills',
        'modified_badlands_plateau', 'modified_gravelly_mountains', 'modified_jungle', 'modified_jungle_edge',
        'modified_wooded_badlands_plateau', 'mountain_edge', 'mountains', 'mushroom_field_shore',
        'shattered_savanna', 'shattered_savanna_plateau', 'snowy_mountains', 'snowy_taiga_hills',
        'snowy_taiga_mountains', 'snowy_tundra', 'stone_shore', 'swamp_hills', 'taiga_hills', 'taiga_mountains',
        'tall_birch_forest', 'tall_birch_hills', 'wooded_badlands_plateau', 'wooded_hills', 'wooded_mountains']) {
        retiredVanillaEntries.add('minecraft:' + name);
    }
    const acceptedEntries = new Set();
    const lines = log.split(/\r?\n/);

    for (let index = 0; index < lines.length; index++) {
        const line = lines[index];
        if (!/\/(?:ERROR|FATAL)\]/.test(line)) continue;

        if (phase === 'upgrade' && line.includes('Unidentified mapping from registry minecraft:')) {
            const entries = [];
            for (let next = index + 1; next < lines.length && lines[next].trim(); next++) {
                const entry = /^\s+(minecraft:[\w.]+): \d+$/.exec(lines[next]);
                if (!entry) break;
                entries.push(entry[1]);
            }

            const registry = line.slice(line.indexOf('Unidentified mapping from registry ')
                + 'Unidentified mapping from registry '.length).trim();
            const knownPaintingSnapshot = isVanillaPaintingSnapshot(registry, entries);
            const renamedTrialSpawnerSounds = registry === 'minecraft:sound_event'
                && entries.length > 0 && entries.every(entry => [
                    'minecraft:block.trial_spawner.ambient_charged',
                    'minecraft:block.trial_spawner.charge_activate'
                ].includes(entry));

            if (knownPaintingSnapshot || renamedTrialSpawnerSounds
                || entries.length && entries.every(entry => retiredVanillaEntries.has(entry))) {
                entries.forEach(entry => acceptedEntries.add(entry));
                continue;
            }
        }

        if (acceptedEntries.size
                && line.endsWith('There are unidentified mappings in this world - we are going to attempt to process anyway')) {
            continue;
        }

        throw new Error('Unexpected runtime error: ' + line);
    }

    return [...acceptedEntries].sort();
}

async function run(directory, launcher, label, marker, properties = []) {
    console.log('Starting ' + label);
    const output = fs.createWriteStream(path.join(directory, label + '.log'));
    const executable = phase === 'capture' ? spec.java8 : (spec.java || spec.java17);
    const launchArguments = phase === 'capture' ? ['-jar', launcher, 'nogui']
        : serverArguments(spec.serverRuntime, directory, '1.21.1', '52.1.0');
    const child = spawn(executable, ['-Xms256m', '-Xmx2g', ...properties, ...launchArguments],
        { cwd: directory, windowsHide: true, stdio: ['ignore', 'pipe', 'pipe'] });
    child.stdout.pipe(output, { end: false });
    child.stderr.pipe(output, { end: false });
    const timeout = setTimeout(() => child.kill(), 10 * 60 * 1000);
    const code = await new Promise((resolve, reject) => {
        child.on('error', reject);
        child.on('close', resolve);
    });
    clearTimeout(timeout);
    await new Promise(resolve => output.end(resolve));
    const log = fs.readFileSync(path.join(directory, label + '.log'), 'utf8') + '\n'
        + fs.readFileSync(path.join(directory, 'logs/latest.log'), 'utf8');
    fs.copyFileSync(path.join(directory, 'logs/latest.log'), path.join(directory, label + '-latest.log'));
    if (code !== 0 || !log.includes(marker) || /BASEMETALS_.* FAIL|Encountered an unexpected exception/.test(log)) {
        throw new Error(label + ' failed (exit ' + code + '); see ' + directory);
    }
    const crashDirectory = path.join(directory, 'crash-reports');
    if (fs.existsSync(crashDirectory) && fs.readdirSync(crashDirectory).length) {
        throw new Error(label + ' created a crash report; see ' + crashDirectory);
    }
    const vanillaChanges = checkUpgradeLog(log);
    if (vanillaChanges.length) {
        console.log(label + ' accepted historical vanilla registry names: ' + vanillaChanges.join(', '));
    }
    console.log(label + ' PASS');
    return [...new Set(log.split(/\r?\n/)
        .filter(line => line.includes('BASEMETALS_') && /PASS|VERIFIED/.test(line)))];
}

(async () => {
    const results = [];
    for (const profile of spec.profiles) {
        const directory = path.join(root, phase, profile.id);
        results.push('Profile: ' + profile.id);
        if (phase === 'capture') {
            const mods = profile.mods || profile.mods110;
            if (!mods) continue;
            const launcher = spec.captureLauncher || 'forge-1.10.2-12.18.3.2511-universal.jar';
            prepare(directory, spec.captureRuntime || spec.runtime110, launcher, [...mods, spec.captureJar]);
            const properties = ['-Dlegacycapture.minecraft=' + captureVersion];
            results.push(...await run(directory, launcher, 'source-first', 'BASEMETALS_LEGACY_CAPTURE PASS', properties));
            results.push(...await run(directory, launcher, 'source-reload', 'BASEMETALS_LEGACY_CAPTURE_RELOAD PASS', properties));
        } else {
            const source = profile.world || path.join(root, 'capture', profile.id, 'world');
            const before = profile.fresh ? null : treeDigest(source);
            const launcher = null;
            prepare(directory, spec.serverRuntime, launcher,
                [spec.modJar, spec.probeJar, spec.oreSpawn, ...(profile.extraMods || [])]);
            if (!profile.fresh) fs.cpSync(source, path.join(directory, 'world'), { recursive: true });
            let expectedOreRules;
            const oreProfileFile = path.join(directory, 'world/serverconfig/orespawn-worldgen.json');
            if (profile.oreOverrides) {
                const geology = JSON.parse(fs.readFileSync(oreProfileFile, 'utf8'));
                for (const [id, values] of Object.entries(profile.oreOverrides)) {
                    if (!id.startsWith('basemetals:') || !geology.ores[id]) throw new Error('Unknown fixture ore: ' + id);
                    Object.assign(geology.ores[id], values);
                }
                expectedOreRules = JSON.stringify(geology.ores);
                fs.writeFileSync(oreProfileFile, JSON.stringify(geology, null, 2) + '\n');
            }
            if (profile.contentMode) {
                if (!['high_fantasy', 'low_fantasy'].includes(profile.contentMode)) {
                    throw new Error('Invalid fixture content mode: ' + profile.contentMode);
                }
                fs.mkdirSync(path.join(directory, 'config'), { recursive: true });
                fs.writeFileSync(path.join(directory, 'config/basemetals-common.toml'),
                    'contentMode="' + profile.contentMode + '"\n');
            }
            for (const pack of profile.dataPacks || []) {
                fs.cpSync(pack, path.join(directory, 'world/datapacks', path.basename(pack)), { recursive: true });
            }
            if (profile.advancementFixture) {
                const fixture = JSON.parse(fs.readFileSync(profile.advancementFixture, 'utf8'));
                const world = path.join(directory, 'world');
                fs.mkdirSync(path.join(world, 'advancements'), { recursive: true });
                fs.writeFileSync(path.join(world, 'advancements', fixture.uuid + '.json'),
                    JSON.stringify(fixture.progress, null, 2) + '\n');
                fs.copyFileSync(profile.advancementFixture,
                    path.join(world, 'basemetals_advancement_upgrade_fixture.json'));
            }
            const manifestFile = path.join(directory, 'world/legacy_registry_manifest_runtime.json');
            let fullManifest;
            if (profile.lateChunks && fs.existsSync(manifestFile)) {
                fullManifest = fs.readFileSync(manifestFile);
                const early = JSON.parse(fullManifest);
                let deferred = 0;
                for (const block of early.blocks) {
                    const states = block.states.filter(state => (state.dimension || 0) === 0);
                    deferred += block.states.length - states.length;
                    block.states = states;
                }
                if (!deferred) throw new Error('The late-chunk fixture has no saved Nether or End blocks');
                fs.writeFileSync(manifestFile, JSON.stringify(early, null, 2) + '\n');
                results.push(profile.id + ' deferred_dimension_states=' + deferred);
            }
            const properties = profile.fixture === false ? [] : ['-Dbasemetals.upgradeWorldSmoke=true'];
            properties.push(...(profile.properties || []));
            results.push(...await run(directory, launcher, 'upgrade-first', profile.marker, properties));
            results.push(...await run(directory, launcher, 'upgrade-reload', profile.marker, properties));
            if (fullManifest) {
                fs.writeFileSync(manifestFile, fullManifest);
                results.push(...await run(directory, launcher, 'upgrade-later-chunks', profile.marker, properties));
            }
            if (expectedOreRules) {
                const actual = JSON.stringify(JSON.parse(fs.readFileSync(oreProfileFile, 'utf8')).ores);
                if (actual !== expectedOreRules) throw new Error('The explicit ore-rule overrides changed');
                results.push(profile.id + ' explicit_ore_overrides=unchanged');
            }
            const after = profile.fresh ? null : treeDigest(source);
            if (before !== after) throw new Error('Source fixture changed: ' + source);
            if (!profile.fresh) results.push(profile.id + ' source_sha256=' + before + ' unchanged=true');
        }
        fs.writeFileSync(path.join(root, phase + '-results.txt'), results.join('\n') + '\n');
    }
})().catch(error => { console.error(error); process.exitCode = 1; });
