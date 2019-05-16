package com.example.netty_learning.chapter17_GroupChatInAction_Part1.server.handler;

import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.request.LogoutRequestPacket;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.response.LogoutResponsePacket;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 登出请求
 */
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket msg) {
        SessionUtil.unBindSession(ctx.channel());
        LogoutResponsePacket logoutResponsePacket = new LogoutResponsePacket();
        logoutResponsePacket.setSuccess(true);
        ctx.channel().writeAndFlush(logoutResponsePacket);
    }
}
