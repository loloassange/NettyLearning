package com.example.netty_learning.chapter10_TransmitMessageInAction.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.example.netty_learning.chapter10_TransmitMessageInAction.Constent;
import com.example.netty_learning.chapter10_TransmitMessageInAction.serialize.Serializer;

public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlogrithm() {
        return Constent.JSON;
    }

    @Override
    public byte[] serialize(Object object) {

        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {

        return JSON.parseObject(bytes, clazz);
    }
}
