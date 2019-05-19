package com.example.netty_learning.chapter19_MessangeSendAndRecive.client.handler;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.response.ListGroupMembersResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket responsePacket) {
        System.out.println("群[" + responsePacket.getGroupId() + "]中的人包括：" + responsePacket.getSessionList());
    }
}
