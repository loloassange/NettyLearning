package com.example.netty_learning.chapter16_ClientChatInAction.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;


    @JSONField(serialize = false)
    public abstract Byte getCommand();

}
