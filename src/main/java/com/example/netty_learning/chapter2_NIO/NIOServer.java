package com.example.netty_learning.chapter2_NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * chapter2_NIO 编程模型中，新来一个连接不再创建一个新的线程，而是可以把这条连接直接绑定到某个固定的线程，然后这条连接所有的读写都由这个线程来负责
 *
 * 1. chapter2_NIO 模型解决线程资源受限的方案 -----》selector
 *
 * 实际开发过程中，开多个线程，每个线程都管理着一批连接，相对于 IO 模型中一个线程管理一条连接，消耗的线程资源大幅减少。
 *
 * chapter2_NIO 模型中 selector 的作用:
 * 一条连接来了之后，现在不创建一个 while 死循环去监听是否有数据可读了，而是直接把这条连接注册到 selector 上，
 * 然后，通过检查这个 selector，就可以批量监测出有数据可读的连接，进而读取数据。
 *
 * 2. 线程切换效率低下
 *
 * 由于 chapter2_NIO 模型中线程数量大大降低，线程切换效率因此也大幅度提高
 *
 * 3.IO读写面向流
 *
 * IO 读写是面向流的，一次性只能从流中读取一个或者多个字节，并且读完之后流无法再读取，你需要自己缓存数据。
 * 而 chapter2_NIO 的读写是面向 Buffer 的，你可以随意读取里面任何一个字节数据，不需要你自己缓存数据，这一切只需要移动读写指针即可。
 *
 */
public class NIOServer {

    /**
     * 1. chapter2_NIO 模型中通常会有两个线程，每个线程绑定一个轮询器 selector ，
     *    在我们这个例子中
     *      serverSelector负责轮询是否有新的连接，
     *      clientSelector负责轮询连接是否有数据可读
     *
     * 2. 服务端监测到新的连接之后，不再创建一个新的线程，
     *    而是直接将新连接绑定到clientSelector上，这样就不用 IO 模型中 1w 个 while 循环在死等，参见(1)
     *
     * 3. clientSelector被一个 while 死循环包裹着，
     *    如果在某一时刻有多条连接有数据可读，那么通过 clientSelector.select(1)方法可以轮询出来，进而批量处理，参见(2)
     *
     * 4. 数据的读写面向 Buffer，参见(3)
     *
     * 其他的细节部分，实在是太复杂，你也不用对代码的细节深究到底。
     * 总之，强烈不建议直接基于JDK原生NIO来进行网络开发，下面是总结的原因：
     *
     * 1. JDK 的 chapter2_NIO 编程需要了解很多的概念，编程复杂，对 chapter2_NIO 入门非常不友好，编程模型不友好，ByteBuffer 的 Api 简直反人类
     *
     * 2. 对 chapter2_NIO 编程来说，一个比较合适的线程模型能充分发挥它的优势，而 JDK 没有给你实现，你需要自己实现，就连简单的自定义协议拆包都要你自己实现
     *
     * 3. JDK 的 chapter2_NIO 底层由 epoll 实现，该实现饱受诟病的空轮询 bug 会导致 cpu 飙升 100%
     *
     * 4. 项目庞大之后，自行实现的 chapter2_NIO 很容易出现各类 bug，维护成本较高，上面这一坨代码我都不能保证没有 bug
     *
     * 可以直接使用IOClient.java与NIOServer.java通信。
     *
     * JDK 的 chapter2_NIO 犹如带刺的玫瑰，虽然美好，让人向往，但是使用不当会让你抓耳挠腮，痛不欲生，正因为如此，Netty 横空出世！
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Selector serverSelector = Selector.open();
        Selector clientSelector = Selector.open();

        new Thread(() -> {
            try {
                // 对应 IO编程 中服务端启动
                ServerSocketChannel listenerChannel = ServerSocketChannel.open();
                listenerChannel.socket().bind(new InetSocketAddress(8000));
                listenerChannel.configureBlocking(false);
                listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

                while (true) {
                    // 监测是否有新的连接，这里的1指的是阻塞的时间为 1ms
                    if (serverSelector.select(1) > 0) {
                        Set<SelectionKey> set = serverSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = set.iterator();

                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();

                            if (key.isAcceptable()) {
                                try {
                                    // (1) 每来一个新连接，不需要创建一个线程，而是直接注册到clientSelector
                                    SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                    clientChannel.configureBlocking(false);
                                    clientChannel.register(clientSelector, SelectionKey.OP_READ);
                                } finally {
                                    keyIterator.remove();
                                }
                            }

                        }
                    }
                }
            } catch (IOException ignored) {
            }

        }).start();


        new Thread(() -> {
            try {
                while (true) {
                    // (2) 批量轮询是否有哪些连接有数据可读，这里的1指的是阻塞的时间为 1ms
                    if (clientSelector.select(1) > 0) {
                        Set<SelectionKey> set = clientSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = set.iterator();

                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();

                            if (key.isReadable()) {
                                try {
                                    SocketChannel clientChannel = (SocketChannel) key.channel();
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    // (3) 面向 Buffer
                                    clientChannel.read(byteBuffer);
                                    byteBuffer.flip();
                                    System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer).toString());
                                } finally {
                                    keyIterator.remove();
                                    key.interestOps(SelectionKey.OP_READ);
                                }
                            }

                        }
                    }
                }
            } catch (IOException ignored) {
            }
        }).start();

    }

}
