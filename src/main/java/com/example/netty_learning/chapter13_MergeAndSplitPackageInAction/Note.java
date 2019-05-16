package com.example.netty_learning.chapter13_MergeAndSplitPackageInAction;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class Note {

    /**
     * 为什么会有粘包半包现象？
     *
     * 我们需要知道，尽管我们在应用层面使用了 Netty，但是对于操作系统来说，只认 TCP 协议，
     * 尽管我们的应用层是按照 ByteBuf 为 单位来发送数据，但是到了底层操作系统仍然是按照字节流发送数据，
     * 因此，数据到了服务端，也是按照字节流的方式读入，然后到了 Netty 应用层面，重新拼装成 ByteBuf，
     * 而这里的 ByteBuf 与客户端按顺序发送的 ByteBuf 可能是不对等的。
     * 因此，我们需要在客户端根据自定义协议来组装我们应用层的数据包，
     * 然后在服务端根据我们的应用层的协议来组装数据包，这个过程通常在服务端称为拆包，而在客户端称为粘包。
     *
     * 拆包和粘包是相对的，一端粘了包，另外一端就需要将粘过的包拆开，
     * 举个栗子，发送端将三个数据包粘成两个 TCP 数据包发送到接收端，接收端就需要根据应用协议将两个数据包重新组装成三个数据包。
     */

    /**
     * 拆包的原理:
     *
     * 在没有 Netty 的情况下，用户如果自己需要拆包，基本原理就是不断从 TCP 缓冲区中读取数据，每次读取完都需要判断是否是一个完整的数据包
     *
     * 1. 如果当前读取的数据不足以拼接成一个完整的业务数据包，那就保留该数据，继续从 TCP 缓冲区中读取，直到得到一个完整的数据包。
     *
     * 2. 如果当前读到的数据加上已经读取的数据足够拼接成一个数据包，那就将已经读取的数据拼接上本次读取的数据，
     *    构成一个完整的业务数据包传递到业务逻辑，多余的数据仍然保留，以便和下次读到的数据尝试拼接。
     *
     * 如果我们自己实现拆包，这个过程将会非常麻烦，我们的每一种自定义协议，都需要自己实现，还需要考虑各种异常，
     * 而 Netty 自带的一些开箱即用的拆包器已经完全满足我们的需求了。
     */

    /**
     * Netty 自带的拆包器
     *
     * 1. 固定长度的拆包器 FixedLengthFrameDecoder
     *
     *    如果你的应用层协议非常简单，每个数据包的长度都是固定的，
     *    比如 100，那么只需要把这个拆包器加到 pipeline 中，Netty 会把一个个长度为 100 的数据包 (ByteBuf) 传递到下一个 channelHandler。
     *
     * 2. 行拆包器 LineBasedFrameDecoder
     *
     *    从字面意思来看，发送端发送数据包的时候，每个数据包之间以换行符作为分隔，
     *    接收端通过 LineBasedFrameDecoder 将粘过的 ByteBuf 拆分成一个个完整的应用层数据包。
     *
     * 3. 分隔符拆包器 DelimiterBasedFrameDecoder
     *
     *    DelimiterBasedFrameDecoder 是行拆包器的通用版本，只不过我们可以自定义分隔符。
     *
     * 4. 基于长度域拆包器 LengthFieldBasedFrameDecoder
     *
     *    最后一种拆包器是最通用的一种拆包器，只要你的自定义协议中包含长度域字段，均可以使用这个拆包器来实现应用层拆包。
     *
     */

    /**
     * 关于拆包，我们只需要关注
     *
     * 1. 在我们的自定义协议中，我们的长度域在整个数据包的哪个地方，专业术语来说就是长度域相对整个数据包的偏移量是多少。
     * 2. 另外需要关注的就是，我们长度域的长度是多少。
     *
     * 有了长度域偏移量和长度域的长度，我们就可以构造一个拆包器。
     *
     *
     * new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4);
     *
     * 其中，第一个参数指的是数据包的最大长度，第二个参数指的是长度域的偏移量，第三个参数指的是长度域的长度，
     * 这样一个拆包器写好之后，只需要在 pipeline 的最前面加上这个拆包器。
     *
     *
     * 服务端
     *
     * ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
     * ch.pipeline().addLast(new PacketDecoder());
     * ch.pipeline().addLast(new LoginRequestHandler());
     * ch.pipeline().addLast(new MessageRequestHandler());
     * ch.pipeline().addLast(new PacketEncoder());
     *
     *
     * 客户端
     *
     * ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
     * ch.pipeline().addLast(new PacketDecoder());
     * ch.pipeline().addLast(new LoginResponseHandler());
     * ch.pipeline().addLast(new MessageResponseHandler());
     * ch.pipeline().addLast(new PacketEncoder());
     *
     * 这样，在后续 PacketDecoder 进行 decode 操作的时候，ByteBuf 就是一个完整的自定义协议数据包。
     */

    /**
     * 拒绝非本协议连接:
     *
     * 我们在设计协议的时候为什么在数据包的开头加上一个魔数?
     *
     * 遗忘的同学可以参考客户端与服务端通信协议编解码回顾一下。
     * 我们设计魔数的原因是为了尽早屏蔽非本协议的客户端，通常在第一个 handler 处理这段逻辑。
     * 我们接下来的做法是每个客户端发过来的数据包都做一次快速判断，判断当前发来的数据包是否是满足我的自定义协议，
     * 我们只需要继承自 LengthFieldBasedFrameDecoder 的 decode() 方法，然后在 decode 之前判断前四个字节是否是等于我们定义的魔数 0x12345678
     */

    public class Spliter extends LengthFieldBasedFrameDecoder {
        private static final int LENGTH_FIELD_OFFSET = 7;
        private static final int LENGTH_FIELD_LENGTH = 4;

        private static final int PacketCodeC_MAGIC_NUMBER = 0x12345678;

        public Spliter() {
            super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
            // 屏蔽非本协议的客户端
            if (in.getInt(in.readerIndex()) != PacketCodeC_MAGIC_NUMBER) {
                ctx.channel().close();
                return null;
            }

            return super.decode(ctx, in);
        }
    }

    /**
     * 为什么可以在 decode() 方法写这段逻辑？
     * 是因为这里的 decode() 方法中，第二个参数 in，每次传递进来的时候，均为一个数据包的开头。
     *
     * 最后，我们只需要替换一下如下代码即可
     *
     * //ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
     * // 替换为
     * ch.pipeline().addLast(new Spliter());
     */

    /**
     * 总结:
     *
     * 1. 为什么要有拆包器?
     *
     *   说白了，拆包器的作用就是根据我们的自定义协议，
     *   把数据拼装成一个个符合我们自定义数据包大小的 ByteBuf，然后送到我们的自定义协议解码器去解码。
     *
     * 2. Netty 自带的拆包器包括基于固定长度的拆包器，基于换行符和自定义分隔符的拆包器，还有另外一种最重要的基于长度域的拆包器。
     *    通常 Netty 自带的拆包器已完全满足我们的需求，无需重复造轮子。
     *
     * 3. 基于 Netty 自带的拆包器，我们可以在拆包之前判断当前连上来的客户端是否是支持自定义协议的客户端，如果不支持，可尽早关闭，节省资源。
     */

}
