package com.tianwangchong.protocol.command;

/**
 * 命令常量
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public interface Command {

    // 定义登录请求常量
    Byte LOGIN_REQUEST = 1;

    // 定义登录响应常量
    Byte LOGIN_RESPONSE = 2;

    // 客户端发送至服务端的消息对象
    Byte MESSAGE_REQUEST = 3;

    // 服务端发送至客户端的消息对象
    Byte MESSAGE_RESPONSE = 4;
}
