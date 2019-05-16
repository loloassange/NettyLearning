package com.example.netty_learning.chapter15_HasLoginCheckInAction.client.handler;

import com.example.netty_learning.chapter15_HasLoginCheckInAction.protocol.request.LoginRequestPacket;
import com.example.netty_learning.chapter15_HasLoginCheckInAction.protocol.response.LoginResponsePacket;
import com.example.netty_learning.chapter15_HasLoginCheckInAction.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    /**
     * channelActive -----> 里面是客户端发送登陆请求的逻辑
     *               -----> 当 channel 的所有的业务逻辑链准备完毕（也就是说 channel 的 pipeline 中已经添加完所有的 handler）以及绑定好一个 NIO 线程之后，
     *                      这条连接算是真正激活了，接下来就会回调到此方法。
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flash");
        loginRequestPacket.setPassword("pwd");

        /**
         * 注释掉 登录的逻辑 是为了 实现 无身份认证的演示
         *
         * 客户端如果第一个指令为非登录指令，AuthHandler 直接将客户端连接关闭
         */
        // 写数据
        //ctx.channel().writeAndFlush(loginRequestPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        if (loginResponsePacket.isSuccess()) {
            System.out.println(new Date() + ": 客户端登录成功");
            LoginUtil.markAsLogin(ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端连接被关闭!");
    }
}
