package com.example.netty_learning.chapter10_TransmitMessageInAction.util;

import com.example.netty_learning.chapter10_TransmitMessageInAction.Constent;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

public class LoginUtil {

    public static void markAsLogin(Channel channel) {
        channel.attr(Constent.LOGIN).set(true);
    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Constent.LOGIN);

        return loginAttr.get() != null;
    }
}
