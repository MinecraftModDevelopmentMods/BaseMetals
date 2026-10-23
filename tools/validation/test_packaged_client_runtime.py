"""Keep the packaged-client helper aligned with the pinned build target."""

import importlib.util
from pathlib import Path
import unittest


PROJECT = Path(__file__).resolve().parents[2]
SPEC = importlib.util.spec_from_file_location(
    "packaged_client_runtime", PROJECT / "gradle/prepare-forge-client-runtime.py")
HELPER = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(HELPER)


class PackagedClientTargetTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        lines = (PROJECT / "gradle.properties").read_text(encoding="utf-8").splitlines()
        cls.properties = dict(
            line.split("=", 1) for line in lines
            if line and not line.startswith("#") and "=" in line)

    def test_minecraft_version_matches_the_project(self):
        self.assertEqual(self.properties["minecraft_version"], HELPER.MINECRAFT_VERSION)

    def test_forge_version_matches_the_project(self):
        self.assertEqual(self.properties["forge_version"], HELPER.FORGE_VERSION)

    def test_installer_checksum_matches_the_project(self):
        self.assertEqual(self.properties["forge_installer_sha256"].lower(),
                         HELPER.FORGE_INSTALLER_SHA256)


if __name__ == "__main__":
    unittest.main()
