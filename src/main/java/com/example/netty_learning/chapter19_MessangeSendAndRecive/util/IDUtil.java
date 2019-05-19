package com.example.netty_learning.chapter19_MessangeSendAndRecive.util;

import java.util.UUID;

public class IDUtil {
    public static String randomId() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
