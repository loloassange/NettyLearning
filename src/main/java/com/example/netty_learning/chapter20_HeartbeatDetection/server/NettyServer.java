package com.example.netty_learning.chapter20_HeartbeatDetection.server;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.server.handler.IMHandler;
import com.example.netty_learning.chapter20_HeartbeatDetection.codec.PacketCodecHandler;
import com.example.netty_learning.chapter20_HeartbeatDetection.codec.Spliter;
import com.example.netty_learning.chapter20_HeartbeatDetection.handler.IMIdleStateHandler;
import com.example.netty_learning.chapter20_HeartbeatDetection.server.handler.AuthHandler;
import com.example.netty_learning.chapter20_HeartbeatDetection.server.handler.HeartBeatRequestHandler;
import com.example.netty_learning.chapter20_HeartbeatDetection.server.handler.LoginRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

/**
 * 一.  网络问题
 *
 * 连接假死的现象是：
 * 在某一端（服务端或者客户端）看来，底层的 TCP 连接已经断开了，
 * 但是应用程序并没有捕获到，因此会认为这条连接仍然是存在的，
 * 从 TCP 层面来说，只有收到四次握手数据包或者一个 RST 数据包，连接的状态才表示已断开。
 *
 * 连接假死会带来以下两大问题:
 *
 *     对于服务端来说，因为每条连接都会耗费 cpu 和内存资源，大量假死的连接会逐渐耗光服务器的资源，最终导致性能逐渐下降，程序奔溃。
 *     对于客户端来说，连接假死会造成发送数据超时，影响用户体验。
 *
 * 通常，连接假死由以下几个原因造成的:
 *
 *     应用程序出现线程堵塞，无法进行数据的读写。
 *     客户端或者服务端网络相关的设备出现故障，比如网卡，机房故障。
 *     公网丢包。公网环境相对内网而言，非常容易出现丢包，网络抖动等现象，如果在一段时间内用户接入的网络连续出现丢包现象，那么对客户端来说数据一直发送不出去，而服务端也是一直收不到客户端来的数据，连接就一直耗着。
 *
 * 二. 服务端空闲检测
 *
 * 对于服务端来说，客户端的连接如果出现假死，那么服务端将无法收到客户端的数据，
 * 也就是说，如果能一直收到客户端发来的数据，那么可以说明这条连接还是活的，
 * 因此，服务端对于连接假死的应对策略就是空闲检测。
 *
 * 何为空闲检测？
 * 空闲检测指的是每隔一段时间，检测这段时间内是否有数据读写，
 * 简化一下，我们的服务端只需要检测一段时间内，是否收到过客户端发来的数据即可，
 * Netty 自带的 IdleStateHandler 就可以实现这个功能。
 *
 * 三. 客户端定时发心跳
 *
 * 服务端在一段时间内没有收到客户端的数据，这个现象产生的原因可以分为以下两种：
 *
 *     连接假死。
 *     非假死状态下确实没有发送数据。
 *
 * 我们只需要排除掉第二种可能性，那么连接自然就是假死的。
 * 要排查第二种情况，我们可以在客户端定期发送数据到服务端，通常这个数据包称为心跳数据包。
 *
 * 总结：
 *
 * 要处理假死问题首先我们要实现客户端与服务端定期发送心跳，在这里，其实服务端只需要对客户端的定时心跳包进行回复。
 *
 * ‘客户端与服务端如果都需要检测假死，那么直接在 pipeline 的最前方插入一个自定义 IdleStateHandler，在 channelIdle() 方法里面自定义连接假死之后的逻辑。
 *
 * 通常空闲检测时间要比发送心跳的时间的两倍要长一些，这也是为了排除偶发的公网抖动，防止误判。
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
                        // 空闲检测
                        /**
                         * 为什么要插入到最前面？
                         * 是因为如果插入到最后面的话，如果这条连接读到了数据，
                         * 但是在 inBound 传播的过程中出错了或者数据处理完完毕就不往后传递了（我们的应用程序属于这类），
                         * 那么最终 IMIdleStateHandler 就不会读到数据，最终导致误判。
                         */
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(PacketCodecHandler.INSTANCE);
                        ch.pipeline().addLast(LoginRequestHandler.INSTANCE);
                        // 由于这个 handler 的处理其实是无需登录的，所以，我们将该 handler 放置在 AuthHandler 前面
                        ch.pipeline().addLast(HeartBeatRequestHandler.INSTANCE);
                        ch.pipeline().addLast(AuthHandler.INSTANCE);
                        ch.pipeline().addLast(IMHandler.INSTANCE);
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
