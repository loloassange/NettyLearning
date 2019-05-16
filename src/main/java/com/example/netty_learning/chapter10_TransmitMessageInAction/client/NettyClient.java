package com.example.netty_learning.chapter10_TransmitMessageInAction.client;

import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.PacketCodeC;
import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.request.MessageRequestPacket;
import com.example.netty_learning.chapter10_TransmitMessageInAction.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });

        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {

        bootstrap.connect(host, port).addListener(
            future -> {

                if (future.isSuccess()) {

                    System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                    Channel channel = ((ChannelFuture) future).channel();
                    startConsoleThread(channel);

                } else if (retry == 0) {

                    System.err.println("重试次数已用完，放弃连接！");

                } else {

                    // 第几次重连
                    int order = (MAX_RETRY - retry) + 1;
                    // 本次重连的间隔
                    int delay = 1 << order;
                    System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                    bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                            .SECONDS);

                }
            }
        );

    }

    private static void startConsoleThread(Channel channel) {

        new Thread(() -> {
            while (!Thread.interrupted()) {
                /**
                 * 如何判断客户端是否已经登录？
                 *
                 * 可以给客户端连接，也就是 Channel 绑定属性，通过 channel.attr(xxx).set(xx) 的方式，
                 * 那么我们是否可以在登录成功之后，给 Channel 绑定一个登录成功的标志位，
                 * 然后判断是否登录成功的时候取出这个标志位就可以了
                 */
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMessage(line);
                    ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(channel.alloc(), packet);
                    channel.writeAndFlush(byteBuf);
                }
            }
        }).start();

    }
}
