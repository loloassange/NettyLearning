package com.example.netty_learning.chapter10_TransmitMessageInAction.client;

import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.Packet;
import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.PacketCodeC;
import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.request.LoginRequestPacket;
import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.response.LoginResponsePacket;
import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.response.MessageResponsePacket;
import com.example.netty_learning.chapter10_TransmitMessageInAction.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        System.out.println(new Date() + ": 客户端开始登录");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flash");
        loginRequestPacket.setPassword("pwd");

        // 编码
        ByteBuf buffer = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequestPacket);

        // 写数据
        ctx.channel().writeAndFlush(buffer);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

        if (packet instanceof LoginResponsePacket) {

            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            if (loginResponsePacket.isSuccess()) {
                System.out.println(new Date() + ": 客户端登录成功");
                /**
                 * 如何判断客户端是否已经登录？
                 *
                 * 可以给客户端连接，也就是 Channel 绑定属性，通过 channel.attr(xxx).set(xx) 的方式，
                 * 那么我们是否可以在登录成功之后，给 Channel 绑定一个登录成功的标志位，
                 * 然后判断是否登录成功的时候取出这个标志位就可以了
                 */
                LoginUtil.markAsLogin(ctx.channel());
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
            }

        } else if (packet instanceof MessageResponsePacket) {

            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());

        }
    }
}
