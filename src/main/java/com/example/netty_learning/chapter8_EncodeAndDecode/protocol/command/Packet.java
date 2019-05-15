package com.example.netty_learning.chapter8_EncodeAndDecode.protocol.command;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 1. 这个是  通信过程中 Java 对象的抽象类  ，可以看到：
 *    我们定义了一个版本号（默认值为 1 ）以及一个获取指令的抽象方法，所有的指令数据包都必须实现这个方法，这样我们就可以知道某种指令的含义。
 *
 * 2. @Data 注解由 lombok 提供，它会自动帮我们生产 getter/setter 方法，减少大量重复代码，推荐使用。
 */
@Data
public abstract class Packet {
    /**
     * 协议版本
     *
     * @JSONField(deserialize = false, serialize = false)  忽略不想反序列化的字段，忽略不想序列化的字段
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;

    @JSONField(serialize = false)
    public abstract Byte getCommand();

}
