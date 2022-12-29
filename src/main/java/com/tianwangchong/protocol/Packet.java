package com.tianwangchong.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 数据包抽象类
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;


    /**
     * 指令
     *
     * @return
     */
    @JSONField(serialize = false)
    public abstract Byte getCommand();
}
