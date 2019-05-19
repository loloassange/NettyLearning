package com.example.netty_learning.chapter19_MessangeSendAndRecive.server.handler;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.request.LoginRequestPacket;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.response.LoginResponsePacket;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.session.Session;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.util.IDUtil;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Date;

/**
 *
 * 如果一个 handler 要被多个 channel 进行共享，必须要加上 @ChannelHandler.Sharable 显示地告诉 Netty，
 * 这个 handler 是支持多个 channel 共享的，否则会报错。
 */
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    protected LoginRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUserName(loginRequestPacket.getUserName());

        if (valid(loginRequestPacket)) {
            loginResponsePacket.setSuccess(true);
            String userId = IDUtil.randomId();
            loginResponsePacket.setUserId(userId);
            System.out.println("[" + loginRequestPacket.getUserName() + "]登录成功");
            SessionUtil.bindSession(new Session(userId, loginRequestPacket.getUserName()), ctx.channel());
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }

        // 登录响应
        ctx.writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
