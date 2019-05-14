package com.example.netty_learning.chapter6_BidirectionalCommunication.Client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * 1. 这个逻辑处理器继承自 ChannelInboundHandlerAdapter，
 *    然后覆盖了 channelActive()方法，这个方法会在客户端连接建立成功之后被调用
 *
 * 2. 客户端连接建立成功之后，调用到 channelActive() 方法，
 *    在这个方法里面，我们编写向服务端写数据的逻辑
 *
 * 3. 写数据的逻辑分为两步：
 *    首先我们需要获取一个 netty 对二进制数据的抽象 ByteBuf，
 *        下面代码中, ctx.alloc() 获取到一个 ByteBuf 的内存管理器，这个 内存管理器的作用就是分配一个 ByteBuf，
 *    然后我们把字符串的二进制数据填充到 ByteBuf，这样我们就获取到了 Netty 需要的一个数据格式，
 *    最后我们调用 ctx.channel().writeAndFlush() 把数据写到服务端
 *
 * 以上就是客户端启动之后，向服务端写数据的逻辑，我们可以看到:
 *    和传统的 socket 编程不同的是，Netty 里面数据是以 ByteBuf 为单位的，
 *    所有需要写出的数据都必须塞到一个 ByteBuf，数据的写出是如此，数据的读取亦是如此。
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        System.out.println(new Date() + ": 客户端写出数据");

        // 1.获取数据
        ByteBuf buffer = getByteBuf(ctx);

        // 2.写数据
        ctx.channel().writeAndFlush(buffer);

    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {

        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = "你好，Coder!".getBytes(Charset.forName("utf-8"));

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;

    }

    /**
     * 客户端的读取数据的逻辑
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));

    }

}