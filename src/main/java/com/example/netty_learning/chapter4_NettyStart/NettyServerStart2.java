package com.example.netty_learning.chapter4_NettyStart;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

public class NettyServerStart2 {

    /**
     * Example:服务端启动其他方法
     *
     * ServerBootstrap除了指定线程模型，IO 模型，连接读写处理逻辑之外，它还可以干哪些事情？
     *
     * 1. 给服务端 channel NioServerSocketChannel指定一些自定义属性:
     *    handler()
     *    attr()
     *
     * 2. 给每一条连接指定自定义属性
     *    childAttr()
     *    childOption()
     *
     * 3. 给服务端channel设置一些属性
     *    option()
     *
     * @param args
     */
    public static void main(String[] args) {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        /**
         * handler()方法:
         * 可以和我们前面分析的childHandler()方法对应起来，
         * childHandler()用于指定处理新连接数据的读写处理逻辑，
         * handler()用于指定在服务端启动过程中的一些逻辑，
         * 通常情况下，我们用不着这个方法。
         */
        serverBootstrap.handler(new ChannelInitializer<NioServerSocketChannel>() {
            @Override
            protected void initChannel(NioServerSocketChannel ch) {
                System.out.println("服务端启动中");
            }
        });

        /**
         * attr()方法可以给服务端的 channel，也就是NioServerSocketChannel指定一些自定义属性，
         * 然后我们可以通过channel.attr()取出这个属性，
         * 比如，上面的代码我们指定我们服务端channel的一个serverName属性，属性值为nettyServer，
         * 其实说白了就是给NioServerSocketChannel维护一个map而已，
         * 通常情况下，我们也用不上这个方法。
         */
        serverBootstrap.attr(AttributeKey.newInstance("serverName"), "nettyServer");

        /**
         * childAttr可以给每一条连接指定自定义属性，
         * 然后后续我们可以通过channel.attr()取出该属性。
         */
        serverBootstrap.childAttr(AttributeKey.newInstance("clientKey"), "clientValue");

        /**
         * childOption()可以给每条连接设置一些TCP底层相关的属性，比如上面，我们设置了两种TCP属性，其中
         *
         * ChannelOption.SO_KEEPALIVE   表示是否开启TCP底层心跳机制，true为开启
         *
         * ChannelOption.TCP_NODELAY    表示是否开启Nagle算法，true表示关闭，false表示开启，
         *                              通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，
         *                                       如果需要减少发送次数减少网络交互，就开启。
         *
         */
        serverBootstrap
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true);

        /**
         * 除了给每个连接设置这一系列属性之外，
         * 我们还可以给服务端channel设置一些属性，最常见的就是so_backlog，
         *
         * 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，
         * 如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
         */
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);

    }

}
