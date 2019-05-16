package com.example.netty_learning.chapter12_PipelineInAction.server.handler;

import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.request.LoginRequestPacket;
import com.example.netty_learning.chapter12_PipelineInAction.protocol.response.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * SimpleChannelInboundHandler
 *
 *   1. 解决 使用 if else 逻辑来处理业务的 问题
 *
 *   2. SimpleChannelInboundHandler 从字面意思也可以看到，使用它非常简单，我们在继承这个类的时候，给他传递一个泛型参数，
 *      然后在 channelRead0() 方法里面，我们不用再通过 if 逻辑来判断当前对象是否是本 handler 可以处理的对象，也不用强转，不用往下传递本 handler 处理不了的对象，
 *      这一切都已经交给父类 SimpleChannelInboundHandler 来实现了，我们只需要专注于我们要处理的业务逻辑即可。
 *
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) {
        System.out.println(new Date() + ": 收到客户端登录请求……");

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());

        if (valid(loginRequestPacket)) {
            loginResponsePacket.setSuccess(true);
            System.out.println(new Date() + ": 登录成功!");
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
}
