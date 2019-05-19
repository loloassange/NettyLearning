package com.example.netty_learning.chapter19_MessangeSendAndRecive.server;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.codec.PacketCodecHandler;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.codec.Spliter;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.server.handler.AuthHandler;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.server.handler.IMHandler;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.server.handler.LoginRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Date;

public class NettyServer {

    private static final int PORT = 8000;

    /**
     *  优化
     *
     *  一：共享 handler
     *
     *  我们看到，服务端的 pipeline 链里面已经有 12 个 handler，其中，与指令相关的 handler 有 9 个。
     *
     *  Netty 在这里的逻辑是：
     *      每次有新连接到来的时候，都会调用 ChannelInitializer 的 initChannel() 方法，然后这里 9 个指令相关的 handler 都会被 new 一次。
     *
     *  我们可以看到，其实这里的每一个指令 handler，他们内部都是没有成员变量的，也就是说是无状态的，我们完全可以使用单例模式，
     *  即调用 pipeline().addLast() 方法的时候，都直接使用单例，不需要每次都 new，提高效率，也避免了创建很多小的对象。
     *
     *  二：压缩 handler - 合并编解码器
     *
     *  pipeline 中第一个 handler - Spliter，我们是无法动它的，
     *  因为他内部实现是与每个 channel 有关，每个 Spliter 需要维持每个 channel 当前读到的数据，
     *  也就是说他是有状态的。
     *
     *  而 PacketDecoder 与 PacketEncoder 我们是可以继续改造的，
     *  Netty 内部提供了一个类，叫做 MessageToMessageCodec，使用它可以让我们的编解码操作放到一个类里面去实现，
     *  首先我们定义一个 PacketCodecHandler，
     *  接着，PacketDecoder 和 PacketEncoder都可以删掉，我们的 server 端代码就成了如下的样子
     *
     *  可以看到，除了拆包器，所有的 handler 都写成了单例，
     *  当然，如果你的 handler 里有与 channel 相关成员变量，那就不要写成单例的，
     *  不过，其实所有的状态都可以绑定在 channel 的属性上，依然是可以改造成单例模式。
     *
     *  问题：为什么 PacketCodecHandler 这个 handler 可以直接移到前面去，原来的 PacketEncoder 不是在最后吗？
     *
     *  三：缩短事件传播路径
     *
     *  1. 压缩 handler - 合并平行 handler
     *
     *   对我们这个应用程序来说，每次 decode 出来一个指令对象之后，其实只会在一个指令 handler 上进行处理，
     *   因此，我们其实可以把这么多的指令 handler 压缩为一个 handler，
     *
     *   可以看到，现在，我们服务端的代码已经变得很清爽了，所有的平行指令处理 handler，我们都压缩到了一个 IMHandler，
     *   并且 IMHandler 和指令 handler 均为单例模式，在单机十几万甚至几十万的连接情况下，性能能得到一定程度的提升，创建的对象也大大减少了。
     *
     *  当然，如果你对性能要求没这么高，大可不必搞得这么复杂，还是按照我们前面小节的方式来实现即可，
     *  比如，我们的客户端多数情况下是单连接的，其实并不需要搞得如此复杂，还是保持原样即可。
     *
     *  2. 更改事件传播源
     *
     *  另外，关于缩短事件传播路径，除了压缩 handler，还有一个就是，
     *  如果你的 outBound 类型的 handler 较多，在写数据的时候能用 ctx.writeAndFlush() 就用这个方法。
     *
     *  ctx.writeAndFlush() 是从 pipeline 链中的当前节点开始往前找到第一个 outBound 类型的 handler 把对象往前进行传播，
     *  如果这个对象确认不需要经过其他 outBound 类型的 handler 处理，就使用这个方法。
     *
     *  ctx.channel().writeAndFlush() 是从 pipeline 链中的最后一个 outBound 类型的 handler 开始，把对象往前进行传播，
     *  如果你确认当前创建的对象需要经过后面的 outBound 类型的 handler，那么就调用此方法。
     *
     *  由此可见，在我们的应用程序中，当我们没有改造编解码之前，我们必须调用 ctx.channel().writeAndFlush(),
     *  而经过改造之后，我们的编码器（既属于 inBound, 又属于 outBound 类型的 handler）已处于 pipeline 的最前面，
     *  因此，可以大胆使用 ctx.writeAndFlush()。
     *
     *  也可以理解为：
     *  明确知道无需后续OutBound处理(直接编码发出去)就可以直接使用 ctx.writeAndFlush()，反之则要使用 ctx.channel().writeAndFlush()
     *
     *  备注：
     *  在本例中，只有一个OutBound（PacketCodecHandler，原先是PacketEncoder）
     *
     * 四. 减少阻塞主线程的操作
     *
     * 默认情况下，Netty 在启动的时候会开启 2 倍的 cpu 核数个 NIO 线程，
     * 而通常情况下我们单机会有几万或者十几万的连接，因此，一条 NIO 线程会管理着几千或几万个连接，
     *
     * 其中只要有一个 channel 的一个 handler 中的 channelRead0() 方法阻塞了 NIO 线程，
     * 最终都会拖慢绑定在该 NIO 线程上的其他所有的 channel
     *
     * 五.  如何准确统计处理时长
     *
     * writeAndFlush() 这个方法如果在非 NIO 线程（这里，我们其实是在业务线程中调用了该方法）中执行，它是一个异步的操作，
     * 调用之后，其实是会立即返回的，剩下的所有的操作，都是 Netty 内部有一个任务队列异步执行的，
     * 因此，这里的 writeAndFlush() 执行完毕之后，并不能代表相关的逻辑，比如事件传播、编码等逻辑执行完毕，
     * 只是表示 Netty 接收了这个任务，那么如何才能判断 writeAndFlush() 执行完毕呢？我们可以这么做:
     *                      xxx.writeAndFlush().addListener(future -> {
     *                             if (future.isDone()) {
     *                             // 4. balabala 其他的逻辑
     *                            long time =  System.currentTimeMillis() - begin;
     *                      }
     *
     * writeAndFlush() 方法会返回一个 ChannelFuture 对象，我们给这个对象添加一个监听器，
     * 然后在回调方法里面，我们可以监听这个方法执行的结果，进而再执行其他逻辑，
     * 最后统计耗时，这样统计出来的耗时才是最准确的。
     *
     * 最后，需要提出的一点就是，Netty 里面很多方法都是异步的操作，
     * 在业务线程中如果要统计这部分操作的时间，都需要使用监听器回调的方式来统计耗时，
     * 如果在 NIO 线程中调用，就不需要这么干。
     *
     *
     * 总结：
     *
     * 1. 所有指令都实现完之后，我们发现我们的 handler 已经非常臃肿庞大了，
     *     接下来，我们通过单例模式改造、编解码器合并、平行指令 handler 合并、慎重选择两种类型的 writeAndFlush() 的方式来压缩优化。
     *
     * 2. 在 handler 的处理中，如果有耗时的操作，我们需要把这些操作都丢到我们自定义的的业务线程池中处理，
     *     因为 NIO 线程是会有很多 channel 共享的，我们不能阻塞他。
     *
     * 3. 对于统计耗时的场景，如果在自定义业务线程中调用类似 writeAndFlush() 的异步操作，需要通过添加监听器的方式来统计。
     *
     * @param args
     */
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
                        ch.pipeline().addLast(PacketCodecHandler.INSTANCE);
                        ch.pipeline().addLast(LoginRequestHandler.INSTANCE);
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
