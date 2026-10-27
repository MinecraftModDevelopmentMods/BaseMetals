"""Checks that the save comparison keeps item data strict across components."""

import copy
import unittest
import nbtlib
from compare_current_world import canonical_player, canonical_stack


class ComponentComparisonTest(unittest.TestCase):
    def test_player_armour_keeps_its_slot_in_native_equipment(self):
        older = nbtlib.parse_nbt('{Slot:100b,id:"basemetals:adamantine_boots",Count:1b}')
        current = nbtlib.parse_nbt('{equipment:{feet:{id:"basemetals:adamantine_boots",count:1}}}')
        normalized = canonical_player(current)
        self.assertEqual(canonical_stack(older), canonical_stack(normalized['equipment']['feet']))
        self.assertNotIn('Slot', current['equipment']['feet'])

        wrong_slot = nbtlib.parse_nbt('{equipment:{head:{id:"basemetals:adamantine_boots",count:1}}}')
        self.assertNotEqual(canonical_stack(older),
                            canonical_stack(canonical_player(wrong_slot)['equipment']['head']))

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

    def test_native_text_and_flat_enchantment_maps_keep_their_data(self):
        older = nbtlib.parse_nbt('''{id:"basemetals:steel_sword",count:1,components:{
            "minecraft:custom_name":'{"text":"Old sword","color":"gold","bold":true}',
            "minecraft:lore":['{"text":"A keepsake"}'],
            "minecraft:enchantments":{levels:{"minecraft:unbreaking":2}}}}''')
        current = nbtlib.parse_nbt('''{id:"basemetals:steel_sword",count:1,components:{
            "minecraft:custom_name":{text:"Old sword",color:"gold",bold:1b},
            "minecraft:lore":[{text:"A keepsake"}],
            "minecraft:enchantments":{"minecraft:unbreaking":2}}}''')
        self.assertEqual(canonical_stack(older), canonical_stack(current))

        for key in current['components']:
            changed = copy.deepcopy(current)
            changed['components'].pop(key)
            self.assertNotEqual(canonical_stack(older), canonical_stack(changed), key)

        changed = copy.deepcopy(current)
        changed['components']['minecraft:custom_name']['color'] = nbtlib.String('red')
        self.assertNotEqual(canonical_stack(older), canonical_stack(changed))


if __name__ == '__main__':
    unittest.main()
