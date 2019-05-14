package com.example.netty_learning.chapter5_NettyClientStart;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * chapter5 总结：
 * 1. Netty 客户端启动的流程，一句话来说就是：
 *      创建一个引导类，然后给他指定线程模型，IO 模型，连接读写处理逻辑，连接上特定主机和端口，客户端就启动起来了。
 *
 * 2. connect 方法是异步的，我们可以通过这个异步回调机制来实现 指数退避 重连逻辑。
 *
 * 3. Netty 客户端启动额外的参数，主要包括
 *      给客户端 Channel 绑定自定义属性值，
 *      设置底层 TCP 参数。
 */
public class NettyClientStart {

    /**
     * 对于客户端的启动来说，和服务端的启动类似，依然需要
     *  (1)线程模型、
     *  (2)IO 模型，
     *  (3)以及 IO 业务处理逻辑
     * 三大参数
     *
     * 客户端启动的引导类是 Bootstrap，负责启动客户端以及连接服务端，
     * 而上一小节我们在描述服务端的启动的时候，这个辅导类是 ServerBootstrap，
     *
     * 引导类创建完成之后，下面我们描述一下客户端启动的流程:
     *
     * (1)首先，与服务端的启动一样，我们需要给它指定线程模型，驱动着连接的数据读写，
     *    这个线程的概念  可以和第一小节Netty是什么中的  IOClient.java创建的线程  联系起来
     *
     * (2)然后，我们指定 IO 模型为 NioSocketChannel，表示 IO 模型为 NIO，
     *    当然，你可以可以设置 IO 模型为 OioSocketChannel，但是通常不会这么做，因为 Netty 的优势在于 NIO
     *
     * (3)接着，给引导类指定一个 handler，这里主要就是定义连接的业务处理逻辑，不理解没关系，在后面我们会详细分析
     *
     * (4)配置完线程模型、IO 模型、业务处理逻辑之后，调用 connect 方法进行连接，可以看到 connect 方法有两个参数，
     *    第一个参数可以填写 IP 或者域名，
     *    第二个参数填写的是端口号，
     *    由于 connect 方法返回的是一个 Future，也就是说这个方是异步的，
     *    我们通过 addListener 方法可以监听到连接是否成功，进而打印出连接信息
     *
     * 到了这里，一个客户端的启动的 Demo 就完成了，
     * 其实只要和 客户端 Socket 编程模型对应起来，这里的三个概念就会显得非常简单，
     * 遗忘掉的同学可以回顾一下 chapter1 中的 IOClient.java 再回来看这里的启动流程
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
        bootstrap.connect("juejin.im", 80).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else {
                System.err.println("连接失败!");
            }

        });

    }

}
