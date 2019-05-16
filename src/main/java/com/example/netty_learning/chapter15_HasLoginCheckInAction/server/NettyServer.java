package com.example.netty_learning.chapter15_HasLoginCheckInAction.server;

import com.example.netty_learning.chapter15_HasLoginCheckInAction.codec.PacketDecoder;
import com.example.netty_learning.chapter15_HasLoginCheckInAction.codec.PacketEncoder;
import com.example.netty_learning.chapter15_HasLoginCheckInAction.codec.Spliter;
import com.example.netty_learning.chapter15_HasLoginCheckInAction.server.handler.AuthHandler;
import com.example.netty_learning.chapter15_HasLoginCheckInAction.server.handler.LoginRequestHandler;
import com.example.netty_learning.chapter15_HasLoginCheckInAction.server.handler.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

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
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        /**
                         * 新增加用户认证handler:
                         *
                         * Netty 的 pipeline 机制帮我们省去了重复添加同一段逻辑的烦恼，
                         * 我们只需要在后续所有的指令处理 handler 之前插入一个用户认证 handle
                         *
                         * 我们在 MessageRequestHandler 之前插入了一个 AuthHandler，
                         * 因此 MessageRequestHandler 以及后续所有指令相关的 handler（后面小节会逐个添加）的处理都会经过 AuthHandler 的一层过滤，
                         * 只要在 AuthHandler 里面处理掉身份认证相关的逻辑，后续所有的 handler 都不用操心身份认证这个逻辑，
                         */
                        ch.pipeline().addLast(new AuthHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
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
