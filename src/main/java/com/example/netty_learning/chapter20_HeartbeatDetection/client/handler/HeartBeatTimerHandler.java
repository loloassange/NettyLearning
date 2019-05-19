package com.example.netty_learning.chapter20_HeartbeatDetection.client.handler;

import com.example.netty_learning.chapter20_HeartbeatDetection.protocol.request.HeartBeatRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * 服务端的空闲检测时间完毕之后，接下来我们再思考一下，在一段时间之内没有读到客户端的数据，是否一定能判断连接假死呢？
 * 并不能，
 * 如果在这段时间之内客户端确实是没有发送数据过来，但是连接是 ok 的，那么这个时候服务端也是不能关闭这条连接的，
 * 为了防止服务端误判，我们还需要在客户端做点什么。
 *
 * ctx.executor() 返回的是当前的 channel 绑定的 NIO 线程，
 * 然后，NIO 线程有一个方法，schedule()，类似 jdk 的延时任务机制，可以隔一段时间之后执行一个任务，
 * 而我们这边是实现了每隔 5 秒，向服务端发送一个心跳数据包，这个时间段通常要比服务端的空闲检测时间的一半要短一些，
 * 我们这里直接定义为空闲检测时间的三分之一，主要是为了排除公网偶发的秒级抖动。
 *
 * 实际在生产环境中，我们的发送心跳间隔时间和空闲检测时间可以略长一些，可以设置为几分钟级别，具体应用可以具体对待，没有强制的规定。
 *
 * 我们上面其实解决了服务端的空闲检测问题，服务端这个时候是能够在一定时间段之内关掉假死的连接，释放连接的资源了，
 * 但是对于客户端来说，我们也需要检测到假死的连接。需要服务端回复心跳
 */
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {

    private static final int HEARTBEAT_INTERVAL = 5;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        scheduleSendHeartBeat(ctx);

        super.channelActive(ctx);
    }

    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {

        ctx.executor().schedule(() -> {

            if (ctx.channel().isActive()) {
                ctx.writeAndFlush(new HeartBeatRequestPacket());
                scheduleSendHeartBeat(ctx);
            }

        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

}
