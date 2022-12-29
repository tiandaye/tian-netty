package com.tianwangchong.attribute;

import io.netty.util.AttributeKey;

/**
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public interface Attributes {

    // 是否登录成功的标志位
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
