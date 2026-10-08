#!/usr/bin/env bash
set -euo pipefail

repository="${1:?Usage: stage-orespawn-release.sh <repository-directory>}"
value() { sed -n "s/^$1=//p" gradle.properties; }

if [[ "$(value orespawn_dependency_mode)" == "candidate" ]]; then
  echo 'Publication is blocked: pin the public OreSpawn 1.16.5 release first.' >&2
  exit 1
fi

project_id="$(value orespawn_curse_project_id)"
file_id="$(value orespawn_curse_file_id)"
expected_sha="$(value orespawn_sha256)"
for required in project_id file_id expected_sha; do
  if [[ -z "${!required}" ]]; then
    echo "Missing OreSpawn release metadata: $required" >&2
    exit 1
  fi
done

artifact_dir="$repository/curse/maven/mmd-orespawn-$project_id/$file_id"
artifact="$artifact_dir/mmd-orespawn-$project_id-$file_id.jar"
pom="$artifact_dir/mmd-orespawn-$project_id-$file_id.pom"
mkdir -p "$artifact_dir"
curl --fail --location --silent --show-error \
  --retry 5 --retry-all-errors --retry-delay 5 \
  "https://www.curseforge.com/api/v1/mods/$project_id/files/$file_id/download" \
  --output "$artifact"

actual_sha="$(sha256sum "$artifact" | awk '{print toupper($1)}')"
if [[ "$actual_sha" != "${expected_sha^^}" ]]; then
  echo "OreSpawn file $file_id has SHA-256 $actual_sha, expected ${expected_sha^^}" >&2
  exit 1
fi
printf '%s\n' \
  '<?xml version="1.0" encoding="UTF-8"?>' \
  '<project xmlns="http://maven.apache.org/POM/4.0.0">' \
  '  <modelVersion>4.0.0</modelVersion>' \
  "  <groupId>curse.maven</groupId>" \
  "  <artifactId>mmd-orespawn-$project_id</artifactId>" \
  "  <version>$file_id</version>" \
  '</project>' > "$pom"
echo "Staged exact OreSpawn file $file_id in $repository"
