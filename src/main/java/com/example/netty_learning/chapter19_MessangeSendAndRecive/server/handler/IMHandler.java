package com.example.netty_learning.chapter19_MessangeSendAndRecive.server.handler;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.Constent;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * 1. 首先，IMHandler 是无状态的，依然是可以写成一个单例模式的类。
 *
 * 2. 我们定义一个 map，存放指令到各个指令处理器的映射。
 *
 * 3. 每次回调到 IMHandler 的 channelRead0() 方法的时候，我们通过指令找到具体的 handler，
 *     然后调用指令 handler 的 channelRead，他内部会做指令类型转换，
 *     最终调用到每个指令 handler 的 channelRead0() 方法。
 *
 */
@ChannelHandler.Sharable
public class IMHandler extends SimpleChannelInboundHandler<Packet> {

    public static final IMHandler INSTANCE = new IMHandler();

    private Map<Byte, SimpleChannelInboundHandler<? extends Packet>> handlerMap;

    private IMHandler() {
        handlerMap = new HashMap<>();

        handlerMap.put(Constent.MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);
        handlerMap.put(Constent.CREATE_GROUP_REQUEST, CreateGroupRequestHandler.INSTANCE);
        handlerMap.put(Constent.JOIN_GROUP_REQUEST, JoinGroupRequestHandler.INSTANCE);
        handlerMap.put(Constent.QUIT_GROUP_REQUEST, QuitGroupRequestHandler.INSTANCE);
        handlerMap.put(Constent.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestHandler.INSTANCE);
        handlerMap.put(Constent.GROUP_MESSAGE_REQUEST, GroupMessageRequestHandler.INSTANCE);
        handlerMap.put(Constent.LOGOUT_REQUEST, LogoutRequestHandler.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        handlerMap.get(packet.getCommand()).channelRead(ctx, packet);
    }

}
