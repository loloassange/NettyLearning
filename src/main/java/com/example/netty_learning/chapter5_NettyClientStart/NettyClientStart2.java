package com.example.netty_learning.chapter5_NettyClientStart;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.util.AttributeKey;

/**
 * 客户端启动其他方法
 */
public class NettyClientStart2 {

    public static void main(String[] args) {

        Bootstrap bootstrap = new Bootstrap();

        /**
         * attr() 方法可以给客户端 Channel，也就是NioSocketChannel绑定自定义属性，
         * 然后我们可以通过channel.attr()取出这个属性，
         * 比如，上面的代码我们指定我们客户端 Channel 的一个clientName属性，属性值为nettyClient，
         * 其实说白了就是给NioSocketChannel维护一个 map 而已，
         * 后续在这个 NioSocketChannel 通过参数传来传去的时候，就可以通过他来取出设置的属性，非常方便。
         */
        bootstrap.attr(AttributeKey.newInstance("clientName"), "nettyClient");

        /**
         * option() 方法可以给连接设置一些 TCP 底层相关的属性，比如下面，我们设置了三种 TCP 属性，其中
         *
         * 1. ChannelOption.CONNECT_TIMEOUT_MILLIS
         *    表示连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
         *
         * 2. ChannelOption.SO_KEEPALIVE
         *    表示是否开启 TCP 底层心跳机制，true 为开启
         * 3. ChannelOption.TCP_NODELAY
         *    表示是否开始 Nagle 算法，true 表示关闭，false 表示开启，
         *    通俗地说，如果要求高实时性，有数据发送时就马上发送，就设置为 true 关闭，
         *             如果需要减少发送次数减少网络交互，就设置为 false 开启
         */
        bootstrap
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);

    }

}
