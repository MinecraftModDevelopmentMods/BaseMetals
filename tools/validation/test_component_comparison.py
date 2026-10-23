"""Checks that the save comparison keeps item data strict across components."""

import copy
import unittest
import nbtlib
from compare_current_world import canonical_stack


class ComponentComparisonTest(unittest.TestCase):
    def test_legacy_data_equals_its_native_components(self):
        legacy = nbtlib.parse_nbt('''{id:"basemetals:steel_sword",Count:1b,Slot:2b,tag:{
            Damage:19,display:{Name:'{"text":"Old sword"}',Lore:['{"text":"A keepsake"}']},
            Enchantments:[{id:"minecraft:unbreaking",lvl:2s}],Proof:47}}''')
        native = nbtlib.parse_nbt('''{id:"basemetals:steel_sword",count:1,Slot:2b,components:{
            "minecraft:damage":19,"minecraft:custom_name":"Old sword","minecraft:lore":["A keepsake"],
            "minecraft:enchantments":{levels:{"minecraft:unbreaking":2}},"minecraft:custom_data":{Proof:47}}}''')
        self.assertEqual(canonical_stack(legacy), canonical_stack(native))

        for key in list(native['components']):
            changed = copy.deepcopy(native)
            changed['components'].pop(key)
            self.assertNotEqual(canonical_stack(legacy), canonical_stack(changed), key)

    def test_unknown_component_and_custom_nbt_are_not_ignored(self):
        original = nbtlib.parse_nbt('{id:"basemetals:steel_arrow",count:1,components:{"example:data":{value:9b}}}')
        changed = nbtlib.parse_nbt('{id:"basemetals:steel_arrow",count:1,components:{"example:data":{value:9}}}')
        self.assertNotEqual(canonical_stack(original), canonical_stack(changed))


if __name__ == '__main__':
    unittest.main()
