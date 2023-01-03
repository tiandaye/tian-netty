package com.tianwangchong.clinet.handler;

import com.tianwangchong.protocol.response.LoginResponsePacket;
import com.tianwangchong.session.Session;
import com.tianwangchong.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 登录响应
 * <p>
 * Copyright (c) 2022, Bongmi
 * All rights reserved
 * Author: tianwangchong@bongmi.com
 */

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    // @Override
    // public void channelActive(ChannelHandlerContext ctx) {
    //     // 创建登录对象
    //     LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
    //     loginRequestPacket.setUserId(UUID.randomUUID().toString());
    //     loginRequestPacket.setUsername("flash");
    //     loginRequestPacket.setPassword("pwd");
    //
    //     // 写数据
    //     ctx.channel().writeAndFlush(loginRequestPacket);
    // }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        String userId = loginResponsePacket.getUserId();
        String userName = loginResponsePacket.getUserName();
        if (loginResponsePacket.isSuccess()) {
            System.out.println("[" + userName + "]登录成功，userId 为: " + loginResponsePacket.getUserId());
            // 这行是不是没什么用???
            SessionUtil.bindSession(new Session(userId, userName), ctx.channel());
        } else {
            System.out.println("[" + userName + "]登录失败，原因：" + loginResponsePacket.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端连接被关闭!");
        // SessionUtil.unBindSession(ctx.channel());
    }
}