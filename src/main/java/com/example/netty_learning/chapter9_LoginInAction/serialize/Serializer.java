package com.example.netty_learning.chapter9_LoginInAction.serialize;


import com.example.netty_learning.chapter9_LoginInAction.serialize.impl.JSONSerializer;

public interface Serializer {

    Serializer DEFAULT = new JSONSerializer();

    /**
     * 获取序列化算法，即使用哪种工具解析
     * @return
     */
    byte getSerializerAlogrithm();

    byte[] serialize(Object object);

    <T> T deserialize(Class<T> clazz,byte[] bytes);

}

