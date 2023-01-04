package com.tianwangchong.server.handler;

import com.tianwangchong.protocol.request.MessageRequestPacket;
import com.tianwangchong.protocol.response.MessageResponsePacket;
import com.tianwangchong.session.Session;
import com.tianwangchong.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 消息请求处理器
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

// 单例
@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    private MessageRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) {
        // 1. 拿到消息发送方的会话信息
        Session session = SessionUtil.getSession(ctx.channel());

        // 2. 通过消息发送方的会话信息构造要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());

        // 3. 拿到消息接收方的 channel
        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());

        // 4. 将消息发送给消息接收方
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            System.err.println("[" + messageRequestPacket.getToUserId() + "] 不在线，发送失败!");
        }

        /**
         * 老代码
         */
        // MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        // System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());
        // messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
        //
        // ctx.channel().writeAndFlush(messageResponsePacket);
    }
}
