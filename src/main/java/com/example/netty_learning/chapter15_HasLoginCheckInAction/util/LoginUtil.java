package com.example.netty_learning.chapter15_HasLoginCheckInAction.util;

import com.example.netty_learning.chapter15_HasLoginCheckInAction.Constent;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * 是否登陆的核心逻辑，chapter_15才介绍到的逻辑
 *
 */
public class LoginUtil {

    public static void markAsLogin(Channel channel) {

        // 在登录成功之后，我们通过给 channel 打上属性标记的方式，标记这个 channel 已成功登录
        channel.attr(Constent.LOGIN).set(true);
    }

    public static boolean hasLogin(Channel channel) {

        Attribute<Boolean> loginAttr = channel.attr(Constent.LOGIN);

        return loginAttr.get() != null;

    }

}
