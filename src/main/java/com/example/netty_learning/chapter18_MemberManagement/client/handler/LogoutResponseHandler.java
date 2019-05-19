package com.example.netty_learning.chapter18_MemberManagement.client.handler;

import com.example.netty_learning.chapter18_MemberManagement.protocol.response.LogoutResponsePacket;
import com.example.netty_learning.chapter18_MemberManagement.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
