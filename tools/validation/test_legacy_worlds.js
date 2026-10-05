// Each profile uses its own worlds. Never launch Minecraft against a source fixture.
const fs = require('fs');
const path = require('path');
const crypto = require('crypto');
const { spawn } = require('child_process');

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
    for (const file of [launcher, 'minecraft_server.' + (phase === 'capture' ? captureVersion : '1.14.4') + '.jar']) {
        fs.copyFileSync(path.join(runtime, file), path.join(directory, file));
    }
    fs.cpSync(path.join(runtime, 'libraries'), path.join(directory, 'libraries'), { recursive: true });
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
        'level-name=world\nlevel-seed=8675309\nlevel-type=FLAT\ngenerator-settings=3;minecraft:bedrock,2*minecraft:dirt,minecraft:grass;1;\n'
        + 'online-mode=false\nserver-port=0\nview-distance=4\nspawn-protection=0\nmax-tick-time=-1\n'
        + 'generate-structures=false\n');
}

async function run(directory, launcher, label, marker, properties = []) {
    console.log('Starting ' + label);
    const output = fs.createWriteStream(path.join(directory, label + '.log'));
    const child = spawn(spec.java8, ['-Xms256m', '-Xmx2g', ...properties, '-jar', launcher, 'nogui'],
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
            const before = treeDigest(source);
            const launcher = 'forge-1.14.4-28.2.26.jar';
            prepare(directory, spec.runtime114, launcher, [spec.modJar, spec.probeJar, spec.oreSpawn]);
            fs.cpSync(source, path.join(directory, 'world'), { recursive: true });
            if (profile.advancementFixture) {
                const fixture = JSON.parse(fs.readFileSync(profile.advancementFixture, 'utf8'));
                const world = path.join(directory, 'world');
                fs.mkdirSync(path.join(world, 'advancements'), { recursive: true });
                fs.writeFileSync(path.join(world, 'advancements', fixture.uuid + '.json'),
                    JSON.stringify(fixture.progress, null, 2) + '\n');
                fs.copyFileSync(profile.advancementFixture,
                    path.join(world, 'basemetals_advancement_upgrade_fixture.json'));
            }
            results.push(...await run(directory, launcher, 'upgrade-first', profile.marker,
                ['-Dbasemetalsprobe.mode=legacy-upgrade']));
            results.push(...await run(directory, launcher, 'upgrade-reload', profile.marker,
                ['-Dbasemetalsprobe.mode=legacy-upgrade']));
            const after = treeDigest(source);
            if (before !== after) throw new Error('Source fixture changed: ' + source);
            results.push(profile.id + ' source_sha256=' + before + ' unchanged=true');
        }
        fs.writeFileSync(path.join(root, phase + '-results.txt'), results.join('\n') + '\n');
    }
})().catch(error => { console.error(error); process.exitCode = 1; });
