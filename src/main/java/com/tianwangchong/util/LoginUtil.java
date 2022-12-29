package com.tianwangchong.util;

import com.tianwangchong.attribute.Attributes;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class LoginUtil {

    /**
     * 用attr设置登录标志位
     *
     * @param channel
     */
    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    /**
     * 判断是否有标志位
     *
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);

        // 如果有标志位，不管标志位的值是什么，都表示已经成功登录过
        return loginAttr.get() != null;
    }
}
