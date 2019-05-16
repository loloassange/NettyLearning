package com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol;

import com.example.netty_learning.chapter17_GroupChatInAction_Part1.Constent;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.request.CreateGroupRequestPacket;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.request.LoginRequestPacket;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.request.LogoutRequestPacket;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.request.MessageRequestPacket;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.response.CreateGroupResponsePacket;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.response.LoginResponsePacket;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.response.LogoutResponsePacket;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.response.MessageResponsePacket;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.serialize.Serializer;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class PacketCodeC {

    public static final int MAGIC_NUMBER = 0x12345678;
    public static final PacketCodeC INSTANCE = new PacketCodeC();

    private final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private final Map<Byte, Serializer> serializerMap;


    private PacketCodeC() {

        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Constent.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Constent.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Constent.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Constent.MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(Constent.LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetTypeMap.put(Constent.LOGOUT_RESPONSE, LogoutResponsePacket.class);
        packetTypeMap.put(Constent.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(Constent.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public void encode(ByteBuf byteBuf, Packet packet) {

        // 1. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 2. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }


    public Packet decode(ByteBuf byteBuf) {

        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {

        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {

        return packetTypeMap.get(command);
    }
}
