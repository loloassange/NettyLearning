package com.example.netty_learning.chapter17_GroupChatInAction_Part1.codec;

import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.Packet;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        PacketCodeC.INSTANCE.encode(out, packet);
    }
}
