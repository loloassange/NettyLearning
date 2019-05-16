package com.example.netty_learning.chapter15_HasLoginCheckInAction.server.handler;

import com.example.netty_learning.chapter15_HasLoginCheckInAction.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 1. AuthHandler 继承自 ChannelInboundHandlerAdapter，
 *    覆盖了 channelRead() 方法，表明他可以处理所有类型的数据
 *
 * 2. 在 channelRead() 方法里面，在决定是否把读到的数据传递到后续指令处理器之前，首先会判断是否登录成功，
 *    如果未登录，直接强制关闭连接（实际生产环境可能逻辑要复杂些，这里我们的重心在于学习 Netty，这里就粗暴些），
 *    否则，就把读到的数据向下传递，传递给后续指令处理器
 *
 * 3.
 *
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!LoginUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();
        } else {

            /**
             * 避免资源与性能的浪费 -----> pipeline 的热插拔机制
             *
             * 比如，平均每次用户登录之后发送100次消息，其实剩余的 99 次身份校验逻辑都是没有必要的，
             * 因为只要连接未断开，客户端只要成功登录过，后续就不需要再进行客户端的身份校验。
             *
             * 判断如果已经经过权限认证，那么就直接调用 pipeline 的 remove() 方法删除自身，
             * 这里的 this 指的其实就是 AuthHandler 这个对象，删除之后，这条客户端连接的逻辑链中就不再有这段逻辑了。
             */
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    /**
     * 覆盖 handlerRemoved() 方法
     *
     * @param ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if (LoginUtil.hasLogin(ctx.channel())) {
            System.out.println("当前连接登录验证完毕，无需再次验证, AuthHandler 被移除");
        } else {
            System.out.println("无登录验证，强制关闭连接!");
        }
    }

}
