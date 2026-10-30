"""Checks for the read-only saved-content comparison."""

import gzip
import io
import struct
import tempfile
import unittest
import zlib
from pathlib import Path
from unittest.mock import patch

import nbtlib
import compare_current_world as content


class NativeBlockStateTest(unittest.TestCase):
    def setUp(self):
        self.defaults = {'basemetals:steel_wall': {
            'north': 'none', 'south': 'none', 'east': 'none', 'west': 'none',
            'up': 'true', 'waterlogged': 'false',
        }}

    def canonical(self, value):
        return content.canonical_block_state(nbtlib.parse_nbt(value), self.defaults)

    def test_old_and_native_default_states_match(self):
        old = '{Name:"basemetals:steel_wall",Properties:{north:"false",south:"false",east:"false",west:"false",up:"true",waterlogged:"false"}}'
        native = '{id:"basemetals:steel_wall",properties:{north:"none",south:"none",east:"none",west:"none",up:"true",waterlogged:"false"}}'
        self.assertEqual(self.canonical(old), self.canonical(native))
        self.assertEqual(self.canonical(old), self.canonical('"basemetals:steel_wall"'))

    def test_non_default_connections_are_not_lost(self):
        old = '{Name:"basemetals:steel_wall",Properties:{north:"true",east:"tall"}}'
        native = '{id:"basemetals:steel_wall",properties:{north:"low",east:"tall"}}'
        self.assertEqual(self.canonical(old), self.canonical(native))
        self.assertNotEqual(self.canonical(old), self.canonical('"basemetals:steel_wall"'))

    def test_unknown_properties_are_not_discarded(self):
        old = '{Name:"basemetals:steel_wall",Properties:{future_property:"keep_me"}}'
        self.assertNotEqual(self.canonical(old), self.canonical('"basemetals:steel_wall"'))

    def test_native_mixed_palette_wrappers(self):
        self.assertEqual(self.canonical('"basemetals:steel_wall"'),
                         self.canonical('{"":"basemetals:steel_wall"}'))
        self.assertEqual(self.canonical('{id:"basemetals:steel_wall",properties:{east:"tall"}}'),
                         self.canonical('{"":{id:"basemetals:steel_wall",properties:{east:"tall"}}}'))


class SavedEntitiesTest(unittest.TestCase):
    def test_entity_regions_include_ammunition_and_equipment(self):
        for compression in (1, 2):
            with self.subTest(compression=compression), tempfile.TemporaryDirectory() as directory:
                world = Path(directory)
                entity = nbtlib.Compound({
                    'id': nbtlib.String('basemetals:custom_bolt'),
                    'inGround': nbtlib.Byte(1),
                    'BaseMetalsAmmo': nbtlib.Compound({
                        'id': nbtlib.String('basemetals:adamantine_bolt'),
                        'Count': nbtlib.Byte(1),
                        'tag': nbtlib.Compound({'test': nbtlib.String('unchanged')}),
                    }),
                })
                stream = io.BytesIO()
                nbtlib.File({'Entities': nbtlib.List[nbtlib.Compound]([entity])}).write(stream)
                payload = (gzip.compress(stream.getvalue()) if compression == 1
                           else zlib.compress(stream.getvalue()))
                region = bytearray(8192)
                region[:4] = bytes((0, 0, 2, 1))
                region.extend(struct.pack('>I', len(payload) + 1) + bytes((compression,)) + payload)
                entity_directory = world / 'DIM-1' / 'entities'
                entity_directory.mkdir(parents=True)
                (entity_directory / 'r.0.0.mca').write_bytes(region)
                nbtlib.File({'Data': nbtlib.Compound()}).save(world / 'level.dat', gzipped=True)

                self.assertEqual([entity], list(content.saved_entities(world)))
                with patch.object(content, 'chunks', return_value={}):
                    blocks, stacks, advancements = content.snapshot(world)
                self.assertEqual({}, blocks)
                self.assertEqual(1, sum(stacks.values()))
                self.assertIn('adamantine_bolt', next(iter(stacks)))
                self.assertIn('unchanged', next(iter(stacks)))
                self.assertEqual({}, advancements)

                # Unloaded converted chunks can still carry their own entity list.
                with patch.object(content, 'chunks', return_value={('', 0, 0): {'entities': [entity]}}):
                    _, combined, _ = content.snapshot(world)
                self.assertEqual(2, sum(combined.values()))

                entity['inGround'] = nbtlib.Byte(0)
                with patch.object(content, 'saved_entities', return_value=[entity]), \
                        patch.object(content, 'chunks', return_value={}):
                    _, moving, _ = content.snapshot(world)
                self.assertEqual({}, moving)


if __name__ == '__main__':
    unittest.main()
