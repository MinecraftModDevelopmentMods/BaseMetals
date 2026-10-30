var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function initializeCoreMod() {
    return {
        'basemetals_forge25_leaves_fixer': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.util.datafix.fixes.LeavesFix$Section' },
            'transformer': function(classNode) {
                var patched = false;
                for (var i = 0; i < classNode.methods.size(); ++i) {
                    var method = classNode.methods.get(i);
                    if (method.desc !== '(Lcom/mojang/datafixers/Typed;)Lcom/mojang/datafixers/Typed;') continue;
                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        // Some Forge installations already contain the corrected erased overload.
                        if (instruction.getOpcode() === Opcodes.INVOKEVIRTUAL
                                && instruction.owner === 'com/mojang/datafixers/Typed' && instruction.name === 'set'
                                && instruction.desc === '(Lcom/mojang/datafixers/OpticFinder;' +
                                        'Ljava/lang/Object;)Lcom/mojang/datafixers/Typed;') {
                            patched = true;
                            continue;
                        }
                        if (instruction.getOpcode() !== Opcodes.CHECKCAST
                                || instruction.desc !== 'com/mojang/datafixers/Typed') continue;
                        var call = instruction.getNext();
                        while (call !== null && call.getOpcode() < 0) call = call.getNext();
                        if (call === null || call.getOpcode() !== Opcodes.INVOKEVIRTUAL
                                || call.owner !== 'com/mojang/datafixers/Typed' || call.name !== 'set'
                                || call.desc !== '(Lcom/mojang/datafixers/OpticFinder;' +
                                        'Lcom/mojang/datafixers/Typed;)Lcom/mojang/datafixers/Typed;') continue;
                        method.instructions.remove(instruction);
                        call.desc = '(Lcom/mojang/datafixers/OpticFinder;' +
                                'Ljava/lang/Object;)Lcom/mojang/datafixers/Typed;';
                        patched = true;
                        break;
                    }
                }
                if (!patched) throw new Error('Base Metals could not repair the Forge 25 leaves data fixer');
                return classNode;
            }
        },
        'basemetals_legacy_world_info': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.storage.SaveFormatOld' },
            'transformer': function(classNode) {
                for (var i = 0; i < classNode.methods.size(); ++i) {
                    var method = classNode.methods.get(i);
                    if (method.desc !== '(Ljava/io/File;Lcom/mojang/datafixers/DataFixer;' +
                            'Lnet/minecraft/world/storage/SaveHandler;)Lnet/minecraft/world/storage/WorldInfo;') continue;
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            'zone/moddev/mc/basemetals/migration/LegacyWorldDataHook',
                            'prepareLegacyWorld', '(Ljava/io/File;)V', false));
                    method.instructions.insert(prefix);
                }
                return classNode;
            }
        },
        'basemetals_legacy_chunk_status': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.chunk.storage.AnvilChunkLoader' },
            'transformer': function(classNode) {
                var descriptor = '(Lnet/minecraft/world/dimension/DimensionType;' +
                        'Lnet/minecraft/world/storage/WorldSavedDataStorage;II)' +
                        'Lnet/minecraft/nbt/NBTTagCompound;';
                var patchedRead = false;
                var patchedReturn = false;
                for (var i = 0; i < classNode.methods.size(); ++i) {
                    var method = classNode.methods.get(i);
                    if (method.desc !== descriptor) continue;
                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        var previous = instruction.getPrevious();
                        if (instruction.getOpcode() === Opcodes.ASTORE && previous !== null
                                && previous.getOpcode() === Opcodes.INVOKESTATIC
                                && previous.owner === 'net/minecraft/nbt/CompressedStreamTools'
                                && previous.desc.endsWith(')Lnet/minecraft/nbt/NBTTagCompound;')) {
                            var prepare = new InsnList();
                            prepare.add(new VarInsnNode(Opcodes.ALOAD, instruction.var));
                            prepare.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    'zone/moddev/mc/basemetals/migration/LegacyWorldDataHook',
                                    'prepareLegacyChunk', '(Lnet/minecraft/nbt/NBTTagCompound;)V', false));
                            method.instructions.insert(instruction, prepare);
                            patchedRead = true;
                        } else if (instruction.getOpcode() === Opcodes.ARETURN) {
                            method.instructions.insertBefore(instruction, new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    'zone/moddev/mc/basemetals/migration/LegacyWorldDataHook',
                                    'finalizeLegacyChunk',
                                    '(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/nbt/NBTTagCompound;', false));
                            patchedReturn = true;
                        }
                    }
                }
                if (!patchedRead || !patchedReturn) throw new Error('Base Metals could not patch the Forge 25 legacy chunk loader');
                return classNode;
            }
        },
        'basemetals_legacy_worldgen_guard': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.gen.WorldGenRegion' },
            'transformer': function(classNode) {
                var descriptor = '(Lnet/minecraft/util/math/BlockPos;' +
                        'Lnet/minecraft/block/state/IBlockState;I)Z';
                var patched = false;
                for (var i = 0; i < classNode.methods.size(); ++i) {
                    var method = classNode.methods.get(i);
                    if (method.desc !== descriptor) continue;
                    var allowed = new LabelNode();
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            'zone/moddev/mc/basemetals/migration/LegacyWorldDataHook',
                            'shouldBlockWorldgenWrite', '(Lnet/minecraft/util/math/BlockPos;)Z', false));
                    prefix.add(new JumpInsnNode(Opcodes.IFEQ, allowed));
                    prefix.add(new InsnNode(Opcodes.ICONST_0));
                    prefix.add(new InsnNode(Opcodes.IRETURN));
                    prefix.add(allowed);
                    method.instructions.insert(prefix);
                    patched = true;
                }
                if (!patched) throw new Error('Base Metals could not patch the Forge 25 legacy-world write guard');
                return classNode;
            }
        },
        'basemetals_durable_anvils': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.block.BlockAnvil' },
            'transformer': function(classNode) {
                var descriptor = '(Lnet/minecraft/block/state/IBlockState;)' +
                        'Lnet/minecraft/block/state/IBlockState;';
                var patched = false;
                for (var i = 0; i < classNode.methods.size(); ++i) {
                    var method = classNode.methods.get(i);
                    if (method.desc !== descriptor || (method.access & Opcodes.ACC_STATIC) === 0) continue;
                    var vanilla = new LabelNode();
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            'zone/moddev/mc/basemetals/content/BaseMetalAnvilBlock',
                            'isBaseMetalAnvil', '(Lnet/minecraft/block/state/IBlockState;)Z', false));
                    prefix.add(new JumpInsnNode(Opcodes.IFEQ, vanilla));
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            'zone/moddev/mc/basemetals/content/BaseMetalAnvilBlock',
                            'damageBaseMetalAnvil', descriptor, false));
                    prefix.add(new InsnNode(Opcodes.ARETURN));
                    prefix.add(vanilla);
                    method.instructions.insert(prefix);
                    patched = true;
                }
                if (!patched) throw new Error('Base Metals could not patch Forge 25 anvil durability');
                return classNode;
            }
        },
        'basemetals_molten_metal_renderer': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.client.renderer.BlockFluidRenderer' },
            'transformer': function(classNode) {
                var descriptor = '(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;' +
                        'Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/fluid/IFluidState;)Z';
                for (var i = 0; i < classNode.methods.size(); ++i) {
                    var method = classNode.methods.get(i);
                    if (method.name !== 'render' || method.desc !== descriptor) continue;
                    var flag = false, sprites = false, color = false;
                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        if (!flag && instruction.getOpcode() === Opcodes.ISTORE && instruction.var === 5) {
                            var hookFlag = new InsnList();
                            hookFlag.add(new VarInsnNode(Opcodes.ALOAD, 4));
                            hookFlag.add(new VarInsnNode(Opcodes.ILOAD, 5));
                            hookFlag.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    'zone/moddev/mc/basemetals/client/ClientMoltenMetalRenderer',
                                    'useOpaqueFluidPath', '(Lnet/minecraft/fluid/IFluidState;Z)Z', false));
                            hookFlag.add(new VarInsnNode(Opcodes.ISTORE, 5));
                            method.instructions.insert(instruction, hookFlag);
                            flag = true;
                        } else if (!sprites && instruction.getOpcode() === Opcodes.ASTORE && instruction.var === 6) {
                            var hookSprites = new InsnList();
                            hookSprites.add(new VarInsnNode(Opcodes.ALOAD, 4));
                            hookSprites.add(new VarInsnNode(Opcodes.ALOAD, 6));
                            hookSprites.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    'zone/moddev/mc/basemetals/client/ClientMoltenMetalRenderer',
                                    'overrideSprites',
                                    '(Lnet/minecraft/fluid/IFluidState;[Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)' +
                                    '[Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;', false));
                            hookSprites.add(new VarInsnNode(Opcodes.ASTORE, 6));
                            method.instructions.insert(instruction, hookSprites);
                            sprites = true;
                        } else if (!color && instruction.getOpcode() === Opcodes.ISTORE && instruction.var === 7) {
                            var hookColor = new InsnList();
                            hookColor.add(new VarInsnNode(Opcodes.ALOAD, 4));
                            hookColor.add(new VarInsnNode(Opcodes.ILOAD, 7));
                            hookColor.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    'zone/moddev/mc/basemetals/client/ClientMoltenMetalRenderer',
                                    'overrideColor', '(Lnet/minecraft/fluid/IFluidState;I)I', false));
                            hookColor.add(new VarInsnNode(Opcodes.ISTORE, 7));
                            method.instructions.insert(instruction, hookColor);
                            color = true;
                        }
                    }
                    if (!flag || !sprites || !color) throw new Error('Base Metals could not patch the Forge 25 fluid renderer');
                }
                return classNode;
            }
        }
    };
}
