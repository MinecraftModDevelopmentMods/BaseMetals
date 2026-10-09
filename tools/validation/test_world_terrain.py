"""Regression tests for the read-only world-terrain comparison."""

import unittest
from pathlib import Path
from unittest.mock import patch

import compare_world_terrain as terrain


class PaletteLayoutTest(unittest.TestCase):
    def test_contiguous_and_padded_palettes(self):
        for palette_size in (16, 17, 33, 65):
            bits = max(4, (palette_size - 1).bit_length())
            expected = [index % palette_size for index in range(4096)]

            for padded in (False, True):
                entries_per_word = 64 // bits
                length = ((4096 + entries_per_word - 1) // entries_per_word if padded
                          else (4096 * bits + 63) // 64)
                words = [0] * length

                for index, value in enumerate(expected):
                    word, offset = (divmod(index, entries_per_word) if padded
                                    else divmod(index * bits, 64))
                    shift = offset * bits if padded else offset
                    words[word] |= (value << shift) & ((1 << 64) - 1)
                    if not padded and shift + bits > 64:
                        words[word + 1] |= value >> (64 - shift)

                section = {"Palette": [{"Name": str(index)} for index in range(palette_size)],
                           "BlockStates": words}
                self.assertEqual([str(value) for value in expected], list(terrain.block_names(section)))


def gravel(x=0.5, y=35, z=0.5, dimension=""):
    return dimension, "minecraft:gravel", x, y, z


class FallingTerrainTest(unittest.TestCase):
    def test_landed_block_is_recovered(self):
        terrain.match_fallen_blocks([gravel()], [gravel(y=20)])

    def test_in_flight_block_is_recovered(self):
        terrain.match_fallen_blocks([gravel()], [gravel(y=32)])

    def test_dropped_stack_can_drift(self):
        terrain.match_fallen_blocks([gravel()], [gravel(x=3, y=20, z=2)])

    def test_neighbouring_columns_do_not_greedily_take_each_others_blocks(self):
        terrain.match_fallen_blocks([gravel(x=0), gravel(x=4)],
                                   [gravel(x=4, y=20), gravel(x=8, y=20)])

    def test_deleted_grain_is_rejected(self):
        with self.assertRaises(AssertionError):
            terrain.match_fallen_blocks([gravel()], [])

    def test_one_grain_cannot_cover_two_losses(self):
        with self.assertRaises(AssertionError):
            terrain.match_fallen_blocks([gravel(), gravel(y=36)], [gravel(y=20)])

    def test_different_material_is_rejected(self):
        with self.assertRaises(AssertionError):
            terrain.match_fallen_blocks([gravel()], [("", "minecraft:sand", 0.5, 20, 0.5)])

    def test_different_dimension_is_rejected(self):
        with self.assertRaises(AssertionError):
            terrain.match_fallen_blocks([gravel()], [gravel(y=20, dimension="DIM-1")])

    def test_upward_replacement_is_rejected(self):
        with self.assertRaises(AssertionError):
            terrain.match_fallen_blocks([gravel()], [gravel(y=40)])

    def test_distant_replacement_is_rejected(self):
        with self.assertRaises(AssertionError):
            terrain.match_fallen_blocks([gravel()], [gravel(x=5, y=20)])

    def test_ore_replacing_fixed_terrain_is_rejected(self):
        def chunk(name):
            return {("DIM-1", 0, 0): {"Sections": [
                {"Y": 0, "Palette": [{"Name": name}]}
            ]}}

        with patch.object(terrain, "chunks", side_effect=[chunk("minecraft:netherrack"),
                                                         chunk("minecraft:nether_quartz_ore")]), \
                patch.object(terrain, "falling_blocks", return_value={}):
            with self.assertRaisesRegex(AssertionError, "netherrack became minecraft:nether_quartz_ore"):
                terrain.compare(Path("original"), Path("upgraded"))


if __name__ == "__main__":
    unittest.main()
