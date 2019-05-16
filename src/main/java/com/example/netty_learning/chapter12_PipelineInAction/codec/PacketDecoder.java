package com.example.netty_learning.chapter12_PipelineInAction.codec;

import com.example.netty_learning.chapter12_PipelineInAction.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * ByteToMessageDecoder
 *      1. 把二进制数据转换到我们的一个 Java 对象
 *      2. 使用 ByteToMessageDecoder，Netty 会自动进行内存的释放
 *
 */
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) {
        out.add(PacketCodeC.INSTANCE.decode(in));
    }

}
