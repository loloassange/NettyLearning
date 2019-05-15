package com.example.netty_learning.chapter9_LoginInAction.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    @JSONField(deserialize = false,serialize = true)
    private Byte version = 1;

    /**
     * 获取指令
     * @return
     */
    @JSONField(serialize = false)
    public abstract Byte getCommand();

}
