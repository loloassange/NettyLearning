package com.example.netty_learning.chapter9_LoginInAction.client;

import com.example.netty_learning.chapter9_LoginInAction.protocol.LoginRequestPacket;
import com.example.netty_learning.chapter9_LoginInAction.protocol.LoginResponsePacket;
import com.example.netty_learning.chapter9_LoginInAction.protocol.Packet;
import com.example.netty_learning.chapter9_LoginInAction.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 客户端处理登录请求
     *
     * @param channelHandlerContext
     */
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext){

        System.out.println(new Date() + ":客户端开始登陆");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("userA");
        loginRequestPacket.setPassword("pwd");

        // 编码
        ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(channelHandlerContext.alloc(),loginRequestPacket);

        // 写数据
        channelHandlerContext.channel().writeAndFlush(byteBuf);
    }

    /**
     * 处理服务端的响应
     *
     * @param channelHandlerContext
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext,Object msg){
        ByteBuf byteBuf = (ByteBuf)msg;

        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

        if(packet instanceof LoginResponsePacket){
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket)packet;

            if (loginResponsePacket.isSuccess()) {
                System.out.println(new Date() + ": 客户端登录成功");
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
            }
        }
    }

}
