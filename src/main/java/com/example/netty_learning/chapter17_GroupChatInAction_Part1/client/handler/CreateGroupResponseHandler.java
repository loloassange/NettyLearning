package com.example.netty_learning.chapter17_GroupChatInAction_Part1.client.handler;

import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.response.CreateGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket createGroupResponsePacket) {

        /**
         * 把创建群聊成功之后的具体信息打印出来。
         * 在实际生产环境中，CreateGroupResponsePacket 对象里面可能有更多的信息，然后以上逻辑的处理也会更加复杂。
         */
        System.out.print("群创建成功，id 为[" + createGroupResponsePacket.getGroupId() + "], ");
        System.out.println("群里面有：" + createGroupResponsePacket.getUserNameList());
    }

}
