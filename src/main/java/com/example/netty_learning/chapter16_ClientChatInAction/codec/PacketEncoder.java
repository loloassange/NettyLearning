package com.example.netty_learning.chapter16_ClientChatInAction.codec;

import com.example.netty_learning.chapter16_ClientChatInAction.protocol.Packet;
import com.example.netty_learning.chapter16_ClientChatInAction.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        PacketCodeC.INSTANCE.encode(out, packet);
    }

}
