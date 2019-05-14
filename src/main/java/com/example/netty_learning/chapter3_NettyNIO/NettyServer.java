package com.example.netty_learning.chapter3_NettyNIO;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 那么 Netty 到底是何方神圣？
 *     用一句简单的话来说就是：Netty 封装了 JDK 的 chapter2_NIO，让你用得更爽，你不用再写一大堆复杂的代码了。
 *
 * 用官方正式的话来说就是：
 *     Netty 是一个 异步事件驱动 的网络应用框架，用于 快速开发可维护的高性能服务器和客户端。
 *
 * 使用 Netty 不使用 JDK 原生 chapter2_NIO 的原因:
 *
 * 1. 使用 JDK 自带的NIO需要了解太多的概念，编程复杂，一不小心 bug 横飞
 *
 * 2. Netty 底层 IO 模型随意切换，而这一切只需要做微小的改动，改改参数，Netty可以直接从 chapter2_NIO 模型变身为 IO 模型
 *
 * 3. Netty 自带的拆包解包，异常检测等机制让你从NIO的繁重细节中脱离出来，让你只需要关心业务逻辑
 *
 * 4. Netty 解决了 JDK 的很多包括空轮询在内的 Bug
 *
 * 5. Netty 底层对线程，selector 做了很多细小的优化，精心设计的 reactor 线程模型做到非常高效的并发处理
 *
 * 6. 自带各种协议栈让你处理任何一种通用协议都几乎不用亲自动手
 *
 * 7. Netty 社区活跃，遇到问题随时邮件列表或者 issue
 *
 * 8. Netty 已经历各大 RPC 框架，消息中间件，分布式通信中间件线上的广泛验证，健壮性无比强大
 *
 */
public class NettyServer {

    /**
     * 这么一小段代码就实现了我们前面 chapter2_NIO 编程中的所有的功能，包括服务端启动，接受新连接，打印客户端传来的数据
     *
     * @param args
     */
    public static void main(String[] args) {

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // boss 对应 IOServer.java 中的接受新连接线程，主要负责创建新连接
        NioEventLoopGroup boss = new NioEventLoopGroup();
        // worker 对应 IOServer.java 中的负责读取数据的线程，主要用于读取数据以及业务逻辑处理
        NioEventLoopGroup worker = new NioEventLoopGroup();

        serverBootstrap.group(boss, worker)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) {
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                            System.out.println(msg);
                        }
                    });
                }
            })
            .bind(8000);

    }

}
