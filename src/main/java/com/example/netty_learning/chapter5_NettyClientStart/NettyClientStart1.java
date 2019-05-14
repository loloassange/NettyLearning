package com.example.netty_learning.chapter5_NettyClientStart;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 失败重连
 */
public class NettyClientStart1 {

    private static final int MAX_RETRY = 5;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap
                // 1.指定线程模型
                .group(workerGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                    }
                });

        // 4.建立连接
        connect(bootstrap, "juejin.im", 8009, MAX_RETRY);

    }

    /**
     * 递归connect方法
     *
     * @param bootstrap
     * @param host
     * @param port
     */
    private static void connect(Bootstrap bootstrap, String host, int port) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else {
                System.err.println("连接失败，开始重连");
                connect(bootstrap, host, port);
            }
        });
    }

    /**
     * 但是，通常情况下，连接建立失败不会立即重新连接，
     * 而是会通过一个指数退避的方式，
     * 比如每隔 1 秒、2 秒、4 秒、8 秒，以 2 的幂次来建立连接，
     * 然后到达一定次数之后就放弃连接，
     * 接下来我们就来实现一下这段逻辑，我们默认重试 5 次
     *
     * 从下面的代码可以看到，通过判断连接是否成功以及剩余重试次数，分别执行不同的逻辑
     *
     * 1. 如果连接成功则打印连接成功的消息
     * 2. 如果连接失败但是重试次数已经用完，放弃连接
     * 3. 如果连接失败但是重试次数仍然没有用完，则计算下一次重连间隔 delay，然后定期重连
     *
     * 我们定时任务是调用 bootstrap.config().group().schedule(),
     * 其中 bootstrap.config() 这个方法返回的是 BootstrapConfig，他是对 Bootstrap 配置参数的抽象，
     * 然后 bootstrap.config().group() 返回的就是我们在一开始的时候配置的线程模型 workerGroup，
     * 调 workerGroup 的 schedule 方法即可实现定时任务逻辑。
     *
     * 在 schedule 方法块里面，前面四个参数我们原封不动地传递，最后一个重试次数参数减掉一，就是下一次建立连接时候的上下文信息。
     * 读者可以自行修改代码，更改到一个连接不上的服务端 Host 或者 Port，查看控制台日志就可以看到5次重连日志。
     *
     * @param bootstrap
     * @param host
     * @param port
     * @param retry
     */
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {

        bootstrap.connect(host, port).addListener(future -> {

            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(
                        () -> connect(bootstrap, host, port, retry - 1),
                        delay,
                        TimeUnit.SECONDS
                );
            }

        });

    }

}
