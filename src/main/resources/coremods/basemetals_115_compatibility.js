var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var HOOK = 'zone/moddev/mc/basemetals/migration/LegacyWorldDataHook';
var NBT = 'Lnet/minecraft/nbt/CompoundNBT;';
var STATE = 'Lnet/minecraft/block/BlockState;';

function initializeCoreMod() {
    return {
        'basemetals_legacy_world_info': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.storage.SaveFormat' },
            'transformer': function(node) {
                var patched = false;
                for (var i = 0; i < node.methods.size(); ++i) {
                    var method = node.methods.get(i);
                    if (method.desc !== '(Ljava/io/File;Lcom/mojang/datafixers/DataFixer;' +
                            'Lnet/minecraft/world/storage/SaveHandler;)Lnet/minecraft/world/storage/WorldInfo;') continue;
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOK,
                            'prepareLegacyWorld', '(Ljava/io/File;)V', false));
                    method.instructions.insert(prefix);
                    patched = true;
                }
                if (!patched) throw new Error('Base Metals could not attach its legacy level.dat reader');
                return node;
            }
        },
        'basemetals_legacy_chunk_status': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.chunk.storage.ChunkLoader' },
            'transformer': function(node) {
                var patched = false;
                for (var i = 0; i < node.methods.size(); ++i) {
                    var method = node.methods.get(i);
                    if (method.desc !== '(Lnet/minecraft/world/dimension/DimensionType;' +
                            'Ljava/util/function/Supplier;' + NBT + ')' + NBT) continue;

                    // Restore mod IDs before vanilla flattens the numeric block palette.
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOK,
                            'prepareLegacyChunk', '(' + NBT + ')V', false));
                    method.instructions.insert(prefix);

                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        if (instruction.getOpcode() === Opcodes.ARETURN) {
                            method.instructions.insertBefore(instruction, new MethodInsnNode(Opcodes.INVOKESTATIC,
                                    HOOK, 'finalizeLegacyChunk', '(' + NBT + ')' + NBT, false));
                        }
                    }
                    patched = true;
                }
                if (!patched) throw new Error('Base Metals could not attach its legacy chunk converter');
                return node;
            }
        },
        'basemetals_legacy_worldgen_guard': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.gen.WorldGenRegion' },
            'transformer': function(node) {
                var patched = false;
                for (var i = 0; i < node.methods.size(); ++i) {
                    var method = node.methods.get(i);
                    if (method.desc !== '(Lnet/minecraft/util/math/BlockPos;' + STATE + 'I)Z') continue;
                    var allowed = new LabelNode();
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOK,
                            'shouldBlockWorldgenWrite', '(Lnet/minecraft/util/math/BlockPos;)Z', false));
                    prefix.add(new JumpInsnNode(Opcodes.IFEQ, allowed));
                    prefix.add(new InsnNode(Opcodes.ICONST_0));
                    prefix.add(new InsnNode(Opcodes.IRETURN));
                    prefix.add(allowed);
                    method.instructions.insert(prefix);
                    patched = true;
                }
                if (!patched) throw new Error('Base Metals could not protect existing legacy terrain');
                return node;
            }
        },
        'basemetals_durable_anvils': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.block.AnvilBlock' },
            'transformer': function(node) {
                var patched = false;
                for (var i = 0; i < node.methods.size(); ++i) {
                    var method = node.methods.get(i);
                    if (method.desc !== '(' + STATE + ')' + STATE
                            || (method.access & Opcodes.ACC_STATIC) === 0) continue;
                    var vanilla = new LabelNode();
                    var owner = 'zone/moddev/mc/basemetals/content/BaseMetalAnvilBlock';
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner,
                            'isBaseMetalAnvil', '(' + STATE + ')Z', false));
                    prefix.add(new JumpInsnNode(Opcodes.IFEQ, vanilla));
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner,
                            'damageBaseMetalAnvil', '(' + STATE + ')' + STATE, false));
                    prefix.add(new InsnNode(Opcodes.ARETURN));
                    prefix.add(vanilla);
                    method.instructions.insert(prefix);
                    patched = true;
                }
                if (!patched) throw new Error('Base Metals could not attach custom anvil wear');
                return node;
            }
        }
    };
}
