package com.example.netty_learning.chapter16_ClientChatInAction.client.handler;

import com.example.netty_learning.chapter16_ClientChatInAction.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    /**
     * 客户端收到消息之后，只是把当前消息打印出来，
     * 这里把发送方的用户标识打印出来是为了方便我们在控制台回消息的时候，可以直接复制
     *
     * @param ctx
     * @param messageResponsePacket
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) {

        String fromUserId = messageResponsePacket.getFromUserId();
        String fromUserName = messageResponsePacket.getFromUserName();

        System.out.println(
                fromUserId + ":" +
                fromUserName + " -> " +
                messageResponsePacket.getMessage()
        );

    }

}
