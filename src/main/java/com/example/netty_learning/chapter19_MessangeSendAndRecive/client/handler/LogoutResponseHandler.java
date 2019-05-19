package com.example.netty_learning.chapter19_MessangeSendAndRecive.client.handler;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.response.LogoutResponsePacket;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
