"""Build a checksum-verified Maven mirror from downloads or an existing Gradle cache."""

import argparse
import hashlib
import json
import time
import urllib.error
import urllib.request
from pathlib import Path


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("destination", type=Path)
    parser.add_argument("--cache", type=Path, help="Existing Gradle user home; matching files are verified before reuse")
    args = parser.parse_args()
    specs = json.loads(Path(__file__).with_name("dependencies.json").read_text(encoding="utf-8"))

    for spec in specs:
        group, artifact, version, *classifier = spec["coordinate"].split(":")
        filename = f"{artifact}-{version}" + ("-" + classifier[0] if classifier else "") + ".jar"
        relative = Path(group.replace(".", "/")) / artifact / version / filename
        target = args.destination.resolve() / relative
        target.parent.mkdir(parents=True, exist_ok=True)

        if not target.exists():
            cached = list((args.cache / "caches/modules-2/files-2.1" / group / artifact / version).glob("*/" + filename)) if args.cache else []
            if cached:
                data = cached[0].read_bytes()
            else:
                url = spec["repository"].rstrip("/") + "/" + relative.as_posix()
                request = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
                for attempt in range(3):
                    try:
                        with urllib.request.urlopen(request, timeout=30) as response:
                            data = response.read()
                        break
                    except (OSError, urllib.error.URLError):
                        if attempt == 2:
                            raise
                        print(f"Retrying {spec['coordinate']}", flush=True)
                        time.sleep(2)
            if hashlib.sha256(data).hexdigest().upper() != spec["sha256"]:
                raise ValueError(f"Checksum mismatch: {spec['coordinate']}")
            target.write_bytes(data)

        if hashlib.sha256(target.read_bytes()).hexdigest().upper() != spec["sha256"]:
            raise ValueError(f"Checksum mismatch in mirror: {target}")
        print(spec["id"], spec["sha256"], flush=True)


if __name__ == "__main__":
    main()
