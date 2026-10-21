#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 2 ]]; then
  echo "usage: $0 <empty-runtime-directory> <java-8-executable>" >&2
  exit 2
fi
runtime="$1"
java8="$2"
version=1.10.2-12.18.3.2511
expected=664393C5A924B8F06CFB953903ABCF9FE5E99ECBE6AF8C7EA217339C766E519C

if [[ -e "$runtime" && -n "$(find "$runtime" -mindepth 1 -maxdepth 1 -print -quit)" ]]; then
  echo "Packaged Forge destination must be empty: $runtime" >&2
  exit 1
fi
mkdir -p "$runtime"
installer="$runtime/forge-$version-installer.jar"
curl --fail --location --silent --show-error --retry 3 --connect-timeout 20 --max-time 180 \
  "https://maven.minecraftforge.net/net/minecraftforge/forge/$version/forge-$version-installer.jar" \
  --output "$installer"
actual="$(sha256sum "$installer" | awk '{print toupper($1)}')"
if [[ "$actual" != "$expected" ]]; then
  echo "Forge installer checksum mismatch: $actual" >&2
  exit 1
fi
# The 1.10 installer writes to its working directory and ignores a destination argument.
(cd "$runtime" && "$java8" -jar "forge-$version-installer.jar" --installServer)
if [[ ! -f "$runtime/forge-$version-universal.jar" && -f "$runtime/forge-$version.jar" ]]; then
  cp "$runtime/forge-$version.jar" "$runtime/forge-$version-universal.jar"
fi
test -f "$runtime/forge-$version-universal.jar"
test -f "$runtime/minecraft_server.1.10.2.jar"
test -d "$runtime/libraries"
