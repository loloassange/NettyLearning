package com.example.netty_learning.chapter17_GroupChatInAction_Part1.client.handler;

import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.response.LogoutResponsePacket;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
