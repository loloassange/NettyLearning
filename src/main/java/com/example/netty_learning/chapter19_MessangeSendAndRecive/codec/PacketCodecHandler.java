package com.example.netty_learning.chapter19_MessangeSendAndRecive.codec;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.Packet;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import java.util.List;

/**
 * 首先，这里 PacketCodecHandler，他是一个无状态的 handler，因此，同样可以使用单例模式来实现。
 *
 *  我们看到，我们需要实现 decode() 和 encode() 方法，
 *  decode 是将二进制数据 ByteBuf 转换为 java 对象 Packet，
 *  而 encode 操作是一个相反的过程，在 encode() 方法里面，我们调用了 channel 的 内存分配器手工分配了 ByteBuf。
 *
 */
@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {

    public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();

    private PacketCodecHandler() {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {

        out.add(PacketCodec.INSTANCE.decode(byteBuf));
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) {

        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        PacketCodec.INSTANCE.encode(byteBuf, packet);
        out.add(byteBuf);
    }
}
