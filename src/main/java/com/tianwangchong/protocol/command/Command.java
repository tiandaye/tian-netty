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

    // 登出请求
    Byte LOGOUT_REQUEST = 5;

    // 登出响应
    Byte LOGOUT_RESPONSE = 6;

    // 创建群组请求
    Byte CREATE_GROUP_REQUEST = 7;

    // 创建群组响应
    Byte CREATE_GROUP_RESPONSE = 8;

    // 群组列表请求
    Byte LIST_GROUP_MEMBERS_REQUEST = 9;

    // 群组列表响应
    Byte LIST_GROUP_MEMBERS_RESPONSE = 10;

    // 加入群组请求
    Byte JOIN_GROUP_REQUEST = 11;

    // 加入群组响应
    Byte JOIN_GROUP_RESPONSE = 12;

    // 退出群组请求
    Byte QUIT_GROUP_REQUEST = 13;

    // 退出群组响应
    Byte QUIT_GROUP_RESPONSE = 14;

    // 群聊请求
    Byte GROUP_MESSAGE_REQUEST = 15;

    // 群聊响应
    Byte GROUP_MESSAGE_RESPONSE = 16;
}
