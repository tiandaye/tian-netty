package com.tianwangchong.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.tianwangchong.serialize.Serializer;
import com.tianwangchong.serialize.SerializerAlgorithm;

/**
 * json序列化实现类
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class JSONSerializer implements Serializer {

    /**
     * 序列化算法
     *
     * @return
     */
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    /**
     * 序列化(java 对象转换成二进制)
     *
     * @param object
     * @return
     */
    @Override
    public byte[] serialize(Object object) {

        return JSON.toJSONBytes(object);
    }

    /**
     * 反序列化(二进制转换成 java 对象)
     *
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {

        return JSON.parseObject(bytes, clazz);
    }
}
