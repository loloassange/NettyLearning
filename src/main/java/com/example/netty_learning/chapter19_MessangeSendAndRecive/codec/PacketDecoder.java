package com.example.netty_learning.chapter19_MessangeSendAndRecive.codec;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) {
        out.add(PacketCodec.INSTANCE.decode(in));
    }
}
