"""Check both palette layouts used by the upgrade fixtures."""

import unittest

from compare_world_terrain import block_names


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
                self.assertEqual([str(value) for value in expected], list(block_names(section)))


if __name__ == "__main__":
    unittest.main()
