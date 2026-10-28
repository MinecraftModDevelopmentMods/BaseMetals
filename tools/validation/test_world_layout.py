"""The comparison tools must follow Minecraft's 26.1 file moves."""

import io
import json
import struct
import tempfile
import unittest
import zlib
from pathlib import Path

import nbtlib
import compare_current_world as content
import compare_world_terrain as terrain


class WorldLayoutTest(unittest.TestCase):
    def test_native_dimensions_keep_the_same_comparison_keys(self):
        with tempfile.TemporaryDirectory() as directory:
            world = Path(directory)
            for name in ('overworld', 'the_nether', 'the_end'):
                self.region(world / 'dimensions/minecraft' / name / 'region')

            self.assertEqual({('', 0, 0), ('DIM-1', 0, 0), ('DIM1', 0, 0)}, set(terrain.chunks(world)))

    def test_nested_custom_dimensions_are_not_skipped(self):
        with tempfile.TemporaryDirectory() as directory:
            world = Path(directory)
            self.region(world / 'dimensions/example/nested/cavern/region')
            self.assertIn(('dimensions/example/nested/cavern', 0, 0), terrain.chunks(world))

    def test_player_files_and_advancements_follow_the_native_paths(self):
        with tempfile.TemporaryDirectory() as directory:
            world = Path(directory)
            nbtlib.File({'Data': nbtlib.Compound()}).save(world / 'level.dat', gzipped=True)
            players = world / 'players/data'
            players.mkdir(parents=True)
            item = nbtlib.Compound({'id': nbtlib.String('basemetals:steel_ingot'), 'count': nbtlib.Int(12)})
            nbtlib.File({'Inventory': nbtlib.List[nbtlib.Compound]([item])}).save(players / 'player.dat', gzipped=True)
            advances = world / 'players/advancements'
            advances.mkdir()
            progress = {'basemetals:this_is_new': {'done': True}}
            (advances / 'player.json').write_text(json.dumps(progress), encoding='utf8')

            _, stacks, achievements = content.snapshot(world)

            self.assertEqual(1, sum(stacks.values()))
            self.assertEqual({'player.json': progress}, achievements)

    def test_embedded_and_disk_copies_of_one_player_are_counted_once(self):
        with tempfile.TemporaryDirectory() as directory:
            world = Path(directory)
            player = nbtlib.Compound({
                'UUID': nbtlib.IntArray([0, 0, 0, 280]),
                'Inventory': nbtlib.List[nbtlib.Compound]([
                    nbtlib.Compound({'id': nbtlib.String('basemetals:steel_ingot'), 'count': nbtlib.Int(12)})
                ]),
            })
            nbtlib.File({'Data': nbtlib.Compound({'Player': player})}).save(world / 'level.dat', gzipped=True)
            players = world / 'playerdata'
            players.mkdir()
            nbtlib.File(player).save(players / '00000000-0000-0000-0000-000000000118.dat', gzipped=True)

            _, stacks, _ = content.snapshot(world)

            self.assertEqual(1, sum(stacks.values()))

    @staticmethod
    def region(directory):
        directory.mkdir(parents=True)
        stream = io.BytesIO()
        nbtlib.File({'xPos': nbtlib.Int(0), 'zPos': nbtlib.Int(0)}).write(stream)
        payload = zlib.compress(stream.getvalue())
        region = bytearray(8192)
        region[:4] = bytes((0, 0, 2, 1))
        region.extend(struct.pack('>I', len(payload) + 1) + bytes((2,)) + payload)
        (directory / 'r.0.0.mca').write_bytes(region)
