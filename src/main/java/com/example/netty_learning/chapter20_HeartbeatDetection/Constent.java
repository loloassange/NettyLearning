package com.example.netty_learning.chapter20_HeartbeatDetection;

import com.example.netty_learning.chapter20_HeartbeatDetection.session.Session;
import io.netty.util.AttributeKey;

public class Constent {

    /**
     * Command (指令)
     */
    public static final Byte LOGIN_REQUEST = 1;
    public static final Byte LOGIN_RESPONSE = 2;
    public static final Byte MESSAGE_REQUEST = 3;
    public static final Byte MESSAGE_RESPONSE = 4;
    public static final Byte LOGOUT_REQUEST = 5;
    public static final Byte LOGOUT_RESPONSE = 6;
    public static final Byte CREATE_GROUP_REQUEST = 7;
    public static final Byte CREATE_GROUP_RESPONSE = 8;
    public static final Byte LIST_GROUP_MEMBERS_REQUEST = 9;
    public static final Byte LIST_GROUP_MEMBERS_RESPONSE = 10;
    public static final Byte JOIN_GROUP_REQUEST = 11;
    public static final Byte JOIN_GROUP_RESPONSE = 12;
    public static final Byte QUIT_GROUP_REQUEST = 13;
    public static final Byte QUIT_GROUP_RESPONSE = 14;
    public static final Byte GROUP_MESSAGE_REQUEST = 15;
    public static final Byte GROUP_MESSAGE_RESPONSE = 16;
    public static final Byte HEARTBEAT_REQUEST = 17;
    public static final Byte HEARTBEAT_RESPONSE = 18;
    /**
     * json 序列化
     */
    public static final Byte JSON = 1;

    /**
     * 设置 attr 的 key
     */
    public static final AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
    public static final AttributeKey<Session> SESSION = AttributeKey.newInstance("session");

}
