#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 1 ]]; then
  echo "usage: $0 <destination-maven-repository>" >&2
  exit 2
fi

destination="$1"
if command -v cygpath >/dev/null 2>&1; then
  destination="$(cygpath -u "$destination")"
fi
properties="gradle.properties"
if [[ ! -f "$properties" ]]; then
  echo "Run this script from the Base Metals project root" >&2
  exit 1
fi

value() {
  sed -n "s/^$1=//p" "$properties" | tr -d '\r'
}

stage() {
  local slug="$1"
  local project_id="$2"
  local file_id="$3"
  local expected_sha="$4"
  local module="${slug}-${project_id}"
  local target_dir="$destination/curse/maven/$module/$file_id"
  local target="$target_dir/$module-$file_id.jar"
  local temporary="$target.download"

  mkdir -p "$target_dir"
  if [[ ! -f "$target" ]] || [[ "$(sha256sum "$target" | awk '{print toupper($1)}')" != "$expected_sha" ]]; then
    rm -f "$temporary"
    curl --fail --location --silent --show-error \
      "https://www.curseforge.com/api/v1/mods/$project_id/files/$file_id/download" \
      --output "$temporary"
    actual_sha="$(sha256sum "$temporary" | awk '{print toupper($1)}')"
    if [[ "$actual_sha" != "$expected_sha" ]]; then
      echo "$slug SHA-256 is $actual_sha; expected $expected_sha" >&2
      rm -f "$temporary"
      exit 1
    fi
    mv "$temporary" "$target"
  fi
  printf '%s  %s\n' "$expected_sha" "$target"
}

mkdir -p "$destination"

stage mmdlib "$(value mmdlib_curse_project_id)" "$(value mmdlib_curse_file_id)" "$(value mmdlib_sha256)"
stage mmd-orespawn "$(value orespawn4_curse_project_id)" "$(value orespawn4_curse_file_id)" "$(value orespawn4_sha256)"
stage mmd-orespawn "$(value orespawn3_curse_project_id)" "$(value orespawn3_curse_file_id)" "$(value orespawn3_sha256)"
stage additional-loot-tables "$(value alt_curse_project_id)" "$(value alt_curse_file_id)" "$(value alt_sha256)"
stage mantle "$(value mantle_curse_project_id)" "$(value mantle_curse_file_id)" "$(value mantle_sha256)"
stage tinkers-construct "$(value tconstruct_curse_project_id)" "$(value tconstruct_curse_file_id)" "$(value tconstruct_sha256)"
stage taiga-tinkers-alloying-addon "$(value taiga_curse_project_id)" "$(value taiga_curse_file_id)" "$(value taiga_sha256)"
stage constructs-armory "$(value conarm_curse_project_id)" "$(value conarm_curse_file_id)" "$(value conarm_sha256)"
stage thaumcraft "$(value thaumcraft_curse_project_id)" "$(value thaumcraft_curse_file_id)" "$(value thaumcraft_sha256)"
stage baubles "$(value baubles_curse_project_id)" "$(value baubles_curse_file_id)" "$(value baubles_sha256)"
stage cofh-core "$(value cofhcore_curse_project_id)" "$(value cofhcore_curse_file_id)" "$(value cofhcore_sha256)"
stage cofh-world "$(value cofhworld_curse_project_id)" "$(value cofhworld_curse_file_id)" "$(value cofhworld_sha256)"
stage redstone-flux "$(value redstoneflux_curse_project_id)" "$(value redstoneflux_curse_file_id)" "$(value redstoneflux_sha256)"
stage ancient-warfare-2 "$(value ancientwarfare_curse_project_id)" "$(value ancientwarfare_curse_file_id)" "$(value ancientwarfare_sha256)"
stage codechicken-lib-1-8 "$(value codechickenlib_curse_project_id)" "$(value codechickenlib_curse_file_id)" "$(value codechickenlib_sha256)"
stage base-metals "$(value legacy_basemetals_curse_project_id)" "$(value legacy_basemetals_curse_file_id)" "$(value legacy_basemetals_sha256)"
