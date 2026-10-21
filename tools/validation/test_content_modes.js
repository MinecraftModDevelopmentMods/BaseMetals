'use strict';

// Launch separate processes so the login check cannot share a client's configuration.
const fs = require('fs');
const path = require('path');
const net = require('net');
const crypto = require('crypto');
const { spawn } = require('child_process');
const { serverArguments, clientLaunch } = require('./forge_launcher');

const spec = JSON.parse(fs.readFileSync(process.argv[2], 'utf8'));
const output = path.resolve(spec.output);
if (fs.existsSync(output)) throw new Error('Choose a new output directory: ' + output);
fs.mkdirSync(output, { recursive: true });

const cases = [
    ['old-configs', 'legacy', 'legacy'],
    ['missing-explicit', 'legacy', 'high_fantasy'],
    ['explicit-missing', 'high_fantasy', 'legacy'],
    ['no-configs', null, null],
    ['low-fantasy', 'low_fantasy', 'low_fantasy'],
    ['reject-high-client', 'low_fantasy', 'high_fantasy', true],
    ['reject-low-client', 'high_fantasy', 'low_fantasy', true]
];

function hash(file) {
    return crypto.createHash('sha256').update(fs.readFileSync(file)).digest('hex');
}

function prepare(directory, mode, probe) {
    fs.mkdirSync(path.join(directory, 'mods'), { recursive: true });
    // Skip first-run accessibility setup in the automated client.
    fs.writeFileSync(path.join(directory, 'options.txt'), 'fullscreen:false\nonboardAccessibility:false\n');
    for (const artifact of [spec.modJar, spec.oreSpawn, probe]) {
        const file = typeof artifact === 'string' ? artifact : artifact.path;
        if (artifact.sha256 && hash(file).toLowerCase() !== artifact.sha256.toLowerCase()) {
            throw new Error('Unexpected artifact: ' + file);
        }
        fs.copyFileSync(file, path.join(directory, 'mods', path.basename(file)));
    }
    if (mode !== null) {
        fs.mkdirSync(path.join(directory, 'config'));
        const config = ['specialEffects', 'starsteelRegeneration', 'mercuryImmersionEffects', 'villagerTrades']
                .map(key => `${key} = false`).join('\n') + '\n'
            + (mode === 'legacy' ? '' : `contentMode = "${mode}"\n`);
        fs.writeFileSync(path.join(directory, 'config/basemetals-common.toml'), config);
    }
}

function launch(directory, args, filename) {
    const stream = fs.createWriteStream(path.join(directory, filename));
    const child = spawn(spec.java17, args, { cwd: directory, windowsHide: true });
    const state = { child, log: '', done: false, code: null };
    for (const pipe of [child.stdout, child.stderr]) pipe.on('data', data => {
        stream.write(data);
        state.log += data.toString();
    });
    state.exit = new Promise((resolve, reject) => {
        child.on('error', reject);
        child.on('close', code => {
            state.done = true;
            state.code = code;
            stream.end();
            resolve(code);
        });
    });
    return state;
}

async function waitFor(state, marker, timeout) {
    const deadline = Date.now() + timeout;
    while (!state.log.includes(marker)) {
        if (state.done || Date.now() > deadline) throw new Error('Missing ' + marker + '\n' + state.log.slice(-2500));
        await new Promise(resolve => setTimeout(resolve, 500));
    }
}

async function freePort() {
    const socket = net.createServer();
    await new Promise(resolve => socket.listen(0, '127.0.0.1', resolve));
    const port = socket.address().port;
    await new Promise(resolve => socket.close(resolve));
    return port;
}

function verifyConfig(directory, initial) {
    const config = fs.readFileSync(path.join(directory, 'config/basemetals-common.toml'), 'utf8');
    if (initial !== null) {
        for (const key of ['specialEffects', 'starsteelRegeneration', 'mercuryImmersionEffects', 'villagerTrades']) {
            if (!config.includes(`${key} = false`)) throw new Error('Changed existing setting: ' + key);
        }
    }
}

(async () => {
    const results = [];
    const client = clientLaunch(spec.clientRuntime, '1.20.1', '47.4.10');
    const selected = spec.cases ? cases.filter(([name]) => spec.cases.includes(name)) : cases;
    if (!selected.length || (spec.cases && selected.length !== spec.cases.length)) {
        throw new Error('The requested mode-test case is unknown or duplicated.');
    }
    for (const [name, serverMode, clientMode, reject] of selected) {
        console.log('Starting mode login: ' + name);
        const directory = path.join(output, name);
        const serverDir = path.join(directory, 'server');
        const clientDir = path.join(directory, 'client');
        prepare(serverDir, serverMode, spec.serverProbe);
        prepare(clientDir, clientMode, spec.clientProbe);
        const port = await freePort();
        fs.writeFileSync(path.join(serverDir, 'eula.txt'), 'eula=true\n');
        fs.writeFileSync(path.join(serverDir, 'server.properties'),
            `online-mode=false\nserver-ip=127.0.0.1\nserver-port=${port}\nview-distance=3\n`
            + 'simulation-distance=3\nlevel-type=minecraft:flat\n'
            + 'generator-settings={"layers":[{"block":"minecraft:bedrock","height":1},{"block":"minecraft:dirt","height":2},{"block":"minecraft:grass_block","height":1}],"biome":"minecraft:plains"}\n'
            + 'generate-structures=false\nmax-tick-time=-1\n');
        const server = launch(serverDir, ['-Xms256m', '-Xmx2g', '-Dbasemetals.awaitLoginSmoke=true',
            ...serverArguments(spec.serverRuntime, serverDir, '1.20.1', '47.4.10')], 'server.log');
        let player;
        try {
            await waitFor(server, 'BASEMETALS_PACKAGED_SERVER_SMOKE PASS', 240000);
            player = launch(clientDir, ['-Xms512m', '-Xmx2g', '-Dbasemetals.remoteLoginSmoke=true',
                `-Dbasemetals.remoteAddress=127.0.0.1:${port}`, `-Dbasemetals.expectModeReject=${!!reject}`,
                ...client.args, ...client.metadata.arguments.game,
                '--username', 'BMValidation', '--version', '1.20.1', '--gameDir', clientDir,
                '--assetsDir', path.join(spec.clientRuntime, 'assets'), '--assetIndex', client.vanilla.assetIndex.id,
                '--uuid', '00000000000000000000000000000118', '--accessToken', '0', '--userType', 'legacy'], 'client.log');
            await waitFor(player, 'BASEMETALS_MODE_LOGIN_CLIENT PASS result=' + (reject ? 'rejected' : 'connected'), 240000);
            if (await player.exit !== 0) throw new Error('Client exited unsuccessfully');
            verifyConfig(serverDir, serverMode);
            verifyConfig(clientDir, clientMode);
            results.push({ name, result: reject ? 'rejected' : 'connected' });
            fs.writeFileSync(path.join(output, 'results.json'), JSON.stringify(results, null, 2) + '\n');
            console.log('MODE_LOGIN PASS ' + name);
        } finally {
            if (player && !player.done) player.child.kill();
            if (!server.done) server.child.stdin.end('stop\n');
            const stopped = await Promise.race([server.exit.then(() => true),
                new Promise(resolve => setTimeout(() => resolve(false), 30000))]);
            if (!stopped) server.child.kill();
        }
    }
    fs.writeFileSync(path.join(output, 'results.json'), JSON.stringify(results, null, 2) + '\n');
})().catch(error => { console.error(error); process.exitCode = 1; });
