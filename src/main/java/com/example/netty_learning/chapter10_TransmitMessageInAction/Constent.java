package com.example.netty_learning.chapter10_TransmitMessageInAction;

import io.netty.util.AttributeKey;

public class Constent {

    /**
     * Command (指令)
     */
    public static final Byte LOGIN_REQUEST = 1;
    public static final Byte LOGIN_RESPONSE = 2;
    public static final Byte MESSAGE_REQUEST = 3;
    public static final Byte MESSAGE_RESPONSE = 4;

    /**
     * json 序列化
     */
    public static final Byte JSON = 1;

    public static final AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

}
