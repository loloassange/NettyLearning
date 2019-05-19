package com.example.netty_learning.chapter19_MessangeSendAndRecive.codec;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.Packet;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        PacketCodec.INSTANCE.encode(out, packet);
    }
}
