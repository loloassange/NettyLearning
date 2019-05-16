package com.example.netty_learning.chapter16_ClientChatInAction.server.handler;

import com.example.netty_learning.chapter16_ClientChatInAction.protocol.request.MessageRequestPacket;
import com.example.netty_learning.chapter16_ClientChatInAction.protocol.response.MessageResponsePacket;
import com.example.netty_learning.chapter16_ClientChatInAction.session.Session;
import com.example.netty_learning.chapter16_ClientChatInAction.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    /**
     * 1. 服务端在收到客户端发来的消息之后，首先拿到当前用户，也就是消息发送方的会话信息。
     *
     * 2. 拿到消息发送方的会话信息之后，构造一个发送给客户端的消息对象 MessageResponsePacket，
     *    填上发送消息方的用户标识、昵称、消息内容。
     *
     * 3. 通过消息接收方的标识拿到对应的 channel。
     *
     * 4. 如果消息接收方当前是登录状态，直接发送，
     *    如果不在线，控制台打印出一条警告消息。
     *
     * 这里，服务端的功能相当于消息转发：
     *      收到一个客户端的消息之后，构建一条发送给另一个客户端的消息，
     *      接着拿到另一个客户端的 channel，然后通过 writeAndFlush() 写出。
     *
     * @param ctx
     * @param messageRequestPacket
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) {

        // 1.拿到消息发送方的会话信息
        Session session = SessionUtil.getSession(ctx.channel());

        // 2.通过消息发送方的会话信息构造要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());

        // 3.拿到消息接收方的 channel
        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());

        // 4.将消息发送给消息接收方
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            System.err.println("[" + messageRequestPacket.getToUserId() + "] 不在线，发送失败!");
        }

    }
}
