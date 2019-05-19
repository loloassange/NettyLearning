package com.example.netty_learning.chapter17_InitiationAndNotification.codec;

import com.example.netty_learning.chapter17_InitiationAndNotification.protocol.Packet;
import com.example.netty_learning.chapter17_InitiationAndNotification.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        PacketCodeC.INSTANCE.encode(out, packet);
    }
}
