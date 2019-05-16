package com.example.netty_learning.chapter16_ClientChatInAction.server.handler;

import com.example.netty_learning.chapter16_ClientChatInAction.protocol.request.LoginRequestPacket;
import com.example.netty_learning.chapter16_ClientChatInAction.protocol.response.LoginResponsePacket;
import com.example.netty_learning.chapter16_ClientChatInAction.session.Session;
import com.example.netty_learning.chapter16_ClientChatInAction.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) {

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUserName(loginRequestPacket.getUserName());

        if (valid(loginRequestPacket)) {

            loginResponsePacket.setSuccess(true);

            // 简单的生成UUID
            String userId = randomUserId();
            loginResponsePacket.setUserId(userId);

            System.out.println("[" + loginRequestPacket.getUserName() + "]登录成功");

            // 保存用户的会话信息
            SessionUtil.bindSession(new Session(userId, loginRequestPacket.getUserName()), ctx.channel());

        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }

        // 登录响应
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
