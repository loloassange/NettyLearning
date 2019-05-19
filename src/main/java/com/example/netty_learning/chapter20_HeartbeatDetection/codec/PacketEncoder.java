package com.example.netty_learning.chapter20_HeartbeatDetection.codec;

import com.example.netty_learning.chapter20_HeartbeatDetection.protocol.Packet;
import com.example.netty_learning.chapter20_HeartbeatDetection.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        PacketCodec.INSTANCE.encode(out, packet);
    }
}
