package com.example.netty_learning.chapter19_MessangeSendAndRecive.server.handler;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.request.LogoutRequestPacket;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.response.LogoutResponsePacket;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 登出请求
 */
@ChannelHandler.Sharable
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {

    public static final LogoutRequestHandler INSTANCE = new LogoutRequestHandler();

    private LogoutRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket msg) {
        SessionUtil.unBindSession(ctx.channel());
        LogoutResponsePacket logoutResponsePacket = new LogoutResponsePacket();
        logoutResponsePacket.setSuccess(true);
        ctx.writeAndFlush(logoutResponsePacket);
    }
}
