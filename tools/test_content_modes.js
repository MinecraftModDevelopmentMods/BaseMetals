'use strict';

// Uses disposable profiles and the same packaged Forge runtimes as the other smoke tests.
const fs = require('fs');
const path = require('path');
const net = require('net');
const { spawn } = require('child_process');
const [java, serverRuntime, clientRuntime, fixture, root] = process.argv.slice(2).map(value => path.resolve(value));
const build = path.resolve(__dirname, '..', 'build');
if (path.dirname(root) !== build || path.basename(root) !== 'content-mode-tests') {
  throw new Error(`Content-mode profiles must be inside the build directory: ${root}`);
}

const version = '1.13.2';
const forge = '25.0.223';
const forgeMetadata = JSON.parse(fs.readFileSync(path.join(clientRuntime, 'versions', `${version}-forge-${forge}`, `${version}-forge-${forge}.json`)));
const vanillaMetadata = JSON.parse(fs.readFileSync(path.join(clientRuntime, 'versions', version, `${version}.json`)));
const libraries = new Map();
const osName = process.platform === 'win32' ? 'windows' : process.platform === 'darwin' ? 'osx' : 'linux';
for (const metadata of [vanillaMetadata, forgeMetadata]) {
  for (const library of metadata.libraries) {
    let allowed = !library.rules || library.rules.length === 0;
    for (const rule of library.rules || []) {
      if (!rule.os || (rule.os.name === osName && (!rule.os.arch || rule.os.arch === 'x86_64'))) {
        allowed = rule.action === 'allow';
      }
    }
    if (allowed) libraries.set(library.name.split(':').slice(0, 2).join(':'), path.join(clientRuntime, 'libraries', library.downloads.artifact.path));
  }
}
const classpath = [...libraries.values(), path.join(clientRuntime, 'versions', version, `${version}.jar`)].join(path.delimiter);
const sleep = milliseconds => new Promise(resolve => setTimeout(resolve, milliseconds));

function profile(directory, client, mode, oldConfig) {
  fs.mkdirSync(path.join(directory, 'mods'), { recursive: true });
  for (const file of fs.readdirSync(fixture)) {
    if (file.includes('clientprobe') && !client || file === 'basemetalsprobe.jar' && client) continue;
    fs.copyFileSync(path.join(fixture, file), path.join(directory, 'mods', file));
  }
  if (oldConfig || mode !== undefined) {
    fs.mkdirSync(path.join(directory, 'config'), { recursive: true });
    const text = (mode === undefined ? '' : `contentMode = "${mode}"\n`)
      + ['specialEffects', 'starsteelRegeneration', 'mercuryImmersionEffects', 'villagerTrades']
        .map(key => `${key} = ${!oldConfig}\n`).join('');
    fs.writeFileSync(path.join(directory, 'config', 'basemetals-common.toml'), text);
  }
  if (client) fs.writeFileSync(path.join(directory, 'options.txt'), 'fullscreen:false\nlang:en_us\n');
  else fs.writeFileSync(path.join(directory, 'eula.txt'), 'eula=true\n');
}

function launch(args, directory, label) {
  const log = fs.openSync(path.join(directory, `${label}.log`), 'w');
  const child = spawn(java, args, { cwd: directory, stdio: ['pipe', log, log], windowsHide: true });
  fs.closeSync(log);
  child.done = new Promise((resolve, reject) => {
    child.on('error', reject);
    child.on('exit', code => resolve(code));
  });
  return child;
}

async function waitFor(child, file, marker, timeout) {
  const deadline = Date.now() + timeout;
  while (Date.now() < deadline) {
    if (fs.existsSync(file) && fs.readFileSync(file, 'utf8').includes(marker)) return;
    if (child.exitCode !== null) throw new Error(`Process exited before ${marker}: ${file}`);
    await sleep(250);
  }
  throw new Error(`Timed out waiting for ${marker}: ${file}`);
}

async function finish(child, timeout) {
  let timer;
  let result;
  try {
    result = await Promise.race([child.done, new Promise(resolve => {
      timer = setTimeout(() => resolve('timeout'), timeout);
    })]);
  } finally {
    clearTimeout(timer);
  }
  if (result === 'timeout') {
    child.kill();
    throw new Error('Packaged content-mode process exceeded its time limit');
  }
  if (result !== 0) throw new Error(`Packaged content-mode process exited ${result}`);
}

async function portNumber() {
  const listener = net.createServer();
  await new Promise(resolve => listener.listen(0, '127.0.0.1', resolve));
  const port = listener.address().port;
  await new Promise(resolve => listener.close(resolve));
  return port;
}

