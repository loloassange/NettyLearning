package com.example.netty_learning.chapter16_ClientChatInAction.client;

import com.example.netty_learning.chapter16_ClientChatInAction.client.handler.LoginResponseHandler;
import com.example.netty_learning.chapter16_ClientChatInAction.client.handler.MessageResponseHandler;
import com.example.netty_learning.chapter16_ClientChatInAction.codec.PacketDecoder;
import com.example.netty_learning.chapter16_ClientChatInAction.codec.PacketEncoder;
import com.example.netty_learning.chapter16_ClientChatInAction.codec.Spliter;
import com.example.netty_learning.chapter16_ClientChatInAction.protocol.request.LoginRequestPacket;
import com.example.netty_learning.chapter16_ClientChatInAction.protocol.request.MessageRequestPacket;
import com.example.netty_learning.chapter16_ClientChatInAction.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 *
 * 当有两个客户端登录成功之后，在控制台输入 userId + 空格 + 消息 来操作
 *
 */
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
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });

        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {

        bootstrap.connect(host, port).addListener(future -> {

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
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);

            }
        });
    }

    /**
     * 我们在客户端启动的时候，起一个线程:
     *
     * 如果当前用户还未登录，我们在控制台输入一个用户名，然后构造一个登录数据包发送给服务器，
     * 发完之后，我们等待一个超时时间，可以当做是登录逻辑的最大处理时间，这里就简单粗暴点了。
     *
     * 如果当前用户已经是登录状态，我们可以在控制台输入消息接收方的 userId，
     * 然后输入一个空格，再输入消息的具体内容，
     * 然后，我们就可以构建一个消息数据包，发送到服务端。
     *
     * @param channel
     */
    private static void startConsoleThread(Channel channel) {

        Scanner sc = new Scanner(System.in);
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        new Thread(() -> {

            while (!Thread.interrupted()) {

                if (!SessionUtil.hasLogin(channel)) {

                    System.out.print("输入用户名登录: ");
                    String username = sc.nextLine();
                    loginRequestPacket.setUserName(username);

                    // 密码使用默认的
                    loginRequestPacket.setPassword("pwd");

                    // 发送登录数据包
                    channel.writeAndFlush(loginRequestPacket);

                    // 当做是登录逻辑的最大处理时间
                    waitForLoginResponse();

                } else {

                    String toUserId = sc.next();
                    String message = sc.next();

                    channel.writeAndFlush(new MessageRequestPacket(toUserId, message));

                }

            }

        }).start();
    }


    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
