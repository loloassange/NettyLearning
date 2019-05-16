package com.example.netty_learning.chapter12_PipelineInAction.codec;

import com.example.netty_learning.chapter12_PipelineInAction.protocol.Packet;
import com.example.netty_learning.chapter12_PipelineInAction.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * MessageToByteEncoder
 *
 * 1. 将对象转换到二进制数据
 *
 * 2. 我们不需要每一次将响应写到对端的时候调用一次编码逻辑进行编码，也不需要自行创建 ByteBuf
 *
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        PacketCodeC.INSTANCE.encode(out, packet);
    }
}
