package com.example.netty_learning.chapter19_MessangeSendAndRecive.server.handler;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.response.GroupMessageResponsePacket;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.request.GroupMessageRequestPacket;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket requestPacket) {


        // 1.拿到 groupId 构造群聊消息的响应
        String groupId = requestPacket.getToGroupId();
        GroupMessageResponsePacket responsePacket = new GroupMessageResponsePacket();
        responsePacket.setFromGroupId(groupId);
        responsePacket.setMessage(requestPacket.getMessage());
        responsePacket.setFromUser(SessionUtil.getSession(ctx.channel()));


        // 2. 拿到群聊对应的 channelGroup，写到每个客户端
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        channelGroup.writeAndFlush(responsePacket);
    }

}
