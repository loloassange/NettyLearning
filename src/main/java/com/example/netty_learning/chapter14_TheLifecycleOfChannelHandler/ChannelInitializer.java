package com.example.netty_learning.chapter14_TheLifecycleOfChannelHandler;

/**
 * ChannelHandler 生命周期各回调方法用法举例:
 *
 * 一. ChannelInitializer 的实现原理:
 *
 * 仔细翻看一下我们的服务端启动代码，
 * 我们在给新连接定义 handler 的时候，其实只是通过 childHandler() 方法给新连接设置了一个 handler，
 * 这个 handler 就是 ChannelInitializer，
 * 而在 ChannelInitializer 的 initChannel() 方法里面，我们通过拿到 channel 对应的 pipeline，然后往里面塞 handler
 *
 * NettyServer.java
 *
 * .childHandler(new ChannelInitializer<NioSocketChannel>() {
 *     protected void initChannel(NioSocketChannel ch) {
 *         ch.pipeline().addLast(new LifeCyCleTestHandler());
 *         ch.pipeline().addLast(new PacketDecoder());
 *         ch.pipeline().addLast(new LoginRequestHandler());
 *         ch.pipeline().addLast(new MessageRequestHandler());
 *         ch.pipeline().addLast(new PacketEncoder());
 *     }
 * });
 *
 * 这里的 ChannelInitializer 其实就利用了 Netty 的 handler 生命周期中 channelRegistered() 与 handlerAdded() 两个特性
 *
 * 下面是部分源码：
 */
public class ChannelInitializer {
    /**
     *     protected abstract void initChannel(C ch) throws Exception;
     *
     *     public final void channelRegistered(ChannelHandlerContext ctx) throws Exception {
     *         // ...
     *         initChannel(ctx);
     *         // ...
     *     }
     *
     *     public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
     *         // ...
     *         if (ctx.channel().isRegistered()) {
     *             initChannel(ctx);
     *         }
     *         // ...
     *     }
     *
     *     private boolean initChannel(ChannelHandlerContext ctx) throws Exception {
     *         if (initMap.putIfAbsent(ctx, Boolean.TRUE) == null) {
     *             initChannel((C) ctx.channel());
     *             // ...
     *             return true;
     *         }
     *         return false;
     *     }
     *
     */

}

/**
 * 1. ChannelInitializer 定义了一个抽象的方法 initChannel()，
 *    这个抽象方法由我们自行实现，
 *    我们在服务端启动的流程里面的实现逻辑就是往 pipeline 里面塞我们的 handler 链
 *
 * 2. handlerAdded() 和 channelRegistered() 方法，都会尝试去调用 initChannel() 方法，
 *    initChannel() 使用 putIfAbsent() 来防止 initChannel() 被调用多次
 *
 * 3. 如果你 debug 了 ChannelInitializer 的上述两个方法，你会发现，
 *    在 handlerAdded() 方法被调用的时候，channel 其实已经和某个线程绑定上了，
 *    所以，就我们的应用程序来说，这里的 channelRegistered() 其实是多余的，那为什么这里还要尝试调用一次呢？
 *    我猜测应该是担心我们自己写了个类继承自 ChannelInitializer，然后覆盖掉了 handlerAdded() 方法，
 *    这样即使覆盖掉，在 channelRegistered() 方法里面还有机会再调一次 initChannel()，
 *    把我们自定义的 handler 都添加到 pipeline 中去。
 */

/**
 * 二. handlerAdded() 与 handlerRemoved()
 *
 *    这两个方法通常可以用在一些资源的申请和释放
 *
 * 三. channelActive() 与 channelInActive()
 *
 *    对我们的应用程序来说，这两个方法表明的含义是 TCP 连接的建立与释放，
 *    通常我们在这两个回调里面统计单机的连接数，channelActive() 被调用，连接数加一，
 *                                         channelInActive() 被调用，连接数减一
 *
 *    另外，我们也可以在 channelActive() 方法中，实现对客户端连接 ip 黑白名单的过滤，具体这里就不展开了
 *
 * 四. channelRead()
 *
 *    我们在前面小节讲拆包粘包原理，服务端根据自定义协议来进行拆包，
 *    其实就是在这个方法里面，每次读到一定的数据，都会累加到一个容器里面，
 *    然后判断是否能够拆出来一个完整的数据包，
 *    如果够的话就拆了之后，往下进行传递。
 *
 */
