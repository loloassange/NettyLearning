package com.example.netty_learning.chapter17_GroupChatInAction_Part1.util;

import java.util.UUID;

public class IDUtil {

    public static String randomId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

}
