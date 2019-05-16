package com.example.netty_learning.chapter16_ClientChatInAction.util;

import com.example.netty_learning.chapter16_ClientChatInAction.Constent;
import com.example.netty_learning.chapter16_ClientChatInAction.session.Session;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户会话信息的保存的逻辑:
 *
 * 1. SessionUtil 里面维持了一个 useId -> channel 的映射 map，
 *    调用 bindSession() 方法的时候，在 map 里面保存这个映射关系，
 *
 *    SessionUtil 还提供了 getChannel() 方法，这样就可以通过 userId 拿到对应的 channel。
 *
 * 2. 除了在 map 里面维持映射关系之外，在 bindSession() 方法中，我们还给 channel 附上了一个属性，
 *    这个属性就是当前用户的 Session，
 *
 *    我们也提供了 getSession() 方法，非常方便地拿到对应 channel 的会话信息。
 *
 * 3. 这里的 SessionUtil 其实就是前面小节的 LoginUtil，这里重构了一下，
 *    其中 hasLogin() 方法，只需要判断当前是否有用户的会话信息即可。
 *
 * 4. 在 LoginRequestHandler 中，我们还重写了 channelInactive() 方法，
 *    用户下线之后，我们需要在内存里面自动删除 userId 到 channel 的映射关系，
 *    这是通过调用 SessionUtil.unBindSession() 来实现的。
 *
 * 总结一点就是：
 *              登录的时候保存会话信息，登出的时候删除会话信息
 *
 */
public class SessionUtil {

    // userId -> channel 的映射
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {

        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Constent.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {

        if (hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Constent.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {

        return channel.hasAttr(Constent.SESSION);
    }

    public static Session getSession(Channel channel) {

        return channel.attr(Constent.SESSION).get();
    }

    public static Channel getChannel(String userId) {

        return userIdChannelMap.get(userId);
    }

}
