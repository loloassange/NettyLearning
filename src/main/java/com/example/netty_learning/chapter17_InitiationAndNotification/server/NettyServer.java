package com.example.netty_learning.chapter17_InitiationAndNotification.server;

import com.example.netty_learning.chapter17_InitiationAndNotification.server.handler.CreateGroupRequestHandler;
import com.example.netty_learning.chapter17_InitiationAndNotification.server.handler.LogoutRequestHandler;
import com.example.netty_learning.chapter17_InitiationAndNotification.codec.PacketDecoder;
import com.example.netty_learning.chapter17_InitiationAndNotification.codec.PacketEncoder;
import com.example.netty_learning.chapter17_InitiationAndNotification.codec.Spliter;
import com.example.netty_learning.chapter17_InitiationAndNotification.server.handler.AuthHandler;
import com.example.netty_learning.chapter17_InitiationAndNotification.server.handler.LoginRequestHandler;
import com.example.netty_learning.chapter17_InitiationAndNotification.server.handler.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

/**
 * 1. 群聊的原理和单聊类似，无非都是通过标识拿到 channel。
 *
 * 2. 本小节，我们重构了一下控制台的程序结构，在实际带有 UI 的 IM 应用中，我们输入的第一个指令其实就是对应我们点击 UI 的某些按钮或菜单的操作。
 *
 * 3. 通过 ChannelGroup，我们可以很方便地对一组 channel 进行批量操作。
 *
 */
public class NettyServer {

    private static final int PORT = 8000;

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
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new AuthHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        // 添加一个新的 handler 来处理创建群聊请求的指令
                        ch.pipeline().addLast(new CreateGroupRequestHandler());
                        ch.pipeline().addLast(new LogoutRequestHandler());
                        ch.pipeline().addLast(new PacketEncoder());
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
