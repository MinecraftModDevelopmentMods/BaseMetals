"""Run a generated Eclipse profile in an isolated directory, with a test-only probe."""

import argparse
import json
import os
import shlex
import shutil
import subprocess
import time
import xml.etree.ElementTree as ET
from pathlib import Path


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("profile", type=Path)
    parser.add_argument("java", type=Path)
    parser.add_argument("directory", type=Path)
    parser.add_argument("phase", choices=["fresh", "reload"])
    parser.add_argument("--probe", type=Path, required=True)
    parser.add_argument("--production-output", type=Path,
                        help="Use a disposable Eclipse build without touching the editor's bin directory")
    args = parser.parse_args()
    profile = args.profile.resolve()
    root = ET.parse(profile).getroot()
    strings = {node.get("key"): node.get("value") for node in root.findall("stringAttribute")}
    environment = os.environ.copy()
    for node in root.findall("mapAttribute/mapEntry"):
        environment[node.get("key")] = node.get("value")
    production_output = (args.production_output or profile.parent / "bin/main").resolve()
    environment["MOD_CLASSES"] = str(production_output)

    archives = []
    for node in root.findall("listAttribute[@key='org.eclipse.jdt.launching.CLASSPATH']/listEntry"):
        entry = ET.fromstring(node.get("value"))
        path = entry.get("externalArchive")
        if path:
            archives.append(path)
        elif entry.get("projectName"):
            archives.append(str(production_output))

    directory = args.directory.resolve()
    (directory / "mods").mkdir(parents=True, exist_ok=True)
    shutil.copy2(args.probe, directory / "mods" / args.probe.name)
    (directory / "eula.txt").write_text("eula=true\n", encoding="utf-8")
    if not (directory / "server.properties").exists():
        (directory / "server.properties").write_text("server-ip=127.0.0.1\nserver-port=0\nonline-mode=false\nlevel-type=FLAT\nview-distance=3\n", encoding="utf-8")

    command = [str(args.java), "-Xmx3G", "-Dlaunch.phase=" + args.phase]
    command += shlex.split(strings.get("org.eclipse.jdt.launching.VM_ARGUMENTS", ""))
    command += ["-cp", os.pathsep.join(dict.fromkeys(archives)), strings["org.eclipse.jdt.launching.MAIN_TYPE"]]
    command += shlex.split(strings["org.eclipse.jdt.launching.PROGRAM_ARGUMENTS"])
    started = time.time_ns()
    with (directory / (args.phase + "-console.log")).open("w", encoding="utf-8") as log:
        process = subprocess.run(command, cwd=directory, env=environment, stdout=log,
                                 stderr=subprocess.STDOUT, timeout=300)

    report = directory / (args.phase + "-launch.json")
    if (process.returncode or not report.exists() or report.stat().st_mtime_ns < started
            or not json.loads(report.read_text())["passed"]):
        raise SystemExit(f"Launch failed; inspect {directory / (args.phase + '-console.log')}")
    print(report.read_text(), flush=True)


if __name__ == "__main__":
    main()
