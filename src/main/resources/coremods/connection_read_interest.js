var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

function initializeCoreMod() {
    return {
        'basemetals_connection_read_interest': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.network.Connection' },
            'transformer': serializeReadChanges
        }
    };
}

function serializeReadChanges(node) {
    var packet = 'Lnet/minecraft/network/protocol/Packet;';
    var listener = 'Lnet/minecraft/network/PacketSendListener;';
    var protocol = 'Lnet/minecraft/network/ConnectionProtocol;';
    var sender = findMethod(node, ASMAPI.mapMethod('m_129520_'), '(' + packet + listener + ')V');
    var writer = findMethod(node, ASMAPI.mapMethod('m_243087_'), '(' + packet + listener + protocol + protocol + ')V');
    var pause = findCall(sender, 'io/netty/channel/ChannelConfig', 'setAutoRead');

    if (pause === null && findCall(writer, 'io/netty/channel/ChannelConfig', 'setAutoRead') !== null) {
        return node;
    }
    if (pause === null || findCall(writer, 'io/netty/channel/ChannelConfig', 'setAutoRead') !== null) {
        throw new Error('Base Metals found an unexpected Connection read-pause layout');
    }

    var value = previousCode(pause);
    var config = previousCode(value);
    var channel = previousCode(config);
    var self = previousCode(channel);
    var discard = pause.getNext();
    var changeProtocol = findCall(writer, node.name, ASMAPI.mapMethod('m_129498_'));
    var newProtocol = changeProtocol === null ? null : previousCode(changeProtocol);
    var receiver = newProtocol === null ? null : previousCode(newProtocol);

    if (value === null || value.getOpcode() !== Opcodes.ICONST_0
            || !(config instanceof MethodInsnNode) || config.name !== 'config'
            || !(channel instanceof FieldInsnNode) || channel.desc !== 'Lio/netty/channel/Channel;'
            || !(self instanceof VarInsnNode) || self.getOpcode() !== Opcodes.ALOAD || self.var !== 0
            || discard === null || discard.getOpcode() !== Opcodes.POP
            || !(receiver instanceof VarInsnNode) || receiver.getOpcode() !== Opcodes.ALOAD || receiver.var !== 0
            || !(newProtocol instanceof VarInsnNode) || newProtocol.var !== 3) {
        throw new Error('Base Metals could not move Connection read changes onto the event loop');
    }

    // Forge resumes reads on the event loop. Pause them there too, so a delayed
    // off-thread pause cannot erase the newer socket read request during login.
    var instructions = new InsnList();
    var instruction = self;
    var end = discard.getNext();
    while (instruction !== end) {
        var next = instruction.getNext();
        sender.instructions.remove(instruction);
        instructions.add(instruction);
        instruction = next;
    }
    writer.instructions.insertBefore(receiver, instructions);
    return node;
}

function findMethod(node, name, descriptor) {
    for (var i = 0; i < node.methods.size(); ++i) {
        var method = node.methods.get(i);
        if (method.name === name && method.desc === descriptor) return method;
    }
    throw new Error('Base Metals could not find Connection.' + name + descriptor);
}

function findCall(method, owner, name) {
    var found = null;
    for (var instruction = method.instructions.getFirst(); instruction !== null; instruction = instruction.getNext()) {
        if (instruction instanceof MethodInsnNode && instruction.owner === owner && instruction.name === name) {
            if (found !== null) throw new Error('Base Metals found duplicate calls to ' + owner + '.' + name);
            found = instruction;
        }
    }
    return found;
}

function previousCode(instruction) {
    if (instruction === null) return null;
    var previous = instruction.getPrevious();
    while (previous !== null && previous.getOpcode() < 0) previous = previous.getPrevious();
    return previous;
}
