#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 2 ]]; then
  echo "usage: $0 <empty-runtime-directory> <java-8-executable>" >&2
  exit 2
fi
runtime="$1"
java8="$2"
version=1.11.2-13.20.1.2588
expected=00AEA3DDF1E78BBB4D5D05E8C22FB3E849DE09B47D828E57DAD373A16BB8A26F

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
# The legacy installer writes to its working directory.
(cd "$runtime" && "$java8" -jar "forge-$version-installer.jar" --installServer)
if [[ ! -f "$runtime/forge-$version-universal.jar" && -f "$runtime/forge-$version.jar" ]]; then
  cp "$runtime/forge-$version.jar" "$runtime/forge-$version-universal.jar"
fi
test -f "$runtime/forge-$version-universal.jar"
test -f "$runtime/minecraft_server.1.11.2.jar"
test -d "$runtime/libraries"
