package com.tianwangchong.clinet.handler;

import com.tianwangchong.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 消息响应
 *
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) {
        // System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());

        String fromUserId = messageResponsePacket.getFromUserId();
        String fromUserName = messageResponsePacket.getFromUserName();
        System.out.println(fromUserId + ":" + fromUserName + " ->给你发送了消息: " + messageResponsePacket
                .getMessage());
    }
}