async function scenario(test, reload = false) {
  const directory = path.join(root, test.name);
  const server = path.join(directory, 'server');
  const client = path.join(directory, 'client');
  if (!reload) {
    profile(server, false, test.server, test.old);
    profile(client, true, test.client, test.old);
  }
  const port = await portNumber();
  fs.writeFileSync(path.join(server, 'server.properties'), `level-name=world\nonline-mode=false\nserver-ip=127.0.0.1\nserver-port=${port}\nview-distance=2\nspawn-protection=0\nmax-tick-time=-1\n`);
  const serverProcess = launch(['-Xms256m', '-Xmx2g', '-Dbasemetalsprobe.mode=login',
    `-Dbasemetalsprobe.modeSwitch=${!!test.cycle}`,
    '-jar', path.join(serverRuntime, `forge-${version}-${forge}.jar`), 'nogui'], server, reload ? 'server-reload' : 'server-console');
  let clientProcess;
  try {
    await waitFor(serverProcess, path.join(server, 'logs', 'latest.log'), 'BASEMETALS_CONTENT_MODE_PROBE PASS', 120000);
    clientProcess = launch(['-Xms256m', '-Xmx2g', '-Dbasemetalsclientprobe.enabled=true',
      '-Dbasemetalsclientprobe.login=true', `-Dbasemetalsclientprobe.expectedMode=${test.client === 'low_fantasy' ? 'low_fantasy' : 'high_fantasy'}`,
      `-Dbasemetalsclientprobe.modeSwitch=${!!test.cycle}`,
      `-Dbasemetalsclientprobe.expectReject=${!!test.reject}`, `-Dbasemetalsclientprobe.port=${port}`,
      `-Djava.library.path=${path.join(clientRuntime, 'natives')}`, '-cp', classpath, forgeMetadata.mainClass,
      ...forgeMetadata.arguments.game, '--username', 'ModeValidation', '--version', `${version}-forge-${forge}`,
      '--gameDir', client, '--assetsDir', path.join(clientRuntime, 'assets-local'), '--assetIndex', vanillaMetadata.assetIndex.id,
      '--uuid', '00000000-0000-0000-0000-000000000002', '--accessToken', 'validation-token', '--userType', 'legacy',
      '--versionType', 'release', '--width', '854', '--height', '480'], client, reload ? 'client-reload' : 'client-console');
    await finish(clientProcess, 120000);
    const result = fs.readFileSync(path.join(client, 'mode-login-pass.properties'), 'utf8');
    if (!result.includes(`result=${test.reject ? 'rejected' : 'connected'}`)) throw new Error(`Wrong login result: ${test.name}`);
    const log = fs.readFileSync(path.join(server, 'logs', 'latest.log'), 'utf8');
    if (log.includes('BASEMETALS_MODE_LOGIN_SERVER PASS') === !!test.reject) throw new Error(`Unexpected gameplay entry: ${test.name}`);
    for (const profileDirectory of [server, client]) {
      const config = fs.readFileSync(path.join(profileDirectory, 'config', 'basemetals-common.toml'), 'utf8');
      for (const key of ['specialEffects', 'starsteelRegeneration', 'mercuryImmersionEffects', 'villagerTrades']) {
        if (!new RegExp(`${key}\\s*=\\s*${!test.old}`).test(config)) throw new Error(`Upgrade changed ${key}: ${test.name}`);
      }
    }
    console.log(`CONTENT_MODE_LOGIN PASS ${test.name}${reload ? ' reload' : ''}`);
  } finally {
    if (clientProcess && clientProcess.exitCode === null) clientProcess.kill();
    if (serverProcess.exitCode === null) serverProcess.stdin.write('stop\n');
    await finish(serverProcess, 20000);
  }
}

(async () => {
  const tests = [
    { name: 'legacy-both', old: true },
    { name: 'legacy-explicit-high', old: true, client: 'high_fantasy' },
    { name: 'missing-both' },
    { name: 'low-both', server: 'low_fantasy', client: 'low_fantasy' },
    { name: 'high-server-low-client', server: 'high_fantasy', client: 'low_fantasy', reject: true },
    { name: 'low-server-high-client', server: 'low_fantasy', client: 'high_fantasy', reject: true },
    { name: 'invalid-mode', server: 'unknown_mode', client: 'high_fantasy', old: true }
  ];
  if (!process.argv.includes('--cycle-only')) {
    for (const test of tests) await scenario(test);
    await scenario(tests[0], true);
  }
  const cycle = { name: 'mode-cycle', server: 'low_fantasy', client: 'low_fantasy', cycle: true };
  await scenario(cycle);
  for (const side of ['server', 'client']) {
    const config = path.join(root, cycle.name, side, 'config', 'basemetals-common.toml');
    fs.writeFileSync(config, fs.readFileSync(config, 'utf8').replace('"low_fantasy"', '"high_fantasy"'));
  }
  await scenario({ ...cycle, server: 'high_fantasy', client: 'high_fantasy' }, true);
  console.log(`CONTENT_MODE_INTEGRATION PASS scenarios=${process.argv.includes('--cycle-only') ? 2 : 10}`);
})().catch(error => { console.error(error); process.exitCode = 1; });
