package com.example.netty_learning.chapter4_NettyStart;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * chapter4_NettyStart 总结：
 * 1. Netty 服务端启动的流程，一句话来说就是：
 *      创建一个引导类，然后给他指定线程模型，IO模型，连接读写处理逻辑，绑定端口之后，服务端就启动起来了。
 *
 * 2. bind 方法是异步的，我们可以通过这个异步机制来实现端口递增绑定。
 *
 * 3. Netty 服务端启动额外的参数，
 *      主要包括给服务端 Channel 或者客户端 Channel 设置属性值，设置底层 TCP 参数。
 */

public class NettyServerStart {

    /**
     * 1. 首先看到，我们创建了两个NioEventLoopGroup，这两个对象可以看做是传统IO编程模型的两大线程组，
     *    bossGroup表示监听端口，accept 新连接的线程组，
     *    workerGroup表示处理每一条连接的数据读写的线程组。
     *
     *    用生活中的例子来讲就是，一个工厂要运作，必然要有一个老板负责从外面接活，
     *    然后有很多员工，负责具体干活，老板就是bossGroup，员工们就是workerGroup，bossGroup接收完连接，扔给workerGroup去处理。
     *
     * 2. 接下来 我们创建了一个引导类 ServerBootstrap，这个类将引导我们进行服务端的启动工作，直接new出来开搞。
     *
     * 3. 我们通过.group(bossGroup, workerGroup)给引导类配置两大线程组，这个引导类的线程模型也就定型了。
     *
     * 4. 然后，我们指定我们服务端的 IO 模型为NIO，我们通过.channel(NioServerSocketChannel.class)来指定 IO 模型，
     *    当然，这里也有其他的选择，
     *    如果你想指定 IO 模型为 BIO，那么这里配置上OioServerSocketChannel.class类型即可，
     *    当然通常我们也不会这么做，因为Netty的优势就在于NIO。
     *
     * 5. 接着，我们调用childHandler()方法，给这个引导类创建一个ChannelInitializer，
     *    这里主要就是定义后续每条连接的数据读写，业务处理逻辑，不理解没关系，在后面我们会详细分析。
     *    ChannelInitializer这个类中，我们注意到有一个泛型参数NioSocketChannel，这个类呢，就是 Netty 对 NIO 类型的连接的抽象，
     *    而我们前面NioServerSocketChannel也是对 NIO 类型的连接的抽象，
     *    NioServerSocketChannel和NioSocketChannel的概念可以和 BIO 编程模型中的ServerSocket以及Socket两个概念对应上
     *
     * 我们的最小化参数配置到这里就完成了，我们总结一下就是，
     * 要启动一个Netty服务端，必须要指定三类属性，分别是
     * （1）线程模型、
     * （2）IO 模型、
     * （3）连接读写处理逻辑(业务逻辑)，
     * 有了这三者，之后在调用bind(8000)，我们就可以在本地绑定一个 8000 端口启动起来。
     *
     * @param args
     */
    public static void main(String[] args) {

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

        serverBootstrap.bind(8000);

    }

}
