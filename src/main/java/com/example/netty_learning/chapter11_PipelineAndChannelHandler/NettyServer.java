package com.example.netty_learning.chapter11_PipelineAndChannelHandler;

import com.example.netty_learning.chapter11_PipelineAndChannelHandler.handler.inbound.InBoundHandlerA;
import com.example.netty_learning.chapter11_PipelineAndChannelHandler.handler.outbound.OutBoundHandlerC;
import com.example.netty_learning.chapter11_PipelineAndChannelHandler.handler.inbound.InBoundHandlerB;
import com.example.netty_learning.chapter11_PipelineAndChannelHandler.handler.inbound.InBoundHandlerC;
import com.example.netty_learning.chapter11_PipelineAndChannelHandler.handler.outbound.OutBoundHandlerA;
import com.example.netty_learning.chapter11_PipelineAndChannelHandler.handler.outbound.OutBoundHandlerB;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Date;

public class NettyServer {

    private static final int PORT = 8000;

    /**
     * 如何避免 else 泛滥？
     * Netty 中的 pipeline 和 channelHandler 正是用来解决这个问题的：
     *   它通过责任链设计模式来组织代码逻辑，并且能够支持逻辑的动态添加和删除 ，
     *   Netty 能够支持各类协议的扩展，比如 HTTP，Websocket，Redis，靠的就是 pipeline 和 channelHandler。
     *
     * 1. channelHandler 分为 inBound 和 outBound 两种类型的接口，
     *    分别是处理数据读与数据写的逻辑，可与 tcp 协议栈联系起来。
     *
     * 2. 两种类型的 handler 均有相应的默认实现，默认会把事件传递到下一个，
     *    这里的传递事件其实说白了就是把本 handler 的处理结果传递到下一个 handler 继续处理。
     *
     * 3. inBoundHandler 的执行顺序与我们实际的添加顺序相同，而 outBoundHandler 则相反。
     *
     * @param args
     */
    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        /**
                         * 不管我们定义的是哪种类型的 handler,
                         * 最终它们都是以双向链表的方式连接，
                         * 这里实际链表的节点是 ChannelHandlerContext
                         */
                        // inBound，处理读数据的逻辑链
                        // inBoundHandler 的执行顺序与通过 addLast() 方法 添加的顺序保持一致
                        ch.pipeline().addLast(new InBoundHandlerA());
                        ch.pipeline().addLast(new InBoundHandlerB());
                        ch.pipeline().addLast(new InBoundHandlerC());

                        // outBound，处理写数据的逻辑链
                        // outBoundHandler 的执行顺序与通过 addLast() 方法 添加的顺序保持相反
                        ch.pipeline().addLast(new OutBoundHandlerA());
                        ch.pipeline().addLast(new OutBoundHandlerB());
                        ch.pipeline().addLast(new OutBoundHandlerC());
                    }
                });


        bind(serverBootstrap, PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }
}
