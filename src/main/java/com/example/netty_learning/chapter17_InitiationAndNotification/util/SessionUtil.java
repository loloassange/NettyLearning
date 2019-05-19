package com.example.netty_learning.chapter17_InitiationAndNotification.util;

import com.example.netty_learning.chapter17_InitiationAndNotification.Constent;
import com.example.netty_learning.chapter17_InitiationAndNotification.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {

    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();
    private static final Map<String, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {

        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Constent.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {

        if (hasLogin(channel)) {
            Session session = getSession(channel);
            userIdChannelMap.remove(session.getUserId());
            channel.attr(Constent.SESSION).set(null);
            System.out.println(session + " 退出登录!");
        }
    }

    public static boolean hasLogin(Channel channel) {

        return getSession(channel) != null;
    }

    public static Session getSession(Channel channel) {

        return channel.attr(Constent.SESSION).get();
    }

    public static Channel getChannel(String userId) {

        return userIdChannelMap.get(userId);
    }

}