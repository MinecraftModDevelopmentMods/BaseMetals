#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 1 ]]; then
  echo "usage: $0 <maven-mirror-directory>" >&2
  exit 2
fi

"${PYTHON:-python3}" "$(dirname "$0")/stage-legacy-dependencies.py" "$1"
