package com.example.netty_learning.chapter8;

import com.example.netty_learning.chapter8_EncodeAndDecode.protocol.command.LoginRequestPacket;
import com.example.netty_learning.chapter8_EncodeAndDecode.protocol.command.Packet;
import com.example.netty_learning.chapter8_EncodeAndDecode.protocol.command.PacketCodeC;
import com.example.netty_learning.chapter8_EncodeAndDecode.serialize.Serializer;
import com.example.netty_learning.chapter8_EncodeAndDecode.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;

public class PacketCodeCTest {

    @Test
    public void encode() {

        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUserId(123);
        loginRequestPacket.setUsername("testman");
        loginRequestPacket.setPassword("password");

        PacketCodeC packetCodeC = new PacketCodeC();
        ByteBuf byteBuf = packetCodeC.encode(loginRequestPacket);
        Packet decodedPacket = packetCodeC.decode(byteBuf);

        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));

    }

}
