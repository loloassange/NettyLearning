package com.example.netty_learning.chapter4_NettyStart;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 *
 */
public class NettyServerStart1 {

    /**
     * Example:自动绑定递增端口
     */
    public static void main(String[] args){

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                    }
                });

        bind(serverBootstrap, 1000);

    }

    /**
     * 在main方法中我们绑定了 8000 端口，接下来我们实现一个稍微复杂一点的逻辑:
     * 我们指定一个起始端口号，比如 1000，然后呢，我们从1000号端口往上找一个端口，直到这个端口能够绑定成功，
     * 比如 1000 端口不可用，我们就尝试绑定 1001，然后 1002，依次类推。
     *
     * serverBootstrap.bind(8000);方法是一个异步的方法，调用之后是立即返回的，他的返回值是一个ChannelFuture，
     * 我们可以给这个ChannelFuture添加一个监听器GenericFutureListener，
     * 然后我们在GenericFutureListener的operationComplete方法里面，我们可以监听端口是否绑定成功
     *
     * 我们接下来从 1000 端口号，开始往上找端口号，直到端口绑定成功，
     * 我们要做的就是在 if (future.isSuccess())的else逻辑里面重新绑定一个递增的端口号，
     * 接下来，我们把这段绑定逻辑抽取出一个bind方法
     *
     * @param serverBootstrap
     * @param port
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) {
                if (future.isSuccess()) {
                    System.out.println("端口[" + port + "]绑定成功!");
                } else {
                    System.err.println("端口[" + port + "]绑定失败!");
                    bind(serverBootstrap, port + 1);
                }
            }
        });
    }

}
