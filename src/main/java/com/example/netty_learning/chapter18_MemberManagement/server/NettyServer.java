package com.example.netty_learning.chapter18_MemberManagement.server;

import com.example.netty_learning.chapter18_MemberManagement.codec.PacketDecoder;
import com.example.netty_learning.chapter18_MemberManagement.codec.PacketEncoder;
import com.example.netty_learning.chapter18_MemberManagement.codec.Spliter;
import com.example.netty_learning.chapter18_MemberManagement.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Date;

/**
 * 添加一个服务端和客户端交互的新功能只需要遵循以下的步骤：
 *
 *     1. 创建控制台指令对应的 ConsoleCommand 并添加到 ConsoleCommandManager。
 *
 *     2. 控制台输入指令和数据之后填入协议对应的指令数据包 - xxxRequestPacket，将请求写到服务端。
 *
 *     3. 服务端创建对应的 xxxRequestPacketHandler 并添加到服务端的 pipeline 中，
 *         在 xxxRequestPacketHandler 处理完之后构造对应的 xxxResponsePacket 发送给客户端。
 *
 *     4. 客户端创建对应的 xxxResponsePacketHandler 并添加到客户端的 pipeline 中，最后在 xxxResponsePacketHandler 完成响应的处理。
 *
 *     5. 最后，最容易忽略的一点就是，新添加 xxxPacket 别忘了完善编解码器 PacketCodec 中的 packetTypeMap！
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
                        // 登录请求处理器
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new AuthHandler());
                        // 单聊消息请求处理器
                        ch.pipeline().addLast(new MessageRequestHandler());
                        // 创建群请求处理器
                        ch.pipeline().addLast(new CreateGroupRequestHandler());
                        // 加群请求处理器
                        ch.pipeline().addLast(new JoinGroupRequestHandler());
                        // 退群请求处理器
                        ch.pipeline().addLast(new QuitGroupRequestHandler());
                        // 获取群成员请求处理器
                        ch.pipeline().addLast(new ListGroupMembersRequestHandler());
                        // 登出请求处理器
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
