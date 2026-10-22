var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var HOOK = 'zone/moddev/mc/basemetals/migration/LegacyWorldDataHook';
var NBT = 'Lnet/minecraft/nbt/CompoundTag;';
var STATE = 'Lnet/minecraft/world/level/block/state/BlockState;';
var ASMAPI = Java.type('net.neoforged.coremod.api.ASMAPI');

function initializeCoreMod() {
    return {
        'basemetals_expand_flattening_table': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.util.datafix.fixes.BlockStateData' },
            'transformer': function(node) {
                var patched = false;
                for (var i = 0; i < node.fields.size(); ++i) {
                    var field = node.fields.get(i);
                    if (field.name !== ASMAPI.mapField('f_14934_')
                            || field.desc !== '[Lcom/mojang/serialization/Dynamic;') continue;
                    // Modded numeric IDs need more room than vanilla's 4096-entry table.
                    field.access &= ~Opcodes.ACC_FINAL;
                    patched = true;
                }
                if (!patched) throw new Error('Base Metals could not make the legacy block table expandable');
                return node;
            }
        },
        'basemetals_legacy_world_info': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.level.storage.LevelStorageSource' },
            'transformer': function(node) {
                var patched = false;
                for (var i = 0; i < node.methods.size(); ++i) {
                    var method = node.methods.get(i);
                    if (method.desc !== '(Lcom/mojang/serialization/DynamicOps;' +
                            'Lnet/minecraft/world/level/WorldDataConfiguration;' +
                            'Lnet/minecraft/core/Registry;' +
                            'Lcom/mojang/serialization/Lifecycle;' +
                            'Ljava/nio/file/Path;Lcom/mojang/datafixers/DataFixer;)' +
                            'Lcom/mojang/datafixers/util/Pair;') continue;
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 4));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, 'java/nio/file/Path',
                            'toFile', '()Ljava/io/File;', true));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOK,
                            'prepareLegacyWorld', '(Ljava/io/File;)V', false));
                    method.instructions.insert(prefix);
                    patched = true;
                }
                if (!patched) throw new Error('Base Metals could not attach its legacy level.dat reader');
                return node;
            }
        },
        'basemetals_legacy_forge_snapshot': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.level.storage.LevelStorageSource$LevelStorageAccess' },
            'transformer': function(node) {
                var patched = false;
                for (var i = 0; i < node.methods.size(); ++i) {
                    var method = node.methods.get(i);
                    if (method.desc !== '(Ljava/nio/file/Path;Lcom/mojang/datafixers/DataFixer;)Ljava/lang/String;') continue;
                    var readsSnapshot = false;
                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        if (instruction instanceof MethodInsnNode
                                && instruction.owner === 'net/neoforged/common/ForgeHooks'
                                && instruction.name === 'readAdditionalLevelSaveData') readsSnapshot = true;
                    }
                    if (!readsSnapshot) continue;

                    // Forge reads its saved registry index before vanilla converts level.dat.
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, 'java/nio/file/Path',
                            'toFile', '()Ljava/io/File;', true));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOK,
                            'prepareLegacyWorld', '(Ljava/io/File;)V', false));
                    method.instructions.insert(prefix);
                    patched = true;
                }
                if (!patched) throw new Error('Base Metals could not prepare the saved Forge registry snapshot');
                return node;
            }
        },
        'basemetals_legacy_chunk_status': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.level.chunk.storage.ChunkStorage' },
            'transformer': function(node) {
                var patched = false;
                for (var i = 0; i < node.methods.size(); ++i) {
                    var method = node.methods.get(i);
                    if (method.desc !== '(Lnet/minecraft/resources/ResourceKey;' +
                            'Ljava/util/function/Supplier;' + NBT + 'Ljava/util/Optional;)' + NBT) continue;

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
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.server.level.WorldGenRegion' },
            'transformer': function(node) {
                var patched = false;
                for (var i = 0; i < node.methods.size(); ++i) {
                    var method = node.methods.get(i);
                    // Both setBlock and direct ore-section writes use this permission check.
                    if (method.name !== ASMAPI.mapMethod('m_180807_')
                            || method.desc !== '(Lnet/minecraft/core/BlockPos;)Z') continue;
                    var allowed = new LabelNode();
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOK,
                            'shouldBlockWorldgenWrite', '(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/core/BlockPos;)Z', false));
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
        }
    };
}
